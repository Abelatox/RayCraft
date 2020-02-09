package com.abelatox.raycraft.models;

import com.abelatox.raycraft.entities.EntityBarrel;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class ModelBarrel extends EntityModel<EntityBarrel>{
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

	/**
	 * This is a helper function from Tabula to set the rotation of model parts
	 */
	public void setRotateAngle(ModelRenderer ModelRenderer, float x, float y, float z) {
		ModelRenderer.rotateAngleX = x;
		ModelRenderer.rotateAngleY = y;
		ModelRenderer.rotateAngleZ = z;
	}

	@Override
	public void render(EntityBarrel arg0, float arg1, float arg2, float arg3, float arg4, float arg5) {
		
	}

	@Override
	public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		matrixStackIn.push();
		
		matrixStackIn.translate(0, 0.1, 0);
		//matrixStackIn.rotate(Vector3f.YP.rotationDegrees());
		//rotate(new Vector3f(0,0.707,0.707).rotationDegrees(15)) 
		shape1.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn,red,green,blue,alpha);
		matrixStackIn.pop();
	}
}