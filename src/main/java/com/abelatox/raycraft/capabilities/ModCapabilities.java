package com.abelatox.raycraft.capabilities;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.LazyOptional;

public class ModCapabilities {

	@CapabilityInject(IPlayerCapabilities.class)
	public static final Capability<IPlayerCapabilities> PLAYER_CAPABILITIES = null;

	public static IPlayerCapabilities get(PlayerEntity player) {
		LazyOptional<IPlayerCapabilities> props = player.getCapability(ModCapabilities.PLAYER_CAPABILITIES, null);
		return props.orElse(null);
	}

	public static void register() {
		CapabilityManager.INSTANCE.register(IPlayerCapabilities.class, new PlayerCapabilities.Storage(), () -> new PlayerCapabilities());

	}

}
