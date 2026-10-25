package com.abelatox.raycraft.entities;

import com.abelatox.raycraft.blocks.BlockCage;
import com.abelatox.raycraft.blocks.BlockTimerSwitch;
import com.abelatox.raycraft.sounds.ModSounds;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public abstract class RaycraftProjectile extends ThrowableProjectile {
	private static final EntityDataAccessor<Integer> HOMING_TARGET = SynchedEntityData.defineId(RaycraftProjectile.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Optional<BlockPos>> HOMING_CAGE = SynchedEntityData.defineId(RaycraftProjectile.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);

	private static final double HOMING_STRENGTH_START = 0.12;
	private static final double HOMING_STRENGTH_PER_TICK = 0.03;
	private static final double DIRECT_HIT_RANGE = 3.0;
	private static final double MAX_LEAD = 3.0;

	protected float power;
	private int maxBounces;
	private int maxTicks = 60;
	private int bounces;

	protected RaycraftProjectile(EntityType<? extends RaycraftProjectile> type, Level level) {
		super(type, level);
	}

	protected RaycraftProjectile(EntityType<? extends RaycraftProjectile> type, Level level, LivingEntity shooter) {
		this(type, level);
		setOwner(shooter);
		setPos(shooter.getX(), shooter.getEyeY() - 0.1, shooter.getZ());
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		builder.define(HOMING_TARGET, -1);
		builder.define(HOMING_CAGE, Optional.empty());
	}

	@Override
	protected double getDefaultGravity() {
		return 0.0;
	}

	public void setPower(float power) {
		this.power = power;
	}

	public void setMaxBounces(int maxBounces) {
		this.maxBounces = maxBounces;
	}

	public void setMaxTicks(int maxTicks) {
		this.maxTicks = maxTicks;
	}

	@Override
	public void tick() {
		if (tickCount > maxTicks) {
			discard();
		}
		steerTowardsTarget();
		super.tick();
	}

	@Override
	protected void onHit(HitResult hitResult) {
		if (hitInteractiveBlock(hitResult) || hitInteractiveEntity(hitResult) || !(level() instanceof ServerLevel serverLevel)) {
			return;
		}
		if (hitResult instanceof EntityHitResult entityHit) {
			if (!(entityHit.getEntity() instanceof LivingEntity target)) {
				discard();
			} else if (target != getOwner()) {
				target.hurtServer(serverLevel, damageSources().thrown(this, getOwner()), power);
				onImpact();
			}
		} else if (hitResult instanceof BlockHitResult blockHit && hitResult.getType() == HitResult.Type.BLOCK) {
			onHitBlockFace(blockHit);
		} else {
			discard();
		}
	}

	protected void onImpact() {
		discard();
	}

	protected void onHitBlockFace(BlockHitResult hit) {
		level().playSound(null, blockPosition(), ModSounds.FIST_BOUNCE.get(), SoundSource.MASTER, 1F, 1F);
		if (bounces++ >= maxBounces) {
			discard();
			return;
		}
		Direction.Axis axis = hit.getDirection().getAxis();
		setDeltaMovement(getDeltaMovement().multiply(axis == Direction.Axis.X ? -1 : 1, axis == Direction.Axis.Y ? -1 : 1, axis == Direction.Axis.Z ? -1 : 1));
	}

	protected boolean hitInteractiveBlock(HitResult result) {
		if (result instanceof BlockHitResult blockHit && level() instanceof ServerLevel serverLevel) {
			BlockState state = serverLevel.getBlockState(blockHit.getBlockPos());
			if (state.getBlock() instanceof BlockCage) {
				BlockCage.hit(serverLevel, blockHit.getBlockPos(), state, getOwner() instanceof Player player ? player : null, getCageDamage());
			} else if (state.getBlock() instanceof BlockTimerSwitch) {
				BlockTimerSwitch.activate(serverLevel, blockHit.getBlockPos(), state);
			} else {
				return false;
			}
			discard();
			return true;
		}
		return false;
	}

	protected boolean hitInteractiveEntity(HitResult result) {
		if (result instanceof EntityHitResult entityHit && entityHit.getEntity() instanceof EntityPlum plum) {
			if (!plum.hasPassenger(getOwner()) && !level().isClientSide()) {
				plum.pushFrom(getDeltaMovement());
				discard();
			}
			return true;
		}
		return false;
	}

	protected int getCageDamage() {
		return 1;
	}

	public void setHomingTarget(Entity target) {
		this.getEntityData().set(HOMING_TARGET, target.getId());
	}

	public void setHomingTarget(BlockPos cage) {
		this.getEntityData().set(HOMING_CAGE, Optional.of(cage));
	}

	private Vec3 getHomingAim(double speed) {
		int targetId = this.getEntityData().get(HOMING_TARGET);
		if (targetId >= 0) {
			Entity target = level().getEntity(targetId);
			if (target != null && target.isAlive()) {
				Vec3 center = target.getBoundingBox().getCenter();
				// Aim where the target will be when the shot gets there. Horizontal only: a grounded mob's
				// velocity always carries gravity, which would aim the shot into the floor.
				Vec3 motion = target.getDeltaMovement();
				Vec3 lead = new Vec3(motion.x, 0.0, motion.z).scale(center.distanceTo(position()) / speed);
				if (lead.length() > MAX_LEAD) {
					lead = lead.normalize().scale(MAX_LEAD);
				}
				return center.add(lead);
			}
			if (!level().isClientSide()) {
				this.getEntityData().set(HOMING_TARGET, -1);
			}
			return null;
		}

		Optional<BlockPos> cage = this.getEntityData().get(HOMING_CAGE);
		if (cage.isPresent()) {
			if (level().getBlockState(cage.get()).getBlock() instanceof BlockCage) {
				return Vec3.atCenterOf(cage.get());
			}
			if (!level().isClientSide()) {
				this.getEntityData().set(HOMING_CAGE, Optional.empty());
			}
		}
		return null;
	}

	private void steerTowardsTarget() {
		Vec3 velocity = getDeltaMovement();
		double speed = velocity.length();
		Vec3 aim = speed < 1.0E-4 ? null : getHomingAim(speed);
		if (aim == null) {
			return;
		}
		Vec3 desired = aim.subtract(position()).normalize().scale(speed);
		double strength = aim.distanceTo(position()) < DIRECT_HIT_RANGE ? 1.0 : Math.min(1.0, HOMING_STRENGTH_START + HOMING_STRENGTH_PER_TICK * tickCount);
		setDeltaMovement(velocity.add(desired.subtract(velocity).scale(strength)).normalize().scale(speed));
	}
}
