package com.abelatox.raycraft.models;

import com.abelatox.raycraft.capabilities.ModCapabilities;
import com.abelatox.raycraft.items.ModItems;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

public class ModelHoodlum extends BipedModel {

	public ModelRenderer body;
    public ModelRenderer leftArm;
    public ModelRenderer head;
    public ModelRenderer rightArm;
    public ModelRenderer hat;
    public ModelRenderer hat2;
    public ModelRenderer hat3;

	public ModelHoodlum(float size) {
		super(size);
		this.textureWidth = 64;
		this.textureHeight = 64;
        this.head = new ModelRenderer(this, 0, 0);
        this.head.setRotationPoint(0.0F, -1.0F, 0.0F);
        this.head.addBox(-4.0F, -5.0F, -4.0F, 8, 6, 8, 0.0F);
        this.hat3 = new ModelRenderer(this, 24, 0);
        this.hat3.setRotationPoint(-1.5F, -3.0F, -0.4F);
        this.hat3.addBox(0.0F, 0.0F, 0.0F, 3, 5, 3, 0.0F);
        this.setRotateAngle(hat3, -0.36425021489121656F, 0.0F, 0.0F);
        this.hat = new ModelRenderer(this, 0, 46);
        this.hat.setRotationPoint(0.0F, -5.1F, 0.0F);
        this.hat.addBox(-8.0F, 0.0F, -8.0F, 16, 1, 16, 0.0F);
        this.hat2 = new ModelRenderer(this, 32, 2);
        this.hat2.setRotationPoint(0.0F, -3.0F, 0.0F);
        this.hat2.addBox(-3.0F, 0.0F, -3.0F, 6, 3, 6, 0.0F);
        this.body = new ModelRenderer(this, 16, 16);
        this.body.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.body.addBox(-5.0F, 0.0F, -3.0F, 10, 24, 6, 0.0F);
        this.rightArm = new ModelRenderer(this, 0, 16);
        this.rightArm.mirror = true;
        this.rightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
        this.rightArm.addBox(-4.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F);
        this.leftArm = new ModelRenderer(this, 0, 16);
        this.leftArm.mirror = true;
        this.leftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
        this.leftArm.addBox(0.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F);
        this.hat2.addChild(this.hat3);
        this.head.addChild(this.hat);
        this.hat.addChild(this.hat2);
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
		
		if(isCharging) {
			yaw = entityIn.rotationYaw;
			entityIn.setRotationYawHead(entityIn.prevRenderYawOffset);
		} else {
			yaw = entityIn.prevRenderYawOffset;
		}
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

				//this.rightLeg.render(matrixStackIn, builderIn, packedLightIn, OverlayTexture.DEFAULT_LIGHT, 1F, 1F, 1F, 1F);
				this.leftArm.render(matrixStackIn, builderIn, packedLightIn, OverlayTexture.DEFAULT_LIGHT, 1F, 1F, 1F, 1F);
				this.body.render(matrixStackIn, builderIn, packedLightIn, OverlayTexture.DEFAULT_LIGHT, 1F, 1F, 1F, 1F);

				matrixStackIn.push();
				{
					matrixStackIn.translate(0, 0.2, 0);
					this.head.render(matrixStackIn, builderIn, packedLightIn, OverlayTexture.DEFAULT_LIGHT, 1F, 1F, 1F, 1F);
				}
				matrixStackIn.pop();

				//this.leftLeg.render(matrixStackIn, builderIn, packedLightIn, OverlayTexture.DEFAULT_LIGHT, 1F, 1F, 1F, 1F);
				this.rightArm.render(matrixStackIn, builderIn, packedLightIn, OverlayTexture.DEFAULT_LIGHT, 1F, 1F, 1F, 1F);
								
			} else {
				this.head.render(matrixStackIn, builderIn, packedLightIn, OverlayTexture.DEFAULT_LIGHT, 1F, 1F, 1F, 1F);

				if (isCharging) {
					matrixStackIn.push();
					matrixStackIn.rotate(Vector3f.XN.rotationDegrees(90 - pitch));
					this.leftArm.render(matrixStackIn, builderIn, packedLightIn, OverlayTexture.DEFAULT_LIGHT, 1F, 1F, 1F, 1F);
					matrixStackIn.pop();
					
					matrixStackIn.push();
					//matrixStackIn.translate(0, 0.2, 0.2);
					matrixStackIn.rotate(Vector3f.XN.rotationDegrees(100 - pitch));
					matrixStackIn.rotate(Vector3f.ZN.rotationDegrees(45));
					this.rightArm.render(matrixStackIn, builderIn, packedLightIn, OverlayTexture.DEFAULT_LIGHT, 1F, 1F, 1F, 1F);
					matrixStackIn.pop();
					
					matrixStackIn.push();
					matrixStackIn.scale(1.5F, 1, 1.5F);
					matrixStackIn.pop();
				} else {
					
					this.rightArm.render(matrixStackIn, builderIn, packedLightIn, OverlayTexture.DEFAULT_LIGHT, 1F, 1F, 1F, 1F);
					this.leftArm.render(matrixStackIn, builderIn, packedLightIn, OverlayTexture.DEFAULT_LIGHT, 1F, 1F, 1F, 1F);
				}
				this.body.render(matrixStackIn, builderIn, packedLightIn, OverlayTexture.DEFAULT_LIGHT, 1F, 1F, 1F, 1F);

				//this.leftLeg.render(matrixStackIn, builderIn, packedLightIn, OverlayTexture.DEFAULT_LIGHT, 1F, 1F, 1F, 1F);
				//this.rightLeg.render(matrixStackIn, builderIn, packedLightIn, OverlayTexture.DEFAULT_LIGHT, 1F, 1F, 1F, 1F);

				/*
				 * matrixStackIn.push(); matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90));
				 * matrixStackIn.rotate(Vector3f.ZN.rotationDegrees(90)); //GL11.glRotated(-90,
				 * 0, 0, 1); GL11.glRotated(90, 0, 1, 0); GL11.glTranslated(0.4, 0.2, -0.7);
				 * //Minecraft.getInstance().gameRenderer.itemRenderer.renderItem((PlayerEntity)
				 * entity, ((PlayerEntity) entity).getHeldItemMainhand(),
				 * TransformType.THIRD_PERSON_RIGHT_HAND);
				 */
			}
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

		//this.leftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 0.8F * limbSwingAmount;
		//this.rightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 0.8F * limbSwingAmount;

		this.rightArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 0.4F * limbSwingAmount;
		this.leftArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 0.4F * limbSwingAmount;

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
			//rightLeg.rotationPointZ = 4.0F;
		//	leftLeg.rotationPointZ = 4.0F;
			//rightLeg.rotationPointY = 9.0F;
		//	leftLeg.rotationPointY = 9.0F;
			head.rotationPointY = 1.0F;
		} else {
			body.rotateAngleX = 0.0F;
		//	rightLeg.rotationPointZ = 0.1F;
		//	leftLeg.rotationPointZ = 0.1F;
		//	rightLeg.rotationPointY = 12.0F;
		//	leftLeg.rotationPointY = 12.0F;
			head.rotationPointY = 0.0F;
		}
	}
}
