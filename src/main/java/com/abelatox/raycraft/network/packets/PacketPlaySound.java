package com.abelatox.raycraft.network.packets;

import java.util.function.Supplier;

import com.abelatox.raycraft.capabilities.IPlayerCapabilities;
import com.abelatox.raycraft.capabilities.ModCapabilities;
import com.abelatox.raycraft.sounds.ModSounds;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketPlaySound {

	private String model;

	public PacketPlaySound() {
	}

	public PacketPlaySound(String power) {
		this.model = power;
	}

	public void encode(PacketBuffer buffer) {
		buffer.writeInt(this.model.length());
		buffer.writeString(this.model);
	}

	public static PacketPlaySound decode(PacketBuffer buffer) {
		PacketPlaySound msg = new PacketPlaySound();
		int len = buffer.readInt();
		msg.model = buffer.readString(len);
		return msg;
	}

	public static void handle(PacketPlaySound message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			PlayerEntity player = ctx.get().getSender();
			player.world.playSound(player.getPosX(), player.getPosY(), player.getPosZ(), ModSounds.pirateShot1, SoundCategory.PLAYERS, 1F, 1F, false);
		});
		ctx.get().setPacketHandled(true);
	}

}
