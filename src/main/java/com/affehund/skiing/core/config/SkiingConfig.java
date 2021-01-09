package com.affehund.skiing.core.config;

import org.apache.commons.lang3.tuple.Pair;

import com.affehund.skiing.core.ModConstants;
import com.affehund.skiing.core.SkiingMod;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.config.ModConfig;

@Mod.EventBusSubscriber(modid = ModConstants.MOD_ID, bus = Bus.MOD)
public class SkiingConfig {
	
	public static class SkiingCommonConfig {
		public SkiingCommonConfig(ForgeConfigSpec.Builder builder) {
			builder.comment("Skiing Common Config").push("general");
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
		final Pair<SkiingClientConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(SkiingClientConfig::new);
		CLIENT_CONFIG_SPEC = specPair.getRight();
		CLIENT_CONFIG = specPair.getLeft();
	}
	
	public static final ForgeConfigSpec COMMON_CONFIG_SPEC;
	public static final SkiingCommonConfig COMMON_CONFIG;
	static {
		final Pair<SkiingCommonConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(SkiingCommonConfig::new);
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
