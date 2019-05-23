package com.abelatox.raycraft.capabilities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class PlayerModelProvider implements ICapabilitySerializable<NBTTagCompound> {
	IPlayerModelCapability instance = ModCapabilities.PLAYER_MODEL.getDefaultInstance();

	@SuppressWarnings("unchecked")
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, EnumFacing side) {
		return ModCapabilities.PLAYER_MODEL.orEmpty(cap, LazyOptional.of(() -> instance));
	}

	@Override
	public NBTTagCompound serializeNBT() {
		return (NBTTagCompound) ModCapabilities.PLAYER_MODEL.getStorage().writeNBT(ModCapabilities.PLAYER_MODEL, instance, null);
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		ModCapabilities.PLAYER_MODEL.getStorage().readNBT(ModCapabilities.PLAYER_MODEL, instance, null, nbt);
	}
}
