package com.abelatox.raycraft.events;

import com.abelatox.raycraft.capabilities.IPlayerCapabilities;
import com.abelatox.raycraft.capabilities.ModCapabilities;
import com.abelatox.raycraft.lib.Reference;
import com.abelatox.raycraft.sounds.ModSounds;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@EventBusSubscriber(modid = Reference.MODID)
public class LumRewards {

	public static final int LUMS_PER_HEART = 50;
	private static final int MAX_BONUS_HEARTS = 10;
	private static final Identifier MODIFIER_ID = Identifier.fromNamespaceAndPath(Reference.MODID, "lum_health_bonus");

	public static int getBonusHearts(int lums) {
		return Math.min(MAX_BONUS_HEARTS, lums / LUMS_PER_HEART);
	}

	public static void apply(Player player) {
		AttributeInstance maxHealth = player.getAttribute(Attributes.MAX_HEALTH);
		if (maxHealth == null) {
			return;
		}
		int hearts = getBonusHearts(ModCapabilities.get(player).getLums());
		maxHealth.removeModifier(MODIFIER_ID);
		if (hearts > 0) {
			maxHealth.addOrUpdateTransientModifier(new AttributeModifier(MODIFIER_ID, hearts * 2.0, AttributeModifier.Operation.ADD_VALUE));
		}
	}

	public static void addLum(Player player) {
		IPlayerCapabilities props = ModCapabilities.get(player);
		int before = getBonusHearts(props.getLums());
		props.setLums(props.getLums() + 1);
		ModCapabilities.sync(player);

		if (getBonusHearts(props.getLums()) > before) {
			apply(player);
			player.heal(2.0F);
			player.sendOverlayMessage(Component.translatable("message.raycraft.lum_heart"));
			player.level().playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.CAGE_HEAL_BONUS.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
		}
	}

	@SubscribeEvent
	public static void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
		apply(event.getEntity());
	}

	@SubscribeEvent
	public static void onRespawn(PlayerEvent.PlayerRespawnEvent event) {
		apply(event.getEntity());
	}
}
