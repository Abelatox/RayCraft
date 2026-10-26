package com.abelatox.raycraft.items;

import com.abelatox.raycraft.entities.EntitySpiralPortal;
import com.abelatox.raycraft.entities.ModEntities;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.phys.Vec3;

public class ItemSpiralPortal extends BaseItem {

	public ItemSpiralPortal(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		if (context.getPlayer() instanceof ServerPlayer player && player.isCreative()) {
			EntitySpiralPortal portal = new EntitySpiralPortal(ModEntities.SPIRAL_PORTAL.get(), context.getLevel());
			portal.setFlat(Math.abs(player.getXRot()) > FLAT_PITCH);
			Vec3 pos = placement(context, portal.getBbHeight());
			portal.setPos(pos.x, pos.y, pos.z);
			portal.setYRot(player.getYRot());
			context.getLevel().addFreshEntity(portal);
			portal.openConfig(player);
		}
		return InteractionResult.SUCCESS;
	}

	/** Looking further up or down than this lays the portal flat. */
	private static final float FLAT_PITCH = 50.0F;

	/** Centred on the point aimed at: standing on a floor, hanging from a ceiling, or centred on a wall. */
	private static Vec3 placement(UseOnContext context, float height) {
		Vec3 hit = context.getClickLocation();
		return switch (context.getClickedFace()) {
			case UP -> hit;
			case DOWN -> hit.subtract(0.0, height, 0.0);
			default -> hit.add(context.getClickedFace().getUnitVec3().scale(0.1)).subtract(0.0, height / 2.0, 0.0);
		};
	}
}
