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
		if (!world.isClientSide) {
			List<BlockPos> blocksToBreak = getBlocksToBreak(world, playerEntity, radius);
			ItemStack heldItem = playerEntity.getMainHandItem();
			int silktouch = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, heldItem);
			int fortune = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, heldItem);
			for (BlockPos pos : blocksToBreak) {
				BlockState state = world.getBlockState(pos);
				if (heldItem.getItem().isCorrectToolForDrops(state)) {
					if (playerEntity.abilities.instabuild) {
						if (state.removedByPlayer(world, pos, playerEntity, true, state.getFluidState()))
							state.getBlock().destroy(world, pos, state);
					} else {
						heldItem.getItem().mineBlock(heldItem, world, state, pos, playerEntity);
						TileEntity tileEntity = world.getBlockEntity(pos);
						state.getBlock().destroy(world, pos, state);
						state.getBlock().playerDestroy(world, playerEntity, pos, state, tileEntity, heldItem);
						state.getBlock().popExperience((ServerWorld) world, pos,
								state.getBlock().getExpDrop(state, world, pos, fortune, silktouch));
					}
					world.removeBlock(pos, false);
					world.levelEvent(2001, pos, Block.getId(state));
					((ServerPlayerEntity) playerEntity).connection.send(new SChangeBlockPacket(world, pos));
				}
			}
		}
	}

	public static List<BlockPos> getBlocksToBreak(World world, PlayerEntity player, int radius) {
		ArrayList<BlockPos> blocksList = new ArrayList<>();

		BlockRayTraceResult rayTraceResult = getLookingAtBlockRayTrace(world, player);

		if (rayTraceResult.getType() == RayTraceResult.Type.BLOCK) {
			Direction.Axis axis = rayTraceResult.getDirection().getAxis();
			ArrayList<BlockPos> positions = new ArrayList<>();

			for (int x = -radius; x <= radius; x++) {
				for (int y = -radius; y <= radius; y++) {
					for (int z = -radius; z <= radius; z++) {
						positions.add(new BlockPos(x, y, z));
					}
				}
			}

			BlockPos origin = rayTraceResult.getBlockPos();

			for (BlockPos pos : positions) {
				if (axis == Direction.Axis.Y) {
					if (pos.getY() == 0) {
						blocksList.add(origin.offset(pos));
					}
				} else if (axis == Direction.Axis.X) {
					if (pos.getX() == 0) {
						blocksList.add(origin.offset(pos));
					}
				} else if (axis == Direction.Axis.Z) {
					if (pos.getZ() == 0) {
						blocksList.add(origin.offset(pos));
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
		Vector3d rotation = player.getViewVector(1);
		Vector3d combined = eyePosition.add(rotation.x * 5, rotation.y * 5, rotation.z * 5);

		BlockRayTraceResult rayTraceResult = world.clip(new RayTraceContext(eyePosition, combined,
				RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.NONE, player));
		return rayTraceResult;
	}

	public static String getTpStringFromBlockPos(BlockPos pos) {
		return "/tp " + pos.getX() + " " + pos.getY() + " " + pos.getZ();
	}
}
