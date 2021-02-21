package com.abelatox.raycraft.gui;

import com.abelatox.raycraft.capabilities.ModCapabilities;
import com.abelatox.raycraft.lib.Reference;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class GUIHealth extends Screen {

	public GUIHealth() {
		super(new TranslationTextComponent(""));
	}

	@SubscribeEvent
	public void onRenderOverlayPost(RenderGameOverlayEvent event) {
		Minecraft mc = Minecraft.getInstance();
		PlayerEntity player = mc.player;

		if (event.getType().equals(RenderGameOverlayEvent.ElementType.HEALTH) && event.isCancelable()) {
			event.setCanceled(true);
		}
		if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
			if (player != null && ModCapabilities.get(player) != null) {
				String model = ModCapabilities.get(player).getPlayerType();
				int screenWidth = mc.getMainWindow().getScaledWidth();
				int screenHeight = mc.getMainWindow().getScaledHeight();
				MatrixStack ms = event.getMatrixStack();
				ms.push();
				{

					ms.translate(10, 10, 0);
					ms.scale(1.2F, 1.2F, 1.2F);

					ms.push();
					{// Face
						mc.textureManager.bindTexture(new ResourceLocation(Reference.MODID, "textures/gui/" + model + "_face.png"));

						ms.scale(0.2F, 0.18F, 0.2F);
						blit(event.getMatrixStack(),0, 0, 0, 0, 127, 147);
					}
					ms.pop();

					mc.textureManager.bindTexture(new ResourceLocation(Reference.MODID, "textures/gui/hp.png"));

					ms.translate(30, 10, 0);
					ms.push();
					{// HP Background
						RenderSystem.color4f(1, 1, 1, 1);

						for (int i = 0; i < player.getMaxHealth() * 2; i++)
							blit(event.getMatrixStack(),i, 0, 0, 0, 1, 6);
					}
					ms.pop();

					ms.push();
					{// HP Bar
						RenderSystem.color4f(1, 1, 1, 1);

						for (int i = 0; i < player.getHealth() * 2; i++)
							blit(event.getMatrixStack(),i, 0, 1, 0, 1, 6);
					}
					ms.pop();

				}
				ms.pop();
			}
		}
	}
}
