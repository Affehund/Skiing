package com.affehund.skiing;

import com.affehund.skiing.core.config.SkiingConfig;
import com.affehund.skiing.core.data.*;
import com.affehund.skiing.core.network.PacketHandler;
import com.affehund.skiing.core.registry.SkiingEntities;
import com.affehund.skiing.core.registry.SkiingItems;
import com.affehund.skiing.core.registry.SkiingPaintingVariants;
import com.affehund.skiing.core.registry.SkiingVillagers;
import com.affehund.skiing.core.util.SkiingUtils;
import com.mojang.logging.LogUtils;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.Arrays;

@Mod(Skiing.MOD_ID)
public class Skiing {

    public static final String MOD_ID = "skiing";
    public static final CreativeModeTab SKIING_TAB = new CreativeModeTab(MOD_ID) {
        @Override
        public @NotNull ItemStack makeIcon() {
            return SkiingItems.SKI_ITEM.get().getDefaultInstance();
        }
    };

    public static final Logger LOGGER = LogUtils.getLogger();

    public Skiing() {
        LOGGER.debug("Loading up {}!", MOD_ID);
        final var modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        final var forgeEventBus = MinecraftForge.EVENT_BUS;

        modEventBus.addListener(this::clientSetup);
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::gatherData);

        SkiingEntities.ENTITIES.register(modEventBus);
        SkiingItems.ITEMS.register(modEventBus);
        SkiingPaintingVariants.PAINTINGS.register(modEventBus);
        SkiingVillagers.POINTS_OF_INTEREST.register(modEventBus);
        SkiingVillagers.PROFESSIONS.register(modEventBus);

        forgeEventBus.addListener(this::registerTrades);
        forgeEventBus.register(this);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, SkiingConfig.COMMON_CONFIG_SPEC,
                MOD_ID + "-common.toml");
        LOGGER.info("{} has finished loading for now!", MOD_ID);
    }

    private void clientSetup(FMLClientSetupEvent event) {
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(SkiingVillagers::registerPointOfInterests);
        PacketHandler.registerMessages();
    }

    private void gatherData(GatherDataEvent event) {
        var generator = event.getGenerator();
        var existingFileHelper = event.getExistingFileHelper();
        var isClientProvider = event.includeClient();

        generator.addProvider(isClientProvider, new BlockTagsGenerator(generator, MOD_ID, existingFileHelper));
        generator.addProvider(isClientProvider, new PaintingVariantTagsGenerator(generator, MOD_ID, existingFileHelper));
        generator.addProvider(isClientProvider, new PoiTypeTagsGenerator(generator, MOD_ID, existingFileHelper));
        generator.addProvider(isClientProvider, new RecipeGenerator(generator));

        generator.addProvider(isClientProvider, new LanguageGenerator(generator, MOD_ID));
        generator.addProvider(isClientProvider, new ItemModelGenerator(generator, MOD_ID, existingFileHelper));
    }

    private void registerTrades(VillagerTradesEvent event) {
        if (event.getType().equals(SkiingVillagers.SKI_MERCHANT.get())) {
            var trades = event.getTrades();

            var novice = new VillagerTrades.ItemListing[]{
                    new VillagerTrades.ItemsForEmeralds(SkiingItems.SNOW_SHOVEL.get(), 5, 1, 12, 4),
                    new VillagerTrades.ItemsForEmeralds(SkiingUtils.getRandomPullover(), 8, 1, 14, 4)
            };
            var apprentice = new VillagerTrades.ItemListing[]{
                    new VillagerTrades.ItemsForEmeralds(SkiingUtils.getRandomVehicle(SkiingItems.SKI_ITEM.get()), 9, 1, 16, 3),
                    new VillagerTrades.ItemsForEmeralds(SkiingUtils.getRandomVehicle(SkiingItems.SNOWBOARD_ITEM.get()), 9, 1, 16, 3),
                    new VillagerTrades.ItemsForEmeralds(SkiingUtils.getRandomVehicle(SkiingItems.SLED_ITEM.get()), 11, 1, 12, 3)
            };
            var journeyman = new VillagerTrades.ItemListing[]{
                    new VillagerTrades.ItemsForEmeralds(SkiingUtils.getRandomSkiStick(), 7, 2, 12, 2),
                    new VillagerTrades.EmeraldForItems(SkiingItems.PULLOVER.get(), 1, 12, 3)
            };
            var expert = new VillagerTrades.ItemListing[]{
                    new VillagerTrades.EmeraldForItems(SkiingItems.SKI_ITEM.get(), 1, 14, 3),
                    new VillagerTrades.EmeraldForItems(SkiingItems.SNOWBOARD_ITEM.get(), 1, 14, 3),
            };
            var master = new VillagerTrades.ItemListing[]{
                    new VillagerTrades.EmeraldForItems(SkiingItems.SLED_ITEM.get(), 1, 16, 3),
                    new VillagerTrades.EmeraldForItems(SkiingItems.SNOW_SHOVEL.get(), 1, 14, 3),
                    new VillagerTrades.ItemsForEmeralds(Items.POWDER_SNOW_BUCKET, 1, 1, 18, 5)
            };

            Arrays.stream(novice).forEach(itemListing -> trades.get(1).add(itemListing));
            Arrays.stream(apprentice).forEach(itemListing -> trades.get(2).add(itemListing));
            Arrays.stream(journeyman).forEach(itemListing -> trades.get(3).add(itemListing));
            Arrays.stream(expert).forEach(itemListing -> trades.get(4).add(itemListing));
            Arrays.stream(master).forEach(itemListing -> trades.get(5).add(itemListing));
        }
    }
}
