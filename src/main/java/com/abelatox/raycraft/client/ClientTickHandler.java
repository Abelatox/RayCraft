package com.abelatox.raycraft.client;

import com.abelatox.raycraft.capabilities.IPlayerCapabilities;
import com.abelatox.raycraft.capabilities.ModCapabilities;
import com.abelatox.raycraft.events.BarrelFlight;
import com.abelatox.raycraft.lib.Reference;
import com.abelatox.raycraft.lib.Strings;
import com.abelatox.raycraft.network.payloads.SetGlidingPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

@EventBusSubscriber(modid = Reference.MODID, value = Dist.CLIENT)
public class ClientTickHandler {
	private static boolean gliding;

	@SubscribeEvent
	public static void onClientTick(ClientTickEvent.Post event) {
		Minecraft mc = Minecraft.getInstance();
		LocalPlayer player = mc.player;
		if (player == null) {
			gliding = false;
			return;
		}

		IPlayerCapabilities props = ModCapabilities.get(player);

		boolean falling = gliding || player.getDeltaMovement().y < -0.1;
		boolean shouldGlide = Strings.RAYMAN.equals(props.getPlayerType())
				&& !player.onGround()
				&& falling
				&& !player.isInWater()
				&& !player.getAbilities().flying
				&& !player.isPassenger()
				&& !GrappleHandler.isSwinging()
				&& !BarrelFlight.isFlying(player)
				&& mc.options.keyJump.isDown();

		if (shouldGlide != gliding) {
			gliding = shouldGlide;
			ClientPacketDistributor.sendToServer(new SetGlidingPayload(shouldGlide));
		}
		props.setGliding(gliding);
	}
}
