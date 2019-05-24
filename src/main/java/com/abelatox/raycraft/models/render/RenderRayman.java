package com.abelatox.raycraft.models.render;

import org.lwjgl.opengl.GL11;

import com.abelatox.raycraft.capabilities.ModCapabilities;
import com.abelatox.raycraft.lib.Reference;
import com.abelatox.raycraft.models.ModelRayman;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class RenderRayman extends Render<EntityLivingBase> implements IRayCraftRender{
	float scale;
	private ModelRayman model;
	private float offset[] = new float[3];

	public RenderRayman(RenderManager renderManager, ModelRayman model, float scale) {
		super(renderManager);
		this.model = model;
		this.scale = scale;
		this.offset = new float[] { 0, 0, 0 };
	}

	@Override
	public void doRender(EntityLivingBase entityLiving, double x, double y, double z, float u, float v) {
		GL11.glPushMatrix();
		{
			GL11.glTranslatef((float) x + this.offset[0], (float) y + 1.5F + this.offset[1], (float) z + this.offset[2]);

			GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
			GL11.glScaled(this.scale, this.scale, this.scale);
			float ageInTicks = entityLiving.ticksExisted + v;

			float headYawOffset = this.interpolateRotation(entityLiving.prevRenderYawOffset, entityLiving.renderYawOffset, v);
			float headYaw = this.interpolateRotation(entityLiving.prevRotationYawHead, entityLiving.rotationYawHead, v);

			float headPitch = entityLiving.prevRotationPitch + (entityLiving.rotationPitch - entityLiving.prevRotationPitch) * v;

			this.rotateCorpse(entityLiving, ageInTicks, headYawOffset, v);

			float limbSwingAmount = entityLiving.prevLimbSwingAmount + (entityLiving.limbSwingAmount - entityLiving.prevLimbSwingAmount) * v;
			float limbSwing = entityLiving.limbSwing - entityLiving.limbSwingAmount * (1.0F - v);

			Minecraft.getInstance().textureManager.bindTexture(getEntityTexture(entityLiving));
			this.model.render(entityLiving, limbSwing, limbSwingAmount, ageInTicks, headYaw - headYawOffset, headPitch, 0.0625F);
		}
		GL11.glPopMatrix();
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

	protected void rotateCorpse(EntityLivingBase entityLiving, float ageInTicks, float headYawOffset, float v) {
		GL11.glRotatef(180.0F + headYawOffset, 0.0F, 1.0F, 0.0F);

		if (entityLiving.deathTime > 0) {
			float f3 = ((float) entityLiving.deathTime + v - 1.0F) / 20.0F * 1.6F;
			f3 = MathHelper.sqrt(f3);

			if (f3 > 1.0F) {
				f3 = 1.0F;
			}
		}
	}

	@Override
	public void renderFirstPersonArm(EntityPlayer player) {
		Minecraft mc = Minecraft.getInstance();
		RenderHelper.enableStandardItemLighting();
		//Minecraft.getInstance().entityRenderer.enableLightmap();
		//GlStateManager.enableBlend();
		mc.getTextureManager().bindTexture(getEntityTexture(player));
		GL11.glTranslated(1, -1.0, -0.6);
		GL11.glRotated(-20, 1, 0, 0);
		GL11.glRotated(150, 0, 1, 0);
		GL11.glRotated(-50, 0, 0, 1);

		GL11.glColor4f(0.6F, 0.6F, 0.6F, 1.0F);

		if (mc.gameSettings.thirdPersonView == 0 && !mc.gameSettings.hideGUI && !player.isPlayerSleeping()) {
			model.swingProgress = 0.0F;
			model.isSneak = false;
			model.field_205061_a = 0.0F;
			model.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, player);
			model.rightArm.rotateAngleX = 0.0F;
			model.rightArm.render(0.0625F);
		}
		//GlStateManager.disableBlend();
		//Minecraft.getInstance().entityRenderer.disableLightmap();
		// RenderHelper.disableStandardItemLighting();
	}

	@Override
	public ResourceLocation getEntityTexture(EntityLivingBase entity) {
		return new ResourceLocation(Reference.MODID, "textures/models/" + ModCapabilities.get((EntityPlayer) entity).getModel() + ".png");
	}
}
