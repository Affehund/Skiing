package com.affehund.skiing.core.util;

import com.affehund.skiing.Skiing;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class SkiingTags {
    public static class Blocks {
        public static final TagKey<Block> SKIING_MATERIALS = modTag("skiing_materials");
        public static final TagKey<Block> SNOWY_BLOCKS = modTag("snowy_blocks");

        private static TagKey<Block> modTag(String name) {
            return TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(Skiing.MOD_ID, name));
        }
    }
}
