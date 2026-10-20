package com.abelatox.raycraft.models;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;

/**
 * Helper shared by the character models: reproduces the attack swing (the old
 * {@code entity.isSwingInProgress}/{@code swingProgress} from 1.16.5) using the
 * render state's {@code swingAnimation} and {@code currentSwing}.
 */
public final class RaycraftModelAnim {

	private RaycraftModelAnim() {
	}

	public static void applyAttackSwing(AvatarRenderState state, ModelPart rightArm, ModelPart leftArm) {
		float swing = state.swingAnimation;
		if (swing <= 0F || state.currentSwing == null) {
			return;
		}

		boolean swingRight = state.currentSwing.hand().asArm(state.mainArm) != HumanoidArm.LEFT;
		ModelPart arm = swingRight ? rightArm : leftArm;
		float mirror = swingRight ? 1.0F : -1.0F;

		arm.xRot = Mth.sin(swing * 3.0F + (float) Math.PI) * 1.2F;
		arm.yRot = Mth.sin(swing * 3.0F + (float) Math.PI) * -0.2F * mirror;
		arm.zRot = -Mth.cos(swing * 4.0F + (float) Math.PI) * 0.5F * mirror;
	}
}
