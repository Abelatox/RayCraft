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

public class ModelPirateShot extends Model<EntityRenderState> {

	public ModelPirateShot(ModelPart root) {
		super(root, RenderTypes::entityCutout);
	}

	@Override
	public void setupAnim(EntityRenderState renderState) {
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition baseCenter = root.addOrReplaceChild("baseCenter",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -1.0F, -9.0F, 2, 2, 8),
				PartPose.offset(0.0F, 0.0F, 4.0F));

		PartDefinition baseRing = baseCenter.addOrReplaceChild("baseRing",
				CubeListBuilder.create().texOffs(0, 10).addBox(0.0F, 0.0F, 0.0F, 4, 4, 0),
				PartPose.offset(-2.0F, -2.0F, -2.0F));

		PartDefinition ring1Ring = baseRing.addOrReplaceChild("ring1Ring",
				CubeListBuilder.create().texOffs(0, 14).addBox(-1.5F, -1.5F, -1.0F, 5, 5, 0),
				PartPose.offset(1.0F, 1.0F, -1.0F));

		PartDefinition ring2Ring = ring1Ring.addOrReplaceChild("ring2Ring",
				CubeListBuilder.create().texOffs(0, 19).addBox(-2.0F, -2.0F, -1.0F, 6, 6, 0),
				PartPose.offset(0.0F, 0.0F, -2.0F));

		ring2Ring.addOrReplaceChild("ring3Ring",
				CubeListBuilder.create().texOffs(0, 25).addBox(-2.5F, -2.5F, -1.0F, 7, 7, 0),
				PartPose.offset(0.0F, 0.0F, -2.0F));

		return LayerDefinition.create(mesh, 32, 32);
	}
}
