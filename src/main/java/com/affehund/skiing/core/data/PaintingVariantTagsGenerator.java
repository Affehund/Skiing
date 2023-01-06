package com.affehund.skiing.core.data;

import com.affehund.skiing.core.registry.SkiingPaintingVariants;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.PaintingVariantTagsProvider;
import net.minecraft.tags.PaintingVariantTags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class PaintingVariantTagsGenerator extends PaintingVariantTagsProvider {
    public PaintingVariantTagsGenerator(DataGenerator generator, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, modId, existingFileHelper);
    }

    @Override
    protected void addTags() {
        this.tag(PaintingVariantTags.PLACEABLE).add(SkiingPaintingVariants.AURORA.get(), SkiingPaintingVariants.BAUBLE.get(), SkiingPaintingVariants.GONDOLA.get(), SkiingPaintingVariants.RUDOLPH.get(), SkiingPaintingVariants.SNOWMAN.get(), SkiingPaintingVariants.SKIING.get(), SkiingPaintingVariants.TREE.get());
    }
}
