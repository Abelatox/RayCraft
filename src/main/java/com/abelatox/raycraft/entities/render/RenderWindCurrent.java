package com.abelatox.raycraft.entities.render;

import com.abelatox.raycraft.entities.EntityWindCurrent;
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
import net.minecraft.world.phys.AABB;

/** Layers of wind streaks wrapped round the current, narrow at the bottom, spinning faster the higher they are. */
public class RenderWindCurrent extends EntityRenderer<EntityWindCurrent, RenderWindCurrent.WindRenderState> {

	private static final RenderType WIND = RenderTypes.entityTranslucent(Identifier.fromNamespaceAndPath(Reference.MODID, "textures/models/wind_current.png"));
	private static final int SEGMENTS = 24;
	private static final float RING_HEIGHT = 1.0F;
	private static final float SPIN_PER_TICK = 0.04F;
	private static final float RISE_PER_TICK = 0.03F;

	public RenderWindCurrent(EntityRendererProvider.Context context) {
		super(context);
	}

	/** The entity's box is only the funnel's narrow base; culling needs the wide top too. */
	@Override
	protected AABB getBoundingBoxForCulling(EntityWindCurrent entity, float partialTicks) {
		return entity.getBoundingBox().inflate(entity.getRadius() * (1.0F - EntityWindCurrent.BOTTOM_SCALE), 0.0, entity.getRadius() * (1.0F - EntityWindCurrent.BOTTOM_SCALE));
	}

	@Override
	public WindRenderState createRenderState() {
		return new WindRenderState();
	}

	@Override
	public void extractRenderState(EntityWindCurrent entity, WindRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.height = entity.getHeight();
		state.radius = entity.getRadius();
	}

	@Override
	public void submit(WindRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
		float time = state.ageInTicks;
		collector.submitCustomGeometry(poseStack, WIND, (pose, buffer) -> {
			float layerScale = 1.0F - 1 * 0.3F;
			float spin = time * SPIN_PER_TICK * (1.0F + 1 * 0.6F);
			for (float y0 = 0.0F; y0 < state.height; y0 += RING_HEIGHT) {
				float y1 = Math.min(y0 + RING_HEIGHT, state.height);
				float r0 = radius(state, y0) * layerScale;
				float r1 = radius(state, y1) * layerScale;
				float v0 = y0 / RING_HEIGHT * 0.25F + time * RISE_PER_TICK;
				float v1 = y1 / RING_HEIGHT * 0.25F + time * RISE_PER_TICK;
				int alpha = (int) (170 * Math.min(1.0F, Math.min(y0 + 1.0F, state.height - y0) / 2.0F)) << 24;
				for (int i = 0; i < SEGMENTS; i++) {
					float a0 = Mth.TWO_PI * i / SEGMENTS + spin;
					float a1 = Mth.TWO_PI * (i + 1) / SEGMENTS + spin;
					float u0 = (float) i / SEGMENTS * 2.0F;
					float u1 = (float) (i + 1) / SEGMENTS * 2.0F;
					int color = alpha | 0xFFFFFF;
					quad(buffer, pose, color, r0, r1, a0, a1, y0, y1, u0, u1, v0, v1);
					quad(buffer, pose, color, r0, r1, a1, a0, y0, y1, u1, u0, v0, v1);
				}

			}
		});
		super.submit(state, poseStack, collector, camera);
	}

	private static float radius(WindRenderState state, float y) {
		return state.radius * Mth.lerp(y / state.height, EntityWindCurrent.BOTTOM_SCALE, 1.0F);
	}

	private static void quad(VertexConsumer buffer, PoseStack.Pose pose, int color, float r0, float r1, float a0, float a1,
			float y0, float y1, float u0, float u1, float v0, float v1) {
		vertex(buffer, pose, color, Mth.cos(a0) * r0, y0, Mth.sin(a0) * r0, u0, v1);
		vertex(buffer, pose, color, Mth.cos(a1) * r0, y0, Mth.sin(a1) * r0, u1, v1);
		vertex(buffer, pose, color, Mth.cos(a1) * r1, y1, Mth.sin(a1) * r1, u1, v0);
		vertex(buffer, pose, color, Mth.cos(a0) * r1, y1, Mth.sin(a0) * r1, u0, v0);
	}

	private static void vertex(VertexConsumer buffer, PoseStack.Pose pose, int color, float x, float y, float z, float u, float v) {
		buffer.addVertex(pose, x, y, z).setColor(color).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(LightCoordsUtil.FULL_BRIGHT).setNormal(pose, 0.0F, 1.0F, 0.0F);
	}

	public static class WindRenderState extends EntityRenderState {
		public float height;
		public float radius;
	}
}
