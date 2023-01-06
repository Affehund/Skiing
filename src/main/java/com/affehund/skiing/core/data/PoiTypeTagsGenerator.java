package com.affehund.skiing.core.data;

import com.affehund.skiing.core.registry.SkiingVillagers;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.PoiTypeTagsProvider;
import net.minecraft.tags.PoiTypeTags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class PoiTypeTagsGenerator extends PoiTypeTagsProvider {
    public PoiTypeTagsGenerator(DataGenerator generator, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, modId, existingFileHelper);
    }

    @Override
    protected void addTags() {
        this.tag(PoiTypeTags.ACQUIRABLE_JOB_SITE).add(SkiingVillagers.SKI_MERCHANT_POI.get());
    }
}
