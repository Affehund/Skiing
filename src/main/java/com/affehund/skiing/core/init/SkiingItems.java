package com.affehund.skiing.core.init;

import com.affehund.skiing.Skiing;
import com.affehund.skiing.common.item.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SkiingItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Skiing.MOD_ID);

    public static final RegistryObject<Item> SKI_ITEM = ITEMS.register("ski",
            () -> new SkiItem(new Item.Properties().stacksTo(1).tab(Skiing.SKIING_TAB)));

    public static final RegistryObject<Item> SLED_ITEM = ITEMS.register("sled",
            () -> new SledItem(new Item.Properties().stacksTo(1).tab(Skiing.SKIING_TAB)));

    public static final RegistryObject<Item> SNOWBOARD_ITEM = ITEMS.register("snowboard",
            () -> new SnowboardItem(new Item.Properties().stacksTo(1).tab(Skiing.SKIING_TAB)));

    public static final RegistryObject<Item> SNOW_SHOVEL = ITEMS.register("snow_shovel",
            () -> new SnowShovelItem(1.5F, -3.0F, Tiers.IRON, BlockTags.SNOW,
                    new Item.Properties().durability(1561).tab(Skiing.SKIING_TAB)));

    static {
        for (WoodType woodType : WoodType.values().toList()) {
            ITEMS.register(woodType.name() + "_ski_stick",
                    () -> new SkiStickItem(new Item.Properties().stacksTo(2).tab(Skiing.SKIING_TAB), ForgeRegistries.BLOCKS.getValue(new ResourceLocation(woodType.name() + "_planks"))));
        }
    }

    public static final RegistryObject<Item> PULLOVER = ITEMS.register("pullover",
            () -> new PulloverItem(ArmorMaterials.LEATHER, EquipmentSlot.CHEST,
                    new Item.Properties().stacksTo(1).tab(Skiing.SKIING_TAB)));
}
