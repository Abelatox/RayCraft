package com.abelatox.raycraft.models;

import net.minecraft.client.renderer.entity.model.ModelBase;
import net.minecraft.client.renderer.entity.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelBarrel extends ModelBase{
	public ModelRenderer base;
	public ModelRenderer shape2;
	public ModelRenderer shape3;
	public ModelRenderer shape4;
	public ModelRenderer shape5;
	public ModelRenderer shape6;

	public ModelBarrel() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.shape3 = new ModelRenderer(this, 0, 0);
        this.shape3.setRotationPoint(4.0F, 0.0F, 0.0F);
        this.shape3.addBox(0.0F, 0.0F, 0.0F, 4, 1, 6, 0.0F);
        this.setRotateAngle(shape3, 0.0F, 0.0F, 1.0471975511965976F);
        this.shape5 = new ModelRenderer(this, 0, 0);
        this.shape5.setRotationPoint(4.0F, 0.0F, 0.0F);
        this.shape5.addBox(0.0F, 0.0F, 0.0F, 4, 1, 6, 0.0F);
        this.setRotateAngle(shape5, 0.0F, 0.0F, 1.0471975511965976F);
        this.base = new ModelRenderer(this, 0, 0);
        this.base.setRotationPoint(-2.0F, 0.0F, 0.0F);
        this.base.addBox(0.0F, 0.0F, 0.0F, 4, 1, 6, 0.0F);
        this.shape2 = new ModelRenderer(this, 0, 0);
        this.shape2.setRotationPoint(4.0F, 0.0F, 0.0F);
        this.shape2.addBox(0.0F, 0.0F, 0.0F, 4, 1, 6, 0.0F);
        this.setRotateAngle(shape2, 0.0F, 0.0F, 1.0471975511965976F);
        this.shape6 = new ModelRenderer(this, 0, 0);
        this.shape6.setRotationPoint(4.0F, 0.0F, 0.0F);
        this.shape6.addBox(0.0F, 0.0F, 0.0F, 4, 1, 6, 0.0F);
        this.setRotateAngle(shape6, 0.0F, 0.0F, 1.0471975511965976F);
        this.shape4 = new ModelRenderer(this, 0, 0);
        this.shape4.setRotationPoint(4.0F, 0.0F, 0.0F);
        this.shape4.addBox(0.0F, 0.0F, 0.0F, 4, 1, 6, 0.0F);
        this.setRotateAngle(shape4, 0.0F, 0.0F, 1.0471975511965976F);
        this.shape2.addChild(this.shape3);
        this.shape4.addChild(this.shape5);
        this.base.addChild(this.shape2);
        this.shape5.addChild(this.shape6);
        this.shape3.addChild(this.shape4);
    }

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		this.base.render(f5);
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