package com.abelatox.raycraft.entities;

import java.util.function.BiFunction;

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
	public static EntityType<EntityBarrel> TYPE_BARREL = createEntityType(EntityBarrel.class, EntityBarrel::new, (spawnEntity, world) -> new EntityBarrel(world), EntityClassification.MISC, "entity_barrel", 1, 1F);
	public static EntityType<EntityBaseFist> TYPE_FIST_0 = createEntityType(EntityFist0.class, EntityFist0::new, (spawnEntity, world) -> new EntityFist0(world), EntityClassification.MISC, "entity_fist0", 1, 1);
	public static EntityType<EntityBaseFist> TYPE_FIST_1 = createEntityType(EntityFist1.class, EntityFist1::new, (spawnEntity, world) -> new EntityFist1(world), EntityClassification.MISC, "entity_fist1", 1, 1);
	public static EntityType<EntityBaseFist> TYPE_FIST_2 = createEntityType(EntityFist2.class, EntityFist2::new, (spawnEntity, world) -> new EntityFist2(world), EntityClassification.MISC, "entity_fist2", 1, 1);
	public static EntityType<EntityBaseFist> TYPE_FIST_3 = createEntityType(EntityFist3.class, EntityFist3::new, (spawnEntity, world) -> new EntityFist3(world), EntityClassification.MISC, "entity_fist3", 1, 1);
	public static EntityType<EntityBaseFist> TYPE_FIST_4 = createEntityType(EntityFist4.class, EntityFist4::new, (spawnEntity, world) -> new EntityFist4(world), EntityClassification.MISC, "entity_fist4", 1, 1);
	public static EntityType<EntityPirateShot> TYPE_PIRATE_SHOT = createEntityType(EntityPirateShot.class, EntityPirateShot::new, (spawnEntity, world) -> new EntityPirateShot(world), EntityClassification.MISC, "entity_pirate_shot", 1, 1);
	public static EntityType<EntityPirateShot2> TYPE_PIRATE_SHOT_2 = createEntityType(EntityPirateShot2.class, EntityPirateShot2::new, (spawnEntity, world) -> new EntityPirateShot2(world), EntityClassification.MISC, "entity_pirate_shot2", 1, 1);
	public static EntityType<EntityYellowLum> TYPE_YELLOW_LUM = createEntityType(EntityYellowLum.class, EntityYellowLum::new, (spawnEntity, world) -> new EntityYellowLum(world), EntityClassification.MISC, "entity_yellow_lum", 1, 1);

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
		RenderingRegistry.registerEntityRenderingHandler(EntityPirateShot.class, RenderEntityPirateShot.FACTORY);
		RenderingRegistry.registerEntityRenderingHandler(EntityPirateShot2.class, RenderEntityPirateShot2.FACTORY);
		RenderingRegistry.registerEntityRenderingHandler(EntityYellowLum.class, RenderEntityLum.FACTORY);
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
			event.getRegistry().register(TYPE_PIRATE_SHOT);
			event.getRegistry().register(TYPE_PIRATE_SHOT_2);
			event.getRegistry().register(TYPE_YELLOW_LUM);
		}
	}
}
