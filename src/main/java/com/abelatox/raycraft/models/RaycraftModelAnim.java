package com.abelatox.raycraft.models;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;

/**
 * Ayudante compartido por los modelos de personaje: reproduce el swing de ataque
 * (equivalente al viejo {@code entity.isSwingInProgress}/{@code swingProgress} de 1.16.5)
 * usando {@link AvatarRenderState#attackTime} y {@link AvatarRenderState#attackArm}.
 */
public final class RaycraftModelAnim {

	private RaycraftModelAnim() {
	}

	public static void applyAttackSwing(AvatarRenderState state, ModelPart rightArm, ModelPart leftArm) {
		if (state.attackTime <= 0F) {
			return;
		}

		boolean swingRight = state.attackArm != HumanoidArm.LEFT;
		ModelPart arm = swingRight ? rightArm : leftArm;
		float mirror = swingRight ? 1.0F : -1.0F;

		arm.xRot = Mth.sin(state.attackTime * 3.0F + (float) Math.PI) * 1.2F;
		arm.yRot = Mth.sin(state.attackTime * 3.0F + (float) Math.PI) * -0.2F * mirror;
		arm.zRot = -Mth.cos(state.attackTime * 4.0F + (float) Math.PI) * 0.5F * mirror;
	}
}
