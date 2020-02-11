package com.abelatox.raycraft.models;

import com.abelatox.raycraft.models.render.RenderGlobox;
import com.abelatox.raycraft.models.render.RenderHoodlum;
import com.abelatox.raycraft.models.render.RenderPirate;
import com.abelatox.raycraft.models.render.RenderRayman;

import net.minecraft.client.Minecraft;

public class ModModels {

	public static RenderPirate renderPirate;
	public static RenderRayman renderRayman;	
	public static RenderGlobox renderGlobox;
	public static RenderHoodlum renderHoodlum;
	
	public static void register() {
		renderPirate = new RenderPirate(Minecraft.getInstance().getRenderManager(), new ModelRoboPirate(1), 1);
		renderRayman = new RenderRayman(Minecraft.getInstance().getRenderManager(), new ModelRayman(1), 1);
		renderGlobox = new RenderGlobox(Minecraft.getInstance().getRenderManager(), new ModelGlobox(1), 1);
		renderHoodlum = new RenderHoodlum(Minecraft.getInstance().getRenderManager(), new ModelHoodlum(1), 1);
	}
}
