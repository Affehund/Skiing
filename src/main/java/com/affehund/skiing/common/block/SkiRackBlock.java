package com.affehund.skiing.common.block;

import java.util.stream.Stream;

import javax.annotation.Nonnull;

import com.affehund.skiing.common.item.SkisItem;
import com.affehund.skiing.common.tile.SkiRackTileEntity;
import com.affehund.skiing.core.init.ModTileEntities;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class SkiRackBlock extends Block {

	public static final DirectionProperty DIRECTION = BlockStateProperties.HORIZONTAL_FACING;
	public static final EnumProperty<SkiRackType> TYPE = EnumProperty.create("type", SkiRackType.class);

	public SkiRackBlock(Properties properties) {
		super(properties);
		this.setDefaultState(this.getStateContainer().getBaseState().with(DIRECTION, Direction.NORTH).with(TYPE,
				SkiRackType.ACACIA));
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return ModTileEntities.SKI_RACK_TILE_ENTITY.get().create();
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player,
			Hand hand, BlockRayTraceResult hit) {
		if (!world.isRemote && hand == Hand.MAIN_HAND) {
			final TileEntity tile = world.getTileEntity(pos);
			if (tile instanceof SkiRackTileEntity) {
				SkiRackTileEntity skiRackTile = (SkiRackTileEntity) tile;
				ItemStack heldItem = player.getHeldItem(hand);
				if (!Screen.hasShiftDown() && heldItem.getItem() instanceof SkisItem) {
					if (skiRackTile.addItem(heldItem)) {
						return ActionResultType.SUCCESS;
					}
				}
				NetworkHooks.openGui((ServerPlayerEntity) player, (SkiRackTileEntity) tile, pos);
				return ActionResultType.CONSUME;
			}
		}
		return ActionResultType.FAIL;
	}

	private static VoxelShape getNorthSouthShape() {
		return Stream.of(Block.makeCuboidShape(0, 0, 1, 3, 2, 15), Block.makeCuboidShape(13, 0, 1, 16, 2, 15),
				Block.makeCuboidShape(1, 2, 5, 2, 16, 11), Block.makeCuboidShape(2, 2, 5, 14, 15, 11),
				Block.makeCuboidShape(14, 2, 5, 15, 16, 11)).reduce((v1, v2) -> {
					return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);
				}).get();
	}

	private static VoxelShape getEastWestShape() {
		return Stream.of(Block.makeCuboidShape(1, 0, 13, 15, 2, 16), Block.makeCuboidShape(1, 0, 0, 15, 2, 3),
				Block.makeCuboidShape(5, 2, 14, 11, 16, 15), Block.makeCuboidShape(5, 2, 2, 11, 15, 14),
				Block.makeCuboidShape(5, 2, 1, 11, 16, 2)).reduce((v1, v2) -> {
					return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);
				}).get();
	}

	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		if (state.get(DIRECTION) == Direction.NORTH || state.get(DIRECTION) == Direction.SOUTH)
			return getNorthSouthShape();
		else
			return getEastWestShape();
	}

	@Override
	public BlockState rotate(BlockState state, IWorld world, BlockPos pos, Rotation direction) {
		return rotate(state, direction);
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rot) {
		return state.with(DIRECTION, rot.rotate(state.get(DIRECTION)));
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return rotate(state, mirrorIn.toRotation(state.get(DIRECTION)));
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.getDefaultState().with(DIRECTION, context.getPlacementHorizontalFacing().getOpposite());
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(DIRECTION, TYPE);
	}

	@Nonnull
	public static SkiRackType getSkiRackType(BlockState state) {
		SkiRackType[] types = SkiRackType.values();
		int type = state.get(TYPE).ordinal();
		if (type < 0 || type >= types.length) {
			return SkiRackType.ACACIA;
		} else {
			return types[type];
		}
	}

	public static enum SkiRackType implements IStringSerializable {
		ACACIA("acacia", Blocks.ACACIA_PLANKS, Blocks.ACACIA_SLAB),
		BIRCH("birch", Blocks.BIRCH_PLANKS, Blocks.BIRCH_SLAB),
		CRIMSON("crimson", Blocks.CRIMSON_PLANKS, Blocks.CRIMSON_SLAB),
		DARK_OAK("dark_oak", Blocks.DARK_OAK_PLANKS, Blocks.DARK_OAK_SLAB),
		JUNGLE("jungle", Blocks.JUNGLE_PLANKS, Blocks.JUNGLE_SLAB), OAK("oak", Blocks.OAK_PLANKS, Blocks.OAK_SLAB),
		SPRUCE("spruce", Blocks.SPRUCE_PLANKS, Blocks.SPRUCE_SLAB),
		WARPED("warped", Blocks.WARPED_PLANKS, Blocks.WARPED_SLAB);

		private final String name;
		private final Block plank;
		private final Block slab;

		SkiRackType(String name, Block plank, Block slab) {
			this.name = name;
			this.plank = plank;
			this.slab = slab;
		}

		public Block getPlank() {
			return this.plank;
		}

		public Block getSlab() {
			return this.slab;
		}

		public String getName() {
			return this.name;
		}

		@Override
		public String getString() {
			return this.name;
		}

		public static SkiRackType getByName(String name) {
			return SkiRackType.valueOf(name.toUpperCase());
		}
	}
}
