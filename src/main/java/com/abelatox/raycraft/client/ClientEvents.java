package com.abelatox.raycraft.client;

import com.abelatox.raycraft.capabilities.IPlayerCapabilities;
import com.abelatox.raycraft.capabilities.ModCapabilities;
import com.abelatox.raycraft.items.ModItems;
import com.abelatox.raycraft.lib.Utils;
import com.abelatox.raycraft.models.render.IRayCraftRender;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
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

			IPlayerCapabilities props = ModCapabilities.get((PlayerEntity) player);
			IRayCraftRender render = Utils.getRender(props);
			if (render != null) {
				event.setCanceled(true);
				render.doRender(event.getEntityLiving(), 0.0625F, event.getMatrixStack(), event.getBuffers(), event.getLight(), OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
			}
		}
	}

	@SubscribeEvent
	public void CustomHandRendering(RenderHandEvent event) {
		PlayerEntity player = Minecraft.getInstance().player;

		if (player.inventory.hasItemStack(new ItemStack(ModItems.barrel))) {
			Utils.lockSelectedItem(player, new ItemStack(ModItems.barrel));
		}

		IPlayerCapabilities props = ModCapabilities.get(player);
		IRayCraftRender render = Utils.getRender(props);

		if (ItemStack.areItemStacksEqual(player.getHeldItemMainhand(), ItemStack.EMPTY) && render != null) {
			event.setCanceled(true);
			render.renderFirstPersonArm(player, event.getMatrixStack(), event.getBuffers(), event.getLight());
		}
	}

	long time = 0;
	boolean shouldShoot = false;

	/*@SubscribeEvent
	public void MouseClick(MouseInputEvent event) {
		ClientPlayerEntity player = Minecraft.getInstance().player;
		if (player != null) {
			IPlayerCapabilities props = ModCapabilities.get(player);

			/*if (Minecraft.getInstance().currentScreen == null) {
				switch (event.getAction()) {
				case 1: //Press
					time = System.currentTimeMillis();
					// If empty hand should shoot, if not it shouldn't (barrel + fist)
					shouldShoot = false;

					if (player != null && ItemStack.areItemStacksEqual(player.getHeldItemMainhand(), ItemStack.EMPTY)) {
						shouldShoot = true;
						props.setCharging(true);
						//PacketHandler.sendToServer(new PacketPlaySound("pirateShot"));
						//player.world.playSound(player.posX, player.posY, player.posZ, ModSounds.pirateShot2, SoundCategory.PLAYERS, 1F, 1F, false);
					}
					break;
				case 0: //Release
					boolean charged = false;
					if (time + 1000 < System.currentTimeMillis()) {
						charged = true;
					}

					switch (event.getButton()) {
					case 1:
						//PacketHandler.sendToServer(new PacketSecondaryAction());
						//Minecraft.getInstance().displayGuiScreen(new GUISelectModel());
						break;
					case 0:
						if (shouldShoot) {
							PacketHandler.sendToServer(new PacketShoot(charged));
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
	}*/
}
