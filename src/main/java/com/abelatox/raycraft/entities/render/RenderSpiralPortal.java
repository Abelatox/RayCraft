package com.abelatox.raycraft.entities.render;

import com.abelatox.raycraft.entities.EntitySpiralPortal;
import com.abelatox.raycraft.lib.Reference;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.LightCoordsUtil;

public class RenderSpiralPortal extends EntityRenderer<EntitySpiralPortal, RenderSpiralPortal.PortalRenderState> {

	private static final RenderType BLUE = RenderTypes.entityTranslucent(Identifier.fromNamespaceAndPath(Reference.MODID, "textures/models/spiral_portal.png"));
	private static final RenderType RED = RenderTypes.entityTranslucent(Identifier.fromNamespaceAndPath(Reference.MODID, "textures/models/spiral_portal_red.png"));
	/** The swirl's soft rim goes a little past the portal's box. */
	private static final float RIM = 1.2F;
	private static final float SPIN_DEGREES_PER_TICK = 6.0F;

	public RenderSpiralPortal(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public PortalRenderState createRenderState() {
		return new PortalRenderState();
	}

	@Override
	public void extractRenderState(EntitySpiralPortal entity, PortalRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.yRot = entity.getYRot();
		state.red = entity.isRed();
		state.flat = entity.isFlat();
		state.size = entity.getSize() * RIM;
	}

	@Override
	public void submit(PortalRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
		RenderType type = state.red ? RED : BLUE;
		poseStack.pushPose();
		poseStack.translate(0.0F, state.boundingBoxHeight / 2.0F, 0.0F);
		poseStack.rotate(Axis.YP.rotationDegrees(-state.yRot));
		if (state.flat) {
			poseStack.rotate(Axis.XP.rotationDegrees(90.0F));
		}
		layer(poseStack, collector, type, state.size, state.ageInTicks * SPIN_DEGREES_PER_TICK, 0.0F, 0xFFFFFFFF);
		layer(poseStack, collector, type, state.size * 0.6F, state.ageInTicks * SPIN_DEGREES_PER_TICK * 1.8F, 0.01F, 0xB0FFFFFF);
		poseStack.popPose();
		super.submit(state, poseStack, collector, camera);
	}

	private static void layer(PoseStack poseStack, SubmitNodeCollector collector, RenderType type, float size, float angle, float depth, int color) {
		poseStack.pushPose();
		poseStack.rotate(Axis.ZP.rotationDegrees(-angle));
		float h = size / 2.0F;
		collector.submitCustomGeometry(poseStack, type, (pose, buffer) -> {
			// Both windings, so it shows from either side.
			for (float z : new float[] {depth, -depth}) {
				float side = z < 0 ? -1.0F : 1.0F;
				vertex(buffer, pose, color, -h * side, -h, z, side > 0 ? 0.0F : 1.0F, 1.0F);
				vertex(buffer, pose, color, h * side, -h, z, side > 0 ? 1.0F : 0.0F, 1.0F);
				vertex(buffer, pose, color, h * side, h, z, side > 0 ? 1.0F : 0.0F, 0.0F);
				vertex(buffer, pose, color, -h * side, h, z, side > 0 ? 0.0F : 1.0F, 0.0F);
			}
		});
		poseStack.popPose();
	}

	private static void vertex(VertexConsumer buffer, PoseStack.Pose pose, int color, float x, float y, float z, float u, float v) {
		buffer.addVertex(pose, x, y, z).setColor(color).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(LightCoordsUtil.FULL_BRIGHT).setNormal(pose, 0.0F, 1.0F, 0.0F);
	}

	public static class PortalRenderState extends EntityRenderState {
		public float yRot;
		public boolean red;
		public boolean flat;
		public float size;
	}
}
