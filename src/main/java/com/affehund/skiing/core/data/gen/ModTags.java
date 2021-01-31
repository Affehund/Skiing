package com.affehund.skiing.core.data.gen;

import com.affehund.skiing.core.ModConstants;

import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;

@SuppressWarnings("unused")
public class ModTags {
    public static class Blocks {
        public static final ITag.INamedTag<Block> SNOWY_BLOCKS = modTag("snowy_blocks");
        
        private static ITag.INamedTag<Block> forgeTag(String name) {
    		return BlockTags.makeWrapperTag("forge:" + name);
    	}

    	private static ITag.INamedTag<Block> modTag(String name) {
    		return BlockTags.makeWrapperTag(ModConstants.MOD_ID + ":" + name);
    	}

    	private static ITag.INamedTag<Block> vanillaTag(String name) {
    		return BlockTags.makeWrapperTag(name);
    	}
    }
    
    public static class Items {
    	public static final ITag.INamedTag<Item> PLANKS = forgeTag("planks");
    	public static final ITag.INamedTag<Item> WOODEN_SLABS = forgeTag("wooden_slabs");
    	public static final ITag.INamedTag<Item> WOOL = forgeTag("wool");
    	
        private static ITag.INamedTag<Item> forgeTag(String name) {
    		return ItemTags.makeWrapperTag("forge:" + name);
    	}

    	private static ITag.INamedTag<Item> modTag(String name, String modID) {
    		return ItemTags.makeWrapperTag(modID + ":" + name);
    	}

    	private static ITag.INamedTag<Item> vanillaTag(String name) {
    		return ItemTags.makeWrapperTag(name);
    	}
    }
    
    public static class Fluids {
        private static ITag.INamedTag<Fluid> forgeTag(String name) {
    		return FluidTags.makeWrapperTag("forge:" + name);
    	}

    	private static ITag.INamedTag<Fluid> modTag(String name, String modID) {
    		return FluidTags.makeWrapperTag(modID + ":" + name);
    	}

    	private static ITag.INamedTag<Fluid> vanillaTag(String name) {
    		return FluidTags.makeWrapperTag(name);
    	}
    }
}
