package com.abelatox.raycraft.network.payloads;

import com.abelatox.raycraft.lib.Reference;

import com.abelatox.raycraft.gui.GUIWindCurrent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record OpenWindConfigPayload(int windId, float height, float radius) implements CustomPacketPayload {

	public static final Type<OpenWindConfigPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(Reference.MODID, "open_wind_config"));

	public static final StreamCodec<ByteBuf, OpenWindConfigPayload> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT, OpenWindConfigPayload::windId,
			ByteBufCodecs.FLOAT, OpenWindConfigPayload::height,
			ByteBufCodecs.FLOAT, OpenWindConfigPayload::radius,
			OpenWindConfigPayload::new
	);

	public void handle(IPayloadContext context) {
		context.enqueueWork(() -> GUIWindCurrent.open(this));
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
