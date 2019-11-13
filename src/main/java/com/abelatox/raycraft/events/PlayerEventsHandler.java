package com.abelatox.raycraft.events;

import org.lwjgl.glfw.GLFW;

import com.abelatox.raycraft.blocks.ModBlocks;
import com.abelatox.raycraft.capabilities.IPlayerCapabilities;
import com.abelatox.raycraft.capabilities.ModCapabilities;
import com.abelatox.raycraft.client.KeyboardHelper;
import com.abelatox.raycraft.entities.EntityBarrel;
import com.abelatox.raycraft.items.ModItems;
import com.abelatox.raycraft.lib.Utils;
import com.abelatox.raycraft.network.PacketHandler;
import com.abelatox.raycraft.network.packets.PacketSetTarget;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PlayerEventsHandler {

	long timeLastAlert;
	boolean warned = false;

	@SubscribeEvent
	public void onAttack(LivingHurtEvent event) {
		if (event.getSource().getTrueSource() instanceof PlayerEntity) {
			PacketHandler.sendTo(new PacketSetTarget(event.getEntityLiving().getEntityId()), (ServerPlayerEntity)event.getSource().getTrueSource());
			System.out.println(event.getEntityLiving().world.isRemote + " asd " + event.getSource().getTrueSource());
		}
	}

	@SubscribeEvent
	public void updatePlayerEvent(LivingUpdateEvent event) {
		if (event.getEntityLiving() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) event.getEntityLiving();
			if (player.world.isRemote && !player.onGround && player.getMotion().y < 0 && KeyboardHelper.isKeyDown(GLFW.GLFW_KEY_SPACE)) {
				System.out.println(event.getEntity().world.isRemote);
				// if(player.world.isRemote && Minecraft.getInstance().) {
				player.setMotion(player.getMotion().x, -0.1, player.getMotion().z);
				// }
			}
			IPlayerCapabilities props = ModCapabilities.get(player);
			if (props != null) {
				// Slow down the player while holding a barrel
				if (ItemStack.areItemStacksEqual(player.getHeldItemMainhand(), new ItemStack(ModItems.barrel))) {
					// if (props.getCarrying().equals("barrel")) {
					if (player.getMotion().y <= 0) {
						player.setMotion(new Vec3d(player.getMotion().x / 2, player.getMotion().y * 2, player.getMotion().z / 2));
					} else {
						player.setMotion(new Vec3d(player.getMotion().x / 2, player.getMotion().y, player.getMotion().z / 2));

					}

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
		if (event.getEntityLiving() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) event.getEntityLiving();
			int slot = Utils.getSlotFor(player, new ItemStack(ModItems.barrel));
			if (slot > -1) {
				player.inventory.removeStackFromSlot(slot);
				if (!player.world.isRemote) {
					EntityBarrel barrel = new EntityBarrel(player.world, player);
					player.world.addEntity(barrel);
					barrel.shoot(player, -90, player.rotationYaw, 0, 1f, 0);
					// System.out.println("throwing up barrelino");
					player.inventory.removeStackFromSlot(player.inventory.currentItem);
				}
			}

		}
	}

	@SubscribeEvent
	public void onPlayerClone(PlayerEvent.Clone event) {
		if (event.isWasDeath()) {
			updateCap(event.getOriginal(), event.getPlayer());
		}
	}

	private void updateCap(PlayerEntity original, PlayerEntity player) {
		// System.out.println(original + "\n" + player);
		IPlayerCapabilities oProps = ModCapabilities.get(original);
		IPlayerCapabilities props = ModCapabilities.get(player);
		props.setPlayerType(oProps.getPlayerType());
		props.setShotLevel(oProps.getShotLevel());
		props.setCharging(oProps.getCharging());
		props.setLums(oProps.getLums());

		// System.out.println(oProps + " " + props);
	}

	@SubscribeEvent
	public void onItemTossEvent(ItemTossEvent event) { // If tosses the barrel it will try to drop
		PlayerEntity player = event.getPlayer();
		if (ItemStack.areItemStacksEqual(event.getEntityItem().getItem(), new ItemStack(ModItems.barrel))) {
			event.setCanceled(true);
			if (Utils.getAvailablePos(player) == null) {
				if (!warned) {
					player.sendMessage(new TranslationTextComponent("You can't drop the barrel here"));
					warned = true;
				}
			} else {
				player.inventory.removeStackFromSlot(player.inventory.currentItem);
				player.world.setBlockState(Utils.getAvailablePos(player), ModBlocks.barrel.getDefaultState());
				warned = false;
			}
		}
	}

	@SubscribeEvent
	public void playerStartedTracking(PlayerEvent.StartTracking e) {
		if (e.getTarget() instanceof PlayerEntity) {
			PlayerEntity targetPlayer = (PlayerEntity) e.getTarget();
			IPlayerCapabilities props = ModCapabilities.get(targetPlayer);
			PacketHandler.syncToAllAround(targetPlayer, props);
		}
	}

	/*
	 * @SubscribeEvent public void EntityAttack(LivingAttackEvent event) { if
	 * (event.getSource().getImmediateSource() != null &&
	 * event.getSource().getImmediateSource() instanceof PlayerEntity) {
	 * PlayerEntity player = (PlayerEntity) event.getSource().getImmediateSource();
	 * IPlayerCapabilities props = ModCapabilities.get(player); } }
	 */
}
