package com.abelatox.raycraft.entities;

import com.abelatox.raycraft.events.LumRewards;
import com.abelatox.raycraft.items.ModItems;
import com.abelatox.raycraft.sounds.ModSounds;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.List;

public class EntityLum extends Entity {

	private static final int BLUE_LUM_BUBBLES = 3;
	private static final int AIR_PER_BUBBLE = 30;

	public static final String PURPLE = "purple";
	public static final List<String> TYPES = List.of("yellow", "green", "red", "blue", PURPLE);

	public static int getColor(String lumType) {
		return switch (lumType) {
			case "yellow" -> 0xFFFFE040;
			case "green" -> 0xFF50FF50;
			case "red" -> 0xFFFF3A3A;
			case "blue" -> 0xFF00c4f5;
			case PURPLE -> 0xFFB45CFF;
			default -> 0xFFFFFFFF;
		};
	}

	private static final EntityDataAccessor<String> TYPE = SynchedEntityData.defineId(EntityLum.class, EntityDataSerializers.STRING);

	public EntityLum(EntityType<? extends EntityLum> type, Level level) {
		super(type, level);
	}

	public EntityLum(Level level) {
		this(ModEntities.LUM.get(), level);
	}

	@Override
	public void tick() {
		/*switch (getLumType()) {
			case "yellow" -> level().addParticle(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, 1F, 1F, 0F), getX(), getY(), getZ(), 0, 0, 0);
			case "green" -> level().addParticle(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, 0F, 1F, 0F), getX(), getY(), getZ(), 0, 0, 0);
			case "red" -> level().addParticle(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, 1F, 0F, 0F), getX(), getY(), getZ(), 0, 0, 0);
			case "blue" -> level().addParticle(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, 0F, 0F, 1F), getX(), getY(), getZ(), 0, 0, 0);
		}*/

		if (!TYPES.contains(getLumType())) {
			discard();
		}
		super.tick();
	}

	@Override
	public void playerTouch(Player player) {
		if (this.tickCount > 20 && !isPurple()) {
			if (!level().isClientSide()) {
				switch (getLumType()) {
					case "yellow" -> {
						LumRewards.addLum(player);
						level().playSound(null, player.blockPosition(), ModSounds.LUM_YELLOW.get(), SoundSource.MASTER, 1F, 1F);
					}
					case "green" -> {
						level().playSound(null, player.blockPosition(), ModSounds.LUM_GREEN.get(), SoundSource.MASTER, 1F, 1F);
						if (player instanceof ServerPlayer serverPlayer) {
							LevelData.RespawnData respawnData = LevelData.RespawnData.of(level().dimension(), blockPosition(), player.getYRot(), 0F);
							serverPlayer.setRespawnPosition(new ServerPlayer.RespawnConfig(respawnData, true), true);
						}
					}
					case "red" -> {
						level().playSound(null, player.blockPosition(), ModSounds.LUM_RED.get(), SoundSource.MASTER, 1F, 1F);
						player.heal(2);
					}
					case "blue" -> {
						player.setAirSupply(Math.min(player.getMaxAirSupply(), player.getAirSupply() + BLUE_LUM_BUBBLES * AIR_PER_BUBBLE));
						level().playSound(null, player.blockPosition(), SoundEvents.BUBBLE_COLUMN_BUBBLE_POP, SoundSource.MASTER, 1F, 1F);
					}
				}
			}
			discard();
		}
		super.playerTouch(player);
	}

	/**
	 * Every Lum can be targeted, so one can be hit with the Lum spawner to remove it.
	 */
	@Override
	public boolean isPickable() {
		return true;
	}

	@Override
	public boolean canBeHitByProjectile() {
		return isPurple();
	}

	public boolean isPurple() {
		return PURPLE.equals(getLumType());
	}

	public String getLumType() {
		return this.getEntityData().get(TYPE);
	}

	public void setLumType(String type) {
		this.getEntityData().set(TYPE, type);
	}

	@Override
	protected void addAdditionalSaveData(ValueOutput output) {
		output.putString("type", this.getLumType());
	}

	@Override
	protected void readAdditionalSaveData(ValueInput input) {
		this.setLumType(input.getStringOr("type", "yellow"));
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		builder.define(TYPE, "yellow");
	}

	@Override
	public boolean hurtServer(ServerLevel level, DamageSource damageSource, float amount) {
		if (damageSource.getEntity() instanceof Player player && player.getMainHandItem().is(ModItems.LUM_SPAWNER.get())) {
			discard();
			return true;
		}
		return false;
	}
}
