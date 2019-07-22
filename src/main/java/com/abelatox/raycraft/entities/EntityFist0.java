package com.abelatox.raycraft.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

public class EntityFist0 extends EntityBaseFist {

    public EntityFist0(EntityType<? extends Entity> type, World world) {
		super(world);
	}

	public EntityFist0(World worldIn, LivingEntity throwerIn) {
		super(ModEntities.TYPE_FIST_0, worldIn, throwerIn, 0);
		this.maxBounces = 0;
		this.power = 2;
		this.maxTicks = 60;
	}
}
