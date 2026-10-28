package com.abelatox.raycraft.gui;

import com.abelatox.raycraft.entities.EntityWindCurrent;
import com.abelatox.raycraft.network.payloads.OpenWindConfigPayload;
import com.abelatox.raycraft.network.payloads.SetWindConfigPayload;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

public class GUIWindCurrent extends Screen {

	private final int windId;
	private float height;
	private float radius;

	public GUIWindCurrent(OpenWindConfigPayload config) {
		super(Component.translatable("gui.raycraft.wind_current"));
		this.windId = config.windId();
		this.height = config.height();
		this.radius = config.radius();
	}

	public static void open(OpenWindConfigPayload config) {
		Minecraft.getInstance().gui.setScreen(new GUIWindCurrent(config));
	}

	@Override
	protected void init() {
		int left = width / 2 - 100;
		int top = height() / 2 - 35;
		addRenderableWidget(new ValueSlider(left, top, EntityWindCurrent.MIN_HEIGHT, EntityWindCurrent.MAX_HEIGHT, 0.5F, height, "gui.raycraft.wind_current.height", value -> height = value));
		addRenderableWidget(new ValueSlider(left, top + 25, EntityWindCurrent.MIN_RADIUS, EntityWindCurrent.MAX_RADIUS, 0.5F, radius, "gui.raycraft.wind_current.radius", value -> radius = value));
		addRenderableWidget(Button.builder(Component.translatable("gui.done"), b -> {
			ClientPacketDistributor.sendToServer(new SetWindConfigPayload(windId, height, radius));
			onClose();
		}).bounds(left, top + 50, 200, 20).build());
	}

	private int height() {
		return super.height;
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}
}
