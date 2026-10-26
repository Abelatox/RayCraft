package com.abelatox.raycraft.entities;

import com.abelatox.raycraft.sounds.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.function.Predicate;

public class EntityWalkingShell extends PathfinderMob {

	private static final EntityDataAccessor<Byte> STATE = SynchedEntityData.defineId(EntityWalkingShell.class, EntityDataSerializers.BYTE);
	protected static final byte IDLE = 0;
	protected static final byte CHASING = 1;
	protected static final byte TIRED = 2;
	protected static final byte RUNAWAY = 3;
	protected static final byte MOUNTING = 4;

	private static final double DETECT_RANGE = 16.0;
	private static final double CHASE_SPEED = 1.0;
	private static final double CHARGE_DISTANCE = 3.0;
	private static final double CONTACT_MARGIN = 0.3;
	private static final int TIRE_AFTER_TICKS = 8 * 20;
	private static final int TIRED_TICKS = 10 * 20;
	private static final int REST_AFTER_RIDE_TICKS = 3 * 20;
	private static final int STEP_SOUND_INTERVAL = 3;
	protected static final double STRAFE = 0.4;
	/**
	 * Ridden, the forward input is 1 like a player's, so this is directly comparable to a player's speed.
	 */
	private static final float RIDE_SPEED = 0.3F;
	private static final int DISMOUNT_GRACE_TICKS = 20;
	private static final int RUNAWAY_MAX_TICKS = 10 * 20;
	public static final int REAR_TICKS = 30;
	private static final int[] REAR_HOPS = {6, 18};
	private static final double REAR_HOP_POWER = 0.32;
	private static final double JUMP_POWER = 0.5;
	private static final double SEAT_HEIGHT = 0.8;

	private int stateTicks;
	protected Player target;
	private Player exRider;
	protected float runYaw;
	protected float runPitch;
	private int rearTicks;
	private boolean exploded;

