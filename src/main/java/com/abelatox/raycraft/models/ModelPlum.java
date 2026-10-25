package com.abelatox.raycraft.models;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.util.Mth;

public class ModelPlum extends Model<EntityRenderState> {

	public static final float CENTER_Y = 19.0F;
	private static final int LEAVES = 6;

	public ModelPlum(ModelPart root) {
		super(root, RenderTypes::entityCutout);
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();
		root.addOrReplaceChild("ball", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -4.0F, -4.0F, 10, 8, 8).texOffs(0, 0).addBox(-4.0F, -5.0F, -4.0F, 8, 10, 8).texOffs(0, 0).addBox(-4.0F, -4.0F, -5.0F, 8, 8, 10), PartPose.offset(0.0F, CENTER_Y, 0.0F));
		root.addOrReplaceChild("stem", CubeListBuilder.create().texOffs(0, 40).addBox(-0.5F, -7.0F, -0.5F, 1, 2, 1), PartPose.offset(0.0F, CENTER_Y, 0.0F));
		for (int i = 0; i < LEAVES; i++) {
			root.addOrReplaceChild("leaf_" + i, CubeListBuilder.create().texOffs(8, 40).addBox(0.5F, -7.0F, -1.0F, 3, 0, 2), PartPose.offsetAndRotation(0.0F, CENTER_Y, 0.0F, 0.0F, i * Mth.TWO_PI / LEAVES, 0.0F));
		}
		return LayerDefinition.create(mesh, 64, 64);
	}
}
