package com.abelatox.raycraft.blocks;

import com.abelatox.raycraft.lib.Reference;

import net.minecraft.world.level.block.SoundType;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {

	public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Reference.MODID);

	public static final DeferredBlock<BlockBarrel>
			BARREL = BLOCKS.registerBlock("barrelblock", BlockBarrel::new, props -> props.strength(1.0F, 10.0F).sound(SoundType.METAL)
	);

	public static final DeferredBlock<BlockCage>
			CAGE = BLOCKS.registerBlock("cage", BlockCage::new, props -> props.strength(-1.0F, 3600000.0F).sound(SoundType.METAL).noOcclusion()
	);

	public static final DeferredBlock<BlockTimerSwitch>
			TIMER_SWITCH = BLOCKS.registerBlock("timer_switch", BlockTimerSwitch::new, props -> props.strength(1.5F, 6.0F).sound(SoundType.METAL).lightLevel(state -> BlockTimerSwitch.isOn(state) ? 7 : 0)
	);
}
