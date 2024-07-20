package com.github.palomox.realistic_electricity.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CableHolderBlock extends Block{
	
	public static final DirectionProperty FACING = BlockStateProperties.FACING;

	private static final VoxelShape UP = Block.box(5.5, 0, 5.5, 10.5, 11, 10.5);
	private static final VoxelShape DOWN = Block.box(5.5, 5, 5.5, 10.5, 16, 10.5);
	private static final VoxelShape NORTH = Block.box(5.5, 5.5, 5.5, 10.5, 10.5, 16);
	private static final VoxelShape SOUTH = Block.box(5.5, 5.5, 0, 10.5, 10.5, 11);
	private static final VoxelShape WEST = Block.box(5.5, 5.5, 5.5, 16, 10.5, 10.5);
	private static final VoxelShape EAST = Block.box(0, 5.5, 5.5, 11, 10.5, 10.5);

	public CableHolderBlock() {
		super(BlockBehaviour.Properties.of()
				.requiresCorrectToolForDrops()
				.strength(3.5f)
				.sound(SoundType.METAL));
		
		registerDefaultState(stateDefinition.any()
				.setValue(FACING, Direction.NORTH));
		
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
		pBuilder.add(FACING);
	}
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext pContext) {
		return this.defaultBlockState().setValue(BlockStateProperties.FACING, pContext.getClickedFace());
	}

	@Override
	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		return switch (pState.getValue(BlockStateProperties.FACING)) {
			case UP -> UP;
			case DOWN -> DOWN;
			case EAST -> EAST;
			case NORTH -> NORTH;
			case SOUTH -> SOUTH;
			case WEST -> WEST;
		};
	}

}
