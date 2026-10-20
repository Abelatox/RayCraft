package com.abelatox.raycraft.models;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.HumanoidArm;

public interface RaycraftCharacterModel {
	ModelPart getArm(HumanoidArm arm);
}
