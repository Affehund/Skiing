package com.affehund.skiing.core.data;

import com.affehund.skiing.core.util.SkiingTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class BlockTagsGenerator extends BlockTagsProvider {
    public BlockTagsGenerator(DataGenerator dataGenerator, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(dataGenerator, modId, existingFileHelper);
    }

    @Override
    protected void addTags() {
        tag(SkiingTags.Blocks.SKIING_MATERIALS).addTags(BlockTags.PLANKS, BlockTags.LOGS);
        tag(SkiingTags.Blocks.SNOWY_BLOCKS).add(Blocks.ICE, Blocks.BLUE_ICE, Blocks.FROSTED_ICE, Blocks.PACKED_ICE)
                .add(Blocks.POWDER_SNOW, Blocks.SNOW_BLOCK, Blocks.SNOW);
    }
}
