package com.affehund.skiing.core.init;

import com.affehund.skiing.Skiing;
import com.affehund.skiing.common.block.SkiRackBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SkiingBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Skiing.MOD_ID);

    static {
        for (WoodType woodType : WoodType.values().toList()) {
            BLOCKS.register(woodType.name() + "_ski_rack", () -> new SkiRackBlock(BlockBehaviour.Properties.of(Material.WOOD).sound(SoundType.WOOD).strength(2.5F)));
        }
    }

    public static Block[] getSkiRacks() {
        return SkiingBlocks.BLOCKS.getEntries().stream().filter(block -> block.get() instanceof SkiRackBlock).map(RegistryObject::get).toArray(Block[]::new);
    }
}
