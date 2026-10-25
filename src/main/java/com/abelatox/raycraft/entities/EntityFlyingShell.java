package com.abelatox.raycraft.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class EntityFlyingShell extends EntityWalkingShell {
	private static final EntityDataAccessor<Float> ROLL = SynchedEntityData.defineId(EntityFlyingShell.class, EntityDataSerializers.FLOAT);
	private static final double FLY_SPEED = 0.5;
	private static final double ACCELERATION = 0.25;
	private static final float ROLL_SPEED = 12.0F;
	private static final double ROLL_STRAFE = 0.8;
	/**
	 * Taking off from the ground it still touches it for a moment.
	 */
	private static final int TAKEOFF_TICKS = 10;

	/**
	 * Degrees, positive rolling left. The rider's client and the server work it out from the keys; other clients read the synced value.
	 */
	private float roll;
	private float rollO;
	private int flyingTicks;

	public EntityFlyingShell(EntityType<? extends EntityFlyingShell> type, Level level) {
		super(type, level);
	}

	public boolean isFlying() {
		return (isVehicle() && !isRearing()) || getState() == RUNAWAY;
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(ROLL, 0.0F);
	}

	@Override
	public void tick() {
		setNoGravity(isFlying());
		flyingTicks = isFlying() ? flyingTicks + 1 : 0;
		rollO = roll;
		if (getControllingPassenger() instanceof Player rider && (!level().isClientSide() || rider.isLocalPlayer())) {
			if (isFlying()) {
				roll += strafeInput(rider) * ROLL_SPEED;
			}
			if (!level().isClientSide()) {
				getEntityData().set(ROLL, roll);
			}
		} else if (level().isClientSide()) {
			roll = getEntityData().get(ROLL);
		}
		super.tick();
	}

	/**
	 * 1 holding left, -1 holding right, 0 neither.
	 */
	private static int strafeInput(Player rider) {
		if (rider instanceof ServerPlayer serverPlayer) {
			boolean left = serverPlayer.getLastClientInput().left();
			return left == serverPlayer.getLastClientInput().right() ? 0 : left ? 1 : -1;
		}
		return (int) Math.signum(rider.xxa);
	}

	public float getRollAngle(float partialTick) {
		return Mth.lerp(partialTick, rollO, roll);
	}

	/**
	 * The rider turns with the shell around its axis: their eyes (so the camera) go round it, and the
	 * renderer rolls the model around the eyes by the same angle.
	 */
	@Override
	protected void positionRider(Entity passenger, MoveFunction moveFunction) {
		Vec3 feet = getPassengerRidingPosition(passenger).subtract(passenger.getVehicleAttachmentPoint(this));
		Vec3 axis = position().add(0.0, getBbHeight() * 0.5, 0.0);
		Vector3f eye = feet.add(0.0, passenger.getEyeHeight(), 0.0).subtract(axis).toVector3f();
		float yaw = getYRot() * Mth.DEG_TO_RAD;
		eye.rotateAxis(roll * Mth.DEG_TO_RAD, Mth.sin(yaw), 0.0F, -Mth.cos(yaw));
		moveFunction.accept(passenger, axis.x + eye.x, axis.y + eye.y - passenger.getEyeHeight(), axis.z + eye.z);
	}

	@Override
	public void travel(Vec3 input) {
		if (!isFlying()) {
			super.travel(input);
			return;
		}
		if (!canSimulateMovement()) {
			return;
		}
		setDeltaMovement(getDeltaMovement().lerp(flyVelocity(input), ACCELERATION));
		move(MoverType.SELF, getDeltaMovement());
	}

	private Vec3 flyVelocity(Vec3 input) {
		if (getState() == RUNAWAY) {
			return Vec3.directionFromRotation(runPitch, runYaw).scale(FLY_SPEED);
		}
		float yaw = getYRot() * Mth.DEG_TO_RAD;
		Vec3 left = new Vec3(Mth.cos(yaw), 0.0, Mth.sin(yaw));
		return Vec3.directionFromRotation(getXRot(), getYRot()).add(left.scale(Mth.sin(roll * Mth.DEG_TO_RAD) * ROLL_STRAFE)).scale(FLY_SPEED);
	}

	@Override
	protected void tickRidden(Player controller, Vec3 riddenInput) {
		super.tickRidden(controller, riddenInput);
		if (isFlying()) {
			setXRot(controller.getXRot());
		}
	}

	@Override
	protected boolean isCrashing() {
		if (!isFlying()) {
			return super.isCrashing();
		}
		Vec3 center = position().add(0.0, getBbHeight() * 0.5, 0.0);
		Vec3 front = center.add(Vec3.directionFromRotation(getXRot(), getYRot()).scale(getBbWidth() / 2 + 0.3));
		boolean touchingBlock = flyingTicks > TAKEOFF_TICKS && !level().noBlockCollision(this, getBoundingBox().inflate(0.1));
		return touchingBlock || isSolid(BlockPos.containing(front)) || (getState() == RUNAWAY && verticalCollision);
	}

	@Override
	protected void moveSoundTick() {
		if (!isFlying()) {
			super.moveSoundTick();
		}
	}
}
