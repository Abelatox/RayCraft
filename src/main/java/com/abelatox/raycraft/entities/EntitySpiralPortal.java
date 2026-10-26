package com.abelatox.raycraft.entities;

import com.abelatox.raycraft.particles.ModParticles;

import com.abelatox.raycraft.network.payloads.OpenPortalConfigPayload;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

public class EntitySpiralPortal extends Entity {
	private static final EntityDataAccessor<Boolean> RED = SynchedEntityData.defineId(EntitySpiralPortal.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> FLAT = SynchedEntityData.defineId(EntitySpiralPortal.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Float> SIZE = SynchedEntityData.defineId(EntitySpiralPortal.class, EntityDataSerializers.FLOAT);
	public static final float MIN_SIZE = 1.0F;
	public static final float MAX_SIZE = 10.0F;
	private static final float DEFAULT_SIZE = 2.0F;
	private static final float FLAT_HEIGHT = 0.5F;
	private static final double STAR_RANGE = 2.0;
	private static final int STARS_PER_TICK = 5;
	private static final double BODY_MARGIN = 0.15;
	private static final double STAR_TRAVEL_TICKS = 10.0;

	private Vec3 destination;
	private ResourceKey<Level> destinationLevel = Level.OVERWORLD;

	public EntitySpiralPortal(EntityType<? extends EntitySpiralPortal> type, Level level) {
		super(type, level);
		setNoGravity(true);
	}

	public void setDestination(Vec3 destination, ResourceKey<Level> level) {
		this.destination = destination;
		this.destinationLevel = level;
	}

	public boolean isRed() {
		return getEntityData().get(RED);
	}

	public void setRed(boolean red) {
		getEntityData().set(RED, red);
	}

	public boolean isFlat() {
		return getEntityData().get(FLAT);
	}

	public void setFlat(boolean flat) {
		getEntityData().set(FLAT, flat);
	}

	/** Blocks across the swirl. */
	public float getSize() {
		return getEntityData().get(SIZE);
	}

	public void setSize(float size) {
		getEntityData().set(SIZE, Mth.clamp(size, MIN_SIZE, MAX_SIZE));
	}

	@Override
	public EntityDimensions getDimensions(Pose pose) {
		return EntityDimensions.fixed(getSize(), isFlat() ? FLAT_HEIGHT : getSize());
	}

	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
		super.onSyncedDataUpdated(accessor);
		if (FLAT.equals(accessor) || SIZE.equals(accessor)) {
			refreshDimensions();
		}
	}

	@Override
	public void tick() {
		super.tick();
		if (level().isClientSide()) {
			pullStars();
		}
		if (destination == null || !(level() instanceof ServerLevel serverLevel)) {
			return;
		}
		ServerLevel target = serverLevel.getServer().getLevel(destinationLevel);
		if (target == null) {
			return;
		}
		for (LivingEntity entity : level().getEntitiesOfClass(LivingEntity.class, getBoundingBox(), e -> !e.isPassenger() && !e.isOnPortalCooldown())) {
			level().playSound(null, getX(), getY(), getZ(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.NEUTRAL, 1.0F, 1.2F);
			Entity arrived = entity.teleport(new TeleportTransition(target, destination, Vec3.ZERO, entity.getYRot(), entity.getXRot(), TeleportTransition.DO_NOTHING));
			if (arrived != null) {
				arrived.setPortalCooldown();
				target.playSound(null, destination.x, destination.y, destination.z, SoundEvents.ENDERMAN_TELEPORT, SoundSource.NEUTRAL, 1.0F, 1.2F);
			}
		}
	}

	private void pullStars() {
		Vec3 centre = getBoundingBox().getCenter();
		for (Player player : level().getEntitiesOfClass(Player.class, getBoundingBox().inflate(STAR_RANGE), p -> !p.isSpectator())) {
			AABB body = player.getBoundingBox().inflate(BODY_MARGIN);
			for (int i = 0; i < STARS_PER_TICK; i++) {
				Vec3 from = new Vec3(Mth.lerp(random.nextDouble(), body.minX, body.maxX), Mth.lerp(random.nextDouble(), body.minY, body.maxY),
						Mth.lerp(random.nextDouble(), body.minZ, body.maxZ));
				Vec3 velocity = centre.subtract(from).scale(1.0 / STAR_TRAVEL_TICKS);
				level().addParticle(ModParticles.PORTAL_STAR.get(), from.x, from.y, from.z, velocity.x, velocity.y, velocity.z);
			}
		}
	}

	@Override
	public InteractionResult interact(Player player, InteractionHand hand, Vec3 location) {
		if (!player.isCreative()) {
			return InteractionResult.PASS;
		}
		if (player instanceof ServerPlayer serverPlayer) {
			openConfig(serverPlayer);
		}
		return InteractionResult.SUCCESS;
	}

	public void openConfig(ServerPlayer player) {
		Vec3 shown = destination != null ? destination : position();
		ResourceKey<Level> shownLevel = destination != null ? destinationLevel : level().dimension();
		List<String> levels = player.level().getServer().levelKeys().stream().map(key -> key.identifier().toString()).sorted().toList();
		PacketDistributor.sendToPlayer(player, new OpenPortalConfigPayload(getId(), shown.x, shown.y, shown.z, shownLevel.identifier().toString(), levels, isRed(), getSize()));
	}

	@Override
	public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
		if (source.getEntity() instanceof Player player && player.isCreative()) {
			discard();
			return true;
		}
		return false;
	}

	@Override
	public boolean isPickable() {
		return true;
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		builder.define(RED, false);
		builder.define(FLAT, false);
		builder.define(SIZE, DEFAULT_SIZE);
	}

	@Override
	protected void addAdditionalSaveData(ValueOutput output) {
		output.putBoolean("red", isRed());
		output.putBoolean("flat", isFlat());
		output.putFloat("size", getSize());
		if (destination != null) {
			output.putDouble("dest_x", destination.x);
			output.putDouble("dest_y", destination.y);
			output.putDouble("dest_z", destination.z);
			output.putString("dest_level", destinationLevel.identifier().toString());
		}
	}

	@Override
	protected void readAdditionalSaveData(ValueInput input) {
		setRed(input.getBooleanOr("red", false));
		setFlat(input.getBooleanOr("flat", false));
		setSize(input.getFloatOr("size", DEFAULT_SIZE));
		input.getString("dest_level").ifPresent(level -> setDestination(
				new Vec3(input.getDoubleOr("dest_x", 0), input.getDoubleOr("dest_y", 0), input.getDoubleOr("dest_z", 0)),
				ResourceKey.create(Registries.DIMENSION, Identifier.parse(level))));
	}
}
