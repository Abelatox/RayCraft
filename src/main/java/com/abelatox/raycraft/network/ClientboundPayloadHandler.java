package com.abelatox.raycraft.network;

import com.abelatox.raycraft.client.GrappleHandler;
import com.abelatox.raycraft.gui.GUISpiralPortal;
import com.abelatox.raycraft.network.payloads.OpenPortalConfigPayload;
import com.abelatox.raycraft.network.payloads.StartGrapplePayload;
import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ClientboundPayloadHandler {

	public static void handleStartGrapple(final StartGrapplePayload payload, final IPayloadContext context) {
		context.enqueueWork(() -> GrappleHandler.startFromServer(payload.lumId()));
	}

	public static void handleOpenPortalConfig(final OpenPortalConfigPayload payload, final IPayloadContext context) {
		context.enqueueWork(() -> Minecraft.getInstance().gui.setScreen(new GUISpiralPortal(payload)));
	}
}
