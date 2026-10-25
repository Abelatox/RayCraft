package com.abelatox.raycraft.blocks;

import com.abelatox.raycraft.sounds.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.EnumMap;
import java.util.Map;

public class BlockTimerSwitch extends BaseBlock {

	public static final int SECONDS = 10;
	private static final int STEP_TICKS = 8;
	private static final int STEPS = SECONDS * 20 / STEP_TICKS;
	private static final int HURRY_STEPS = 5 * 20 / STEP_TICKS;
	public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;
	public static final IntegerProperty TIME = IntegerProperty.create("time", 0, STEPS);

	private static final Map<Direction, VoxelShape> SHAPES = new EnumMap<>(Map.of(
			Direction.UP, Block.box(1, 0, 1, 15, 4, 15),
			Direction.DOWN, Block.box(1, 12, 1, 15, 16, 15),
			Direction.NORTH, Block.box(1, 1, 12, 15, 15, 16),
			Direction.SOUTH, Block.box(1, 1, 0, 15, 15, 4),
			Direction.EAST, Block.box(0, 1, 1, 4, 15, 15),
			Direction.WEST, Block.box(12, 1, 1, 16, 15, 15)));

	public BlockTimerSwitch(BlockBehaviour.Properties properties) {
		super(properties);
		registerDefaultState(stateDefinition.any().setValue(FACING, Direction.UP).setValue(TIME, 0));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, TIME);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return defaultBlockState().setValue(FACING, context.getClickedFace());
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPES.get(state.getValue(FACING));
	}

	@Override
	protected BlockState rotate(BlockState state, Rotation rotation) {
		return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
	}

	@Override
	protected BlockState mirror(BlockState state, Mirror mirror) {
		return state.rotate(mirror.getRotation(state.getValue(FACING)));
	}

	public static boolean isOn(BlockState state) {
		return state.getValue(TIME) > 0;
	}

	@Override
	protected void attack(BlockState state, Level level, BlockPos pos, Player player) {
		if (level instanceof ServerLevel serverLevel) {
			activate(serverLevel, pos, state);
		}
	}

	public static void activate(ServerLevel level, BlockPos pos, BlockState state) {
		boolean wasOn = isOn(state);
		setTime(level, pos, state, STEPS);
		playClock(level, pos, STEPS);
		if (!wasOn) {
			level.scheduleTick(pos, state.getBlock(), STEP_TICKS);
		}
	}

	@Override
	protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		int time = state.getValue(TIME);
		if (time <= 0) {
			return;
		}
		int next = time - 1;
		setTime(level, pos, state, next);
		if (next > 0) {
			playClock(level, pos, next);
			level.scheduleTick(pos, this, STEP_TICKS);
		}
	}

	private static void setTime(ServerLevel level, BlockPos pos, BlockState state, int time) {
		BlockState newState = state.setValue(TIME, time);
		level.setBlock(pos, newState, Block.UPDATE_ALL);
		if (isOn(state) != isOn(newState)) {
			// Like a button: the attached block's neighbours must hear about the strong power too.
			level.updateNeighborsAt(pos.relative(state.getValue(FACING).getOpposite()), state.getBlock(), null);
		}
	}

	private static void playClock(ServerLevel level, BlockPos pos, int stepsLeft) {
		level.playSound(null, pos, (stepsLeft > HURRY_STEPS ? ModSounds.SWITCH_TICK : ModSounds.SWITCH_TOCK).get(), SoundSource.BLOCKS, 1.0F, 1.0F);
	}

	@Override
	protected boolean isSignalSource(BlockState state) {
		return true;
	}

	@Override
	protected int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
		return isOn(state) ? 15 : 0;
	}

	@Override
	protected int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
		return isOn(state) && direction == state.getValue(FACING) ? 15 : 0;
	}
}
