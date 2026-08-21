package com.abelatox.raycraft.gui;

import com.abelatox.raycraft.network.payloads.SetModelPayload;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

public class GUISelectModel extends Screen {

	public GUISelectModel() {
		super(Component.empty());
	}

	@Override
	protected void init() {
		clearWidgets();
		addRenderableWidget(Button.builder(Component.literal(Minecraft.getInstance().player.getDisplayName().getString()), b -> select("")).bounds(this.width / 2 - 100, this.height - 120, 200, 20).build());
		addRenderableWidget(Button.builder(Component.literal("Rayman"), b -> select("rayman")).bounds(this.width / 2 - 100, this.height - 100, 200, 20).build());
		addRenderableWidget(Button.builder(Component.literal("Red Robopirate"), b -> select("robopirate")).bounds(this.width / 2 - 100, this.height - 80, 200, 20).build());
		addRenderableWidget(Button.builder(Component.literal("Green Robopirate"), b -> select("robopirate2")).bounds(this.width / 2 - 100, this.height - 60, 200, 20).build());
		addRenderableWidget(Button.builder(Component.literal("Globox"), b -> select("globox")).bounds(this.width / 2 - 100, this.height - 40, 200, 20).build());
		addRenderableWidget(Button.builder(Component.literal("Hoodlum"), b -> select("hoodlum")).bounds(this.width / 2 - 100, this.height - 20, 200, 20).build());

		super.init();
	}

	private void select(String model) {
		ClientPacketDistributor.sendToServer(new SetModelPayload(model));
	}
}
