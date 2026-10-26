package com.abelatox.raycraft.network;

import com.abelatox.raycraft.blocks.BlockCage;
import com.abelatox.raycraft.capabilities.IPlayerCapabilities;
import com.abelatox.raycraft.capabilities.ModCapabilities;
import com.abelatox.raycraft.entities.*;
import com.abelatox.raycraft.events.BarrelFlight;
import com.abelatox.raycraft.events.GrappleServer;
import com.abelatox.raycraft.lib.Strings;
import com.abelatox.raycraft.lib.Utils;
import com.abelatox.raycraft.network.payloads.*;
import com.abelatox.raycraft.sounds.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.component.SwingAnimation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Optional;
import java.util.function.Consumer;

public class ServerPayloadHandler {

	private static final double MAX_LOCK_ON_RANGE = 48.0;
	private static final float CURVE_ANGLE = 35.0F * Mth.DEG_TO_RAD;
	private static final double FULL_CURVE_DISTANCE = 10.0;

	private static void updateCapabilities(IPayloadContext context, Consumer<IPlayerCapabilities> change) {
		context.enqueueWork(() -> {
			change.accept(ModCapabilities.get(context.player()));
			ModCapabilities.sync(context.player());
		});
	}

	public static void handleSetModel(final SetModelPayload payload, final IPayloadContext context) {
		updateCapabilities(context, props -> props.setPlayerType(payload.model()));
	}

	public static void handleSetCharging(final SetChargingPayload payload, final IPayloadContext context) {
		updateCapabilities(context, props -> props.setCharging(payload.charging()));
	}

	public static void handleSetBarrelFlight(final SetBarrelFlightPayload payload, final IPayloadContext context) {
		context.enqueueWork(() -> BarrelFlight.onFlightPayload(context.player(), payload.flying(), payload.letGo()));
	}

	public static void handleSetSwinging(final SetSwingingPayload payload, final IPayloadContext context) {
		context.enqueueWork(() -> GrappleServer.setSwinging(context.player(), payload.swinging()));
	}

	public static void handleSetGliding(final SetGlidingPayload payload, final IPayloadContext context) {
		updateCapabilities(context, props -> props.setGliding(payload.gliding()));
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
					Entity target = getLockOnTarget(player, payload.targetId());
					BlockPos cage = getLockOnCage(player, payload.cageTarget());
					int curve = Mth.clamp(payload.curve(), -1, 1);
					if (target != null) {
						aimAt(shot, target.getBoundingBox().getCenter(), curve);
						shot.setHomingTarget(target);
					} else if (cage != null) {
						aimAt(shot, Vec3.atCenterOf(cage), curve);
						shot.setHomingTarget(cage);
					} else {
						shot.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 1F, 0);
					}
					player.level().addFreshEntity(shot);
					if (player.getVehicle() instanceof EntityPlum plum) {
						plum.pushFrom(shot.getDeltaMovement().reverse());
					}
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

	private static Entity getLockOnTarget(ServerPlayer player, int targetId) {
		if (targetId < 0) {
			return null;
		}
		Entity entity = player.level().getEntity(targetId);
		boolean lockable = entity instanceof LivingEntity living ? living != player && living.isAlive() : entity instanceof EntityLum lum && lum.isPurple();
		if (lockable && player.distanceToSqr(entity) <= MAX_LOCK_ON_RANGE * MAX_LOCK_ON_RANGE) {
			return entity;
		}
		return null;
	}

	private static BlockPos getLockOnCage(ServerPlayer player, Optional<BlockPos> cage) {
		return cage.filter(pos -> player.level().getBlockState(pos).getBlock() instanceof BlockCage && player.distanceToSqr(Vec3.atCenterOf(pos)) <= MAX_LOCK_ON_RANGE * MAX_LOCK_ON_RANGE).orElse(null);
	}

	private static void aimAt(RaycraftProjectile shot, Vec3 aim, int curve) {
		Vec3 toTarget = aim.subtract(shot.position());
		float angle = CURVE_ANGLE * (float) Mth.clamp(toTarget.length() / FULL_CURVE_DISTANCE, 0.3, 1.0);
		// Vec3#yRot turns left for positive angles, so strafing right uses a negative angle.
		Vec3 direction = toTarget.normalize().yRot(-curve * angle);
		shot.shoot(direction.x, direction.y, direction.z, 1F, 0F);
	}

	public static void handlePlaySound(final PlaySoundPayload payload, final IPayloadContext context) {
		context.enqueueWork(() -> {
			ServerPlayer player = (ServerPlayer) context.player();
			player.level().playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.PIRATE_SHOT_1.get(), SoundSource.PLAYERS, 1F, 1F);
		});
	}

	public static void handleSetPortalDestination(final SetPortalDestinationPayload payload, final IPayloadContext context) {
		context.enqueueWork(() -> {
			ServerPlayer player = (ServerPlayer) context.player();
			ResourceKey<Level> level = ResourceKey.create(Registries.DIMENSION, Identifier.parse(payload.level()));
			if (player.isCreative() && player.level().getEntity(payload.portalId()) instanceof EntitySpiralPortal portal && player.distanceToSqr(portal) < 64 * 64 && player.level().getServer().getLevel(level) != null) {
				portal.setDestination(new Vec3(payload.x(), payload.y(), payload.z()), level);
				portal.setRed(payload.red());
				resizeKeepingCentre(portal, payload.size());
			}
		});
	}

	/**
	 * The portal's position is the bottom of its box: move it so the swirl keeps its centre when it grows or shrinks.
	 */
	private static void resizeKeepingCentre(EntitySpiralPortal portal, float size) {
		Vec3 centre = portal.getBoundingBox().getCenter();
		portal.setSize(size);
		portal.setPos(centre.x, centre.y - portal.getBbHeight() / 2.0, centre.z);
	}

	public static void handleSetShellRoll(final SetShellRollPayload payload, final IPayloadContext context) {
		context.enqueueWork(() -> {
			if (context.player().level().getEntity(payload.shellId()) instanceof EntityFlyingShell shell && shell.getControllingPassenger() == context.player() && Float.isFinite(payload.roll())) {
				shell.setRollFromRider(payload.roll());
			}
		});
	}
}
