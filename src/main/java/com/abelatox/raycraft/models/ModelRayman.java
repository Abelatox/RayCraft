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

public class ModelRayman extends Model<AvatarRenderState> implements RaycraftCharacterModel {

	public final ModelPart head;
	public final ModelPart body;
	public final ModelPart rightArm;
	public final ModelPart leftArm;
	public final ModelPart rightLeg;
	public final ModelPart leftLeg;
	public final ModelPart leftEar1;
	public final ModelPart rightEar1;
	/** How far each lock of hair rises from the horizontal: the helicopter is a V. */
	private static final float HELI_V_ANGLE = 0.6F;

	public final ModelPart heli1;

	public static final int GOLD_FIST_LEVEL = 3;
	public static final int GOLD_TINT = 0xFFFFE600;

	// The tint applies to the whole model, so gold arms are drawn by a second instance that only shows the arms.
	private final boolean armsOnly;

	public ModelRayman(ModelPart root) {
		this(root, false);
	}

	public ModelRayman(ModelPart root, boolean armsOnly) {
		super(root, RenderTypes::entityCutout);
		this.armsOnly = armsOnly;
		this.head = root.getChild("head");
		this.body = root.getChild("body");
		this.rightArm = root.getChild("right_arm");
		this.leftArm = root.getChild("left_arm");
		this.rightLeg = root.getChild("right_leg");
		this.leftLeg = root.getChild("left_leg");
		this.leftEar1 = head.getChild("left_ear1");
		this.rightEar1 = head.getChild("right_ear1");
		this.heli1 = head.getChild("heli1");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition head = root.addOrReplaceChild("head",
				CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition leftLeg = root.addOrReplaceChild("left_leg",
				CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -2.0F, 4, 9, 4),
				PartPose.offset(1.9F, 12.0F, 0.1F));

		leftLeg.addOrReplaceChild("left_foot",
				CubeListBuilder.create().texOffs(0, 29).addBox(0.0F, 0.0F, 0.0F, 4, 3, 5),
				PartPose.offset(-2.0F, 9.0F, -3.0F));

		PartDefinition rightLeg = root.addOrReplaceChild("right_leg",
				CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4, 9, 4),
				PartPose.offset(-1.9F, 12.0F, 0.1F));

		rightLeg.addOrReplaceChild("right_foot",
				CubeListBuilder.create().texOffs(0, 29).addBox(0.0F, 0.0F, 0.0F, 4, 3, 5),
				PartPose.offset(-2.0F, 9.0F, -3.0F));

		PartDefinition rightEar1 = head.addOrReplaceChild("right_ear1",
				CubeListBuilder.create().texOffs(40, 0).addBox(0.0F, 0.0F, 0.0F, 2, 4, 1),
				PartPose.offsetAndRotation(2.0F, -11.5F, -1.0F, 0.2617993877991494F, 0.0F, 0.2617993877991494F));

		rightEar1.addOrReplaceChild("right_ear2",
				CubeListBuilder.create().texOffs(40, 0).addBox(0.0F, -3.0F, 0.0F, 2, 4, 1),
				PartPose.offsetAndRotation(0.0F, 0.0F, -0.3F, 0.6108652381980153F, 0.0F, 0.0F));

		PartDefinition heli1 = head.addOrReplaceChild("heli1", CubeListBuilder.create(), PartPose.offset(0.0F, -8.0F, 0.0F));

