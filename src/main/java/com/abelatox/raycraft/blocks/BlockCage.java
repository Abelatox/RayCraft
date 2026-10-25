package com.abelatox.raycraft.blocks;

import com.abelatox.raycraft.entities.EntityLum;
import com.abelatox.raycraft.sounds.ModSounds;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class BlockCage extends BaseBlock {

	public static final int HITS_TO_BREAK = 2;
	public static final IntegerProperty HITS = IntegerProperty.create("hits", 0, HITS_TO_BREAK - 1);

	private static final int YELLOW_LUMS = 3;
	private static final int HELP_CHANCE = 4;

	public BlockCage(BlockBehaviour.Properties properties) {
		super(properties);
		registerDefaultState(stateDefinition.any().setValue(HITS, 0));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(HITS);
	}

	@Override
	protected void attack(BlockState state, Level level, BlockPos pos, Player player) {
		if (level instanceof ServerLevel serverLevel) {
			hit(serverLevel, pos, state, player, 1);
		}
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		if (random.nextInt(HELP_CHANCE) == 0) {
			level.playLocalSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, ModSounds.CAGE_HELP.get(), SoundSource.BLOCKS, 0.8F, 0.9F + random.nextFloat() * 0.2F, false);
		}
	}

	public static void hit(ServerLevel level, BlockPos pos, BlockState state, Player breaker, int damage) {
		int hits = state.getValue(HITS) + damage;
		if (hits >= HITS_TO_BREAK) {
			breakCage(level, pos, breaker);
			return;
		}
		level.setBlock(pos, state.setValue(HITS, hits), Block.UPDATE_ALL);
		level.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, pos, Block.getId(state));
		level.playSound(null, pos, ModSounds.CAGE_HIT.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
	}

	public static void breakCage(ServerLevel level, BlockPos pos, Player breaker) {
		BlockState state = level.getBlockState(pos);
		level.removeBlock(pos, false);
		level.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, pos, Block.getId(state));
		level.playSound(null, pos, ModSounds.CAGE_BREAK.get(), SoundSource.BLOCKS, 1.0F, 1.0F);

		for (int i = 0; i <= YELLOW_LUMS; i++) {
			EntityLum lum = new EntityLum(level);
			lum.setLumType(i == YELLOW_LUMS ? "red" : "yellow");
			float angle = i * Mth.TWO_PI / (YELLOW_LUMS + 1);
			lum.setPos(pos.getX() + 0.5 + Mth.cos(angle) * 0.8, pos.getY() + 0.5, pos.getZ() + 0.5 + Mth.sin(angle) * 0.8);
			level.addFreshEntity(lum);
		}

		/*if (breaker != null) {
			breaker.heal(breaker.getMaxHealth());
			level.playSound(null, breaker.getX(), breaker.getY(), breaker.getZ(), ModSounds.CAGE_HEAL_BONUS.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
		}*/
	}
}
