package com.abelatox.raycraft.entities.render;

import com.abelatox.raycraft.entities.EntityFist;
import com.abelatox.raycraft.entities.RaycraftProjectile;
import com.abelatox.raycraft.lib.Reference;
import com.abelatox.raycraft.models.ModModels;
import com.abelatox.raycraft.models.ModelFist;
import com.abelatox.raycraft.models.ModelPirateShot;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;

import java.util.function.BiConsumer;
import java.util.function.ToIntFunction;

public class RenderRaycraftProjectile extends EntityRenderer<RaycraftProjectile, RenderRaycraftProjectile.ProjectileRenderState> {

	private static final int[] FIST_TINTS = {
			ARGB.colorFromFloat(1F, 1F, 1F, 1F),
			ARGB.colorFromFloat(1F, 0F, 0.9F, 1F),
			ARGB.colorFromFloat(1F, 1F, 0.7F, 0F),
			ARGB.colorFromFloat(1F, 1F, 0.9F, 0F),
			ARGB.colorFromFloat(1F, 1F, 0.9F, 0F)};

	private final Model<EntityRenderState> model;
	private final Identifier texture;
	private final float firstVisibleTick;
	private final BiConsumer<ProjectileRenderState, PoseStack> transform;
	private final ToIntFunction<ProjectileRenderState> tint;

	private RenderRaycraftProjectile(EntityRendererProvider.Context context, Model<EntityRenderState> model, String texture, float firstVisibleTick,
	                                 BiConsumer<ProjectileRenderState, PoseStack> transform, ToIntFunction<ProjectileRenderState> tint) {
		super(context);
		this.shadowRadius = 0.25F;
		this.model = model;
		this.texture = Identifier.fromNamespaceAndPath(Reference.MODID, "textures/models/" + texture + ".png");
		this.firstVisibleTick = firstVisibleTick;
		this.transform = transform;
		this.tint = tint;
	}

	public static RenderRaycraftProjectile fist(EntityRendererProvider.Context context) {
		return new RenderRaycraftProjectile(context, new ModelFist(context.bakeLayer(ModModels.FIST_LAYER)), "fist", 2, (state, pose) -> {
			// The model cube is centred on its origin; lift it to the centre of the hitbox.
			pose.translate(0.0F, state.boundingBoxHeight / 2.0F, 0.0F);
			if (state.lvl == EntityFist.GIANT_LEVEL) {
				pose.scale(EntityFist.GIANT_SCALE, EntityFist.GIANT_SCALE, EntityFist.GIANT_SCALE);
			}
			faceFlight(state, pose);
		}, state -> FIST_TINTS[state.lvl]);
	}

	public static RenderRaycraftProjectile pirateShot(EntityRendererProvider.Context context) {
		return new RenderRaycraftProjectile(context, new ModelFist(context.bakeLayer(ModModels.FIST_LAYER)), "fist", 3, (state, pose) -> {
			pose.scale(1.5F, 1.5F, 1.5F);
			faceFlight(state, pose);
		}, state -> ARGB.colorFromFloat(1F, 1F, 0.1F, 0.1F));
	}

	public static RenderRaycraftProjectile pirateShot2(EntityRendererProvider.Context context) {
		return new RenderRaycraftProjectile(context, new ModelPirateShot(context.bakeLayer(ModModels.PIRATE_SHOT_LAYER)), "pirateshot", 3, (state, pose) -> {
			faceFlight(state, pose);
			pose.scale(2F, 2F, 2F);
			pose.translate(0, 0.05, 0);
			pose.rotate(Axis.YP.rotationDegrees(180));
		}, state -> -1);
	}

	private static void faceFlight(ProjectileRenderState state, PoseStack pose) {
		pose.rotate(Axis.YP.rotationDegrees(state.yRot));
		pose.rotate(Axis.XN.rotationDegrees(state.xRot));
	}

	@Override
	public ProjectileRenderState createRenderState() {
		return new ProjectileRenderState();
	}

	@Override
	public void extractRenderState(RaycraftProjectile entity, ProjectileRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.yRot = entity.getYRot(partialTick);
		state.xRot = entity.getXRot(partialTick);
		state.lvl = entity instanceof EntityFist fist ? fist.getLvl() : 0;
	}

	@Override
	public void submit(ProjectileRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState cameraState) {
		super.submit(state, poseStack, collector, cameraState);
		if (state.ageInTicks < firstVisibleTick) {
			return;
		}
		poseStack.pushPose();
		transform.accept(state, poseStack);
		collector.submitModel(model, state, poseStack, model.renderType(texture), state.lightCoords, OverlayTexture.NO_OVERLAY, tint.applyAsInt(state), null, 0);
		poseStack.popPose();
	}

	public static class ProjectileRenderState extends EntityRenderState {
		public float yRot;
		public float xRot;
		public int lvl;
	}
}
