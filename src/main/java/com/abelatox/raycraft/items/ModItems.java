package com.abelatox.raycraft.items;

import com.abelatox.raycraft.blocks.ModBlocks;
import com.abelatox.raycraft.entities.ModEntities;
import com.abelatox.raycraft.lib.Reference;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.SpawnEggItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {

	public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Reference.MODID);

	public static final DeferredItem<BlockItem> BARREL_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(ModBlocks.BARREL);
	public static final DeferredItem<BlockItem> CAGE_ITEM = ITEMS.registerSimpleBlockItem(ModBlocks.CAGE);
	public static final DeferredItem<BlockItem> TIMER_SWITCH_ITEM = ITEMS.registerSimpleBlockItem(ModBlocks.TIMER_SWITCH);

	public static final DeferredItem<ItemBarrel> BARREL = ITEMS.registerItem(
			"barrel", ItemBarrel::new, props -> props.stacksTo(1));

	public static final DeferredItem<ItemPowerUp> POWER_UP = ITEMS.registerItem(
			"power_up", ItemPowerUp::new, props -> props.stacksTo(1));

	public static final DeferredItem<ItemSummonLum> LUM_SPAWNER = ITEMS.registerItem(
			"lum_spawner", ItemSummonLum::new, props -> props.stacksTo(1));

	public static final DeferredItem<SpawnEggItem> ROBO_PIRATE_SPAWN_EGG = ITEMS.registerItem(
			"robo_pirate_spawn_egg", SpawnEggItem::new, props -> props.spawnEgg(ModEntities.ROBO_PIRATE.get()));

	public static final DeferredItem<SpawnEggItem> ROBO_PIRATE_GREEN_SPAWN_EGG = ITEMS.registerItem(
			"robo_pirate_green_spawn_egg", SpawnEggItem::new, props -> props.spawnEgg(ModEntities.ROBO_PIRATE_GREEN.get()));

	public static final DeferredItem<ItemPlum> PLUM = ITEMS.registerItem(
			"plum", ItemPlum::new, props -> props.stacksTo(1));

	public static final DeferredItem<SpawnEggItem> WALKING_SHELL_SPAWN_EGG = ITEMS.registerItem(
			"walking_shell_spawn_egg", SpawnEggItem::new, props -> props.spawnEgg(ModEntities.WALKING_SHELL.get()));

	public static final DeferredItem<SpawnEggItem> FLYING_SHELL_SPAWN_EGG = ITEMS.registerItem(
			"flying_shell_spawn_egg", SpawnEggItem::new, props -> props.spawnEgg(ModEntities.FLYING_SHELL.get()));
}
