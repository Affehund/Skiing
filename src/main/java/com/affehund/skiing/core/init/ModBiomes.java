package com.affehund.skiing.core.init;

import com.affehund.skiing.core.ModConstants;
import com.affehund.skiing.core.config.SkiingConfig;
import com.google.common.base.Supplier;

import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeMaker;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModBiomes {

	// deferred registry
	public static final DeferredRegister<Biome> BIOMES = DeferredRegister.create(ForgeRegistries.BIOMES,
			ModConstants.MOD_ID);

	static {
		if (SkiingConfig.COMMON_CONFIG.GENERATE_ALPS_BIOME.get()) {
			createBiome(ModConstants.RegistryStrings.ALPS_BIOME, BiomeMaker::makeVoidBiome);
		}
	}

	public static RegistryObject<Biome> createBiome(String name, Supplier<Biome> biome) {
		return BIOMES.register(name, biome);
	}

	// register the biome and add it to the biome manager
	public static RegistryKey<Biome> ALPS_BIOME_KEY = registryKey(ModConstants.RegistryStrings.ALPS_BIOME);

	public static RegistryKey<Biome> registryKey(String name) {
		return RegistryKey.getOrCreateKey(Registry.BIOME_KEY, new ResourceLocation(ModConstants.MOD_ID, name));
	}

	public static void registerBiomes() {
		if (SkiingConfig.COMMON_CONFIG.GENERATE_ALPS_BIOME.get()) {
			BiomeManager.addBiome(BiomeManager.BiomeType.ICY, new BiomeManager.BiomeEntry(ALPS_BIOME_KEY, 5));
		}
	}
}
