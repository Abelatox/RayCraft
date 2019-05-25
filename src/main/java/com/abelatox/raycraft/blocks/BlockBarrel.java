package com.abelatox.raycraft.blocks;

import com.abelatox.raycraft.capabilities.IPlayerModelCapability;
import com.abelatox.raycraft.capabilities.ModCapabilities;
import com.abelatox.raycraft.items.ModItems;
import com.abelatox.raycraft.network.PacketHandler;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BlockBarrel extends BaseBlock {

	/**
	 * Smaller collision box otherwise
	 * {@link #onEntityCollision(IBlockState, World, BlockPos, Entity)} doesn't
	 * trigger
	 */
	private static final VoxelShape collisionShape = Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 15.0D, 15.0D);
	private float damage = 3.0F;

	public BlockBarrel(String name, Properties properties) {
		super(name, properties);
	}

	@SuppressWarnings("deprecation")
	@Override
	public VoxelShape getCollisionShape(IBlockState state, IBlockReader world, BlockPos pos) {
		return collisionShape;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onEntityCollision(IBlockState state, World world, BlockPos pos, Entity entity) {

		if (entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			IPlayerModelCapability props = ModCapabilities.get(player);
			if (player.motionX == 0 && player.motionY == 0 && player.motionZ == 0 && player.isSneaking()) {
				player.inventory.mainInventory.set(player.inventory.currentItem, new ItemStack(ModItems.barrel));
				world.removeBlock(pos);
			}
		}
	
	}

	@Override
	public void onBlockClicked(IBlockState state, World world, BlockPos pos, EntityPlayer player) {
		/*IPlayerModelCapability props = ModCapabilities.get(player);
		System.out.println("change");
		props.setCarrying("barrel");
		PacketHandler.sendToAllAround(player, props);
		world.removeBlock(pos);*/
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onBlockActivated(IBlockState state, World world, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (ItemStack.areItemStacksEqual(player.getHeldItemMainhand(), ItemStack.EMPTY)) {
			player.inventory.mainInventory.set(player.inventory.currentItem, new ItemStack(ModItems.barrel));
			world.removeBlock(pos);
		}
		return false;
	}
}
