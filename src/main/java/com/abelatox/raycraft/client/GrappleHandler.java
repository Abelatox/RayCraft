package com.abelatox.raycraft.client;

import com.abelatox.raycraft.entities.EntityLum;
import com.abelatox.raycraft.lib.Reference;
import com.abelatox.raycraft.network.payloads.SetSwingingPayload;
import com.abelatox.raycraft.sounds.ModSounds;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

@EventBusSubscriber(modid = Reference.MODID, value = Dist.CLIENT)
public class GrappleHandler {

	public static final double GRAPPLE_RANGE = 32.0;
	private static final double MIN_ROPE = 3.0;
	private static final double MAX_ROPE = 8.0;

	private static final double ROPE_SHORTEN = 0.8;
	private static final double SWING_AMPLITUDE = Math.toRadians(90.0);
	private static final double SWING_PERIOD_TICKS = 60.0;
	private static final double RELEASE_BOOST = 0.45;
	private static final double STEER = 0.12;

	private static EntityLum anchor;
	private static double ropeLength;
	private static Vec3 swingDir;
	private static double phase;
	private static boolean jumpWasDown;

	public static boolean isSwinging() {
		return anchor != null;
	}

	public static EntityLum getAnchor() {
		return anchor;
	}

	public static Vec3 anchorPoint(EntityLum lum, float partialTick) {
		return lum.getPosition(partialTick).add(0, lum.getBbHeight() * 0.5, 0);
	}

	public static Vec3 ropeOrigin(Player player, float partialTick) {
		return player.getPosition(partialTick).add(0, ropeOffset(player), 0);
	}

	private static double ropeOffset(Player player) {
		return player.getBbHeight() * 0.85;
	}

	public static void startFromServer(int lumId) {
		LocalPlayer player = Minecraft.getInstance().player;
		if (player != null && player.level().getEntity(lumId) instanceof EntityLum lum && lum.isPurple()) {
			tryStart(player, lum);
		}
	}

	public static boolean tryStart(LocalPlayer player, EntityLum lum) {
		Vec3 fromAnchor = ropeOrigin(player, 1.0F).subtract(anchorPoint(lum, 1.0F));
		double distance = fromAnchor.length();
		if (distance > GRAPPLE_RANGE) {
			return false;
		}

		Vec3 horizontal = new Vec3(fromAnchor.x, 0, fromAnchor.z);
		if (horizontal.lengthSqr() < 0.01) {
			Vec3 look = player.getViewVector(1.0F);
			horizontal = new Vec3(-look.x, 0, -look.z);
		}
		swingDir = horizontal.lengthSqr() < 1.0E-6 ? new Vec3(1, 0, 0) : horizontal.normalize();

		ropeLength = Mth.clamp(distance * ROPE_SHORTEN, MIN_ROPE, MAX_ROPE);
		// Start the arc where the player hangs now, so the hook doesn't snap them across.
		double angle = Math.atan2(horizontal.length(), -fromAnchor.y);
		phase = Math.asin(Mth.clamp(angle / SWING_AMPLITUDE, -1.0, 1.0));

		anchor = lum;
		jumpWasDown = Minecraft.getInstance().options.keyJump.isDown();
		ClientPacketDistributor.sendToServer(new SetSwingingPayload(true));
		player.level().playLocalSound(player.getX(), player.getY(), player.getZ(), ModSounds.FIST_SHOT_1.get(), SoundSource.PLAYERS, 1.0F, 1.2F, false);
		return true;
	}

	private static void stop(LocalPlayer player, boolean jumpOff) {
		anchor = null;
		if (player != null) {
			if (jumpOff) {
				player.setDeltaMovement(player.getDeltaMovement().add(0, RELEASE_BOOST, 0));
			}
			ClientPacketDistributor.sendToServer(new SetSwingingPayload(false));
		}
	}

	private static void steerTowardsLook(LocalPlayer player) {
		Vec3 look = player.getViewVector(1.0F);
		Vec3 target = new Vec3(look.x, 0, look.z);
		if (target.lengthSqr() < 1.0E-6) {
			return;
		}
		target = target.normalize();
		if (target.dot(swingDir) < 0) {
			target = target.scale(-1);
		}
		Vec3 turned = swingDir.add(target.subtract(swingDir).scale(STEER));
		if (turned.lengthSqr() > 1.0E-6) {
			swingDir = turned.normalize();
		}
	}

	@SubscribeEvent
	public static void onClientTick(ClientTickEvent.Post event) {
		if (anchor == null) {
			return;
		}
		Minecraft mc = Minecraft.getInstance();
		LocalPlayer player = mc.player;
		if (player == null) {
			anchor = null;
			return;
		}
		if (anchor.isRemoved() || anchor.level() != player.level() || player.isInWater() || player.getAbilities().flying) {
			stop(player, false);
			return;
		}

		boolean jump = mc.options.keyJump.isDown();
		if (jump && !jumpWasDown) {
			stop(player, true);
			return;
		}
		jumpWasDown = jump;

		if (player.input.keyPresses.forward()) {
			steerTowardsLook(player);
		}

		phase += Mth.TWO_PI / SWING_PERIOD_TICKS;
		double angle = SWING_AMPLITUDE * Math.sin(phase);
		Vec3 arcPoint = anchorPoint(anchor, 1.0F)
				.add(swingDir.scale(Math.sin(angle) * ropeLength))
				.add(0, -Math.cos(angle) * ropeLength - ropeOffset(player), 0);

		// Velocity that reaches the arc point on the next movement step.
		player.setDeltaMovement(arcPoint.subtract(player.position()));
		player.resetFallDistance();
	}
}
