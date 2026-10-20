package com.abelatox.raycraft.entities;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public abstract class RaycraftProjectile extends ThrowableProjectile {
	private static final EntityDataAccessor<Integer> HOMING_TARGET = SynchedEntityData.defineId(RaycraftProjectile.class, EntityDataSerializers.INT);

	private static final double HOMING_STRENGTH_START = 0.12;
	private static final double HOMING_STRENGTH_PER_TICK = 0.03;
	private static final double DIRECT_HIT_RANGE = 3.0;
	private static final double MAX_LEAD = 3.0;

	protected RaycraftProjectile(EntityType<? extends RaycraftProjectile> type, Level level) {
		super(type, level);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		builder.define(HOMING_TARGET, -1);
	}

	public void setHomingTarget(LivingEntity target) {
		this.getEntityData().set(HOMING_TARGET, target.getId());
	}

	@Override
	public void tick() {
		steerTowardsTarget();
		super.tick();
	}

	private void steerTowardsTarget() {
		int targetId = this.getEntityData().get(HOMING_TARGET);
		if (targetId < 0) {
			return;
		}

		Entity target = level().getEntity(targetId);
		if (!(target instanceof LivingEntity living) || !living.isAlive()) {
			if (!level().isClientSide()) {
				this.getEntityData().set(HOMING_TARGET, -1);
			}
			return;
		}

		Vec3 velocity = getDeltaMovement();
		double speed = velocity.length();
		if (speed < 1.0E-4) {
			return;
		}

		Vec3 center = living.getBoundingBox().getCenter();
		double distance = center.distanceTo(position());

		// Aim where the target will be when the shot gets there. Horizontal only: a grounded mob's
		// velocity always carries gravity, which would aim the shot into the floor.
		Vec3 motion = living.getDeltaMovement();
		Vec3 lead = new Vec3(motion.x, 0.0, motion.z).scale(distance / speed);
		if (lead.length() > MAX_LEAD) {
			lead = lead.normalize().scale(MAX_LEAD);
		}
		Vec3 desired = center.add(lead).subtract(position()).normalize().scale(speed);

		double strength = distance < DIRECT_HIT_RANGE ? 1.0 : Math.min(1.0, HOMING_STRENGTH_START + HOMING_STRENGTH_PER_TICK * tickCount);
		setDeltaMovement(velocity.add(desired.subtract(velocity).scale(strength)).normalize().scale(speed));
	}
}
