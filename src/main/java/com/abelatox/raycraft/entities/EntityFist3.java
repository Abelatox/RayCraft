package com.abelatox.raycraft.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.world.World;

public class EntityFist3 extends EntityBaseFist {

	public EntityFist3(EntityType<? extends Entity> type, World world) {
		super(world);
	}
    
    public EntityFist3(World worldIn) {
		super(ModEntities.TYPE_FIST_3,worldIn);
	}

	public EntityFist3(World worldIn, LivingEntity throwerIn, boolean explosion) {
		super(ModEntities.TYPE_FIST_3, worldIn, throwerIn, 3);
		this.maxBounces = 3;
		this.power = 16;
		this.explosion = explosion;
		this.maxTicks = 120;
	}
	
	@Override
	public void tick() {
		world.addParticle(ParticleTypes.ENTITY_EFFECT, posX, posY, posZ, 1, 1, 0);
		super.tick();
	}

}
