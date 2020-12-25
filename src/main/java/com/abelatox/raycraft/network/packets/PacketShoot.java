package com.abelatox.raycraft.network.packets;

import java.util.function.Supplier;

import com.abelatox.raycraft.capabilities.IPlayerCapabilities;
import com.abelatox.raycraft.capabilities.ModCapabilities;
import com.abelatox.raycraft.lib.Utils;
import com.abelatox.raycraft.sounds.ModSounds;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketShoot {

	private boolean charged = false;

	public PacketShoot() {
	}

	public PacketShoot(boolean charged) {
		this.charged = charged;
	}

	public void encode(PacketBuffer buffer) {
		buffer.writeBoolean(this.charged);

	}

	public static PacketShoot decode(PacketBuffer buffer) {
		PacketShoot msg = new PacketShoot();
		msg.charged = buffer.readBoolean();
		return msg;
	}

	public static void handle(PacketShoot message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			PlayerEntity player = ctx.get().getSender();
			IPlayerCapabilities props = ModCapabilities.get(player);
			if (props.hasCustomPlayerType() && ItemStack.areItemStacksEqual(player.getHeldItemMainhand(), ItemStack.EMPTY)) {
				ThrowableEntity shot = Utils.getEntityShot(player, message.charged);
				if (shot != null) {
					player.world.addEntity(shot);
					shot.setShooter(player);
					shot.func_234612_a_(player, player.rotationPitch, player.rotationYaw, 0, 1F, 0);
					player.world.playSound(null, player.getPosition(), ModSounds.fistShot0, SoundCategory.MASTER, 1F, 1F);
					player.world.playSound(null, player.getPosition(), Utils.getShootSound(player, message.charged), SoundCategory.MASTER, 1F, 1F);
					player.swingArm(Hand.MAIN_HAND);
				}
			}
		});
		ctx.get().setPacketHandled(true);
	}

}
