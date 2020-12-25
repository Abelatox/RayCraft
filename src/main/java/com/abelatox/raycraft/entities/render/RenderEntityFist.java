package com.abelatox.raycraft.entities.render;

import javax.annotation.Nullable;

import com.abelatox.raycraft.entities.EntityFist;
import com.abelatox.raycraft.lib.Reference;
import com.abelatox.raycraft.models.ModelFist;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.IRenderFactory;

@OnlyIn(Dist.CLIENT)
public class RenderEntityFist extends EntityRenderer<EntityFist> {

	public static final Factory FACTORY = new RenderEntityFist.Factory();
	ModelFist shot;
	int test;

	public RenderEntityFist(EntityRendererManager renderManager, ModelFist fist) {
		super(renderManager);
		this.shot = fist;
		this.shadowSize = 0.25F;
	}

	@Override
	public void render(EntityFist entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
		matrixStackIn.push();
		float r = 0, g = 0, b = 0;

		// Minecraft.getInstance().textureManager.bindTexture(getEntityTexture(entity));
		// GL11.glTranslated(x, y, z);
		// GL11.glRotatef(entity.prevRotationYaw + (entity.rotationYaw -
		// entity.prevRotationYaw) * partialTicks - 90.0F, 0.0F, 1.0F, 0.0F);
		
		// GL11.glRotatef(entity.prevRotationPitch + (entity.rotationPitch -
		// entity.prevRotationPitch) * partialTicks, 0.0F, 0.0F, 1.0F);

		switch (entity.getLvl()) {
		case 0:
			r = 1;
			g = 1;
			b = 1;
			break;
		case 1:
			r = 0;
			g = 0.9F;
			b = 1;
			break;
		case 2:
			r = 1;
			g = 0.7F;
			b = 0;
			break;
		case 3:
			r = 1;
			g = 0.9F;
			b = 0;
			break;
		case 4:
			r = 1;
			g = 0.9F;
			b = 0;
			matrixStackIn.scale(4, 4, 4);
			break;
		}
		
		matrixStackIn.rotate(Vector3f.YP.rotationDegrees(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw)));
		matrixStackIn.rotate(Vector3f.XN.rotationDegrees(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch)));


		if (entity.ticksExisted > 1) //Prevent entity rendering in your face
			shot.render(matrixStackIn, bufferIn.getBuffer(shot.getRenderType(getEntityTexture(entity))), packedLightIn, OverlayTexture.NO_OVERLAY, r, g, b, 1F);
		// GL11.glColor4ub((byte) 255, (byte) 255, (byte) 255, (byte) 255);

		matrixStackIn.pop();
		super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	@Nullable
	@Override
	public ResourceLocation getEntityTexture(EntityFist entity) {
		return new ResourceLocation(Reference.MODID, "textures/models/fist.png");
	}

	public static class Factory implements IRenderFactory<EntityFist> {
		@Override
		public EntityRenderer<? super EntityFist> createRenderFor(EntityRendererManager manager) {
			return new RenderEntityFist(manager, new ModelFist());
		}
	}
}
