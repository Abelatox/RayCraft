package com.abelatox.raycraft.lib;

import com.abelatox.raycraft.items.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModCreativeTabs {

	public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Reference.MODID);

	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> RAYCRAFT_TAB = TABS.register("raycraft",
			() -> CreativeModeTab.builder()
					.title(Component.translatable("itemGroup." + Reference.MODID))
					.icon(() -> new ItemStack(ModItems.BARREL_BLOCK_ITEM.get()))
					.displayItems((params, output) -> {
						output.accept(ModItems.BARREL_BLOCK_ITEM.get());
						output.accept(ModItems.BARREL.get());
						output.accept(ModItems.POWER_UP.get());
						output.accept(ModItems.LUM_SPAWNER.get());
						output.accept(ModItems.CAGE_ITEM.get());
						output.accept(ModItems.TIMER_SWITCH_ITEM.get());
						output.accept(ModItems.ROBO_PIRATE_SPAWN_EGG.get());
						output.accept(ModItems.ROBO_PIRATE_GREEN_SPAWN_EGG.get());
						output.accept(ModItems.PLUM.get());
						output.accept(ModItems.WALKING_SHELL_SPAWN_EGG.get());
						output.accept(ModItems.FLYING_SHELL_SPAWN_EGG.get());
						output.accept(ModItems.SPIRAL_PORTAL.get());
						output.accept(ModItems.WIND_CURRENT.get());
					})
					.build());
}
