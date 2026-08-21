package com.abelatox.raycraft.items;

import com.abelatox.raycraft.entities.EntityBarrel;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class ItemBarrel extends BaseItem {

	public ItemBarrel(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		if (!level.isClientSide()) {
			EntityBarrel barrel = new EntityBarrel(level, player);
			level.addFreshEntity(barrel);
			barrel.setOwner(player);

			barrel.shootFromRotation(player, player.getXRot(), player.getYRot(), 0F, 1F, 0F);
			player.getInventory().removeItemNoUpdate(player.getInventory().getSelectedSlot());
		}
		return InteractionResult.PASS;
	}
}
