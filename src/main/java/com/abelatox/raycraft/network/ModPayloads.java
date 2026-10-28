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

		registrar.playToServer(SetModelPayload.TYPE, SetModelPayload.STREAM_CODEC, SetModelPayload::handle);
		registrar.playToServer(SetChargingPayload.TYPE, SetChargingPayload.STREAM_CODEC, SetChargingPayload::handle);
		registrar.playToServer(SetGlidingPayload.TYPE, SetGlidingPayload.STREAM_CODEC, SetGlidingPayload::handle);
		registrar.playToServer(SetSwingingPayload.TYPE, SetSwingingPayload.STREAM_CODEC, SetSwingingPayload::handle);
		registrar.playToServer(SetBarrelFlightPayload.TYPE, SetBarrelFlightPayload.STREAM_CODEC, SetBarrelFlightPayload::handle);
		registrar.playToClient(StartGrapplePayload.TYPE, StartGrapplePayload.STREAM_CODEC, StartGrapplePayload::handle);
		registrar.playToServer(ShootPayload.TYPE, ShootPayload.STREAM_CODEC, ShootPayload::handle);
		registrar.playToServer(PlaySoundPayload.TYPE, PlaySoundPayload.STREAM_CODEC, PlaySoundPayload::handle);
		registrar.playToServer(SetWindConfigPayload.TYPE, SetWindConfigPayload.STREAM_CODEC, SetWindConfigPayload::handle);
		registrar.playToClient(OpenWindConfigPayload.TYPE, OpenWindConfigPayload.STREAM_CODEC, OpenWindConfigPayload::handle);
		registrar.playToServer(SetShellRollPayload.TYPE, SetShellRollPayload.STREAM_CODEC, SetShellRollPayload::handle);
		registrar.playToServer(SetPortalDestinationPayload.TYPE, SetPortalDestinationPayload.STREAM_CODEC, SetPortalDestinationPayload::handle);
		registrar.playToClient(OpenPortalConfigPayload.TYPE, OpenPortalConfigPayload.STREAM_CODEC, OpenPortalConfigPayload::handle);
	}
}
