package com.abelatox.raycraft.network.payloads;

import com.abelatox.raycraft.lib.Reference;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

import java.util.Optional;

public record ShootPayload(boolean charged, int targetId, Optional<BlockPos> cageTarget, int curve) implements CustomPacketPayload {

	public static final Type<ShootPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(Reference.MODID, "shoot"));

	public static final StreamCodec<ByteBuf, ShootPayload> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.BOOL, ShootPayload::charged,
			ByteBufCodecs.VAR_INT, ShootPayload::targetId,
			ByteBufCodecs.optional(BlockPos.STREAM_CODEC), ShootPayload::cageTarget,
			ByteBufCodecs.VAR_INT, ShootPayload::curve,
			ShootPayload::new
	);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
