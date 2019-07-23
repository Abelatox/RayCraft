package com.abelatox.raycraft.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

public class EntityFist2 extends EntityBaseFist {

	public EntityFist2(EntityType<? extends Entity> type, World world) {
		super(world);
	}
    
    public EntityFist2(World worldIn) {
		super(ModEntities.TYPE_FIST_2,worldIn);
	}

	public EntityFist2(World worldIn, LivingEntity throwerIn) {
		super(ModEntities.TYPE_FIST_2, worldIn, throwerIn, 2);
		this.maxBounces = 2;
		this.power = 8;
		this.maxTicks = 100;
	}

}
