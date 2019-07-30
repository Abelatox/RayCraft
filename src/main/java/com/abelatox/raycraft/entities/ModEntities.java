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
	public static EntityType<EntityFist> TYPE_FIST = createEntityType(EntityFist.class, EntityFist::new, (spawnEntity, world) -> new EntityFist(world), EntityClassification.MISC, "entity_fist", 0.4F, 0.4F);
	public static EntityType<EntityPirateShot> TYPE_PIRATE_SHOT = createEntityType(EntityPirateShot.class, EntityPirateShot::new, (spawnEntity, world) -> new EntityPirateShot(world), EntityClassification.MISC, "entity_pirate_shot", 1, 1);
	public static EntityType<EntityPirateShot2> TYPE_PIRATE_SHOT_2 = createEntityType(EntityPirateShot2.class, EntityPirateShot2::new, (spawnEntity, world) -> new EntityPirateShot2(world), EntityClassification.MISC, "entity_pirate_shot2", 1, 1);
	public static EntityType<EntityLum> TYPE_LUM = createEntityType(EntityLum.class, EntityLum::new, (spawnEntity, world) -> new EntityLum(world), EntityClassification.MISC, "entity_lum", 0.5F, 0.5F);
	public static EntityType<EntityRunningShell> TYPE_SHELL = createEntityType(EntityRunningShell.class, EntityRunningShell::new, (spawnEntity, world) -> new EntityRunningShell(world), EntityClassification.MISC, "entity_running_shell", 0.5F, 0.5F);

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
		RenderingRegistry.registerEntityRenderingHandler(EntityFist.class, RenderEntityFist.FACTORY);
		RenderingRegistry.registerEntityRenderingHandler(EntityPirateShot.class, RenderEntityPirateShot.FACTORY);
		RenderingRegistry.registerEntityRenderingHandler(EntityPirateShot2.class, RenderEntityPirateShot2.FACTORY);
		RenderingRegistry.registerEntityRenderingHandler(EntityLum.class, RenderEntityLum.FACTORY);
		RenderingRegistry.registerEntityRenderingHandler(EntityRunningShell.class, RenderEntityShell.FACTORY);
	}

	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class Registry {

		@SubscribeEvent
		public static void registerEntities(RegistryEvent.Register<EntityType<?>> event) {
			event.getRegistry().register(TYPE_BARREL);
			event.getRegistry().register(TYPE_FIST);
			event.getRegistry().register(TYPE_PIRATE_SHOT);
			event.getRegistry().register(TYPE_PIRATE_SHOT_2);
			event.getRegistry().register(TYPE_LUM);
			event.getRegistry().register(TYPE_SHELL);
		}
	}
}
