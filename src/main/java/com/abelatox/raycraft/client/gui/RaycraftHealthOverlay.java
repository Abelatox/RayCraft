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

import java.util.HashMap;
import java.util.Map;

/**
 * Wraps the vanilla health HUD: with a RayCraft character it draws the character's face and
 * its own health bar, like {@code GUIHealth} in the original 1.16.4 mod; without a
 * character it keeps the vanilla hearts.
 */
public class RaycraftHealthOverlay implements GuiLayer {

	private static final float HUD_SCALE = 1.2F;
	private static final int HUD_X = 10;
	private static final int HUD_Y = 10;

	private static final int GUI_TEX_SIZE = 256;
	private static final int FACE_U_SIZE = 127;
	private static final int FACE_V_SIZE = 147;
	private static final int FACE_W = Math.round(FACE_U_SIZE * 0.2F);
	private static final int FACE_H = Math.round(FACE_V_SIZE * 0.18F);

	// hp.png: column 0 is the bar background and column 1 the fill.
	private static final Identifier HP_TEXTURE = Identifier.fromNamespaceAndPath(Reference.MODID, "textures/gui/hp.png");
	private static final Identifier FALLBACK_FACE = Identifier.fromNamespaceAndPath(Reference.MODID, "textures/gui/face_happy.png");
	private static final int BAR_X = 30;
	private static final int BAR_Y = 10;
	private static final int BAR_H = 6;
	private static final int PIXELS_PER_HP = 2;

	private static final Map<String, Identifier> FACES = new HashMap<>();

	private final GuiLayer vanillaHealth;

	public RaycraftHealthOverlay(GuiLayer vanillaHealth) {
		this.vanillaHealth = vanillaHealth;
	}

	@Override
	public void render(GuiGraphicsExtractor guiGraphics, DeltaTracker deltaTracker) {
		Minecraft mc = Minecraft.getInstance();
		Player player = mc.player;
		IPlayerCapabilities props = player != null ? ModCapabilities.get(player) : null;

		if (props == null || !props.hasCustomPlayerType()) {
			vanillaHealth.render(guiGraphics, deltaTracker);
			return;
		}

		guiGraphics.pose().pushMatrix();
		guiGraphics.pose().translate(HUD_X, HUD_Y);
		guiGraphics.pose().scale(HUD_SCALE, HUD_SCALE);

		guiGraphics.blit(getFace(mc, props.getPlayerType()), 0, 0, FACE_W, FACE_H,
				0F, (float) FACE_U_SIZE / GUI_TEX_SIZE, 0F, (float) FACE_V_SIZE / GUI_TEX_SIZE);

		int maxWidth = Math.max(1, Math.round(player.getMaxHealth() * PIXELS_PER_HP));
		int width = Math.clamp(Math.round(player.getHealth() * PIXELS_PER_HP), 0, maxWidth);
		float barV = (float) BAR_H / GUI_TEX_SIZE;

		guiGraphics.blit(HP_TEXTURE, BAR_X, BAR_Y, BAR_X + maxWidth, BAR_Y + BAR_H, 0F, 1F / GUI_TEX_SIZE, 0F, barV);
		if (width > 0) {
			guiGraphics.blit(HP_TEXTURE, BAR_X, BAR_Y, BAR_X + width, BAR_Y + BAR_H, 1F / GUI_TEX_SIZE, 2F / GUI_TEX_SIZE, 0F, barV);
		}

		guiGraphics.pose().popMatrix();
	}

	private static Identifier getFace(Minecraft mc, String playerType) {
		return FACES.computeIfAbsent(playerType, type -> {
			Identifier face = Identifier.fromNamespaceAndPath(Reference.MODID, "textures/gui/" + type + "_face.png");
			return mc.getResourceManager().getResource(face).isPresent() ? face : FALLBACK_FACE;
		});
	}
}
