package com.abelatox.raycraft.entities;

import java.util.List;

import com.abelatox.raycraft.items.ModItems;
import com.abelatox.raycraft.sounds.ModSounds;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class EntityBarrel extends ThrowableEntity {

	public EntityBarrel(EntityType<? extends Entity> type, World world) {
		super(ModEntities.TYPE_BARREL, world);
		this.preventEntitySpawning = true;
		// this.setSize(0.98F, 0.98F);
	}

	public EntityBarrel(World worldIn, LivingEntity throwerIn) {
		super(ModEntities.TYPE_BARREL, throwerIn, worldIn);
		this.preventEntitySpawning = true;
		// this.setSize(0.98F, 0.98F);
	}

	public EntityBarrel(World worldIn) {
		super(ModEntities.TYPE_BARREL, worldIn);
	}

	@Override
	protected float getGravityVelocity() {
		return 0.05F;
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		if (result.getType() == Type.ENTITY) {
			EntityRayTraceResult entityResult = (EntityRayTraceResult) result;

			if (entityResult.getEntity() != null && entityResult.getEntity() instanceof PlayerEntity && entityResult.getEntity() == owner && !world.isRemote) {
				PlayerEntity player = (PlayerEntity) owner;
				if (ItemStack.areItemStacksEqual(player.getHeldItemMainhand(), ItemStack.EMPTY)) {
					player.inventory.mainInventory.set(player.inventory.currentItem, new ItemStack(ModItems.barrel));
					this.remove();
					return;
				} else { // if owner has full hands
					explode();
				}
			} else { // if entity is null or entity is not the owner
				explode();
			}
		} else { // if block
			explode();
		}
	}

	private void explode() {
		// System.out.println("Boom");
		// world.createExplosion(this, posX, posY, posZ, 5, Explosion.Mode.NONE);
		for (int i = 0; i < 5; i++) {
			world.addParticle(ParticleTypes.EXPLOSION, posX, posY, posZ, i, i, i);
		}

		List<Entity> entities = world.getEntitiesWithinAABBExcludingEntity(this, getBoundingBox().grow(16.0D, 10.0D, 16.0D).offset(-8.0D, -5.0D, -8.0D));
		if (!entities.isEmpty()) {
			for (Entity entity : entities) {
				if (owner instanceof PlayerEntity && entity != owner) {
					entity.attackEntityFrom(DamageSource.causePlayerDamage((PlayerEntity) owner), 1000);
				}
			}
		}

		removeBreakableBlocks();
		world.playSound(this.posX, this.posY, this.posZ, ModSounds.bombExplode, SoundCategory.PLAYERS, 5F, 1F, false);
		this.remove();
	}

	private void removeBreakableBlocks() {
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				for (int z = 0; z < 3; z++) {
					BlockPos pos = new BlockPos(posX + 1 - x, posY + 1 - y, posZ + 1 - z);
					if (world.getBlockState(pos).getBlock() == Blocks.IRON_DOOR || world.getBlockState(pos).getBlock() == Blocks.IRON_TRAPDOOR) {
						System.out.println("Block found");
						world.setBlockState(pos, Blocks.AIR.getDefaultState());
					}
				}
			}
		}
	}

	@Override
	protected void registerData() {

	}
}
