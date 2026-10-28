package com.abelatox.raycraft.network.payloads;

import com.abelatox.raycraft.events.GrappleServer;
import com.abelatox.raycraft.lib.Reference;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SetSwingingPayload(boolean swinging) implements CustomPacketPayload {

	public static final Type<SetSwingingPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(Reference.MODID, "set_swinging"));

	public static final StreamCodec<ByteBuf, SetSwingingPayload> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.BOOL, SetSwingingPayload::swinging,
			SetSwingingPayload::new
	);

	public void handle(IPayloadContext context) {
		context.enqueueWork(() -> GrappleServer.setSwinging(context.player(), swinging));
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
