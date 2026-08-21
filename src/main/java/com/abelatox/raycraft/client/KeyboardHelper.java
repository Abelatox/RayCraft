package com.abelatox.raycraft.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

public class KeyboardHelper {

	public static boolean isShiftDown() {
		return isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT) || isKeyDown(GLFW.GLFW_KEY_RIGHT_SHIFT);
	}

	public static boolean isScrollActivatorDown() {
		return isKeyDown(GLFW.GLFW_KEY_LEFT_ALT);
	}

	public static boolean isKeyDown(int key) {
		return InputConstants.isKeyDown(Minecraft.getInstance().getWindow(), key);
	}
}
