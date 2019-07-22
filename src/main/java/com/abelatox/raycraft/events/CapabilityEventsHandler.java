package com.abelatox.raycraft.events;

import com.abelatox.raycraft.capabilities.IPlayerModelCapability;
import com.abelatox.raycraft.capabilities.ModCapabilities;
import com.abelatox.raycraft.capabilities.PlayerModelProvider;
import com.abelatox.raycraft.lib.Reference;
import com.abelatox.raycraft.network.PacketHandler;
import com.abelatox.raycraft.network.packets.PacketSyncCapability;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CapabilityEventsHandler {

	@SubscribeEvent
	public void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
		if (event.getObject() instanceof PlayerEntity) {
			event.addCapability(new ResourceLocation(Reference.MODID, "playermodel"), new PlayerModelProvider());
		//	event.addCapability(new ResourceLocation(Reference.MODID, "test"), new TestCapabilityProvider());
		}
	}
	
	@SubscribeEvent
	public void onPlayerJoin(EntityJoinWorldEvent event) {
		if (!event.getEntity().world.isRemote && event.getEntity() instanceof PlayerEntity) {
			IPlayerModelCapability props = ModCapabilities.get((PlayerEntity) event.getEntity());
			PacketHandler.sendTo(new PacketSyncCapability(props), (ServerPlayerEntity) event.getEntity());
		}
	}

}
