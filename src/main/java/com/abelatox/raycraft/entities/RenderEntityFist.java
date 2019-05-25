package com.abelatox.raycraft.entities;

import javax.annotation.Nullable;

import org.lwjgl.opengl.GL11;

import com.abelatox.raycraft.lib.Reference;
import com.abelatox.raycraft.models.ModelFist;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.IRenderFactory;

@OnlyIn(Dist.CLIENT)
public class RenderEntityFist extends Render<EntityFist> {

    public static final Factory FACTORY = new RenderEntityFist.Factory();
    ModelFist shot;
    int[] color;
    int test;
    
    public RenderEntityFist(RenderManager renderManager, ModelFist fist, int[] color) {
        super(renderManager);
        this.shot = fist;
        this.shadowSize = 0.25F;
        this.color = color;
    }

    @Override
    public void doRender(EntityFist entity, double x, double y, double z, float entityYaw, float partialTicks) {
    	GL11.glPushMatrix();
		{
			Minecraft.getInstance().textureManager.bindTexture(getEntityTexture(entity));
			GL11.glTranslated(x, y, z);
			GL11.glColor3ub((byte)color[0], (byte)color[1], (byte)color[2]);
			//GL11.glColor3d(1,1,1);
			GL11.glRotatef(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks - 90.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, 0.0F, 0.0F, 1.0F);
			this.shot.render(entity, 0, 0, 0, 0, 0, 0.0625F);
		}
		GL11.glPopMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityFist entity) {
		return new ResourceLocation(Reference.MODID, "textures/models/fist.png");
    }

    public static class Factory implements IRenderFactory<EntityFist> {
        @Override
        public Render<? super EntityFist> createRenderFor(RenderManager manager) {
            return new RenderEntityFist(manager, new ModelFist(), new int[] {200,230,255});
        }
    }
}
