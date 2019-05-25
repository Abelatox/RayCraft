package com.abelatox.raycraft.events;

import com.abelatox.raycraft.blocks.ModBlocks;
import com.abelatox.raycraft.capabilities.IPlayerModelCapability;
import com.abelatox.raycraft.capabilities.ModCapabilities;
import com.abelatox.raycraft.entities.EntityBarrel;
import com.abelatox.raycraft.items.ModItems;
import com.abelatox.raycraft.lib.Utils;
import com.abelatox.raycraft.network.PacketHandler;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PlayerEventsHandler {

	long timeLastAlert;
	boolean warned = false;

	@SubscribeEvent
	public void updatePlayerEvent(LivingUpdateEvent event) {
		if (event.getEntityLiving() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			IPlayerModelCapability props = ModCapabilities.get(player);
			if (props != null) {
				// Slow down the player while holding a barrel
				if (ItemStack.areItemStacksEqual(player.getHeldItemMainhand(), new ItemStack(ModItems.barrel))) {
					// if (props.getCarrying().equals("barrel")) {
					player.motionX /= 2;
					if (player.motionY <= 0)
						player.motionY *= 2;
					player.motionZ /= 2;

					// If sneaking drop the barrel as block
					/*if (player.isSneaking()) {
						if (Utils.getAvailablePos(player) == null) {
							if (!warned) {
								player.sendMessage(new TextComponentString("You can't drop the barrel here"));
								warned = true;
							}
						} else {
							player.inventory.removeStackFromSlot(player.inventory.currentItem);
							// props.setCarrying("null");
							player.world.setBlockState(Utils.getAvailablePos(player), ModBlocks.barrel.getDefaultState());
							// PacketHandler.sendToAllAround(player, props);
						}
					} else {
						warned = false;

						// System.out.println("attack");
					}*/
				}

				// Prevent the player from changing item while holding a barrel
				if (player.inventory.hasItemStack(new ItemStack(ModItems.barrel))) {
					Utils.lockSelectedItem(player, new ItemStack(ModItems.barrel));
				}
			}
		}
	}
	


	@SubscribeEvent
	public void onPlayerJump(LivingJumpEvent event) {
		if(event.getEntityLiving() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			int slot = Utils.getSlotFor(player, new ItemStack(ModItems.barrel));
			if(slot > -1) {
				player.inventory.removeStackFromSlot(slot);
				if (!player.world.isRemote) {
					EntityBarrel barrel = new EntityBarrel(player.world, player);
					player.world.spawnEntity(barrel);
					barrel.shoot(player, -90, player.rotationYaw, 0, 1f, 0);
					System.out.println("throwing up barrelino");
					player.inventory.removeStackFromSlot(player.inventory.currentItem);
				}
			}
			
		}
	}

	@SubscribeEvent
	public void onPlayerClone(PlayerEvent.Clone event) {
		updateCap(event.getOriginal(), event.getEntityPlayer());

		/*
		 * if (event.isWasDeath()) { updateDeath(event.getOriginal(),
		 * event.getEntityPlayer()); }
		 */

	}

	private void updateCap(EntityPlayer original, EntityPlayer player) {
		System.out.println(original + "\n" + player);
		LazyOptional<IPlayerModelCapability> oProps = original.getCapability(ModCapabilities.PLAYER_MODEL);
		LazyOptional<IPlayerModelCapability> props = player.getCapability(ModCapabilities.PLAYER_MODEL);
		System.out.println(oProps + " " + props);
	}

	@SubscribeEvent
	public void onItemTossEvent(ItemTossEvent event) { // If tosses the barrel it will try to drop
		EntityPlayer player = event.getPlayer();
		if (ItemStack.areItemStacksEqual(event.getEntityItem().getItem(), new ItemStack(ModItems.barrel))) {
			event.setCanceled(true);
			if (Utils.getAvailablePos(player) == null) {
				if (!warned) {
					player.sendMessage(new TextComponentString("You can't drop the barrel here"));
					warned = true;
				}
			} else {
				player.inventory.removeStackFromSlot(player.inventory.currentItem);
				// props.setCarrying("null");
				player.world.setBlockState(Utils.getAvailablePos(player), ModBlocks.barrel.getDefaultState());
				// PacketHandler.sendToAllAround(player, props);
			}
		}
	}

	@SubscribeEvent
	public void playerStartedTracking(PlayerEvent.StartTracking e) {
		if (e.getTarget() instanceof EntityPlayer) {
			EntityPlayer targetPlayer = (EntityPlayer) e.getTarget();
			IPlayerModelCapability props = ModCapabilities.get(targetPlayer);
			PacketHandler.sendToAllAround(targetPlayer, props);
		}
	}

	@SubscribeEvent
	public void EntityAttack(LivingAttackEvent event) {
		if (event.getSource().getImmediateSource() != null && event.getSource().getImmediateSource() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getSource().getImmediateSource();
			IPlayerModelCapability props = ModCapabilities.get(player);

			if (props.getModel().equals("robopirate")) {
				props.setModel("robopirate2");
			} else if (props.getModel().equals("robopirate2")) {
				props.setModel("rayman");
			} else {
				props.setModel("robopirate");
			}

			PacketHandler.sendToAllAround(player, props);
		}
	}
}
