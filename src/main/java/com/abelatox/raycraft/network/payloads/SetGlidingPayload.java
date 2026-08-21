package com.abelatox.raycraft.network.payloads;

import com.abelatox.raycraft.lib.Reference;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record SetGlidingPayload(boolean gliding) implements CustomPacketPayload {

	public static final Type<SetGlidingPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(Reference.MODID, "set_gliding"));

	public static final StreamCodec<ByteBuf, SetGlidingPayload> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.BOOL, SetGlidingPayload::gliding,
			SetGlidingPayload::new
	);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
