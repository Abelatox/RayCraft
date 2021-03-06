package com.abelatox.raycraft.models.render;

import com.abelatox.raycraft.capabilities.ModCapabilities;
import com.abelatox.raycraft.items.ModItems;
import com.abelatox.raycraft.lib.Reference;
import com.abelatox.raycraft.lib.Utils;
import com.abelatox.raycraft.models.ModelRoboPirate;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;

public class RenderPirate extends PlayerRenderer implements IRayCraftRender {
	float scale;
	private ModelRoboPirate model;

	public RenderPirate(EntityRendererManager renderManager, ModelRoboPirate model, float scale) {
		super(renderManager, false);
		this.model = model;
		this.scale = scale;
		addLayer(new HeldItemLayer<>(this));
	}

	/*
	 * @Override public void render(LivingEntity entityIn, float entityYaw, float
	 * partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int
	 * packedLightIn) { // model.render(matrixStackIn, //
	 * bufferIn.getBuffer(model.getRenderType(getEntityTexture(entityIn))), //
	 * packedLightIn, OverlayTexture.DEFAULT_LIGHT, 1F, 1F, 1F, 1F);
	 * System.out.println("Aa"); super.render(entityIn, entityYaw, partialTicks,
	 * matrixStackIn, bufferIn, packedLightIn); }
	 */

	@Override
	public void doRender(LivingEntity entityLiving, float v, MatrixStack matrixStackIn, IRenderTypeBuffer iRenderTypeBuffer, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		matrixStackIn.push();
		{
			matrixStackIn.push();

			float ageInTicks = entityLiving.ticksExisted + v;

			float headYawOffset = this.interpolateRotation(entityLiving.prevRenderYawOffset, entityLiving.renderYawOffset, v);
			float headYaw = this.interpolateRotation(entityLiving.prevRotationYawHead, entityLiving.rotationYawHead, v);
			float headPitch = entityLiving.prevRotationPitch + (entityLiving.rotationPitch - entityLiving.prevRotationPitch) * v;

			//this.rotateCorpse(matrixStackIn,entityLiving, ageInTicks, headYawOffset, v);

			float limbSwingAmount = entityLiving.prevLimbSwingAmount + (entityLiving.limbSwingAmount - entityLiving.prevLimbSwingAmount) * v;
			float limbSwing = entityLiving.limbSwing - entityLiving.limbSwingAmount * (1.0F - v);

			this.model.setRotationAngles(entityLiving, limbSwing, limbSwingAmount, ageInTicks, headYaw - headYawOffset, headPitch);
			this.model.render(matrixStackIn, iRenderTypeBuffer.getBuffer(model.getRenderType(getEntityTexture(entityLiving))), packedLightIn, packedOverlayIn, red, green, blue, alpha);
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
				model.rightArm.rotateAngleX = 0.0F;

				model.rightArm.render(matrixStackIn, buffer.getBuffer(model.getRenderType(getEntityTexture(player))), packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
			}
		}
		matrixStackIn.pop();
	}

	public ResourceLocation getEntityTexture(LivingEntity entity) {
		return new ResourceLocation(Reference.MODID, "textures/models/" + ModCapabilities.get((PlayerEntity) entity).getPlayerType() + ".png");
	}
}