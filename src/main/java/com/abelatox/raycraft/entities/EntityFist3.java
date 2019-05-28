package com.abelatox.raycraft.entities;

import com.abelatox.raycraft.sounds.ModSounds;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Particles;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class EntityFist3 extends EntityBaseFist {

	public EntityFist3(World world) {
		super(world);
	}

	public EntityFist3(World worldIn, EntityLivingBase throwerIn, boolean explosion) {
		super(ModEntities.TYPE_FIST_3, worldIn, throwerIn, 3);
		this.maxBounces = 3;
		this.power = 16;
		this.explosion = explosion;
		this.maxTicks = 120;
	}
	
	@Override
	public void tick() {
		world.addParticle(Particles.ENTITY_EFFECT, posX, posY, posZ, 1, 1, 0);
		super.tick();
	}

}
