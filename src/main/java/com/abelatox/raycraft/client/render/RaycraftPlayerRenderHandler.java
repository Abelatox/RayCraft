package com.abelatox.raycraft.client.render;

import com.abelatox.raycraft.capabilities.IPlayerCapabilities;
import com.abelatox.raycraft.capabilities.ModCapabilities;
import com.abelatox.raycraft.entities.EntityFlyingShell;
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
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.*;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;

@EventBusSubscriber(modid = Reference.MODID, value = Dist.CLIENT)
public class RaycraftPlayerRenderHandler {
	private static final Map<String, Model<AvatarRenderState>> WORLD_MODELS = new HashMap<>();
	private static final Map<String, Model<AvatarRenderState>> HAND_MODELS = new HashMap<>();
	private static final Identifier PLUM_TEXTURE = Identifier.fromNamespaceAndPath(Reference.MODID, "textures/models/plum.png");
	private static final float PLUM_HEIGHT = 2.05F;
	/**
	 * Matches the 1 s charge in InputHandler.
	 */
	private static final float CHARGE_TICKS = 20.0F;
	private static ModelRayman raymanGoldArms;
	private static ModelBarrel barrelModel;
	private static ModelPlum plumModel;
	private static float chargeStart = -1.0F;

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

	private static ModelPlum getPlumModel() {
		if (plumModel == null) {
			plumModel = new ModelPlum(Minecraft.getInstance().getEntityModels().bakeLayer(ModModels.PLUM_LAYER));
		}
		return plumModel;
	}

	private static Identifier getTexture(String playerType) {
		return Identifier.fromNamespaceAndPath(Reference.MODID, "textures/models/" + playerType + ".png");
	}

	private static boolean hasGoldArms(String playerType, int punchLevel) {
		return Strings.RAYMAN.equals(playerType) && punchLevel == ModelRayman.GOLD_FIST_LEVEL;
	}

	private static void applyShellRoll(PoseStack poseStack, RaycraftAvatarRenderData data) {
		if (data.shellRotation() == null) {
			return;
		}
		poseStack.translate(data.shellOffset().x, data.shellOffset().y, data.shellOffset().z);
		poseStack.rotate(data.shellRotation());
	}

	private static final float MAX_HEAD_YAW = 80.0F;

	/**
	 * The look angles are in world space, but the rolled model's head turns in its own frame: undo the roll on the look
	 * direction, so looking at the ground while rolled on a side turns the head sideways instead of nodding.
	 */
	private static void alignHeadWithShellRoll(AvatarRenderState state, RaycraftAvatarRenderData data) {
		if (data.shellRotation() == null) {
			return;
		}
		Vector3f look = Vec3.directionFromRotation(state.xRot, state.bodyRot + state.yRot).toVector3f();
		new Quaternionf(data.shellRotation()).conjugate().transform(look);
		float headYaw = Mth.wrapDegrees((float) (Mth.atan2(-look.x, look.z) * Mth.RAD_TO_DEG) - state.bodyRot);
		state.yRot = Mth.clamp(headYaw, -MAX_HEAD_YAW, MAX_HEAD_YAW);
		state.xRot = (float) (Math.asin(Mth.clamp(-look.y, -1.0F, 1.0F)) * Mth.RAD_TO_DEG);
	}

	private static boolean pitchLifted;
	private static float realPitch;
	private static float realPitchO;

	static {
		EntityFlyingShell.setTurnHook(RaycraftPlayerRenderHandler::restorePitch);
	}

	/**
	 * Vanilla clamps the pitch at +-90 while turning. Riding a flying shell the turn is applied to a zero pitch instead,
	 * and {@link #restorePitch} adds the result to the real pitch right after, with no limit.
	 */
	@SubscribeEvent
	public static void onCalculateTurn(CalculatePlayerTurnEvent event) {
		LocalPlayer player = Minecraft.getInstance().player;
		if (player == null || pitchLifted || !(player.getVehicle() instanceof EntityFlyingShell)) {
			return;
		}
		realPitch = player.getXRot();
		realPitchO = player.xRotO;
		player.setXRot(0.0F);
		player.xRotO = 0.0F;
		pitchLifted = true;
	}

	private static void restorePitch(Entity passenger) {
		if (!pitchLifted || passenger != Minecraft.getInstance().player) {
			return;
		}
		pitchLifted = false;
		float delta = passenger.getXRot();
		passenger.xRot = realPitch + delta;
		passenger.xRotO = realPitchO + delta;
	}

	private static boolean turnTracked;
	private static float lastYaw;
	private static float lastPitch;

