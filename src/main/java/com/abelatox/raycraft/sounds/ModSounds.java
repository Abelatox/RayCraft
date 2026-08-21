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

	private static DeferredHolder<SoundEvent, SoundEvent> registerSound(String name) {
		return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(Reference.MODID, name)));
	}
}
