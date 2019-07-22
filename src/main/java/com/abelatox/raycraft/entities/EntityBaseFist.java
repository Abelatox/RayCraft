package com.abelatox.raycraft.entities;

import com.abelatox.raycraft.sounds.ModSounds;

import net.minecraft.client.renderer.FaceDirection;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.Explosion.Mode;
import net.minecraft.world.World;

public class EntityBaseFist extends ThrowableEntity {

	int bounces;
	int maxBounces;
	int lvl = 0;
	float power;
	boolean explosion = false;
	int maxTicks = 60;
	//private static final DataParameter<Boolean> CHARGED = EntityDataManager.createKey(EntityFist.class, DataSerializers.BOOLEAN);

	public EntityBaseFist(World world) {
		super(ModEntities.TYPE_FIST_0, world);
		this.preventEntitySpawning = true;
		//this.setSize(0.3F, 0.3F);
	}

	public EntityBaseFist(EntityType<EntityBaseFist> type, World worldIn, LivingEntity throwerIn, int lvl) {
		super(type, throwerIn, worldIn);
		this.preventEntitySpawning = true;
		//this.setSize(0.3F, 0.3F);
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
	protected void onImpact(RayTraceResult rtRes) {
		if (!world.isRemote) {
			if (bounces >= maxBounces) {
				this.remove();
			}
			EntityRayTraceResult result = null;
			if(rtRes instanceof EntityRayTraceResult) {
				result = (EntityRayTraceResult) rtRes;
			}
			if (result != null && result.getEntity() != null && result.getEntity() instanceof LivingEntity) {
				LivingEntity target = (LivingEntity) result.getEntity();
				target.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), power);
				//System.out.println(power);
				if (explosion) { // if is charged && high level do explosion
					explode();
				} else {
					remove();
				}
			} else {
				if (result != null && result.getType() == Type.BLOCK) {
					if (explosion) { // if is charged && high level do explosion
						explode();
					} else {
						world.playSound(null, getPosition(), ModSounds.fistBounce, SoundCategory.MASTER, 1F, 1F);

						bounces++;
						/*if (result.sideHit == FaceDirection.NORTH || result.sideHit == FaceDirection.SOUTH) {
							this.motionZ = -this.motionZ;
						}
						if (result.sideHit == FaceDirection.EAST || result.sideHit == FaceDirection.WEST) {
							this.motionX = -this.motionX;
						}
						if (result.sideHit == FaceDirection.UP || result.sideHit == FaceDirection.DOWN) {
							this.motionY = -this.motionY;
						}*/
					}
				}
			}
		}
	}

	private void explode() {
		//System.out.println("Boom");
		world.createExplosion(this, posX, posY, posZ, power/4, Mode.NONE);
		this.remove();
	}

	@Override
	protected void registerData() {
		// TODO Auto-generated method stub
		
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
