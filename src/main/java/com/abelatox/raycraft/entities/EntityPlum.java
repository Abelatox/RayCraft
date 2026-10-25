package com.abelatox.raycraft.entities;

import com.abelatox.raycraft.items.ModItems;
import com.abelatox.raycraft.lib.Utils;
import com.abelatox.raycraft.sounds.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

public class EntityPlum extends Entity {

	private static final double GRAVITY = 0.08;
	private static final double BOUNCE = 0.6;
	private static final double IDLE_BOUNCE = 0.4;
	private static final double MOUNT_TOP_MARGIN = 0.3;
	private static final double FLOAT_DEPTH = 0.4;
	private static final double FLUID_DRAG = 0.75;
	private static final double FLUID_HORIZONTAL_DRAG = 0.92;
	private static final float THROWN_DAMAGE = 4.0F;
	private static final double HIT_SPEED_SQR = 0.25;
	public static final double PUSH = 0.6;

	private static final int PICKUP_DELAY_AFTER_RIDE = 20;

	private static final double[] DISMOUNT_DISTANCES = {1.0, 1.6, 2.2};

	private static final double JUMP_OFF_SPEED = 0.6;

	private Entity thrower;
	private boolean jumpingOff;

	private boolean riderWasJumping;
	private int pickupDelay;

	public EntityPlum(EntityType<? extends EntityPlum> type, Level level) {
		super(type, level);
	}

	public EntityPlum(Level level, LivingEntity thrower) {
		this(ModEntities.PLUM.get(), level);
		this.thrower = thrower;
		setPos(thrower.getX(), thrower.getEyeY() - 0.3, thrower.getZ());
		setDeltaMovement(thrower.getLookAngle().scale(0.9).add(thrower.getDeltaMovement()));
	}

	/**
	 * Physics only runs on the server; without this the client snaps to every position update.
	 */
	@Override
	protected InterpolationHandler createInterpolationHandler() {
		return LinearInterpolationHandler.create(this);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
	}

	@Override
	public void tick() {
		super.tick();
		if (pickupDelay > 0) {
			pickupDelay--;
		}
		if (!canSimulateMovement()) {
			return;
		}

		Vec3 velocity = getDeltaMovement();
		double fluid = Math.max(getFluidHeight(FluidTags.WATER), getFluidHeight(FluidTags.LAVA));
		if (fluid > 0) {
			// A spring towards the floating depth: one continuous force, so it settles without jumps at the surface.
			double submerged = Math.min(fluid / getBbHeight(), 1.0);
			double vy = (velocity.y + GRAVITY * (submerged / FLOAT_DEPTH - 1.0)) * FLUID_DRAG;
			velocity = new Vec3(velocity.x * FLUID_HORIZONTAL_DRAG, vy, velocity.z * FLUID_HORIZONTAL_DRAG);
		} else {
			velocity = velocity.add(0, -GRAVITY, 0);
		}
		Vec3 start = position();
		setDeltaMovement(velocity);
		move(MoverType.SELF, velocity);
		Vec3 moved = position().subtract(start);
		double vx = Mth.equal(velocity.x, moved.x) ? velocity.x : -velocity.x * BOUNCE;
		double vz = Mth.equal(velocity.z, moved.z) ? velocity.z : -velocity.z * BOUNCE;
		double vy = onGround() && velocity.y <= 0 ? Math.max(-velocity.y * BOUNCE, IDLE_BOUNCE) : getDeltaMovement().y;
		setDeltaMovement(vx, vy, vz);

		double friction = onGround() ? 0.6 : 0.98;
		setDeltaMovement(getDeltaMovement().multiply(friction, 0.98, friction));

		if (!level().isClientSide()) {
			hitCreatures();
			mountPlayerOnTop();
			jumpOffOnRiderJump();
		}
	}

	private void mountPlayerOnTop() {
		if (isVehicle() || thrower != null || pickupDelay > 0) {
			return;
		}
		double top = getBoundingBox().maxY;
		for (Player player : level().getEntitiesOfClass(Player.class, getBoundingBox().inflate(0.0, 0.2, 0.0))) {
			boolean onTop = player.getBoundingBox().minY >= top - MOUNT_TOP_MARGIN;
			if (onTop && !player.isPassenger() && !player.isSpectator() && !player.isShiftKeyDown() && player.startRiding(this)) {
				riderWasJumping = true;
				return;
			}
		}
	}

