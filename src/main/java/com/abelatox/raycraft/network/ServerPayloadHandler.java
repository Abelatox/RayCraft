package com.abelatox.raycraft.network;

import com.abelatox.raycraft.capabilities.IPlayerCapabilities;
import com.abelatox.raycraft.capabilities.ModCapabilities;
import com.abelatox.raycraft.entities.RaycraftProjectile;
import com.abelatox.raycraft.lib.Strings;
import com.abelatox.raycraft.lib.Utils;
import com.abelatox.raycraft.network.payloads.*;
import com.abelatox.raycraft.sounds.ModSounds;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.component.SwingAnimation;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ServerPayloadHandler {

	public static void handleSetModel(final SetModelPayload payload, final IPayloadContext context) {
		context.enqueueWork(() -> {
			ServerPlayer player = (ServerPlayer) context.player();
			IPlayerCapabilities props = ModCapabilities.get(player);
			props.setPlayerType(payload.model());
			ModCapabilities.sync(player);
		});
	}

	public static void handleSetCharging(final SetChargingPayload payload, final IPayloadContext context) {
		context.enqueueWork(() -> {
			ServerPlayer player = (ServerPlayer) context.player();
			IPlayerCapabilities props = ModCapabilities.get(player);
			props.setCharging(payload.charging());
			ModCapabilities.sync(player);
		});
	}

	public static void handleSetGliding(final SetGlidingPayload payload, final IPayloadContext context) {
		context.enqueueWork(() -> {
			ServerPlayer player = (ServerPlayer) context.player();
			IPlayerCapabilities props = ModCapabilities.get(player);
			props.setGliding(payload.gliding());
			ModCapabilities.sync(player);
		});
	}

	public static void handleShoot(final ShootPayload payload, final IPayloadContext context) {
		context.enqueueWork(() -> {
			ServerPlayer player = (ServerPlayer) context.player();
			IPlayerCapabilities props = ModCapabilities.get(player);
			if (props.hasCustomPlayerType() && player.getMainHandItem().isEmpty()) {
				RaycraftProjectile shot = Utils.getEntityShot(player, payload.charged());
				if (shot != null) {
					shot.setOwner(player);
					// Centre the shot on the eye line (its position is the bottom of its hitbox).
					shot.setPos(shot.getX(), player.getEyeY() - 0.1 - shot.getBbHeight() / 2.0, shot.getZ());
					LivingEntity target = getLockOnTarget(player, payload.targetId());
					if (target != null) {
						aimAtTarget(shot, target, Mth.clamp(payload.curve(), -1, 1));
					} else {
						shot.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 1F, 0);
					}
					player.level().addFreshEntity(shot);
					if (player.isUnderWater() && Strings.RAYMAN.equals(props.getPlayerType())) {
						player.level().playSound(null, player.blockPosition(), ModSounds.FIST_UNDERWATER.get(), SoundSource.MASTER, 1F, 1F);
					} else {
						player.level().playSound(null, player.blockPosition(), ModSounds.FIST_SHOT_0.get(), SoundSource.MASTER, 1F, 1F);
						player.level().playSound(null, player.blockPosition(), Utils.getShootSound(player, payload.charged()), SoundSource.MASTER, 1F, 1F);
					}
					player.swing(InteractionHand.MAIN_HAND, SwingAnimation.DEFAULT, false);
				}
			}
		});
	}

	private static final double MAX_LOCK_ON_RANGE = 48.0;
	private static final float CURVE_ANGLE = 35.0F * Mth.DEG_TO_RAD;
	private static final double FULL_CURVE_DISTANCE = 10.0;

	private static LivingEntity getLockOnTarget(ServerPlayer player, int targetId) {
		if (targetId < 0) {
			return null;
		}
		Entity entity = player.level().getEntity(targetId);
		if (entity instanceof LivingEntity living && living != player && living.isAlive()
				&& player.distanceToSqr(living) <= MAX_LOCK_ON_RANGE * MAX_LOCK_ON_RANGE) {
			return living;
		}
		return null;
	}

	private static void aimAtTarget(RaycraftProjectile shot, LivingEntity target, int curve) {
		Vec3 toTarget = target.getBoundingBox().getCenter().subtract(shot.position());
		float angle = CURVE_ANGLE * (float) Mth.clamp(toTarget.length() / FULL_CURVE_DISTANCE, 0.3, 1.0);
		// Vec3#yRot turns left for positive angles, so strafing right uses a negative angle.
		Vec3 direction = toTarget.normalize().yRot(-curve * angle);
		shot.shoot(direction.x, direction.y, direction.z, 1F, 0F);
		shot.setHomingTarget(target);
	}

	public static void handlePlaySound(final PlaySoundPayload payload, final IPayloadContext context) {
		context.enqueueWork(() -> {
			ServerPlayer player = (ServerPlayer) context.player();
			player.level().playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.PIRATE_SHOT_1.get(), SoundSource.PLAYERS, 1F, 1F);
		});
	}
}
