package com.affehund.skiing.common.block;

import com.affehund.skiing.common.block_entity.SkiRackBlockEntity;
import com.affehund.skiing.core.init.SkiingBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.stats.Stats;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Stream;

public class SkiRackBlock extends HorizontalDirectionalBlock implements EntityBlock {
    private static final Map<Direction, VoxelShape> SHAPES = new EnumMap<>(Direction.class);
    private static final VoxelShape SHAPE = getDefaultShape();

    public SkiRackBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH));
        for (final Direction direction : Direction.values()) {
            SHAPES.put(direction, rotateShapesTo(direction));
        }
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPES.get(state.getValue(FACING));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public @NotNull BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public @NotNull BlockState mirror(@NotNull BlockState state, Mirror mirror) {
        return rotate(state, mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return SkiingBlockEntities.SKI_RACK_BLOCK_ENTITY.get().create(blockPos, blockState);
    }

/*
    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult blockHitResult) {
        if (level.getBlockEntity(pos) instanceof SkiRackBlockEntity skiRackBlockEntity) {
            ItemStack itemstack = player.getItemInHand(hand);
            if (!level.isClientSide && skiRackBlockEntity.addItem(player.getAbilities().instabuild ? itemstack.copy() : itemstack)) {
                player.awardStat(Stats.INTERACT_WITH_CAMPFIRE);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }
*/

    @Override
    public void onRemove(BlockState state, @NotNull Level level, @NotNull BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            if (level.getBlockEntity(pos) instanceof SkiRackBlockEntity skiRackBlockEntity) {
                Containers.dropContents(level, pos, skiRackBlockEntity.getItems());
            }
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    private static VoxelShape getDefaultShape() {
        return Stream.of(Block.box(0, 0, 1, 3, 2, 15), Block.box(13, 0, 1, 16, 2, 15),
                Block.box(1, 2, 5, 2, 16, 11), Block.box(2, 2, 5, 14, 15, 11),
                Block.box(14, 2, 5, 15, 16, 11)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    }

    private static VoxelShape rotateShapesTo(Direction direction) {
        VoxelShape[] buffer = {SHAPE, Shapes.empty() };
        int times = (direction.get2DDataValue() - Direction.NORTH.get2DDataValue() + 4) % 4;
        for (int i = 0; i < times; i++) {
            buffer[0].forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> buffer[1] = Shapes.or(buffer[1],
                    Shapes.create(1 - maxZ, minY, minX, 1 - minZ, maxY, maxX)));
            buffer[0] = buffer[1];
            buffer[1] = Shapes.empty();
        }
        return buffer[0];
    }
}
