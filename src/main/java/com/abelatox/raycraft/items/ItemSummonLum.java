package com.abelatox.raycraft.items;

import com.abelatox.raycraft.entities.EntityLum;
import com.abelatox.raycraft.sounds.ModSounds;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class ItemSummonLum extends BaseItem {

	private static final String DEFAULT_TYPE = "yellow";

	public ItemSummonLum(Properties properties) {
		super(properties);
	}

	public static String getLumType(ItemStack stack) {
		return stack.getOrDefault(ModDataComponents.LUM_TYPE.get(), DEFAULT_TYPE);
	}

	public static Component getLumName(String type) {
		return Component.translatable("lum.raycraft." + type).withColor(EntityLum.getColor(type) & 0xFFFFFF);
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);

		if (!level.isClientSide()) {
			int current = EntityLum.TYPES.indexOf(getLumType(stack));
			String next = EntityLum.TYPES.get((current + 1) % EntityLum.TYPES.size());
			stack.set(ModDataComponents.LUM_TYPE.get(), next);
			player.sendOverlayMessage(Component.translatable("message.raycraft.lum_type", getLumName(next)));
		}
		return InteractionResult.SUCCESS;
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level level = context.getLevel();

		if (!level.isClientSide()) {
			EntityLum lum = new EntityLum(level);
			lum.setLumType(getLumType(context.getItemInHand()));
			lum.setPos(context.getClickedPos().getX() + 0.5, context.getClickedPos().getY() + 1.5, context.getClickedPos().getZ() + 0.5);
			level.addFreshEntity(lum);
			level.playSound(null, lum.getX(), lum.getY(), lum.getZ(), ModSounds.LUM_YELLOW_SPAWN.get(), SoundSource.NEUTRAL, 1F, 1F);
		}
		// SUCCESS so clicking a block doesn't also call use() and change the type.
		return InteractionResult.SUCCESS;
	}
}
