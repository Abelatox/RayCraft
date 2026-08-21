package com.abelatox.raycraft.network.packets;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSetTarget {

	private int id;

	public PacketSetTarget() {
	}

	public PacketSetTarget(int targetID) {
		this.id = targetID;
	}

	public void encode(PacketBuffer buffer) {
		buffer.writeInt(this.id);
	}

	public static PacketSetTarget decode(PacketBuffer buffer) {
		PacketSetTarget msg = new PacketSetTarget();
		msg.id = buffer.readInt();
		return msg;
	}

	public static void handle(PacketSetTarget message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			PlayerEntity player = ctx.get().getSender();

		});
		ctx.get().setPacketHandled(true);
	}

}
