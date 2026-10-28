package com.abelatox.raycraft.network.payloads;

import com.abelatox.raycraft.capabilities.ModCapabilities;
import com.abelatox.raycraft.lib.Reference;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SetModelPayload(String model) implements CustomPacketPayload {

	public static final Type<SetModelPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(Reference.MODID, "set_model"));

	public static final StreamCodec<ByteBuf, SetModelPayload> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.STRING_UTF8, SetModelPayload::model,
			SetModelPayload::new
	);

	public void handle(IPayloadContext context) {
		ModCapabilities.update(context, props -> props.setPlayerType(model));
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
