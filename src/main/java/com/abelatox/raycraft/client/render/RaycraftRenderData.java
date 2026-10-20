package com.abelatox.raycraft.client.render;

import com.abelatox.raycraft.lib.Reference;

import net.minecraft.resources.Identifier;
import net.minecraft.util.context.ContextKey;

public class RaycraftRenderData {
	public static final ContextKey<RaycraftAvatarRenderData> AVATAR_DATA = new ContextKey<>(Identifier.fromNamespaceAndPath(Reference.MODID, "avatar_data"));
}
