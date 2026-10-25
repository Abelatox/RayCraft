package com.abelatox.raycraft.entities.render;

import com.abelatox.raycraft.entities.EntityFlyingShell;
import com.abelatox.raycraft.entities.EntityWalkingShell;
import com.abelatox.raycraft.lib.Reference;
import com.abelatox.raycraft.models.ModModels;
import com.abelatox.raycraft.models.ModelWalkingShell;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

public class RenderWalkingShell extends MobRenderer<EntityWalkingShell, RenderWalkingShell.ShellRenderState, ModelWalkingShell> {

	public static final Identifier WALKING_TEXTURE = Identifier.fromNamespaceAndPath(Reference.MODID, "textures/models/walking_shell.png");
	public static final Identifier FLYING_TEXTURE = Identifier.fromNamespaceAndPath(Reference.MODID, "textures/models/flying_shell.png");

	private static final float SCALE = 0.65F;
	private static final float HALF_HEIGHT = 0.5F;

	private final Identifier texture;

	public RenderWalkingShell(EntityRendererProvider.Context context, Identifier texture) {
		super(context, new ModelWalkingShell(context.bakeLayer(ModModels.WALKING_SHELL_LAYER)), 0.45F);
		this.texture = texture;
	}

	@Override
	public ShellRenderState createRenderState() {
		return new ShellRenderState();
	}

	@Override
	public void extractRenderState(EntityWalkingShell entity, ShellRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.tired = entity.isTired();
		state.flying = entity instanceof EntityFlyingShell flyingShell && flyingShell.isFlying();
		state.roll = entity instanceof EntityFlyingShell flyingShell ? flyingShell.getRollAngle(partialTick) : 0.0F;
		state.rear = entity.isRearing() ? rearCurve(entity.getRearProgress(partialTick)) : 0.0F;
	}

	private static float rearCurve(float progress) {
		return Mth.clamp(Math.min(progress * 5.0F, (1.0F - progress) * 4.0F), 0.0F, 1.0F);
	}

	@Override
	protected void setupRotations(ShellRenderState state, PoseStack poseStack, float bodyRot, float entityScale) {
		super.setupRotations(state, poseStack, bodyRot, entityScale);
		if (state.flying) {
			poseStack.translate(0.0F, HALF_HEIGHT, 0.0F);
			poseStack.rotateDegrees(Axis.XP, -state.xRot);
			poseStack.rotateDegrees(Axis.ZP, state.roll);
			poseStack.translate(0.0F, -HALF_HEIGHT, 0.0F);
		}
	}

	@Override
	protected void scale(ShellRenderState state, PoseStack poseStack) {
		poseStack.scale(SCALE, SCALE, SCALE);
	}

	@Override
	public Identifier getTextureLocation(ShellRenderState state) {
		return texture;
	}

	public static class ShellRenderState extends LivingEntityRenderState {
		public boolean tired;
		public boolean flying;
		public float rear;
		public float roll;
	}
}
