package com.affehund.skiing.core.init;

import java.util.function.Supplier;

import com.affehund.skiing.core.ModConstants;

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
	
	//create keys
    public static RegistryKey<Biome> ALPS_BIOME_KEY = registryKey(ModConstants.RegistryStrings.ALPS_BIOME);
    
    public static void registerBiomes()
    {
        BiomeManager.addBiome(BiomeManager.BiomeType.ICY, new BiomeManager.BiomeEntry(ALPS_BIOME_KEY, 5));
//        BiomeManager.addBiome(BiomeManager.BiomeType.ICY, new BiomeManager.BiomeEntry(registryKey("snowy_mountains"), 5));
    }
    
    public static RegistryKey<Biome> registryKey(String name)
    {
        return RegistryKey.getOrCreateKey(Registry.BIOME_KEY, new ResourceLocation(ModConstants.MOD_ID, name));
    }
    
    
    //reserve biomes
	public static final DeferredRegister<Biome> BIOMES = DeferredRegister.create(ForgeRegistries.BIOMES,
			ModConstants.MOD_ID);

	static {
		createBiome(ModConstants.RegistryStrings.ALPS_BIOME, BiomeMaker::makeVoidBiome);
//		createBiome("snowy_mountains", BiomeMaker::makeVoidBiome);
	}

	public static RegistryObject<Biome> createBiome(String name, Supplier<Biome> biome) {
		return BIOMES.register(name, biome);
	}
}
