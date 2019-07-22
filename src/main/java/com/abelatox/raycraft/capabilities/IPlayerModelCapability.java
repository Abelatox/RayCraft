package com.abelatox.raycraft.capabilities;

public interface IPlayerModelCapability {
	String getPlayerType();
	void setPlayerType(String modelString);
	boolean hasCustomPlayerType();
	
	int getShotLevel();
	void setShotLevel(int level);
	
	boolean getCharging();
	void setCharging(boolean b);
}
