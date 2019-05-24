package com.abelatox.raycraft.entities;

import com.abelatox.raycraft.capabilities.IPlayerModelCapability;
import com.abelatox.raycraft.capabilities.ModCapabilities;
import com.abelatox.raycraft.items.ModItems;
import com.abelatox.raycraft.network.PacketHandler;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
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
		System.out.println("Boom");
		world.createExplosion(this, posX, posY, posZ, 5, false);
		this.remove();
	}

}
