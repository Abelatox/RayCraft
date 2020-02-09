package com.abelatox.raycraft.entities.render;

import javax.annotation.Nullable;

import org.lwjgl.opengl.GL11;

import com.abelatox.raycraft.entities.EntityBarrel;
import com.abelatox.raycraft.entities.EntityFist;
import com.abelatox.raycraft.entities.EntityPirateShot;
import com.abelatox.raycraft.lib.Reference;
import com.abelatox.raycraft.models.ModelBarrel;
import com.abelatox.raycraft.models.ModelFist;
import com.abelatox.raycraft.models.ModelPirateShot;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.IRenderFactory;

@OnlyIn(Dist.CLIENT)
public class RenderEntityPirateShot extends EntityRenderer<EntityPirateShot> {

    public static final Factory FACTORY = new RenderEntityPirateShot.Factory();
    ModelFist shot;
    int test;
    
    public RenderEntityPirateShot(EntityRendererManager renderManager, ModelFist fist) {
        super(renderManager);
        this.shot = fist;
        this.shadowSize = 0.25F;
    }
    

	@Override
	public void render(EntityPirateShot entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
		matrixStackIn.push();

		matrixStackIn.scale(1.5F, 1.5F, 1.5F);
		matrixStackIn.rotate(Vector3f.YP.rotationDegrees(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw)));
		matrixStackIn.rotate(Vector3f.XN.rotationDegrees(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch)));
		
		if (entity.ticksExisted > 2) //Prevent entity rendering in your face
			shot.render(matrixStackIn, bufferIn.getBuffer(shot.getRenderType(getEntityTexture(entity))), packedLightIn, OverlayTexture.DEFAULT_LIGHT, 1F, 0.1F, 0.1F, 1F);
		matrixStackIn.pop();
		super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	@Nullable
	@Override
	public ResourceLocation getEntityTexture(EntityPirateShot entity) {
		return new ResourceLocation(Reference.MODID, "textures/models/fist.png");
	}

	public static class Factory implements IRenderFactory<EntityPirateShot> {
		@Override
		public EntityRenderer<? super EntityPirateShot> createRenderFor(EntityRendererManager manager) {
			// TODO Auto-generated method stub
			return new RenderEntityPirateShot(manager, new ModelFist());
		}
	}
}

/*
    @Override
    public void doRender(EntityPirateShot entity, double x, double y, double z, float entityYaw, float partialTicks) {
    	GL11.glPushMatrix();
		{
			Minecraft.getInstance().textureManager.bindTexture(getEntityTexture(entity));
			GL11.glTranslated(x, y, z);
			GL11.glRotatef(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks - 90.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, 0.0F, 0.0F, 1.0F);

			if(entity instanceof EntityPirateShot) {
				//System.out.println("0- white");
				GL11.glColor4ub((byte)255, (byte)30, (byte)30, (byte)255);
			}
			
			this.shot.render(entity, 0, 0, 0, 0, 0, 0.0625F);
			GL11.glColor4ub((byte)255, (byte)255, (byte)255, (byte)255);

		}
		GL11.glPopMatrix();
       // super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityPirateShot entity) {
		return new ResourceLocation(Reference.MODID, "textures/models/fist.png");
    }

    public static class Factory implements IRenderFactory<EntityPirateShot> {
        @Override
        public EntityRenderer<? super EntityPirateShot> createRenderFor(EntityRendererManager manager) {
            return new RenderEntityPirateShot(manager, new ModelFist(), new int[] {200,230,255});
        }
    }
}
*/