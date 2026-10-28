package com.abelatox.raycraft.network.payloads;

import com.abelatox.raycraft.client.GrappleHandler;
import com.abelatox.raycraft.lib.Reference;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record StartGrapplePayload(int lumId) implements CustomPacketPayload {

	public static final Type<StartGrapplePayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(Reference.MODID, "start_grapple"));

	public static final StreamCodec<ByteBuf, StartGrapplePayload> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT, StartGrapplePayload::lumId,
			StartGrapplePayload::new
	);

	public void handle(IPayloadContext context) {
		context.enqueueWork(() -> GrappleHandler.startFromServer(lumId));
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
