package com.abelatox.raycraft.client;

import com.abelatox.raycraft.capabilities.ModCapabilities;
import com.abelatox.raycraft.lib.Reference;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderFrameEvent;

@EventBusSubscriber(modid = Reference.MODID, value = Dist.CLIENT)
public class LockOnHandler {

	private static final double ACQUIRE_RANGE = 32.0;
	private static final double MAX_RANGE = 40.0;
	private static final double ACQUIRE_CONE_DEGREES = 15.0;

	private static LivingEntity target;

	public static LivingEntity getTarget() {
		return target;
	}

	public static int getStrafe() {
		LocalPlayer player = Minecraft.getInstance().player;
		if (target == null || player == null) {
			return 0;
		}
		boolean left = player.input.keyPresses.left();
		boolean right = player.input.keyPresses.right();
		return left == right ? 0 : (right ? 1 : -1);
	}

	@SubscribeEvent
	public static void onClientTick(ClientTickEvent.Post event) {
		Minecraft mc = Minecraft.getInstance();
		LocalPlayer player = mc.player;

		boolean held = player != null && mc.gui.screen() == null
				&& ModCapabilities.get(player).hasCustomPlayerType()
				&& player.getMainHandItem().isEmpty()
				&& InputHandler.Keybinds.LOCK_ON.getKeybind().isDown();

		if (!held) {
			target = null;
			return;
		}

		if (target != null && !isValid(player, target, MAX_RANGE)) {
			target = null;
		}
		if (target == null) {
			target = findTarget(player);
		}
		if (target != null) {
			// The default lock-on key is also sprint; strafing around a target shouldn't sprint.
			player.setSprinting(false);
		}
	}

	@SubscribeEvent
	public static void onRenderFrame(RenderFrameEvent.Pre event) {
		LocalPlayer player = Minecraft.getInstance().player;
		if (target == null || player == null) {
			return;
		}

		float partialTick = event.getPartialTick().getGameTimeDeltaPartialTick(true);
		Vec3 eye = player.getEyePosition(partialTick);
		Vec3 aim = target.getPosition(partialTick).add(0, target.getBbHeight() * 0.5, 0);
		Vec3 delta = aim.subtract(eye);

		float yaw = (float) (Mth.atan2(delta.z, delta.x) * Mth.RAD_TO_DEG) - 90.0F;
		float pitch = (float) -(Mth.atan2(delta.y, delta.horizontalDistance()) * Mth.RAD_TO_DEG);

		player.setYRot(yaw);
		player.setXRot(pitch);
		player.yRotO = yaw;
		player.xRotO = pitch;
		player.setYHeadRot(yaw);
	}

	private static LivingEntity findTarget(LocalPlayer player) {
		Vec3 eye = player.getEyePosition();
		Vec3 look = player.getViewVector(1.0F);
		double maxCos = Math.cos(Math.toRadians(ACQUIRE_CONE_DEGREES));

		LivingEntity best = null;
		double bestCos = maxCos;
		for (Entity entity : player.level().getEntities(player, player.getBoundingBox().inflate(ACQUIRE_RANGE))) {
			if (!(entity instanceof LivingEntity living) || !isValid(player, living, ACQUIRE_RANGE)) {
				continue;
			}
			Vec3 toTarget = living.getBoundingBox().getCenter().subtract(eye).normalize();
			double cos = look.dot(toTarget);
			if (cos > bestCos) {
				bestCos = cos;
				best = living;
			}
		}
		return best;
	}

	private static boolean isValid(LocalPlayer player, LivingEntity living, double range) {
		return living != player && living.isAlive() && !living.isSpectator() && !(living instanceof ArmorStand)
				&& living.level() == player.level()
				&& player.distanceToSqr(living) <= range * range
				&& player.hasLineOfSight(living);
	}
}
