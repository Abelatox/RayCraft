package com.abelatox.raycraft.capabilities;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class PlayerCapabilities implements IPlayerCapabilities {

	public static class Storage implements IStorage<IPlayerCapabilities> {
		@Override
		public INBT writeNBT(Capability<IPlayerCapabilities> capability, IPlayerCapabilities instance, Direction side) {
			CompoundNBT props = new CompoundNBT();
			props.putString("PlayerModel", instance.getPlayerType());
			props.putInt("ShotLevel", instance.getShotLevel());
			props.putBoolean("Charging", instance.getIsCharging());
			props.putInt("Lums", instance.getLums());
			return props;
		}

		@Override
		public void readNBT(Capability<IPlayerCapabilities> capability, IPlayerCapabilities instance, Direction side, INBT nbt) {
			CompoundNBT properties = (CompoundNBT) nbt;
			instance.setPlayerType(properties.getString("PlayerModel"));
			instance.setShotLevel(properties.getInt("ShotLevel"));
			instance.setCharging(properties.getBoolean("Charging"));
			instance.setLums(properties.getInt("Lums"));
		}
	}

	private String playerModel = "";
	private int shotLevel = 0;
	private boolean charging = false;
	private int lums = 0;
	private float armRotation;
	
	public String getPlayerType() {
		return playerModel;
	}

	public void setPlayerType(String modelString) {
		playerModel = modelString;
	}

	@Override
	public boolean hasCustomPlayerType() {
		return !playerModel.equals("");
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
	public boolean getIsCharging() {
		return charging;
	}

	@Override
	public void setCharging(boolean b) {
		this.charging = b;
	}

	@Override
	public int getLums() {
		return lums;
	}

	@Override
	public void setLums(int lums) {
		this.lums = lums;
	}

	@Override
	public float getArmRotation() {
		return armRotation;
	}

	@Override
	public void setArmRotation(float val) {
		this.armRotation = val;
	}	
}
