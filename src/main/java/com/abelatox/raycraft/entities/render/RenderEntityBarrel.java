package com.abelatox.raycraft.entities.render;

import com.abelatox.raycraft.entities.EntityBarrel;
import com.abelatox.raycraft.lib.Reference;
import com.abelatox.raycraft.models.ModModels;
import com.abelatox.raycraft.models.ModelBarrel;

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

public class RenderEntityBarrel extends EntityRenderer<EntityBarrel, RenderEntityBarrel.BarrelRenderState> {

	private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(Reference.MODID, "textures/models/barrel.png");

	private final ModelBarrel model;

	public RenderEntityBarrel(EntityRendererProvider.Context context) {
		super(context);
		this.shadowRadius = 0.5F;
		this.model = new ModelBarrel(context.bakeLayer(ModModels.BARREL_LAYER));
	}

	@Override
	public BarrelRenderState createRenderState() {
		return new BarrelRenderState();
	}

	@Override
	public void extractRenderState(EntityBarrel entity, BarrelRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.yRot = entity.yRotO + (entity.getYRot() - entity.yRotO) * partialTick;
		state.xRot = entity.xRotO + (entity.getXRot() - entity.xRotO) * partialTick;
		state.lightCoords = LightCoordsUtil.getLightCoords(entity.level(), entity.blockPosition());
	}

	@Override
	public void submit(BarrelRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState cameraState) {
		super.submit(state, poseStack, collector, cameraState);

		poseStack.pushPose();
		poseStack.scale(1.5F, 1.5F, 1.5F);
		poseStack.rotate(Axis.YP.rotationDegrees(state.yRot));
		poseStack.rotate(Axis.XN.rotationDegrees(state.xRot));

		collector.submitModel(model, state, poseStack, model.renderType(TEXTURE), state.lightCoords, OverlayTexture.NO_OVERLAY, 0);

		poseStack.popPose();
	}

	public static class BarrelRenderState extends EntityRenderState {
		public float yRot;
		public float xRot;
		public int lightCoords;
	}
}
