package com.abelatox.raycraft.network.packets;

import java.util.function.Supplier;

import com.abelatox.raycraft.capabilities.IPlayerCapabilities;
import com.abelatox.raycraft.capabilities.ModCapabilities;
import com.abelatox.raycraft.network.PacketHandler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketSetCharging {

	private boolean charging;

	public PacketSetCharging() {
	}

	public PacketSetCharging(boolean charging) {
		this.charging = charging;
	}

	public void encode(PacketBuffer buffer) {
		buffer.writeBoolean(this.charging);
	}

	public static PacketSetCharging decode(PacketBuffer buffer) {
		PacketSetCharging msg = new PacketSetCharging();
		msg.charging = buffer.readBoolean();
		return msg;
	}

	public static void handle(PacketSetCharging message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			PlayerEntity player = ctx.get().getSender();
			IPlayerCapabilities props = ModCapabilities.get(player);
			
				props.setCharging(message.charging);
			
			System.out.println(props.getPlayerType());
			PacketHandler.syncToAllAround(player, props);
		});
		ctx.get().setPacketHandled(true);
	}

}
