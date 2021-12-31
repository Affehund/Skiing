package com.affehund.skiing.core.init;

import com.affehund.skiing.Skiing;
import com.affehund.skiing.common.entity.SkiEntity;
import com.affehund.skiing.common.entity.SledEntity;
import com.affehund.skiing.common.entity.SnowboardEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SkiingEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES,
            Skiing.MOD_ID);

    public static final RegistryObject<EntityType<SkiEntity>> SKI_ENTITY = ENTITIES.register("ski",
            () -> EntityType.Builder.<SkiEntity>of(SkiEntity::new, MobCategory.MISC)
                    .sized(1.5F, 0.109375F)
                    .clientTrackingRange(10)
                    .build(new ResourceLocation(Skiing.MOD_ID, "ski").toString()));

    public static final RegistryObject<EntityType<SledEntity>> SLED_ENTITY = ENTITIES.register("sled",
            () -> EntityType.Builder.<SledEntity>of(SledEntity::new, MobCategory.MISC)
                    .sized(1.375F, 0.4375F)
                    .clientTrackingRange(10)
                    .build(new ResourceLocation(Skiing.MOD_ID, "sled").toString()));

    public static final RegistryObject<EntityType<SnowboardEntity>> SNOWBOARD_ENTITY = ENTITIES.register("snowboard",
            () -> EntityType.Builder.<SnowboardEntity>of(SnowboardEntity::new, MobCategory.MISC)
                    .sized(1.25F, 0.109375F)
                    .clientTrackingRange(10)
                    .build(new ResourceLocation(Skiing.MOD_ID, "snowboard").toString()));
}
