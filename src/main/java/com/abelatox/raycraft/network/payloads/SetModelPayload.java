package com.abelatox.raycraft.network.payloads;

import com.abelatox.raycraft.lib.Reference;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record SetModelPayload(String model) implements CustomPacketPayload {

	public static final Type<SetModelPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(Reference.MODID, "set_model"));

	public static final StreamCodec<ByteBuf, SetModelPayload> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.STRING_UTF8, SetModelPayload::model,
			SetModelPayload::new
	);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
