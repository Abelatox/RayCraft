package com.abelatox.raycraft.events;

import com.abelatox.raycraft.blocks.ModBlocks;
import com.abelatox.raycraft.capabilities.IPlayerCapabilities;
import com.abelatox.raycraft.capabilities.ModCapabilities;
import com.abelatox.raycraft.entities.EntityBarrel;
import com.abelatox.raycraft.entities.EntityPlum;
import com.abelatox.raycraft.entities.EntityWindCurrent;
import com.abelatox.raycraft.items.ModItems;
import com.abelatox.raycraft.lib.Utils;
import com.abelatox.raycraft.sounds.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityMountEvent;
import net.neoforged.neoforge.event.entity.item.ItemTossEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

public class PlayerEventsHandler {

	private static final double STILL_SQR = 1.0E-4;
	private static final double PLUM_REACH = 0.5;
	private static final double PLUM_JUMP_THROW_SPEED = 0.9;
	private static final double WIND_LIFT = 0.35;

	@SubscribeEvent
	public void updatePlayerEvent(EntityTickEvent.Pre event) {
		if (event.getEntity() instanceof Player player) {
			IPlayerCapabilities props = ModCapabilities.get(player);
			if (props != null) {
				if (props.getIsGliding()) {
					double fall = isInWindCurrent(player) ? WIND_LIFT : -0.1;
					player.setDeltaMovement(player.getDeltaMovement().x, fall, player.getDeltaMovement().z);
					player.resetFallDistance();
				}

				ItemStack held = player.getMainHandItem();
				boolean carrying = (held.is(ModItems.BARREL.get()) && !BarrelFlight.isFlying(player)) || held.is(ModItems.PLUM.get());
				if (carrying) {
					if (player.getDeltaMovement().y <= 0) {
						player.setDeltaMovement(new Vec3(player.getDeltaMovement().x / 2, player.getDeltaMovement().y * 2, player.getDeltaMovement().z / 2));
					} else {
						player.setDeltaMovement(new Vec3(player.getDeltaMovement().x / 2, player.getDeltaMovement().y, player.getDeltaMovement().z / 2));
					}
				}

				if (player.getInventory().contains(new ItemStack(ModItems.BARREL.get()))) {
					Utils.lockSelectedItem(player, new ItemStack(ModItems.BARREL.get()));
				} else if (player.getInventory().contains(new ItemStack(ModItems.PLUM.get()))) {
					Utils.lockSelectedItem(player, new ItemStack(ModItems.PLUM.get()));
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
				if (!player.isPassenger() && player.getKnownMovement().horizontalDistanceSqr() < STILL_SQR) {
					for (EntityPlum plum : player.level().getEntitiesOfClass(EntityPlum.class, player.getBoundingBox().inflate(PLUM_REACH))) {
						if (plum.tryPickUp(player)) {
							break;
						}
					}
				}
			}
		}
	}

	private static boolean isInWindCurrent(Player player) {
		AABB search = player.getBoundingBox().inflate(EntityWindCurrent.MAX_RADIUS, 0.0, EntityWindCurrent.MAX_RADIUS).expandTowards(0.0, -EntityWindCurrent.MAX_HEIGHT, 0.0);
		return !player.level().getEntitiesOfClass(EntityWindCurrent.class, search, wind -> wind.catches(player)).isEmpty();
	}

	@SubscribeEvent
	public void onPlayerJump(LivingEvent.LivingJumpEvent event) {
		if (event.getEntity() instanceof Player player) {
			int slot = Utils.getSlotFor(player, new ItemStack(ModItems.BARREL.get()));
			if (slot > -1 && !BarrelFlight.isFlying(player)) {
				player.getInventory().removeItemNoUpdate(slot);
				if (!player.level().isClientSide()) {
					EntityBarrel barrel = new EntityBarrel(player.level(), player);
					player.level().addFreshEntity(barrel);
					barrel.setOwner(player);
					barrel.shootFromRotation(player, -90, player.getYRot(), 0, 1F, 0);
					player.level().playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.BOMB_THROW.get(), SoundSource.PLAYERS, 1F, 1F);
				}
				return;
			}
			slot = Utils.getSlotFor(player, new ItemStack(ModItems.PLUM.get()));
			if (slot > -1) {
				player.getInventory().removeItemNoUpdate(slot);
				if (!player.level().isClientSide()) {
					EntityPlum plum = new EntityPlum(player.level(), player);
					plum.setDeltaMovement(player.getDeltaMovement().add(0, PLUM_JUMP_THROW_SPEED, 0));
					player.level().addFreshEntity(plum);
				}
			}
		}
	}

	@SubscribeEvent
	public void onPlumRiderTick(EntityTickEvent.Post event) {
		if (event.getEntity() instanceof Player player && player.getVehicle() instanceof EntityPlum && player.isOnFire()) {
			player.clearFire();
		}
	}

	/**
	 * Plums are only left by jumping off: crouching doesn't dismount. Other dismounts (death, teleports...) still
	 * happen, except in the middle of a lava pool with nowhere to get off to. Server only: the client just follows.
	 */
	@SubscribeEvent
	public void onPlumDismount(EntityMountEvent event) {
		if (event.getLevel().isClientSide() || !event.isDismounting() || !(event.getEntityBeingMounted() instanceof EntityPlum plum)
				|| !(event.getEntityMounting() instanceof Player rider) || plum.isRemoved() || !rider.isAlive() || plum.isJumpingOff()) {
			return;
		}
		if (rider.isShiftKeyDown() || (plum.isInLavaPool() && plum.findSafeDismount(rider) == null)) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public void onPlumRiderHurt(LivingIncomingDamageEvent event) {
		if (event.getEntity().getVehicle() instanceof EntityPlum && event.getSource().is(DamageTypeTags.IS_FIRE)) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public void onItemTossEvent(ItemTossEvent event) {
		Player player = event.getPlayer();
		if (event.getEntity().getItem().is(ModItems.PLUM.get())) {
			event.setCanceled(true);
			player.getInventory().removeItemNoUpdate(player.getInventory().getSelectedSlot());
			if (!player.level().isClientSide()) {
				EntityPlum plum = new EntityPlum(player.level(), player);
				plum.setDeltaMovement(player.getDeltaMovement());
				player.level().addFreshEntity(plum);
			}
		} else if (ItemStack.isSameItemSameComponents(event.getEntity().getItem(), new ItemStack(ModItems.BARREL.get()))) {
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