	private void hitCreatures() {
		if (thrower == null) {
			return;
		}
		if (onGround() || getDeltaMovement().lengthSqr() < HIT_SPEED_SQR) {
			thrower = null;
			return;
		}
		for (Entity entity : level().getEntities(this, getBoundingBox().inflate(0.2))) {
			if (entity instanceof LivingEntity living && entity != thrower && level() instanceof ServerLevel serverLevel) {
				DamageSource source = damageSources().thrown(this, thrower);
				living.hurtServer(serverLevel, source, THROWN_DAMAGE);
				level().playSound(null, blockPosition(), ModSounds.PLUM_HIT.get(), SoundSource.NEUTRAL, 1.0F, 1.0F);
				setDeltaMovement(getDeltaMovement().scale(-0.2));
				thrower = null;
				return;
			}
		}
	}

	public void pushFrom(Vec3 direction) {
		Vec3 push = new Vec3(direction.x, 0, direction.z);
		if (push.lengthSqr() > 1.0E-6) {
			push = push.normalize().scale(PUSH);
		}
		setDeltaMovement(getDeltaMovement().add(push).add(0, 0.15, 0));
		this.needsSync = true;
		level().playSound(null, blockPosition(), ModSounds.PLUM_HIT.get(), SoundSource.NEUTRAL, 1.0F, 1.0F);
	}

	public boolean tryPickUp(Player player) {
		if (thrower != null || isVehicle() || pickupDelay > 0) {
			return false;
		}
		if (!Utils.tryCarry(player, new ItemStack(ModItems.PLUM.get()))) {
			return false;
		}
		level().playSound(null, getX(), getY(), getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.4F, 1.0F);
		discard();
		return true;
	}

	private void jumpOffOnRiderJump() {
		if (!(getFirstPassenger() instanceof ServerPlayer rider)) {
			return;
		}
		boolean jumping = rider.getLastClientInput().jump();
		if (jumping && !riderWasJumping) {
			jumpingOff = true;
			rider.stopRiding();
			jumpingOff = false;
			if (!rider.isPassenger()) {
				rider.setDeltaMovement(getDeltaMovement().x, JUMP_OFF_SPEED, getDeltaMovement().z);
				// The client owns the player's movement, and entity tracking never sends a player its own motion.
				rider.connection.send(new ClientboundSetEntityMotionPacket(rider));
			}
		}
		riderWasJumping = jumping;
	}

	/**
	 * Jumping off is the rider's choice, so it's allowed even over lava.
	 */
	public boolean isJumpingOff() {
		return jumpingOff;
	}

	public boolean isInLavaPool() {
		return getFluidHeight(FluidTags.LAVA) > 0;
	}

	@Override
	public Vec3 getDismountLocationForPassenger(LivingEntity passenger) {
		if (jumpingOff) {
			return new Vec3(getX(), getBoundingBox().maxY, getZ());
		}
		Vec3 safe = findSafeDismount(passenger);
		return safe != null ? safe : super.getDismountLocationForPassenger(passenger);
	}

	public Vec3 findSafeDismount(LivingEntity passenger) {
		for (double distance : DISMOUNT_DISTANCES) {
			for (int i = 0; i < 8; i++) {
				double angle = (passenger.getYRot() + 90.0 + i * 45.0) * Mth.DEG_TO_RAD;
				double x = getX() + Math.cos(angle) * distance;
				double z = getZ() + Math.sin(angle) * distance;
				for (int dy = 1; dy >= -1; dy--) {
					BlockPos pos = BlockPos.containing(x, getY() + dy, z);
					double floor = level().getBlockFloorHeight(pos);
					if (DismountHelper.isBlockFloorValid(floor)) {
						Vec3 target = new Vec3(x, pos.getY() + floor, z);
						if (DismountHelper.canDismountTo(level(), target, passenger, Pose.STANDING)) {
							return target;
						}
					}
				}
			}
		}
		return null;
	}

	@Override
	public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
		if (source.getDirectEntity() instanceof Player player) {
			pushFrom(player.getLookAngle());
		}
		return false;
	}

	@Override
	public boolean canBeCollidedWith(Entity other) {
		return true;
	}

	@Override
	public boolean isPickable() {
		return !isRemoved();
	}

	@Override
	protected void removePassenger(Entity passenger) {
		super.removePassenger(passenger);
		pickupDelay = PICKUP_DELAY_AFTER_RIDE;
	}

	@Override
	protected boolean canAddPassenger(Entity passenger) {
		return getPassengers().isEmpty();
	}

	@Override
	protected void readAdditionalSaveData(ValueInput input) {
	}

	@Override
	protected void addAdditionalSaveData(ValueOutput output) {
	}
}
