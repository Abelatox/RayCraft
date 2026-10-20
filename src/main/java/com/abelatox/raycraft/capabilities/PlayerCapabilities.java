package com.abelatox.raycraft.capabilities;

import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;

public class PlayerCapabilities implements IPlayerCapabilities, ValueIOSerializable {

	private String playerModel = "";
	private int shotLevel = 0;
	private boolean charging = false;
	private int lums = 0;
	private boolean gliding;

	@Override
	public String getPlayerType() {
		return playerModel;
	}

	@Override
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
	public boolean getIsGliding() {
		return gliding;
	}

	@Override
	public void setGliding(boolean b) {
		this.gliding = b;
	}

	@Override
	public void serialize(ValueOutput output) {
		output.putString("PlayerModel", getPlayerType());
		output.putInt("ShotLevel", getShotLevel());
		output.putBoolean("Charging", getIsCharging());
		output.putInt("Lums", getLums());
		output.putBoolean("Gliding", getIsGliding());
	}

	@Override
	public void deserialize(ValueInput input) {
		setPlayerType(input.getStringOr("PlayerModel", ""));
		setShotLevel(input.getIntOr("ShotLevel", 0));
		setCharging(input.getBooleanOr("Charging", false));
		setLums(input.getIntOr("Lums", 0));
		setGliding(input.getBooleanOr("Gliding", false));
	}
}
