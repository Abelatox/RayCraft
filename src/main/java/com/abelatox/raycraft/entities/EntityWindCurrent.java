package com.abelatox.raycraft.entities;

import com.abelatox.raycraft.network.payloads.OpenWindConfigPayload;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

public class EntityWindCurrent extends Entity {
	public static final float MIN_HEIGHT = 2.0F;
	public static final float MAX_HEIGHT = 40.0F;
	public static final float MIN_RADIUS = 0.5F;
	public static final float MAX_RADIUS = 6.0F;
	public static final float BOTTOM_SCALE = 0.5F;
	private static final EntityDataAccessor<Float> HEIGHT = SynchedEntityData.defineId(EntityWindCurrent.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> RADIUS = SynchedEntityData.defineId(EntityWindCurrent.class, EntityDataSerializers.FLOAT);

	public EntityWindCurrent(EntityType<? extends EntityWindCurrent> type, Level level) {
		super(type, level);
		setNoGravity(true);
	}

	public float getHeight() {
		return getEntityData().get(HEIGHT);
	}

	public float getRadius() {
		return getEntityData().get(RADIUS);
	}

	public void setShape(float height, float radius) {
		getEntityData().set(HEIGHT, Mth.clamp(height, MIN_HEIGHT, MAX_HEIGHT));
		getEntityData().set(RADIUS, Mth.clamp(radius, MIN_RADIUS, MAX_RADIUS));
	}

	public float radiusAt(double y) {
		float t = (float) Mth.clamp((y - getY()) / getHeight(), 0.0, 1.0);
		return getRadius() * Mth.lerp(t, BOTTOM_SCALE, 1.0F);
	}

	public boolean catches(Entity entity) {
		double y = entity.getY() + entity.getBbHeight() * 0.5;
		if (entity.getBoundingBox().maxY < getY() || entity.getY() > getY() + getHeight()) {
			return false;
		}
		double dx = entity.getX() - getX();
		double dz = entity.getZ() - getZ();
		double reach = radiusAt(y) + entity.getBbWidth() * 0.5;
		return dx * dx + dz * dz <= reach * reach;
	}

	@Override
	public EntityDimensions getDimensions(Pose pose) {
		return EntityDimensions.fixed(getRadius() * 2.0F * BOTTOM_SCALE, getHeight());
	}

	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
		super.onSyncedDataUpdated(accessor);
		if (HEIGHT.equals(accessor) || RADIUS.equals(accessor)) {
			refreshDimensions();
		}
	}

	@Override
	public void tick() {
		super.tick();
		if (level().isClientSide() && random.nextInt(3) == 0) {
			double y = getY() + random.nextDouble() * getHeight();
			float angle = random.nextFloat() * Mth.TWO_PI;
			float r = radiusAt(y) * (0.4F + random.nextFloat() * 0.6F);
			level().addParticle(ParticleTypes.CLOUD, getX() + Mth.cos(angle) * r, y, getZ() + Mth.sin(angle) * r, -Mth.sin(angle) * 0.08, 0.12, Mth.cos(angle) * 0.08);
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
		PacketDistributor.sendToPlayer(player, new OpenWindConfigPayload(getId(), getHeight(), getRadius()));
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
	public boolean canBeHitByProjectile() {
		return false;
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		builder.define(HEIGHT, 8.0F);
		builder.define(RADIUS, 1.5F);
	}

	@Override
	protected void addAdditionalSaveData(ValueOutput output) {
		output.putFloat("height", getHeight());
		output.putFloat("radius", getRadius());
	}

	@Override
	protected void readAdditionalSaveData(ValueInput input) {
		setShape(input.getFloatOr("height", 8.0F), input.getFloatOr("radius", 1.5F));
	}
}
