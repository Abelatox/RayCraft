package com.abelatox.raycraft.entities.render;

import com.abelatox.raycraft.entities.EntityPlum;
import com.abelatox.raycraft.lib.Reference;
import com.abelatox.raycraft.models.ModModels;
import com.abelatox.raycraft.models.ModelPlum;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;

public class RenderEntityPlum extends EntityRenderer<EntityPlum, EntityRenderState> {

	private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(Reference.MODID, "textures/models/plum.png");

	private final ModelPlum model;

	public RenderEntityPlum(EntityRendererProvider.Context context) {
		super(context);
		this.shadowRadius = 0.35F;
		this.model = new ModelPlum(context.bakeLayer(ModModels.PLUM_LAYER));
	}

	@Override
	public EntityRenderState createRenderState() {
		return new EntityRenderState();
	}

	@Override
	public void submit(EntityRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
		poseStack.pushPose();
		poseStack.scale(-1.0F, -1.0F, 1.0F);
		poseStack.translate(0.0F, -1.5F, 0.0F);
		collector.submitModel(model, state, poseStack, model.renderType(TEXTURE), state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
		poseStack.popPose();
		super.submit(state, poseStack, collector, camera);
	}
}
