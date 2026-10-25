package com.abelatox.raycraft.models;

import com.abelatox.raycraft.lib.Reference;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

/**
 * Registers the {@link ModelLayerLocation}s of the simple projectile models
 * (barrel, fist, pirate shot) and of the playable character models
 * (Rayman/Hoodlum/Globox/RoboPirate).
 */
public class ModModels {

	public static final ModelLayerLocation BARREL_LAYER = new ModelLayerLocation(Identifier.fromNamespaceAndPath(Reference.MODID, "barrel"), "main");
	public static final ModelLayerLocation FIST_LAYER = new ModelLayerLocation(Identifier.fromNamespaceAndPath(Reference.MODID, "fist"), "main");
	public static final ModelLayerLocation PIRATE_SHOT_LAYER = new ModelLayerLocation(Identifier.fromNamespaceAndPath(Reference.MODID, "pirate_shot"), "main");

	public static final ModelLayerLocation RAYMAN_LAYER = new ModelLayerLocation(Identifier.fromNamespaceAndPath(Reference.MODID, "rayman"), "main");
	public static final ModelLayerLocation HOODLUM_LAYER = new ModelLayerLocation(Identifier.fromNamespaceAndPath(Reference.MODID, "hoodlum"), "main");
	public static final ModelLayerLocation GLOBOX_LAYER = new ModelLayerLocation(Identifier.fromNamespaceAndPath(Reference.MODID, "globox"), "main");
	public static final ModelLayerLocation ROBO_PIRATE_LAYER = new ModelLayerLocation(Identifier.fromNamespaceAndPath(Reference.MODID, "robo_pirate"), "main");
	public static final ModelLayerLocation PLUM_LAYER = new ModelLayerLocation(Identifier.fromNamespaceAndPath(Reference.MODID, "plum"), "main");
	public static final ModelLayerLocation WALKING_SHELL_LAYER = new ModelLayerLocation(Identifier.fromNamespaceAndPath(Reference.MODID, "walking_shell"), "main");

	public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(BARREL_LAYER, ModelBarrel::createBodyLayer);
		event.registerLayerDefinition(FIST_LAYER, ModelFist::createBodyLayer);
		event.registerLayerDefinition(PIRATE_SHOT_LAYER, ModelPirateShot::createBodyLayer);

		event.registerLayerDefinition(RAYMAN_LAYER, ModelRayman::createBodyLayer);
		event.registerLayerDefinition(HOODLUM_LAYER, ModelHoodlum::createBodyLayer);
		event.registerLayerDefinition(GLOBOX_LAYER, ModelGlobox::createBodyLayer);
		event.registerLayerDefinition(ROBO_PIRATE_LAYER, ModelRoboPirate::createBodyLayer);
		event.registerLayerDefinition(PLUM_LAYER, ModelPlum::createBodyLayer);
		event.registerLayerDefinition(WALKING_SHELL_LAYER, ModelWalkingShell::createBodyLayer);
	}
}
