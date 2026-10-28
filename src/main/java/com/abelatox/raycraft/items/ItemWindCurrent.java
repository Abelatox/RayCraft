package com.abelatox.raycraft.items;

import com.abelatox.raycraft.entities.EntityWindCurrent;
import com.abelatox.raycraft.entities.ModEntities;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.phys.Vec3;

public class ItemWindCurrent extends BaseItem {

	public ItemWindCurrent(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		if (context.getPlayer() instanceof ServerPlayer player && player.isCreative()) {
			EntityWindCurrent wind = new EntityWindCurrent(ModEntities.WIND_CURRENT.get(), context.getLevel());
			Vec3 pos = Vec3.atBottomCenterOf(context.getClickedPos().relative(context.getClickedFace()));
			wind.setPos(pos.x, pos.y, pos.z);
			context.getLevel().addFreshEntity(wind);
			wind.openConfig(player);
		}
		return InteractionResult.SUCCESS;
	}
}
