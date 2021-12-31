package com.affehund.skiing.core.init;

import com.affehund.skiing.Skiing;
import com.affehund.skiing.common.block_entity.SkiRackBlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SkiingBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister
            .create(ForgeRegistries.BLOCK_ENTITIES, Skiing.MOD_ID);

   public static final RegistryObject<BlockEntityType<SkiRackBlockEntity>> SKI_RACK_BLOCK_ENTITY = BLOCK_ENTITIES
            .register("ski_rack", () -> BlockEntityType.Builder.of(SkiRackBlockEntity::new, SkiingBlocks.getSkiRacks()).build(null));
}
