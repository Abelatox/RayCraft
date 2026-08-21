package com.abelatox.raycraft.items;

import com.abelatox.raycraft.capabilities.IPlayerCapabilities;
import com.abelatox.raycraft.capabilities.ModCapabilities;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class ItemPowerUp extends BaseItem {

	public ItemPowerUp(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		IPlayerCapabilities props = ModCapabilities.get(player);

		if (!level.isClientSide()) {
			if (props.getShotLevel() < 3) {
				props.setShotLevel(props.getShotLevel() + 1);
			} else {
				props.setShotLevel(0);
			}

			ModCapabilities.sync(player);
		}
		return InteractionResult.PASS;
	}
}
