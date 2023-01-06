package com.affehund.skiing.core.util;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.IExtensibleEnum;

public enum SkiingMaterial implements IExtensibleEnum {
    ACACIA_PLANKS(Blocks.ACACIA_PLANKS, Blocks.ACACIA_SLAB, Blocks.ACACIA_STAIRS),
    BIRCH_PLANKS(Blocks.BIRCH_PLANKS, Blocks.BIRCH_SLAB, Blocks.BIRCH_STAIRS),
    CRIMSON_PLANKS(Blocks.CRIMSON_PLANKS, Blocks.CRIMSON_SLAB, Blocks.CRIMSON_STAIRS),
    DARK_OAK_PLANKS(Blocks.DARK_OAK_PLANKS, Blocks.DARK_OAK_SLAB, Blocks.DARK_OAK_STAIRS),
    JUNGLE_PLANKS(Blocks.JUNGLE_PLANKS, Blocks.JUNGLE_SLAB, Blocks.JUNGLE_STAIRS),
    OAK_PLANKS(Blocks.OAK_PLANKS, Blocks.OAK_SLAB, Blocks.OAK_STAIRS),
    MANGROVE_PLANKS(Blocks.MANGROVE_PLANKS, Blocks.MANGROVE_SLAB, Blocks.MANGROVE_STAIRS),
    SPRUCE_PLANKS(Blocks.SPRUCE_PLANKS, Blocks.SPRUCE_SLAB, Blocks.SPRUCE_STAIRS),
    WARPED_PLANKS(Blocks.WARPED_PLANKS, Blocks.WARPED_SLAB, Blocks.WARPED_STAIRS);


    private final Block block;
    private final Block slabBlock;
    private final Block stairsBlock;

    SkiingMaterial(Block block, Block slabBlock, Block stairsBlock) {
        this.block = block;
        this.slabBlock = slabBlock;
        this.stairsBlock = stairsBlock;
    }

    public static SkiingMaterial create(String string, Block baseBlock, Block slabBlock, Block stairsBlock) {
        throw new IllegalStateException("Enum not extended");
    }

    public static SkiingMaterial getRandom() {
        return values()[(int) (Math.random() * values().length)];
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
}
