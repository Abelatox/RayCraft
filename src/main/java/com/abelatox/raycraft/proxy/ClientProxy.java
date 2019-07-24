package com.abelatox.raycraft.proxy;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
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
}
