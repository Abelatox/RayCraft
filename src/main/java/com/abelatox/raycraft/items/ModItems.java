package com.abelatox.raycraft.items;

import com.abelatox.raycraft.blocks.ModBlocks;
import com.abelatox.raycraft.lib.Reference;

import net.minecraft.world.item.BlockItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {

	public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Reference.MODID);

	public static final DeferredItem<BlockItem> BARREL_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(ModBlocks.BARREL);

	public static final DeferredItem<ItemBarrel> BARREL = ITEMS.registerItem(
			"barrel", ItemBarrel::new, props -> props.stacksTo(1));

	public static final DeferredItem<ItemPowerUp> POWER_UP = ITEMS.registerItem(
			"power_up", ItemPowerUp::new, props -> props.stacksTo(1));

	public static final DeferredItem<ItemSummonLum> LUM_SPAWNER = ITEMS.registerItem(
			"lum_spawner", ItemSummonLum::new, props -> props.stacksTo(1));
}
