package com.abelatox.raycraft.client.gui;

import com.abelatox.raycraft.capabilities.IPlayerCapabilities;
import com.abelatox.raycraft.capabilities.ModCapabilities;
import com.abelatox.raycraft.lib.Reference;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.client.gui.GuiLayer;

/**
 * Reemplazo del HUD de vida vanilla: dibuja la cara del personaje seleccionado y una
 * barra de vida propia, igual que {@code GUIHealth} en el mod original de 1.16.4 (que
 * cancelaba el overlay de corazones y pintaba su propio HUD en la esquina superior
 * izquierda).
 */
public class RaycraftHealthOverlay implements GuiLayer {

	private static final int X = 10;
	private static final int Y = 10;
	private static final int FACE_SIZE = 26;
	private static final int FACE_TEX_W = 127;
	private static final int FACE_TEX_H = 147;

	@Override
	public void render(GuiGraphicsExtractor guiGraphics, DeltaTracker deltaTracker) {
		Minecraft mc = Minecraft.getInstance();
		Player player = mc.player;
		if (player == null) {
			return;
		}

		IPlayerCapabilities props = ModCapabilities.get(player);
		if (props == null || !props.hasCustomPlayerType()) {
			return;
		}

		String model = props.getPlayerType();
		Identifier faceTexture = Identifier.fromNamespaceAndPath(Reference.MODID, "textures/gui/" + model + "_face.png");

		guiGraphics.blit(faceTexture, X, Y, 0, 0, FACE_SIZE, FACE_SIZE, FACE_TEX_W, FACE_TEX_H);

		int barX = X + FACE_SIZE + 6;
		int barY = Y + FACE_SIZE / 2 - 3;
		int maxHealth = Math.max(1, Math.round(player.getMaxHealth()));
		int health = Math.max(0, Math.round(player.getHealth()));

		guiGraphics.fill(barX, barY, barX + maxHealth * 2, barY + 6, 0x80000000);
		guiGraphics.fill(barX, barY, barX + health * 2, barY + 6, 0xFFCC0000);
	}
}
