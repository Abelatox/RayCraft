package com.abelatox.raycraft.client.render;

/**
 * RayCraft capability data copied into the player's {@code AvatarRenderState} during
 * {@code RegisterRenderStateModifiersEvent}, so it can be read safely on the render thread
 * (in {@link RaycraftPlayerRenderHandler}) without touching the entity.
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
