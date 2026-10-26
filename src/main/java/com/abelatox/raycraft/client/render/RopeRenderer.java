package com.abelatox.raycraft.client.render;

import com.abelatox.raycraft.client.GrappleHandler;
import com.abelatox.raycraft.entities.EntityLum;
import com.abelatox.raycraft.lib.Reference;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.SubmitCustomGeometryEvent;
import org.joml.Quaternionf;

@EventBusSubscriber(modid = Reference.MODID, value = Dist.CLIENT)
public class RopeRenderer {

	private static final RenderType STAR = RenderTypes.text(Identifier.fromNamespaceAndPath(Reference.MODID, "textures/particle/portal_star.png"));
	private static final int COLOR = 0xFF73BFFF;
	private static final double SPACING = 0.22;
	private static final float STAR_SIZE = 0.16F;
	/** Blocks per tick the stars flow along the rope, from Rayman to the Lum. */
	private static final float FLOW_SPEED = 0.05F;
	private static final float SPIN = 0.2F;

	@SubscribeEvent
	public static void onSubmitCustomGeometry(SubmitCustomGeometryEvent event) {
		EntityLum anchor = GrappleHandler.getAnchor();
		Player player = Minecraft.getInstance().player;
		if (anchor == null || player == null) {
			return;
		}
		float partialTick = Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(false);
		float time = player.tickCount + partialTick;
		Vec3 camera = event.getLevelRenderState().cameraRenderState.pos;
		Quaternionf facing = event.getLevelRenderState().cameraRenderState.orientation;
		Vec3 a = GrappleHandler.ropeOrigin(player, partialTick).subtract(camera);
		Vec3 b = GrappleHandler.anchorPoint(anchor, partialTick).subtract(camera);
		double length = a.distanceTo(b);
		double offset = (time * FLOW_SPEED) % SPACING;

		PoseStack poseStack = event.getPoseStack();
		int index = 0;
		for (double d = offset; d < length; d += SPACING, index++) {
			Vec3 point = a.lerp(b, d / length);
			float size = STAR_SIZE * (0.8F + 0.2F * Mth.sin(time * 0.4F + index));
			float angle = time * SPIN + index * 1.3F;
			poseStack.pushPose();
			poseStack.translate(point.x, point.y, point.z);
			poseStack.rotate(facing);
			poseStack.rotate(Axis.ZP.rotation(angle));
			event.getSubmitNodeCollector().submitCustomGeometry(poseStack, STAR, (pose, buffer) -> {
				float h = size / 2.0F;
				vertex(buffer, pose, -h, -h, 0, 1);
				vertex(buffer, pose, h, -h, 1, 1);
				vertex(buffer, pose, h, h, 1, 0);
				vertex(buffer, pose, -h, h, 0, 0);
			});
			poseStack.popPose();
		}
	}

	private static void vertex(VertexConsumer buffer, PoseStack.Pose pose, float x, float y, float u, float v) {
		buffer.addVertex(pose, x, y, 0.0F).setColor(COLOR).setUv(u, v).setLight(LightCoordsUtil.FULL_BRIGHT);
	}
}
