package com.abelatox.raycraft.capabilities;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class PlayerModelProvider implements ICapabilitySerializable<CompoundNBT> {
	IPlayerModelCapability instance = ModCapabilities.PLAYER_MODEL.getDefaultInstance();

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		return ModCapabilities.PLAYER_MODEL.orEmpty(cap, LazyOptional.of(() -> instance));
	}

	@Override
	public CompoundNBT serializeNBT() {
		return (CompoundNBT) ModCapabilities.PLAYER_MODEL.getStorage().writeNBT(ModCapabilities.PLAYER_MODEL, instance, null);
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		ModCapabilities.PLAYER_MODEL.getStorage().readNBT(ModCapabilities.PLAYER_MODEL, instance, null, nbt);
	}

}
