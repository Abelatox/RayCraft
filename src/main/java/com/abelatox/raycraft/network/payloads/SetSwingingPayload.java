package com.abelatox.raycraft.network.payloads;

import com.abelatox.raycraft.lib.Reference;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record SetSwingingPayload(boolean swinging) implements CustomPacketPayload {

	public static final Type<SetSwingingPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(Reference.MODID, "set_swinging"));

	public static final StreamCodec<ByteBuf, SetSwingingPayload> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.BOOL, SetSwingingPayload::swinging,
			SetSwingingPayload::new
	);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
