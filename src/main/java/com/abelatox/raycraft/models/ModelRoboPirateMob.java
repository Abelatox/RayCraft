package com.abelatox.raycraft.models;

import com.abelatox.raycraft.entities.render.RenderRoboPirate;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;

public class ModelRoboPirateMob extends EntityModel<RenderRoboPirate.RoboPirateRenderState> {

	private final ModelPart head;
	private final ModelPart rightArm;
	private final ModelPart leftArm;
	private final ModelPart rightLeg;
	private final ModelPart leftLeg;

	public ModelRoboPirateMob(ModelPart root) {
		super(root);
		this.head = root.getChild("head");
		this.rightArm = root.getChild("right_arm");
		this.leftArm = root.getChild("left_arm");
		this.rightLeg = root.getChild("right_leg");
		this.leftLeg = root.getChild("left_leg");
	}

	@Override
	public void setupAnim(RenderRoboPirate.RoboPirateRenderState state) {
		super.setupAnim(state);
		head.yRot = state.yRot * Mth.DEG_TO_RAD;
		head.xRot = state.xRot * Mth.DEG_TO_RAD;

		float walk = state.walkAnimationPos * 0.6662F;
		rightLeg.xRot = Mth.cos(walk) * 1.4F * state.walkAnimationSpeed;
		leftLeg.xRot = Mth.cos(walk + Mth.PI) * 1.4F * state.walkAnimationSpeed;
		leftArm.xRot = Mth.cos(walk) * 0.8F * state.walkAnimationSpeed;

		if (state.aiming) {
			rightArm.xRot = -Mth.HALF_PI + head.xRot;
			rightArm.yRot = head.yRot;
		} else {
			rightArm.xRot = Mth.cos(walk + Mth.PI) * 0.8F * state.walkAnimationSpeed;
		}
	}
}
