package com.abelatox.raycraft.blocks;

import com.abelatox.raycraft.items.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockBarrel extends BaseBlock {

	public BlockBarrel(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return Block.box(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D);
	}

	@Override
	protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean intersects) {
		if (entity instanceof Player player) {
			boolean notMoving = player.getDeltaMovement().x == 0 && player.getDeltaMovement().y == 0 && player.getDeltaMovement().z == 0;
			if (notMoving && player.isShiftKeyDown() && player.getMainHandItem().isEmpty()) {
				player.getInventory().setItem(player.getInventory().getSelectedSlot(), new ItemStack(ModItems.BARREL.get()));
				level.removeBlock(pos, true);
			}
		}
	}
}
