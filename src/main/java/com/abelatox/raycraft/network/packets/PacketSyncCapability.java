package com.abelatox.raycraft.network.packets;

import java.util.function.Supplier;

import com.abelatox.raycraft.capabilities.IPlayerModelCapability;
import com.abelatox.raycraft.capabilities.ModCapabilities;
import com.abelatox.raycraft.capabilities.PlayerModelCapability;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketSyncCapability {

	private String model;

	public PacketSyncCapability() {
	}

	public PacketSyncCapability(IPlayerModelCapability capability) {
		this.model = capability.getModel();
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
			LazyOptional<IPlayerModelCapability> props = Minecraft.getInstance().player.getCapability(ModCapabilities.PLAYER_MODEL);
			props.ifPresent(cap -> cap.setModel(message.model));
		});
		ctx.get().setPacketHandled(true);
	}

}
