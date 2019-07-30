package com.abelatox.raycraft.network.packets;

import java.util.function.Supplier;

import com.abelatox.raycraft.capabilities.IPlayerCapabilities;
import com.abelatox.raycraft.capabilities.ModCapabilities;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketSecondaryAction {

	//private String model;

	public PacketSecondaryAction() {
	}

	public PacketSecondaryAction(String power) {
		//this.model = power;
	}

	public void encode(PacketBuffer buffer) {
		//buffer.writeInt(this.model.length());
		//buffer.writeString(this.model);
	}

	public static PacketSecondaryAction decode(PacketBuffer buffer) {
		PacketSecondaryAction msg = new PacketSecondaryAction();
		//int len = buffer.readInt();
		//msg.model = buffer.readString(len);
		return msg;
	}

	public static void handle(PacketSecondaryAction message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			PlayerEntity player = ctx.get().getSender();
			IPlayerCapabilities props = ModCapabilities.get(player);
			
		});
		ctx.get().setPacketHandled(true);
	}

}
