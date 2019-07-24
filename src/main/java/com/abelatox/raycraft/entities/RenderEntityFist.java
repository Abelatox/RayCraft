package com.abelatox.raycraft.entities;

import javax.annotation.Nullable;

import org.lwjgl.opengl.GL11;

import com.abelatox.raycraft.lib.Reference;
import com.abelatox.raycraft.models.ModelFist;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.IRenderFactory;

@OnlyIn(Dist.CLIENT)
public class RenderEntityFist extends EntityRenderer<EntityBaseFist> {

    public static final Factory FACTORY = new RenderEntityFist.Factory();
    ModelFist shot;
    int[] color;
    int test;
    
    public RenderEntityFist(EntityRendererManager renderManager, ModelFist fist, int[] color) {
        super(renderManager);
        this.shot = fist;
        this.shadowSize = 0.25F;
        this.color = color;
    }

    @Override
    public void doRender(EntityBaseFist entity, double x, double y, double z, float entityYaw, float partialTicks) {
    	GL11.glPushMatrix();
		{
			Minecraft.getInstance().textureManager.bindTexture(getEntityTexture(entity));
			GL11.glTranslated(x, y, z);
			GL11.glRotatef(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks - 90.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, 0.0F, 0.0F, 1.0F);

			if(entity instanceof EntityFist0) {
				//System.out.println("0- white");
				GL11.glColor4ub((byte)255, (byte)255, (byte)255, (byte)255);
			}
			if(entity instanceof EntityFist1) {
				//System.out.println("1- blue");
				GL11.glColor4ub((byte)0, (byte)250, (byte)255, (byte)255);
			}
			if(entity instanceof EntityFist2) {
				//System.out.println("2- yellow");
				GL11.glColor4ub((byte)255, (byte)180, (byte)0, (byte)255);
			}
			if(entity instanceof EntityFist3) {
				//System.out.println("3- Golden");
				GL11.glColor4ub((byte)255, (byte)240, (byte)0, (byte)255);
			}
			if(entity instanceof EntityFist4) {
				//System.out.println("4- Charged Golden");
				GL11.glColor4ub((byte)255, (byte)240, (byte)0, (byte)255);
				GL11.glScaled(4, 4, 4);
			}
			this.shot.render(entity, 0, 0, 0, 0, 0, 0.0625F);
			GL11.glColor4ub((byte)255, (byte)255, (byte)255, (byte)255);

		}
		GL11.glPopMatrix();
       // super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityBaseFist entity) {
		return new ResourceLocation(Reference.MODID, "textures/models/fist.png");
    }

    public static class Factory implements IRenderFactory<EntityBaseFist> {
        @Override
        public EntityRenderer<? super EntityBaseFist> createRenderFor(EntityRendererManager manager) {
            return new RenderEntityFist(manager, new ModelFist(), new int[] {200,230,255});
        }
    }
}
