package com.abelatox.raycraft.network.packets;

import java.util.function.Supplier;

import com.abelatox.raycraft.capabilities.IPlayerModelCapability;
import com.abelatox.raycraft.capabilities.ModCapabilities;
import com.abelatox.raycraft.lib.Utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketRightMouse {

	private boolean charged = false;

	public PacketRightMouse() {
	}

	public PacketRightMouse(boolean charged) {
		this.charged = charged;
	}

	public void encode(PacketBuffer buffer) {
		buffer.writeBoolean(this.charged);
		
	}

	public static PacketRightMouse decode(PacketBuffer buffer) {
		PacketRightMouse msg = new PacketRightMouse();
		msg.charged = buffer.readBoolean();
		return msg;
	}

	public static void handle(PacketRightMouse message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			EntityPlayer player = ctx.get().getSender();
			IPlayerModelCapability props = ModCapabilities.get(player);
			if(props.hasCustomPlayerType() && ItemStack.areItemStacksEqual(player.getHeldItemMainhand(), ItemStack.EMPTY)) {
				EntityThrowable shot = Utils.getEntityShot(player, message.charged);
				player.world.spawnEntity(shot);
				shot.shoot(player, player.rotationPitch, player.rotationYaw, 0, 1F, 0);
			}
		});
		ctx.get().setPacketHandled(true);
	}

}
