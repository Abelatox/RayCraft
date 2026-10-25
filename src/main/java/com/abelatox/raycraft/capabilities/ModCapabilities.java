package com.abelatox.raycraft.capabilities;

import com.abelatox.raycraft.lib.Reference;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class ModCapabilities {

	public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Reference.MODID);

	private static final StreamCodec<ByteBuf, PlayerCapabilities> SYNC_STREAM_CODEC = StreamCodec.of(
			(buf, value) -> {
				ByteBufCodecs.STRING_UTF8.encode(buf, value.getPlayerType());
				ByteBufCodecs.VAR_INT.encode(buf, value.getShotLevel());
				ByteBufCodecs.BOOL.encode(buf, value.getIsCharging());
				ByteBufCodecs.BOOL.encode(buf, value.getIsGliding());
				ByteBufCodecs.VAR_INT.encode(buf, value.getLums());
			},
			(buf) -> {
				PlayerCapabilities data = new PlayerCapabilities();
				data.setPlayerType(ByteBufCodecs.STRING_UTF8.decode(buf));
				data.setShotLevel(ByteBufCodecs.VAR_INT.decode(buf));
				data.setCharging(ByteBufCodecs.BOOL.decode(buf));
				data.setGliding(ByteBufCodecs.BOOL.decode(buf));
				data.setLums(ByteBufCodecs.VAR_INT.decode(buf));
				return data;
			}
	);

	public static final Supplier<AttachmentType<PlayerCapabilities>> PLAYER_DATA = ATTACHMENT_TYPES.register(
			"player_data",
			() -> AttachmentType.serializable(PlayerCapabilities::new)
					.copyOnDeath()
					.sync(SYNC_STREAM_CODEC)
					.build()
	);

	public static IPlayerCapabilities get(Player player) {
		return player.getData(PLAYER_DATA);
	}

	public static void sync(Player player) {
		player.setData(PLAYER_DATA, (PlayerCapabilities) get(player));
	}
}
