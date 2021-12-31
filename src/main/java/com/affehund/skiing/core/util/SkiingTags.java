package com.affehund.skiing.core.util;

import com.affehund.skiing.Skiing;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.level.block.Block;

public class SkiingTags {
    public static class Blocks {
        public static final Tag.Named<Block> SKIING_MATERIALS = modTag("skiing_materials");
        public static final Tag.Named<Block> SNOWY_BLOCKS = modTag("snowy_blocks");

        private static Tag.Named<Block> modTag(String name) {
            return BlockTags.bind(Skiing.MOD_ID + ":" + name);
        }
    }
}
