package com.abelatox.raycraft.entities;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;

public class EntityBaseFist extends EntityThrowable {

	int bounces;
	int maxBounces;
	int lvl = 0;
	float power;
	boolean charged = false;
	//private static final DataParameter<Boolean> CHARGED = EntityDataManager.createKey(EntityFist.class, DataSerializers.BOOLEAN);

	public EntityBaseFist(World world) {
		super(ModEntities.TYPE_FIST, world);
		this.preventEntitySpawning = true;
		this.isImmuneToFire = true;
		this.setSize(0.3F, 0.3F);
	}

	public EntityBaseFist(EntityType<EntityBaseFist> type, World worldIn, EntityLivingBase throwerIn, int lvl, boolean charged) {
		super(type, throwerIn, worldIn);
		this.preventEntitySpawning = true;
		this.isImmuneToFire = true;
		this.setSize(0.3F, 0.3F);
		this.lvl = lvl;
		this.charged = charged;
		this.maxBounces = 0;
		this.power = 2;
	}

	@Override
	protected float getGravityVelocity() {
		return 0F;
	}

	@Override
	public void tick() {
		if (this.ticksExisted > 60) {
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
				System.out.println(power);
				if (charged && lvl >= 2) { // if is charged && high level do explosion
					explode();
				} else {
					remove();
				}
			} else {
				if (result.type == Type.BLOCK) {
					if (charged && lvl >= 2) { // if is charged && high level do explosion
						explode();
					} else {
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
		world.createExplosion(this, posX, posY, posZ, power, false);
		this.remove();
	}

	private void loadData() {
		switch (lvl) {
		case 0: // First punch, no bounces
			maxBounces = 0;
			power = 2;
			break;
		case 1: // Blue punch, grab purple lums, 1 bounce
			maxBounces = 1;
			power = 4;
			break;
		case 2: // Yellow punch, 2 bounces, explode if charged
			maxBounces = 2;
			power = 8;
			break;
		case 3: // Golden punch, 3 bounces, explode if charged
			maxBounces = 3;
			power = 20;
			break;
		}

		if (charged) {
			power *= 2;
		}
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
