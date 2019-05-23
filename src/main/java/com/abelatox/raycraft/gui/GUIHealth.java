package com.abelatox.raycraft.gui;

import org.lwjgl.opengl.GL11;

import com.abelatox.raycraft.lib.Reference;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class GUIHealth extends GuiScreen{

    @SubscribeEvent
    public void onRenderOverlayPost(RenderGameOverlayEvent event) {
        Minecraft mc = Minecraft.getInstance();
        EntityPlayer player = mc.player;

        if (event.getType().equals(RenderGameOverlayEvent.ElementType.HEALTH) && event.isCancelable()) {
           event.setCanceled(true);
        }
        if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {

            int screenWidth = mc.mainWindow.getScaledWidth();
            int screenHeight = mc.mainWindow.getScaledHeight();
            
            GL11.glPushMatrix();
            {	

            	GL11.glTranslated(10, 10, 0);
            	GL11.glScaled(1.2, 1.2, 1.2);

            	GL11.glPushMatrix();
                {// Face
                    mc.textureManager.bindTexture(new ResourceLocation(Reference.MODID, "textures/gui/face_normal.png"));

                	GL11.glScaled(0.2, 0.18, 0.2);
                	drawTexturedModalRect(0, 0, 0, 0, 111, 147);
                }
                GL11.glPopMatrix();
                
                mc.textureManager.bindTexture(new ResourceLocation(Reference.MODID, "textures/gui/hp.png"));

            	GL11.glTranslated(30, 10, 0);
                GL11.glPushMatrix();
                {// HP Background
                	GL11.glColor4d(1, 1, 1, 1);
                	
                	for(int i=0;i<player.getMaxHealth()*2;i++)
                		drawTexturedModalRect(i, 0, 0, 0, 1, 6);
                }
                GL11.glPopMatrix();
                
                GL11.glPushMatrix();
                {// HP Bar
                	GL11.glColor4d(1, 1, 1, 1);
                	
                	for(int i=0;i<player.getHealth()*2;i++)
                		drawTexturedModalRect(i, 0, 1, 0, 1, 6);
                }
                GL11.glPopMatrix();
                
            	
            }
            GL11.glPopMatrix();
        }
    }
}
