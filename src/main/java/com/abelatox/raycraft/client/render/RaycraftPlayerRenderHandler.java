package com.abelatox.raycraft.client.render;

import com.abelatox.raycraft.lib.Reference;
import com.abelatox.raycraft.lib.Strings;
import com.abelatox.raycraft.models.ModModels;
import com.abelatox.raycraft.models.ModelGlobox;
import com.abelatox.raycraft.models.ModelHoodlum;
import com.abelatox.raycraft.models.ModelRayman;
import com.abelatox.raycraft.models.ModelRoboPirate;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Pose;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderPlayerEvent;

@EventBusSubscriber(modid = Reference.MODID, value = Dist.CLIENT)
public class RaycraftPlayerRenderHandler {

	private static Model<AvatarRenderState> raymanModel;
	private static Model<AvatarRenderState> hoodlumModel;
	private static Model<AvatarRenderState> globoxModel;
	private static Model<AvatarRenderState> roboPirateModel;

	private static Model<AvatarRenderState> getModel(String playerType) {
		EntityModelSet models = Minecraft.getInstance().getEntityModels();

		switch (playerType) {
			case Strings.RAYMAN -> {
				if (raymanModel == null) {
					raymanModel = new ModelRayman(models.bakeLayer(ModModels.RAYMAN_LAYER));
				}
				return raymanModel;
			}
			case Strings.HOODLUM -> {
				if (hoodlumModel == null) {
					hoodlumModel = new ModelHoodlum(models.bakeLayer(ModModels.HOODLUM_LAYER));
				}
				return hoodlumModel;
			}
			case Strings.GLOBOX -> {
				if (globoxModel == null) {
					globoxModel = new ModelGlobox(models.bakeLayer(ModModels.GLOBOX_LAYER));
				}
				return globoxModel;
			}
			case Strings.ROBO_PIRATE_RED, Strings.ROBO_PIRATE_GREEN -> {
				if (roboPirateModel == null) {
					roboPirateModel = new ModelRoboPirate(models.bakeLayer(ModModels.ROBO_PIRATE_LAYER));
				}
				return roboPirateModel;
			}
			default -> {
				return null;
			}
		}
	}

	@SubscribeEvent
	public static void onRenderPlayerPre(RenderPlayerEvent.Pre<?> event) {
		AvatarRenderState state = event.getRenderState();
		RaycraftAvatarRenderData data = state.getRenderDataOrDefault(RaycraftRenderData.AVATAR_DATA, RaycraftAvatarRenderData.EMPTY);

		if (data.playerType() == null || data.playerType().isEmpty()) {
			return;
		}

		Model<AvatarRenderState> model = getModel(data.playerType());
		if (model == null) {
			return;
		}

		event.setCanceled(true);

		Identifier texture = Identifier.fromNamespaceAndPath(Reference.MODID, "textures/models/" + data.playerType() + ".png");

		PoseStack poseStack = event.getPoseStack();
		SubmitNodeCollector collector = event.getSubmitNodeCollector();

		model.setupAnim(state);

		poseStack.pushPose();

		if (state.pose == Pose.SWIMMING) {
			poseStack.translate(0, 1.3, 0);
			poseStack.mulPose(Axis.XP.rotationDegrees(90));
		} else if (state.pose == Pose.SLEEPING) {
			poseStack.translate(0, 0, -1.5);
			poseStack.mulPose(Axis.XN.rotationDegrees(90));
			poseStack.mulPose(Axis.ZP.rotationDegrees(180));
		} else {
			poseStack.mulPose(Axis.YN.rotationDegrees(state.bodyRot));
		}

		poseStack.translate(0, 1.5, 0);
		poseStack.mulPose(Axis.YP.rotationDegrees(180));
		poseStack.mulPose(Axis.ZP.rotationDegrees(180));

		collector.submitModel(model, state, poseStack, model.renderType(texture), data.lightCoords(), OverlayTexture.NO_OVERLAY, 0, null);

		poseStack.popPose();
	}
}
