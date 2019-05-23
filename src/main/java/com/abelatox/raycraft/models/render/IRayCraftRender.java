package com.abelatox.raycraft.models.render;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public interface IRayCraftRender {

	void renderFirstPersonArm(EntityPlayer player);
	void doRender(EntityLivingBase entityLiving, double x, double y, double z, float u, float v);

}
