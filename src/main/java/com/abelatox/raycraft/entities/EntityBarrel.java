package com.abelatox.raycraft.entities;

import com.abelatox.raycraft.items.ModItems;
import com.abelatox.raycraft.sounds.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.List;

public class EntityBarrel extends ThrowableProjectile {

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
		return 0.05F;
	}

	@Override
	protected void onHit(HitResult result) {
		if (result.getType() == HitResult.Type.ENTITY) {
			EntityHitResult entityResult = (EntityHitResult) result;
			Entity hitEntity = entityResult.getEntity();

			if (hitEntity != null && hitEntity instanceof Player player && hitEntity == this.getOwner() && !level().isClientSide()) {
				if (player.getMainHandItem().isEmpty()) {
					player.getInventory().setItem(player.getInventory().getSelectedSlot(), new ItemStack(ModItems.BARREL.get()));
					this.discard();
					return;
				} else {
					explode();
				}
			} else {
				explode();
			}
		} else {
			explode();
		}
	}

	private void explode() {
		for (int i = 0; i < 5; i++) {
			level().addParticle(ParticleTypes.EXPLOSION, getX(), getY(), getZ(), i, i, i);
		}

		List<Entity> entities = level().getEntities(this, getBoundingBox().inflate(8.0D, 6.0D, 8.0D).move(-4.0D, -3.0D, -4.0D));
		if (!entities.isEmpty() && level() instanceof ServerLevel serverLevel) {
			for (Entity entity : entities) {
				if (this.getOwner() instanceof Player owner && entity != this.getOwner()) {
					DamageSource damageSource = this.damageSources().playerAttack(owner);
					entity.hurtServer(serverLevel, damageSource, 20);
				}
			}
		}

		removeBreakableBlocks();
		level().playSound(null, this.getX(), this.getY(), this.getZ(), ModSounds.BOMB_EXPLODE.get(), SoundSource.PLAYERS, 5F, 1F);
		this.discard();
	}

	private void removeBreakableBlocks() {
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				for (int z = 0; z < 3; z++) {
					BlockPos pos = BlockPos.containing(getX() + 1 - x, getY() + 1 - y, getZ() + 1 - z);
					if (level().getBlockState(pos).getBlock() == Blocks.IRON_DOOR || level().getBlockState(pos).getBlock() == Blocks.IRON_TRAPDOOR) {
						level().setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
					}
				}
			}
		}
	}

	@Override
	protected void defineSynchedData(net.minecraft.network.syncher.SynchedEntityData.Builder builder) {
	}

	@Override
	protected void readAdditionalSaveData(ValueInput input) {
	}

	@Override
	protected void addAdditionalSaveData(ValueOutput output) {
	}
}
