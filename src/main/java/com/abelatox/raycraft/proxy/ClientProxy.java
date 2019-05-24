package com.abelatox.raycraft.proxy;

import com.abelatox.raycraft.entities.ModEntities;
import com.abelatox.raycraft.lib.Reference;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.b3d.B3DLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class ClientProxy implements IProxy {
    @Override
    public void setup(FMLCommonSetupEvent event) {
        //OBJLoader and B3DLoader currently aren't hooked up however, this is here for when they are
        OBJLoader.INSTANCE.addDomain(Reference.MODID);
        B3DLoader.INSTANCE.addDomain(Reference.MODID);
    }

    //Register the entity models
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        ModEntities.registerModels();
		//ModelLoader.setCustomModelResourceLocation(ModItems.kingdomKey, 0, new ModelResourceLocation("", "inventory"));

    }

}
