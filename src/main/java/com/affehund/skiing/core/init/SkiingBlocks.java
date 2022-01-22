package com.affehund.skiing.core.init;

import com.affehund.skiing.Skiing;
import com.affehund.skiing.common.block.SkiRackBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.Objects;

public class SkiingBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Skiing.MOD_ID);

    static {
        List<Block> skiingBlocks = List.of(Blocks.OAK_PLANKS, Blocks.SPRUCE_PLANKS, Blocks.BIRCH_PLANKS, Blocks.ACACIA_PLANKS, Blocks.JUNGLE_PLANKS, Blocks.DARK_OAK_PLANKS, Blocks.CRIMSON_PLANKS, Blocks.WARPED_PLANKS);
        for (Block block : skiingBlocks) {
            BLOCKS.register(Objects.requireNonNull(block.getRegistryName()).getPath().replace("_planks", "") + "_ski_rack", () -> new SkiRackBlock(BlockBehaviour.Properties.of(Material.WOOD).sound(SoundType.WOOD).strength(2.5F)));
        }
    }

    public static Block[] getSkiRacks() {
        return SkiingBlocks.BLOCKS.getEntries().stream().filter(block -> block.get() instanceof SkiRackBlock).map(RegistryObject::get).toArray(Block[]::new);
    }
}
