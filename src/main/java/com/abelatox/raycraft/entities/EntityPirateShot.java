package com.abelatox.raycraft.entities;

import com.abelatox.raycraft.sounds.ModSounds;

import net.minecraft.core.Direction;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class EntityPirateShot extends RaycraftProjectile {

	int bounces;
	int maxBounces;
	float power;
	int maxTicks = 60;

	public EntityPirateShot(EntityType<? extends EntityPirateShot> type, Level level) {
		super(type, level);
	}

	public EntityPirateShot(Level level, LivingEntity thrower) {
		this(ModEntities.PIRATE_SHOT.get(), level);
		this.setOwner(thrower);
		this.setPos(thrower.getX(), thrower.getEyeY() - 0.1, thrower.getZ());
		this.maxBounces = 0;
		this.power = 2;
		this.maxTicks = 60;
	}

	@Override
	protected double getDefaultGravity() {
		return 0F;
	}

	@Override
	public void tick() {
		if (this.tickCount > maxTicks) {
			this.discard();
		}
		super.tick();
	}

	@Override
	protected void onHit(HitResult hitResult) {
		if (!level().isClientSide()) {
			if (bounces >= maxBounces) {
				this.discard();
			}

			EntityHitResult entityResult = hitResult instanceof EntityHitResult eh ? eh : null;
			BlockHitResult blockResult = hitResult instanceof BlockHitResult bh ? bh : null;

			if (entityResult != null && entityResult.getEntity() instanceof LivingEntity target) {
				if (target != this.getOwner() && level() instanceof ServerLevel serverLevel) {
					target.hurtServer(serverLevel, this.damageSources().thrown(this, this.getOwner()), power);
					discard();
				}
			} else {
				if (blockResult != null && hitResult.getType() == HitResult.Type.BLOCK) {
					level().playSound(null, blockPosition(), ModSounds.FIST_BOUNCE.get(), SoundSource.MASTER, 1F, 1F);

					bounces++;
					Direction face = blockResult.getDirection();
					if (face == Direction.NORTH || face == Direction.SOUTH) {
						this.setDeltaMovement(getDeltaMovement().x, getDeltaMovement().y, -getDeltaMovement().z);
					} else if (face == Direction.EAST || face == Direction.WEST) {
						this.setDeltaMovement(-getDeltaMovement().x, getDeltaMovement().y, getDeltaMovement().z);
					} else if (face == Direction.UP || face == Direction.DOWN) {
						this.setDeltaMovement(getDeltaMovement().x, -getDeltaMovement().y, getDeltaMovement().z);
					}
				} else {
					discard();
				}
			}
		}
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
	}
}
