package com.abelatox.raycraft.events;

import com.abelatox.raycraft.capabilities.IPlayerModelCapability;
import com.abelatox.raycraft.capabilities.ModCapabilities;
import com.abelatox.raycraft.items.ModItems;
import com.abelatox.raycraft.lib.Utils;
import com.abelatox.raycraft.models.render.IRayCraftRender;
import com.abelatox.raycraft.network.PacketHandler;
import com.abelatox.raycraft.network.packets.PacketLeftMouse;
import com.abelatox.raycraft.network.packets.PacketRightMouse;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.InputEvent.MouseInputEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ClientEvents {

	@SubscribeEvent
	public void RenderEntity(RenderPlayerEvent.Pre event) {
		if (event.getEntityLiving() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getEntityLiving();

			if (player.inventory.hasItemStack(new ItemStack(ModItems.barrel))) {
				Utils.lockSelectedItem(player, new ItemStack(ModItems.barrel));
			}

			IPlayerModelCapability props = ModCapabilities.get((EntityPlayer) player);
			IRayCraftRender render = Utils.getRender(props);
			if (render != null) {
				event.setCanceled(true);
				render.doRender(event.getEntityLiving(), event.getX(), event.getY(), event.getZ(), 0F, 0.0625F);
			}
		}
	}

	@SubscribeEvent
	public void CustomHandRendering(RenderHandEvent event) {
		EntityPlayer player = Minecraft.getInstance().player;

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

	@SubscribeEvent
	public void MouseClick(MouseInputEvent event) {
		if(event.getAction() == 1) {
			time = System.currentTimeMillis();
		}
		if (event.getAction() == 0) {
			boolean charged = false;
			if(time + 2000 < System.currentTimeMillis()) {
				charged = true;
			}
			if (Minecraft.getInstance().player != null) {
				switch (event.getButton()) {
				case 0:
					PacketHandler.sendToServer(new PacketLeftMouse());
					break;
				case 1:
					PacketHandler.sendToServer(new PacketRightMouse(charged));
					break;
				}
			}
		}
	}
}
