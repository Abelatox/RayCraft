package com.abelatox.raycraft.entities;

import com.abelatox.raycraft.blocks.BlockCage;
import com.abelatox.raycraft.network.payloads.StartGrapplePayload;

import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.network.PacketDistributor;

public class EntityFist extends RaycraftProjectile {

	private static final EntityDataAccessor<Integer> LEVEL = SynchedEntityData.defineId(EntityFist.class, EntityDataSerializers.INT);

	public static final int GOLD_LEVEL = 3;
	public static final int GIANT_LEVEL = 4;
	public static final float GIANT_SCALE = 4.0F;
	private static final float[] POWER = {2, 4, 6, 10, 16};
	private static final int[] MAX_TICKS = {60, 75, 90, 100, 120};

	private boolean explosion;
	private boolean charged;

	public EntityFist(EntityType<? extends EntityFist> type, Level level) {
		super(type, level);
	}

	public EntityFist(Level level, Player player, int punchLevel, boolean charged) {
		super(ModEntities.FIST.get(), level, player);
		int lvl = charged ? Math.min(punchLevel + 1, GIANT_LEVEL) : punchLevel;
		setLvl(lvl);
		setPower(POWER[lvl]);
		setMaxBounces(lvl);
		setMaxTicks(MAX_TICKS[lvl]);
		this.charged = charged;
		this.explosion = charged && lvl >= GOLD_LEVEL;
	}

	@Override
	public EntityDimensions getDimensions(Pose pose) {
		EntityDimensions dimensions = super.getDimensions(pose);
		return getLvl() == GIANT_LEVEL ? dimensions.scale(GIANT_SCALE) : dimensions;
	}

	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
		super.onSyncedDataUpdated(accessor);
		if (LEVEL.equals(accessor)) {
			refreshDimensions();
		}
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(LEVEL, 0);
	}

	@Override
	public void tick() {
		if (getLvl() >= GOLD_LEVEL) {
			level().addParticle(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, 1F, 1F, 0F), getX(), getY(), getZ(), 0, 0, 0);
		}
		super.tick();
	}

	@Override
	protected void onHit(HitResult hitResult) {
		if (hitResult instanceof EntityHitResult entityHit && entityHit.getEntity() instanceof EntityLum lum && lum.isPurple()) {
			if (getOwner() instanceof ServerPlayer owner) {
				PacketDistributor.sendToPlayer(owner, new StartGrapplePayload(lum.getId()));
			}
			if (!level().isClientSide()) {
				discard();
			}
			return;
		}
		super.onHit(hitResult);
	}

	@Override
	protected void onImpact() {
		if (explosion) {
			explode();
		} else {
			discard();
		}
	}

	@Override
	protected void onHitBlockFace(BlockHitResult hit) {
		if (explosion) {
			explode();
		} else {
			super.onHitBlockFace(hit);
		}
	}

	private void explode() {
		if (level() instanceof ServerLevel serverLevel) {
			serverLevel.explode(this, getX(), getY(), getZ(), 0, Level.ExplosionInteraction.NONE);
			for (LivingEntity living : level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(4.0D).move(-2.0D, -2.0D, -2.0D), e -> e != getOwner())) {
				living.hurtServer(serverLevel, damageSources().thrown(this, getOwner()), power);
			}
		}
		discard();
	}

	public int getLvl() {
		return this.getEntityData().get(LEVEL);
	}

	public void setLvl(int lvl) {
		this.getEntityData().set(LEVEL, lvl);
	}

	@Override
	protected int getCageDamage() {
		return charged || getLvl() >= GOLD_LEVEL ? BlockCage.HITS_TO_BREAK : 1;
	}

	@Override
	protected void addAdditionalSaveData(ValueOutput output) {
		output.putInt("lvl", this.getLvl());
	}

	@Override
	protected void readAdditionalSaveData(ValueInput input) {
		this.setLvl(input.getIntOr("lvl", 0));
	}
}
