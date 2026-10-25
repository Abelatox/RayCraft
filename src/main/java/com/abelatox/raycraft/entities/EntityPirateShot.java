package com.abelatox.raycraft.entities;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class EntityPirateShot extends RaycraftProjectile {

	public EntityPirateShot(EntityType<? extends EntityPirateShot> type, Level level) {
		super(type, level);
	}

	public EntityPirateShot(EntityType<? extends EntityPirateShot> type, Level level, LivingEntity shooter) {
		super(type, level, shooter);
		setPower(type == ModEntities.PIRATE_SHOT_2.get() ? 4 : 2);
	}
}
