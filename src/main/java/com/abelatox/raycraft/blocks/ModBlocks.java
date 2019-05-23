package com.abelatox.raycraft.blocks;

import com.abelatox.raycraft.MainRayCraft;
import com.abelatox.raycraft.lib.Reference;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Reference.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBlocks {

	 public static Block barrel;

	    //Array of all blocks to reduce registry code
	    private static final Block[] BLOCKS = {
	            barrel = new BlockBarrel("barrelblock", Block.Properties.create(Material.IRON).hardnessAndResistance(1.0F, 10.0F)),
	    };

	    private static Block createNewBlock(String name, Block.Properties properties) {
	        return new Block(properties).setRegistryName(Reference.MODID, name);
	    }

	    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
	    public static class Registry {
	        //Register every block from the blocks array
	        @SubscribeEvent
	        public static void registerBlocks(final RegistryEvent.Register<Block> event) {
	            event.getRegistry().registerAll(BLOCKS);
	        }

	        //Create and register the ItemBlock for each block in the blocks array
	        @SubscribeEvent
	        public static void registerItemBlocks(final RegistryEvent.Register<Item> event) {
	            for (Block b : BLOCKS) {
	                event.getRegistry().register(new ItemBlockWrapper(b, MainRayCraft.rayCraftGroup));
	            }
	        }

	    }
}
