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
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.component.SwingAnimation;
import net.minecraft.world.phys.HitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

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
		if (player == null || mc.gui.screen() != null || event.getButton() != Constants.RIGHT_MOUSE) {
			return;
		}

		IPlayerCapabilities props = ModCapabilities.get(player);

		if (event.getAction() == InputConstants.PRESS) {
			shouldShoot = false;

			boolean aimingAtNothing = mc.hitResult == null || mc.hitResult.getType() == HitResult.Type.MISS;
			boolean ctrlDown = event.getMouseButtonInfo().hasControlDown();

			if (Utils.canShoot(props.getPlayerType()) && player.getMainHandItem().isEmpty() && (aimingAtNothing || ctrlDown)) {
				time = System.currentTimeMillis();
				shouldShoot = true;
				props.setCharging(true);
				ClientPacketDistributor.sendToServer(new SetChargingPayload(true));
				event.setCanceled(true);
			}
		} else if (event.getAction() == InputConstants.RELEASE && shouldShoot) {
			boolean charged = time + 1000 < System.currentTimeMillis();

			LivingEntity target = LockOnHandler.getTarget();
			ClientPacketDistributor.sendToServer(new ShootPayload(charged, target != null ? target.getId() : -1, LockOnHandler.getStrafe()));
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
