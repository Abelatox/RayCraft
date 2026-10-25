package com.abelatox.raycraft.network;

import com.abelatox.raycraft.client.GrappleHandler;
import com.abelatox.raycraft.network.payloads.StartGrapplePayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ClientboundPayloadHandler {

	public static void handleStartGrapple(final StartGrapplePayload payload, final IPayloadContext context) {
		context.enqueueWork(() -> GrappleHandler.startFromServer(payload.lumId()));
	}
}
