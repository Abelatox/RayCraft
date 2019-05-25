package com.abelatox.raycraft.capabilities;

public interface IPlayerModelCapability {
	String getModel();
	void setModel(String modelString);
	String getCarrying();
	void setCarrying(String itemCarrying);
	boolean hasCustomModel();
	
	int getShotLevel();
	void setShotLevel(int level);
}
