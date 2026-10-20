package com.abelatox.raycraft.entities;

import com.abelatox.raycraft.items.ModItems;
import com.abelatox.raycraft.lib.Reference;
import com.abelatox.raycraft.sounds.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
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

	public static final TagKey<Block> BREAKABLE = BlockTags.create(Identifier.fromNamespaceAndPath(Reference.MODID, "barrel_breakable"));
	private static final int BLOCK_RADIUS = 1;

	@Override
	protected void onHit(HitResult result) {
		if (!(level() instanceof ServerLevel serverLevel)) {
			return;
		}

		if (result instanceof EntityHitResult entityResult && entityResult.getEntity() == this.getOwner() && entityResult.getEntity() instanceof Player player && player.getMainHandItem().isEmpty()) {
			player.getInventory().setItem(player.getInventory().getSelectedSlot(), new ItemStack(ModItems.BARREL.get()));
			this.discard();
			return;
		}

		explode(serverLevel);
	}

	private void explode(ServerLevel serverLevel) {
		serverLevel.sendParticles(ParticleTypes.EXPLOSION_EMITTER, getX(), getY(), getZ(), 1, 0, 0, 0, 0);
		serverLevel.sendParticles(ParticleTypes.EXPLOSION, getX(), getY(), getZ(), 12, 1.0, 1.0, 1.0, 0);
		serverLevel.sendParticles(ParticleTypes.LARGE_SMOKE, getX(), getY(), getZ(), 20, 1.0, 0.5, 1.0, 0.05);

		List<Entity> entities = level().getEntities(this, getBoundingBox().inflate(8.0D, 6.0D, 8.0D).move(-4.0D, -3.0D, -4.0D));
		if (this.getOwner() instanceof Player owner) {
			DamageSource damageSource = this.damageSources().playerAttack(owner);
			for (Entity entity : entities) {
				if (entity != owner) {
					entity.hurtServer(serverLevel, damageSource, 20);
				}
			}
		}

		breakFragileBlocks(serverLevel);
		level().playSound(null, this.getX(), this.getY(), this.getZ(), ModSounds.BOMB_EXPLODE.get(), SoundSource.PLAYERS, 5F, 1F);
		this.discard();
	}

	private void breakFragileBlocks(ServerLevel serverLevel) {
		BlockPos center = blockPosition();
		for (BlockPos pos : BlockPos.betweenClosed(center.offset(-BLOCK_RADIUS, -BLOCK_RADIUS, -BLOCK_RADIUS), center.offset(BLOCK_RADIUS, BLOCK_RADIUS, BLOCK_RADIUS))) {
			if (serverLevel.getBlockState(pos).is(BREAKABLE)) {
				serverLevel.destroyBlock(pos, false, this);
			}
		}
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
	}

	@Override
	protected void readAdditionalSaveData(ValueInput input) {
	}

	@Override
	protected void addAdditionalSaveData(ValueOutput output) {
	}
}
