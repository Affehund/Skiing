package com.affehund.skiing.core.utils;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SChangeBlockPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class SnowShovelUtils {
	public static void breakBlocksInRadius(World world, PlayerEntity playerEntity, int radius) {
		if (!world.isRemote) {
			List<BlockPos> blocksToBreak = getBlocksToBreak(world, playerEntity, radius);
			ItemStack heldItem = playerEntity.getHeldItemMainhand();
			int silktouch = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, heldItem);
			int fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, heldItem);
			for (BlockPos pos : blocksToBreak) {
				BlockState state = world.getBlockState(pos);
				if (heldItem.getItem().canHarvestBlock(state)) {
					if (playerEntity.abilities.isCreativeMode) {
						if (state.removedByPlayer(world, pos, playerEntity, true, state.getFluidState()))
							state.getBlock().onPlayerDestroy(world, pos, state);
					} else {
						heldItem.getItem().onBlockDestroyed(heldItem, world, state, pos, playerEntity);
						TileEntity tileEntity = world.getTileEntity(pos);
						state.getBlock().onPlayerDestroy(world, pos, state);
						state.getBlock().harvestBlock(world, playerEntity, pos, state, tileEntity, heldItem);
						state.getBlock().dropXpOnBlockBreak((ServerWorld) world, pos,
								state.getBlock().getExpDrop(state, world, pos, fortune, silktouch));
					}
					world.removeBlock(pos, false);
					world.playEvent(2001, pos, Block.getStateId(state));
					((ServerPlayerEntity) playerEntity).connection.sendPacket(new SChangeBlockPacket(world, pos));
				}
			}
		}
	}

	public static List<BlockPos> getBlocksToBreak(World world, PlayerEntity player, int radius) {
		ArrayList<BlockPos> blocksList = new ArrayList<>();

		BlockRayTraceResult rayTraceResult = getLookingAtBlockRayTrace(world, player);

		if (rayTraceResult.getType() == RayTraceResult.Type.BLOCK) {
			Direction.Axis axis = rayTraceResult.getFace().getAxis();
			ArrayList<BlockPos> positions = new ArrayList<>();

			for (int x = -radius; x <= radius; x++) {
				for (int y = -radius; y <= radius; y++) {
					for (int z = -radius; z <= radius; z++) {
						positions.add(new BlockPos(x, y, z));
					}
				}
			}

			BlockPos origin = rayTraceResult.getPos();

			for (BlockPos pos : positions) {
				if (axis == Direction.Axis.Y) {
					if (pos.getY() == 0) {
						blocksList.add(origin.add(pos));
					}
				} else if (axis == Direction.Axis.X) {
					if (pos.getX() == 0) {
						blocksList.add(origin.add(pos));
					}
				} else if (axis == Direction.Axis.Z) {
					if (pos.getZ() == 0) {
						blocksList.add(origin.add(pos));
					}
				}
			}
			blocksList.remove(origin);

			blocksList.removeIf(newBlockPos -> (world.getBlockState(newBlockPos).getBlock() != world
					.getBlockState(origin).getBlock()));
		}
		return blocksList;
	}

	public static BlockRayTraceResult getLookingAtBlockRayTrace(World world, PlayerEntity player) {
		Vector3d eyePosition = player.getEyePosition(1);
		Vector3d rotation = player.getLook(1);
		Vector3d combined = eyePosition.add(rotation.x * 5, rotation.y * 5, rotation.z * 5);

		BlockRayTraceResult rayTraceResult = world.rayTraceBlocks(new RayTraceContext(eyePosition, combined,
				RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.NONE, player));
		return rayTraceResult;
	}

	public static String getTpStringFromBlockPos(BlockPos pos) {
		return "/tp " + pos.getX() + " " + pos.getY() + " " + pos.getZ();
	}
}
