package com.abelatox.raycraft.items;

import com.abelatox.raycraft.MainRayCraft;
import com.abelatox.raycraft.lib.Reference;

import net.minecraft.item.Item;
import net.minecraft.item.Item.Properties;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Reference.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModItems {
	public static Item barrel, powerUp;

	@SubscribeEvent
	public static void registerItems(final RegistryEvent.Register<Item> event) {
		// Generic property that will cover most standard items
		Item.Properties genericItemProperties = new Item.Properties().group(MainRayCraft.rayCraftGroup);

		// Register and set item references here, no array needed here as only 1 thing needs to be registered
		event.getRegistry().registerAll(
			barrel = new ItemBarrel(new Properties().maxStackSize(1).group(MainRayCraft.rayCraftGroup)).setRegistryName(Reference.MODID, "barrel"),
			powerUp = new ItemPowerUp(new Properties().maxStackSize(1).group(MainRayCraft.rayCraftGroup)).setRegistryName(Reference.MODID, "power_up")
		);
	}

	public static Item createNewItem(String name, Item.Properties properties) {
		return new Item(properties).setRegistryName(Reference.MODID, name);
	}

}
