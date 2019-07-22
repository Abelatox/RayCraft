package com.abelatox.raycraft.blocks;

import com.abelatox.raycraft.lib.Reference;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class BaseBlock extends Block {

	public BaseBlock(String name, Properties properties) {
		super(properties);
		setRegistryName(Reference.MODID, name);
	}

	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
		// Minecraft.getInstance().displayGuiScreen(new GUIChooseWeebPower(player));

		return new ActionResult<>(ActionResultType.PASS, player.getHeldItem(hand));
	}
}
