package com.affehund.skiing.core.init;

import com.affehund.skiing.common.block.SkiRackBlock;
import com.affehund.skiing.core.ModConstants;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModBlocks {
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,
			ModConstants.MOD_ID);

	public static final RegistryObject<Block> SKI_RACK = BLOCKS.register(ModConstants.RegistryStrings.SKI_RACK,
			() -> new SkiRackBlock(Block.Properties.create(Material.WOOD).hardnessAndResistance(2.0f).harvestLevel(0)
					.harvestTool(ToolType.AXE).sound(SoundType.WOOD)));
}
