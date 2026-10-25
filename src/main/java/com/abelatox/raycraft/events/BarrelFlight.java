package com.abelatox.raycraft.events;

import com.abelatox.raycraft.entities.EntityBarrel;
import com.abelatox.raycraft.items.ModItems;
import com.abelatox.raycraft.lib.Reference;
import com.abelatox.raycraft.lib.Utils;
import com.abelatox.raycraft.sounds.ModSounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

@EventBusSubscriber(modid = Reference.MODID)
public class BarrelFlight {
	public static final double SPEED = 0.7;
	private static final Set<Player> FLYING = Collections.newSetFromMap(new WeakHashMap<>());

	public static boolean isFlying(Player player) {
		return FLYING.contains(player);
	}

	public static void setFlying(Player player, boolean flying) {
		if (flying) {
			FLYING.add(player);
		} else {
			FLYING.remove(player);
		}
	}

	public static void onFlightPayload(Player player, boolean flying, boolean letGo) {
		if (!(player.level() instanceof ServerLevel level)) {
			return;
		}
		ItemStack barrelItem = new ItemStack(ModItems.BARREL.get());
		if (flying) {
			if (ItemStack.isSameItemSameComponents(player.getMainHandItem(), barrelItem)) {
				setFlying(player, true);
				level.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.BURN.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
			}
			return;
		}
		if (!isFlying(player)) {
			return;
		}
		setFlying(player, false);
		int slot = Utils.getSlotFor(player, barrelItem);
		if (slot >= 0) {
			player.getInventory().removeItemNoUpdate(slot);
		}
		EntityBarrel barrel = new EntityBarrel(level, player);
		barrel.setPos(player.getX(), player.getY() + player.getBbHeight() + 0.3, player.getZ());
		level.addFreshEntity(barrel);
		if (letGo) {
			barrel.light(player.getLookAngle().scale(SPEED));
		} else {
			barrel.explode(level);
		}
	}

	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		if (isFlying(event.getEntity())) {
			event.getEntity().resetFallDistance();
		}
	}
}
