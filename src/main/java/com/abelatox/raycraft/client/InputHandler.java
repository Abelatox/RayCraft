package com.abelatox.raycraft.client;

import com.abelatox.raycraft.capabilities.IPlayerCapabilities;
import com.abelatox.raycraft.capabilities.ModCapabilities;
import com.abelatox.raycraft.gui.GUISelectModel;
import com.abelatox.raycraft.lib.Constants;
import com.abelatox.raycraft.lib.Reference;
import com.abelatox.raycraft.lib.Utils;
import com.abelatox.raycraft.network.payloads.SetChargingPayload;
import com.abelatox.raycraft.network.payloads.ShootPayload;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.component.SwingAnimation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

import java.util.Optional;

public class InputHandler {
	public static final KeyMapping.Category CATEGORY = new KeyMapping.Category(Identifier.fromNamespaceAndPath(Reference.MODID, Reference.MODID));

	public enum Keybinds {
		SELECT("key.raycraft.select", InputConstants.KEY_P), LOCK_ON("key.raycraft.lockon", InputConstants.KEY_LCONTROL);

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
		Minecraft mc = Minecraft.getInstance();
		LocalPlayer player = mc.player;
		if (player == null || mc.gui.screen() != null || event.getButton() != Constants.LEFT_MOUSE) {
			return;
		}

		IPlayerCapabilities props = ModCapabilities.get(player);

		if (event.getAction() == InputConstants.PRESS) {
			shouldShoot = false;

			if (GrappleHandler.isSwinging()) {
				event.setCanceled(true);
				return;
			}

			if (Utils.canShoot(props.getPlayerType()) && player.getMainHandItem().isEmpty()) {
				time = System.currentTimeMillis();
				shouldShoot = true;
				props.setCharging(true);
				ClientPacketDistributor.sendToServer(new SetChargingPayload(true));
				event.setCanceled(true);
			}
		} else if (event.getAction() == InputConstants.RELEASE && shouldShoot) {
			boolean charged = time + 1000 < System.currentTimeMillis();

			Entity target = LockOnHandler.getTarget() != null ? LockOnHandler.getTarget() : LockOnHandler.getLumTarget();
			ClientPacketDistributor.sendToServer(new ShootPayload(charged, target != null ? target.getId() : -1,
					Optional.ofNullable(LockOnHandler.getCageTarget()), LockOnHandler.getStrafe()));
			shouldShoot = false;
			props.setCharging(false);
			player.swing(InteractionHand.MAIN_HAND, SwingAnimation.DEFAULT, false);
			ClientPacketDistributor.sendToServer(new SetChargingPayload(false));
		}
	}

	private Keybinds getPressedKey() {
		for (Keybinds key : Keybinds.values())
			if (key.isPressed())
				return key;
		return null;
	}
}
