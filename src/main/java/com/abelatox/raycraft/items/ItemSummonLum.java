package com.abelatox.raycraft.items;

import com.abelatox.raycraft.entities.EntityLum;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class ItemSummonLum extends BaseItem {

	public ItemSummonLum(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Player player = context.getPlayer();
		Level level = context.getLevel();

		if (!level.isClientSide()) {
			String type = "green";
			EntityLum lum = new EntityLum(level);
			lum.setLumType(type);
			lum.setPos(context.getClickedPos().getX() + 0.5, context.getClickedPos().getY() + 1.5, context.getClickedPos().getZ() + 0.5);
			level.addFreshEntity(lum);
		}
		return super.useOn(context);
	}
}
