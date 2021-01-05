package com.affehund.skiing.core.data.gen;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ModelFile;

public class AlwaysExistingModelFile extends ModelFile {

    public AlwaysExistingModelFile(ResourceLocation resourceLocation) {
        super(resourceLocation);
    }

    @Override
    protected boolean exists() {
        return true;
    }
}
