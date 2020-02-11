package com.abelatox.raycraft.models.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

public interface IRayCraftRender {
	void doRender(LivingEntity entityLiving, double x, double y, double z, float u, float v, MatrixStack matrixStackIn, IRenderTypeBuffer iRenderTypeBuffer, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha);
	void renderFirstPersonArm(PlayerEntity player, MatrixStack matrixStackIn, IRenderTypeBuffer iRenderTypeBuffer, int packedLightIn);
}
