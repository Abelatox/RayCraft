package com.abelatox.raycraft.client.render;

import com.abelatox.raycraft.capabilities.IPlayerCapabilities;
import com.abelatox.raycraft.capabilities.ModCapabilities;
import com.abelatox.raycraft.lib.Reference;
import com.abelatox.raycraft.lib.Strings;
import com.abelatox.raycraft.lib.Utils;
import com.abelatox.raycraft.models.*;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderArmEvent;
import net.neoforged.neoforge.client.event.RenderPlayerEvent;

import java.util.HashMap;
import java.util.Map;

@EventBusSubscriber(modid = Reference.MODID, value = Dist.CLIENT)
public class RaycraftPlayerRenderHandler {

	// submitModelPart doesn't call setupAnim: the first-person arm uses its own model instances
	// so it doesn't inherit the pose of another player rendered in third person.
	private static final Map<String, Model<AvatarRenderState>> WORLD_MODELS = new HashMap<>();
	private static final Map<String, Model<AvatarRenderState>> HAND_MODELS = new HashMap<>();
	private static ModelRayman raymanGoldArms;
	private static ModelBarrel barrelModel;

	private static Model<AvatarRenderState> bakeModel(String playerType) {
		EntityModelSet models = Minecraft.getInstance().getEntityModels();

		return switch (playerType) {
			case Strings.RAYMAN -> new ModelRayman(models.bakeLayer(ModModels.RAYMAN_LAYER));
			case Strings.HOODLUM -> new ModelHoodlum(models.bakeLayer(ModModels.HOODLUM_LAYER));
			case Strings.GLOBOX -> new ModelGlobox(models.bakeLayer(ModModels.GLOBOX_LAYER));
			case Strings.ROBO_PIRATE_RED, Strings.ROBO_PIRATE_GREEN -> new ModelRoboPirate(models.bakeLayer(ModModels.ROBO_PIRATE_LAYER));
			default -> null;
		};
	}

	private static Model<AvatarRenderState> getModel(Map<String, Model<AvatarRenderState>> cache, String playerType) {
		if (!cache.containsKey(playerType)) {
			cache.put(playerType, bakeModel(playerType));
		}
		return cache.get(playerType);
	}

	private static ModelRayman getRaymanGoldArms() {
		if (raymanGoldArms == null) {
			raymanGoldArms = new ModelRayman(Minecraft.getInstance().getEntityModels().bakeLayer(ModModels.RAYMAN_LAYER), true);
		}
		return raymanGoldArms;
	}

	private static ModelBarrel getBarrelModel() {
		if (barrelModel == null) {
			barrelModel = new ModelBarrel(Minecraft.getInstance().getEntityModels().bakeLayer(ModModels.BARREL_LAYER));
		}
		return barrelModel;
	}

	private static Identifier getTexture(String playerType) {
		return Identifier.fromNamespaceAndPath(Reference.MODID, "textures/models/" + playerType + ".png");
	}

	private static boolean hasGoldArms(String playerType, int punchLevel) {
		return Strings.RAYMAN.equals(playerType) && punchLevel == ModelRayman.GOLD_FIST_LEVEL;
	}

