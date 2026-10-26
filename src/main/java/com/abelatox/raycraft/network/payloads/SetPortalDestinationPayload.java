package com.abelatox.raycraft.network.payloads;

import com.abelatox.raycraft.lib.Reference;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record SetPortalDestinationPayload(int portalId, double x, double y, double z, String level, boolean red, float size) implements CustomPacketPayload {

	public static final Type<SetPortalDestinationPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(Reference.MODID, "set_portal_destination"));

	public static final StreamCodec<ByteBuf, SetPortalDestinationPayload> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT, SetPortalDestinationPayload::portalId,
			ByteBufCodecs.DOUBLE, SetPortalDestinationPayload::x,
			ByteBufCodecs.DOUBLE, SetPortalDestinationPayload::y,
			ByteBufCodecs.DOUBLE, SetPortalDestinationPayload::z,
			ByteBufCodecs.STRING_UTF8, SetPortalDestinationPayload::level,
			ByteBufCodecs.BOOL, SetPortalDestinationPayload::red,
			ByteBufCodecs.FLOAT, SetPortalDestinationPayload::size,
			SetPortalDestinationPayload::new
	);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