	public EntityWalkingShell(EntityType<? extends EntityWalkingShell> type, Level level) {
		super(type, level);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 20.0)
				.add(Attributes.MOVEMENT_SPEED, 0.34)
				.add(Attributes.STEP_HEIGHT, 1.0)
				.add(Attributes.FOLLOW_RANGE, DETECT_RANGE);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(STATE, IDLE);
	}

	protected byte getState() {
		return getEntityData().get(STATE);
	}

	private void setState(byte state) {
		getEntityData().set(STATE, state);
		stateTicks = 0;
	}

	public boolean isTired() {
		return getState() == TIRED && !isVehicle();
	}

	public boolean isRearing() {
		return getState() == MOUNTING && isVehicle();
	}

	public float getRearProgress(float partialTick) {
		return Mth.clamp((rearTicks + partialTick) / REAR_TICKS, 0.0F, 1.0F);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FloatGoal(this));
	}

	@Override
	public void tick() {
		super.tick();
		rearTicks = isRearing() ? rearTicks + 1 : 0;
		if (level().isClientSide()) {
			clientEffects();
			return;
		}
		if (isInWater() || isInLava()) {
			explode(null);
			return;
		}
		if (isRearing()) {
			stateTicks++;
			if (stateTicks >= REAR_TICKS) {
				setState(IDLE);
			}
			return;
		}
		if ((isVehicle() || getState() == CHASING) && isCrashing()) {
			explode(null);
			return;
		}
		if ((isVehicle() || getState() == CHASING || getState() == RUNAWAY) && isTouchingAnotherShell()
				|| isVehicle() && isTouchingCreature(entity -> !hasPassenger(entity))) {
			explode(null);
			return;
		}

		moveSoundTick();
		if (isVehicle()) {
			return;
		}
		stateTicks++;
		switch (getState()) {
			case CHASING -> chaseTick();
			case TIRED -> tiredTick();
			case RUNAWAY -> runawayTick();
			default -> idleTick();
		}
	}

	private void idleTick() {
		if (stateTicks < REST_AFTER_RIDE_TICKS) {
			return;
		}
		target = findTarget();
		if (target != null) {
			setState(CHASING);
		}
	}

	private void chaseTick() {
		if (target == null || !isValidTarget(target)) {
			target = findTarget();
			if (target == null) {
				getNavigation().stop();
				setState(IDLE);
				stateTicks = REST_AFTER_RIDE_TICKS;
				return;
			}
		}
		chaseMove(target);

		if (getBoundingBox().inflate(CONTACT_MARGIN).intersects(target.getBoundingBox())) {
			explode(null);
			return;
		}
		if (stateTicks >= TIRE_AFTER_TICKS) {
			getNavigation().stop();
			setState(TIRED);
			playSound(ModSounds.SHELL_STOP.get(), 1.0F, 1.0F);
		}
	}

	private void tiredTick() {
		getNavigation().stop();
		for (Player player : level().getEntitiesOfClass(Player.class, getBoundingBox().inflate(0.2))) {
			if (!player.isSpectator() && !player.isPassenger() && mount(player)) {
				return;
			}
		}
		if (stateTicks >= TIRED_TICKS) {
			setState(CHASING);
		}
	}

	private boolean mount(Player player) {
		if (!player.startRiding(this)) {
			return false;
		}
		getNavigation().stop();
		setState(MOUNTING);
		level().playSound(null, getX(), getY(), getZ(), ModSounds.SHELL_NEIGH.get(), SoundSource.NEUTRAL, 1.0F, 1.0F);
		return true;
	}

	@Override
	protected InteractionResult mobInteract(Player player, InteractionHand hand) {
		if (!player.isCreative() || isVehicle() || player.isPassenger()) {
			return super.mobInteract(player, hand);
		}
		if (!level().isClientSide()) {
			mount(player);
		}
		return InteractionResult.SUCCESS;
	}

	/**
	 * Pathfinding gives up a little short of its target; once close it charges straight at the player instead,
	 * so it actually runs into them.
	 */
	protected void chaseMove(Player target) {
		if (distanceToSqr(target) < CHARGE_DISTANCE * CHARGE_DISTANCE) {
			getNavigation().stop();
			getMoveControl().setWantedPosition(target.getX(), target.getY(), target.getZ(), CHASE_SPEED);
		} else {
			getNavigation().moveTo(target, CHASE_SPEED);
		}
		getLookControl().setLookAt(target);
	}

	/**
	 * Fixed cadence in every state; vanilla steps go by distance, so the slower chase sounded sparser.
	 */
	protected void moveSoundTick() {
		boolean moved = (getX() - xo) * (getX() - xo) + (getZ() - zo) * (getZ() - zo) > 1.0E-4;
		if ((moved || getControllingPassenger() != null) && tickCount % STEP_SOUND_INTERVAL == 0) {
			level().playSound(null, getX(), getY(), getZ(), ModSounds.SHELL_WALK.get(), SoundSource.NEUTRAL, 0.8F, 1.0F);
		}
	}

	private void runawayTick() {
		setYRot(runYaw);
		this.yBodyRot = this.yHeadRot = runYaw;
		setSpeed(RIDE_SPEED);
		if (isTouchingCreature(entity -> entity != exRider || stateTicks > DISMOUNT_GRACE_TICKS) || horizontalCollision || isCrashing() || stateTicks > RUNAWAY_MAX_TICKS) {
			explode(exRider);
		}
	}

	@Override
	protected void removePassenger(Entity passenger) {
		super.removePassenger(passenger);
		if (!level().isClientSide() && !isVehicle() && !isRemoved()) {
			target = null;
			exRider = passenger instanceof Player player ? player : null;
			runYaw = getYRot();
			runPitch = getXRot();
			setState(RUNAWAY);
		}
	}

	@Override
	public void travel(Vec3 input) {
		super.travel(getState() == RUNAWAY && !isVehicle() ? new Vec3(0.0, 0.0, 1.0) : input);
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState blockState) {
	}

	@Override
	public float maxUpStep() {
		return getState() == RUNAWAY && !isVehicle() ? 0.0F : super.maxUpStep();
	}

	private Player findTarget() {
		Player nearest = level().getNearestPlayer(this, DETECT_RANGE);
		return nearest != null && isValidTarget(nearest) ? nearest : null;
	}

	private boolean isValidTarget(Player player) {
		return player.isAlive() && !player.isCreative() && !player.isSpectator() && distanceToSqr(player) <= DETECT_RANGE * DETECT_RANGE && hasLineOfSight(player);
	}

	/**
	 * Whether it's running into a wall at least two blocks high. Checked on the server from the
	 * blocks in front, not from movement collisions: when ridden, the rider's client moves it.
	 */
	protected boolean isCrashing() {
		float yaw = getYRot() * Mth.DEG_TO_RAD;
		Vec3 front = position().add(-Mth.sin(yaw) * (getBbWidth() / 2 + 0.2), 0.1, Mth.cos(yaw) * (getBbWidth() / 2 + 0.2));
		BlockPos feet = BlockPos.containing(front);
		return isSolid(feet) && isSolid(feet.above());
	}

	private boolean isTouchingCreature(Predicate<LivingEntity> filter) {
		return !level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(0.1),
				entity -> entity != this && !entity.isSpectator() && filter.test(entity)).isEmpty();
	}

	private boolean isTouchingAnotherShell() {
		return !level().getEntitiesOfClass(EntityWalkingShell.class, getBoundingBox().inflate(0.1), shell -> shell != this).isEmpty();
	}

	protected boolean isSolid(BlockPos pos) {
		return !level().getBlockState(pos).getCollisionShape(level(), pos).isEmpty();
	}

	@Override
	public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
		if (isInvulnerableTo(level, source) || exploded) {
			return false;
		}
		explode(null);
		return true;
	}

	@Override
	public boolean causeFallDamage(double fallDistance, float damageModifier, DamageSource damageSource) {
		return false;
	}

	private void explode(Player owner) {
		if (exploded) {
			return;
		}
		exploded = true;
		if (level() instanceof ServerLevel serverLevel) {
			EntityBarrel.explode(serverLevel, this, owner);
		} else {
			discard();
		}
	}

	private void clientEffects() {
		float yaw = yBodyRot * Mth.DEG_TO_RAD;
		Vec3 forward = new Vec3(-Mth.sin(yaw), 0, Mth.cos(yaw));
		double axisY = getY() + getBbHeight() * 0.68;
		if (getState() == CHASING || getState() == RUNAWAY || (isVehicle() && !isRearing())) {
			Vec3 tail = position().add(forward.scale(-0.45)).add(0, axisY - getY(), 0);
			level().addParticle(ParticleTypes.FLAME, tail.x, tail.y, tail.z, -forward.x * 0.08, 0.01, -forward.z * 0.08);
		} else if (isTired() && tickCount % 8 == 0) {
			Vec3 nose = position().add(forward.scale(0.65)).add(0, axisY - getY() - 0.2, 0);
			level().addParticle(ParticleTypes.CLOUD, nose.x, nose.y, nose.z, forward.x * 0.03, -0.01, forward.z * 0.03);
		}
	}

	/**
	 * Low enough that the rider's hips sit on the shell's back instead of floating over it.
	 */
	@Override
	protected Vec3 getPassengerAttachmentPoint(Entity passenger, EntityDimensions dimensions, float scale) {
		return new Vec3(0.0, SEAT_HEIGHT, 0.0);
	}

	@Override
	public LivingEntity getControllingPassenger() {
		return getFirstPassenger() instanceof Player player ? player : super.getControllingPassenger();
	}

	@Override
	protected void tickRidden(Player controller, Vec3 riddenInput) {
		super.tickRidden(controller, riddenInput);
		setRot(controller.getYRot(), controller.getXRot() * 0.5F);
		this.yRotO = this.yBodyRot = this.yHeadRot = getYRot();
		if (isRearing() && onGround()) {
			for (int hop : REAR_HOPS) {
				if (rearTicks == hop) {
					setDeltaMovement(getDeltaMovement().x, REAR_HOP_POWER, getDeltaMovement().z);
				}
			}
		} else if (controller.isJumping() && onGround() && !isNoGravity()) {
			setDeltaMovement(getDeltaMovement().x, JUMP_POWER, getDeltaMovement().z);
		}
	}

	@Override
	protected Vec3 getRiddenInput(Player controller, Vec3 selfInput) {
		return isRearing() ? Vec3.ZERO : new Vec3(controller.xxa * STRAFE, 0.0, 1.0);
	}

	@Override
	protected float getRiddenSpeed(Player controller) {
		return RIDE_SPEED;
	}

	@Override
	public boolean removeWhenFarAway(double distanceToClosestPlayer) {
		return false;
	}
}
