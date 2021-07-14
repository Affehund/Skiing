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
		ServerChunkProvider chunkProvider = world.getChunkSource();
		if (chunkProvider.getGenerator() instanceof DebugChunkGenerator) {
			return;
		}

		chunkProvider.chunkMap.getChunks().forEach(holder -> {
			Optional<Chunk> optional = holder.getEntityTickingChunkFuture().getNow(ChunkHolder.UNLOADED_LEVEL_CHUNK).left();
			if (optional.isPresent() && world.random.nextInt(16) == 0) {
				Chunk chunk = optional.get();
				int x = chunk.getPos().getMinBlockX();
				int z = chunk.getPos().getMinBlockZ();

				if (world.isRaining()) {
					// chance to add snow layers
					if (SkiingConfig.COMMON_CONFIG.ADD_SNOW_LAYERS.get()) {
						if (world.random.nextInt(SkiingConfig.COMMON_CONFIG.ADD_SNOW_LAYERS_CHANCE.get()) == 0) {
							BlockPos pos = world.getHeightmapPos(Heightmap.Type.MOTION_BLOCKING,
									world.getBlockRandomPos(x, 0, z, 15));
							if (doesSnowGenerate(world, pos, world.getBiome(pos))) {
								addSnowLayers(world, pos);
							}
						}
					}
				} else {
					if (SkiingConfig.COMMON_CONFIG.REMOVE_SNOW_LAYERS.get()) {
						// chance to remove snow layers
						if (world.random.nextInt(SkiingConfig.COMMON_CONFIG.REMOVE_SNOW_LAYERS_CHANCE.get()) == 0) {
							BlockPos pos = world.getHeightmapPos(Heightmap.Type.MOTION_BLOCKING,
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
			if (pos.getY() >= 0 && pos.getY() < 256 && worldIn.getBrightness(LightType.BLOCK, pos) < 10) {
				BlockState blockstate = worldIn.getBlockState(pos);
				if ((blockstate.isAir(worldIn, pos) || blockstate.getBlock() instanceof SnowBlock)
						&& Blocks.SNOW.defaultBlockState().canSurvive(worldIn, pos)) {
					return true;
				}
			}
			return false;
		}
	}

	private void addSnowLayers(ServerWorld world, BlockPos pos) {
		BlockState state = world.getBlockState(pos);
		if (state.getBlock() instanceof SnowBlock) {
			int layerHeight = state.getValue(SnowBlock.LAYERS);
			if (layerHeight >= 8) {
				return;
			}
			float surroundingsHeight = 0;
			for (Direction direction : Direction.values()) {
				if (direction.getAxis() != Direction.Axis.Y) {
					BlockState surroundingState = world.getBlockState(pos.relative(direction));
					if (surroundingState.getBlock() instanceof SnowBlock) {
						surroundingsHeight += surroundingState.getValue(SnowBlock.LAYERS);
					} else if (surroundingState.canOcclude()) {
						surroundingsHeight += 8;
					}
				}
			}
			surroundingsHeight /= 4;
			if (surroundingsHeight >= layerHeight) {
				if (layerHeight < 8) {
					world.setBlockAndUpdate(pos, state.setValue(SnowBlock.LAYERS, layerHeight + 1));
				}
			}
		} else if (state.isAir(world, pos)) {
			world.setBlockAndUpdate(pos, Blocks.SNOW.defaultBlockState());
			if (state.hasProperty(SnowyDirtBlock.SNOWY))
				world.setBlock(pos, state.setValue(SnowyDirtBlock.SNOWY, true), 2);
		}
	}

	private void removeSnowLayers(ServerWorld world, BlockPos pos) {
		BlockState state = world.getBlockState(pos);
		if (state.getBlock() instanceof SnowBlock) {
			int currentLayers = state.getValue(SnowBlock.LAYERS);
			if (currentLayers > 1) {
				world.setBlockAndUpdate(pos, state.setValue(SnowBlock.LAYERS, currentLayers - 1));
			}
		}
	}
}
