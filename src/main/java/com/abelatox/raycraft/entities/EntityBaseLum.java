package com.abelatox.raycraft.entities;

import com.abelatox.raycraft.entities.EntityBaseLum.TYPE;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class EntityBaseLum extends Entity {
	
	enum TYPE {
		YELLOW,RED,GREEN,BLUE	
	};
	
	TYPE lumType;
	
	public EntityBaseLum(EntityType<? extends Entity> type, World world, TYPE lumType) {
		super(type, world);
		this.preventEntitySpawning = true;
		this.lumType = lumType;
	}

	public EntityBaseLum(World worldIn) {
		super(ModEntities.TYPE_YELLOW_LUM, worldIn);
	}
	
	public EntityBaseLum(EntityType<EntityBaseLum> type, World world) {
		super(type, world);
		
		this.preventEntitySpawning = true;
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	protected void registerData() {

	}

	@Override
	protected void readAdditional(CompoundNBT compound) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void writeAdditional(CompoundNBT compound) {
		// TODO Auto-generated method stub
		
	}
}
