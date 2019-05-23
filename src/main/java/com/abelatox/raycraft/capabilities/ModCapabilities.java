package com.abelatox.raycraft.capabilities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.INBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.LazyOptional;

public class ModCapabilities {

	@CapabilityInject(IPlayerModelCapability.class)
	public static final Capability<IPlayerModelCapability> PLAYER_MODEL = null;

	public static IPlayerModelCapability get(EntityPlayer player) {
		LazyOptional<IPlayerModelCapability> props = player.getCapability(ModCapabilities.PLAYER_MODEL, null);
		return props.orElse(null);
	}

	public static void register() {
		CapabilityManager.INSTANCE.register(IPlayerModelCapability.class, new PlayerModelCapability.Storage(), () -> new PlayerModelCapability());

	}

}
