package com.abelatox.raycraft.models;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.entity.Entity;

/**
 * ModelFist - Abelatox
 * Created using Tabula 7.0.1
 */
public class ModelPirateShot extends EntityModel {
    public RendererModel baseCenter;
    public RendererModel baseRing;
    public RendererModel ring1Ring;
    public RendererModel ring2Ring;
    public RendererModel ring3Ring;

    public ModelPirateShot() {
        this.textureWidth = 32;
        this.textureHeight = 32;
        this.baseCenter = new RendererModel(this, 0, 0);
        this.baseCenter.setRotationPoint(0.0F, 0.0F, 4.0F);
        this.baseCenter.addBox(-1.0F, -1.0F, -9.0F, 2, 2, 8, 0.0F);
        this.ring1Ring = new RendererModel(this, 0, 14);
        this.ring1Ring.setRotationPoint(1.0F, 1.0F, -1.0F);
        this.ring1Ring.addBox(-1.5F, -1.5F, -1.0F, 5, 5, 0, 0.0F);
        this.ring2Ring = new RendererModel(this, 0, 19);
        this.ring2Ring.setRotationPoint(0.0F, 0.0F, -2.0F);
        this.ring2Ring.addBox(-2.0F, -2.0F, -1.0F, 6, 6, 0, 0.0F);
        this.baseRing = new RendererModel(this, 0, 10);
        this.baseRing.setRotationPoint(-2.0F, -2.0F, -2.0F);
        this.baseRing.addBox(0.0F, 0.0F, 0.0F, 4, 4, 0, 0.0F);
        this.ring3Ring = new RendererModel(this, 0, 25);
        this.ring3Ring.setRotationPoint(0.0F, 0.0F, -2.0F);
        this.ring3Ring.addBox(-2.5F, -2.5F, -1.0F, 7, 7, 0, 0.0F);
        this.baseRing.addChild(this.ring1Ring);
        this.ring1Ring.addChild(this.ring2Ring);
        this.baseCenter.addChild(this.baseRing);
        this.ring2Ring.addChild(this.ring3Ring);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.baseCenter.render(f5);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(RendererModel modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
