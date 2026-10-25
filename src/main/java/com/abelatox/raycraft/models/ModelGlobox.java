package com.abelatox.raycraft.models;

import com.abelatox.raycraft.client.render.RaycraftAvatarRenderData;
import com.abelatox.raycraft.client.render.RaycraftRenderData;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;

public class ModelGlobox extends Model<AvatarRenderState> implements RaycraftCharacterModel {

	public final ModelPart head;
	public final ModelPart body;
	public final ModelPart rightArm;
	public final ModelPart leftArm;
	public final ModelPart rightLeg;
	public final ModelPart leftLeg;

	public ModelGlobox(ModelPart root) {
		super(root, RenderTypes::entityCutout);
		this.head = root.getChild("head");
		this.body = root.getChild("body");
		this.rightArm = root.getChild("right_arm");
		this.leftArm = root.getChild("left_arm");
		this.rightLeg = root.getChild("right_leg");
		this.leftLeg = root.getChild("left_leg");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition body = root.addOrReplaceChild("body",
				CubeListBuilder.create().texOffs(0, 97).addBox(-4.0F, 0.0F, -4.5F, 12, 22, 9),
				PartPose.offset(-2.0F, 0.0F, 0.0F));

		PartDefinition belly1 = body.addOrReplaceChild("belly1",
				CubeListBuilder.create().texOffs(43, 108).addBox(0.0F, 0.0F, 0.0F, 10, 18, 2),
				PartPose.offset(-3.0F, 2.0F, -6.0F));

		belly1.addOrReplaceChild("belly2",
				CubeListBuilder.create().texOffs(68, 113).addBox(0.0F, 0.0F, 0.0F, 8, 14, 1),
				PartPose.offset(1.0F, 2.0F, -1.0F));

		PartDefinition head = root.addOrReplaceChild("head",
				CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -6.0F, -4.0F, 14, 6, 8),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		head.addOrReplaceChild("eye1",
				CubeListBuilder.create().texOffs(45, 0).addBox(1.0F, -8.0F, 0.0F, 2, 2, 2),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		head.addOrReplaceChild("eye2",
				CubeListBuilder.create().texOffs(56, 0).addBox(-3.0F, -8.0F, 0.0F, 2, 2, 2),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition mouth1 = head.addOrReplaceChild("mouth1",
				CubeListBuilder.create().texOffs(0, 14).addBox(-5.0F, -5.0F, -7.0F, 10, 4, 3),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		mouth1.addOrReplaceChild("mouth2",
				CubeListBuilder.create().texOffs(26, 14).addBox(-3.0F, -4.0F, -9.0F, 6, 2, 2),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition rightArm = root.addOrReplaceChild("right_arm",
				CubeListBuilder.create().texOffs(110, 0).addBox(-1.0F, 0.0F, 0.0F, 1, 12, 1),
				PartPose.offset(-6.0F, 0.0F, 0.0F));

		PartDefinition rightHand = rightArm.addOrReplaceChild("right_hand",
				CubeListBuilder.create().texOffs(120, 0).addBox(-1.0F, 0.0F, -0.5F, 3, 2, 2),
				PartPose.offset(-1.0F, 12.0F, 0.0F));

		rightHand.addOrReplaceChild("right_finger1",
				CubeListBuilder.create().texOffs(120, 8).addBox(-1.0F, 0.0F, 0.0F, 1, 3, 1),
				PartPose.offset(0.0F, 0.0F, 0.0F));
		rightHand.addOrReplaceChild("right_finger2",
				CubeListBuilder.create().texOffs(120, 8).addBox(0.0F, 0.0F, -0.5F, 1, 3, 1),
				PartPose.offset(0.0F, 0.0F, 0.0F));
		rightHand.addOrReplaceChild("right_finger3",
				CubeListBuilder.create().texOffs(120, 8).addBox(1.0F, 0.0F, 0.0F, 1, 3, 1),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition leftArm = root.addOrReplaceChild("left_arm",
				CubeListBuilder.create().texOffs(110, 0).addBox(0.0F, 0.0F, 0.0F, 1, 12, 1),
				PartPose.offset(6.0F, 0.0F, -0.5F));

		PartDefinition leftHand = leftArm.addOrReplaceChild("left_hand",
				CubeListBuilder.create().texOffs(120, 0).addBox(-1.0F, 0.0F, -0.5F, 3, 2, 2),
				PartPose.offset(0.0F, 12.0F, 0.0F));

		leftHand.addOrReplaceChild("left_finger1",
				CubeListBuilder.create().texOffs(120, 8).addBox(-1.0F, 0.0F, 0.0F, 1, 3, 1),
				PartPose.offset(0.0F, 0.0F, 0.0F));
		leftHand.addOrReplaceChild("left_finger2",
				CubeListBuilder.create().texOffs(120, 8).addBox(0.0F, 0.0F, -0.5F, 1, 3, 1),
				PartPose.offset(0.0F, 0.0F, 0.0F));
		leftHand.addOrReplaceChild("left_finger3",
				CubeListBuilder.create().texOffs(120, 8).addBox(1.0F, 0.0F, 0.0F, 1, 3, 1),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition rightLeg = root.addOrReplaceChild("right_leg",
				CubeListBuilder.create().texOffs(0, 30).addBox(-0.5F, 10.0F, 0.0F, 1, 1, 1),
				PartPose.offset(-4.0F, 12.0F, -1.0F));

		rightLeg.addOrReplaceChild("right_foot",
				CubeListBuilder.create().texOffs(30, 22).addBox(1.0F, 10.0F, 0.0F, 6, 1, 7),
				PartPose.offset(-4.0F, 1.0F, -4.5F));

		PartDefinition leftLeg = root.addOrReplaceChild("left_leg",
				CubeListBuilder.create().texOffs(6, 30).addBox(0.5F, 10.0F, 0.0F, 1, 1, 1),
				PartPose.offset(3.0F, 12.0F, -1.0F));

		leftLeg.addOrReplaceChild("left_foot",
				CubeListBuilder.create().texOffs(0, 22).addBox(-1.0F, 10.0F, 0.0F, 6, 1, 7),
				PartPose.offset(-1.0F, 1.0F, -4.5F));

		return LayerDefinition.create(mesh, 128, 128);
	}

	@Override
	public void setupAnim(AvatarRenderState state) {
		RaycraftAvatarRenderData data = state.getRenderDataOrDefault(RaycraftRenderData.AVATAR_DATA, RaycraftAvatarRenderData.EMPTY);

		head.yRot = state.yRot / (180F / (float) Math.PI);
		head.xRot = state.xRot / (180F / (float) Math.PI);
		head.y = 0.0F;

		leftLeg.xRot = Mth.cos(state.walkAnimationPos * 0.6662F) * 0.8F * state.walkAnimationSpeed;
		rightLeg.xRot = Mth.cos(state.walkAnimationPos * 0.6662F + (float) Math.PI) * 0.8F * state.walkAnimationSpeed;

		leftArm.xRot = Mth.cos(state.walkAnimationPos * 0.6662F + (float) Math.PI) * 0.4F * state.walkAnimationSpeed;
		rightArm.xRot = Mth.cos(state.walkAnimationPos * 0.6662F) * 0.4F * state.walkAnimationSpeed;
		rightArm.zRot = 0.1F;
		leftArm.zRot = -0.1F;

		RaycraftModelAnim.applyAttackSwing(state, rightArm, leftArm);
		RaycraftModelAnim.applyShellRide(data, rightLeg, leftLeg, false);

		if (state.isCrouching) {
			body.xRot = 0.5F;
			rightArm.xRot += 0.4F;
			leftArm.xRot += 0.4F;
			head.y = 1.0F;
		} else {
			body.xRot = 0.0F;
		}

		if (data.carriesOverHead()) {
			rightArm.xRot = -3.0F;
			leftArm.xRot = -3.0F;
			rightArm.zRot = 0.1F;
			leftArm.zRot = -0.1F;
			head.y += 0.2F * 16.0F;
		}
	}

	@Override
	public ModelPart getArm(HumanoidArm arm) {
		return arm == HumanoidArm.LEFT ? leftArm : rightArm;
	}
}
