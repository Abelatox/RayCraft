package com.abelatox.raycraft.network.payloads;

import com.abelatox.raycraft.lib.Reference;
import com.abelatox.raycraft.sounds.ModSounds;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record PlaySoundPayload(String sound) implements CustomPacketPayload {

	public static final Type<PlaySoundPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(Reference.MODID, "play_sound"));

	public static final StreamCodec<ByteBuf, PlaySoundPayload> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.STRING_UTF8, PlaySoundPayload::sound,
			PlaySoundPayload::new
	);

	public void handle(IPayloadContext context) {
		context.enqueueWork(() -> {
			Player player = context.player();
			player.level().playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.PIRATE_SHOT_1.get(), SoundSource.PLAYERS, 1F, 1F);
		});
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
