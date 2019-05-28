package com.abelatox.raycraft.entities;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Particles;
import net.minecraft.world.World;

public class EntityFist4 extends EntityBaseFist {

	public EntityFist4(World world) {
		super(world);
	}

	public EntityFist4(World worldIn, EntityLivingBase throwerIn, boolean explosion) {
		super(ModEntities.TYPE_FIST_4, worldIn, throwerIn, 4);
		this.maxBounces = 0;
		this.power = 30;
		this.explosion = explosion;
		this.maxTicks = 140;
	}
	
	@Override
	public void tick() {
		world.addParticle(Particles.ENTITY_EFFECT, posX, posY, posZ, 1, 1, 0);
		super.tick();
	}
}
