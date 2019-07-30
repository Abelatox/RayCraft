package com.abelatox.raycraft.network.packets;

import java.util.List;
import java.util.function.Supplier;

import com.abelatox.raycraft.capabilities.IPlayerCapabilities;
import com.abelatox.raycraft.capabilities.ModCapabilities;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketSyncCapabilityToAll {

	private String name;
	private String model;
	private int shotLevel;
	private boolean charging;

	public PacketSyncCapabilityToAll() {
	}

	public PacketSyncCapabilityToAll(String name, IPlayerCapabilities capability) {
		this.name = name;
		this.model = capability.getPlayerType();
		this.shotLevel = capability.getShotLevel();
		this.charging = capability.getCharging();
	}

	public void encode(PacketBuffer buffer) {
		buffer.writeString(name);
		buffer.writeString(model);
		buffer.writeInt(shotLevel);
		buffer.writeBoolean(charging);
	}

	public static PacketSyncCapabilityToAll decode(PacketBuffer buffer) {
		PacketSyncCapabilityToAll msg = new PacketSyncCapabilityToAll();
		msg.name = buffer.readString(40);
		msg.model = buffer.readString(40);
		msg.shotLevel = buffer.readInt();
		msg.charging = buffer.readBoolean();
		return msg;
	}

	public static void handle(final PacketSyncCapabilityToAll message, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			List<AbstractClientPlayerEntity> list = Minecraft.getInstance().world.getPlayers();
			PlayerEntity player = null;
			for (int i = 0; i < list.size(); i++) {
				String name = list.get(i).getName().getFormattedText();
				System.out.println(name+" "+message.name);
				if (name.equals(message.name)) {
					player = list.get(i);
				}
			}
			if (player != null) {
				LazyOptional<IPlayerCapabilities> props = player.getCapability(ModCapabilities.PLAYER_CAPABILITIES);
				props.ifPresent(cap -> cap.setPlayerType(message.model));
				props.ifPresent(cap -> cap.setShotLevel(message.shotLevel));
				props.ifPresent(cap -> cap.setCharging(message.charging));
			}
		});
		ctx.get().setPacketHandled(true);
	}

}
