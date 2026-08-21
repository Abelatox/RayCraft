package com.abelatox.raycraft.network;

import com.abelatox.raycraft.lib.Reference;
import com.abelatox.raycraft.network.payloads.*;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = Reference.MODID)
public class ModPayloads {

	@SubscribeEvent
	public static void register(RegisterPayloadHandlersEvent event) {
		final PayloadRegistrar registrar = event.registrar("1");

		registrar.playToServer(SetModelPayload.TYPE, SetModelPayload.STREAM_CODEC, ServerPayloadHandler::handleSetModel);
		registrar.playToServer(SetChargingPayload.TYPE, SetChargingPayload.STREAM_CODEC, ServerPayloadHandler::handleSetCharging);
		registrar.playToServer(SetGlidingPayload.TYPE, SetGlidingPayload.STREAM_CODEC, ServerPayloadHandler::handleSetGliding);
		registrar.playToServer(ShootPayload.TYPE, ShootPayload.STREAM_CODEC, ServerPayloadHandler::handleShoot);
		registrar.playToServer(PlaySoundPayload.TYPE, PlaySoundPayload.STREAM_CODEC, ServerPayloadHandler::handlePlaySound);
	}
}
