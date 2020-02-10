package com.abelatox.raycraft.models;

import java.util.function.Function;

import org.lwjgl.opengl.GL11;

import com.abelatox.raycraft.capabilities.ModCapabilities;
import com.abelatox.raycraft.items.ModItems;
import com.abelatox.raycraft.lib.Reference;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class ModelGlobox extends BipedModel {

	public ModelBarrel barrel;
	public ModelRenderer body;
	public ModelRenderer head;
	public ModelRenderer rightLeg;
	public ModelRenderer leftLeg;
	public ModelRenderer rightArm;
	public ModelRenderer belly1;
	public ModelRenderer leftArm;
	public ModelRenderer belly2;
	public ModelRenderer leftHand;
	public ModelRenderer leftFinger1;
	public ModelRenderer leftFinger2;
	public ModelRenderer leftFinger3;
	public ModelRenderer mouth1;
	public ModelRenderer eye2;
	public ModelRenderer eye1;
	public ModelRenderer mouth2;
	public ModelRenderer rightFoot;
	public ModelRenderer leftFoot;
	public ModelRenderer rightHand;
	public ModelRenderer rightFinger1;
	public ModelRenderer rightFinger2;
	public ModelRenderer rightFinger3;

	public ModelGlobox(float scale) {
		super(scale);
		this.textureWidth = 128;
		this.textureHeight = 128;
		this.barrel = new ModelBarrel();
		this.body = new ModelRenderer(this, 0, 97);
		this.body.setRotationPoint(-2.0F, 0.0F, 0.0F);
		this.body.addBox(-4.0F, 0.0F, -4.5F, 12, 22, 9, 0.0F);
		this.leftFinger2 = new ModelRenderer(this, 120, 8);
		this.leftFinger2.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.leftFinger2.addBox(0.0F, 0.0F, -0.5F, 1, 3, 1, 0.0F);
		this.leftLeg = new ModelRenderer(this, 6, 30);
		this.leftLeg.setRotationPoint(3.0F, 12.0F, -1.0F);
		this.leftLeg.addBox(0.5F, 10.0F, 0.0F, 1, 1, 1, 0.0F);
		this.eye1 = new ModelRenderer(this, 45, 0);
		this.eye1.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.eye1.addBox(1.0F, -8.0F, 0.0F, 2, 2, 2, 0.0F);
		this.mouth2 = new ModelRenderer(this, 26, 14);
		this.mouth2.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.mouth2.addBox(-3.0F, -4.0F, -9.0F, 6, 2, 2, 0.0F);
		this.head = new ModelRenderer(this, 0, 0);
		this.head.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.head.addBox(-7.0F, -6.0F, -4.0F, 14, 6, 8, 0.0F);
		this.rightFinger2 = new ModelRenderer(this, 120, 8);
		this.rightFinger2.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.rightFinger2.addBox(0.0F, 0.0F, -0.5F, 1, 3, 1, 0.0F);
		this.eye2 = new ModelRenderer(this, 56, 0);
		this.eye2.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.eye2.addBox(-3.0F, -8.0F, 0.0F, 2, 2, 2, 0.0F);
		this.rightFoot = new ModelRenderer(this, 30, 22);
		this.rightFoot.setRotationPoint(-4.0F, 1.0F, -4.5F);
		this.rightFoot.addBox(1.0F, 10.0F, 0.0F, 6, 1, 7, 0.0F);
		this.belly2 = new ModelRenderer(this, 68, 113);
		this.belly2.setRotationPoint(1.0F, 2.0F, -1.0F);
		this.belly2.addBox(0.0F, 0.0F, 0.0F, 8, 14, 1, 0.0F);
		this.rightHand = new ModelRenderer(this, 120, 0);
		this.rightHand.setRotationPoint(-1.0F, 12.0F, 0.0F);
		this.rightHand.addBox(-1.0F, 0.0F, -0.5F, 3, 2, 2, 0.0F);
		this.rightFinger3 = new ModelRenderer(this, 120, 8);
		this.rightFinger3.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.rightFinger3.addBox(1.0F, 0.0F, 0.0F, 1, 3, 1, 0.0F);
		this.mouth1 = new ModelRenderer(this, 0, 14);
		this.mouth1.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.mouth1.addBox(-5.0F, -5.0F, -7.0F, 10, 4, 3, 0.0F);
		this.rightFinger1 = new ModelRenderer(this, 120, 8);
		this.rightFinger1.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.rightFinger1.addBox(-1.0F, 0.0F, 0.0F, 1, 3, 1, 0.0F);
		this.leftArm = new ModelRenderer(this, 110, 0);
		this.leftArm.setRotationPoint(6.0F, 0.0F, -0.5F);
		this.leftArm.addBox(0.0F, 0.0F, 0.0F, 1, 12, 1, 0.0F);
		this.leftFoot = new ModelRenderer(this, 0, 22);
		this.leftFoot.setRotationPoint(-1.0F, 1.0F, -4.5F);
		this.leftFoot.addBox(-1.0F, 10.0F, 0.0F, 6, 1, 7, 0.0F);
		this.leftFinger3 = new ModelRenderer(this, 120, 8);
		this.leftFinger3.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.leftFinger3.addBox(1.0F, 0.0F, 0.0F, 1, 3, 1, 0.0F);
		this.rightLeg = new ModelRenderer(this, 0, 30);
		this.rightLeg.setRotationPoint(-4.0F, 12.0F, -1.0F);
		this.rightLeg.addBox(-0.5F, 10.0F, 0.0F, 1, 1, 1, 0.0F);
		this.rightArm = new ModelRenderer(this, 110, 0);
		this.rightArm.setRotationPoint(-6.0F, 0.0F, 0.0F);
		this.rightArm.addBox(-1.0F, 0.0F, 0.0F, 1, 12, 1, 0.0F);
		this.leftHand = new ModelRenderer(this, 120, 0);
		this.leftHand.setRotationPoint(0.0F, 12.0F, 0.0F);
		this.leftHand.addBox(-1.0F, 0.0F, -0.5F, 3, 2, 2, 0.0F);
		this.belly1 = new ModelRenderer(this, 43, 108);
		this.belly1.setRotationPoint(-3.0F, 2.0F, -6.0F);
		this.belly1.addBox(0.0F, 0.0F, 0.0F, 10, 18, 2, 0.0F);
		this.leftFinger1 = new ModelRenderer(this, 120, 8);
		this.leftFinger1.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.leftFinger1.addBox(-1.0F, 0.0F, 0.0F, 1, 3, 1, 0.0F);
		this.leftHand.addChild(this.leftFinger2);
		this.head.addChild(this.eye1);
		this.mouth1.addChild(this.mouth2);
		this.rightHand.addChild(this.rightFinger2);
		this.head.addChild(this.eye2);
		this.rightLeg.addChild(this.rightFoot);
		this.belly1.addChild(this.belly2);
		this.rightArm.addChild(this.rightHand);
		this.rightHand.addChild(this.rightFinger3);
		this.head.addChild(this.mouth1);
		this.rightHand.addChild(this.rightFinger1);
		this.leftLeg.addChild(this.leftFoot);
		this.leftHand.addChild(this.leftFinger3);
		this.leftArm.addChild(this.leftHand);
		this.body.addChild(this.belly1);
		this.leftHand.addChild(this.leftFinger1);
	}

	boolean isHoldingBarrel = false;
	boolean isSwimming = false;
	boolean isSleeping = false;
	boolean isCharging = false;

	float yaw = 0;
	float pitch = 0;

	@Override
	public void render(LivingEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, 1, entityIn);
		isHoldingBarrel = ItemStack.areItemStacksEqual(entityIn.getHeldItemMainhand(), new ItemStack(ModItems.barrel));
		isSwimming = entityIn.getPose() == Pose.SWIMMING;
		isSleeping = entityIn.isSleeping();
		isCharging = ModCapabilities.get((PlayerEntity) entityIn).getIsCharging();
		//System.out.println(isCharging);
		yaw = entityIn.prevRenderYawOffset;
		pitch = headPitch;

		super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
	}

	@Override
	public void render(MatrixStack matrixStackIn, IVertexBuilder builderIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		matrixStackIn.push();
		{

			if (isSwimming) {
				matrixStackIn.translate(0, 1.3, 0);
				matrixStackIn.rotate(Vector3f.XP.rotationDegrees(90));
			}

			if (isSleeping) {
				matrixStackIn.translate(0, 0, -1.5);
				matrixStackIn.rotate(Vector3f.XN.rotationDegrees(90));
				matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(180));
			} else {
				matrixStackIn.rotate(Vector3f.YN.rotationDegrees(yaw));
			}

			matrixStackIn.translate(0, 1.5, 0);
			matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180));
			matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(180));

			if (isHoldingBarrel) {
				this.rightArm.rotateAngleX = -3;
				this.leftArm.rotateAngleX = -3;

				matrixStackIn.push();
				{
					matrixStackIn.translate(0, 0.2, 0);
					this.head.render(matrixStackIn, builderIn, packedLightIn, OverlayTexture.DEFAULT_LIGHT, 1F, 1F, 1F, 1F);
				}
				matrixStackIn.pop();

				matrixStackIn.push();
				{
					matrixStackIn.scale(1.3F, 1.3F, 1.3F);
					matrixStackIn.translate(0, -0.7, -0.1);
					barrel.render(matrixStackIn, builderIn, packedLightIn, OverlayTexture.DEFAULT_LIGHT, 1F, 1F, 1F, 1F);
				}
				matrixStackIn.pop();
			} else {
				this.rightArm.rotateAngleZ = 0.1F;
				this.leftArm.rotateAngleZ = -0.1F;

				this.head.render(matrixStackIn, builderIn, packedLightIn, OverlayTexture.DEFAULT_LIGHT, 1F, 1F, 1F, 1F);
			}

			
			if (isCharging) {
				matrixStackIn.push();
				matrixStackIn.translate(0.18, 0.1, 0);
				this.leftArm.render(matrixStackIn, builderIn, packedLightIn, OverlayTexture.DEFAULT_LIGHT, 1F, 1F, 1F, 1F);
				matrixStackIn.pop();
				
				matrixStackIn.push();
				matrixStackIn.translate(-0.18, 0.1, 0);
				this.rightArm.render(matrixStackIn, builderIn, packedLightIn, OverlayTexture.DEFAULT_LIGHT, 1F, 1F, 1F, 1F);
				matrixStackIn.pop();
				
				matrixStackIn.push();
				matrixStackIn.scale(1.5F, 1, 1.5F);
				this.body.render(matrixStackIn, builderIn, packedLightIn, OverlayTexture.DEFAULT_LIGHT, 1F, 1F, 1F, 1F);
				matrixStackIn.pop();
			} else {
				this.rightArm.render(matrixStackIn, builderIn, packedLightIn, OverlayTexture.DEFAULT_LIGHT, 1F, 1F, 1F, 1F);
				this.leftArm.render(matrixStackIn, builderIn, packedLightIn, OverlayTexture.DEFAULT_LIGHT, 1F, 1F, 1F, 1F);
				this.body.render(matrixStackIn, builderIn, packedLightIn, OverlayTexture.DEFAULT_LIGHT, 1F, 1F, 1F, 1F);
			}
			this.leftLeg.render(matrixStackIn, builderIn, packedLightIn, OverlayTexture.DEFAULT_LIGHT, 1F, 1F, 1F, 1F);
			this.rightLeg.render(matrixStackIn, builderIn, packedLightIn, OverlayTexture.DEFAULT_LIGHT, 1F, 1F, 1F, 1F);

		}

		// super.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red,
		// green, blue, alpha);
		matrixStackIn.pop();

	}

	public void setRotateAngle(ModelRenderer RendererModel, float x, float y, float z) {
		RendererModel.rotateAngleX = x;
		RendererModel.rotateAngleY = y;
		RendererModel.rotateAngleZ = z;
	}

	public ModelRenderer getHandRenderer() {
		return this.rightArm;
	}

	public void setLivingAnimations(LivingEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTickTime) {
		this.swimAnimation = entitylivingbaseIn.getSwimAnimation(partialTickTime);
		super.setLivingAnimations(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTickTime);
	}

	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch, float scaleFactor, Entity entityIn) {
		LivingEntity entity = (LivingEntity) entityIn;

		this.head.rotateAngleY = headYaw / (180F / (float) Math.PI);
		this.head.rotateAngleX = headPitch / (180F / (float) Math.PI);

		this.leftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 0.8F * limbSwingAmount;
		this.rightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 0.8F * limbSwingAmount;

		this.leftArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 0.4F * limbSwingAmount;
		this.rightArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 0.4F * limbSwingAmount;

		if (entity.isSwingInProgress) {
			this.rightArm.rotateAngleX = MathHelper.sin(entity.swingProgress * 3.0F + (float) Math.PI) * 1.2F;
			this.rightArm.rotateAngleY = MathHelper.sin(entity.swingProgress * 3.0F + (float) Math.PI) * -0.2F;
			this.rightArm.rotateAngleZ = -MathHelper.cos(entity.swingProgress * 4.0F + (float) Math.PI) * 0.5F;
		}

		if (entityIn.getDistanceSq(entityIn.prevPosX, entityIn.prevPosY, entityIn.prevPosZ) <= 0.05F && !entity.isSwingInProgress) {
			this.rightArm.rotateAngleX = 0;
			this.rightArm.rotateAngleY = 0;
			this.rightArm.rotateAngleZ = 0F;
		} else if (!entity.isSwingInProgress && entityIn.getDistanceSq(entityIn.prevPosX, entityIn.prevPosY, entityIn.prevPosZ) > 0) {
			this.rightArm.rotateAngleY = 0;
			this.rightArm.rotateAngleZ = 0F;
		}

		switch (leftArmPose) {
		case EMPTY:
			leftArm.rotateAngleY = 0.0F;
			break;
		case BLOCK:
			leftArm.rotateAngleX = leftArm.rotateAngleX * 0.5F - 0.9424779F;
			leftArm.rotateAngleY = ((float) Math.PI / 6F);
			break;
		case ITEM:
			leftArm.rotateAngleX = leftArm.rotateAngleX * 0.5F - ((float) Math.PI / 10F);
			leftArm.rotateAngleY = 0.0F;
		}

		switch (rightArmPose) {
		case EMPTY:
			rightArm.rotateAngleY = 0.0F;
			break;
		case BLOCK:
			rightArm.rotateAngleX = rightArm.rotateAngleX * 0.5F - 0.9424779F;
			rightArm.rotateAngleY = (-(float) Math.PI / 6F);
			break;
		case ITEM:
			rightArm.rotateAngleX = rightArm.rotateAngleX * 0.5F - ((float) Math.PI / 10F);
			rightArm.rotateAngleY = 0.0F;
			break;
		case THROW_SPEAR:
			rightArm.rotateAngleX = rightArm.rotateAngleX * 0.5F - (float) Math.PI;
			rightArm.rotateAngleY = 0.0F;
		}

		if (entityIn.isCrouching()) {
			body.rotateAngleX = 0.5F;
			rightArm.rotateAngleX += 0.4F;
			leftArm.rotateAngleX += 0.4F;
			rightLeg.rotationPointZ = 4.0F;
			leftLeg.rotationPointZ = 4.0F;
			rightLeg.rotationPointY = 9.0F;
			leftLeg.rotationPointY = 9.0F;
			head.rotationPointY = 1.0F;
		} else {
			body.rotateAngleX = 0.0F;
			rightLeg.rotationPointZ = 0.1F;
			leftLeg.rotationPointZ = 0.1F;
			rightLeg.rotationPointY = 12.0F;
			leftLeg.rotationPointY = 12.0F;
			head.rotationPointY = 0.0F;
		}
	}
}
