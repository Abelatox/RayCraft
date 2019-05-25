package com.abelatox.raycraft.entities;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public class EntityFist1 extends EntityBaseFist {

	public EntityFist1(World world) {
		super(world);
	}

	public EntityFist1(World worldIn, EntityLivingBase throwerIn, int lvl, boolean charged) {
		super(ModEntities.TYPE_FIST_1, worldIn, throwerIn, 0, charged);
		this.maxBounces = 0;
		this.power = 4;
		if (charged)
			power *= 2;
	}

	private void loadData() {
		switch (lvl) {
		case 0: // First punch, no bounces
			maxBounces = 0;
			power = 2;
			break;
		case 1: // Blue punch, grab purple lums, 1 bounce
			maxBounces = 1;
			power = 4;
			break;
		case 2: // Yellow punch, 2 bounces, explode if charged
			maxBounces = 2;
			power = 8;
			break;
		case 3: // Golden punch, 3 bounces, explode if charged
			maxBounces = 3;
			power = 20;
			break;
		}

		if (charged) {
			power *= 2;
		}
	}
}
