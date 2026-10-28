package com.abelatox.raycraft.network.payloads;

import com.abelatox.raycraft.events.BarrelFlight;
import com.abelatox.raycraft.lib.Reference;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SetBarrelFlightPayload(boolean flying, boolean letGo) implements CustomPacketPayload {

	public static final Type<SetBarrelFlightPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(Reference.MODID, "set_barrel_flight"));

	public static final StreamCodec<ByteBuf, SetBarrelFlightPayload> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.BOOL, SetBarrelFlightPayload::flying,
			ByteBufCodecs.BOOL, SetBarrelFlightPayload::letGo,
			SetBarrelFlightPayload::new
	);

	public void handle(IPayloadContext context) {
		context.enqueueWork(() -> BarrelFlight.onFlightPayload(context.player(), flying, letGo));
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
