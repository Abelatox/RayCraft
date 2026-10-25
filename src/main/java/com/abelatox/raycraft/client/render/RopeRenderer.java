package com.abelatox.raycraft.client.render;

import com.abelatox.raycraft.client.GrappleHandler;
import com.abelatox.raycraft.entities.EntityLum;
import com.abelatox.raycraft.lib.Reference;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.SubmitCustomGeometryEvent;

@EventBusSubscriber(modid = Reference.MODID, value = Dist.CLIENT)
public class RopeRenderer {

	private static final RenderType ROPE = RenderTypes.text(Identifier.fromNamespaceAndPath(Reference.MODID, "textures/misc/rope.png"));
	private static final float WIDTH = 0.06F;
	private static final int COLOR = EntityLum.getColor("blue");

	@SubscribeEvent
	public static void onSubmitCustomGeometry(SubmitCustomGeometryEvent event) {
		EntityLum anchor = GrappleHandler.getAnchor();
		Player player = Minecraft.getInstance().player;
		if (anchor == null || player == null) {
			return;
		}
		float partialTick = Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(false);
		Vec3 camera = event.getLevelRenderState().cameraRenderState.pos;
		Vec3 a = GrappleHandler.ropeOrigin(player, partialTick).subtract(camera);
		Vec3 b = GrappleHandler.anchorPoint(anchor, partialTick).subtract(camera);
		// Ribbon sideways to both the rope and the view direction, so it always shows its width.
		Vec3 side = b.subtract(a).cross(a.add(b).scale(0.5));
		if (side.lengthSqr() < 1.0E-8) {
			return;
		}
		Vec3 s = side.normalize().scale(WIDTH / 2.0);

		PoseStack poseStack = event.getPoseStack();
		event.getSubmitNodeCollector().submitCustomGeometry(poseStack, ROPE, (pose, buffer) -> {
			// Both windings, so it's visible whichever way it faces.
			quad(buffer, pose, a.add(s), a.subtract(s), b.subtract(s), b.add(s));
			quad(buffer, pose, b.add(s), b.subtract(s), a.subtract(s), a.add(s));
		});
	}

	private static void quad(VertexConsumer buffer, PoseStack.Pose pose, Vec3 p0, Vec3 p1, Vec3 p2, Vec3 p3) {
		vertex(buffer, pose, p0, 0, 0);
		vertex(buffer, pose, p1, 1, 0);
		vertex(buffer, pose, p2, 1, 1);
		vertex(buffer, pose, p3, 0, 1);
	}

	private static void vertex(VertexConsumer buffer, PoseStack.Pose pose, Vec3 p, float u, float v) {
		buffer.addVertex(pose, (float) p.x, (float) p.y, (float) p.z).setColor(COLOR).setUv(u, v).setLight(LightCoordsUtil.FULL_BRIGHT);
	}
}
