package com.abelatox.raycraft.network.packets;

import java.util.function.Supplier;

import com.abelatox.raycraft.capabilities.IPlayerCapabilities;
import com.abelatox.raycraft.capabilities.ModCapabilities;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketSyncCapability {

	private String model;

	public PacketSyncCapability() {
	}

	public PacketSyncCapability(IPlayerCapabilities capability) {
		this.model = capability.getPlayerType();
	}

	public void encode(PacketBuffer buffer) {
		buffer.writeInt(this.model.length());
		buffer.writeString(this.model);
	}

	public static PacketSyncCapability decode(PacketBuffer buffer) {
		PacketSyncCapability msg = new PacketSyncCapability();
		int len = buffer.readInt();
		msg.model = buffer.readString(len);
		return msg;
	}

	public static void handle(final PacketSyncCapability message, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			LazyOptional<IPlayerCapabilities> props = Minecraft.getInstance().player.getCapability(ModCapabilities.PLAYER_CAPABILITIES);
			props.ifPresent(cap -> cap.setPlayerType(message.model));
		});
		ctx.get().setPacketHandled(true);
	}

}
