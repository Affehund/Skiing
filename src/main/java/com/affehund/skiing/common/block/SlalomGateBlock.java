//package com.affehund.skiing.common.block;
//
//import java.util.stream.Stream;
//
//import net.minecraft.block.Block;
//import net.minecraft.block.BlockState;
//import net.minecraft.item.BlockItemUseContext;
//import net.minecraft.state.DirectionProperty;
//import net.minecraft.state.StateContainer;
//import net.minecraft.state.properties.BlockStateProperties;
//import net.minecraft.util.Direction;
//import net.minecraft.util.Mirror;
//import net.minecraft.util.Rotation;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.math.shapes.IBooleanFunction;
//import net.minecraft.util.math.shapes.ISelectionContext;
//import net.minecraft.util.math.shapes.VoxelShape;
//import net.minecraft.util.math.shapes.VoxelShapes;
//import net.minecraft.world.IBlockReader;
//import net.minecraft.world.IWorld;
//
//public class SlalomGateBlock extends Block {
//	public static final DirectionProperty DIRECTION = BlockStateProperties.HORIZONTAL_FACING;
//
//	public SlalomGateBlock(Properties properties) {
//		super(properties);
//		this.setDefaultState(this.getStateContainer().getBaseState().with(DIRECTION, Direction.NORTH));
//	}
//
//	private static VoxelShape getNorthSouthShape() {
//		return Stream.of(Block.makeCuboidShape(2, 12, 7.5, 14, 20, 8.5),
//				Block.makeCuboidShape(14, 16, 7.5, 15, 26, 8.5), Block.makeCuboidShape(14, 0, 7.5, 15, 16, 8.5),
//				Block.makeCuboidShape(1, 16, 7.5, 2, 26, 8.5), Block.makeCuboidShape(1, 0, 7.5, 2, 16, 8.5))
//				.reduce((v1, v2) -> {
//					return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);
//				}).get();
//	}
//
//	private static VoxelShape getEastWestShape() {
//		return Stream.of(Block.makeCuboidShape(7.5, 12, 2, 8.5, 20, 14), Block.makeCuboidShape(7.5, 16, 1, 8.5, 26, 2),
//				Block.makeCuboidShape(7.5, 0, 1, 8.5, 16, 2), Block.makeCuboidShape(7.5, 16, 14, 8.5, 26, 15),
//				Block.makeCuboidShape(7.5, 0, 14, 8.5, 16, 15)).reduce((v1, v2) -> {
//					return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);
//				}).get();
//	}
//
//	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
//		if (state.get(DIRECTION) == Direction.NORTH || state.get(DIRECTION) == Direction.SOUTH)
//			return getNorthSouthShape();
//		else
//			return getEastWestShape();
//	}
//
//	@Override
//	public BlockState rotate(BlockState state, IWorld world, BlockPos pos, Rotation direction) {
//		return rotate(state, direction);
//	}
//
//	@Override
//	public BlockState rotate(BlockState state, Rotation rot) {
//		return state.with(DIRECTION, rot.rotate(state.get(DIRECTION)));
//	}
//
//	@Override
//	public BlockState mirror(BlockState state, Mirror mirrorIn) {
//		return rotate(state, mirrorIn.toRotation(state.get(DIRECTION)));
//	}
//
//	@Override
//	public BlockState getStateForPlacement(BlockItemUseContext context) {
//		return this.getDefaultState().with(DIRECTION, context.getPlacementHorizontalFacing().getOpposite());
//	}
//
//	@Override
//	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
//		builder.add(DIRECTION);
//	}
//}
