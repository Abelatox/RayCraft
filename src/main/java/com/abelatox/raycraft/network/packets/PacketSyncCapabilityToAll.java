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
	private int shotLevel;

	public PacketSyncCapabilityToAll() {
	}

	public PacketSyncCapabilityToAll(String name, IPlayerModelCapability capability) {
		this.name = name;
		this.model = capability.getPlayerType();
		this.shotLevel = capability.getShotLevel();
	}

	public void encode(PacketBuffer buffer) {
		buffer.writeString(name);
		buffer.writeString(model);
		buffer.writeInt(shotLevel);
	}

	public static PacketSyncCapabilityToAll decode(PacketBuffer buffer) {
		PacketSyncCapabilityToAll msg = new PacketSyncCapabilityToAll();
		msg.name = buffer.readString(40);
		msg.model = buffer.readString(40);
		msg.shotLevel = buffer.readInt();
		return msg;
	}

	public static void handle(final PacketSyncCapabilityToAll message, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			EntityPlayer player = Minecraft.getInstance().world.getPlayerEntityByName(message.name);
			//System.out.println(player+" "+message.model);
			LazyOptional<IPlayerModelCapability> props = player.getCapability(ModCapabilities.PLAYER_MODEL);
			
			props.ifPresent(cap -> cap.setPlayerType(message.model));
			props.ifPresent(cap -> cap.setShotLevel(message.shotLevel));
			
		});
		ctx.get().setPacketHandled(true);
	}

}
