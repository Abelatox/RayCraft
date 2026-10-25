package com.abelatox.raycraft.client;

import com.abelatox.raycraft.client.sound.EntityLoopSound;
import com.abelatox.raycraft.events.BarrelFlight;
import com.abelatox.raycraft.items.ModItems;
import com.abelatox.raycraft.lib.Reference;
import com.abelatox.raycraft.network.payloads.SetBarrelFlightPayload;
import com.abelatox.raycraft.sounds.ModSounds;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

@EventBusSubscriber(modid = Reference.MODID, value = Dist.CLIENT)
public class BarrelFlightHandler {

	private static boolean jumpWasDown;
	private static EntityLoopSound fuseSound;

	@SubscribeEvent
	public static void onClientTick(ClientTickEvent.Post event) {
		Minecraft mc = Minecraft.getInstance();
		LocalPlayer player = mc.player;
		if (player == null) {
			fuseSound = null;
			return;
		}
		boolean holdingBarrel = ItemStack.isSameItemSameComponents(player.getMainHandItem(), new ItemStack(ModItems.BARREL.get()));

		if (!BarrelFlight.isFlying(player)) {
			if (holdingBarrel && touchesTorch(player)) {
				start(mc, player);
			}
			return;
		}

		boolean jump = mc.options.keyJump.isDown();
		if (!holdingBarrel || (jump && !jumpWasDown)) {
			end(player, true);
			return;
		}
		jumpWasDown = jump;
		Vec3 velocity = player.getLookAngle().scale(BarrelFlight.SPEED);
		player.setDeltaMovement(velocity);
		player.resetFallDistance();
		spawnFlames(player, velocity);

		if (player.horizontalCollision || player.verticalCollision || player.isInWater()) {
			end(player, false);
		}
	}

	private static void start(Minecraft mc, LocalPlayer player) {
		BarrelFlight.setFlying(player, true);
		jumpWasDown = mc.options.keyJump.isDown();
		ClientPacketDistributor.sendToServer(new SetBarrelFlightPayload(true, false));
		fuseSound = new EntityLoopSound(player, ModSounds.BOMB_LIVING.get());
		mc.getSoundManager().play(fuseSound);
	}

	private static void end(LocalPlayer player, boolean letGo) {
		BarrelFlight.setFlying(player, false);
		if (fuseSound != null) {
			fuseSound.stopNow();
			fuseSound = null;
		}
		ClientPacketDistributor.sendToServer(new SetBarrelFlightPayload(false, letGo));
	}

	private static boolean touchesTorch(LocalPlayer player) {
		AABB box = player.getBoundingBox().expandTowards(0, 1.0, 0).inflate(0.1);
		for (BlockPos pos : BlockPos.betweenClosed(BlockPos.containing(box.minX, box.minY, box.minZ), BlockPos.containing(box.maxX, box.maxY, box.maxZ))) {
			BlockState state = player.level().getBlockState(pos);
			if (!state.is(Blocks.TORCH) && !state.is(Blocks.WALL_TORCH)) {
				continue;
			}
			for (AABB part : state.getShape(player.level(), pos).toAabbs()) {
				if (part.move(pos).intersects(box)) {
					return true;
				}
			}
		}
		return false;
	}

	private static void spawnFlames(LocalPlayer player, Vec3 velocity) {
		Vec3 back = velocity.normalize().scale(-0.6);
		double x = player.getX() + back.x;
		double y = player.getY() + player.getBbHeight() + 0.4 + back.y;
		double z = player.getZ() + back.z;
		player.level().addParticle(ParticleTypes.FLAME, x, y, z, back.x * 0.2, back.y * 0.2, back.z * 0.2);
		player.level().addParticle(ParticleTypes.SMOKE, x, y, z, back.x * 0.1, 0.02, back.z * 0.1);
	}
}
