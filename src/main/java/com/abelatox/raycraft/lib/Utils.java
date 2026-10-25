package com.abelatox.raycraft.lib;

import com.abelatox.raycraft.capabilities.IPlayerCapabilities;
import com.abelatox.raycraft.capabilities.ModCapabilities;
import com.abelatox.raycraft.entities.EntityFist;
import com.abelatox.raycraft.entities.EntityPirateShot;
import com.abelatox.raycraft.entities.ModEntities;
import com.abelatox.raycraft.entities.RaycraftProjectile;
import com.abelatox.raycraft.items.ModItems;
import com.abelatox.raycraft.sounds.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;

public class Utils {

	public static Identifier getBarrelTexture() {
		return Identifier.fromNamespaceAndPath(Reference.MODID, "textures/models/barrel.png");
	}

	public static int getSlotFor(Player player, ItemStack stack) {
		for (int i = 0; i < player.getInventory().getContainerSize(); ++i) {
			ItemStack slotStack = player.getInventory().getItem(i);
			if (!slotStack.isEmpty() && ItemStack.isSameItemSameComponents(stack, slotStack)) {
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
		return switch (props.getPlayerType()) {
			case Strings.ROBO_PIRATE_RED -> new EntityPirateShot(ModEntities.PIRATE_SHOT_2.get(), player.level(), player);
			case Strings.ROBO_PIRATE_GREEN -> new EntityPirateShot(ModEntities.PIRATE_SHOT.get(), player.level(), player);
			case Strings.RAYMAN -> new EntityFist(player.level(), player, props.getShotLevel(), charged);
			default -> null;
		};
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
		if (!tryCarry(player, new ItemStack(ModItems.BARREL.get()))) {
			return false;
		}
		player.level().removeBlock(pos, false);
		return true;
	}

	public static boolean tryCarry(Player player, ItemStack carried) {
		Inventory inventory = player.getInventory();
		if (player.level().isClientSide() || getSlotFor(player, carried) >= 0) {
			return false;
		}

		int slot = inventory.getSelectedItem().isEmpty() ? inventory.getSelectedSlot() : inventory.getFreeSlot();
		if (slot < 0) {
			return false;
		}

		inventory.setItem(slot, carried);
		lockSelectedItem(player, carried);
		return true;
	}

	public static BlockPos getAvailablePos(Player player) {
		for (Direction direction : Direction.Plane.HORIZONTAL) {
			BlockPos pos = player.blockPosition().relative(direction);
			if (player.level().getBlockState(pos).isAir()) {
				return pos;
			}
		}
		return null;
	}

	public static SoundEvent getShootSound(Player player, boolean charged) {
		IPlayerCapabilities props = ModCapabilities.get(player);
		DeferredHolder<SoundEvent, SoundEvent> sound = switch (props.getPlayerType()) {
			case Strings.RAYMAN -> switch (props.getShotLevel()) {
				case 0, 1 -> ModSounds.FIST_SHOT_1;
				case 2 -> charged ? ModSounds.FIST_SHOT_2 : ModSounds.FIST_SHOT_1;
				default -> charged ? ModSounds.FIST_GOLD_CHARGED : ModSounds.FIST_SHOT_2;
			};
			case Strings.ROBO_PIRATE_GREEN -> charged ? ModSounds.PIRATE_LASER : ModSounds.PIRATE_SHOT_1;
			case Strings.ROBO_PIRATE_RED -> charged ? ModSounds.PIRATE_LASER : ModSounds.PIRATE_SHOT_2;
			default -> null;
		};
		return sound != null ? sound.get() : null;
	}
}
