package com.abelatox.raycraft.entities;

import com.abelatox.raycraft.sounds.ModSounds;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.component.SwingAnimation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.phys.Vec3;

public class EntityRoboPirate extends Monster implements RangedAttackMob {

	private static final float SHOT_SPEED = 1.0F;
	private static final float ATTACK_RANGE = 16.0F;

	public EntityRoboPirate(EntityType<? extends EntityRoboPirate> type, Level level) {
		super(type, level);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH, 30.0)
				.add(Attributes.MOVEMENT_SPEED, 0.25)
				.add(Attributes.FOLLOW_RANGE, 32.0);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(1, new FloatGoal(this));
		this.goalSelector.addGoal(2, new RangedAttackGoal(this, 1.0, 30, 50, ATTACK_RANGE));
		this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8));
		this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
	}

	@Override
	public void performRangedAttack(LivingEntity target, float power) {
		RaycraftProjectile shot = createShot();
		shot.setPos(getX(), getEyeY() - 0.1 - shot.getBbHeight() / 2.0, getZ());
		Vec3 aim = target.getBoundingBox().getCenter().subtract(shot.getBoundingBox().getCenter());
		float inaccuracy = 6.0F - level().getDifficulty().getId() * 2.0F;
		shot.shoot(aim.x, aim.y, aim.z, SHOT_SPEED, inaccuracy);
		level().addFreshEntity(shot);
		playSound(getShotSound(), 1.0F, 1.0F);
		swing(InteractionHand.MAIN_HAND, SwingAnimation.DEFAULT, false);
	}

	protected RaycraftProjectile createShot() {
		return new EntityPirateShot(ModEntities.PIRATE_SHOT_2.get(), level(), this);
	}

	protected SoundEvent getShotSound() {
		return ModSounds.PIRATE_SHOT_2.get();
	}

	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
		level.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, blockPosition().below(), Block.getId(level.getBlockState(blockPosition().below())));
		level.playSound(null, blockPosition(), ModSounds.PIRATE_DIGUP.get(), SoundSource.HOSTILE);
		return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return ModSounds.PIRATE_LIVING.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return ModSounds.PIRATE_HURT.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return ModSounds.PIRATE_DEATH.get();
	}
}
