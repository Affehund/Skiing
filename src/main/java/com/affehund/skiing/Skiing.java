package com.affehund.skiing;

import com.affehund.skiing.core.compat.SkiingCompatHandler;
import com.affehund.skiing.core.config.SkiingConfig;
import com.affehund.skiing.core.data.*;
import com.affehund.skiing.core.init.*;
import com.affehund.skiing.core.network.PacketHandler;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import net.minecraftforge.registries.RegistryObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@Mod(Skiing.MOD_ID)
public class Skiing {
    public static final String MOD_ID = "skiing";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static final CreativeModeTab SKIING_TAB = new CreativeModeTab(MOD_ID) {
        @Override
        public @NotNull ItemStack makeIcon() {
            return SkiingItems.SKI_ITEM.get().getDefaultInstance();
        }
    };

    public Skiing() {
        LOGGER.debug("Loading up {}!", MOD_ID);
        final var modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        final var forgeEventBus = MinecraftForge.EVENT_BUS;

        modEventBus.addListener(this::clientSetup);
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::gatherData);
        modEventBus.addGenericListener(Item.class, this::registerBlockItems);

        SkiingBlocks.BLOCKS.register(modEventBus);
        SkiingBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        SkiingEntities.ENTITIES.register(modEventBus);
        SkiingItems.ITEMS.register(modEventBus);
        SkiingMotives.MOTIVES.register(modEventBus);
        SkiingVillagers.POINTS_OF_INTEREST.register(modEventBus);
        SkiingVillagers.PROFESSIONS.register(modEventBus);

        forgeEventBus.register(this);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, SkiingConfig.COMMON_CONFIG_SPEC,
                MOD_ID + "-common.toml");
        LOGGER.info("{} has finished loading for now!", MOD_ID);
    }

    private void clientSetup(FMLClientSetupEvent event) {
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(SkiingVillagers::initVillagers);
        PacketHandler.registerMessages();
        SkiingCompatHandler.initCompats();
    }

    private void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        if (event.includeServer()) {
            BlockTagsProvider blockTagsProvider = new BlockTagsGenerator(generator, MOD_ID, existingFileHelper);
            generator.addProvider(blockTagsProvider);
            generator.addProvider(new RecipeGenerator(generator));
            generator.addProvider(new LootTablesGenerator(generator));
        }
        if (event.includeClient()) {
            generator.addProvider(new LanguageGenerator(generator, MOD_ID));
            generator.addProvider(new ItemModelGenerator(generator, MOD_ID, existingFileHelper));
        }
    }

    private void registerBlockItems(RegistryEvent.Register<Item> event) {
        SkiingBlocks.BLOCKS.getEntries().stream()
                .map(RegistryObject::get).forEach(block -> {
                    var properties = new Item.Properties().tab(Skiing.SKIING_TAB);
                    var blockItem = new BlockItem(block, properties);
                    blockItem.setRegistryName(Objects.requireNonNull(block.getRegistryName()));
                    event.getRegistry().register(blockItem);
                });
        LOGGER.debug("Registered block items!");
    }
}
