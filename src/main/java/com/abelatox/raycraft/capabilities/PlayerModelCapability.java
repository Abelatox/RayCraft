package com.abelatox.raycraft.capabilities;

import net.minecraft.nbt.INBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class PlayerModelCapability implements IPlayerModelCapability {

	public static class Storage implements IStorage<IPlayerModelCapability> {
		@Override
		public INBTBase writeNBT(Capability<IPlayerModelCapability> capability, IPlayerModelCapability instance, EnumFacing side) {
			NBTTagCompound props = new NBTTagCompound();
			props.setString("PlayerModel", instance.getPlayerType());
			props.setInt("ShotLevel", instance.getShotLevel());
			return props;
		}

		@Override
		public void readNBT(Capability<IPlayerModelCapability> capability, IPlayerModelCapability instance, EnumFacing side, INBTBase nbt) {
			NBTTagCompound properties = (NBTTagCompound) nbt;
			instance.setPlayerType(properties.getString("PlayerModel"));
			instance.setShotLevel(properties.getInt("ShotLevel"));
		}
	}

	private String playerModel = "null";
	private String carrying = "null";
	private int shotLevel = 0;
	
	public String getPlayerType() {
		return playerModel;
	}

	public void setPlayerType(String modelString) {
		playerModel = modelString;
	}

	@Override
	public boolean hasCustomPlayerType() {
		return !playerModel.equals("null");
	}

	@Override
	public int getShotLevel() {
		return shotLevel;
	}

	@Override
	public void setShotLevel(int level) {
		this.shotLevel = level;
	}	
}
