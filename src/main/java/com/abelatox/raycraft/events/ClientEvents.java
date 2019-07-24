package com.abelatox.raycraft.events;

import com.abelatox.raycraft.capabilities.IPlayerModelCapability;
import com.abelatox.raycraft.capabilities.ModCapabilities;
import com.abelatox.raycraft.items.ModItems;
import com.abelatox.raycraft.lib.Utils;
import com.abelatox.raycraft.models.render.IRayCraftRender;
import com.abelatox.raycraft.network.PacketHandler;
import com.abelatox.raycraft.network.packets.PacketLeftMouse;
import com.abelatox.raycraft.network.packets.PacketRightMouse;
import com.abelatox.raycraft.network.packets.PacketSyncCapabilityToAllFromClient;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.InputEvent.MouseInputEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ClientEvents {

	// Register the entity models
	
	@SubscribeEvent
	public void RenderEntity(RenderPlayerEvent.Pre event) {
		if (event.getEntityLiving() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) event.getEntityLiving();

			if (player.inventory.hasItemStack(new ItemStack(ModItems.barrel))) {
				Utils.lockSelectedItem(player, new ItemStack(ModItems.barrel));
			}

			IPlayerModelCapability props = ModCapabilities.get((PlayerEntity) player);
			IRayCraftRender render = Utils.getRender(props);
			if (render != null) {
				event.setCanceled(true);
				render.doRender(event.getEntityLiving(), event.getX(), event.getY(), event.getZ(), 0F, 0.0625F);
			}
		}
	}

	@SubscribeEvent
	public void CustomHandRendering(RenderHandEvent event) {
		PlayerEntity player = Minecraft.getInstance().player;

		if (player.inventory.hasItemStack(new ItemStack(ModItems.barrel))) {
			Utils.lockSelectedItem(player, new ItemStack(ModItems.barrel));
		}

		IPlayerModelCapability props = ModCapabilities.get(player);
		IRayCraftRender render = Utils.getRender(props);

		if (ItemStack.areItemStacksEqual(player.getHeldItemMainhand(), ItemStack.EMPTY) && render != null) {
			event.setCanceled(true);
			render.renderFirstPersonArm(player);
		}
	}

	long time = 0;
	boolean shouldShoot = false;

	@SubscribeEvent
	public void MouseClick(MouseInputEvent event) {
		ClientPlayerEntity player = Minecraft.getInstance().player;
		if (player != null) {
			IPlayerModelCapability props = ModCapabilities.get(player);

			if (Minecraft.getInstance().currentScreen == null) {
				switch (event.getAction()) {
				case 1:
					time = System.currentTimeMillis();
					// If empty hand should shoot, if not it shouldn't (barrel + fist)
					shouldShoot = false;

					if (player != null && ItemStack.areItemStacksEqual(player.getHeldItemMainhand(), ItemStack.EMPTY)) {
						shouldShoot = true;
						props.setCharging(true);
					}
					break;
				case 0:
					boolean charged = false;
					if (time + 1000 < System.currentTimeMillis()) {
						charged = true;
					}

					switch (event.getButton()) {
					case 0:
						PacketHandler.sendToServer(new PacketLeftMouse());
						break;
					case 1:
						if (shouldShoot) {
							PacketHandler.sendToServer(new PacketRightMouse(charged));
							props.setCharging(false);
						}
						break;
					}
					break;
				}

				// PacketHandler.syncToAllAround(player, props);
				PacketHandler.sendToServer(new PacketSyncCapabilityToAllFromClient());
			}
		}
	}
}
