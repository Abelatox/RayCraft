package com.abelatox.raycraft.network.payloads;

import com.abelatox.raycraft.entities.EntityFlyingShell;
import com.abelatox.raycraft.lib.Reference;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SetShellRollPayload(int shellId, float roll) implements CustomPacketPayload {

	public static final Type<SetShellRollPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(Reference.MODID, "set_shell_roll"));

	public static final StreamCodec<ByteBuf, SetShellRollPayload> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT, SetShellRollPayload::shellId,
			ByteBufCodecs.FLOAT, SetShellRollPayload::roll,
			SetShellRollPayload::new
	);

	public void handle(IPayloadContext context) {
		context.enqueueWork(() -> {
			if (context.player().level().getEntity(shellId) instanceof EntityFlyingShell shell && shell.getControllingPassenger() == context.player() && Float.isFinite(roll)) {
				shell.setRollFromRider(roll);
			}
		});
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
