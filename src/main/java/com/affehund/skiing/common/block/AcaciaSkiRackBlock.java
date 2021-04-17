package com.affehund.skiing.common.block;

import com.affehund.skiing.common.tile.AcaciaSkiRackTileEntity;
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

/**
 * @author Affehund
 *
 */
public class AcaciaSkiRackBlock extends AbstractSkiRackBlock {
	public AcaciaSkiRackBlock(Properties properties) {
		super(properties);
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return ModTileEntities.ACACIA_SKI_RACK_TILE_ENTITY.get().create();
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player,
			Hand hand, BlockRayTraceResult hit) {
		if (!world.isRemote) {
			TileEntity tile = world.getTileEntity(pos);
			if (tile instanceof AcaciaSkiRackTileEntity) {
				NetworkHooks.openGui((ServerPlayerEntity) player, (AcaciaSkiRackTileEntity) tile, pos);
			}
		}
		return ActionResultType.SUCCESS;
	}

	@Override
	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			TileEntity tileentity = worldIn.getTileEntity(pos);
			if (tileentity instanceof AcaciaSkiRackTileEntity) {
				InventoryHelper.dropInventoryItems(worldIn, pos, (AcaciaSkiRackTileEntity) tileentity);
				worldIn.updateComparatorOutputLevel(pos, this);
			}
		}
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		if (stack.hasDisplayName()) {
			TileEntity tileentity = worldIn.getTileEntity(pos);
			if (tileentity instanceof AcaciaSkiRackTileEntity) {
				((AcaciaSkiRackTileEntity) tileentity).setCustomName(stack.getDisplayName());
			}
		}
	}
}
