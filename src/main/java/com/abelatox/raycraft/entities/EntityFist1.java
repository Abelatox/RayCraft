package com.abelatox.raycraft.entities;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public class EntityFist1 extends EntityBaseFist {

	public EntityFist1(World world) {
		super(world);
	}

	public EntityFist1(World worldIn, EntityLivingBase throwerIn) {
		super(ModEntities.TYPE_FIST_1, worldIn, throwerIn, 1);
		this.maxBounces = 1;
		this.power = 4;
		this.maxTicks = 80;
	}

}
