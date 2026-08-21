package com.abelatox.raycraft.entities.render;

import com.abelatox.raycraft.entities.EntityFist;
import com.abelatox.raycraft.lib.Reference;
import com.abelatox.raycraft.models.ModModels;
import com.abelatox.raycraft.models.ModelFist;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.LightCoordsUtil;

public class RenderEntityFist extends EntityRenderer<EntityFist, RenderEntityFist.FistRenderState> {

	private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(Reference.MODID, "textures/models/fist.png");

	private final ModelFist model;

	public RenderEntityFist(EntityRendererProvider.Context context) {
		super(context);
		this.shadowRadius = 0.25F;
		this.model = new ModelFist(context.bakeLayer(ModModels.FIST_LAYER));
	}

	@Override
	public FistRenderState createRenderState() {
		return new FistRenderState();
	}

	@Override
	public void extractRenderState(EntityFist entity, FistRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.yRot = entity.yRotO + (entity.getYRot() - entity.yRotO) * partialTick;
		state.xRot = entity.xRotO + (entity.getXRot() - entity.xRotO) * partialTick;
		state.lightCoords = LightCoordsUtil.getLightCoords(entity.level(), entity.blockPosition());
		state.tickCount = entity.tickCount;
		state.lvl = entity.getLvl();
	}

	@Override
	public void submit(FistRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState cameraState) {
		super.submit(state, poseStack, collector, cameraState);

		// El proyectil original evita renderizarse durante los primeros ticks para que no tape la cámara
		if (state.tickCount <= 1) {
			return;
		}

		float r, g, b;
		switch (state.lvl) {
			case 1 -> {
				r = 0F;
				g = 0.9F;
				b = 1F;
			}
			case 2 -> {
				r = 1F;
				g = 0.7F;
				b = 0F;
			}
			case 3, 4 -> {
				r = 1F;
				g = 0.9F;
				b = 0F;
			}
			default -> {
				r = 1F;
				g = 1F;
				b = 1F;
			}
		}

		poseStack.pushPose();
		if (state.lvl == 4) {
			poseStack.scale(4F, 4F, 4F);
		}
		poseStack.mulPose(Axis.YP.rotationDegrees(state.yRot));
		poseStack.mulPose(Axis.XN.rotationDegrees(state.xRot));

		int color = ARGB.colorFromFloat(1F, r, g, b);
		collector.submitModel(model, state, poseStack, model.renderType(TEXTURE), state.lightCoords, OverlayTexture.NO_OVERLAY, color, null, 0, null);

		poseStack.popPose();
	}

	public static class FistRenderState extends EntityRenderState {
		public float yRot;
		public float xRot;
		public int lightCoords;
		public int tickCount;
		public int lvl;
	}
}
