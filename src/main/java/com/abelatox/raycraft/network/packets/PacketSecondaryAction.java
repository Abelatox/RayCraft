package com.abelatox.raycraft.network.packets;

import com.abelatox.raycraft.capabilities.IPlayerCapabilities;
import com.abelatox.raycraft.capabilities.ModCapabilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSecondaryAction {

	public PacketSecondaryAction() {
	}

	public PacketSecondaryAction(String power) {

	}

	public void encode(PacketBuffer buffer) {

	}

	public static PacketSecondaryAction decode(PacketBuffer buffer) {
		PacketSecondaryAction msg = new PacketSecondaryAction();

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
