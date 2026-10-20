package com.abelatox.raycraft.events;

import com.abelatox.raycraft.capabilities.IPlayerCapabilities;
import com.abelatox.raycraft.capabilities.ModCapabilities;
import com.abelatox.raycraft.lib.Reference;
import com.abelatox.raycraft.lib.Strings;
import com.abelatox.raycraft.sounds.ModSounds;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.PlayLevelSoundEvent;
import net.neoforged.neoforge.event.entity.EntityStruckByLightningEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.block.BreakBlockEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.Map;
import java.util.WeakHashMap;

@EventBusSubscriber(modid = Reference.MODID)
public class SoundEventsHandler {

	private static final int PIRATE_AMBIENT_CHANCE = 400;
	private static final int PIRATE_SNORE_INTERVAL = 50;
	private static final long ELECTROCUTED_COOLDOWN = 40;

	private static final Map<Player, Boolean> WAS_IN_WATER = new WeakHashMap<>();
	private static final Map<Player, Boolean> WAS_ON_FIRE = new WeakHashMap<>();
	private static final Map<Player, Long> LAST_ELECTROCUTED = new WeakHashMap<>();

	private static boolean isRoboPirate(String type) {
		return Strings.ROBO_PIRATE_RED.equals(type) || Strings.ROBO_PIRATE_GREEN.equals(type);
	}

	private static void play(Player player, Holder<SoundEvent> sound) {
		player.level().playSound(null, player.getX(), player.getY(), player.getZ(), sound.value(), SoundSource.PLAYERS, 1.0F, 1.0F);
	}

	// Player sounds are played by position only, so the player is found by its exact position.
	@SubscribeEvent
	public static void onPlaySoundAtPosition(PlayLevelSoundEvent.AtPosition event) {
		if (event.getSound() == null) {
			return;
		}
		SoundEvent sound = event.getSound().value();
		boolean death = sound == SoundEvents.PLAYER_DEATH;
		boolean hurt = sound == SoundEvents.PLAYER_HURT || sound == SoundEvents.PLAYER_HURT_DROWN || sound == SoundEvents.PLAYER_HURT_FREEZE
				|| sound == SoundEvents.PLAYER_HURT_ON_FIRE || sound == SoundEvents.PLAYER_HURT_SWEET_BERRY_BUSH;
		if (!death && !hurt) {
			return;
		}

		Vec3 pos = event.getPosition();
		for (Player player : event.getLevel().players()) {
			if (player.getX() != pos.x || player.getY() != pos.y || player.getZ() != pos.z) {
				continue;
			}
			String type = ModCapabilities.get(player).getPlayerType();
			if (isRoboPirate(type)) {
				event.setSound(death ? ModSounds.PIRATE_DEATH : ModSounds.PIRATE_HURT);
			} else if (death && Strings.RAYMAN.equals(type)) {
				event.setSound(ModSounds.RAYMAN_DEATH);
			}
			return;
		}
	}

	@SubscribeEvent
	public static void onRespawn(PlayerEvent.PlayerRespawnEvent event) {
		Player player = event.getEntity();
		String type = ModCapabilities.get(player).getPlayerType();
		if (Strings.RAYMAN.equals(type)) {
			play(player, ModSounds.RAYMAN_RESPAWN);
		} else if (isRoboPirate(type)) {
			play(player, ModSounds.PIRATE_DIGUP);
		}
	}

	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		Player player = event.getEntity();
		if (player.level().isClientSide()) {
			return;
		}
		IPlayerCapabilities props = ModCapabilities.get(player);
		if (!props.hasCustomPlayerType()) {
			return;
		}

		if (isRoboPirate(props.getPlayerType())) {
			if (player.isSleeping()) {
				if (player.tickCount % PIRATE_SNORE_INTERVAL == 0) {
					play(player, ModSounds.PIRATE_SLEEP);
				}
			} else if (player.getRandom().nextInt(PIRATE_AMBIENT_CHANCE) == 0) {
				play(player, ModSounds.PIRATE_LIVING);
			}
		}

		boolean inWater = player.isInWater();
		if (inWater && !WAS_IN_WATER.getOrDefault(player, true)) {
			play(player, ModSounds.SPLASH);
		}
		WAS_IN_WATER.put(player, inWater);

		boolean onFire = player.isOnFire();
		if (onFire && !WAS_ON_FIRE.getOrDefault(player, true)) {
			play(player, ModSounds.BURN);
		}
		WAS_ON_FIRE.put(player, onFire);
	}

	@SubscribeEvent
	public static void onStruckByLightning(EntityStruckByLightningEvent event) {
		if (!(event.getEntity() instanceof Player player) || player.level().isClientSide()
				|| !ModCapabilities.get(player).hasCustomPlayerType()) {
			return;
		}
		// A bolt hits for several ticks; only play the sound once per strike.
		long now = player.level().getGameTime();
		if (now - LAST_ELECTROCUTED.getOrDefault(player, Long.MIN_VALUE / 2) >= ELECTROCUTED_COOLDOWN) {
			LAST_ELECTROCUTED.put(player, now);
			play(player, ModSounds.ELECTROCUTED);
		}
	}

	@SubscribeEvent
	public static void onDamaged(LivingDamageEvent.Post event) {
		if (event.getEntity() instanceof Player player && !player.level().isClientSide() && player.isInWater()
				&& event.getSource().getEntity() instanceof Mob
				&& ModCapabilities.get(player).hasCustomPlayerType()) {
			play(player, ModSounds.PIRANHA_BITE);
		}
	}

	@SubscribeEvent
	public static void onJump(LivingEvent.LivingJumpEvent event) {
		if (event.getEntity() instanceof Player player && !player.level().isClientSide()
				&& ModCapabilities.get(player).hasCustomPlayerType()
				&& player.level().getBlockState(player.getOnPos()).is(Blocks.SLIME_BLOCK)) {
			play(player, ModSounds.TRAMPOLINE);
		}
	}

	@SubscribeEvent
	public static void onBreakBlock(BreakBlockEvent event) {
		Player player = event.getPlayer();
		if (player != null && !player.level().isClientSide() && isRoboPirate(ModCapabilities.get(player).getPlayerType())) {
			play(player, ModSounds.PIRATE_DIG);
		}
	}
}
