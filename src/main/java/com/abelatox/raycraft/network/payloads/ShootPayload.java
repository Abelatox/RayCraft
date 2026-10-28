package com.abelatox.raycraft.network.payloads;

import com.abelatox.raycraft.blocks.BlockCage;
import com.abelatox.raycraft.capabilities.IPlayerCapabilities;
import com.abelatox.raycraft.capabilities.ModCapabilities;
import com.abelatox.raycraft.entities.EntityLum;
import com.abelatox.raycraft.entities.EntityPlum;
import com.abelatox.raycraft.entities.RaycraftProjectile;
import com.abelatox.raycraft.lib.Reference;
import com.abelatox.raycraft.lib.Strings;
import com.abelatox.raycraft.lib.Utils;
import com.abelatox.raycraft.sounds.ModSounds;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.component.SwingAnimation;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Optional;

public record ShootPayload(boolean charged, int targetId, Optional<BlockPos> cageTarget, int curve) implements CustomPacketPayload {

	private static final double MAX_LOCK_ON_RANGE = 48.0;
	private static final float CURVE_ANGLE = 35.0F * Mth.DEG_TO_RAD;
	private static final double FULL_CURVE_DISTANCE = 10.0;

	public static final Type<ShootPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(Reference.MODID, "shoot"));

	public static final StreamCodec<ByteBuf, ShootPayload> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.BOOL, ShootPayload::charged,
			ByteBufCodecs.VAR_INT, ShootPayload::targetId,
			ByteBufCodecs.optional(BlockPos.STREAM_CODEC), ShootPayload::cageTarget,
			ByteBufCodecs.VAR_INT, ShootPayload::curve,
			ShootPayload::new
	);

	public void handle(IPayloadContext context) {
		context.enqueueWork(() -> {
			ServerPlayer player = (ServerPlayer) context.player();
			IPlayerCapabilities props = ModCapabilities.get(player);
			if (props.hasCustomPlayerType() && player.getMainHandItem().isEmpty()) {
				RaycraftProjectile shot = Utils.getEntityShot(player, charged);
				if (shot != null) {
					shot.setOwner(player);
					// Centre the shot on the eye line (its position is the bottom of its hitbox).
					shot.setPos(shot.getX(), player.getEyeY() - 0.1 - shot.getBbHeight() / 2.0, shot.getZ());
					Entity target = getLockOnTarget(player, targetId);
					BlockPos cage = getLockOnCage(player, cageTarget);
					int side = Mth.clamp(curve, -1, 1);
					if (target != null) {
						aimAt(shot, target.getBoundingBox().getCenter(), side);
						shot.setHomingTarget(target);
					} else if (cage != null) {
						aimAt(shot, Vec3.atCenterOf(cage), side);
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
						player.level().playSound(null, player.blockPosition(), Utils.getShootSound(player, charged), SoundSource.MASTER, 1F, 1F);
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

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
