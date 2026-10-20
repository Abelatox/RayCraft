package com.abelatox.raycraft.client.sound;

import com.abelatox.raycraft.capabilities.ModCapabilities;
import com.abelatox.raycraft.entities.EntityBarrel;
import com.abelatox.raycraft.lib.Reference;
import com.abelatox.raycraft.sounds.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

import java.util.HashMap;
import java.util.Map;

@EventBusSubscriber(modid = Reference.MODID, value = Dist.CLIENT)
public class LoopSoundHandler {
	private static final int TICKS_TO_START = 2;
	private static final int TICKS_TO_STOP = 3;

	private static final Map<Integer, Helicopter> HELICOPTERS = new HashMap<>();

	private static class Helicopter {
		private int glidingTicks;
		private int notGlidingTicks;
		private EntityLoopSound sound;
	}

	@SubscribeEvent
	public static void onClientTick(ClientTickEvent.Post event) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.level == null) {
			HELICOPTERS.clear();
			return;
		}

		for (Player player : mc.level.players()) {
			Helicopter heli = HELICOPTERS.computeIfAbsent(player.getId(), id -> new Helicopter());
			boolean playing = heli.sound != null && !heli.sound.isStopped();

			if (ModCapabilities.get(player).getIsGliding()) {
				heli.glidingTicks++;
				heli.notGlidingTicks = 0;
				if (!playing && heli.glidingTicks >= TICKS_TO_START) {
					heli.sound = new EntityLoopSound(player, ModSounds.HELICO.get());
					mc.getSoundManager().play(heli.sound);
				}
			} else {
				heli.notGlidingTicks++;
				heli.glidingTicks = 0;
				if (playing && heli.notGlidingTicks >= TICKS_TO_STOP) {
					heli.sound.stopNow();
					heli.sound = null;
					mc.level.playLocalSound(player.getX(), player.getY(), player.getZ(), ModSounds.HELICO_STOP.get(), SoundSource.PLAYERS, 1.0F, 1.0F, false);
				}
			}
		}

		HELICOPTERS.keySet().removeIf(id -> mc.level.getEntity(id) == null);
	}

	@SubscribeEvent
	public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
		if (event.getLevel().isClientSide() && event.getEntity() instanceof EntityBarrel barrel) {
			Minecraft.getInstance().getSoundManager().play(new EntityLoopSound(barrel, ModSounds.BOMB_LIVING.get()));
		}
	}
}
