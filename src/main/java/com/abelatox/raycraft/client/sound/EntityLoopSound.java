package com.abelatox.raycraft.client.sound;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;

public class EntityLoopSound extends AbstractTickableSoundInstance {
	private final Entity entity;

	public EntityLoopSound(Entity entity, SoundEvent sound) {
		super(sound, entity.getSoundSource(), SoundInstance.createUnseededRandom());
		this.entity = entity;
		this.looping = true;
		this.delay = 0;
		this.x = entity.getX();
		this.y = entity.getY();
		this.z = entity.getZ();
	}

	public void stopNow() {
		stop();
	}

	@Override
	public void tick() {
		if (entity.isRemoved()) {
			stop();
			return;
		}
		this.x = entity.getX();
		this.y = entity.getY();
		this.z = entity.getZ();
	}
}
