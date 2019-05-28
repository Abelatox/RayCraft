package com.abelatox.raycraft.entities;

import com.abelatox.raycraft.sounds.ModSounds;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;

public class EntityBaseFist extends EntityThrowable {

	int bounces;
	int maxBounces;
	int lvl = 0;
	float power;
	boolean explosion = false;
	int maxTicks = 60;
	//private static final DataParameter<Boolean> CHARGED = EntityDataManager.createKey(EntityFist.class, DataSerializers.BOOLEAN);

	public EntityBaseFist(World world) {
		super(ModEntities.TYPE_FIST, world);
		this.preventEntitySpawning = true;
		this.isImmuneToFire = true;
		this.setSize(0.3F, 0.3F);
	}

	public EntityBaseFist(EntityType<EntityBaseFist> type, World worldIn, EntityLivingBase throwerIn, int lvl) {
		super(type, throwerIn, worldIn);
		this.preventEntitySpawning = true;
		this.isImmuneToFire = true;
		this.setSize(0.3F, 0.3F);
		this.lvl = lvl;
		this.maxBounces = 0;
		this.power = 2;
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

		super.tick();
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		if (!world.isRemote) {
			if (bounces >= maxBounces) {
				this.remove();
			}
			if (result.entity != null && result.entity instanceof EntityLivingBase) {
				EntityLivingBase target = (EntityLivingBase) result.entity;
				target.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), power);
				//System.out.println(power);
				if (explosion) { // if is charged && high level do explosion
					explode();
				} else {
					remove();
				}
			} else {
				if (result.type == Type.BLOCK) {
					if (explosion) { // if is charged && high level do explosion
						explode();
					} else {
						world.playSound(null, getPosition(), ModSounds.fistBounce, SoundCategory.MASTER, 1F, 1F);

						bounces++;
						if (result.sideHit == EnumFacing.NORTH || result.sideHit == EnumFacing.SOUTH) {
							this.motionZ = -this.motionZ;
						}
						if (result.sideHit == EnumFacing.EAST || result.sideHit == EnumFacing.WEST) {
							this.motionX = -this.motionX;
						}
						if (result.sideHit == EnumFacing.UP || result.sideHit == EnumFacing.DOWN) {
							this.motionY = -this.motionY;
						}
					}
				}
			}
		}
	}

	private void explode() {
		//System.out.println("Boom");
		world.createExplosion(this, posX, posY, posZ, power/4, false);
		this.remove();
	}

/*	@Override
	public void writeAdditional(NBTTagCompound compound) {
		compound.setBoolean("Charged", this.getCharged());
	}

	@Override
	public void readAdditional(NBTTagCompound compound) {
		this.setCharged(compound.getBoolean("Charged"));
	}

	public void setCharged(boolean charged) {
		this.dataManager.set(CHARGED, charged);
		this.charged = charged;
	}

	@Override
	public void notifyDataManagerChange(DataParameter<?> key) {
		if (CHARGED.equals(key)) {
			this.charged = this.getChargedDataManager();
		}
	}

	@Override
	protected void registerData() {
		this.dataManager.register(CHARGED, false);
	}

	public boolean getChargedDataManager() {
		return this.dataManager.get(CHARGED);
	}

	public boolean getCharged() {
		return this.charged;
	}*/
}
