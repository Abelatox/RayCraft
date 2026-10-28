package com.abelatox.raycraft.network.payloads;

import com.abelatox.raycraft.capabilities.ModCapabilities;
import com.abelatox.raycraft.lib.Reference;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SetGlidingPayload(boolean gliding) implements CustomPacketPayload {

	public static final Type<SetGlidingPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(Reference.MODID, "set_gliding"));

	public static final StreamCodec<ByteBuf, SetGlidingPayload> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.BOOL, SetGlidingPayload::gliding,
			SetGlidingPayload::new
	);

	public void handle(IPayloadContext context) {
		ModCapabilities.update(context, props -> props.setGliding(gliding));
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
