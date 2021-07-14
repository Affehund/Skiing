package com.affehund.skiing.core.init;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Stream;

import com.affehund.skiing.core.ModConstants;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.village.PointOfInterestType;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.jigsaw.LegacySingleJigsawPiece;
import net.minecraft.world.gen.feature.structure.SnowyVillagePools;
import net.minecraft.world.gen.feature.template.ProcessorLists;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModVillagers {
	public static final Set<BlockState> ANY_SKI_RACK = new HashSet<BlockState>();

	public static final DeferredRegister<PointOfInterestType> POINTS_OF_INTEREST = DeferredRegister
			.create(ForgeRegistries.POI_TYPES, ModConstants.MOD_ID);

	public static final DeferredRegister<VillagerProfession> PROFESSIONS = DeferredRegister
			.create(ForgeRegistries.PROFESSIONS, ModConstants.MOD_ID);

	public static final RegistryObject<PointOfInterestType> SKIS_MERCHANT_POI = POINTS_OF_INTEREST.register(
			ModConstants.RegistryStrings.SKIS_MERCHANT_POI,
			() -> createPointOfInterestType(ModConstants.RegistryStrings.SKIS_MERCHANT_POI,
					ModBlocks.BLOCKS.getEntries().stream().map(block -> block.get()).toArray(Block[]::new)));

	public static final RegistryObject<VillagerProfession> SKIS_MERCHANT = PROFESSIONS.register(
			ModConstants.RegistryStrings.SKIS_MERCHANT,
			() -> new VillagerProfession(ModConstants.RegistryStrings.SKIS_MERCHANT, SKIS_MERCHANT_POI.get(),
					ImmutableSet.of(), ImmutableSet.of(), SoundEvents.VILLAGER_WORK_ARMORER));

	private static PointOfInterestType createPointOfInterestType(String name, Collection<BlockState> blockStates,
			int maxFreeTickets, int validRange) {
		PointOfInterestType poiType = new PointOfInterestType(name, ImmutableSet.copyOf(blockStates), maxFreeTickets,
				validRange);
		PointOfInterestType.registerBlockStates(poiType);
		return poiType;
	}

	private static PointOfInterestType createPointOfInterestType(String name, int maxFreeTickets, int validRange,
			Block... blocks) {
		return createPointOfInterestType(name,
				ImmutableSet.copyOf(Stream.of(blocks).map(x -> x.getStateDefinition().getPossibleStates())
						.flatMap(ImmutableList::stream).toArray(BlockState[]::new)),
				maxFreeTickets, validRange);
	}

	private static PointOfInterestType createPointOfInterestType(String name, Block... blocks) {
		return createPointOfInterestType(name, 1, 1, blocks);
	}

	public static void registerVillagerHouses() {
		SnowyVillagePools.bootstrap();
		addToPool(new ResourceLocation("village/snowy/houses"), new ResourceLocation(ModConstants.MOD_ID,
				"village/" + "snowy_" + ModConstants.RegistryStrings.SKIS_MERCHANT + "_house_1"), 1);
	}

	private static void addToPool(ResourceLocation pool, ResourceLocation toAdd, int weight) {
		JigsawPattern old = WorldGenRegistries.TEMPLATE_POOL.get(pool);
		List<JigsawPiece> shuffled = old.getShuffledTemplates(new Random());
		List<Pair<JigsawPiece, Integer>> newPieces = new ArrayList<>();
		for (JigsawPiece p : shuffled)
			newPieces.add(new Pair<>(p, 1));
		newPieces.add(Pair.of(new LegacySingleJigsawPiece(Either.left(toAdd), () -> ProcessorLists.EMPTY,
				JigsawPattern.PlacementBehaviour.RIGID), weight));
		ResourceLocation name = old.getName();
		Registry.register(WorldGenRegistries.TEMPLATE_POOL, pool, new JigsawPattern(pool, name, newPieces));
	}
}
