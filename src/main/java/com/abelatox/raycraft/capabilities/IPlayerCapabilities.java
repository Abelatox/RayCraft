package com.abelatox.raycraft.capabilities;

public interface IPlayerCapabilities {
	String getPlayerType();
	void setPlayerType(String modelString);
	boolean hasCustomPlayerType();
	
	int getShotLevel();
	void setShotLevel(int level);
	
	boolean getIsCharging();
	void setCharging(boolean b);
	
	int getLums();
	void setLums(int lums);
}
