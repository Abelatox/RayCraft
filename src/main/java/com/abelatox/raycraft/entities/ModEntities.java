package com.abelatox.raycraft.entities;

import com.abelatox.raycraft.lib.Reference;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModEntities {

	public static final DeferredRegister.Entities ENTITY_TYPES = DeferredRegister.createEntities(Reference.MODID);

	public static final Supplier<EntityType<EntityBarrel>> BARREL = ENTITY_TYPES.registerEntityType(
			"entity_barrel", EntityBarrel::new, MobCategory.MISC,
			builder -> builder.sized(1F, 1F).clientTrackingRange(128).updateInterval(1));

	public static final Supplier<EntityType<EntityFist>> FIST = ENTITY_TYPES.registerEntityType(
			"entity_fist", EntityFist::new, MobCategory.MISC,
			builder -> builder.sized(0.4F, 0.4F).clientTrackingRange(128).updateInterval(1));

	public static final Supplier<EntityType<EntityPirateShot>> PIRATE_SHOT = ENTITY_TYPES.registerEntityType(
			"entity_pirate_shot", EntityPirateShot::new, MobCategory.MISC,
			builder -> builder.sized(1F, 1F).clientTrackingRange(128).updateInterval(1));

	public static final Supplier<EntityType<EntityPirateShot2>> PIRATE_SHOT_2 = ENTITY_TYPES.registerEntityType(
			"entity_pirate_shot2", EntityPirateShot2::new, MobCategory.MISC,
			builder -> builder.sized(1F, 1F).clientTrackingRange(128).updateInterval(1));

	public static final Supplier<EntityType<EntityLum>> LUM = ENTITY_TYPES.registerEntityType(
			"entity_lum", EntityLum::new, MobCategory.MISC,
			builder -> builder.sized(0.5F, 0.5F).clientTrackingRange(128).updateInterval(1));
}
