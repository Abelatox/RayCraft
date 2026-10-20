package com.abelatox.raycraft.entities;

import com.abelatox.raycraft.sounds.ModSounds;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.List;

public class EntityFist extends RaycraftProjectile {

	private static final EntityDataAccessor<Integer> LEVEL = SynchedEntityData.defineId(EntityFist.class, EntityDataSerializers.INT);

	public static final int GIANT_LEVEL = 4;
	public static final float GIANT_SCALE = 4.0F;

	int bounces;
	int maxBounces;
	float power;
	boolean explosion = false;
	int maxTicks = 60;

	public EntityFist(EntityType<? extends EntityFist> type, Level level) {
		super(type, level);
	}

	public EntityFist(Level level, Player player) {
		this(ModEntities.FIST.get(), level);
		this.setOwner(player);
		this.setPos(player.getX(), player.getEyeY() - 0.1, player.getZ());
	}

	@Override
	public EntityDimensions getDimensions(Pose pose) {
		EntityDimensions dimensions = super.getDimensions(pose);
		return getLvl() == GIANT_LEVEL ? dimensions.scale(GIANT_SCALE) : dimensions;
	}

	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
		super.onSyncedDataUpdated(accessor);
		if (LEVEL.equals(accessor)) {
			refreshDimensions();
		}
	}

	@Override
	protected double getDefaultGravity() {
		return 0F;
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(LEVEL, 0);
	}

	@Override
	public void tick() {
		if (this.tickCount > maxTicks) {
			this.discard();
		}

		if (getLvl() >= 3) {
			level().addParticle(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, 1F, 1F, 0F), getX(), getY(), getZ(), 0, 0, 0);
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
					DamageSource damageSource = this.damageSources().thrown(this, this.getOwner());
					target.hurtServer(serverLevel, damageSource, power);
					level().playSound(null, blockPosition(), ModSounds.FIST_HIT.get(), SoundSource.PLAYERS, 1F, 1F);
					if (explosion) {
						explode();
					} else {
						discard();
					}
				}
			} else {
				if (blockResult != null && hitResult.getType() == HitResult.Type.BLOCK) {
					if (explosion) {
						explode();
					} else {
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
					}
				} else {
					discard();
				}
			}
		}
	}

	private void explode() {
		if (level() instanceof ServerLevel serverLevel) {
			serverLevel.explode(this, getX(), getY(), getZ(), 0, Level.ExplosionInteraction.NONE);

			List<Entity> entities = level().getEntities(this, getBoundingBox().inflate(4.0D, 4.0D, 4.0D).move(-2.0D, -2.0D, -2.0D));
			for (Entity e : entities) {
				if (e instanceof LivingEntity living && e != this.getOwner()) {
					DamageSource damageSource = this.damageSources().thrown(this, this.getOwner());
					living.hurtServer(serverLevel, damageSource, power);
				}
			}
		}

		this.discard();
	}

	public void setMaxBounces(int maxBounces) {
		this.maxBounces = maxBounces;
	}

	public int getLvl() {
		return this.getEntityData().get(LEVEL);
	}

	public void setLvl(int lvl) {
		this.getEntityData().set(LEVEL, lvl);
	}

	public void setPower(float power) {
		this.power = power;
	}

	public void setExplosion(boolean explosion) {
		this.explosion = explosion;
	}

	public void setMaxTicks(int maxTicks) {
		this.maxTicks = maxTicks;
	}

	@Override
	protected void addAdditionalSaveData(ValueOutput output) {
		output.putInt("lvl", this.getLvl());
	}

	@Override
	protected void readAdditionalSaveData(ValueInput input) {
		this.setLvl(input.getIntOr("lvl", 0));
	}
}
