package com.abelatox.raycraft.client.render;

import com.abelatox.raycraft.client.LockOnHandler;
import com.abelatox.raycraft.lib.Reference;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLivingEvent;

@EventBusSubscriber(modid = Reference.MODID, value = Dist.CLIENT)
public class LockOnRenderer {

	public static final ContextKey<Boolean> IS_TARGET = new ContextKey<>(Identifier.fromNamespaceAndPath(Reference.MODID, "lock_on_target"));

	private static final RenderType RING = RenderTypes.text(Identifier.fromNamespaceAndPath(Reference.MODID, "textures/misc/lock_on.png"));
	private static final RenderType ARROW = RenderTypes.text(Identifier.fromNamespaceAndPath(Reference.MODID, "textures/misc/lock_arrow.png"));

	private static final float RING_PADDING = 0.5F;

	private static final float ARROW_TIP_U = 4.3F / 64.0F;
	private static final float ARROW_TIP_V = 23.7F / 32.0F;
	private static final float ARROW_HEAD_DEGREES = 213.2F;
	private static final float ARROW_TIP_RADIUS = 0.46F;

	public static void markTarget(LivingEntity entity, LivingEntityRenderState state) {
		state.setRenderData(IS_TARGET, entity == LockOnHandler.getTarget());
	}

	@SubscribeEvent
	public static void onRenderLivingPost(RenderLivingEvent.Post<?, ?, ?> event) {
		LivingEntityRenderState state = event.getRenderState();
		if (!state.getRenderDataOrDefault(IS_TARGET, false)) {
			return;
		}

		PoseStack poseStack = event.getPoseStack();
		SubmitNodeCollector collector = event.getSubmitNodeCollector();
		float size = Math.max(state.boundingBoxWidth, state.boundingBoxHeight) * 0.9F + RING_PADDING;

		poseStack.pushPose();
		poseStack.translate(0.0F, state.boundingBoxHeight * 0.5F, 0.0F);
		poseStack.rotate(Minecraft.getInstance().gameRenderer.mainCamera().rotation());
		// Billboard space: +z points at the camera; draw in front of the creature, not inside it.
		poseStack.translate(0.0F, 0.0F, state.boundingBoxWidth * 0.6F);

		collector.submitCustomGeometry(poseStack, RING, (pose, buffer) -> quad(buffer, pose, -size / 2.0F, -size / 2.0F, size / 2.0F, size / 2.0F, false));

		int strafe = LockOnHandler.getStrafe();
		if (strafe != 0) {
			float arrowW = size * 0.9F;
			float arrowH = arrowW * 0.5F;
			// lock_arrow.png curves in from the right with its head on the left; mirror it for the left side.
			boolean mirror = strafe < 0;

			poseStack.pushPose();
			poseStack.translate(strafe * size * ARROW_TIP_RADIUS, 0.0F, 0.0F);
			poseStack.rotate(Axis.ZP.rotationDegrees(strafe * (180.0F - ARROW_HEAD_DEGREES)));

			float x0 = -(mirror ? 1.0F - ARROW_TIP_U : ARROW_TIP_U) * arrowW;
			float y1 = ARROW_TIP_V * arrowH;
			collector.submitCustomGeometry(poseStack, ARROW, (pose, buffer) -> quad(buffer, pose, x0, y1 - arrowH, x0 + arrowW, y1, mirror));
			poseStack.popPose();
		}

		poseStack.popPose();
	}

	private static void quad(VertexConsumer buffer, PoseStack.Pose pose, float x0, float y0, float x1, float y1, boolean mirrorU) {
		float u0 = mirrorU ? 1.0F : 0.0F;
		float u1 = mirrorU ? 0.0F : 1.0F;
		vertex(buffer, pose, x0, y0, u0, 1.0F);
		vertex(buffer, pose, x1, y0, u1, 1.0F);
		vertex(buffer, pose, x1, y1, u1, 0.0F);
		vertex(buffer, pose, x0, y1, u0, 0.0F);
	}

	private static void vertex(VertexConsumer buffer, PoseStack.Pose pose, float x, float y, float u, float v) {
		buffer.addVertex(pose, x, y, 0.0F)
				.setColor(0xFFFFFFFF)
				.setUv(u, v)
				.setOverlay(OverlayTexture.NO_OVERLAY)
				.setLight(LightCoordsUtil.FULL_BRIGHT)
				.setNormal(pose, 0.0F, 1.0F, 0.0F);
	}
}
