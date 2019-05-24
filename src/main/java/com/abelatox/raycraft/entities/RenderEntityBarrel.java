package com.abelatox.raycraft.entities;

import javax.annotation.Nullable;

import org.lwjgl.opengl.GL11;

import com.abelatox.raycraft.lib.Reference;
import com.abelatox.raycraft.models.ModelBarrel;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.IRenderFactory;

/**
 * Mostly a copy of {@link net.minecraft.client.renderer.entity.RenderTNTPrimed} with some small changes
 */
@OnlyIn(Dist.CLIENT)
public class RenderEntityBarrel extends Render<EntityBarrel> {

    public static final Factory FACTORY = new RenderEntityBarrel.Factory();
    ModelBarrel barrel;
    
    public RenderEntityBarrel(RenderManager renderManager, ModelBarrel barrel) {
        super(renderManager);
        this.barrel = barrel;
        this.shadowSize = 0.5F;
    }

    @Override
    public void doRender(EntityBarrel entity, double x, double y, double z, float entityYaw, float partialTicks) {
    	GL11.glPushMatrix();
		{
			Minecraft.getInstance().textureManager.bindTexture(getEntityTexture(entity));
			GL11.glTranslated(x, y, z);
			GL11.glScaled(1.5, 1.5, 1.5);
			GL11.glRotatef(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks - 90.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, 0.0F, 0.0F, 1.0F);
	        GL11.glRotatef(90, 0,1,0);
			this.barrel.render(entity, 0, 0, 0, 0 - 0, 0, 0.0625F);
		}
		GL11.glPopMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityBarrel entity) {
		return new ResourceLocation(Reference.MODID, "textures/models/barrel.png");
    }

    public static class Factory implements IRenderFactory<EntityBarrel> {
        @Override
        public Render<? super EntityBarrel> createRenderFor(RenderManager manager) {
            return new RenderEntityBarrel(manager, new ModelBarrel());
        }
    }
}
