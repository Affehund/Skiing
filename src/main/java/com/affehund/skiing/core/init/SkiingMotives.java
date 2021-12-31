package com.affehund.skiing.core.init;

import com.affehund.skiing.Skiing;
import net.minecraft.world.entity.decoration.Motive;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SkiingMotives {
    public static final DeferredRegister<Motive> MOTIVES = DeferredRegister
            .create(ForgeRegistries.PAINTING_TYPES, Skiing.MOD_ID);

    public static final RegistryObject<Motive> AURORA = MOTIVES
            .register("aurora", () -> new Motive(64, 32));

    public static final RegistryObject<Motive> BAUBLE = MOTIVES
            .register("bauble", () -> new Motive(16, 16));

    public static final RegistryObject<Motive> GONDOLA = MOTIVES
            .register("gondola", () -> new Motive(32, 32));

    public static final RegistryObject<Motive> RUDOLPH = MOTIVES
            .register("rudolph", () -> new Motive(16, 16));

    public static final RegistryObject<Motive> SNOWMAN = MOTIVES
            .register("snowman", () -> new Motive(16, 32));

    public static final RegistryObject<Motive> SKIING = MOTIVES
            .register("skiing", () -> new Motive(64, 64));

    public static final RegistryObject<Motive> TREE = MOTIVES
            .register("tree", () -> new Motive(64, 64));
}
