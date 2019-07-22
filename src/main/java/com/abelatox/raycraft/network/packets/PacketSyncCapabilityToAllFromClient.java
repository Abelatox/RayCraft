package com.abelatox.raycraft.network.packets;

import java.util.function.Supplier;

import com.abelatox.raycraft.capabilities.ModCapabilities;
import com.abelatox.raycraft.network.PacketHandler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketSyncCapabilityToAllFromClient {

	private String name;
	private String model;
	private int shotLevel;

	public PacketSyncCapabilityToAllFromClient() {
	}

	/*public PacketSyncCapabilityToAllFromClient(String name, IPlayerModelCapability capability) {
		this.name = name;
		this.model = capability.getPlayerType();
		this.shotLevel = capability.getShotLevel();
	}*/

	public void encode(PacketBuffer buffer) {
		
	}

	public static PacketSyncCapabilityToAllFromClient decode(PacketBuffer buffer) {
		PacketSyncCapabilityToAllFromClient msg = new PacketSyncCapabilityToAllFromClient();
		
		return msg;
	}

	public static void handle(final PacketSyncCapabilityToAllFromClient message, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			PlayerEntity player = ctx.get().getSender();
			System.out.println(player+" "+ModCapabilities.get(player).getCharging());
			PacketHandler.syncToAllAround(player, ModCapabilities.get(player));
		});
		ctx.get().setPacketHandled(true);
	}

}
