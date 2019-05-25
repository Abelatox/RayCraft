package com.abelatox.raycraft.entities;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;

public class EntityFist extends EntityThrowable {

	int bounces;
	int maxBounces;
	int lvl = 0;
	float power;
	private boolean charged = false;

	public EntityFist(World world) {
		super(ModEntities.TYPE_FIST, world);
		this.preventEntitySpawning = true;
		this.isImmuneToFire = true;
		this.setSize(0.3F, 0.3F);
		loadData();
	}

	public EntityFist(World worldIn, EntityLivingBase throwerIn, int lvl, boolean charged) {
		super(ModEntities.TYPE_FIST, throwerIn, worldIn);
		this.preventEntitySpawning = true;
		this.isImmuneToFire = true;
		this.setSize(0.3F, 0.3F);
		this.lvl = lvl;
		this.charged = charged;
		loadData();
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
		if (bounces >= 2) {
			this.remove();
		}
		super.tick();
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		if (!world.isRemote) {
			if (result.entity != null && result.entity instanceof EntityLivingBase) {
				EntityLivingBase target = (EntityLivingBase) result.entity;
				target.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), power);
				System.out.println(power);
				if (charged && lvl >= 2) { //if is charged && high level do explosion
					explode();
				} else {
					remove();
				}
			} else {
				if (result.type == Type.BLOCK) {
					if (charged && lvl >= 2) { //if is charged && high level do explosion
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
		System.out.println("Boom");
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
		
		if(charged) {
			power*=2;
		}
	}

}
