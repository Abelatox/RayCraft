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

public class ModelHoodlum extends Model<AvatarRenderState> implements RaycraftCharacterModel {

	public final ModelPart head;
	public final ModelPart body;
	public final ModelPart rightArm;
	public final ModelPart leftArm;
	public final ModelPart rightLeg;
	public final ModelPart leftLeg;

	public ModelHoodlum(ModelPart root) {
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

		PartDefinition head = root.addOrReplaceChild("head",
				CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -5.0F, -4.0F, 8, 6, 8),
				PartPose.offset(0.0F, -1.0F, 0.0F));

		PartDefinition hat = head.addOrReplaceChild("hat",
				CubeListBuilder.create().texOffs(0, 46).addBox(-8.0F, 0.0F, -8.0F, 16, 1, 16),
				PartPose.offset(0.0F, -5.1F, 0.0F));

		PartDefinition hat2 = hat.addOrReplaceChild("hat2",
				CubeListBuilder.create().texOffs(32, 2).addBox(-3.0F, 0.0F, -3.0F, 6, 3, 6),
				PartPose.offset(0.0F, -3.0F, 0.0F));

		hat2.addOrReplaceChild("hat3",
				CubeListBuilder.create().texOffs(24, 0).addBox(0.0F, 0.0F, 0.0F, 3, 5, 3),
				PartPose.offsetAndRotation(-1.5F, -3.0F, -0.4F, -0.36425021489121656F, 0.0F, 0.0F));

		root.addOrReplaceChild("body",
				CubeListBuilder.create().texOffs(16, 16).addBox(-5.0F, 0.0F, -3.0F, 10, 24, 6),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		root.addOrReplaceChild("right_arm",
				CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-4.0F, -2.0F, -2.0F, 4, 12, 4),
				PartPose.offset(-5.0F, 2.0F, 0.0F));

		root.addOrReplaceChild("left_arm",
				CubeListBuilder.create().texOffs(0, 16).mirror().addBox(0.0F, -2.0F, -2.0F, 4, 12, 4),
				PartPose.offset(5.0F, 2.0F, 0.0F));

		root.addOrReplaceChild("right_leg",
				CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4, 9, 4),
				PartPose.offset(-1.9F, 12.0F, 0.1F));

		root.addOrReplaceChild("left_leg",
				CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -2.0F, 4, 9, 4),
				PartPose.offset(1.9F, 12.0F, 0.1F));

		return LayerDefinition.create(mesh, 64, 64);
	}

	@Override
	public void setupAnim(AvatarRenderState state) {
		RaycraftAvatarRenderData data = state.getRenderDataOrDefault(RaycraftRenderData.AVATAR_DATA, RaycraftAvatarRenderData.EMPTY);

		head.yRot = state.yRot / (180F / (float) Math.PI);
		head.xRot = state.xRot / (180F / (float) Math.PI);
		head.y = 0.0F;

		leftLeg.xRot = 0.0F;
		rightLeg.xRot = 0.0F;

		rightArm.xRot = Mth.cos(state.walkAnimationPos * 0.6662F) * 0.4F * state.walkAnimationSpeed;
		leftArm.xRot = Mth.cos(state.walkAnimationPos * 0.6662F + (float) Math.PI) * 0.4F * state.walkAnimationSpeed;

		RaycraftModelAnim.applyAttackSwing(state, rightArm, leftArm);

		if (state.isCrouching) {
			body.xRot = 0.5F;
			rightArm.xRot += 0.4F;
			leftArm.xRot += 0.4F;
			head.y = 1.0F;
		} else {
			body.xRot = 0.0F;
		}

		if (data.holdingBarrel()) {
			rightArm.xRot = -3.0F;
			leftArm.xRot = -3.0F;
			head.y += 0.2F * 16.0F;
		} else if (data.charging()) {
			leftArm.xRot = -(90.0F - state.xRot) * ((float) Math.PI / 180F);
			rightArm.xRot = -(100.0F - state.xRot) * ((float) Math.PI / 180F);
			rightArm.zRot = -0.7853981634F;
		}
	}

	@Override
	public ModelPart getArm(HumanoidArm arm) {
		return arm == HumanoidArm.LEFT ? leftArm : rightArm;
	}
}
