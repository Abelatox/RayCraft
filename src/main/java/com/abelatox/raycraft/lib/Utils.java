package com.abelatox.raycraft.lib;

import com.abelatox.raycraft.capabilities.IPlayerCapabilities;
import com.abelatox.raycraft.capabilities.ModCapabilities;
import com.abelatox.raycraft.entities.EntityFist;
import com.abelatox.raycraft.entities.EntityPirateShot;
import com.abelatox.raycraft.entities.EntityPirateShot2;
import com.abelatox.raycraft.entities.RaycraftProjectile;
import com.abelatox.raycraft.items.ModItems;
import com.abelatox.raycraft.sounds.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

public class Utils {

	public static Identifier getBarrelTexture() {
		return Identifier.fromNamespaceAndPath(Reference.MODID, "textures/models/barrel.png");
	}

	public static int getSlotFor(Player player, ItemStack stack) {
		for (int i = 0; i < player.getInventory().getContainerSize(); ++i) {
			ItemStack slotStack = player.getInventory().getItem(i);
			if (!slotStack.isEmpty() && stackEqualExact(stack, slotStack)) {
				return i;
			}
		}
		return -1;
	}

	public static boolean canShoot(String playerType) {
		return Strings.RAYMAN.equals(playerType) || Strings.ROBO_PIRATE_RED.equals(playerType) || Strings.ROBO_PIRATE_GREEN.equals(playerType);
	}

	public static RaycraftProjectile getEntityShot(Player player, boolean charged) {
		IPlayerCapabilities props = ModCapabilities.get(player);
		switch (props.getPlayerType()) {
			case Strings.ROBO_PIRATE_RED:
				return new EntityPirateShot2(player.level(), player);
			case Strings.ROBO_PIRATE_GREEN:
				return new EntityPirateShot(player.level(), player);
			case Strings.RAYMAN:
				return getRaymanPunchLevel(player, charged);
		}
		return null;
	}

	private static EntityFist getRaymanPunchLevel(Player player, boolean charged) {
		IPlayerCapabilities props = ModCapabilities.get(player);
		int level = props.getShotLevel();
		if (charged && level < 4) {
			level++;
		}
		EntityFist projectile = new EntityFist(player.level(), player);
		switch (level) {
			case 0:
				projectile.setLvl(0);
				projectile.setPower(2);
				projectile.setMaxBounces(0);
				projectile.setMaxTicks(60);
				break;
			case 1:
				projectile.setLvl(1);
				projectile.setPower(4);
				projectile.setMaxBounces(1);
				projectile.setMaxTicks(75);
				break;
			case 2:
				projectile.setLvl(2);
				projectile.setPower(6);
				projectile.setMaxBounces(2);
				projectile.setMaxTicks(90);
				break;
			case 3:
				projectile.setLvl(3);
				projectile.setPower(10);
				projectile.setMaxBounces(3);
				projectile.setMaxTicks(100);
				projectile.setExplosion(charged);
				break;
			case 4:
				projectile.setLvl(4);
				projectile.setPower(16);
				projectile.setMaxBounces(4);
				projectile.setMaxTicks(120);
				projectile.setExplosion(charged);
				break;
		}
		return projectile;
	}

	private static boolean stackEqualExact(ItemStack stack1, ItemStack stack2) {
		return stack1.getItem() == stack2.getItem() && ItemStack.isSameItemSameComponents(stack1, stack2);
	}

	public static void lockSelectedItem(Player player, ItemStack stack) {
		Inventory inventory = player.getInventory();
		int slot = Utils.getSlotFor(player, stack);

		// setSelectedSlot only accepts hotbar slots (0-8) and throws for any other slot.
		if (Inventory.isHotbarSlot(slot)) {
			inventory.setSelectedSlot(slot);
		} else if (slot >= 0 && slot < Inventory.INVENTORY_SIZE && !player.level().isClientSide()) {
			int selected = inventory.getSelectedSlot();
			ItemStack previous = inventory.getItem(selected);
			inventory.setItem(selected, inventory.getItem(slot));
			inventory.setItem(slot, previous);
		}
	}

	public static boolean tryPickUpBarrel(Player player, BlockPos pos) {
		Inventory inventory = player.getInventory();
		ItemStack barrel = new ItemStack(ModItems.BARREL.get());

		if (player.level().isClientSide() || getSlotFor(player, barrel) >= 0) {
			return false;
		}

		int slot = inventory.getSelectedItem().isEmpty() ? inventory.getSelectedSlot() : inventory.getFreeSlot();
		if (slot < 0) {
			return false;
		}

		inventory.setItem(slot, barrel);
		player.level().removeBlock(pos, false);
		lockSelectedItem(player, barrel);
		return true;
	}

	public static BlockPos getAvailablePos(Player player) {
		BlockPos pos = player.blockPosition();
		if (player.level().getBlockState(pos.north()).getBlock() == Blocks.AIR) {
			return pos.north();
		} else if (player.level().getBlockState(pos.east()).getBlock() == Blocks.AIR) {
			return pos.east();
		} else if (player.level().getBlockState(pos.south()).getBlock() == Blocks.AIR) {
			return pos.south();
		} else if (player.level().getBlockState(pos.west()).getBlock() == Blocks.AIR) {
			return pos.west();
		}
		return null;
	}

	public static SoundEvent getShootSound(Player player, boolean charged) {
		IPlayerCapabilities props = ModCapabilities.get(player);
		switch (props.getPlayerType()) {
			case Strings.RAYMAN:
				switch (props.getShotLevel()) {
					case 0:
					case 1:
						return ModSounds.FIST_SHOT_1.get();
					case 2:
						if (charged) return ModSounds.FIST_SHOT_2.get();
						return ModSounds.FIST_SHOT_1.get();
					case 3:
						if (charged) return ModSounds.FIST_GOLD_CHARGED.get();
						return ModSounds.FIST_SHOT_2.get();
				}
				break;
			case Strings.ROBO_PIRATE_GREEN:
				return charged ? ModSounds.PIRATE_LASER.get() : ModSounds.PIRATE_SHOT_1.get();
			case Strings.ROBO_PIRATE_RED:
				return charged ? ModSounds.PIRATE_LASER.get() : ModSounds.PIRATE_SHOT_2.get();
		}
		return null;
	}
}
