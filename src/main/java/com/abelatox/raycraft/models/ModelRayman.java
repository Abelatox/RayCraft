package com.abelatox.raycraft.models;

import org.lwjgl.opengl.GL11;

import com.abelatox.raycraft.capabilities.IPlayerModelCapability;
import com.abelatox.raycraft.capabilities.ModCapabilities;
import com.abelatox.raycraft.items.ModItems;
import com.abelatox.raycraft.lib.Reference;
import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class ModelRayman extends BipedModel {
	public ModelBarrel barrel;
	public RendererModel rightArm;
	public RendererModel rightLeg;
	public RendererModel head;
	public RendererModel body;
	public RendererModel leftArm;
	public RendererModel leftLeg;
	public RendererModel rightHand;
	public RendererModel rightFoot;
	public RendererModel nose;
	public RendererModel leftHand;
	public RendererModel leftFoot;
	public RendererModel leftEar1;
	public RendererModel rightEar1;
	public RendererModel leftEar2;
	public RendererModel rightEar2;

	// float armRotation = 0;

	public ModelRayman() {
		this.textureWidth = 64;
		this.textureHeight = 64;
		this.barrel = new ModelBarrel();
		this.leftEar2 = new RendererModel(this, 40, 0);
		this.leftEar2.setRotationPoint(0.0F, 0.0F, -0.3F);
		this.leftEar2.addBox(0.0F, -3.0F, 0.0F, 2, 4, 1, 0.0F);
		this.setRotateAngle(leftEar2, 0.6108652381980153F, 0.0F, 0.0F);
		this.rightHand = new RendererModel(this, 0, 37);
		this.rightHand.setRotationPoint(-3.0F, 6.0F, -2.0F);
		this.rightHand.addBox(0.0F, 0.0F, 0.0F, 4, 4, 4, 0.0F);
		this.nose = new RendererModel(this, 18, 32);
		this.nose.setRotationPoint(-2.5F, -3.0F, -7.0F);
		this.nose.addBox(0.0F, 0.0F, 0.0F, 5, 3, 3, 0.0F);
		this.rightArm = new RendererModel(this, 40, 16);
		this.rightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
		this.rightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 8, 4, 0.0F);
		this.leftLeg = new RendererModel(this, 0, 16);
		this.leftLeg.mirror = true;
		this.leftLeg.setRotationPoint(1.9F, 12.0F, 0.1F);
		this.leftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 9, 4, 0.0F);
		this.leftHand = new RendererModel(this, 0, 37);
		this.leftHand.setRotationPoint(-1.0F, 6.0F, -2.0F);
		this.leftHand.addBox(0.0F, 0.0F, 0.0F, 4, 4, 4, 0.0F);
		this.rightFoot = new RendererModel(this, 0, 29);
		this.rightFoot.setRotationPoint(-2.0F, 9.0F, -3.0F);
		this.rightFoot.addBox(0.0F, 0.0F, 0.0F, 4, 3, 5, 0.0F);
		this.rightEar2 = new RendererModel(this, 40, 0);
		this.rightEar2.setRotationPoint(0.0F, 0.0F, -0.3F);
		this.rightEar2.addBox(0.0F, -3.0F, 0.0F, 2, 4, 1, 0.0F);
		this.setRotateAngle(rightEar2, 0.6108652381980153F, 0.0F, 0.0F);
		this.rightLeg = new RendererModel(this, 0, 16);
		this.rightLeg.setRotationPoint(-1.9F, 12.0F, 0.1F);
		this.rightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 9, 4, 0.0F);
		this.body = new RendererModel(this, 16, 16);
		this.body.setRotationPoint(0.0F, 1.5F, 0.0F);
		this.body.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, 0.0F);
		this.leftFoot = new RendererModel(this, 0, 29);
		this.leftFoot.setRotationPoint(-2.0F, 9.0F, -3.0F);
		this.leftFoot.addBox(0.0F, 0.0F, 0.0F, 4, 3, 5, 0.0F);
		this.leftEar1 = new RendererModel(this, 40, 0);
		this.leftEar1.setRotationPoint(-4.0F, -11.0F, -1.0F);
		this.leftEar1.addBox(0.0F, 0.0F, 0.0F, 2, 4, 1, 0.0F);
		this.setRotateAngle(leftEar1, 0.2617993877991494F, 0.0F, -0.2617993877991494F);
		this.leftArm = new RendererModel(this, 40, 16);
		this.leftArm.mirror = true;
		this.leftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
		this.leftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 8, 4, 0.0F);
		this.head = new RendererModel(this, 0, 0);
		this.head.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F);
		this.rightEar1 = new RendererModel(this, 40, 0);
		this.rightEar1.setRotationPoint(2.0F, -11.5F, -1.0F);
		this.rightEar1.addBox(0.0F, 0.0F, 0.0F, 2, 4, 1, 0.0F);
		this.setRotateAngle(rightEar1, 0.2617993877991494F, 0.0F, 0.2617993877991494F);
		this.leftEar1.addChild(this.leftEar2);
		this.rightArm.addChild(this.rightHand);
		this.head.addChild(this.nose);
		this.leftArm.addChild(this.leftHand);
		this.rightLeg.addChild(this.rightFoot);
		this.rightEar1.addChild(this.rightEar2);
		this.leftLeg.addChild(this.leftFoot);
		this.head.addChild(this.leftEar1);
		this.head.addChild(this.rightEar1);
	}

	@Override
	public void render(LivingEntity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		if (entity.isSneaking()) {
			GlStateManager.translatef(0.0F, 0.2F, 0.0F);
		}

		if (entity instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) entity;
			IPlayerModelCapability props = ModCapabilities.get(player);
			if(player instanceof ServerPlayerEntity) { //TODO Other player
				if(props.getCharging()) {
					System.out.println("Remote player charging");
				}
				//System.out.println(props.getCharging());
			}
			
			this.body.offsetY = 0.1F;
			if (ItemStack.areItemStacksEqual(player.getHeldItemMainhand(), new ItemStack(ModItems.barrel))) {
			//if (ModCapabilities.get(player).getCharging()) {
				this.rightArm.rotateAngleX = -3;
				this.leftArm.rotateAngleX = -3;
				this.head.offsetY = 0.1F;

				this.rightLeg.render(f5);
				this.leftArm.render(f5);
				this.body.render(f5);
				this.head.render(f5);
				this.leftLeg.render(f5);
				this.rightArm.render(f5);
				GL11.glPushMatrix();
				{
					Minecraft.getInstance().textureManager.bindTexture(new ResourceLocation(Reference.MODID, "textures/models/barrel.png"));
					GL11.glScaled(1.3, 1.3, 1.3);
					GL11.glTranslated(0, -0.75, -0.1);
					this.barrel.render(entity, f, f1, f2, f3, f4, f5);
				}
				GL11.glPopMatrix();
			} else {
				this.head.offsetY = 0;

				this.rightLeg.render(f5);
				this.body.render(f5);
				this.head.render(f5);
				this.leftLeg.render(f5);
				if (ModCapabilities.get(player).getShotLevel() == 3) {
					GL11.glColor3d(1, 1, 0);
				}
				this.leftArm.render(f5);
		
				/*if (ModCapabilities.get(player).getCharging()) {
				//	armRotation += 0.3;
					this.rightArm.rotateAngleX += 0.3;
				} else {
					//armRotation = 0;
				}
				//this.rightArm.rotateAngleX = armRotation;*/

				this.rightArm.render(f5);
				GL11.glColor3d(1, 1, 1);

				GL11.glPushMatrix();
				{
					GL11.glRotated(90, 0, 1, 0);
					GL11.glRotated(-90, 0, 0, 1);
					GL11.glRotated(90, 0, 1, 0);
					GL11.glTranslated(0.4, 0.2, -0.7);
					Minecraft.getInstance().gameRenderer.itemRenderer.renderItem((PlayerEntity) entity, ((PlayerEntity) entity).getHeldItemMainhand(), TransformType.THIRD_PERSON_RIGHT_HAND);
				}
				GL11.glPopMatrix();
			}
		}

	}

	/**
	 * This is a helper function from Tabula to set the rotation of model parts
	 */
	public void setRotateAngle(RendererModel RendererModel, float x, float y, float z) {
		RendererModel.rotateAngleX = x;
		RendererModel.rotateAngleY = y;
		RendererModel.rotateAngleZ = z;
	}

	public RendererModel getHandRenderer() {
		return this.rightArm;
	}

	public void setLivingAnimations(LivingEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTickTime) {
		this.swimAnimation = entitylivingbaseIn.getSwimAnimation(partialTickTime);
		super.setLivingAnimations(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTickTime);
	}

	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch, float scaleFactor, Entity entityIn) {

		LivingEntity entity = ((LivingEntity) entityIn);

		this.head.rotateAngleY = headYaw / (180F / (float) Math.PI);
		this.head.rotateAngleX = headPitch / (180F / (float) Math.PI);

		this.leftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 0.8F * limbSwingAmount;
		this.rightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 0.8F * limbSwingAmount;

		this.rightArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 0.4F * limbSwingAmount;
		this.leftArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 0.4F * limbSwingAmount;

		if (entity.isSwingInProgress) {
			this.rightArm.rotateAngleX = MathHelper.sin(entity.swingProgress * 3.0F + (float) Math.PI) * 1.2F;
			this.rightArm.rotateAngleY = MathHelper.sin(entity.swingProgress * 3.0F + (float) Math.PI) * -0.2F;
			this.rightArm.rotateAngleZ = -MathHelper.cos(entity.swingProgress * 4.0F + (float) Math.PI) * 0.5F;
		}

		if (entityIn.getDistanceSq(entityIn.prevPosX, entityIn.prevPosY, entityIn.prevPosZ) <= 0.05F && !entity.isSwingInProgress) {
			//this.rightArm.rotateAngleX = 0;
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

		if (entityIn.isSneaking()) {
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
