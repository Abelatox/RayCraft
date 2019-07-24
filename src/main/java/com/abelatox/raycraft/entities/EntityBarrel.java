package com.abelatox.raycraft.entities;

import com.abelatox.raycraft.items.ModItems;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.Explosion;
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
		if (!world.isRemote) {
			if (result.getType() == Type.ENTITY) {
				EntityRayTraceResult entityResult = (EntityRayTraceResult) result;

				if (entityResult.getEntity() != null && entityResult.getEntity() instanceof PlayerEntity && entityResult.getEntity() == owner) {
					PlayerEntity player = (PlayerEntity) owner;
					if (ItemStack.areItemStacksEqual(player.getHeldItemMainhand(), ItemStack.EMPTY)) {
						player.inventory.mainInventory.set(player.inventory.currentItem, new ItemStack(ModItems.barrel));
						this.remove();
						return;
					} else {
						explode();
					}
				} else {
					explode();
				}
			} else {
				explode();
			}
		}
	}

	private void explode() {
		// System.out.println("Boom");
		world.createExplosion(this, posX, posY, posZ, 5, Explosion.Mode.NONE);
		removeBreakableBlocks();
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
		// TODO Auto-generated method stub

	}

}
