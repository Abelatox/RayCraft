package com.abelatox.raycraft.entities;

import com.abelatox.raycraft.capabilities.IPlayerCapabilities;
import com.abelatox.raycraft.capabilities.ModCapabilities;
import com.abelatox.raycraft.sounds.ModSounds;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class EntityLum extends Entity {

	private static final DataParameter<String> TYPE = EntityDataManager.createKey(EntityLum.class, DataSerializers.STRING);
	String lumType;

	public EntityLum(EntityType<? extends Entity> type, World world, String lumType) {
		super(type, world);
		this.preventEntitySpawning = true;
		this.lumType = lumType;
	}

	public EntityLum(World worldIn) {
		super(ModEntities.TYPE_LUM, worldIn);
	}

	public EntityLum(EntityType<EntityLum> type, World world) {
		super(type, world);
		this.preventEntitySpawning = true;
	}

	@Override
	public void tick() {
		switch (lumType) {
		case "yellow":
			world.addParticle(ParticleTypes.ENTITY_EFFECT, getPosX(), getPosY(), getPosZ(), 1, 1, 0);
			break;
		case "green":
			world.addParticle(ParticleTypes.ENTITY_EFFECT, getPosX(), getPosY(), getPosZ(), 0, 1, 0);
			break;
		case "red":
			world.addParticle(ParticleTypes.ENTITY_EFFECT, getPosX(), getPosY(), getPosZ(), 1, 0, 0);
			break;
		case "blue":
			world.addParticle(ParticleTypes.ENTITY_EFFECT, getPosX(), getPosY(), getPosZ(), 0, 0, 1);
			break;
		default:
			remove();
			break;
		}
		super.tick();
	}

	@Override
	public void onCollideWithPlayer(PlayerEntity player) {
		if (this.ticksExisted > 20) {
			if (!world.isRemote) {
				System.out.println(lumType);
				IPlayerCapabilities props = ModCapabilities.get(player);
				switch (lumType) {
				case "yellow":
					props.setLums(props.getLums() + 1);
					player.world.playSound(null, player.getPosition(), ModSounds.lumYellow, SoundCategory.MASTER, 1F, 1F);

					player.sendMessage(new TranslationTextComponent("Lums: " + props.getLums()));
					System.out.println("+1 point");
					break;
				case "green":
					player.world.playSound(null, player.getPosition(), ModSounds.lumGreen, SoundCategory.MASTER, 1F, 1F);
					player.setSpawnPoint(this.getPosition(), true, true, this.dimension);
					System.out.println("Spawn setting");
					break;
				case "red":
					System.out.println("+2 hp");
					player.world.playSound(null, player.getPosition(), ModSounds.lumRed, SoundCategory.MASTER, 1F, 1F);
					player.heal(2);
					break;
				case "blue":
					System.out.println("+1 oxygen");
					break;
				}
			}
			remove();
		}
		super.onCollideWithPlayer(player);
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	public String getLumType() {
		return lumType;
	}

	public void setLumType(String type) {
		this.lumType = type;
		this.dataManager.set(TYPE, type);
	}

	@Override
	public void writeAdditional(CompoundNBT compound) {
		compound.putString("type", this.getLumType());
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		this.setLumType(compound.getString("type"));
	}

	@Override
	public void notifyDataManagerChange(DataParameter<?> key) {
		if (key.equals(TYPE)) {
			this.lumType = this.getTypeDataManager();
		}
	}

	@Override
	protected void registerData() {
		this.dataManager.register(TYPE, "yellow");
	}

	public String getTypeDataManager() {
		return this.dataManager.get(TYPE);
	}
}
