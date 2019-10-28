package com.abelatox.raycraft;

import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.abelatox.raycraft.blocks.ModBlocks;
import com.abelatox.raycraft.capabilities.ModCapabilities;
import com.abelatox.raycraft.client.ClientEvents;
import com.abelatox.raycraft.client.InputHandler;
import com.abelatox.raycraft.entities.ModEntities;
import com.abelatox.raycraft.events.CapabilityEventsHandler;
import com.abelatox.raycraft.events.PlayerEventsHandler;
import com.abelatox.raycraft.gui.GUIHealth;
import com.abelatox.raycraft.lib.Reference;
import com.abelatox.raycraft.models.ModModels;
import com.abelatox.raycraft.network.PacketHandler;
import com.abelatox.raycraft.proxy.ClientProxy;
import com.abelatox.raycraft.proxy.IProxy;
import com.abelatox.raycraft.proxy.ServerProxy;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Reference.MODID)
public class MainRayCraft {
	// Directly reference a log4j logger.
	private static final Logger LOGGER = LogManager.getLogger();

	public static MainRayCraft instance;

	// The proxy instance created for the current dist double lambda prevents class
	// being loaded on the other dist
	@SuppressWarnings("Convert2MethodRef")
	public static IProxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> () -> new ServerProxy());

	public static ItemGroup rayCraftGroup = new ItemGroup("RayCraft") {
		@Override
		public ItemStack createIcon() {
			return new ItemStack(ModBlocks.barrel);
		}
	};

	public MainRayCraft() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

		MinecraftForge.EVENT_BUS.register(this);
	}

	private void setup(final FMLCommonSetupEvent event) {
		// some preinit code
		LOGGER.info("HELLO FROM PREINIT");
		LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
		MinecraftForge.EVENT_BUS.register(new PlayerEventsHandler());
		MinecraftForge.EVENT_BUS.register(new CapabilityEventsHandler());


		ModCapabilities.register();
		DeferredWorkQueue.runLater(() -> {
			PacketHandler.register();
		});
	}

	private void doClientStuff(final FMLClientSetupEvent event) {
		LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().gameSettings);
		for (InputHandler.Keybinds key : InputHandler.Keybinds.values())
			ClientRegistry.registerKeyBinding(key.getKeybind());

		MinecraftForge.EVENT_BUS.register(new InputHandler());
		MinecraftForge.EVENT_BUS.register(new ClientEvents());
		MinecraftForge.EVENT_BUS.register(new GUIHealth());
        ModEntities.registerModels();
		ModModels.register();
	}

	private void enqueueIMC(final InterModEnqueueEvent event) {
		InterModComms.sendTo(Reference.MODID, "helloworld", () -> {
			LOGGER.info("Hello world from the MDK");
			return "Hello world";
		});
	}

	private void processIMC(final InterModProcessEvent event) {
		LOGGER.info("Got IMC {}", event.getIMCStream().map(m -> m.getMessageSupplier().get()).collect(Collectors.toList()));
	}

	@SubscribeEvent
	public void onServerStarting(FMLServerStartingEvent event) {
		// do something when the server starts
		LOGGER.info("HELLO from server starting");
	}

	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class RegistryEvents {
		@SubscribeEvent
		public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
			// register a new block here
			LOGGER.info("HELLO from Register Block");
		}
	}
}
