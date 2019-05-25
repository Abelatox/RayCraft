package com.abelatox.raycraft.models;

import org.lwjgl.opengl.GL11;

import com.abelatox.raycraft.entities.EntityFist0;

import net.minecraft.client.renderer.entity.model.ModelBase;
import net.minecraft.client.renderer.entity.model.ModelRenderer;
import net.minecraft.entity.Entity;


public class ModelFist extends ModelBase {
	public ModelRenderer fist;
	
	public ModelFist() {
		this.textureWidth = 32;
		this.textureHeight = 32;
		this.fist = new ModelRenderer(this, 0, 0);
		this.fist.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.fist.addBox(-2.0F, -2.0F, -2.0F, 4, 4, 4, 0.0F);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		GL11.glPushMatrix();
		{
			//RenderHelper.enableStandardItemLighting();
			//Minecraft.getInstance().entityRenderer.enableLightmap();
			//GlStateManager.enableBlend();

			

			GL11.glTranslated(0, 0.1,0);
			this.fist.render(f5);
			//GlStateManager.disableBlend();
			//Minecraft.getInstance().entityRenderer.disableLightmap();
			//RenderHelper.disableStandardItemLighting();

		}
		GL11.glPopMatrix();
		
	}

	/**
	 * This is a helper function from Tabula to set the rotation of model parts
	 */
	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}
