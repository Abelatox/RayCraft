package com.abelatox.raycraft.entities;

import com.abelatox.raycraft.lib.Reference;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModEntities {

	public static final DeferredRegister.Entities ENTITY_TYPES = DeferredRegister.createEntities(Reference.MODID);

	public static final Supplier<EntityType<EntityBarrel>> BARREL = ENTITY_TYPES.registerEntityType("entity_barrel", EntityBarrel::new, MobCategory.MISC, builder -> builder.sized(1F, 1F).clientTrackingRange(128).updateInterval(1));

	public static final Supplier<EntityType<EntityFist>> FIST = ENTITY_TYPES.registerEntityType("entity_fist", EntityFist::new, MobCategory.MISC, builder -> builder.sized(0.25F, 0.25F).clientTrackingRange(128).updateInterval(1));

	public static final Supplier<EntityType<EntityPirateShot>> PIRATE_SHOT = ENTITY_TYPES.registerEntityType("entity_pirate_shot", EntityPirateShot::new, MobCategory.MISC, builder -> builder.sized(1F, 1F).clientTrackingRange(128).updateInterval(1));

	public static final Supplier<EntityType<EntityPirateShot>> PIRATE_SHOT_2 = ENTITY_TYPES.registerEntityType("entity_pirate_shot2", EntityPirateShot::new, MobCategory.MISC, builder -> builder.sized(1F, 1F).clientTrackingRange(128).updateInterval(1));

	public static final Supplier<EntityType<EntityLum>> LUM = ENTITY_TYPES.registerEntityType("entity_lum", EntityLum::new, MobCategory.MISC, builder -> builder.sized(0.5F, 0.5F).clientTrackingRange(128).updateInterval(1));

	public static final Supplier<EntityType<EntityRoboPirate>> ROBO_PIRATE = ENTITY_TYPES.registerEntityType("robo_pirate", EntityRoboPirate::new, MobCategory.MONSTER, builder -> builder.sized(0.6F, 1.95F).clientTrackingRange(8));

	public static final Supplier<EntityType<EntityRoboPirateGreen>> ROBO_PIRATE_GREEN = ENTITY_TYPES.registerEntityType("robo_pirate_green", EntityRoboPirateGreen::new, MobCategory.MONSTER, builder -> builder.sized(0.6F, 1.95F).clientTrackingRange(8));

	public static final Supplier<EntityType<EntityPlum>> PLUM = ENTITY_TYPES.registerEntityType("plum", EntityPlum::new, MobCategory.MISC, builder -> builder.sized(0.7F, 0.7F).fireImmune().clientTrackingRange(10).updateInterval(1));

	public static final Supplier<EntityType<EntityWalkingShell>> WALKING_SHELL = ENTITY_TYPES.registerEntityType("walking_shell", EntityWalkingShell::new, MobCategory.CREATURE, builder -> builder.sized(0.6F, 1.0F).fireImmune().clientTrackingRange(10));

	public static final Supplier<EntityType<EntityFlyingShell>> FLYING_SHELL = ENTITY_TYPES.registerEntityType("flying_shell", EntityFlyingShell::new, MobCategory.CREATURE, builder -> builder.sized(0.6F, 1.0F).fireImmune().clientTrackingRange(10));

	public static final Supplier<EntityType<EntitySpiralPortal>> SPIRAL_PORTAL = ENTITY_TYPES.registerEntityType("spiral_portal", EntitySpiralPortal::new, MobCategory.MISC, builder -> builder.sized(1.5F, 2.0F).fireImmune().clientTrackingRange(10));
}
