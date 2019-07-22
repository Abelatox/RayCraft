package com.abelatox.raycraft.network.packets;

import java.util.function.Supplier;

import com.abelatox.raycraft.capabilities.IPlayerModelCapability;
import com.abelatox.raycraft.capabilities.ModCapabilities;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketSetModel {

	private String model;

	public PacketSetModel() {
	}

	public PacketSetModel(String power) {
		this.model = power;
	}

	public void encode(PacketBuffer buffer) {
		buffer.writeInt(this.model.length());
		buffer.writeString(this.model);
	}

	public static PacketSetModel decode(PacketBuffer buffer) {
		PacketSetModel msg = new PacketSetModel();
		int len = buffer.readInt();
		msg.model = buffer.readString(len);
		return msg;
	}

	public static void handle(PacketSetModel message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			PlayerEntity player = ctx.get().getSender();
			IPlayerModelCapability props = ModCapabilities.get(player);
			props.setPlayerType(message.model);
			System.out.println(props.getPlayerType());
		});
		ctx.get().setPacketHandled(true);
	}

}
