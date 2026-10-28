package com.abelatox.raycraft.network.payloads;

import com.abelatox.raycraft.gui.GUISpiralPortal;
import com.abelatox.raycraft.lib.Reference;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;

public record OpenPortalConfigPayload(int portalId, double x, double y, double z, String level, List<String> levels, boolean red, float size) implements CustomPacketPayload {

	public static final Type<OpenPortalConfigPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(Reference.MODID, "open_portal_config"));

	public static final StreamCodec<ByteBuf, OpenPortalConfigPayload> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT, OpenPortalConfigPayload::portalId,
			ByteBufCodecs.DOUBLE, OpenPortalConfigPayload::x,
			ByteBufCodecs.DOUBLE, OpenPortalConfigPayload::y,
			ByteBufCodecs.DOUBLE, OpenPortalConfigPayload::z,
			ByteBufCodecs.STRING_UTF8, OpenPortalConfigPayload::level,
			ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()), OpenPortalConfigPayload::levels,
			ByteBufCodecs.BOOL, OpenPortalConfigPayload::red,
			ByteBufCodecs.FLOAT, OpenPortalConfigPayload::size,
			OpenPortalConfigPayload::new
	);

	public void handle(IPayloadContext context) {
		context.enqueueWork(() -> GUISpiralPortal.open(this));
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
