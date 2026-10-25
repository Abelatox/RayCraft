package com.abelatox.raycraft.entities;

import com.abelatox.raycraft.blocks.BlockCage;
import com.abelatox.raycraft.blocks.BlockTimerSwitch;
import com.abelatox.raycraft.items.ModItems;
import com.abelatox.raycraft.lib.Reference;
import com.abelatox.raycraft.sounds.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class EntityBarrel extends ThrowableProjectile {

	private static final EntityDataAccessor<Integer> FUEL = SynchedEntityData.defineId(EntityBarrel.class, EntityDataSerializers.INT);
	private static final int FUEL_TICKS = 20 * 20;
	private static final int LOW_FUEL_TICKS = 3 * 20;

	public EntityBarrel(EntityType<? extends EntityBarrel> type, Level level) {
		super(type, level);
	}

	public EntityBarrel(Level level, LivingEntity thrower) {
		this(ModEntities.BARREL.get(), level);
		this.setOwner(thrower);
		this.setPos(thrower.getX(), thrower.getEyeY() - 0.1, thrower.getZ());
	}

	@Override
	protected double getDefaultGravity() {
		return isLit() ? 0.0 : 0.05;
	}

	public void light(Vec3 velocity) {
		getEntityData().set(FUEL, FUEL_TICKS);
		setDeltaMovement(velocity);
	}

	public boolean isLit() {
		return getEntityData().get(FUEL) > 0;
	}

	@Override
	public void tick() {
		super.tick();
		int fuel = getEntityData().get(FUEL);
		if (fuel <= 0 || isRemoved()) {
			return;
		}
		if (level().isClientSide()) {
			Vec3 back = getDeltaMovement().normalize().scale(-0.5);
			level().addParticle(ParticleTypes.FLAME, getX() + back.x, getY() + back.y, getZ() + back.z, back.x * 0.2, back.y * 0.2, back.z * 0.2);
			if (fuel < LOW_FUEL_TICKS) {
				for (int i = 0; i < 3; i++) {
					level().addParticle(ParticleTypes.LARGE_SMOKE, getX() + back.x, getY() + back.y + 0.2, getZ() + back.z,
							back.x * 0.1 + (random.nextDouble() - 0.5) * 0.05, 0.05, back.z * 0.1 + (random.nextDouble() - 0.5) * 0.05);
				}
			}
		} else if (level() instanceof ServerLevel serverLevel) {
			getEntityData().set(FUEL, fuel - 1);
			if (fuel == 1) {
				explode(serverLevel);
			}
		}
	}

	public static final TagKey<Block> BREAKABLE = BlockTags.create(Identifier.fromNamespaceAndPath(Reference.MODID, "barrel_breakable"));
	private static final int BLOCK_RADIUS = 1;

	@Override
	protected void onHit(HitResult result) {
		if (!(level() instanceof ServerLevel serverLevel)) {
			return;
		}

		if (!isLit() && result instanceof EntityHitResult entityResult && entityResult.getEntity() == this.getOwner() && entityResult.getEntity() instanceof Player player && player.getMainHandItem().isEmpty()) {
			player.getInventory().setItem(player.getInventory().getSelectedSlot(), new ItemStack(ModItems.BARREL.get()));
			this.discard();
			return;
		}

		explode(serverLevel);
	}

	public void explode(ServerLevel serverLevel) {
		explode(serverLevel, this, getOwner() instanceof Player player ? player : null);
	}

	public static void explode(ServerLevel serverLevel, Entity source, @Nullable Player owner) {
		double x = source.getX(), y = source.getY(), z = source.getZ();
		serverLevel.sendParticles(ParticleTypes.EXPLOSION_EMITTER, x, y, z, 1, 0, 0, 0, 0);
		serverLevel.sendParticles(ParticleTypes.EXPLOSION, x, y, z, 12, 1.0, 1.0, 1.0, 0);
		serverLevel.sendParticles(ParticleTypes.LARGE_SMOKE, x, y, z, 20, 1.0, 0.5, 1.0, 0.05);

		List<Entity> entities = serverLevel.getEntities(source, source.getBoundingBox().inflate(8.0D, 6.0D, 8.0D).move(-4.0D, -3.0D, -4.0D));
		DamageSource damageSource = owner != null ? source.damageSources().playerAttack(owner) : source.damageSources().explosion(source, null);
		for (Entity entity : entities) {
			if (entity != owner) {
				entity.hurtServer(serverLevel, damageSource, 20);
			}
		}

		breakFragileBlocks(serverLevel, source, owner);
		serverLevel.playSound(null, x, y, z, ModSounds.BOMB_EXPLODE.get(), SoundSource.PLAYERS, 5F, 1F);
		source.discard();
	}

	private static void breakFragileBlocks(ServerLevel serverLevel, Entity source, @Nullable Player owner) {
		BlockPos center = source.blockPosition();
		for (BlockPos pos : BlockPos.betweenClosed(center.offset(-BLOCK_RADIUS, -BLOCK_RADIUS, -BLOCK_RADIUS), center.offset(BLOCK_RADIUS, BLOCK_RADIUS, BLOCK_RADIUS))) {
			if (serverLevel.getBlockState(pos).getBlock() instanceof BlockCage) {
				BlockCage.breakCage(serverLevel, pos.immutable(), owner);
			} else if (serverLevel.getBlockState(pos).getBlock() instanceof BlockTimerSwitch) {
				BlockTimerSwitch.activate(serverLevel, pos.immutable(), serverLevel.getBlockState(pos));
			} else if (serverLevel.getBlockState(pos).is(BREAKABLE)) {
				serverLevel.destroyBlock(pos, false, source);
			}
		}
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		builder.define(FUEL, 0);
	}

	@Override
	protected void readAdditionalSaveData(ValueInput input) {
	}

	@Override
	protected void addAdditionalSaveData(ValueOutput output) {
	}
}
