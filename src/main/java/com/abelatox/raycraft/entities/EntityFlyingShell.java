package com.abelatox.raycraft.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import com.abelatox.raycraft.network.payloads.SetShellRollPayload;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
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
	 * Degrees, positive rolling left.
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
		if (getControllingPassenger() instanceof Player rider && rider.isLocalPlayer()) {
			if (isFlying()) {
				roll += (int) Math.signum(rider.xxa) * ROLL_SPEED;
			}
			if (roll != rollO) {
				ClientPacketDistributor.sendToServer(new SetShellRollPayload(getId(), roll));
			}
		} else if (level().isClientSide()) {
			roll = getEntityData().get(ROLL);
		}
		super.tick();
	}

	/** The rider's client owns the roll; the server shares it with everyone else. */
	public void setRollFromRider(float roll) {
		this.roll = roll;
		getEntityData().set(ROLL, roll);
	}

	public float getRollAngle(float partialTick) {
		return Mth.lerp(partialTick, rollO, roll);
	}

	/**
	 * The shell's own pitch and roll as a world rotation around its middle, the same the renderer applies to its
	 * model; the rider is turned by it so they stay seated on it.
	 */
	public Quaternionf getRiderRotation(float partialTick) {
		float facing = (180.0F - getYRot(partialTick)) * Mth.DEG_TO_RAD;
		float pitch = isFlying() ? getXRot(partialTick) : 0.0F;
		return new Quaternionf().rotateY(facing).rotateX(-pitch * Mth.DEG_TO_RAD).rotateZ(getRollAngle(partialTick) * Mth.DEG_TO_RAD).rotateY(-facing);
	}

	@Override
	protected void positionRider(Entity passenger, MoveFunction moveFunction) {
		Vec3 feet = getPassengerRidingPosition(passenger).subtract(passenger.getVehicleAttachmentPoint(this));
		Vec3 axis = position().add(0.0, getBbHeight() * 0.5, 0.0);
		Vector3f eye = feet.add(0.0, passenger.getEyeHeight(), 0.0).subtract(axis).toVector3f();
		float facing = (180.0F - getYRot()) * Mth.DEG_TO_RAD;
		new Quaternionf().rotateY(facing).rotateZ(roll * Mth.DEG_TO_RAD).rotateY(-facing).transform(eye);
		moveFunction.accept(passenger, axis.x + eye.x, axis.y + eye.y - passenger.getEyeHeight(), axis.z + eye.z);
	}

	/**
	 * Where the renderer must move the rider's model (from its feet) before turning it by {@link #getRiderRotation}, so
	 * it sits on the shell however it's pitched. The rider itself only goes round with the roll: following the pitch too
	 * would swing the camera about with every mouse movement.
	 */
	public Vector3f getRiderRenderOffset(Entity rider, float partialTick) {
		Vec3 shell = getPosition(partialTick);
		Vec3 axis = shell.add(0.0, getBbHeight() * 0.5, 0.0);
		Vec3 seat = shell.add(getPassengerAttachmentPoint(rider, getDimensions(getPose()), 1.0F)).subtract(rider.getVehicleAttachmentPoint(this));
		Vector3f offset = getRiderRotation(partialTick).transform(seat.subtract(axis).toVector3f());
		return offset.add(axis.subtract(rider.getPosition(partialTick)).toVector3f());
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
			// Not setXRot, which clamps to +-90: the shell loops with its rider.
			this.xRot = controller.getXRot();
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
	public void onPassengerTurned(Entity passenger) {
		super.onPassengerTurned(passenger);
		turnHook.accept(passenger);
	}

	private static java.util.function.Consumer<Entity> turnHook = passenger -> {
	};

	/** The client lifts the vertical look limit through this, run right after vanilla turns (and clamps) the rider. */
	public static void setTurnHook(java.util.function.Consumer<Entity> hook) {
		turnHook = hook;
	}

	@Override
	protected void moveSoundTick() {
		if (!isFlying()) {
			super.moveSoundTick();
		}
	}
}
