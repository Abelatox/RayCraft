package com.abelatox.raycraft.entities;

import com.abelatox.raycraft.sounds.ModSounds;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.network.IPacket;
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

public class EntityPirateShot extends ThrowableEntity {

	int bounces;
	int maxBounces;
	int lvl = 0;
	float power;
	int maxTicks = 60;

	public EntityPirateShot(World world) {
		super(ModEntities.TYPE_PIRATE_SHOT, world);
		this.preventEntitySpawning = true;
	}
	
	public EntityPirateShot(World worldIn, LivingEntity throwerIn) {
		super(ModEntities.TYPE_PIRATE_SHOT, throwerIn, worldIn);
		this.maxBounces = 0;
		this.power = 2;
		this.maxTicks = 60;
	}
	
	public EntityPirateShot(EntityType<EntityPirateShot> type, World world) {
		super(type, world);
		this.preventEntitySpawning = true;
	}

	public EntityPirateShot(EntityType<EntityPirateShot> type, World worldIn, LivingEntity throwerIn, int lvl) {
		super(type, throwerIn, worldIn);
		this.preventEntitySpawning = true;
		this.lvl = lvl;
		this.maxBounces = 0;
		this.power = 2;
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
				/*if (this.func_234616_v_() instanceof PlayerEntity) {
					target.attackEntityFrom(DamageSource.causePlayerDamage((PlayerEntity) this.func_234616_v_(), power);
				} else {
					target.attackEntityFrom(DamageSource.causeThrownDamage(this, this.func_234616_v_()), power);
				}*/

			} else { // Block (not ERTR)
				if (brtResult != null && rtRes.getType() == Type.BLOCK) {
					world.playSound(null, getPosition(), ModSounds.fistBounce, SoundCategory.MASTER, 1F, 1F);

					bounces++;
					if (brtResult.getFace() == Direction.NORTH || brtResult.getFace() == Direction.SOUTH) {
						this.setMotion(getMotion().x, getMotion().y, -getMotion().z);
					} else if (brtResult.getFace() == Direction.EAST || brtResult.getFace() == Direction.WEST) {
						this.setMotion(-getMotion().x, getMotion().y, getMotion().z);
					} else if (brtResult.getFace() == Direction.UP || brtResult.getFace() == Direction.DOWN) {
						this.setMotion(getMotion().x, -getMotion().y, getMotion().z);
					}
				} else {
					remove();
				}
			}
		}
	}

	private void explode() {
		// System.out.println("Boom");
		world.createExplosion(this, getPosX(), getPosY(), getPosZ(), power / 4, Mode.NONE);
		this.remove();
	}

	@Override
	protected void registerData() {
		// TODO Auto-generated method stub

	}
}
