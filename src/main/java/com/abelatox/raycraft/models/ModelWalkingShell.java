package com.abelatox.raycraft.models;

import com.abelatox.raycraft.entities.render.RenderWalkingShell;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

public class ModelWalkingShell extends EntityModel<RenderWalkingShell.ShellRenderState> {

	private static final float TIRED_PITCH = 0.4F;
	private static final float REAR_PITCH = 0.7F;
	private static final float LEG_SPREAD = 1.3F;

	private final ModelPart body;
	private final ModelPart rightLeg;
	private final ModelPart leftLeg;

	public ModelWalkingShell(ModelPart root) {
		super(root);
		this.body = root.getChild("body");
		this.rightLeg = root.getChild("right_leg");
		this.leftLeg = root.getChild("left_leg");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();
		root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -1.0F, -15.0F, 2.0F, 2.0F, 2.0F).texOffs(8, 0).addBox(-2.0F, -2.0F, -13.0F, 4.0F, 4.0F, 2.0F).texOffs(20, 0).addBox(-4.0F, -4.0F, -11.0F, 8.0F, 8.0F, 2.0F).texOffs(0, 10).addBox(-6.0F, -6.0F, -9.0F, 12.0F, 12.0F, 2.0F).texOffs(0, 25).addBox(-6.5F, -6.5F, -7.0F, 13.0F, 13.0F, 6.0F).texOffs(0, 44).addBox(-5.5F, -5.5F, -1.0F, 11.0F, 11.0F, 6.0F).texOffs(34, 44).addBox(-4.5F, -4.5F, 5.0F, 9.0F, 9.0F, 3.0F).texOffs(0, 61).addBox(-4.0F, -4.0F, 8.0F, 8.0F, 8.0F, 2.0F).texOffs(20, 61).addBox(-3.0F, -3.0F, 10.0F, 6.0F, 6.0F, 1.0F), PartPose.offset(0.0F, 7.0F, 0.0F));
		root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(34, 61).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 10.0F, 1.0F).texOffs(0, 71).addBox(-1.5F, 10.0F, -4.0F, 3.0F, 2.0F, 6.0F), PartPose.offset(-2.5F, 12.0F, 0.0F));
		root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(18, 71).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 10.0F, 1.0F).texOffs(34, 71).addBox(-1.5F, 10.0F, -4.0F, 3.0F, 2.0F, 6.0F), PartPose.offset(2.5F, 12.0F, 0.0F));
		return LayerDefinition.create(mesh, 64, 128);
	}

	@Override
	public void setupAnim(RenderWalkingShell.ShellRenderState state) {
		super.setupAnim(state);
		if (state.flying) {
			float flap = Mth.sin(state.ageInTicks * 0.3F) * 0.15F;
			rightLeg.zRot = LEG_SPREAD + flap;
			leftLeg.zRot = -LEG_SPREAD - flap;
			rightLeg.xRot = leftLeg.xRot = 0.3F;
		}
		if (state.tired) {
			body.xRot = TIRED_PITCH;
			body.y += Mth.sin(state.ageInTicks * 0.8F) * 0.4F;
			return;
		}
		if (state.rear > 0.0F) {
			body.xRot = -REAR_PITCH * state.rear;
			return;
		}
		if (!state.flying) {
			float walk = state.walkAnimationPos * 0.9F;
			float amount = 1.3F * state.walkAnimationSpeed;
			rightLeg.xRot = Mth.cos(walk) * amount;
			leftLeg.xRot = Mth.cos(walk + Mth.PI) * amount;
		}
	}
}