	/**
	 * Mouse turns change the world yaw and pitch, but with the camera rolled the screen's axes are turned too:
	 * re-aim each frame's turn so moving the mouse right still looks to the right of the screen.
	 */
	@SubscribeEvent
	public static void onRenderFrame(RenderFrameEvent.Pre event) {
		LocalPlayer player = Minecraft.getInstance().player;
		if (player == null || !(player.getVehicle() instanceof EntityFlyingShell shell)) {
			turnTracked = false;
			return;
		}
		if (turnTracked) {
			float dYaw = player.getYRot() - lastYaw;
			float dPitch = player.getXRot() - lastPitch;
			float roll = shell.getRollAngle(event.getPartialTick().getGameTimeDeltaPartialTick(false)) * Mth.DEG_TO_RAD;
			float yaw = lastYaw + dYaw * Mth.cos(roll) + dPitch * Mth.sin(roll);
			float pitch = lastPitch + dPitch * Mth.cos(roll) - dYaw * Mth.sin(roll);
			player.yRotO += yaw - player.getYRot();
			player.xRotO += pitch - player.getXRot();
			player.setYRot(yaw);
			player.xRot = pitch;
		}
		lastYaw = player.getYRot();
		lastPitch = player.getXRot();
		turnTracked = true;
	}

	@SubscribeEvent
	public static void onCameraAngles(ViewportEvent.ComputeCameraAngles event) {
		if (event.getCamera().entity() instanceof Player player && player.getVehicle() instanceof EntityFlyingShell shell) {
			event.setRoll(event.getRoll() - shell.getRollAngle(event.getPartialTick()));
		}
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

		alignHeadWithShellRoll(state, data);
		model.setupAnim(state);

		poseStack.pushPose();
		applyShellRoll(poseStack, data);

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
		} else if (data.holdingPlum()) {
			ModelPlum plum = getPlumModel();
			poseStack.pushPose();
			poseStack.translate(0, PLUM_HEIGHT, 0F);
			poseStack.rotate(Axis.YN.rotationDegrees(state.bodyRot));
			poseStack.scale(-1.0F, -1.0F, 1.0F);
			poseStack.translate(0.0F, -ModelPlum.CENTER_Y / 16.0F, 0.0F);
			collector.submitModel(plum, state, poseStack, plum.renderType(PLUM_TEXTURE), data.lightCoords(), OverlayTexture.NO_OVERLAY, 0);
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

	/**
	 * Angle after {@code elapsed} ticks when the speed grows from {@code startSpeed} to
	 * {@code startSpeed + extraSpeed} over the charge. It integrates the speed: multiplying the
	 * current speed by the time would also add the ramp to every tick already spun, racing at first.
	 */
	private static float rampedAngle(float elapsed, float startSpeed, float extraSpeed) {
		float ramp = Math.min(elapsed, CHARGE_TICKS);
		return startSpeed * elapsed + extraSpeed * (ramp * ramp / (2.0F * CHARGE_TICKS) + Math.max(0.0F, elapsed - CHARGE_TICKS));
	}

	private static void applyChargePose(ModelPart arm, Player player, HumanoidArm side) {
		if (!ModCapabilities.get(player).getIsCharging()) {
			chargeStart = -1.0F;
			return;
		}
		float time = player.tickCount + Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(false);
		if (chargeStart < 0.0F || chargeStart > time) {
			chargeStart = time;
		}
		float elapsed = time - chargeStart;
		float progress = Mth.clamp(elapsed / CHARGE_TICKS, 0.0F, 1.0F);
		float sign = side == HumanoidArm.RIGHT ? 1.0F : -1.0F;
		float circle = rampedAngle(elapsed, 0.2F, 0.2F);
		float radius = 0.08F + 0.22F * progress;
		arm.yRot = rampedAngle(elapsed, 0.2F, 0.4F) * sign;
		arm.xRot = -0.25F * progress + Mth.sin(circle) * radius;
		arm.zRot = Mth.cos(circle) * radius * sign;
		if (progress >= 1.0F) {
			// Tremble from the interpolated time, not random per frame, so it looks the same at any frame rate.
			arm.xRot += (Mth.sin(time * 2.3F) + Mth.sin(time * 3.7F)) * 0.015F;
			arm.zRot += (Mth.sin(time * 2.9F) + Mth.sin(time * 4.1F)) * 0.015F;
		}
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
		Player localPlayer = Minecraft.getInstance().player;
		if (localPlayer != null && event.getArm() == localPlayer.getMainArm()) {
			applyChargePose(arm, localPlayer, event.getArm());
		}

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
