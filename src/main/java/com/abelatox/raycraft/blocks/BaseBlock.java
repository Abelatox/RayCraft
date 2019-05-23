package com.abelatox.raycraft.blocks;

import com.abelatox.raycraft.lib.Reference;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class BaseBlock extends Block {

	public BaseBlock(String name, Properties properties) {
		super(properties);
		setRegistryName(Reference.MODID, name);
	}

	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		// Minecraft.getInstance().displayGuiScreen(new GUIChooseWeebPower(player));

		return new ActionResult<>(EnumActionResult.PASS, player.getHeldItem(hand));
	}
}
