package com.abelatox.raycraft.network.packets;

import java.util.function.Supplier;

import com.abelatox.raycraft.capabilities.IPlayerModelCapability;
import com.abelatox.raycraft.capabilities.ModCapabilities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketRightMouse {

	//private String model;

	public PacketRightMouse() {
	}

	public PacketRightMouse(String power) {
		//this.model = power;
	}

	public void encode(PacketBuffer buffer) {
		//buffer.writeInt(this.model.length());
		//buffer.writeString(this.model);
	}

	public static PacketRightMouse decode(PacketBuffer buffer) {
		PacketRightMouse msg = new PacketRightMouse();
		//int len = buffer.readInt();
		//msg.model = buffer.readString(len);
		return msg;
	}

	public static void handle(PacketRightMouse message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			EntityPlayer player = ctx.get().getSender();
			IPlayerModelCapability props = ModCapabilities.get(player);
			if(props.getModel().startsWith("robo") && props.getCarrying().equals("null")) {
				System.out.println("shoot");
			}
		});
		ctx.get().setPacketHandled(true);
	}

}
