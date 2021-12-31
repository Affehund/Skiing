package com.affehund.skiing.core.util;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public enum SkiingMaterial {
    ACACIA_PLANKS(Blocks.ACACIA_PLANKS, Blocks.ACACIA_SLAB, Blocks.ACACIA_STAIRS),
    ACACIA_LOG(Blocks.ACACIA_LOG, Blocks.ACACIA_SLAB, Blocks.ACACIA_STAIRS),
    ACACIA_WOOD(Blocks.ACACIA_WOOD, Blocks.ACACIA_SLAB, Blocks.ACACIA_STAIRS),
    STRIPPED_ACACIA_LOG(Blocks.STRIPPED_ACACIA_LOG, Blocks.ACACIA_SLAB, Blocks.ACACIA_STAIRS),
    STRIPPED_ACACIA_WOOD(Blocks.STRIPPED_ACACIA_WOOD, Blocks.ACACIA_SLAB, Blocks.ACACIA_STAIRS),

    BIRCH_PLANKS(Blocks.BIRCH_PLANKS, Blocks.BIRCH_SLAB, Blocks.BIRCH_STAIRS),
    BIRCH_LOG(Blocks.BIRCH_LOG, Blocks.BIRCH_SLAB, Blocks.BIRCH_STAIRS),
    BIRCH_WOOD(Blocks.BIRCH_WOOD, Blocks.BIRCH_SLAB, Blocks.BIRCH_STAIRS),
    STRIPPED_BIRCH_LOG(Blocks.STRIPPED_BIRCH_LOG, Blocks.BIRCH_SLAB, Blocks.BIRCH_STAIRS),
    STRIPPED_BIRCH_WOOD(Blocks.STRIPPED_BIRCH_WOOD, Blocks.BIRCH_SLAB, Blocks.BIRCH_STAIRS),

    CRIMSON_PLANKS(Blocks.CRIMSON_PLANKS, Blocks.CRIMSON_SLAB, Blocks.CRIMSON_STAIRS),
    CRIMSON_LOG(Blocks.CRIMSON_HYPHAE, Blocks.CRIMSON_SLAB, Blocks.CRIMSON_STAIRS),
    CRIMSON_WOOD(Blocks.CRIMSON_STEM, Blocks.CRIMSON_SLAB, Blocks.CRIMSON_STAIRS),
    STRIPPED_CRIMSON_LOG(Blocks.STRIPPED_CRIMSON_HYPHAE, Blocks.CRIMSON_SLAB, Blocks.CRIMSON_STAIRS),
    STRIPPED_CRIMSON_WOOD(Blocks.STRIPPED_CRIMSON_STEM, Blocks.CRIMSON_SLAB, Blocks.CRIMSON_STAIRS),

    DARK_OAK_PLANKS(Blocks.DARK_OAK_PLANKS, Blocks.DARK_OAK_SLAB, Blocks.DARK_OAK_STAIRS),
    DARK_OAK_LOG(Blocks.DARK_OAK_LOG, Blocks.DARK_OAK_SLAB, Blocks.DARK_OAK_STAIRS),
    DARK_OAK_WOOD(Blocks.DARK_OAK_WOOD, Blocks.DARK_OAK_SLAB, Blocks.DARK_OAK_STAIRS),
    STRIPPED_DARK_OAK_LOG(Blocks.STRIPPED_DARK_OAK_LOG, Blocks.DARK_OAK_SLAB, Blocks.DARK_OAK_STAIRS),
    STRIPPED_DARK_OAK_WOOD(Blocks.STRIPPED_DARK_OAK_WOOD, Blocks.DARK_OAK_SLAB, Blocks.DARK_OAK_STAIRS),

    JUNGLE_PLANKS(Blocks.JUNGLE_PLANKS, Blocks.JUNGLE_SLAB, Blocks.JUNGLE_STAIRS),
    JUNGLE_LOG(Blocks.JUNGLE_LOG, Blocks.JUNGLE_SLAB, Blocks.JUNGLE_STAIRS),
    JUNGLE_WOOD(Blocks.JUNGLE_WOOD, Blocks.JUNGLE_SLAB, Blocks.JUNGLE_STAIRS),
    STRIPPED_JUNGLE_LOG(Blocks.STRIPPED_JUNGLE_LOG, Blocks.JUNGLE_SLAB, Blocks.JUNGLE_STAIRS),
    STRIPPED_JUNGLE_WOOD(Blocks.STRIPPED_JUNGLE_WOOD, Blocks.JUNGLE_SLAB, Blocks.JUNGLE_STAIRS),

    OAK_PLANKS(Blocks.OAK_PLANKS, Blocks.OAK_SLAB, Blocks.OAK_STAIRS),
    OAK_LOG(Blocks.OAK_LOG, Blocks.OAK_SLAB, Blocks.OAK_STAIRS),
    OAK_WOOD(Blocks.OAK_WOOD, Blocks.OAK_SLAB, Blocks.OAK_STAIRS),
    STRIPPED_OAK_LOG(Blocks.STRIPPED_OAK_LOG, Blocks.OAK_SLAB, Blocks.OAK_STAIRS),
    STRIPPED_OAK_WOOD(Blocks.STRIPPED_OAK_WOOD, Blocks.OAK_SLAB, Blocks.OAK_STAIRS),

    WARPED_PLANKS(Blocks.WARPED_PLANKS, Blocks.WARPED_SLAB, Blocks.WARPED_STAIRS),
    WARPED_LOG(Blocks.WARPED_HYPHAE, Blocks.WARPED_SLAB, Blocks.WARPED_STAIRS),
    WARPED_WOOD(Blocks.WARPED_STEM, Blocks.WARPED_SLAB, Blocks.WARPED_STAIRS),
    STRIPPED_WARPED_LOG(Blocks.STRIPPED_WARPED_HYPHAE, Blocks.WARPED_SLAB, Blocks.WARPED_STAIRS),
    STRIPPED_WARPED_WOOD(Blocks.STRIPPED_WARPED_STEM, Blocks.WARPED_SLAB, Blocks.WARPED_STAIRS),

    SPRUCE_PLANKS(Blocks.SPRUCE_PLANKS, Blocks.SPRUCE_SLAB, Blocks.SPRUCE_STAIRS),
    SPRUCE_LOG(Blocks.SPRUCE_LOG, Blocks.SPRUCE_SLAB, Blocks.SPRUCE_STAIRS),
    SPRUCE_WOOD(Blocks.SPRUCE_WOOD, Blocks.SPRUCE_SLAB, Blocks.SPRUCE_STAIRS),
    STRIPPED_SPRUCE_LOG(Blocks.STRIPPED_SPRUCE_LOG, Blocks.SPRUCE_SLAB, Blocks.SPRUCE_STAIRS),
    STRIPPED_SPRUCE_WOOD(Blocks.STRIPPED_SPRUCE_WOOD, Blocks.SPRUCE_SLAB, Blocks.SPRUCE_STAIRS);

    private final Block block;
    private final Block slabBlock;
    private final Block stairsBlock;
    
    SkiingMaterial(Block block, Block slabBlock, Block stairsBlock) {
        this.block = block;
        this.slabBlock = slabBlock;
        this.stairsBlock = stairsBlock;
    }

    public Block getBlock() {
        return block;
    }

    public Block getSlabBlock() {
        return slabBlock;
    }

    public Block getStairsBlock() {
        return stairsBlock;
    }

    public static SkiingMaterial getRandom() {
        return values()[(int) (Math.random() * values().length)];
    }
}
