package com.abelatox.raycraft.models;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.vector.Vector3f;

/**
 * ModelFist - Abelatox
 * Created using Tabula 7.0.1
 */
public class ModelPirateShot extends EntityModel {
    public ModelRenderer baseCenter;
    public ModelRenderer baseRing;
    public ModelRenderer ring1Ring;
    public ModelRenderer ring2Ring;
    public ModelRenderer ring3Ring;

    public ModelPirateShot() {
        this.textureWidth = 32;
        this.textureHeight = 32;
        this.baseCenter = new ModelRenderer(this, 0, 0);
        this.baseCenter.setRotationPoint(0.0F, 0.0F, 4.0F);
        this.baseCenter.addBox(-1.0F, -1.0F, -9.0F, 2, 2, 8, 0.0F);
        this.ring1Ring = new ModelRenderer(this, 0, 14);
        this.ring1Ring.setRotationPoint(1.0F, 1.0F, -1.0F);
        this.ring1Ring.addBox(-1.5F, -1.5F, -1.0F, 5, 5, 0, 0.0F);
        this.ring2Ring = new ModelRenderer(this, 0, 19);
        this.ring2Ring.setRotationPoint(0.0F, 0.0F, -2.0F);
        this.ring2Ring.addBox(-2.0F, -2.0F, -1.0F, 6, 6, 0, 0.0F);
        this.baseRing = new ModelRenderer(this, 0, 10);
        this.baseRing.setRotationPoint(-2.0F, -2.0F, -2.0F);
        this.baseRing.addBox(0.0F, 0.0F, 0.0F, 4, 4, 0, 0.0F);
        this.ring3Ring = new ModelRenderer(this, 0, 25);
        this.ring3Ring.setRotationPoint(0.0F, 0.0F, -2.0F);
        this.ring3Ring.addBox(-2.5F, -2.5F, -1.0F, 7, 7, 0, 0.0F);
        this.baseRing.addChild(this.ring1Ring);
        this.ring1Ring.addChild(this.ring2Ring);
        this.baseCenter.addChild(this.baseRing);
        this.ring2Ring.addChild(this.ring3Ring);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

	@Override
	public void setRotationAngles(Entity arg0, float arg1, float arg2, float arg3, float arg4, float arg5) {
      //  this.baseCenter.render(f5);
		
	}

	@Override
	public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		matrixStackIn.push();
		
		matrixStackIn.translate(0, 0.05, 0);
		matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180));
		baseCenter.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		matrixStackIn.pop();
	}
}
