package com.abelatox.raycraft.blocks;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class ItemBlockWrapper extends BlockItem {

	public ItemBlockWrapper(Block blockIn, ItemGroup group) {
		super(blockIn, new Item.Properties().group(group));
		setRegistryName(blockIn.getRegistryName());
	}
}
