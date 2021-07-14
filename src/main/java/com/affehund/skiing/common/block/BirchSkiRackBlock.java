package com.affehund.skiing.common.block;

import com.affehund.skiing.common.tile.BirchSkiRackTileEntity;
import com.affehund.skiing.core.init.ModTileEntities;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import net.minecraft.block.AbstractBlock.Properties;

/**
 * @author Affehund
 *
 */
public class BirchSkiRackBlock extends AbstractSkiRackBlock {
	public BirchSkiRackBlock(Properties properties) {
		super(properties);
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return ModTileEntities.BIRCH_SKI_RACK_TILE_ENTITY.get().create();
	}

	@Override
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player,
			Hand hand, BlockRayTraceResult hit) {
		if (!world.isClientSide) {
			TileEntity tile = world.getBlockEntity(pos);
			if (tile instanceof BirchSkiRackTileEntity) {
				NetworkHooks.openGui((ServerPlayerEntity) player, (BirchSkiRackTileEntity) tile, pos);
				return ActionResultType.SUCCESS;
			}
		}
		return ActionResultType.FAIL;
	}

	@Override
	public void onRemove(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			TileEntity tileentity = worldIn.getBlockEntity(pos);
			if (tileentity instanceof BirchSkiRackTileEntity) {
				InventoryHelper.dropContents(worldIn, pos, (BirchSkiRackTileEntity) tileentity);
				worldIn.updateNeighbourForOutputSignal(pos, this);
			}
		}
	}

	@Override
	public void setPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		if (stack.hasCustomHoverName()) {
			TileEntity tileentity = worldIn.getBlockEntity(pos);
			if (tileentity instanceof BirchSkiRackTileEntity) {
				((BirchSkiRackTileEntity) tileentity).setCustomName(stack.getHoverName());
			}
		}
	}
}
