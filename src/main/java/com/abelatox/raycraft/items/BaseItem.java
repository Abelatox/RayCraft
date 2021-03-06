package com.abelatox.raycraft.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class BaseItem extends Item {

	public BaseItem(Properties properties) {
		super(properties);
		
	}

	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
		return new ActionResult<>(ActionResultType.PASS, player.getHeldItem(hand));
	}
	
}
