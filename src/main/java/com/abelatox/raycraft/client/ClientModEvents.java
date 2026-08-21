package com.abelatox.raycraft.client;

import com.abelatox.raycraft.capabilities.IPlayerCapabilities;
import com.abelatox.raycraft.capabilities.ModCapabilities;
import com.abelatox.raycraft.client.gui.RaycraftHealthOverlay;
import com.abelatox.raycraft.client.render.RaycraftAvatarRenderData;
import com.abelatox.raycraft.client.render.RaycraftRenderData;
import com.abelatox.raycraft.client.render.StubEntityRenderer;
import com.abelatox.raycraft.entities.ModEntities;
import com.abelatox.raycraft.entities.render.RenderEntityBarrel;
import com.abelatox.raycraft.entities.render.RenderEntityFist;
import com.abelatox.raycraft.entities.render.RenderEntityPirateShot;
import com.abelatox.raycraft.entities.render.RenderEntityPirateShot2;
import com.abelatox.raycraft.items.ModItems;
import com.abelatox.raycraft.lib.Reference;
import com.abelatox.raycraft.models.ModModels;

import net.minecraft.client.entity.ClientAvatarEntity;
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

	@SubscribeEvent
	public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		ModModels.registerLayerDefinitions(event);
	}

	@SubscribeEvent
	public static void registerRenderStateModifiers(RegisterRenderStateModifiersEvent event) {
		event.registerAvatarEntityModifier(new AvatarRenderStateModifier() {
			@Override
			public <T extends Avatar & ClientAvatarEntity> void accept(T avatar, AvatarRenderState renderState) {
				if (!(avatar instanceof Player player)) {
					return;
				}

				IPlayerCapabilities caps = ModCapabilities.get(player);
				boolean holdingBarrel = ItemStack.matches(player.getMainHandItem(), new ItemStack(ModItems.BARREL.get()));
				int lightCoords = LightCoordsUtil.getLightCoords(player.level(), player.blockPosition());

				renderState.setRenderData(RaycraftRenderData.AVATAR_DATA, new RaycraftAvatarRenderData(
						caps.getPlayerType(),
						holdingBarrel,
						caps.getIsCharging(),
						caps.getIsGliding(),
						caps.getArmRotation(),
						caps.getHairRotation(),
						caps.getShotLevel(),
						lightCoords
				));
			}
		});
	}

	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {

		event.registerEntityRenderer(ModEntities.BARREL.get(), RenderEntityBarrel::new);
		event.registerEntityRenderer(ModEntities.FIST.get(), RenderEntityFist::new);
		event.registerEntityRenderer(ModEntities.PIRATE_SHOT.get(), RenderEntityPirateShot::new);
		event.registerEntityRenderer(ModEntities.PIRATE_SHOT_2.get(), RenderEntityPirateShot2::new);
		event.registerEntityRenderer(ModEntities.LUM.get(), StubEntityRenderer::new);
	}

	@SubscribeEvent
	public static void registerGuiLayers(RegisterGuiLayersEvent event) {
		event.replaceLayer(VanillaGuiLayers.PLAYER_HEALTH, new RaycraftHealthOverlay());
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
