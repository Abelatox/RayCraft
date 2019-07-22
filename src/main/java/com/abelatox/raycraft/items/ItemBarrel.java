package com.abelatox.raycraft.items;

import com.abelatox.raycraft.entities.EntityBarrel;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class ItemBarrel extends BaseItem {

	public ItemBarrel(Properties properties) {
		super(properties);
		// TODO Auto-generated constructor stub
	}

		public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {

		// Minecraft.getInstance().displayGuiScreen(new GUIChooseWeebPower(player));
		if (!world.isRemote) {
			EntityBarrel barrel = new EntityBarrel(world, player);
			world.addEntity(barrel);
			barrel.shoot(player, player.rotationPitch, player.rotationYaw, 0, 0.8f, 0);
			System.out.println("throwing barrelino");
			player.inventory.removeStackFromSlot(player.inventory.currentItem);
		}
			return new ActionResult<>(ActionResultType.PASS, player.getHeldItem(hand));
	}
}
