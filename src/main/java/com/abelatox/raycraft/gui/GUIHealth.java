package com.abelatox.raycraft.gui;

import org.lwjgl.opengl.GL11;

import com.abelatox.raycraft.capabilities.ModCapabilities;
import com.abelatox.raycraft.lib.Reference;

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

				GL11.glPushMatrix();
				{

					GL11.glTranslated(10, 10, 0);
					GL11.glScaled(1.2, 1.2, 1.2);

					GL11.glPushMatrix();
					{// Face
						mc.textureManager.bindTexture(new ResourceLocation(Reference.MODID, "textures/gui/" + model + "_face.png"));

						GL11.glScaled(0.2, 0.18, 0.2);
						blit(0, 0, 0, 0, 127, 147);
					}
					GL11.glPopMatrix();

					mc.textureManager.bindTexture(new ResourceLocation(Reference.MODID, "textures/gui/hp.png"));

					GL11.glTranslated(30, 10, 0);
					GL11.glPushMatrix();
					{// HP Background
						GL11.glColor4d(1, 1, 1, 1);

						for (int i = 0; i < player.getMaxHealth() * 2; i++)
							blit(i, 0, 0, 0, 1, 6);
					}
					GL11.glPopMatrix();

					GL11.glPushMatrix();
					{// HP Bar
						GL11.glColor4d(1, 1, 1, 1);

						for (int i = 0; i < player.getHealth() * 2; i++)
							blit(i, 0, 1, 0, 1, 6);
					}
					GL11.glPopMatrix();

				}
				GL11.glPopMatrix();
			}
		}
	}
}
