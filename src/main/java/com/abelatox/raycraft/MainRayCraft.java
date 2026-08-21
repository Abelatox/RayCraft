package com.abelatox.raycraft;

import com.abelatox.raycraft.blocks.ModBlocks;
import com.abelatox.raycraft.capabilities.ModCapabilities;
import com.abelatox.raycraft.entities.ModEntities;
import com.abelatox.raycraft.events.PlayerEventsHandler;
import com.abelatox.raycraft.items.ModItems;
import com.abelatox.raycraft.lib.ModCreativeTabs;
import com.abelatox.raycraft.lib.Reference;
import com.abelatox.raycraft.sounds.ModSounds;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Reference.MODID)
public class MainRayCraft {

	private static final Logger LOGGER = LogManager.getLogger();

	public MainRayCraft(IEventBus modEventBus) {
		ModBlocks.BLOCKS.register(modEventBus);
		ModItems.ITEMS.register(modEventBus);
		ModSounds.SOUNDS.register(modEventBus);
		ModCreativeTabs.TABS.register(modEventBus);
		ModEntities.ENTITY_TYPES.register(modEventBus);
		ModCapabilities.ATTACHMENT_TYPES.register(modEventBus);

		NeoForge.EVENT_BUS.register(new PlayerEventsHandler());

		LOGGER.info("RayCraft (NeoForge {}) initialized", Reference.MODID);
	}
}
