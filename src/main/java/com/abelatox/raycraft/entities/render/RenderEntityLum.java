package com.abelatox.raycraft.entities.render;

import com.abelatox.raycraft.entities.EntityLum;
import com.abelatox.raycraft.lib.Reference;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

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
import net.minecraft.util.Mth;

// lum.png: left half is the ball, right half is one wing (with its base on the left).
public class RenderEntityLum extends EntityRenderer<EntityLum, RenderEntityLum.LumRenderState> {

	private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(Reference.MODID, "textures/models/lum.png");
	private static final RenderType RENDER_TYPE = RenderTypes.entityTranslucent(TEXTURE);

	private static final int WING_COLOR = 0xFFFFFFFF;

	private static final float BALL_SIZE = 0.4F;
	private static final float WING_LENGTH = 0.32F;
	private static final float WING_HEIGHT = 0.2F;
	private static final float WING_ANCHOR_X = BALL_SIZE * 0.35F;
	private static final float WING_ANCHOR_Y = BALL_SIZE * 0.1F;

	private static final float FLAP_SPEED = 3.0F;
	private static final float FLAP_AMPLITUDE = 0.6F;
	private static final float WING_BASE_ANGLE = 0.25F;

	private static final float BOB_SPEED = 0.15F;
	private static final float BOB_HEIGHT = 0.06F;

	public RenderEntityLum(EntityRendererProvider.Context context) {
		super(context);
		this.shadowRadius = 0.15F;
	}

	// side: 1 right, -1 left.
	private static void wing(VertexConsumer buffer, PoseStack.Pose pose, int color, float side, float angle) {
		float ax = WING_ANCHOR_X * side;
		float ay = WING_ANCHOR_Y;
		float dx = Mth.cos(angle) * side;
		float dy = Mth.sin(angle);
		float px = -dy * side * WING_HEIGHT / 2.0F;
		float py = dx * side * WING_HEIGHT / 2.0F;
		float tx = ax + dx * WING_LENGTH;
		float ty = ay + dy * WING_LENGTH;
		float z = -0.01F;

		// Vertex order depends on the side to keep the same quad winding.
		if (side > 0) {
			vertex(buffer, pose, color, ax - px, ay - py, z, 0.5F, 1.0F);
			vertex(buffer, pose, color, tx - px, ty - py, z, 1.0F, 1.0F);
			vertex(buffer, pose, color, tx + px, ty + py, z, 1.0F, 0.0F);
			vertex(buffer, pose, color, ax + px, ay + py, z, 0.5F, 0.0F);
		} else {
			vertex(buffer, pose, color, tx - px, ty - py, z, 1.0F, 1.0F);
			vertex(buffer, pose, color, ax - px, ay - py, z, 0.5F, 1.0F);
			vertex(buffer, pose, color, ax + px, ay + py, z, 0.5F, 0.0F);
			vertex(buffer, pose, color, tx + px, ty + py, z, 1.0F, 0.0F);
		}
	}

	private static void vertex(VertexConsumer buffer, PoseStack.Pose pose, int color, float x, float y, float z, float u, float v) {
		buffer.addVertex(pose, x, y, z).setColor(color).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(LightCoordsUtil.FULL_BRIGHT).setNormal(pose, 0.0F, 1.0F, 0.0F);
	}

	@Override
	public LumRenderState createRenderState() {
		return new LumRenderState();
	}

	@Override
	public void extractRenderState(EntityLum entity, LumRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.color = EntityLum.getColor(entity.getLumType());
	}

	@Override
	public void submit(LumRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
		poseStack.pushPose();
		poseStack.translate(0.0F, 0.25F + Mth.sin(state.ageInTicks * BOB_SPEED) * BOB_HEIGHT, 0.0F);
		poseStack.rotate(camera.orientation);

		float wingAngle = WING_BASE_ANGLE + Mth.sin(state.ageInTicks * FLAP_SPEED) * FLAP_AMPLITUDE;
		int color = state.color;

		collector.submitCustomGeometry(poseStack, RENDER_TYPE, (pose, buffer) -> {
			wing(buffer, pose, WING_COLOR, 1.0F, wingAngle);
			wing(buffer, pose, WING_COLOR, -1.0F, wingAngle);

			float h = BALL_SIZE / 2.0F;
			vertex(buffer, pose, color, -h, -h, 0.0F, 0.0F, 1.0F);
			vertex(buffer, pose, color, h, -h, 0.0F, 0.5F, 1.0F);
			vertex(buffer, pose, color, h, h, 0.0F, 0.5F, 0.0F);
			vertex(buffer, pose, color, -h, h, 0.0F, 0.0F, 0.0F);
		});

		poseStack.popPose();
		super.submit(state, poseStack, collector, camera);
	}

	public static class LumRenderState extends EntityRenderState {
		public int color = 0xFFFFFFFF;
	}
}
