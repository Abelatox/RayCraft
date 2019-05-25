package com.abelatox.raycraft.entities;

import java.util.function.Function;

import com.abelatox.raycraft.lib.Reference;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;

public class ModEntities {

    public static EntityType<EntityBarrel> TYPE_BARREL = createEntityType(EntityBarrel.class, EntityBarrel::new, "entity_barrel");
    public static EntityType<EntityBaseFist> TYPE_FIST = createEntityType(EntityFist0.class, EntityFist0::new, "entity_fist");
    public static EntityType<EntityBaseFist> TYPE_FIST_1 = createEntityType(EntityFist1.class, EntityFist1::new, "entity_fist1");

    /**
     * Helper method to create a new EntityType and set the registry name
     * @param entityClassIn The entity class
     * @param factoryIn The render factory
     * @param name The registry name of the entity
     * @param <T> The entity type
     * @return The EntityType created
     */
    public static <T extends Entity>EntityType<T> createEntityType(Class<? extends T> entityClassIn, Function<? super World, ? extends T> factoryIn, String name) {
        EntityType<T> type = EntityType.Builder.create(entityClassIn, factoryIn).tracker(100, 1, true).build(name);
        type.setRegistryName(Reference.MODID, name);
        return type;
    }

    /**
     * Register the render classes for the entities, called in {@link online.kingdomkeys.kingdomkeys.proxy.ClientProxy#registerModels(ModelRegistryEvent)}  }
     */
    public static void registerModels() {
        RenderingRegistry.registerEntityRenderingHandler(EntityBarrel.class, RenderEntityBarrel.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(EntityFist0.class, RenderEntityFist.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(EntityFist1.class, RenderEntityFist.FACTORY);
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Registry {

        @SubscribeEvent
        public static void registerEntities(RegistryEvent.Register<EntityType<?>> event) {
            event.getRegistry().register(TYPE_BARREL);
            event.getRegistry().register(TYPE_FIST);
            event.getRegistry().register(TYPE_FIST_1);
        }

    }

}