	@SubscribeEvent
	public static void onRenderPlayerPre(RenderPlayerEvent.Pre<?> event) {
		AvatarRenderState state = event.getRenderState();
		RaycraftAvatarRenderData data = state.getRenderDataOrDefault(RaycraftRenderData.AVATAR_DATA, RaycraftAvatarRenderData.EMPTY);

		if (data.playerType() == null || data.playerType().isEmpty()) {
			return;
		}

		Model<AvatarRenderState> model = getModel(WORLD_MODELS, data.playerType());
		if (model == null) {
			return;
		}

		event.setCanceled(true);

		Identifier texture = getTexture(data.playerType());

		PoseStack poseStack = event.getPoseStack();
		SubmitNodeCollector collector = event.getSubmitNodeCollector();

		model.setupAnim(state);

		poseStack.pushPose();

		if (state.pose == Pose.SWIMMING) {
			poseStack.translate(0, 1.3, 0);
			poseStack.rotate(Axis.XP.rotationDegrees(90));
		} else if (state.pose == Pose.SLEEPING) {
			poseStack.translate(0, 0, -1.5);
			poseStack.rotate(Axis.XN.rotationDegrees(90));
			poseStack.rotate(Axis.ZP.rotationDegrees(180));
		} else {
			poseStack.rotate(Axis.YN.rotationDegrees(state.bodyRot));
		}

		poseStack.translate(0, 1.5, 0);
		poseStack.rotate(Axis.YP.rotationDegrees(180));
		poseStack.rotate(Axis.ZP.rotationDegrees(180));

		collector.submitModel(model, state, poseStack, model.renderType(texture), data.lightCoords(), OverlayTexture.NO_OVERLAY, 0);

		if (hasGoldArms(data.playerType(), data.punchLevel())) {
			ModelRayman goldArms = getRaymanGoldArms();
			collector.submitModel(goldArms, state, poseStack, goldArms.renderType(texture), data.lightCoords(), OverlayTexture.NO_OVERLAY,
					ModelRayman.GOLD_TINT, null, 0);
		}

		if (model instanceof ModelHoodlum hoodlum && !state.rightHandItemState.isEmpty()) {
			submitHeldItem(hoodlum, state, poseStack, collector, data.lightCoords());
		}

		poseStack.popPose();

		if (data.holdingBarrel()) {
			ModelBarrel barrel = getBarrelModel();
			poseStack.pushPose();
			poseStack.scale(1.3F, 1.3F, 1.3F);
			poseStack.translate(0, 1.28F, 0F);
			poseStack.rotate(Axis.YN.rotationDegrees(state.bodyRot));
			collector.submitModel(barrel, state, poseStack, barrel.renderType(Utils.getBarrelTexture()), data.lightCoords(), OverlayTexture.NO_OVERLAY, 0);
			poseStack.popPose();
		}
	}

	// Like ItemInHandLayer; the -1/16 on X is because Hoodlum's arm is one pixel wider than the vanilla one.
	private static void submitHeldItem(ModelHoodlum model, AvatarRenderState state, PoseStack poseStack, SubmitNodeCollector collector, int lightCoords) {
		poseStack.pushPose();
		model.rightArm.translateAndRotate(poseStack);
		poseStack.translate(-1F / 16F, 0F, 0F);
		poseStack.rotate(Axis.XP.rotationDegrees(-90.0F));
		poseStack.rotate(Axis.YP.rotationDegrees(180.0F));
		poseStack.translate(1F / 16F, 0.125F, -0.625F);
		state.rightHandItemState.submit(poseStack, collector, lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
		poseStack.popPose();
	}

	@SubscribeEvent
	public static void onRenderArm(RenderArmEvent event) {
		AvatarRenderState avatarState = event.getPlayerRenderState().avatarRenderState;
		RaycraftAvatarRenderData data = avatarState != null
				? avatarState.getRenderDataOrDefault(RaycraftRenderData.AVATAR_DATA, RaycraftAvatarRenderData.EMPTY)
				: RaycraftAvatarRenderData.EMPTY;

		String playerType = data.playerType();
		int punchLevel = data.punchLevel();
		if (playerType == null || playerType.isEmpty()) {
			Player player = Minecraft.getInstance().player;
			if (player == null) {
				return;
			}
			IPlayerCapabilities props = ModCapabilities.get(player);
			playerType = props.getPlayerType();
			punchLevel = props.getShotLevel();
		}

		if (playerType.isEmpty()) {
			return;
		}

		Model<AvatarRenderState> model = getModel(HAND_MODELS, playerType);
		if (!(model instanceof RaycraftCharacterModel characterModel)) {
			return;
		}

		event.setCanceled(true);

		ModelPart arm = characterModel.getArm(event.getArm());
		arm.resetPose();
		arm.visible = true;

		PoseStack poseStack = event.getPoseStack();
		poseStack.pushPose();
		// The vanilla transform assumes the shoulder at (+-5, 2, 0); offset by each character's arm pivot.
		float vanillaShoulderX = event.getArm() == HumanoidArm.LEFT ? 5.0F : -5.0F;
		poseStack.translate((vanillaShoulderX - arm.x) / 16F, (2.0F - arm.y) / 16F, -arm.z / 16F);

		int tint = hasGoldArms(playerType, punchLevel) ? ModelRayman.GOLD_TINT : -1;
		event.getSubmitNodeCollector().submitModelPart(arm, poseStack, model.renderType(getTexture(playerType)),
				event.getLightCoords(), OverlayTexture.NO_OVERLAY, null, tint);

		poseStack.popPose();
	}
}
