package com.abelatox.raycraft.network;

import com.abelatox.raycraft.capabilities.IPlayerCapabilities;
import com.abelatox.raycraft.lib.Reference;
import com.abelatox.raycraft.network.packets.PacketPlaySound;
import com.abelatox.raycraft.network.packets.PacketSecondaryAction;
import com.abelatox.raycraft.network.packets.PacketSetCharging;
import com.abelatox.raycraft.network.packets.PacketSetGliding;
import com.abelatox.raycraft.network.packets.PacketSetModel;
import com.abelatox.raycraft.network.packets.PacketSetTarget;
import com.abelatox.raycraft.network.packets.PacketShoot;
import com.abelatox.raycraft.network.packets.PacketSyncCapability;
import com.abelatox.raycraft.network.packets.PacketSyncCapabilityToAll;
import com.abelatox.raycraft.network.packets.PacketSyncCapabilityToAllFromClient;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandler {
	private static final String PROTOCOL_VERSION = Integer.toString(1);

	private static final SimpleChannel HANDLER = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(Reference.MODID, "main_channel")).clientAcceptedVersions(PROTOCOL_VERSION::equals).serverAcceptedVersions(PROTOCOL_VERSION::equals).networkProtocolVersion(() -> PROTOCOL_VERSION).simpleChannel();

	public static void register() {
		int packetID = 0;
		//ServerToClient
		HANDLER.registerMessage(packetID++, PacketSyncCapability.class, PacketSyncCapability::encode, PacketSyncCapability::decode, PacketSyncCapability::handle);
		HANDLER.registerMessage(packetID++, PacketSyncCapabilityToAll.class, PacketSyncCapabilityToAll::encode, PacketSyncCapabilityToAll::decode, PacketSyncCapabilityToAll::handle);
		HANDLER.registerMessage(packetID++, PacketSetTarget.class, PacketSetTarget::encode, PacketSetTarget::decode, PacketSetTarget::handle);
		
		//ClientToServer
		HANDLER.registerMessage(packetID++, PacketSetModel.class, PacketSetModel::encode, PacketSetModel::decode, PacketSetModel::handle);
		HANDLER.registerMessage(packetID++, PacketSetCharging.class, PacketSetCharging::encode, PacketSetCharging::decode, PacketSetCharging::handle);
		HANDLER.registerMessage(packetID++, PacketSetGliding.class, PacketSetGliding::encode, PacketSetGliding::decode, PacketSetGliding::handle);
		HANDLER.registerMessage(packetID++, PacketSecondaryAction.class, PacketSecondaryAction::encode, PacketSecondaryAction::decode, PacketSecondaryAction::handle);
		HANDLER.registerMessage(packetID++, PacketShoot.class, PacketShoot::encode, PacketShoot::decode, PacketShoot::handle);
		HANDLER.registerMessage(packetID++, PacketSyncCapabilityToAllFromClient.class, PacketSyncCapabilityToAllFromClient::encode, PacketSyncCapabilityToAllFromClient::decode, PacketSyncCapabilityToAllFromClient::handle);
		HANDLER.registerMessage(packetID++, PacketPlaySound.class, PacketPlaySound::encode, PacketPlaySound::decode, PacketPlaySound::handle);
	}

	public static <MSG> void sendToServer(MSG msg) {
		HANDLER.sendToServer(msg);
	}

	public static <MSG> void sendTo(MSG msg, ServerPlayerEntity player) {
		if (!(player instanceof FakePlayer)) {
			HANDLER.sendTo(msg, player.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
		}
	}

	public static void syncToAllAround(PlayerEntity player, IPlayerCapabilities props) {
		if (!player.world.isRemote) {
			for (PlayerEntity playerFromList : player.world.getPlayers()) {
				sendTo(new PacketSyncCapabilityToAll(player.getDisplayName().getString(), props), (ServerPlayerEntity) playerFromList);
			}
		}
	}
}