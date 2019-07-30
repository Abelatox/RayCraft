package com.abelatox.raycraft.entities;

import javax.annotation.Nullable;

import org.lwjgl.opengl.GL11;

import com.abelatox.raycraft.lib.Reference;
import com.abelatox.raycraft.models.ModelBarrel;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.IRenderFactory;

@OnlyIn(Dist.CLIENT)
public class RenderEntityShell extends EntityRenderer<EntityRunningShell> {

    public static final Factory FACTORY = new RenderEntityShell.Factory();
    EntityModel model;
    
    public RenderEntityShell(EntityRendererManager renderManager, EntityModel model) {
        super(renderManager);
        this.shadowSize = 0.25F;
        this.model = model;
    }

    @Override
    public void doRender(EntityRunningShell entity, double x, double y, double z, float entityYaw, float partialTicks) {
    	GL11.glPushMatrix();
		{
			Minecraft.getInstance().textureManager.bindTexture(getEntityTexture(entity));
			GL11.glTranslated(x, y, z);
			GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
			//GL11.glRotatef(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks - 90.0F, 0.0F, 1.0F, 0.0F);
			//GL11.glRotatef(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, 1.0F, 0.0F, 0.0F);
			GL11.glScaled(2,2,2);
			System.out.println(entity.rotationYaw);
			GL11.glRotated(-entity.rotationYaw+90, 0, 1, 0);

		//	GL11.glEnable(GL11.GL_BLEND);
			//GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			//GL11.glColor4f(1.0F, 1.0F, 0.0F, 0.8F);

			this.model.render(entity, 0, 0, 0, 0, 0, 0.0625F);

			//GL11.glDisable(GL11.GL_BLEND);

		}
		GL11.glPopMatrix();
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityRunningShell entity) {
		return new ResourceLocation(Reference.MODID, "textures/models/barrel.png");
    }

    public static class Factory implements IRenderFactory<EntityRunningShell> {
        @Override
        public EntityRenderer<? super EntityRunningShell> createRenderFor(EntityRendererManager manager) {
            return new RenderEntityShell(manager, new ModelBarrel());
        }
    }
}
