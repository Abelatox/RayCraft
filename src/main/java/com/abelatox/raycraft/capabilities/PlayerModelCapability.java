package com.abelatox.raycraft.capabilities;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class PlayerModelCapability implements IPlayerModelCapability {

	public static class Storage implements IStorage<IPlayerModelCapability> {
		@Override
		public INBT writeNBT(Capability<IPlayerModelCapability> capability, IPlayerModelCapability instance, Direction side) {
			CompoundNBT props = new CompoundNBT();
			props.putString("PlayerModel", instance.getPlayerType());
			props.putInt("ShotLevel", instance.getShotLevel());
			props.putBoolean("Charging", instance.getCharging());
			return props;
		}

		@Override
		public void readNBT(Capability<IPlayerModelCapability> capability, IPlayerModelCapability instance, Direction side, INBT nbt) {
			CompoundNBT properties = (CompoundNBT) nbt;
			instance.setPlayerType(properties.getString("PlayerModel"));
			instance.setShotLevel(properties.getInt("ShotLevel"));
			instance.setCharging(properties.getBoolean("Charging"));
		}
	}

	private String playerModel = "";
	private int shotLevel = 0;
	private boolean charging = false;
	
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

	@Override
	public boolean getCharging() {
		return charging;
	}

	@Override
	public void setCharging(boolean b) {
		this.charging = b;
	}	
}
