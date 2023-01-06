package com.affehund.skiing.core.registry;

import com.affehund.skiing.Skiing;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class SkiingVillagers {
    public static final DeferredRegister<PoiType> POINTS_OF_INTEREST = DeferredRegister
            .create(ForgeRegistries.POI_TYPES, Skiing.MOD_ID);

    public static final DeferredRegister<VillagerProfession> PROFESSIONS = DeferredRegister
            .create(ForgeRegistries.VILLAGER_PROFESSIONS, Skiing.MOD_ID);

    public static final RegistryObject<PoiType> SKI_MERCHANT_POI = POINTS_OF_INTEREST.register("ski_merchant",
            () -> new PoiType(ImmutableSet.copyOf(Blocks.JUKEBOX.getStateDefinition().getPossibleStates()),
                    1, 1));

    public static final RegistryObject<VillagerProfession> SKI_MERCHANT = PROFESSIONS.register("ski_merchant",
            () -> {
                Predicate<Holder<PoiType>> isPoi = (poi) -> poi.is(Objects.requireNonNull(SKI_MERCHANT_POI.getKey()));
                return new VillagerProfession("ski_merchant", isPoi,
                        isPoi, ImmutableSet.of(), ImmutableSet.of(), SoundEvents.VILLAGER_WORK_FLETCHER);
            });

    private static PoiType createPoiType(Block... blocks) {
        Collection<BlockState> blockStates = ImmutableSet.copyOf(Stream.of(blocks).map(x -> x.getStateDefinition().getPossibleStates()).flatMap(ImmutableList::stream).toArray(BlockState[]::new));
        return new PoiType(ImmutableSet.copyOf(blockStates), 1, 1);
    }

    public static void registerPointOfInterests() {
        PoiTypes.registerBlockStates(SKI_MERCHANT_POI.getHolder().orElseThrow());
    }
}
