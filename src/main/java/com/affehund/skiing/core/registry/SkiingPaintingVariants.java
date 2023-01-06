package com.affehund.skiing.core.registry;

import com.affehund.skiing.Skiing;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SkiingPaintingVariants {
    public static final DeferredRegister<PaintingVariant> PAINTINGS = DeferredRegister
            .create(ForgeRegistries.PAINTING_VARIANTS, Skiing.MOD_ID);

    public static final RegistryObject<PaintingVariant> AURORA = PAINTINGS
            .register("aurora", () -> new PaintingVariant(64, 32));

    public static final RegistryObject<PaintingVariant> BAUBLE = PAINTINGS
            .register("bauble", () -> new PaintingVariant(16, 16));

    public static final RegistryObject<PaintingVariant> GONDOLA = PAINTINGS
            .register("gondola", () -> new PaintingVariant(32, 32));

    public static final RegistryObject<PaintingVariant> RUDOLPH = PAINTINGS
            .register("rudolph", () -> new PaintingVariant(16, 16));

    public static final RegistryObject<PaintingVariant> SNOWMAN = PAINTINGS
            .register("snowman", () -> new PaintingVariant(16, 32));

    public static final RegistryObject<PaintingVariant> SKIING = PAINTINGS
            .register("skiing", () -> new PaintingVariant(64, 64));

    public static final RegistryObject<PaintingVariant> TREE = PAINTINGS
            .register("tree", () -> new PaintingVariant(64, 64));
}
