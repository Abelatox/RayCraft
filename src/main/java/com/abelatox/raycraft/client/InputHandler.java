package com.abelatox.raycraft.client;

import com.abelatox.raycraft.capabilities.IPlayerCapabilities;
import com.abelatox.raycraft.capabilities.ModCapabilities;
import com.abelatox.raycraft.gui.GUISelectModel;
import com.abelatox.raycraft.lib.Constants;
import com.abelatox.raycraft.lib.Reference;
import com.abelatox.raycraft.network.payloads.SetChargingPayload;
import com.abelatox.raycraft.network.payloads.ShootPayload;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import org.lwjgl.glfw.GLFW;

public class InputHandler {
	public static LivingEntity lockOn = null;

	public static final KeyMapping.Category CATEGORY = new KeyMapping.Category(Identifier.fromNamespaceAndPath(Reference.MODID, Reference.MODID));

	public enum Keybinds {
		SELECT("key.raycraft.select", GLFW.GLFW_KEY_P), LOCK_ON("key.raycraft.lockon", GLFW.GLFW_KEY_LEFT_CONTROL);

		private final KeyMapping keybinding;

		Keybinds(String name, int defaultKey) {
			keybinding = new KeyMapping(name, defaultKey, CATEGORY);
		}

		public KeyMapping getKeybind() {
			return keybinding;
		}

		public boolean isPressed() {
			return keybinding.consumeClick();
		}
	}

	@SubscribeEvent
	public void handleKeyInputEvent(InputEvent.Key event) {
		Minecraft mc = Minecraft.getInstance();

		Keybinds key = getPressedKey();
		if (key != null) {
			switch (key) {
				case SELECT:
					mc.gui.setScreen(new GUISelectModel());
					break;

				case LOCK_ON:
					break;
			}
		}
	}

	long time = 0;
	boolean shouldShoot = false;

	@SubscribeEvent
	public void handleMouseInputEvent(InputEvent.MouseButton.Pre event) {
		LocalPlayer player = Minecraft.getInstance().player;
		if (player != null) {
			IPlayerCapabilities props = ModCapabilities.get(player);

			if (Minecraft.getInstance().gui.screen() == null) {
				switch (event.getAction()) {
					case 1 -> {
						if (event.getButton() == Constants.RIGHT_MOUSE) {
							time = System.currentTimeMillis();
							shouldShoot = false;

							if (player.getMainHandItem().isEmpty() && KeyboardHelper.isKeyDown(GLFW.GLFW_KEY_LEFT_CONTROL)) {
								shouldShoot = true;
								props.setCharging(true);
								ClientPacketDistributor.sendToServer(new SetChargingPayload(true));
							}
						}
					}
					case 0 -> {
						if (event.getButton() == Constants.RIGHT_MOUSE) {
							boolean charged = time + 1000 < System.currentTimeMillis();

							if (shouldShoot && KeyboardHelper.isKeyDown(GLFW.GLFW_KEY_LEFT_CONTROL)) {
								ClientPacketDistributor.sendToServer(new ShootPayload(charged));
								shouldShoot = false;
								props.setCharging(false);
								player.swing(InteractionHand.MAIN_HAND);
								ClientPacketDistributor.sendToServer(new SetChargingPayload(false));
							}
						}
					}
					default -> {
					}
				}
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
