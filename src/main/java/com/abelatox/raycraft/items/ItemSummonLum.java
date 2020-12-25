package com.abelatox.raycraft.items;

import com.abelatox.raycraft.entities.EntityLum;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.world.World;

public class ItemSummonLum extends BaseItem {

	public ItemSummonLum(Properties properties) {
		super(properties);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		PlayerEntity player = context.getPlayer();
		World world = context.getWorld();

		if (!world.isRemote) {
			String type = "green";
			EntityLum lum = new EntityLum(world);
			lum.setLumType(type);
			lum.setPosition(context.getPos().getX() + 0.5, context.getPos().getY() + 1.5, context.getPos().getZ() + 0.5);
			world.addEntity(lum);
			//player.sendMessage(new TranslationTextComponent("Summonned: " + type));
		}
		return super.onItemUse(context);
	}
}
