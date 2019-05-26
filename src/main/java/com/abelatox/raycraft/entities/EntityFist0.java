package com.abelatox.raycraft.entities;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public class EntityFist0 extends EntityBaseFist {

	public EntityFist0(World world) {
		super(world);
	}

	public EntityFist0(World worldIn, EntityLivingBase throwerIn, int lvl) {
		super(ModEntities.TYPE_FIST, worldIn, throwerIn, 0);
		this.maxBounces = 0;
		this.power = 2;
	}
}
