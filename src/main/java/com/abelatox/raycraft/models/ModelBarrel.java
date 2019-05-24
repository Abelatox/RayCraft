package com.abelatox.raycraft.models;

import net.minecraft.client.renderer.entity.model.ModelBase;
import net.minecraft.client.renderer.entity.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelBarrel extends ModelBase{
	public ModelRenderer shape1;
    public ModelRenderer shape2;
    public ModelRenderer lid1;
    public ModelRenderer lid2;
    public ModelRenderer shape3;
    public ModelRenderer shape4;
    public ModelRenderer shape5;
    public ModelRenderer shape6;
    public ModelRenderer lid12;
    public ModelRenderer lid13;
    public ModelRenderer lid21;
    public ModelRenderer lid22;

	public ModelBarrel() {
		this.textureWidth = 64;
        this.textureHeight = 64;
        this.lid21 = new ModelRenderer(this, 0, 0);
        this.lid21.setRotationPoint(-0.6F, 1.5F, 0.0F);
        this.lid21.addBox(0.0F, 0.0F, -0.1F, 2, 2, 1, 0.0F);
        this.lid2 = new ModelRenderer(this, 30, 0);
        this.lid2.setRotationPoint(-2.5F, 1.0F, 8.2F);
        this.lid2.addBox(0.0F, 0.0F, 0.0F, 5, 5, 1, 0.0F);
        this.shape3 = new ModelRenderer(this, 0, 0);
        this.shape3.setRotationPoint(4.0F, 0.0F, 0.0F);
        this.shape3.addBox(0.0F, 0.0F, 0.0F, 4, 1, 9, 0.0F);
        this.setRotateAngle(shape3, 0.0F, 0.0F, 1.0471975511965976F);
        this.shape1 = new ModelRenderer(this, 0, 0);
        this.shape1.setRotationPoint(0.0F, 0.0F, -4.5F);
        this.shape1.addBox(-2.0F, 0.0F, 0.0F, 4, 1, 9, 0.0F);
        this.lid1 = new ModelRenderer(this, 30, 0);
        this.lid1.setRotationPoint(-2.5F, 1.0F, -0.2F);
        this.lid1.addBox(0.0F, 0.0F, 0.0F, 5, 5, 1, 0.0F);
        this.lid13 = new ModelRenderer(this, 0, 0);
        this.lid13.setRotationPoint(3.7F, 1.4F, 0.0F);
        this.lid13.addBox(0.0F, 0.0F, 0.1F, 2, 2, 1, 0.0F);
        this.shape4 = new ModelRenderer(this, 0, 0);
        this.shape4.setRotationPoint(4.0F, 0.0F, 0.0F);
        this.shape4.addBox(0.0F, 0.0F, 0.0F, 4, 1, 9, 0.0F);
        this.setRotateAngle(shape4, 0.0F, 0.0F, 1.0471975511965976F);
        this.shape2 = new ModelRenderer(this, 0, 0);
        this.shape2.setRotationPoint(2.0F, 0.0F, 0.0F);
        this.shape2.addBox(0.0F, 0.0F, 0.0F, 4, 1, 9, 0.0F);
        this.setRotateAngle(shape2, 0.0F, 0.0F, 1.0471975511965976F);
        this.shape6 = new ModelRenderer(this, 0, 0);
        this.shape6.setRotationPoint(4.0F, 0.0F, 0.0F);
        this.shape6.addBox(0.0F, 0.0F, 0.0F, 4, 1, 9, 0.0F);
        this.setRotateAngle(shape6, 0.0F, 0.0F, 1.0471975511965976F);
        this.lid12 = new ModelRenderer(this, 0, 0);
        this.lid12.setRotationPoint(-0.6F, 1.5F, 0.0F);
        this.lid12.addBox(0.0F, 0.0F, 0.1F, 2, 2, 1, 0.0F);
        this.shape5 = new ModelRenderer(this, 0, 0);
        this.shape5.setRotationPoint(4.0F, 0.0F, 0.0F);
        this.shape5.addBox(0.0F, 0.0F, 0.0F, 4, 1, 9, 0.0F);
        this.setRotateAngle(shape5, 0.0F, 0.0F, 1.0471975511965976F);
        this.lid22 = new ModelRenderer(this, 0, 0);
        this.lid22.setRotationPoint(3.7F, 1.4F, 0.0F);
        this.lid22.addBox(0.0F, 0.0F, -0.1F, 2, 2, 1, 0.0F);
        this.lid2.addChild(this.lid21);
        this.shape1.addChild(this.lid2);
        this.shape2.addChild(this.shape3);
        this.shape1.addChild(this.lid1);
        this.lid1.addChild(this.lid13);
        this.shape3.addChild(this.shape4);
        this.shape1.addChild(this.shape2);
        this.shape5.addChild(this.shape6);
        this.lid1.addChild(this.lid12);
        this.shape4.addChild(this.shape5);
        this.lid2.addChild(this.lid22);
    }

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		//System.out.println(f5);
        this.shape1.render(f5);
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