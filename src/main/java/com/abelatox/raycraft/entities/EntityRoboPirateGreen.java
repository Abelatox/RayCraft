package com.abelatox.raycraft.entities;

import com.abelatox.raycraft.sounds.ModSounds;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class EntityRoboPirateGreen extends EntityRoboPirate {

	public EntityRoboPirateGreen(EntityType<? extends EntityRoboPirateGreen> type, Level level) {
		super(type, level);
	}

	@Override
	protected RaycraftProjectile createShot() {
		return new EntityPirateShot(ModEntities.PIRATE_SHOT.get(), level(), this);
	}

	@Override
	protected SoundEvent getShotSound() {
		return ModSounds.PIRATE_SHOT_1.get();
	}
}
