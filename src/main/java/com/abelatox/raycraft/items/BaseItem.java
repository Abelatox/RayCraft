package com.abelatox.raycraft.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class BaseItem extends Item {

	public BaseItem(Properties properties) {
		super(properties);
	}

	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		// Minecraft.getInstance().displayGuiScreen(new GUIChooseWeebPower(player));

		return new ActionResult<>(EnumActionResult.PASS, player.getHeldItem(hand));
	}
}
