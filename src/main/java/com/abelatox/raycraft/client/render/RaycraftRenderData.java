package com.abelatox.raycraft.client.render;

import com.abelatox.raycraft.lib.Reference;

import net.minecraft.resources.Identifier;
import net.minecraft.util.context.ContextKey;

/**
 * {@link ContextKey} usada para adjuntar {@link RaycraftAvatarRenderData} al
 * {@code AvatarRenderState} de cada jugador renderizado.
 */
public class RaycraftRenderData {
	public static final ContextKey<RaycraftAvatarRenderData> AVATAR_DATA =
			new ContextKey<>(Identifier.fromNamespaceAndPath(Reference.MODID, "avatar_data"));
}
