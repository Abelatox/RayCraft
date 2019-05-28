package com.abelatox.raycraft.sounds;

import com.abelatox.raycraft.lib.Reference;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class ModSounds {
	public static SoundEvent
        fistShot0 = registerSound("fist_shot_0"),
        fistShot1 = registerSound("fist_shot_1"),
        fistShot2 = registerSound("fist_shot_2"),
		fistBounce = registerSound("fist_bounce"),
		fistGoldCharged = registerSound("fist_gold_charged"),
		pirateShot1 = registerSound("pirate_shot1"),
		pirateShot2 = registerSound("pirate_shot2");
     
    public static SoundEvent registerSound(String name) {
        final ResourceLocation soundID = new ResourceLocation(Reference.MODID, name);
        return new SoundEvent(soundID).setRegistryName(soundID);
    }
    
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class register {
    	
		@SubscribeEvent
		public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
			event.getRegistry().registerAll(fistShot0, fistShot1, fistShot2, fistBounce, fistGoldCharged, pirateShot1, pirateShot2);
		}
    }
}
