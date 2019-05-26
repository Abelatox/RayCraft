package com.abelatox.raycraft.entities;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Particles;
import net.minecraft.world.World;

public class EntityFist3 extends EntityBaseFist {

	public EntityFist3(World world) {
		super(world);
	}

	public EntityFist3(World worldIn, EntityLivingBase throwerIn, int lvl, boolean explosion) {
		super(ModEntities.TYPE_FIST_3, worldIn, throwerIn, 3);
		this.maxBounces = 3;
		this.power = 16;
		this.explosion = explosion;
	}
	
	@Override
	public void tick() {
		world.spawnParticle(Particles.ENTITY_EFFECT, posX, posY, posZ, 1, 1, 0);
		super.tick();
	}

}
