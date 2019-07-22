package com.abelatox.raycraft.network.packets;

import java.util.function.Supplier;

import com.abelatox.raycraft.capabilities.IPlayerModelCapability;
import com.abelatox.raycraft.capabilities.ModCapabilities;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketLeftMouse {

	//private String model;

	public PacketLeftMouse() {
	}

	public PacketLeftMouse(String power) {
		//this.model = power;
	}

	public void encode(PacketBuffer buffer) {
		//buffer.writeInt(this.model.length());
		//buffer.writeString(this.model);
	}

	public static PacketLeftMouse decode(PacketBuffer buffer) {
		PacketLeftMouse msg = new PacketLeftMouse();
		//int len = buffer.readInt();
		//msg.model = buffer.readString(len);
		return msg;
	}

	public static void handle(PacketLeftMouse message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			PlayerEntity player = ctx.get().getSender();
			IPlayerModelCapability props = ModCapabilities.get(player);
			
		});
		ctx.get().setPacketHandled(true);
	}

}
