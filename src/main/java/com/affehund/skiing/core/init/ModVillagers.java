package com.affehund.skiing.core.init;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.affehund.skiing.core.ModConstants;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;

import net.minecraft.block.Blocks;
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
	public static final DeferredRegister<PointOfInterestType> POINTS_OF_INTEREST = DeferredRegister
			.create(ForgeRegistries.POI_TYPES, ModConstants.MOD_ID);

	public static final DeferredRegister<VillagerProfession> PROFESSIONS = DeferredRegister
			.create(ForgeRegistries.PROFESSIONS, ModConstants.MOD_ID);

	public static RegistryObject<PointOfInterestType> SKIS_MERCHANT_POI = POINTS_OF_INTEREST.register(
			ModConstants.RegistryStrings.SKIS_MERCHANT_POI,
			() -> new PointOfInterestType(ModConstants.RegistryStrings.SKIS_MERCHANT_POI,
					PointOfInterestType.getAllStates(Blocks.CRAFTING_TABLE), 1, 1));

	public static RegistryObject<VillagerProfession> SKIS_MERCHANT = PROFESSIONS.register(
			ModConstants.RegistryStrings.SKIS_MERCHANT,
			() -> new VillagerProfession(ModConstants.RegistryStrings.SKIS_MERCHANT, SKIS_MERCHANT_POI.get(),
					ImmutableSet.of(), ImmutableSet.of(), SoundEvents.ENTITY_VILLAGER_WORK_ARMORER));
	
	public static void initVillagers() {
		registerPointOfInterests();
		registerVillagerHouses();
	}

	private static void registerPointOfInterests() {
		PointOfInterestType.registerBlockStates(ModVillagers.SKIS_MERCHANT_POI.get());
	}

	private static void registerVillagerHouses() {
		SnowyVillagePools.init();
			addToPool(new ResourceLocation("village/snowy/houses"),
					new ResourceLocation(ModConstants.MOD_ID, "village/" + "snowy_" + ModConstants.RegistryStrings.SKIS_MERCHANT + "_house_1"), 1);
	}
	
	private static void addToPool(ResourceLocation pool, ResourceLocation toAdd, int weight) {
		JigsawPattern old = WorldGenRegistries.JIGSAW_POOL.getOrDefault(pool);
		List<JigsawPiece> shuffled = old.getShuffledPieces(new Random());
		List<Pair<JigsawPiece, Integer>> newPieces = new ArrayList<>();
		for (JigsawPiece p : shuffled) newPieces.add(new Pair<>(p, 1));
		newPieces.add(Pair.of(new LegacySingleJigsawPiece(Either.left(toAdd), () -> ProcessorLists.field_244101_a, JigsawPattern.PlacementBehaviour.RIGID), weight));
		ResourceLocation name = old.getName();
		Registry.register(WorldGenRegistries.JIGSAW_POOL, pool, new JigsawPattern(pool, name, newPieces));
	}
}
