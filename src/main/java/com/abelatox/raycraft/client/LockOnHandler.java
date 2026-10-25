package com.abelatox.raycraft.client;

import com.abelatox.raycraft.blocks.BlockCage;
import com.abelatox.raycraft.capabilities.ModCapabilities;
import com.abelatox.raycraft.entities.EntityLum;
import com.abelatox.raycraft.lib.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
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

	// Rays cast inside the acquire cone to find cages (blocks can't be listed like entities).
	private static final int CAGE_RAY_RINGS = 5;
	private static final int CAGE_RAYS_PER_RING = 12;

	private static LivingEntity target;
	private static BlockPos cageTarget;
	private static EntityLum lumTarget;

	public static LivingEntity getTarget() {
		return target;
	}

	public static BlockPos getCageTarget() {
		return cageTarget;
	}

	public static EntityLum getLumTarget() {
		return lumTarget;
	}

	public static boolean hasTarget() {
		return target != null || cageTarget != null || lumTarget != null;
	}

	public static int getStrafe() {
		LocalPlayer player = Minecraft.getInstance().player;
		if (!hasTarget() || player == null) {
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
			cageTarget = null;
			lumTarget = null;
			return;
		}

		if (target != null && !isValid(player, target, MAX_RANGE)) {
			target = null;
		}
		if (cageTarget != null && !isValidCage(player, cageTarget, MAX_RANGE)) {
			cageTarget = null;
		}
		if (lumTarget != null && !isValidLum(player, lumTarget, MAX_RANGE)) {
			lumTarget = null;
		}
		if (!hasTarget()) {
			acquire(player);
		}
		if (hasTarget()) {
			// The default lock-on key is also sprint; strafing around a target shouldn't sprint.
			player.setSprinting(false);
		}
	}

	@SubscribeEvent
	public static void onRenderFrame(RenderFrameEvent.Pre event) {
		LocalPlayer player = Minecraft.getInstance().player;
		if (!hasTarget() || player == null || GrappleHandler.isSwinging()) {
			return;
		}

		float partialTick = event.getPartialTick().getGameTimeDeltaPartialTick(true);
		Vec3 eye = player.getEyePosition(partialTick);
		Entity entity = target != null ? target : lumTarget;
		Vec3 aim = entity != null ? entity.getPosition(partialTick).add(0, entity.getBbHeight() * 0.5, 0) : Vec3.atCenterOf(cageTarget);
		Vec3 delta = aim.subtract(eye);

		float yaw = (float) (Mth.atan2(delta.z, delta.x) * Mth.RAD_TO_DEG) - 90.0F;
		float pitch = (float) -(Mth.atan2(delta.y, delta.horizontalDistance()) * Mth.RAD_TO_DEG);

		player.setYRot(yaw);
		player.setXRot(pitch);
		player.yRotO = yaw;
		player.xRotO = pitch;
		player.setYHeadRot(yaw);
	}

	private static void acquire(LocalPlayer player) {
		Vec3 eye = player.getEyePosition();
		Vec3 look = player.getViewVector(1.0F);
		double maxCos = Math.cos(Math.toRadians(ACQUIRE_CONE_DEGREES));

		Entity bestEntity = null;
		double bestCos = maxCos;
		for (Entity entity : player.level().getEntities(player, player.getBoundingBox().inflate(ACQUIRE_RANGE))) {
			boolean valid = entity instanceof LivingEntity living ? isValid(player, living, ACQUIRE_RANGE)
					: entity instanceof EntityLum lum && isValidLum(player, lum, ACQUIRE_RANGE);
			if (!valid) {
				continue;
			}
			Vec3 toTarget = entity.getBoundingBox().getCenter().subtract(eye).normalize();
			double cos = look.dot(toTarget);
			if (cos > bestCos) {
				bestCos = cos;
				bestEntity = entity;
			}
		}

		BlockPos bestCage = null;
		for (int ring = 0; ring <= CAGE_RAY_RINGS; ring++) {
			double spread = Math.toRadians(ACQUIRE_CONE_DEGREES) * ring / CAGE_RAY_RINGS;
			int rays = ring == 0 ? 1 : CAGE_RAYS_PER_RING;
			for (int i = 0; i < rays; i++) {
				Vec3 dir = rotateAround(look, spread, Mth.TWO_PI * i / rays);
				BlockHitResult hit = player.level().clip(new ClipContext(eye, eye.add(dir.scale(ACQUIRE_RANGE)),
						ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));
				if (hit.getType() != HitResult.Type.BLOCK || !(player.level().getBlockState(hit.getBlockPos()).getBlock() instanceof BlockCage)) {
					continue;
				}
				double cos = look.dot(Vec3.atCenterOf(hit.getBlockPos()).subtract(eye).normalize());
				if (cos > bestCos) {
					bestCos = cos;
					bestCage = hit.getBlockPos();
				}
			}
		}

		if (bestCage != null) {
			cageTarget = bestCage;
		} else if (bestEntity instanceof EntityLum lum) {
			lumTarget = lum;
		} else {
			target = (LivingEntity) bestEntity;
		}
	}

	private static Vec3 rotateAround(Vec3 look, double spread, double around) {
		if (spread == 0) {
			return look;
		}
		Vec3 side = Math.abs(look.y) > 0.99 ? new Vec3(1, 0, 0) : look.cross(new Vec3(0, 1, 0)).normalize();
		Vec3 up = side.cross(look).normalize();
		Vec3 offset = side.scale(Math.cos(around)).add(up.scale(Math.sin(around)));
		return look.scale(Math.cos(spread)).add(offset.scale(Math.sin(spread))).normalize();
	}

	private static boolean isValidLum(LocalPlayer player, EntityLum lum, double range) {
		return lum.isPurple() && !lum.isRemoved() && lum.level() == player.level()
				&& player.distanceToSqr(lum) <= range * range && player.hasLineOfSight(lum);
	}

	private static boolean isValidCage(LocalPlayer player, BlockPos pos, double range) {
		if (!(player.level().getBlockState(pos).getBlock() instanceof BlockCage)) {
			return false;
		}
		Vec3 eye = player.getEyePosition();
		Vec3 centre = Vec3.atCenterOf(pos);
		if (eye.distanceToSqr(centre) > range * range) {
			return false;
		}
		BlockHitResult hit = player.level().clip(new ClipContext(eye, centre, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));
		return hit.getType() == HitResult.Type.MISS || hit.getBlockPos().equals(pos);
	}

	private static boolean isValid(LocalPlayer player, LivingEntity living, double range) {
		return living != player && living.isAlive() && !living.isSpectator() && !(living instanceof ArmorStand)
				&& living.level() == player.level()
				&& player.distanceToSqr(living) <= range * range
				&& player.hasLineOfSight(living);
	}
}
