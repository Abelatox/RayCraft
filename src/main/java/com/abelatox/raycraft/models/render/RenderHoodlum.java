package com.abelatox.raycraft.models.render;

import org.lwjgl.opengl.GL11;

import com.abelatox.raycraft.capabilities.ModCapabilities;
import com.abelatox.raycraft.items.ModItems;
import com.abelatox.raycraft.lib.Reference;
import com.abelatox.raycraft.lib.Utils;
import com.abelatox.raycraft.models.ModelHoodlum;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;

public class RenderHoodlum extends EntityRenderer<LivingEntity> implements IRayCraftRender {
	float scale;
	private ModelHoodlum model;

	public RenderHoodlum(EntityRendererManager renderManager, ModelHoodlum model, float scale) {
		super(renderManager);
		this.model = model;
		this.scale = scale;
	}

	/*@Override
	public void render(LivingEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
		// model.render(matrixStackIn,
		// bufferIn.getBuffer(model.getRenderType(getEntityTexture(entityIn))),
		// packedLightIn, OverlayTexture.DEFAULT_LIGHT, 1F, 1F, 1F, 1F);
		System.out.println("Aa");
		super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}*/
	
	@Override
	public void doRender(LivingEntity entityLiving, float v, MatrixStack matrixStackIn, IRenderTypeBuffer iRenderTypeBuffer, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		matrixStackIn.push();
		{
			float ageInTicks = entityLiving.ticksExisted + v;

			float headYawOffset = this.interpolateRotation(entityLiving.prevRenderYawOffset, entityLiving.renderYawOffset, v);
			//float headYaw = this.interpolateRotation(entityLiving.prevRotationYawHead, entityLiving.rotationYawHead, v);
			float headYaw = entityLiving.prevRotationYaw + (entityLiving.rotationYaw - entityLiving.prevRotationYaw) * v;
			float headPitch = entityLiving.prevRotationPitch + (entityLiving.rotationPitch - entityLiving.prevRotationPitch) * v;

			//this.rotateCorpse(matrixStackIn, entityLiving, ageInTicks, headYawOffset, v);

			float limbSwingAmount = entityLiving.prevLimbSwingAmount + (entityLiving.limbSwingAmount - entityLiving.prevLimbSwingAmount) * v;
			float limbSwing = entityLiving.limbSwing - entityLiving.limbSwingAmount * (1.0F - v);

			// Minecraft.getInstance().textureManager.bindTexture(getEntityTexture(entityLiving));
			this.model.setRotationAngles(entityLiving, limbSwing, limbSwingAmount, ageInTicks, headYaw - headYawOffset, headPitch);
			this.model.render(matrixStackIn, iRenderTypeBuffer.getBuffer(model.getRenderType(getEntityTexture(entityLiving))), packedLightIn, packedOverlayIn, red, green, blue, alpha);
			
			matrixStackIn.push();
			{
				// In-hand item rendering
				ModelRenderer arm = (this.model).bipedRightArm;
				if (arm != null) {
					if (entityLiving.isSneaking())
						matrixStackIn.translate(0, -0.1, -0.2);
	
					//RenderSystem.disableTexture();
					matrixStackIn.translate(-0.22F, 0.05F, 0.2F);
	
					matrixStackIn.rotate(Vector3f.XP.rotationDegrees((float) Math.toDegrees(arm.rotateAngleX)));
					matrixStackIn.rotate(Vector3f.YP.rotationDegrees((float) Math.toDegrees(arm.rotateAngleY)));
					matrixStackIn.rotate(Vector3f.ZP.rotationDegrees((float) Math.toDegrees(arm.rotateAngleZ)));
	
					/*
					 * RenderSystem.rotatef((arm.rotateAngleX), 1, 0, 0);
					 * RenderSystem.rotatef((arm.rotateAngleY), 0, 1, 0);
					 * RenderSystem.rotatef((arm.rotateAngleZ), 0, 0, 1);
					 */
					matrixStackIn.translate(-0.05F, 0.45F, -0.75F);
					//RenderSystem.enableTexture();
	
					ItemStack stack = entityLiving.getHeldItem(Hand.MAIN_HAND);
					if (stack != null) {
						/*
						 * GlStateManager.rotatef(180.0F, 1.0F, 0.0F, 0.0F);
						 * GlStateManager.rotatef(180.0F, 0.0F, 1.0F, 0.0F);
						 * GlStateManager.rotatef(180.0F, 0.0F, 0.0F, 1.0F);
						 */
						matrixStackIn.rotate(Vector3f.XP.rotationDegrees(180));
						matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180));
						matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(180));
						//RenderSystem.color4f(1, 1, 1, 1);
						Minecraft.getInstance().getFirstPersonRenderer().renderItemSide(entityLiving, stack, TransformType.FIRST_PERSON_RIGHT_HAND, false, matrixStackIn, iRenderTypeBuffer, packedOverlayIn);
					}
				}
			}
			matrixStackIn.pop();
		}
		matrixStackIn.pop();
		
		if (ItemStack.areItemStacksEqual(entityLiving.getHeldItemMainhand(), new ItemStack(ModItems.barrel))) {
			matrixStackIn.push();
			matrixStackIn.scale(1.3F, 1.3F, 1.3F);
			matrixStackIn.translate(0, 1.28F, 0F);
			matrixStackIn.rotate(Vector3f.YN.rotationDegrees(entityLiving.prevRenderYawOffset));
			model.barrel.render(matrixStackIn, Minecraft.getInstance().getRenderTypeBuffers().getBufferSource().getBuffer(model.barrel.getRenderType(Utils.getBarrelTexture())), packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
			matrixStackIn.pop();
		}
		
	}

	private float interpolateRotation(float lowerLimit, float upperLimit, float range) {
		float f3;

		for (f3 = upperLimit - lowerLimit; f3 < -180.0F; f3 += 360.0F) {
			;
		}

		while (f3 >= 180.0F) {
			f3 -= 360.0F;
		}

		return lowerLimit + range * f3;
	}

	protected void rotateCorpse(MatrixStack matrixStackIn, LivingEntity entityLiving, float ageInTicks, float headYawOffset, float v) {
		matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180 + headYawOffset));

		if (entityLiving.deathTime > 0) {
			float f3 = ((float) entityLiving.deathTime + v - 1.0F) / 20.0F * 1.6F;
			f3 = MathHelper.sqrt(f3);

			if (f3 > 1.0F) {
				f3 = 1.0F;
			}
		}
	}

	@Override
	public void renderFirstPersonArm(PlayerEntity player, MatrixStack matrixStackIn, IRenderTypeBuffer buffer, int packedLightIn) {
		Minecraft mc = Minecraft.getInstance();
		RenderHelper.enableStandardItemLighting();
		matrixStackIn.push();
		{
			matrixStackIn.translate(1, -1, -0.6);
			matrixStackIn.rotate(Vector3f.XN.rotationDegrees(20));
			matrixStackIn.rotate(Vector3f.YP.rotationDegrees(150));
			matrixStackIn.rotate(Vector3f.ZN.rotationDegrees(50));

			if (mc.gameSettings.getPointOfView() == PointOfView.FIRST_PERSON && !mc.gameSettings.hideGUI && !player.isSleeping()) {
				model.swingProgress = 0.0F;
				model.isSneak = false;
				model.swimAnimation = 0.0F;
				model.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, player);
				model.bipedRightArm.rotateAngleX = 0.0F;

				model.bipedRightArm.render(matrixStackIn, buffer.getBuffer(model.getRenderType(getEntityTexture(player))), packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
			}
		}
		matrixStackIn.pop();
	}

	@Override
	public ResourceLocation getEntityTexture(LivingEntity entity) {
		return new ResourceLocation(Reference.MODID, "textures/models/" + ModCapabilities.get((PlayerEntity) entity).getPlayerType() + ".png");
	}
}