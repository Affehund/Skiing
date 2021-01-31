package com.affehund.skiing.core.event;

import java.util.Optional;

import com.affehund.skiing.core.config.SkiingConfig;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SnowBlock;
import net.minecraft.block.SnowyDirtBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.LightType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.DebugChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ChunkHolder;
import net.minecraft.world.server.ServerChunkProvider;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;

@SuppressWarnings("deprecation")
public class SnowWorldTickEvent {
	@SubscribeEvent
	public void tick(TickEvent.WorldTickEvent event) {
		if (!(event.phase == Phase.START) || !(event.side == LogicalSide.SERVER)) {
			return;
		}

		ServerWorld world = (ServerWorld) event.world;
		ServerChunkProvider chunkProvider = world.getChunkProvider();
		if (chunkProvider.getChunkGenerator() instanceof DebugChunkGenerator) {
			return;
		}

		chunkProvider.chunkManager.getLoadedChunksIterable().forEach(holder -> {
			Optional<Chunk> optional = holder.getEntityTickingFuture().getNow(ChunkHolder.UNLOADED_CHUNK).left();
			if (optional.isPresent() && world.rand.nextInt(16) == 0) {
				Chunk chunk = optional.get();
				int x = chunk.getPos().getXStart();
				int z = chunk.getPos().getZStart();

				if (world.isRaining()) {
					// chance to add snow layers
					if (SkiingConfig.COMMON_CONFIG.ADD_SNOW_LAYERS.get()) {
						if (world.rand.nextInt(SkiingConfig.COMMON_CONFIG.ADD_SNOW_LAYERS_CHANCE.get()) == 0) {
							BlockPos pos = world.getHeight(Heightmap.Type.MOTION_BLOCKING,
									world.getBlockRandomPos(x, 0, z, 15));
							if (doesSnowGenerate(world, pos, world.getBiome(pos))) {
								addSnowLayers(world, pos);
							}
						}
					}
				} else {
					if (SkiingConfig.COMMON_CONFIG.REMOVE_SNOW_LAYERS.get()) {
						// chance to remove snow layers
						if (world.rand.nextInt(SkiingConfig.COMMON_CONFIG.REMOVE_SNOW_LAYERS_CHANCE.get()) == 0) {
							BlockPos pos = world.getHeight(Heightmap.Type.MOTION_BLOCKING,
									world.getBlockRandomPos(x, 0, z, 15));
							removeSnowLayers(world, pos);
						}
					}
				}
			}
		});
	}

	// copy and modification of Biome#doesSnowGenerate
	private boolean doesSnowGenerate(IWorldReader worldIn, BlockPos pos, Biome biome) {
		if (biome.getTemperature(pos) >= 0.15F) {
			return false;
		} else {
			if (pos.getY() >= 0 && pos.getY() < 256 && worldIn.getLightFor(LightType.BLOCK, pos) < 10) {
				BlockState blockstate = worldIn.getBlockState(pos);
				if ((blockstate.isAir(worldIn, pos) || blockstate.getBlock() instanceof SnowBlock)
						&& Blocks.SNOW.getDefaultState().isValidPosition(worldIn, pos)) {
					return true;
				}
			}
			return false;
		}
	}

	private void addSnowLayers(ServerWorld world, BlockPos pos) {
		BlockState state = world.getBlockState(pos);
		if (state.getBlock() instanceof SnowBlock) {
			int layerHeight = state.get(SnowBlock.LAYERS);
			if (layerHeight >= 8) {
				return;
			}
			float surroundingsHeight = 0;
			for (Direction direction : Direction.values()) {
				if (direction.getAxis() != Direction.Axis.Y) {
					BlockState surroundingState = world.getBlockState(pos.offset(direction));
					if (surroundingState.getBlock() instanceof SnowBlock) {
						surroundingsHeight += surroundingState.get(SnowBlock.LAYERS);
					} else if (surroundingState.isSolid()) {
						surroundingsHeight += 8;
					}
				}
			}
			surroundingsHeight /= 4;
			if (surroundingsHeight >= layerHeight) {
				if (layerHeight < 8) {
					world.setBlockState(pos, state.with(SnowBlock.LAYERS, layerHeight + 1));
				}
			}
		} else if (state.isAir(world, pos)) {
			world.setBlockState(pos, Blocks.SNOW.getDefaultState());
			if (state.hasProperty(SnowyDirtBlock.SNOWY))
				world.setBlockState(pos, state.with(SnowyDirtBlock.SNOWY, true), 2);
		}
	}

	private void removeSnowLayers(ServerWorld world, BlockPos pos) {
		BlockState state = world.getBlockState(pos);
		if (state.getBlock() instanceof SnowBlock) {
			int currentLayers = state.get(SnowBlock.LAYERS);
			if (currentLayers > 1) {
				world.setBlockState(pos, state.with(SnowBlock.LAYERS, currentLayers - 1));
			}
		}
	}
}