		heli1.addOrReplaceChild("heli_left",
				CubeListBuilder.create().texOffs(17, 39).addBox(0.0F, -2.0F, -1.0F, 5, 2, 2),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -HELI_V_ANGLE));

		heli1.addOrReplaceChild("heli_right",
				CubeListBuilder.create().texOffs(17, 39).mirror().addBox(-5.0F, -2.0F, -1.0F, 5, 2, 2),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, HELI_V_ANGLE));

		head.addOrReplaceChild("nose",
				CubeListBuilder.create().texOffs(18, 32).addBox(0.0F, 0.0F, 0.0F, 5, 3, 3),
				PartPose.offset(-2.5F, -3.0F, -7.0F));

		PartDefinition leftEar1 = head.addOrReplaceChild("left_ear1",
				CubeListBuilder.create().texOffs(40, 0).addBox(0.0F, 0.0F, 0.0F, 2, 4, 1),
				PartPose.offsetAndRotation(-4.0F, -11.0F, -1.0F, 0.2617993877991494F, 0.0F, -0.2617993877991494F));

		leftEar1.addOrReplaceChild("left_ear2",
				CubeListBuilder.create().texOffs(40, 0).addBox(0.0F, -3.0F, 0.0F, 2, 4, 1),
				PartPose.offsetAndRotation(0.0F, 0.0F, -0.3F, 0.6108652381980153F, 0.0F, 0.0F));

		root.addOrReplaceChild("body",
				CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4),
				PartPose.offset(0.0F, 1.5F, 0.0F));

		PartDefinition rightArm = root.addOrReplaceChild("right_arm",
				CubeListBuilder.create().texOffs(40, 16).addBox(-3.0F, -2.0F, -2.0F, 4, 8, 4),
				PartPose.offset(-5.0F, 2.0F, 0.0F));

		rightArm.addOrReplaceChild("right_hand",
				CubeListBuilder.create().texOffs(0, 37).addBox(0.0F, 0.0F, 0.0F, 4, 4, 4),
				PartPose.offset(-3.0F, 6.0F, -2.0F));

		PartDefinition leftArm = root.addOrReplaceChild("left_arm",
				CubeListBuilder.create().texOffs(40, 16).mirror().addBox(-1.0F, -2.0F, -2.0F, 4, 8, 4),
				PartPose.offset(5.0F, 2.0F, 0.0F));

		leftArm.addOrReplaceChild("left_hand",
				CubeListBuilder.create().texOffs(0, 37).addBox(0.0F, 0.0F, 0.0F, 4, 4, 4),
				PartPose.offset(-1.0F, 6.0F, -2.0F));

		return LayerDefinition.create(mesh, 64, 64);
	}

	@Override
	public void setupAnim(AvatarRenderState state) {
		RaycraftAvatarRenderData data = state.getRenderDataOrDefault(RaycraftRenderData.AVATAR_DATA, RaycraftAvatarRenderData.EMPTY);

		head.yRot = state.yRot / (180F / (float) Math.PI);
		head.xRot = state.xRot / (180F / (float) Math.PI);
		head.y = 0.0F;

		leftLeg.xRot = Mth.cos(state.walkAnimationPos * 0.6662F) * 0.8F * state.walkAnimationSpeed;
		rightLeg.xRot = Mth.cos(state.walkAnimationPos * 0.6662F + (float) Math.PI) * 0.8F * state.walkAnimationSpeed;

		rightArm.xRot = Mth.cos(state.walkAnimationPos * 0.6662F) * 0.4F * state.walkAnimationSpeed;
		leftArm.xRot = Mth.cos(state.walkAnimationPos * 0.6662F + (float) Math.PI) * 0.4F * state.walkAnimationSpeed;
		rightArm.yRot = 0.0F;
		rightArm.zRot = 0.0F;
		leftArm.yRot = 0.0F;
		leftArm.zRot = 0.0F;

		RaycraftModelAnim.applyAttackSwing(state, rightArm, leftArm);
		RaycraftModelAnim.applyShellRide(data, rightLeg, leftLeg, true);

		if (state.isCrouching) {
			body.xRot = 0.5F;
			rightArm.xRot += 0.4F;
			leftArm.xRot += 0.4F;
			rightLeg.z = 4.0F;
			leftLeg.z = 4.0F;
			rightLeg.y = 9.0F;
			leftLeg.y = 9.0F;
			head.y = 1.0F;
		} else {
			body.xRot = 0.0F;
			rightLeg.z = 0.1F;
			leftLeg.z = 0.1F;
			rightLeg.y = 12.0F;
			leftLeg.y = 12.0F;
		}

		if (data.gliding()) {
			heli1.yRot = data.hairRotation();
			leftEar1.visible = false;
			rightEar1.visible = false;
			heli1.visible = true;
		} else {
			leftEar1.visible = true;
			rightEar1.visible = true;
			heli1.visible = false;
		}

		if (data.carriesOverHead()) {
			rightArm.xRot = -3.0F;
			leftArm.xRot = -3.0F;
			head.y += 0.2F * 16.0F;
		} else if (data.charging()) {
			rightArm.xRot = data.armRotation();
		}

		boolean goldArms = data.punchLevel() == GOLD_FIST_LEVEL;
		head.visible = !armsOnly;
		body.visible = !armsOnly;
		rightLeg.visible = !armsOnly;
		leftLeg.visible = !armsOnly;
		rightArm.visible = armsOnly || !goldArms;
		leftArm.visible = armsOnly || !goldArms;
	}

	@Override
	public ModelPart getArm(HumanoidArm arm) {
		return arm == HumanoidArm.LEFT ? leftArm : rightArm;
	}
}
