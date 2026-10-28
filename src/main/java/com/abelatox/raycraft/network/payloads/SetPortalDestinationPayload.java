package com.abelatox.raycraft.network.payloads;

import com.abelatox.raycraft.entities.EntitySpiralPortal;
import com.abelatox.raycraft.lib.Reference;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

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

	public void handle(IPayloadContext context) {
		context.enqueueWork(() -> {
			Player player = context.player();
			ResourceKey<Level> destinationLevel = ResourceKey.create(Registries.DIMENSION, Identifier.parse(level));
			if (player.isCreative() && player.level().getEntity(portalId) instanceof EntitySpiralPortal portal && player.distanceToSqr(portal) < 64 * 64
					&& player.level().getServer().getLevel(destinationLevel) != null) {
				portal.setDestination(new Vec3(x, y, z), destinationLevel);
				portal.setRed(red);
				portal.resizeKeepingCentre(size);
			}
		});
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
