package com.abelatox.raycraft.models;

import com.abelatox.raycraft.entities.EntityFist;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class ModelFist extends EntityModel<EntityFist> {
	public ModelRenderer fist;

	public ModelFist() {
		this.textureWidth = 32;
		this.textureHeight = 32;
		this.fist = new ModelRenderer(this, 0, 0);
		this.fist.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.fist.addBox(-2.0F, -2.0F, -2.0F, 4, 4, 4, 0.0F);
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
	public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		matrixStackIn.push();
		
		matrixStackIn.translate(0, 0.1, 0);
		//matrixStackIn.rotate(Vector3f.YP.rotationDegrees());
		//rotate(new Vector3f(0,0.707,0.707).rotationDegrees(15)) 
		fist.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn,red,green,blue,alpha);
		matrixStackIn.pop();
	}

	@Override
	public void setRotationAngles(EntityFist arg0, float arg1, float arg2, float arg3, float arg4, float arg5) {
		// TODO Auto-generated method stub
		
	}

}