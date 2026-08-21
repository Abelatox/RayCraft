package com.abelatox.raycraft.entities.render;

import com.abelatox.raycraft.entities.EntityPirateShot2;
import com.abelatox.raycraft.lib.Reference;
import com.abelatox.raycraft.models.ModModels;
import com.abelatox.raycraft.models.ModelPirateShot;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.LightCoordsUtil;

public class RenderEntityPirateShot2 extends EntityRenderer<EntityPirateShot2, RenderEntityPirateShot2.PirateShot2RenderState> {

	private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(Reference.MODID, "textures/models/pirateshot.png");

	private final ModelPirateShot model;

	public RenderEntityPirateShot2(EntityRendererProvider.Context context) {
		super(context);
		this.shadowRadius = 0.25F;
		this.model = new ModelPirateShot(context.bakeLayer(ModModels.PIRATE_SHOT_LAYER));
	}

	@Override
	public PirateShot2RenderState createRenderState() {
		return new PirateShot2RenderState();
	}

	@Override
	public void extractRenderState(EntityPirateShot2 entity, PirateShot2RenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.yRot = entity.yRotO + (entity.getYRot() - entity.yRotO) * partialTick;
		state.xRot = entity.xRotO + (entity.getXRot() - entity.xRotO) * partialTick;
		state.lightCoords = LightCoordsUtil.getLightCoords(entity.level(), entity.blockPosition());
		state.tickCount = entity.tickCount;
	}

	@Override
	public void submit(PirateShot2RenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState cameraState) {
		super.submit(state, poseStack, collector, cameraState);

		if (state.tickCount <= 2) {
			return;
		}

		poseStack.pushPose();
		poseStack.mulPose(Axis.YP.rotationDegrees(state.yRot));
		poseStack.mulPose(Axis.XN.rotationDegrees(state.xRot));
		poseStack.scale(2F, 2F, 2F);
		poseStack.translate(0, 0.05, 0);
		poseStack.mulPose(Axis.YP.rotationDegrees(180));

		collector.submitModel(model, state, poseStack, model.renderType(TEXTURE), state.lightCoords, OverlayTexture.NO_OVERLAY, 0, null);

		poseStack.popPose();
	}

	public static class PirateShot2RenderState extends EntityRenderState {
		public float yRot;
		public float xRot;
		public int lightCoords;
		public int tickCount;
	}
}
