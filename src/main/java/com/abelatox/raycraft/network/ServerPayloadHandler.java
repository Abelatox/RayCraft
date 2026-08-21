package com.abelatox.raycraft.network;

import com.abelatox.raycraft.capabilities.IPlayerCapabilities;
import com.abelatox.raycraft.capabilities.ModCapabilities;
import com.abelatox.raycraft.lib.Utils;
import com.abelatox.raycraft.network.payloads.*;
import com.abelatox.raycraft.sounds.ModSounds;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ServerPayloadHandler {

	public static void handleSetModel(final SetModelPayload payload, final IPayloadContext context) {
		context.enqueueWork(() -> {
			ServerPlayer player = (ServerPlayer) context.player();
			IPlayerCapabilities props = ModCapabilities.get(player);
			props.setPlayerType(payload.model());
			ModCapabilities.sync(player);
		});
	}

	public static void handleSetCharging(final SetChargingPayload payload, final IPayloadContext context) {
		context.enqueueWork(() -> {
			ServerPlayer player = (ServerPlayer) context.player();
			IPlayerCapabilities props = ModCapabilities.get(player);
			props.setCharging(payload.charging());
			ModCapabilities.sync(player);
		});
	}

	public static void handleSetGliding(final SetGlidingPayload payload, final IPayloadContext context) {
		context.enqueueWork(() -> {
			ServerPlayer player = (ServerPlayer) context.player();
			IPlayerCapabilities props = ModCapabilities.get(player);
			props.setGliding(payload.gliding());
			ModCapabilities.sync(player);
		});
	}

	public static void handleShoot(final ShootPayload payload, final IPayloadContext context) {
		context.enqueueWork(() -> {
			ServerPlayer player = (ServerPlayer) context.player();
			IPlayerCapabilities props = ModCapabilities.get(player);
			if (props.hasCustomPlayerType() && player.getMainHandItem().isEmpty()) {
				ThrowableProjectile shot = Utils.getEntityShot(player, payload.charged());
				if (shot != null) {
					player.level().addFreshEntity(shot);
					shot.setOwner(player);
					shot.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 1F, 0);
					player.level().playSound(null, player.blockPosition(), ModSounds.FIST_SHOT_0.get(), SoundSource.MASTER, 1F, 1F);
					player.level().playSound(null, player.blockPosition(), Utils.getShootSound(player, payload.charged()), SoundSource.MASTER, 1F, 1F);
					player.swing(InteractionHand.MAIN_HAND);
				}
			}
		});
	}

	public static void handlePlaySound(final PlaySoundPayload payload, final IPayloadContext context) {
		context.enqueueWork(() -> {
			ServerPlayer player = (ServerPlayer) context.player();
			player.level().playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.PIRATE_SHOT_1.get(), SoundSource.PLAYERS, 1F, 1F);
		});
	}
}
