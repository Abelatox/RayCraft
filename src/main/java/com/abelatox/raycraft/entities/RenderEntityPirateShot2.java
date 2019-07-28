package com.abelatox.raycraft.entities;

import javax.annotation.Nullable;

import org.lwjgl.opengl.GL11;

import com.abelatox.raycraft.lib.Reference;
import com.abelatox.raycraft.models.ModelFist;
import com.abelatox.raycraft.models.ModelPirateShot;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
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

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityPirateShot2 entity) {
		return new ResourceLocation(Reference.MODID, "textures/models/pirateshot.png");
    }

    public static class Factory implements IRenderFactory<EntityPirateShot2> {
        @Override
        public EntityRenderer<? super EntityPirateShot2> createRenderFor(EntityRendererManager manager) {
            return new RenderEntityPirateShot2(manager, new ModelPirateShot());
        }
    }
}
