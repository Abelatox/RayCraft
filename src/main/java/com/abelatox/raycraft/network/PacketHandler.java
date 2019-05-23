package com.abelatox.raycraft.network;

import com.abelatox.raycraft.capabilities.IPlayerModelCapability;
import com.abelatox.raycraft.lib.Reference;
import com.abelatox.raycraft.network.packets.PacketLeftMouse;
import com.abelatox.raycraft.network.packets.PacketRightMouse;
import com.abelatox.raycraft.network.packets.PacketSetModel;
import com.abelatox.raycraft.network.packets.PacketSyncCapability;
import com.abelatox.raycraft.network.packets.PacketSyncCapabilityToAll;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
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
		HANDLER.registerMessage(packetID++, PacketSetModel.class, PacketSetModel::encode, PacketSetModel::decode, PacketSetModel::handle);
		HANDLER.registerMessage(packetID++, PacketSyncCapability.class, PacketSyncCapability::encode, PacketSyncCapability::decode, PacketSyncCapability::handle);
		HANDLER.registerMessage(packetID++, PacketSyncCapabilityToAll.class, PacketSyncCapabilityToAll::encode, PacketSyncCapabilityToAll::decode, PacketSyncCapabilityToAll::handle);
		HANDLER.registerMessage(packetID++, PacketLeftMouse.class, PacketLeftMouse::encode, PacketLeftMouse::decode, PacketLeftMouse::handle);
		HANDLER.registerMessage(packetID++, PacketRightMouse.class, PacketRightMouse::encode, PacketRightMouse::decode, PacketRightMouse::handle);
	}

	public static <MSG> void sendToServer(MSG msg) {
		HANDLER.sendToServer(msg);
	}

	public static <MSG> void sendTo(MSG msg, EntityPlayerMP player) {
		if (!(player instanceof FakePlayer)) {
			HANDLER.sendTo(msg, player.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
		}
	}

	public static void sendToAllAround(EntityPlayer player, IPlayerModelCapability props) {
		if (!player.world.isRemote) {
			for (EntityPlayer playerFromList : player.world.playerEntities) {
				sendTo(new PacketSyncCapabilityToAll(player.getDisplayName().getString(), props), (EntityPlayerMP) playerFromList);
			}
		}
	}
}