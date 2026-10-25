package com.abelatox.raycraft.events;

import com.abelatox.raycraft.lib.Reference;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

@EventBusSubscriber(modid = Reference.MODID)
public class GrappleServer {

	private static final Set<Player> SWINGING = Collections.newSetFromMap(new WeakHashMap<>());

	public static void setSwinging(Player player, boolean swinging) {
		if (swinging) {
			SWINGING.add(player);
		} else {
			SWINGING.remove(player);
		}
	}

	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		if (!event.getEntity().level().isClientSide() && SWINGING.contains(event.getEntity())) {
			event.getEntity().resetFallDistance();
		}
	}
}
