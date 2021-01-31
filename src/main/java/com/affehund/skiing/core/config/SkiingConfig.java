package com.affehund.skiing.core.config;

import org.apache.commons.lang3.tuple.Pair;

import com.affehund.skiing.core.ModConstants;
import com.affehund.skiing.core.SkiingMod;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.config.ModConfig;

@Mod.EventBusSubscriber(modid = ModConstants.MOD_ID, bus = Bus.MOD)
public class SkiingConfig {
	public static class SkiingCommonConfig {
		public final BooleanValue CROSS_SKIING;
		public final DoubleValue DEFAULT_MOMENTUM;
		public final DoubleValue IN_AIR_MOMENTUM;
		public final DoubleValue IN_AIR_FLYING_MOMENTUM;
		public final DoubleValue IN_WATER_MOMENTUM;
		public final DoubleValue ON_LAND_MOMENTUM;
		public final DoubleValue ON_SNOW_MOMENTUM;
		public final DoubleValue ON_SNOW_WITH_STICKS_MOMENTUM;
		
		public final BooleanValue ADD_SNOW_LAYERS; 
		public final IntValue ADD_SNOW_LAYERS_CHANCE; 
		public final BooleanValue REMOVE_SNOW_LAYERS; 
		public final IntValue REMOVE_SNOW_LAYERS_CHANCE; 
		
		public SkiingCommonConfig(ForgeConfigSpec.Builder builder) {
			builder.comment("Skiing Common Config").push("skis_entity");
			CROSS_SKIING = builder.comment("This sets whether you are slowed down on non-snowy blocks.")
					.define("cross_skiing", false);
			DEFAULT_MOMENTUM = builder.comment("This sets the default momentum (not very interesting).")
					.defineInRange("default_momentum", 0.4D, 0.0D, 1.0D);
			IN_AIR_MOMENTUM = builder.comment("This sets the momentum in air.")
					.defineInRange("in_air_momentum", 0.8D, 0.0D, 1.0D);
			IN_AIR_FLYING_MOMENTUM = builder.comment("This sets the momentum in air when flying.")
					.defineInRange("in_air_momentum_flying", 0.9f, 0.0D, 1.0D);
			IN_WATER_MOMENTUM = builder.comment("This sets the momentum in water.")
					.defineInRange("in_water_momentum", 0.2f, 0.0D, 1.0D);
			ON_LAND_MOMENTUM = builder.comment("This sets the momentum on land.")
					.defineInRange("on_land_momentum", 0.4f, 0.0D, 1.0D);
			ON_SNOW_MOMENTUM = builder.comment("This sets the momentum on snow.")
					.defineInRange("on_snow_momentum", 0.85F, 0.0D, 1.0D);
			ON_SNOW_WITH_STICKS_MOMENTUM = builder
					.comment("This sets the momentum on snow when holding ski sticks.")
					.defineInRange("on_snow_with_sticks_momentum", 0.92F, 0.0D, 1.0D);
			builder.pop();
			
			builder.push("snow_layering");
			ADD_SNOW_LAYERS = builder.comment("This sets whether snow layers are added when it snows.").define("add_snow_layers", true);
			ADD_SNOW_LAYERS_CHANCE = builder.comment("This sets the chance how often snow layers are added. 100 means every 5 seconds a snow layer is added.").defineInRange("add_snow_layer_chance", 100, 1, Integer.MAX_VALUE);
			REMOVE_SNOW_LAYERS = builder.comment("This sets whether snow layers are removed when it stops snowing.").define("remove_snow_layers", true);
			REMOVE_SNOW_LAYERS_CHANCE = builder.comment("This sets the chance how often snow layers are removed. 100 means every 5 seconds a snow layer is removed.").defineInRange("remove_snow_layer_chance", 200, 1, Integer.MAX_VALUE);
			builder.pop();
		}
	}

	public static class SkiingClientConfig {
		public SkiingClientConfig(ForgeConfigSpec.Builder builder) {

			builder.comment("Skiing Client Config").push("general");
			builder.pop();
		}
	}

	public static final ForgeConfigSpec CLIENT_CONFIG_SPEC;
	public static final SkiingClientConfig CLIENT_CONFIG;
	static {
		final Pair<SkiingClientConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder()
				.configure(SkiingClientConfig::new);
		CLIENT_CONFIG_SPEC = specPair.getRight();
		CLIENT_CONFIG = specPair.getLeft();
	}

	public static final ForgeConfigSpec COMMON_CONFIG_SPEC;
	public static final SkiingCommonConfig COMMON_CONFIG;
	static {
		final Pair<SkiingCommonConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder()
				.configure(SkiingCommonConfig::new);
		COMMON_CONFIG_SPEC = specPair.getRight();
		COMMON_CONFIG = specPair.getLeft();
	}

	@SubscribeEvent
	public static void onLoad(final ModConfig.Loading event) {
		SkiingMod.LOGGER.info("Loaded {} config file {}", ModConstants.MOD_ID, event.getConfig().getFileName());
	}

	@SubscribeEvent
	public static void onFileChange(final ModConfig.Reloading event) {
		SkiingMod.LOGGER.debug(ModConstants.MOD_ID, "Config just got changed on the file system!");
	}
}
