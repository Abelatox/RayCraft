package com.abelatox.raycraft.capabilities;

import com.abelatox.raycraft.entities.EntityBarrel;
import com.abelatox.raycraft.lib.Strings;

import net.minecraft.entity.Entity;
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
			props.setString("PlayerModel", instance.getModel());
			props.setString("Carrying", instance.getCarrying());
			return props;
		}

		@Override
		public void readNBT(Capability<IPlayerModelCapability> capability, IPlayerModelCapability instance, EnumFacing side, INBTBase nbt) {
			NBTTagCompound properties = (NBTTagCompound) nbt;
			instance.setModel(properties.getString("PlayerModel"));
			instance.setCarrying(properties.getString("Carrying"));
		}
	}

	private String playerModel = "null";
	private String carrying = "null";
	private int shotLevel = 0;
	
	public String getModel() {
		return playerModel;
	}

	public void setModel(String modelString) {
		playerModel = modelString;
	}

	@Override
	public String getCarrying() {
		return carrying;
	}

	@Override
	public void setCarrying(String itemCarrying) {
		carrying = itemCarrying;
	}

	@Override
	public boolean hasCustomModel() {
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
