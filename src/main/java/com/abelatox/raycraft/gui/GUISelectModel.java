package com.abelatox.raycraft.gui;

import com.abelatox.raycraft.network.PacketHandler;
import com.abelatox.raycraft.network.packets.PacketSetModel;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.PacketDispatcher;

public class GUISelectModel extends Screen{

	Button ok;
	
	public GUISelectModel() {
        super(new TranslationTextComponent(""));
    }
	
	@Override
	protected void init() {
		buttons.clear();
		int yPos = 120;
		addButton(new Button(this.width / 2 - 100, this.height - 120, 200, 20, Minecraft.getInstance().player.getDisplayName().getFormattedText()+"", (e) -> { select(""); }));
		addButton(new Button(this.width / 2 - 100, this.height - 100, 200, 20, "Rayman", (e) -> { select("rayman"); }));
		addButton(new Button(this.width / 2 - 100, this.height - 80, 200, 20, "Red Robopirate", (e) -> { select("robopirate"); }));
		addButton(new Button(this.width / 2 - 100, this.height - 60, 200, 20, "Green Robopirate", (e) -> { select("robopirate2"); }));
		addButton(new Button(this.width / 2 - 100, this.height - 40, 200, 20, "Globox", (e) -> { select("globox"); }));
		addButton(new Button(this.width / 2 - 100, this.height - 20, 200, 20, "Hoodlum", (e) -> { select("hoodlum"); }));
        //buttons.add(ok = new Button(0, 0, 0, 50, 20, "OK", ));

		super.init();
	}
	
	private void select(String model) {
		PacketHandler.sendToServer(new PacketSetModel(model));
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
        renderBackground();
		super.render(mouseX, mouseY, partialTicks);
	}
}
