package com.abelatox.raycraft.network.packets;

import java.util.function.Supplier;

import com.abelatox.raycraft.capabilities.IPlayerCapabilities;
import com.abelatox.raycraft.capabilities.ModCapabilities;
import com.abelatox.raycraft.network.PacketHandler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketSetGliding {

	private boolean gliding;

	public PacketSetGliding() {
	}

	public PacketSetGliding(boolean gliding) {
		this.gliding = gliding;
	}

	public void encode(PacketBuffer buffer) {
		buffer.writeBoolean(this.gliding);
	}

	public static PacketSetGliding decode(PacketBuffer buffer) {
		PacketSetGliding msg = new PacketSetGliding();
		msg.gliding = buffer.readBoolean();
		return msg;
	}

	public static void handle(PacketSetGliding message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			PlayerEntity player = ctx.get().getSender();
			IPlayerCapabilities props = ModCapabilities.get(player);
			props.setGliding(message.gliding);
			PacketHandler.syncToAllAround(player, props);
		});
		ctx.get().setPacketHandled(true);
	}

}
