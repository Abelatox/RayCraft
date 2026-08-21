package com.abelatox.raycraft.client.render;

/**
 * Datos de la capability de RayCraft copiados al {@code AvatarRenderState} del jugador
 * durante {@code RegisterRenderStateModifiersEvent}, para poder leerlos de forma segura
 * en el hilo de render (en {@link RaycraftPlayerRenderHandler}) sin tocar la entidad.
 */
public record RaycraftAvatarRenderData(
		String playerType,
		boolean holdingBarrel,
		boolean charging,
		boolean gliding,
		float armRotation,
		float hairRotation,
		int punchLevel,
		int lightCoords
) {
	public static final RaycraftAvatarRenderData EMPTY = new RaycraftAvatarRenderData("", false, false, false, 0F, 0F, 0, 0);
}
