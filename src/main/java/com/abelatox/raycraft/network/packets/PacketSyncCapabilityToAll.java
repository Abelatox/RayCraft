package com.abelatox.raycraft.network.packets;

import java.util.function.Supplier;

import com.abelatox.raycraft.capabilities.IPlayerModelCapability;
import com.abelatox.raycraft.capabilities.ModCapabilities;
import com.abelatox.raycraft.capabilities.PlayerModelCapability;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketSyncCapabilityToAll {

	private String name;
	private String model;
	private String carrying;

	public PacketSyncCapabilityToAll() {
	}

	public PacketSyncCapabilityToAll(String name, IPlayerModelCapability capability) {
		this.name = name;
		this.model = capability.getModel();
		this.carrying = capability.getCarrying();
	}

	public void encode(PacketBuffer buffer) {
		buffer.writeString(name);
		buffer.writeString(model);
		buffer.writeString(carrying);
	}

	public static PacketSyncCapabilityToAll decode(PacketBuffer buffer) {
		PacketSyncCapabilityToAll msg = new PacketSyncCapabilityToAll();
		msg.name = buffer.readString(40);
		msg.model = buffer.readString(40);
		msg.carrying = buffer.readString(40);
		return msg;
	}

	public static void handle(final PacketSyncCapabilityToAll message, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			EntityPlayer player = Minecraft.getInstance().world.getPlayerEntityByName(message.name);
			//System.out.println(player+" "+message.model);
			LazyOptional<IPlayerModelCapability> props = player.getCapability(ModCapabilities.PLAYER_MODEL);
			
			props.ifPresent(cap -> cap.setModel(message.model));
			props.ifPresent(cap -> cap.setCarrying(message.carrying));
			
		});
		ctx.get().setPacketHandled(true);
	}

}
