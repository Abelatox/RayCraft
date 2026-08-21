package com.abelatox.raycraft.events;

import com.abelatox.raycraft.blocks.ModBlocks;
import com.abelatox.raycraft.capabilities.IPlayerCapabilities;
import com.abelatox.raycraft.capabilities.ModCapabilities;
import com.abelatox.raycraft.entities.EntityBarrel;
import com.abelatox.raycraft.items.ModItems;
import com.abelatox.raycraft.lib.Utils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.item.ItemTossEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

public class PlayerEventsHandler {

	boolean warned = false;

	@SubscribeEvent
	public void updatePlayerEvent(EntityTickEvent.Pre event) {
		if (event.getEntity() instanceof Player player) {
			IPlayerCapabilities props = ModCapabilities.get(player);
			if (props != null) {
				if (props.getIsGliding()) {
					player.setDeltaMovement(player.getDeltaMovement().x, -0.1, player.getDeltaMovement().z);
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

			// El barril es casi solido (getCollisionShape 1-15), asi que el jugador nunca
			// llega a solaparse de verdad con el bloque y BlockBarrel#entityInside no siempre
			// dispara. Revisamos por proximidad (agacharse "al lado" del barril) en vez de
			// depender de un solape exacto de hitboxes.
			if (player.isShiftKeyDown() && player.getMainHandItem().isEmpty() && !player.level().isClientSide()) {
				BlockPos center = player.blockPosition();
				for (BlockPos pos : BlockPos.betweenClosed(center.offset(-1, -1, -1), center.offset(1, 1, 1))) {
					if (player.level().getBlockState(pos).is(ModBlocks.BARREL.get())) {
						player.getInventory().setItem(player.getInventory().getSelectedSlot(), new ItemStack(ModItems.BARREL.get()));
						player.level().removeBlock(pos, true);
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
				}
			}
		}
	}

	@SubscribeEvent
	public void onItemTossEvent(ItemTossEvent event) {
		Player player = event.getPlayer();
		if (ItemStack.isSameItemSameComponents(event.getEntity().getItem(), new ItemStack(ModItems.BARREL.get()))) {
			event.setCanceled(true);
			if (Utils.getAvailablePos(player) == null) {
				if (!warned) {

					warned = true;
				}
			} else {
				player.getInventory().removeItemNoUpdate(player.getInventory().getSelectedSlot());
				player.level().setBlockAndUpdate(Utils.getAvailablePos(player), ModBlocks.BARREL.get().defaultBlockState());
				warned = false;
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
