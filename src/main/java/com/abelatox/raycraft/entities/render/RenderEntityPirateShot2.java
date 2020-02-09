package com.abelatox.raycraft.entities.render;

import javax.annotation.Nullable;

import org.lwjgl.opengl.GL11;

import com.abelatox.raycraft.entities.EntityFist;
import com.abelatox.raycraft.entities.EntityPirateShot;
import com.abelatox.raycraft.entities.EntityPirateShot2;
import com.abelatox.raycraft.lib.Reference;
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
public class RenderEntityPirateShot2 extends EntityRenderer<EntityPirateShot2> {

    public static final Factory FACTORY = new RenderEntityPirateShot2.Factory();
    ModelPirateShot shot;
    
    public RenderEntityPirateShot2(EntityRendererManager renderManager, ModelPirateShot fist) {
        super(renderManager);
        this.shadowSize = 0.25F;
        this.shot = fist;
    }
    
    @Override
	public void render(EntityPirateShot2 entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
		matrixStackIn.push();

		matrixStackIn.rotate(Vector3f.YP.rotationDegrees(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw)));
		matrixStackIn.rotate(Vector3f.XN.rotationDegrees(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch)));
		matrixStackIn.scale(2,2,2);
		
		if (entity.ticksExisted > 2) //Prevent entity rendering in your face
		shot.render(matrixStackIn, bufferIn.getBuffer(shot.getRenderType(getEntityTexture(entity))), packedLightIn, OverlayTexture.DEFAULT_LIGHT, 1F, 1F, 1F, 1F);
		
		matrixStackIn.pop();
		super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	@Nullable
	@Override
	public ResourceLocation getEntityTexture(EntityPirateShot2 entity) {
		return new ResourceLocation(Reference.MODID, "textures/models/pirateshot.png");
	}

	public static class Factory implements IRenderFactory<EntityPirateShot2> {
		@Override
		public EntityRenderer<? super EntityPirateShot2> createRenderFor(EntityRendererManager manager) {
			// TODO Auto-generated method stub
			return new RenderEntityPirateShot2(manager, new ModelPirateShot());
		}
	}
}

    /*@Override
    public void doRender(EntityPirateShot2 entity, double x, double y, double z, float entityYaw, float partialTicks) {
    	GL11.glPushMatrix();
		{
			Minecraft.getInstance().textureManager.bindTexture(getEntityTexture(entity));
			GL11.glTranslated(x, y, z);
			GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks - 90.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, 1.0F, 0.0F, 0.0F);
			GL11.glScaled(2,2,2);
			
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.8F);

			this.shot.render(entity, 0, 0, 0, 0, 0, 0.0625F);

			GL11.glDisable(GL11.GL_BLEND);

		}
		GL11.glPopMatrix();
       // super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
}*/
