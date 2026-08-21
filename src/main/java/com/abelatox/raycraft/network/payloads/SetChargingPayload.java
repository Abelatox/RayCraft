package com.abelatox.raycraft.network.payloads;

import com.abelatox.raycraft.lib.Reference;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record SetChargingPayload(boolean charging) implements CustomPacketPayload {

	public static final Type<SetChargingPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(Reference.MODID, "set_charging"));

	public static final StreamCodec<ByteBuf, SetChargingPayload> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.BOOL, SetChargingPayload::charging,
			SetChargingPayload::new
	);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
