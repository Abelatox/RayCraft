package com.abelatox.raycraft.events;

import com.abelatox.raycraft.blocks.ModBlocks;
import com.abelatox.raycraft.capabilities.IPlayerCapabilities;
import com.abelatox.raycraft.capabilities.ModCapabilities;
import com.abelatox.raycraft.entities.EntityBarrel;
import com.abelatox.raycraft.items.ModItems;
import com.abelatox.raycraft.lib.Utils;
import com.abelatox.raycraft.sounds.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.item.ItemTossEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

public class PlayerEventsHandler {

	@SubscribeEvent
	public void updatePlayerEvent(EntityTickEvent.Pre event) {
		if (event.getEntity() instanceof Player player) {
			IPlayerCapabilities props = ModCapabilities.get(player);
			if (props != null) {
				if (props.getIsGliding()) {
					player.setDeltaMovement(player.getDeltaMovement().x, -0.1, player.getDeltaMovement().z);
					player.resetFallDistance();
				}

				if (ItemStack.isSameItemSameComponents(player.getMainHandItem(), new ItemStack(ModItems.BARREL.get()))) {
					if (player.getDeltaMovement().y <= 0) {
						player.setDeltaMovement(new Vec3(player.getDeltaMovement().x / 2, player.getDeltaMovement().y * 2, player.getDeltaMovement().z / 2));
					} else {
						player.setDeltaMovement(new Vec3(player.getDeltaMovement().x / 2, player.getDeltaMovement().y, player.getDeltaMovement().z / 2));
					}
				}

				if (player.getInventory().contains(new ItemStack(ModItems.BARREL.get()))) {
					Utils.lockSelectedItem(player, new ItemStack(ModItems.BARREL.get()));
				}
			}

			// The barrel is almost solid (getCollisionShape 1-15), so the player never really
			// overlaps the block and BlockBarrel#entityInside doesn't always fire. Check by
			// proximity (crouching next to the barrel) instead of relying on an exact hitbox overlap.
			if (player.isShiftKeyDown() && !player.level().isClientSide()) {
				BlockPos center = player.blockPosition();
				for (BlockPos pos : BlockPos.betweenClosed(center.offset(-1, -1, -1), center.offset(1, 1, 1))) {
					if (player.level().getBlockState(pos).is(ModBlocks.BARREL.get())) {
						Utils.tryPickUpBarrel(player, pos.immutable());
						break;
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onPlayerJump(LivingEvent.LivingJumpEvent event) {
		if (event.getEntity() instanceof Player player) {
			int slot = Utils.getSlotFor(player, new ItemStack(ModItems.BARREL.get()));
			if (slot > -1) {
				player.getInventory().removeItemNoUpdate(slot);
				if (!player.level().isClientSide()) {
					EntityBarrel barrel = new EntityBarrel(player.level(), player);
					player.level().addFreshEntity(barrel);
					barrel.setOwner(player);
					barrel.shootFromRotation(player, -90, player.getYRot(), 0, 1F, 0);
					player.level().playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.BOMB_THROW.get(), SoundSource.PLAYERS, 1F, 1F);
				}
			}
		}
	}

	@SubscribeEvent
	public void onItemTossEvent(ItemTossEvent event) {
		Player player = event.getPlayer();
		if (ItemStack.isSameItemSameComponents(event.getEntity().getItem(), new ItemStack(ModItems.BARREL.get()))) {
			event.setCanceled(true);
			BlockPos pos = Utils.getAvailablePos(player);
			if (pos != null) {
				player.getInventory().removeItemNoUpdate(player.getInventory().getSelectedSlot());
				player.level().setBlockAndUpdate(pos, ModBlocks.BARREL.get().defaultBlockState());
			}
		}
	}

	@SubscribeEvent
	public void playerStartedTracking(PlayerEvent.StartTracking e) {
		if (e.getTarget() instanceof Player targetPlayer) {
			ModCapabilities.sync(targetPlayer);
		}
	}
}
