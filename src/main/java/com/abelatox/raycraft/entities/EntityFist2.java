package com.abelatox.raycraft.entities;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public class EntityFist2 extends EntityBaseFist {

	public EntityFist2(World world) {
		super(world);
	}

	public EntityFist2(World worldIn, EntityLivingBase throwerIn) {
		super(ModEntities.TYPE_FIST_2, worldIn, throwerIn, 2);
		this.maxBounces = 2;
		this.power = 8;
		this.maxTicks = 100;
	}

}
