package com.abelatox.raycraft.network.payloads;

import com.abelatox.raycraft.capabilities.ModCapabilities;
import com.abelatox.raycraft.lib.Reference;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SetChargingPayload(boolean charging) implements CustomPacketPayload {

	public static final Type<SetChargingPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(Reference.MODID, "set_charging"));

	public static final StreamCodec<ByteBuf, SetChargingPayload> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.BOOL, SetChargingPayload::charging,
			SetChargingPayload::new
	);

	public void handle(IPayloadContext context) {
		ModCapabilities.update(context, props -> props.setCharging(charging));
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
