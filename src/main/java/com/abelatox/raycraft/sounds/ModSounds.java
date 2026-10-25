package com.abelatox.raycraft.sounds;

import com.abelatox.raycraft.lib.Reference;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModSounds {

	public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Registries.SOUND_EVENT, Reference.MODID);

	public static final DeferredHolder<SoundEvent, SoundEvent> FIST_SHOT_0 = registerSound("fist_shot_0");
	public static final DeferredHolder<SoundEvent, SoundEvent> FIST_SHOT_1 = registerSound("fist_shot_1");
	public static final DeferredHolder<SoundEvent, SoundEvent> FIST_SHOT_2 = registerSound("fist_shot_2");
	public static final DeferredHolder<SoundEvent, SoundEvent> FIST_BOUNCE = registerSound("fist_bounce");
	public static final DeferredHolder<SoundEvent, SoundEvent> FIST_GOLD_CHARGED = registerSound("fist_gold_charged");
	public static final DeferredHolder<SoundEvent, SoundEvent> PIRATE_SHOT_1 = registerSound("pirate_shot1");
	public static final DeferredHolder<SoundEvent, SoundEvent> PIRATE_SHOT_2 = registerSound("pirate_shot2");
	public static final DeferredHolder<SoundEvent, SoundEvent> BOMB_EXPLODE = registerSound("bomb_explode");
	public static final DeferredHolder<SoundEvent, SoundEvent> LUM_YELLOW = registerSound("lum_yellow");
	public static final DeferredHolder<SoundEvent, SoundEvent> LUM_GREEN = registerSound("lum_green");
	public static final DeferredHolder<SoundEvent, SoundEvent> LUM_RED = registerSound("lum_red");
	public static final DeferredHolder<SoundEvent, SoundEvent> HELICO = registerSound("helico");
	public static final DeferredHolder<SoundEvent, SoundEvent> HELICO_STOP = registerSound("helico_stop");
	public static final DeferredHolder<SoundEvent, SoundEvent> BOMB_THROW = registerSound("bomb_throw");
	public static final DeferredHolder<SoundEvent, SoundEvent> BOMB_LIVING = registerSound("bomb_living");
	public static final DeferredHolder<SoundEvent, SoundEvent> LUM_YELLOW_SPAWN = registerSound("lum_yellow_spawn");
	public static final DeferredHolder<SoundEvent, SoundEvent> RAYMAN_DEATH = registerSound("rayman_death");
	public static final DeferredHolder<SoundEvent, SoundEvent> RAYMAN_RESPAWN = registerSound("rayman_respawn");
	public static final DeferredHolder<SoundEvent, SoundEvent> RAYMAN_DANCE = registerSound("rayman_dance");
	public static final DeferredHolder<SoundEvent, SoundEvent> PIRATE_HURT = registerSound("pirate_hurt");
	public static final DeferredHolder<SoundEvent, SoundEvent> PIRATE_DEATH = registerSound("pirate_death");
	public static final DeferredHolder<SoundEvent, SoundEvent> PIRATE_LIVING = registerSound("pirate_living");
	public static final DeferredHolder<SoundEvent, SoundEvent> PIRATE_SLEEP = registerSound("pirate_sleep");
	public static final DeferredHolder<SoundEvent, SoundEvent> PIRATE_DIG = registerSound("pirate_dig");
	public static final DeferredHolder<SoundEvent, SoundEvent> PIRATE_DIGUP = registerSound("pirate_digup");
	public static final DeferredHolder<SoundEvent, SoundEvent> PIRATE_LASER = registerSound("pirate_laser");
	public static final DeferredHolder<SoundEvent, SoundEvent> FIST_UNDERWATER = registerSound("fist_underwater");
	public static final DeferredHolder<SoundEvent, SoundEvent> PLUM_HIT = registerSound("plum_hit");
	public static final DeferredHolder<SoundEvent, SoundEvent> SHELL_WALK = registerSound("shell_walk");
	public static final DeferredHolder<SoundEvent, SoundEvent> SHELL_NEIGH = registerSound("shell_neigh");
	public static final DeferredHolder<SoundEvent, SoundEvent> SHELL_STOP = registerSound("shell_stop");
	public static final DeferredHolder<SoundEvent, SoundEvent> PIRANHA_BITE = registerSound("piranha_bite");
	public static final DeferredHolder<SoundEvent, SoundEvent> SPLASH = registerSound("splash");
	public static final DeferredHolder<SoundEvent, SoundEvent> BURN = registerSound("burn");
	public static final DeferredHolder<SoundEvent, SoundEvent> ELECTROCUTED = registerSound("electrocuted");
	public static final DeferredHolder<SoundEvent, SoundEvent> TRAMPOLINE = registerSound("trampoline");
	public static final DeferredHolder<SoundEvent, SoundEvent> CAGE_HELP = registerSound("cage_help");
	public static final DeferredHolder<SoundEvent, SoundEvent> CAGE_HIT = registerSound("cage_hit");
	public static final DeferredHolder<SoundEvent, SoundEvent> CAGE_BREAK = registerSound("cage_break");
	public static final DeferredHolder<SoundEvent, SoundEvent> CAGE_HEAL_BONUS = registerSound("cage_heal_bonus");
	public static final DeferredHolder<SoundEvent, SoundEvent> SWITCH_TICK = registerSound("switch_tick");
	public static final DeferredHolder<SoundEvent, SoundEvent> SWITCH_TOCK = registerSound("switch_tock");

	private static DeferredHolder<SoundEvent, SoundEvent> registerSound(String name) {
		return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(Reference.MODID, name)));
	}
}
