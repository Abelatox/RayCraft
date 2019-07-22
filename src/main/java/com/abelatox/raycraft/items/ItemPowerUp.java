package com.abelatox.raycraft.items;

import com.abelatox.raycraft.capabilities.IPlayerModelCapability;
import com.abelatox.raycraft.capabilities.ModCapabilities;
import com.abelatox.raycraft.network.PacketHandler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class ItemPowerUp extends BaseItem {

	public ItemPowerUp(Properties properties) {
		super(properties);
		// TODO Auto-generated constructor stub
	}

	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
		// Minecraft.getInstance().displayGuiScreen(new GUIChooseWeebPower(player));
		IPlayerModelCapability props = ModCapabilities.get(player);

		if (!world.isRemote) {
			if (props.getShotLevel() < 3) {
				props.setShotLevel(props.getShotLevel() + 1);
			} else {
				props.setShotLevel(0);
			}
			//player.sendMessage(new TextComponentString("Fist level: " + props.getShotLevel()));

		}
		PacketHandler.syncToAllAround(player, props);
			return new ActionResult<>(ActionResultType.PASS, player.getHeldItem(hand));
	}
}
