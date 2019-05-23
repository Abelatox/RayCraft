package com.abelatox.raycraft.models;

import com.abelatox.raycraft.models.render.RenderPirate;
import com.abelatox.raycraft.models.render.RenderRayman;

import net.minecraft.client.Minecraft;

public class ModModels {

	public static RenderPirate renderPirate;
	public static RenderRayman renderRayman;
	
	public static void register() {
		renderPirate = new RenderPirate(Minecraft.getInstance().getRenderManager(), new ModelRoboPirate(), 1);
		renderRayman = new RenderRayman(Minecraft.getInstance().getRenderManager(), new ModelRayman(), 1);
	}
}
