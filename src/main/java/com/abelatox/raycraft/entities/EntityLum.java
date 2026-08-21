package com.abelatox.raycraft.entities;

import com.abelatox.raycraft.capabilities.IPlayerCapabilities;
import com.abelatox.raycraft.capabilities.ModCapabilities;
import com.abelatox.raycraft.sounds.ModSounds;

import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;

import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class EntityLum extends Entity {

	private static final EntityDataAccessor<String> TYPE = SynchedEntityData.defineId(EntityLum.class, EntityDataSerializers.STRING);

	public EntityLum(EntityType<? extends EntityLum> type, Level level) {
		super(type, level);
	}

	public EntityLum(Level level) {
		this(ModEntities.LUM.get(), level);
	}

	@Override
	public void tick() {
		switch (getLumType()) {
			case "yellow" -> level().addParticle(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, 1F, 1F, 0F), getX(), getY(), getZ(), 0, 0, 0);
			case "green" -> level().addParticle(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, 0F, 1F, 0F), getX(), getY(), getZ(), 0, 0, 0);
			case "red" -> level().addParticle(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, 1F, 0F, 0F), getX(), getY(), getZ(), 0, 0, 0);
			case "blue" -> level().addParticle(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, 0F, 0F, 1F), getX(), getY(), getZ(), 0, 0, 0);
			default -> discard();
		}
		super.tick();
	}

	@Override
	public void playerTouch(Player player) {
		if (this.tickCount > 20) {
			if (!level().isClientSide()) {
				IPlayerCapabilities props = ModCapabilities.get(player);
				switch (getLumType()) {
					case "yellow" -> {
						props.setLums(props.getLums() + 1);
						level().playSound(null, player.blockPosition(), ModSounds.LUM_YELLOW.get(), SoundSource.MASTER, 1F, 1F);
					}
					case "green" -> {
						level().playSound(null, player.blockPosition(), ModSounds.LUM_GREEN.get(), SoundSource.MASTER, 1F, 1F);

					}
					case "red" -> {
						level().playSound(null, player.blockPosition(), ModSounds.LUM_RED.get(), SoundSource.MASTER, 1F, 1F);
						player.heal(2);
					}
					case "blue" -> {

					}
				}
			}
			discard();
		}
		super.playerTouch(player);
	}

	public String getLumType() {
		return this.getEntityData().get(TYPE);
	}

	public void setLumType(String type) {
		this.getEntityData().set(TYPE, type);
	}

	@Override
	protected void addAdditionalSaveData(ValueOutput output) {
		output.putString("type", this.getLumType());
	}

	@Override
	protected void readAdditionalSaveData(ValueInput input) {
		this.setLumType(input.getStringOr("type", "yellow"));
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		builder.define(TYPE, "yellow");
	}

	@Override
	public boolean hurtServer(ServerLevel level, DamageSource damageSource, float amount) {

		return false;
	}
}
