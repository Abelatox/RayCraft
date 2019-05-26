package com.abelatox.raycraft.items;

import com.abelatox.raycraft.capabilities.IPlayerModelCapability;
import com.abelatox.raycraft.capabilities.ModCapabilities;
import com.abelatox.raycraft.entities.EntityBarrel;
import com.abelatox.raycraft.network.PacketHandler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class ItemPowerUp extends BaseItem {

	public ItemPowerUp(Properties properties) {
		super(properties);
		// TODO Auto-generated constructor stub
	}

	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		// Minecraft.getInstance().displayGuiScreen(new GUIChooseWeebPower(player));
		IPlayerModelCapability props = ModCapabilities.get(player);

		if (!world.isRemote) {
			if (props.getShotLevel() < 3) {
				props.setShotLevel(props.getShotLevel() + 1);
			} else {
				props.setShotLevel(0);
			}
			player.sendMessage(new TextComponentString("Fist level: " + props.getShotLevel()));

		}
		PacketHandler.sendToAllAround(player, props);

		return new ActionResult<>(EnumActionResult.PASS, player.getHeldItem(hand));
	}
}
