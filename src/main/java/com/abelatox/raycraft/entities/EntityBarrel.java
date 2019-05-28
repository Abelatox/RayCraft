package com.abelatox.raycraft.entities;

import com.abelatox.raycraft.blocks.ModBlocks;
import com.abelatox.raycraft.items.ModItems;
import com.abelatox.raycraft.lib.Utils;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityBarrel extends EntityThrowable {

	public EntityBarrel(World world) {
		super(ModEntities.TYPE_BARREL, world);
		this.preventEntitySpawning = true;
		this.isImmuneToFire = true;
		this.setSize(0.98F, 0.98F);
	}

	public EntityBarrel(World worldIn, EntityLivingBase throwerIn) {
		super(ModEntities.TYPE_BARREL, throwerIn, worldIn);
		this.preventEntitySpawning = true;
		this.isImmuneToFire = true;
		this.setSize(0.98F, 0.98F);
	}

	@Override
	protected float getGravityVelocity() {
		return 0.05F;
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		if (!world.isRemote) {
			if (result.entity != null && result.entity instanceof EntityPlayer && result.entity == thrower) {
				EntityPlayer player = (EntityPlayer) thrower;
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
		}
	}

	private void explode() {
		// System.out.println("Boom");
		world.createExplosion(this, posX, posY, posZ, 5, false);
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

}
