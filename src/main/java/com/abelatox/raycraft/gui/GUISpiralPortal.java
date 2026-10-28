package com.abelatox.raycraft.gui;

import com.abelatox.raycraft.entities.EntitySpiralPortal;
import com.abelatox.raycraft.network.payloads.OpenPortalConfigPayload;
import com.abelatox.raycraft.network.payloads.SetPortalDestinationPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

import java.util.List;

public class GUISpiralPortal extends Screen {

	private final OpenPortalConfigPayload config;
	private final List<String> levels;
	private int levelIndex;
	private boolean red;
	private float size;
	private EditBox x;
	private EditBox y;
	private EditBox z;
	private Button levelButton;

	public static void open(OpenPortalConfigPayload config) {
		Minecraft.getInstance().gui.setScreen(new GUISpiralPortal(config));
	}

	public GUISpiralPortal(OpenPortalConfigPayload config) {
		super(Component.translatable("gui.raycraft.spiral_portal"));
		this.config = config;
		this.levels = config.levels();
		this.levelIndex = Math.max(0, levels.indexOf(config.level()));
		this.red = config.red();
		this.size = config.size();
	}

	private static boolean isNumber(String text) {
		try {
			Double.parseDouble(text);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	@Override
	protected void init() {
		int left = width / 2 - 100;
		int top = height / 2 - 50;
		x = coordinate(left, top, "X", config.x());
		y = coordinate(left + 70, top, "Y", config.y());
		z = coordinate(left + 140, top, "Z", config.z());
		levelButton = addRenderableWidget(Button.builder(levelLabel(), b -> {
			levelIndex = (levelIndex + 1) % levels.size();
			b.setMessage(levelLabel());
		}).bounds(left, top + 25, 200, 20).build());
		addRenderableWidget(Button.builder(Component.translatable("gui.raycraft.spiral_portal.here"), b -> useMyPosition()).bounds(left, top + 50, 200, 20).build());
		addRenderableWidget(Button.builder(colorLabel(), b -> {
			red = !red;
			b.setMessage(colorLabel());
		}).bounds(left, top + 75, 200, 20).build());
		addRenderableWidget(new ValueSlider(left, top + 100, EntitySpiralPortal.MIN_SIZE, EntitySpiralPortal.MAX_SIZE, 0.5F, size, "gui.raycraft.spiral_portal.size", value -> size = value));
		addRenderableWidget(Button.builder(Component.translatable("gui.done"), b -> save()).bounds(left, top + 125, 200, 20).build());
	}

	private EditBox coordinate(int left, int top, String axis, double value) {
		EditBox box = addRenderableWidget(new EditBox(font, left, top, 60, 20, Component.literal(axis)));
		box.setHint(Component.literal(axis));
		box.setValue(String.format(java.util.Locale.ROOT, "%.1f", value));
		return box;
	}

	private Component colorLabel() {
		return Component.translatable("gui.raycraft.spiral_portal.color", Component.translatable(red ? "gui.raycraft.spiral_portal.red" : "gui.raycraft.spiral_portal.blue"));
	}

	private Component levelLabel() {
		return Component.translatable("gui.raycraft.spiral_portal.dimension", levels.get(levelIndex));
	}

	private void useMyPosition() {
		LocalPlayer player = Minecraft.getInstance().player;
		x.setValue(String.format(java.util.Locale.ROOT, "%.1f", player.getX()));
		y.setValue(String.format(java.util.Locale.ROOT, "%.1f", player.getY()));
		z.setValue(String.format(java.util.Locale.ROOT, "%.1f", player.getZ()));
		levelIndex = Math.max(0, levels.indexOf(player.level().dimension().identifier().toString()));
		levelButton.setMessage(levelLabel());
	}

	private void save() {
		try {
			ClientPacketDistributor.sendToServer(new SetPortalDestinationPayload(config.portalId(), Double.parseDouble(x.getValue()), Double.parseDouble(y.getValue()), Double.parseDouble(z.getValue()), levels.get(levelIndex), red, size));
			onClose();
		} catch (NumberFormatException e) {
			for (EditBox box : List.of(x, y, z)) {
				box.setTextColor(isNumber(box.getValue()) ? 0xFFE0E0E0 : 0xFFFF5555);
			}
		}
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}
}
