package com.abelatox.raycraft.client;

import com.abelatox.raycraft.capabilities.IPlayerCapabilities;
import com.abelatox.raycraft.capabilities.ModCapabilities;
import com.abelatox.raycraft.client.gui.RaycraftHealthOverlay;
import com.abelatox.raycraft.client.render.LockOnRenderer;
import com.abelatox.raycraft.client.render.RaycraftAvatarRenderData;
import com.abelatox.raycraft.client.render.RaycraftRenderData;
import com.abelatox.raycraft.entities.EntityFlyingShell;
import org.joml.Quaternionf;
import com.abelatox.raycraft.entities.EntityWalkingShell;
import com.abelatox.raycraft.entities.ModEntities;
import com.abelatox.raycraft.entities.render.*;
import com.abelatox.raycraft.items.ModItems;
import com.abelatox.raycraft.lib.Reference;
import com.abelatox.raycraft.models.ModModels;
import net.minecraft.client.entity.ClientAvatarEntity;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.client.renderstate.AvatarRenderStateModifier;
import net.neoforged.neoforge.client.renderstate.RegisterRenderStateModifiersEvent;
import net.neoforged.neoforge.common.NeoForge;

@EventBusSubscriber(modid = Reference.MODID, value = Dist.CLIENT)
public class ClientModEvents {

	private static final float ARM_SPIN_PER_TICK = 0.8F;
	private static final float HAIR_SPIN_PER_TICK = 2.5F;

	@SubscribeEvent
	public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		ModModels.registerLayerDefinitions(event);
	}

	@SuppressWarnings("unchecked")
	@SubscribeEvent
	public static void registerRenderStateModifiers(RegisterRenderStateModifiersEvent event) {
		// Raw cast on purpose: a class literal has no generics, so it cannot be cast straight to the generic type.
		@SuppressWarnings("rawtypes")
		Class<? extends LivingEntityRenderer<?, ?, ?>> livingRenderers = (Class) LivingEntityRenderer.class;
		event.registerEntityModifier(livingRenderers, LockOnRenderer::markTarget);

		event.registerAvatarEntityModifier(new AvatarRenderStateModifier() {
			@Override
			public <T extends Avatar & ClientAvatarEntity> void accept(T avatar, AvatarRenderState renderState) {
				if (!(avatar instanceof Player player)) {
					return;
				}

				IPlayerCapabilities caps = ModCapabilities.get(player);
				boolean holdingBarrel = ItemStack.matches(player.getMainHandItem(), new ItemStack(ModItems.BARREL.get()));
				boolean holdingPlum = player.getMainHandItem().is(ModItems.PLUM.get());
				boolean ridingShell = player.getVehicle() instanceof EntityWalkingShell;
				EntityFlyingShell flyingShell = player.getVehicle() instanceof EntityFlyingShell shell ? shell : null;
				Quaternionf shellRotation = flyingShell != null ? flyingShell.getRiderRotation(renderState.partialTick) : null;
				float rollPivot = flyingShell != null ? player.getEyeHeight() : 0F;
				int lightCoords = LightCoordsUtil.getLightCoords(player.level(), player.blockPosition());

				float armRotation = caps.getIsCharging() ? -renderState.ageInTicks * ARM_SPIN_PER_TICK : 0F;
				float hairRotation = caps.getIsGliding() ? -renderState.ageInTicks * HAIR_SPIN_PER_TICK : 0F;

				renderState.setRenderData(RaycraftRenderData.AVATAR_DATA, new RaycraftAvatarRenderData(caps.getPlayerType(), holdingBarrel, holdingPlum, ridingShell, shellRotation, rollPivot, caps.getIsCharging(), caps.getIsGliding(), armRotation, hairRotation, caps.getShotLevel(), lightCoords));
			}
		});
	}

	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {

		event.registerEntityRenderer(ModEntities.BARREL.get(), RenderEntityBarrel::new);
		event.registerEntityRenderer(ModEntities.FIST.get(), RenderRaycraftProjectile::fist);
		event.registerEntityRenderer(ModEntities.PIRATE_SHOT.get(), RenderRaycraftProjectile::pirateShot);
		event.registerEntityRenderer(ModEntities.PIRATE_SHOT_2.get(), RenderRaycraftProjectile::pirateShot2);
		event.registerEntityRenderer(ModEntities.LUM.get(), RenderEntityLum::new);
		event.registerEntityRenderer(ModEntities.SPIRAL_PORTAL.get(), RenderSpiralPortal::new);
		event.registerEntityRenderer(ModEntities.ROBO_PIRATE.get(), context -> new RenderRoboPirate(context, RenderRoboPirate.RED_TEXTURE));
		event.registerEntityRenderer(ModEntities.ROBO_PIRATE_GREEN.get(), context -> new RenderRoboPirate(context, RenderRoboPirate.GREEN_TEXTURE));
		event.registerEntityRenderer(ModEntities.PLUM.get(), RenderEntityPlum::new);
		event.registerEntityRenderer(ModEntities.WALKING_SHELL.get(), context -> new RenderWalkingShell(context, RenderWalkingShell.WALKING_TEXTURE));
		event.registerEntityRenderer(ModEntities.FLYING_SHELL.get(), context -> new RenderWalkingShell(context, RenderWalkingShell.FLYING_TEXTURE));
	}

	@SubscribeEvent
	public static void registerGuiLayers(RegisterGuiLayersEvent event) {
		event.wrapLayer(VanillaGuiLayers.PLAYER_HEALTH, RaycraftHealthOverlay::new);
	}

	@SubscribeEvent
	public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
		event.registerCategory(InputHandler.CATEGORY);
		for (InputHandler.Keybinds key : InputHandler.Keybinds.values()) {
			event.register(key.getKeybind());
		}
	}

	@SubscribeEvent
	public static void onClientSetup(FMLClientSetupEvent event) {
		NeoForge.EVENT_BUS.register(new InputHandler());

	}
}
