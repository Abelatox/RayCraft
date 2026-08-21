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

public class ModelBarrel extends Model<EntityRenderState> {

	public ModelBarrel(ModelPart root) {
		super(root, RenderTypes::entityCutout);
	}

	@Override
	public void setupAnim(EntityRenderState renderState) {
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition shape1 = root.addOrReplaceChild("shape1",
				CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, 0.0F, 0.0F, 4, 1, 9),
				PartPose.offset(0.0F, 0.0F, -4.5F));

		PartDefinition shape2 = shape1.addOrReplaceChild("shape2",
				CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 4, 1, 9),
				PartPose.offsetAndRotation(2.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0471976F));

		PartDefinition shape3 = shape2.addOrReplaceChild("shape3",
				CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 4, 1, 9),
				PartPose.offsetAndRotation(4.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0471976F));

		PartDefinition shape4 = shape3.addOrReplaceChild("shape4",
				CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 4, 1, 9),
				PartPose.offsetAndRotation(4.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0471976F));

		PartDefinition shape5 = shape4.addOrReplaceChild("shape5",
				CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 4, 1, 9),
				PartPose.offsetAndRotation(4.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0471976F));

		shape5.addOrReplaceChild("shape6",
				CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 4, 1, 9),
				PartPose.offsetAndRotation(4.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0471976F));

		PartDefinition lid1 = shape1.addOrReplaceChild("lid1",
				CubeListBuilder.create().texOffs(30, 0).addBox(0.0F, 0.0F, 0.0F, 5, 5, 1),
				PartPose.offset(-2.5F, 1.0F, -0.2F));

		lid1.addOrReplaceChild("lid12",
				CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.1F, 2, 2, 1),
				PartPose.offset(-0.6F, 1.5F, 0.0F));

		lid1.addOrReplaceChild("lid13",
				CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.1F, 2, 2, 1),
				PartPose.offset(3.7F, 1.4F, 0.0F));

		PartDefinition lid2 = shape1.addOrReplaceChild("lid2",
				CubeListBuilder.create().texOffs(30, 0).addBox(0.0F, 0.0F, 0.0F, 5, 5, 1),
				PartPose.offset(-2.5F, 1.0F, 8.2F));

		lid2.addOrReplaceChild("lid21",
				CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, -0.1F, 2, 2, 1),
				PartPose.offset(-0.6F, 1.5F, 0.0F));

		lid2.addOrReplaceChild("lid22",
				CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, -0.1F, 2, 2, 1),
				PartPose.offset(3.7F, 1.4F, 0.0F));

		return LayerDefinition.create(mesh, 64, 64);
	}
}
