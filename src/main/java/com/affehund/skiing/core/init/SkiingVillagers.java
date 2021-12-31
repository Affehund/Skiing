package com.affehund.skiing.core.init;

import com.affehund.skiing.Skiing;
import com.affehund.skiing.common.item.PulloverItem;
import com.affehund.skiing.common.item.SkiStickItem;
import com.affehund.skiing.core.util.SkiingMaterial;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Stream;

public class SkiingVillagers {
    public static final DeferredRegister<PoiType> POINTS_OF_INTEREST = DeferredRegister
            .create(ForgeRegistries.POI_TYPES, Skiing.MOD_ID);

    public static final DeferredRegister<VillagerProfession> PROFESSIONS = DeferredRegister
            .create(ForgeRegistries.PROFESSIONS, Skiing.MOD_ID);

    public static final RegistryObject<PoiType> SKI_MERCHANT_POI = POINTS_OF_INTEREST.register("ski_merchant_poi",
            () -> createPoiType("ski_merchant_poi",
                    SkiingBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get).toArray(Block[]::new)));

    public static final RegistryObject<VillagerProfession> SKI_MERCHANT = PROFESSIONS.register("ski_merchant",
            () -> new VillagerProfession("ski_merchant", SKI_MERCHANT_POI.get(), ImmutableSet.of(), ImmutableSet.of(), SoundEvents.VILLAGER_WORK_FLETCHER));

    public static void initVillagers() {
        registerTrades();
        registerPointOfInterests();
    }

    private static PoiType createPoiType(String name, Block... blocks) {
        Collection<BlockState> blockStates = ImmutableSet.copyOf(Stream.of(blocks).map(x -> x.getStateDefinition().getPossibleStates()).flatMap(ImmutableList::stream).toArray(BlockState[]::new));
        PoiType poiType = new PoiType(name, ImmutableSet.copyOf(blockStates), 1, 1);
        PoiType.registerBlockStates(poiType);
        return poiType;
    }

    private static void registerPointOfInterests() {
        PoiType.registerBlockStates(SKI_MERCHANT_POI.get());
    }

    private static void registerTrades() {
        VillagerTrades.ItemListing[] novice = new VillagerTrades.ItemListing[] {
                new VillagerTrades.ItemsForEmeralds(SkiingItems.SNOW_SHOVEL.get(), 5, 1, 12, 4),
                new VillagerTrades.ItemsForEmeralds(getRandomPullover(), 8, 1, 14, 4)
        };
        VillagerTrades.ItemListing[] apprentice = new VillagerTrades.ItemListing[] {
                new VillagerTrades.ItemsForEmeralds(getRandomVehicle(SkiingItems.SKI_ITEM.get()), 9, 1, 16, 3),
                new VillagerTrades.ItemsForEmeralds(getRandomVehicle(SkiingItems.SNOWBOARD_ITEM.get()), 9, 1, 16, 3),
                new VillagerTrades.ItemsForEmeralds(getRandomVehicle(SkiingItems.SLED_ITEM.get()), 11, 1, 12, 3)
        };
        VillagerTrades.ItemListing[] journeyman = new VillagerTrades.ItemListing[] {
                new VillagerTrades.ItemsForEmeralds(getRandomSkiStick(), 7, 2, 12, 2),
                new VillagerTrades.EmeraldForItems(SkiingItems.PULLOVER.get(), 1, 12, 3)
        };
        VillagerTrades.ItemListing[] expert = new VillagerTrades.ItemListing[] {
                new VillagerTrades.EmeraldForItems(SkiingItems.SKI_ITEM.get(), 1, 14, 3),
                new VillagerTrades.EmeraldForItems(SkiingItems.SNOWBOARD_ITEM.get(), 1, 14, 3),
        };
        VillagerTrades.ItemListing[] master = new VillagerTrades.ItemListing[] {
                new VillagerTrades.EmeraldForItems(SkiingItems.SLED_ITEM.get(), 1, 16, 3),
                new VillagerTrades.EmeraldForItems(SkiingItems.SNOW_SHOVEL.get(), 1, 14, 3),
                new VillagerTrades.ItemsForEmeralds(Items.POWDER_SNOW_BUCKET, 1, 1, 18, 5)
        };

        VillagerTrades.TRADES.put(SKI_MERCHANT.get(), VillagerTrades.toIntMap(ImmutableMap.of(1, novice, 2, apprentice, 3, journeyman, 4, expert, 5, master)));
    }

    private static ItemStack getRandomPullover() {
        ItemStack stack = new ItemStack(SkiingItems.PULLOVER.get());
        ((PulloverItem) stack.getItem()).setColor(stack, DyeColor.values()[new Random().nextInt(DyeColor.values().length)].getFireworkColor());
        return stack;
    }

    private static ItemStack getRandomVehicle(Item item) {
        ItemStack stack = new ItemStack(item);
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putString("skiing_material", Objects.requireNonNull(SkiingMaterial.getRandom().getBlock().getRegistryName()).toString());
        stack.addTagElement("EntityTag", compoundTag);
        return stack;
    }

    private static Item getRandomSkiStick() {
        List<Item> skiSticks = ForgeRegistries.ITEMS.getValues().stream().filter(i -> i instanceof SkiStickItem).toList();
        return skiSticks.get(new Random().nextInt(skiSticks.size()));
    }
}
