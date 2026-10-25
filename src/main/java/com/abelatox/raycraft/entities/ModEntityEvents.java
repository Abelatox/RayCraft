package com.abelatox.raycraft.entities;

import com.abelatox.raycraft.lib.Reference;

import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

@EventBusSubscriber(modid = Reference.MODID)
public class ModEntityEvents {

	@SubscribeEvent
	public static void registerAttributes(EntityAttributeCreationEvent event) {
		event.put(ModEntities.ROBO_PIRATE.get(), EntityRoboPirate.createAttributes().build());
		event.put(ModEntities.ROBO_PIRATE_GREEN.get(), EntityRoboPirate.createAttributes().build());
		event.put(ModEntities.WALKING_SHELL.get(), EntityWalkingShell.createAttributes().build());
		event.put(ModEntities.FLYING_SHELL.get(), EntityWalkingShell.createAttributes().build());
	}

	@SubscribeEvent
	public static void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
		event.register(ModEntities.ROBO_PIRATE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
		event.register(ModEntities.ROBO_PIRATE_GREEN.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
	}
}
