package com.abelatox.raycraft.items;

import com.abelatox.raycraft.entities.EntityPlum;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class ItemPlum extends BaseItem {

	public ItemPlum(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		if (!level.isClientSide()) {
			level.addFreshEntity(new EntityPlum(level, player));
			player.getItemInHand(hand).consume(1, player);
		}
		return InteractionResult.SUCCESS;
	}
}
