package com.abelatox.raycraft.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.world.World;

public class EntityYellowLum extends EntityBaseLum {

	public EntityYellowLum(EntityType<? extends Entity> type, World world, TYPE lumType) {
		super(type, world, TYPE.YELLOW);
		this.preventEntitySpawning = true;
		this.lumType = lumType;
	}

	public EntityYellowLum(World worldIn) {
		super(ModEntities.TYPE_YELLOW_LUM, worldIn, TYPE.YELLOW);
		// TODO Auto-generated constructor stub
	}

	public EntityYellowLum(EntityType<EntityYellowLum> type, World world) {
		super(type, world, TYPE.YELLOW);
		this.preventEntitySpawning = true;
	}

	@Override
	public void tick() {
		for (double x = -0.5; x < 0.5; x+=0.3) {
			for (double y = -0.5; y < 0.5; y+=0.3) {
				for (double z = -0.5; z < 0.5; z+=0.3) {
					world.addParticle(ParticleTypes.HAPPY_VILLAGER, posX + x+0.1, posY + y+0.1, posZ + z+0.1, 0, 0, 0);
				}
			}
		}
		super.tick();
	}

	@Override
	public void onCollideWithPlayer(PlayerEntity entityIn) {
		System.out.println(entityIn);
		super.onCollideWithPlayer(entityIn);
	}

}
