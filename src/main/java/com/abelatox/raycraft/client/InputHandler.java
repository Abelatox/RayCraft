package com.abelatox.raycraft.client;

import org.lwjgl.glfw.GLFW;

import com.abelatox.raycraft.gui.GUISelectModel;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class InputHandler {
	public enum Keybinds {
		SELECT("key.raycraft.select", GLFW.GLFW_KEY_P);

		private final KeyBinding keybinding;

		Keybinds(String name, int defaultKey) {
			keybinding = new KeyBinding(name, defaultKey, "key.categories.raycraft");
		}

		public KeyBinding getKeybind() {
			return keybinding;
		}

		public boolean isPressed() {
			return keybinding.isPressed();
		}
	}

	@SubscribeEvent
	public void handleKeyInputEvent(InputEvent.KeyInputEvent event) {
		Minecraft mc = Minecraft.getInstance();
		PlayerEntity player = mc.player;
		World world = mc.world;

		Keybinds key = getPressedKey();
		if (key != null) {
			switch (key) {
			case SELECT:
				mc.displayGuiScreen(new GUISelectModel());
				break;
			}
		}
	}

	 private Keybinds getPressedKey() {
        for (Keybinds key : Keybinds.values())
            if (key.isPressed())
                return key;
        return null;
    }
}
