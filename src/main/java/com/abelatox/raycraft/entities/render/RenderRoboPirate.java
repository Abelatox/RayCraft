package com.abelatox.raycraft.entities.render;

import com.abelatox.raycraft.entities.EntityRoboPirate;
import com.abelatox.raycraft.lib.Reference;
import com.abelatox.raycraft.models.ModModels;
import com.abelatox.raycraft.models.ModelRoboPirateMob;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;

public class RenderRoboPirate extends MobRenderer<EntityRoboPirate, RenderRoboPirate.RoboPirateRenderState, ModelRoboPirateMob> {

	public static final Identifier RED_TEXTURE = Identifier.fromNamespaceAndPath(Reference.MODID, "textures/models/robopirate.png");
	public static final Identifier GREEN_TEXTURE = Identifier.fromNamespaceAndPath(Reference.MODID, "textures/models/robopirate2.png");

	private final Identifier texture;

	public RenderRoboPirate(EntityRendererProvider.Context context, Identifier texture) {
		super(context, new ModelRoboPirateMob(context.bakeLayer(ModModels.ROBO_PIRATE_LAYER)), 0.5F);
		this.texture = texture;
	}

	@Override
	public RoboPirateRenderState createRenderState() {
		return new RoboPirateRenderState();
	}

	@Override
	public void extractRenderState(EntityRoboPirate entity, RoboPirateRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.aiming = entity.isAggressive();
	}

	@Override
	public Identifier getTextureLocation(RoboPirateRenderState state) {
		return texture;
	}

	public static class RoboPirateRenderState extends LivingEntityRenderState {
		public boolean aiming;
	}
}
