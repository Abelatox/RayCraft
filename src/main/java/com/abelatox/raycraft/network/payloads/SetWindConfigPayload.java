package com.abelatox.raycraft.network.payloads;

import com.abelatox.raycraft.lib.Reference;

import com.abelatox.raycraft.entities.EntityWindCurrent;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record SetWindConfigPayload(int windId, float height, float radius) implements CustomPacketPayload {

	public static final Type<SetWindConfigPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(Reference.MODID, "set_wind_config"));

	public static final StreamCodec<ByteBuf, SetWindConfigPayload> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT, SetWindConfigPayload::windId,
			ByteBufCodecs.FLOAT, SetWindConfigPayload::height,
			ByteBufCodecs.FLOAT, SetWindConfigPayload::radius,
			SetWindConfigPayload::new
	);

	public void handle(IPayloadContext context) {
		context.enqueueWork(() -> {
			Player player = context.player();
			if (player.isCreative() && player.level().getEntity(windId) instanceof EntityWindCurrent wind && player.distanceToSqr(wind) < 64 * 64) {
				wind.setShape(height, radius);
			}
		});
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
