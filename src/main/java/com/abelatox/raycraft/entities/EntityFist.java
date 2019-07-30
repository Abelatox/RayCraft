package com.abelatox.raycraft.entities;

import com.abelatox.raycraft.sounds.ModSounds;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.Explosion.Mode;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class EntityFist extends ThrowableEntity {
	private static final DataParameter<Integer> LEVEL = EntityDataManager.createKey(EntityFist.class, DataSerializers.VARINT);
	int bounces;
	int maxBounces;

	int lvl = 0;
	float power;
	boolean explosion = false;
	int maxTicks = 60;

	public EntityFist(World world) {
		super(ModEntities.TYPE_FIST, world);
		this.preventEntitySpawning = true;
		// this.setSize(0.3F, 0.3F);
	}

	public EntityFist(EntityType<EntityFist> type, World world) {
		super(type, world);
		this.preventEntitySpawning = true;
		// this.setSize(0.3F, 0.3F);
	}

	/*
	 * public EntityFist(EntityType<EntityFist> type, World worldIn, LivingEntity
	 * throwerIn, int lvl) { super(type, throwerIn, worldIn);
	 * this.preventEntitySpawning = true; // this.setSize(0.3F, 0.3F); this.lvl =
	 * lvl; this.maxBounces = 0; this.power = 2; }
	 */

	public EntityFist(World world, PlayerEntity player) {
		super(ModEntities.TYPE_FIST, player, world);
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	protected float getGravityVelocity() {
		return 0F;
	}
	
	@Override
	public void tick() {
		if (this.ticksExisted > maxTicks) {
			this.remove();
		}

		if(getLvl() >= 3) {
			world.addParticle(ParticleTypes.ENTITY_EFFECT, posX, posY, posZ, 1, 1, 0);
		}
		super.tick();
	}

	@Override
	protected void onImpact(RayTraceResult rtRes) {
		if (!world.isRemote) {
			if (bounces >= maxBounces) {
				this.remove();
			}
			EntityRayTraceResult ertResult = null;
			BlockRayTraceResult brtResult = null;

			if (rtRes instanceof EntityRayTraceResult) {
				ertResult = (EntityRayTraceResult) rtRes;
			}

			if (rtRes instanceof BlockRayTraceResult) {
				brtResult = (BlockRayTraceResult) rtRes;
			}

			if (ertResult != null && ertResult.getEntity() != null && ertResult.getEntity() instanceof LivingEntity) {
				LivingEntity target = (LivingEntity) ertResult.getEntity();
				target.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), power);
				// System.out.println(power);
				if (explosion) { // if is charged && high level do explosion
					explode();
				} else {
					remove();
				}
			} else { // Block (not ERTR)
				if (brtResult != null && rtRes.getType() == Type.BLOCK) {
					if (explosion) { // if is charged && high level do explosion
						explode();
					} else {
						world.playSound(null, getPosition(), ModSounds.fistBounce, SoundCategory.MASTER, 1F, 1F);

						bounces++;
						if (brtResult.getFace() == Direction.NORTH || brtResult.getFace() == Direction.SOUTH) {
							this.setMotion(getMotion().x, getMotion().y, -getMotion().z);
						} else if (brtResult.getFace() == Direction.EAST || brtResult.getFace() == Direction.WEST) {
							this.setMotion(-getMotion().x, getMotion().y, getMotion().z);
						} else if (brtResult.getFace() == Direction.UP || brtResult.getFace() == Direction.DOWN) {
							this.setMotion(getMotion().x, -getMotion().y, getMotion().z);
						}
					}
				} else {
					remove();
				}
			}
		}
	}

	private void explode() {
		// System.out.println("Boom");
		world.createExplosion(this, posX, posY, posZ, power / 4, Mode.NONE);
		this.remove();
	}

	public int getMaxBounces() {
		return maxBounces;
	}

	public void setMaxBounces(int maxBounces) {
		this.maxBounces = maxBounces;
	}

	public int getLvl() {
		return lvl;
	}

	public void setLvl(int lvl) {
		this.dataManager.set(LEVEL, lvl);
		this.lvl = lvl;
	}

	public float getPower() {
		return power;
	}

	public void setPower(float power) {
		this.power = power;
	}

	public boolean isExplosion() {
		return explosion;
	}

	public void setExplosion(boolean explosion) {
		this.explosion = explosion;
	}

	public int getMaxTicks() {
		return maxTicks;
	}

	public void setMaxTicks(int maxTicks) {
		this.maxTicks = maxTicks;
	}

	@Override
	public void writeAdditional(CompoundNBT compound) {
		compound.putInt("lvl", this.getLvl());
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		this.setLvl(compound.getInt("lvl"));
	}

	@Override
	public void notifyDataManagerChange(DataParameter<?> key) {
		if (key.equals(LEVEL)) {
			this.lvl = this.getChargedDataManager();
		}
	}

	@Override
	protected void registerData() {
		this.dataManager.register(LEVEL, 0);
	}

	public int getChargedDataManager() {
		return this.dataManager.get(LEVEL);
	}
}
