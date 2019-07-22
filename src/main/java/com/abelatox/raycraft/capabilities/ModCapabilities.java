package com.abelatox.raycraft.capabilities;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.LazyOptional;

public class ModCapabilities {

	@CapabilityInject(IPlayerModelCapability.class)
	public static final Capability<IPlayerModelCapability> PLAYER_MODEL = null;

	public static IPlayerModelCapability get(PlayerEntity player) {
		LazyOptional<IPlayerModelCapability> props = player.getCapability(ModCapabilities.PLAYER_MODEL, null);
		return props.orElse(null);
	}

	public static void register() {
		CapabilityManager.INSTANCE.register(IPlayerModelCapability.class, new PlayerModelCapability.Storage(), () -> new PlayerModelCapability());

	}

}
