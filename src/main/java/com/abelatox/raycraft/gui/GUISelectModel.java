package com.abelatox.raycraft.gui;

import com.abelatox.raycraft.network.PacketHandler;
import com.abelatox.raycraft.network.packets.PacketSetModel;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.TranslationTextComponent;

public class GUISelectModel extends Screen{
	
	public GUISelectModel() {
        super(new TranslationTextComponent(""));
    }
	
	@Override
	protected void init() {
		buttons.clear();
		int yPos = 120;
		addButton(new Button(this.width / 2 - 100, this.height - 120, 200, 20, new TranslationTextComponent(Minecraft.getInstance().player.getDisplayName().getString()), (e) -> { select(""); }));
		addButton(new Button(this.width / 2 - 100, this.height - 100, 200, 20, new TranslationTextComponent("Rayman"), (e) -> { select("rayman"); }));
		addButton(new Button(this.width / 2 - 100, this.height - 80, 200, 20, new TranslationTextComponent("Red Robopirate"), (e) -> { select("robopirate"); }));
		addButton(new Button(this.width / 2 - 100, this.height - 60, 200, 20, new TranslationTextComponent("Green Robopirate"), (e) -> { select("robopirate2"); }));
		addButton(new Button(this.width / 2 - 100, this.height - 40, 200, 20, new TranslationTextComponent("Globox"), (e) -> { select("globox"); }));
		addButton(new Button(this.width / 2 - 100, this.height - 20, 200, 20, new TranslationTextComponent("Hoodlum"), (e) -> { select("hoodlum"); }));

		super.init();
	}
	
	private void select(String model) {
		PacketHandler.sendToServer(new PacketSetModel(model));
	}

	@Override
	public void render(MatrixStack matrixStack,int mouseX, int mouseY, float partialTicks) {
        renderBackground(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
	}
}
