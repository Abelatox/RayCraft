package com.abelatox.raycraft.models.render;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

public interface IRayCraftRender {

	void renderFirstPersonArm(PlayerEntity player);
	void doRender(LivingEntity entityLiving, double x, double y, double z, float u, float v);

}
