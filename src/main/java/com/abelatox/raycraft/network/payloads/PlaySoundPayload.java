package com.abelatox.raycraft.network.payloads;

import com.abelatox.raycraft.lib.Reference;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record PlaySoundPayload(String sound) implements CustomPacketPayload {

	public static final Type<PlaySoundPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(Reference.MODID, "play_sound"));

	public static final StreamCodec<ByteBuf, PlaySoundPayload> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.STRING_UTF8, PlaySoundPayload::sound,
			PlaySoundPayload::new
	);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
