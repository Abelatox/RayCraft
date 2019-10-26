package com.abelatox.raycraft.proxy;

import com.abelatox.raycraft.client.InputHandler;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class ClientProxy implements IProxy {
    @Override
    public void setup(FMLCommonSetupEvent event) {
        //OBJLoader and B3DLoader currently aren't hooked up however, this is here for when they are
        //OBJLoader.INSTANCE.addDomain(Reference.MODID);
        //B3DLoader.INSTANCE.addDomain(Reference.MODID);
		//ModelLoader.setCustomModelResourceLocation(ModItems.barrel, 0, new ModelResourceLocation(ModItems.barrel.getRegistryName(), "inventory"));

    }
    
    @SubscribeEvent
    public static void setupClient(FMLClientSetupEvent event) {
       /* for (InputHandler.Keybinds key : InputHandler.Keybinds.values())
            ClientRegistry.registerKeyBinding(key.getKeybind());*/
        
		//MinecraftForge.EVENT_BUS.register(new GuiOverlay());

    }
}
