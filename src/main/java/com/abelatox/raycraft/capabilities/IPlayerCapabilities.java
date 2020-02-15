package com.abelatox.raycraft.capabilities;

public interface IPlayerCapabilities {
	String getPlayerType();
	void setPlayerType(String modelString);
	boolean hasCustomPlayerType();
	
	int getShotLevel();
	void setShotLevel(int level);
	
	boolean getIsCharging();
	void setCharging(boolean b);
	float getArmRotation();
	void setArmRotation(float val);
	
	int getLums();
	void setLums(int lums);
	
	boolean getIsGliding();
	void setGliding(boolean b);
	float getHairRotation();
	void setHairRotation(float val);

}
