package com.abelatox.raycraft.entities.render;

import javax.annotation.Nullable;

import com.abelatox.raycraft.entities.EntityBarrel;
import com.abelatox.raycraft.lib.Reference;
import com.abelatox.raycraft.models.ModelBarrel;
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

/**
 * Mostly a copy of {@link net.minecraft.client.renderer.entity.RenderTNTPrimed}
 * with some small changes
 */
@OnlyIn(Dist.CLIENT)
public class RenderEntityBarrel extends EntityRenderer<EntityBarrel> {

	public static final Factory FACTORY = new RenderEntityBarrel.Factory();
	ModelBarrel barrel;

	public RenderEntityBarrel(EntityRendererManager renderManager, ModelBarrel barrel) {
		super(renderManager);
		this.barrel = barrel;
		this.shadowSize = 0.5F;
	}

	@Override
	public void render(EntityBarrel entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
		matrixStackIn.push();

		matrixStackIn.scale(1.5F, 1.5F, 1.5F);
		matrixStackIn.rotate(Vector3f.YP.rotationDegrees(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw)));
		matrixStackIn.rotate(Vector3f.XN.rotationDegrees(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch)));
		
		barrel.render(matrixStackIn, bufferIn.getBuffer(barrel.getRenderType(getEntityTexture(entity))), packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
		matrixStackIn.pop();
		super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	@Nullable
	@Override
	public ResourceLocation getEntityTexture(EntityBarrel entity) {
		return new ResourceLocation(Reference.MODID, "textures/models/barrel.png");
	}

	public static class Factory implements IRenderFactory<EntityBarrel> {
		@Override
		public EntityRenderer<? super EntityBarrel> createRenderFor(EntityRendererManager manager) {
			// TODO Auto-generated method stub
			return new RenderEntityBarrel(manager, new ModelBarrel());
		}
	}
}
