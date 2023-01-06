package com.affehund.skiing.core.data;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class LootTablesGenerator extends LootTableProvider {
    public LootTablesGenerator(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected @NotNull List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
        return ImmutableList.of(
                Pair.of(Blocks::new, LootContextParamSets.BLOCK)
        );
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, @NotNull ValidationContext validationTracker) {
        map.forEach((resourceLocation, lootTable) -> LootTables.validate(validationTracker, resourceLocation, lootTable));
    }

    public static class Blocks extends BlockLoot {
        @Override
        protected void addTables() {
            /*for (Block block : SkiingBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get).toList()) {
                this.add(block, createSkiRackDrops(block));
            }*/
        }

        /*private LootTable.Builder createSkiRackDrops(Block block) {
            LootPool.Builder builder = LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1))
                    .add(applyExplosionDecay(block, LootItem.lootTableItem(block)
                            .apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY))
                            .apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY)
                                    .copy("inv", "EntityTag.inv", CopyNbtFunction.MergeStrategy.REPLACE))
                            .apply(SetContainerContents.setContents(SkiingBlockEntities.SKI_RACK_BLOCK_ENTITY.get())
                                    .withEntry(DynamicLoot.dynamicEntry(new ResourceLocation("minecraft", "contents"))))
                    ));

            return LootTable.lootTable().withPool(builder);
        }*/

        /*@Override
        protected @NotNull Iterable<Block> getKnownBlocks() {
            return SkiingBlocks.BLOCKS.getEntries().stream().map(Supplier::get).collect(Collectors.toList());
        }*/
    }
}
