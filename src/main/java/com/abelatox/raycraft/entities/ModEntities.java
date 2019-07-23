package com.abelatox.raycraft.entities;

import java.util.function.BiFunction;
import java.util.function.Function;

import com.abelatox.raycraft.lib.Reference;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.FMLPlayMessages.SpawnEntity;

public class ModEntities {

	//public static EntityType<EntityBarrel> TYPE_BARREL = createEntityType(EntityBarrel.class, EntityBarrel::new, EntityClassification.MISC, "entity_barrel", 1, 1);
	//public static EntityType<EntityBarrel> TYPE_BARREL = registerEntity(EntityType.Builder.<EntityBarrel>create(EntityClassification.MISC).setCustomClientFactory(EntityBarrel::new).size(0.25F, 0.25F), "ent_projectile");
	public static EntityType<EntityBarrel> TYPE_BARREL = (EntityType<EntityBarrel>) EntityType.Builder.create(EntityBarrel::new, EntityClassification.MISC).setCustomClientFactory((spawnEntity, world) -> new EntityBarrel(world)).setShouldReceiveVelocityUpdates(true).setUpdateInterval(1).setTrackingRange(128).size(1, 1).build("entity_barrel").setRegistryName(Reference.MODID, "entity_barrel");
	public static EntityType<EntityBaseFist> TYPE_FIST_0 = createEntityType(EntityFist0.class, EntityFist0::new, (spawnEntity, world) -> new EntityFist0(world), EntityClassification.MISC, "entity_fist0", 0.2F, 0.2F);
	//public static EntityType<EntityBaseFist> TYPE_FIST_0 = createEntityType(EntityFist0.class, EntityFist0::new, EntityClassification.MISC, "entity_fist0", 1, 1);
	public static EntityType<EntityBaseFist> TYPE_FIST_1 = createEntityType(EntityFist1.class, EntityFist1::new, (spawnEntity, world) -> new EntityFist1(world), EntityClassification.MISC, "entity_fist1", 1, 1);
	public static EntityType<EntityBaseFist> TYPE_FIST_2 = createEntityType(EntityFist2.class, EntityFist2::new, (spawnEntity, world) -> new EntityFist2(world), EntityClassification.MISC, "entity_fist2", 1, 1);
	public static EntityType<EntityBaseFist> TYPE_FIST_3 = createEntityType(EntityFist3.class, EntityFist3::new, (spawnEntity, world) -> new EntityFist3(world), EntityClassification.MISC, "entity_fist3", 1, 1);
	public static EntityType<EntityBaseFist> TYPE_FIST_4 = createEntityType(EntityFist4.class, EntityFist4::new, (spawnEntity, world) -> new EntityFist4(world), EntityClassification.MISC, "entity_fist4", 1, 1);

	/**
	 * Helper method to create a new EntityType and set the registry name
	 * 
	 * @param entityClassIn The entity class
	 * @param factoryIn     The render factory
	 * @param name          The registry name of the entity
	 * @param clientFactory 
	 * @param <T>           The entity type
	 * @return The EntityType created
	 */

	public static <T extends Entity> EntityType<T> createEntityType(Class<? extends T> entityClassIn, EntityType.IFactory<T> factory, EntityClassification classification, String name, float sizeX, float sizeY) {
		EntityType<T> type = EntityType.Builder.create(factory, classification).setShouldReceiveVelocityUpdates(true).setUpdateInterval(1).setTrackingRange(128).size(sizeX, sizeY).build(name);
		type.setRegistryName(Reference.MODID, name);
		return type;
	}
	
	public static <T extends Entity> EntityType<T> createEntityType(Class<? extends T> entityClassIn, EntityType.IFactory<T> factory, BiFunction<SpawnEntity, World, T> clientFactory, EntityClassification classification, String name, float sizeX, float sizeY) {
		EntityType<T> type = EntityType.Builder.create(factory, classification).setCustomClientFactory(clientFactory).setShouldReceiveVelocityUpdates(true).setUpdateInterval(1).setTrackingRange(128).size(sizeX, sizeY).build(name);
		type.setRegistryName(Reference.MODID, name);
		return type;
	}

	public static void registerModels() {
		RenderingRegistry.registerEntityRenderingHandler(EntityBarrel.class, RenderEntityBarrel.FACTORY);
		RenderingRegistry.registerEntityRenderingHandler(EntityFist0.class, RenderEntityFist.FACTORY);
		RenderingRegistry.registerEntityRenderingHandler(EntityFist1.class, RenderEntityFist.FACTORY);
		RenderingRegistry.registerEntityRenderingHandler(EntityFist2.class, RenderEntityFist.FACTORY);
		RenderingRegistry.registerEntityRenderingHandler(EntityFist3.class, RenderEntityFist.FACTORY);
		RenderingRegistry.registerEntityRenderingHandler(EntityFist4.class, RenderEntityFist.FACTORY);
	}

	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class Registry {

		@SubscribeEvent
		public static void registerEntities(RegistryEvent.Register<EntityType<?>> event) {
			event.getRegistry().register(TYPE_BARREL);
			event.getRegistry().register(TYPE_FIST_0);
			event.getRegistry().register(TYPE_FIST_1);
			event.getRegistry().register(TYPE_FIST_2);
			event.getRegistry().register(TYPE_FIST_3);
			event.getRegistry().register(TYPE_FIST_4);
		}

	}

}
