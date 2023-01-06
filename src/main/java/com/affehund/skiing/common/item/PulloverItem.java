package com.affehund.skiing.common.item;

import com.affehund.skiing.Skiing;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class PulloverItem extends ArmorItem implements DyeableLeatherItem {
    private static final int DEFAULT_COLOR = 0xFFFFFF;

    public PulloverItem(ArmorMaterial armorMaterial, EquipmentSlot equipmentSlot, Properties properties) {
        super(armorMaterial, equipmentSlot, properties);
    }

    @Override
    public boolean isEnchantable(@NotNull ItemStack itemStack) {
        return false;
    }

    @Nullable
    @Override
    public String getArmorTexture(ItemStack itemStack, Entity entity, EquipmentSlot equipmentSlot, String type) {
        return Skiing.MOD_ID + ":textures/models/armor/" + Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(this)).getPath() + "_model" + (type == null ? "" : "_overlay") + ".png";
    }

    @Override
    public void fillItemCategory(@NotNull CreativeModeTab group, @NotNull NonNullList<ItemStack> items) {
        if (allowedIn(group)) {
            for (DyeColor color : DyeColor.values()) {
                ItemStack stack = new ItemStack(this);
                setColor(stack, color.getFireworkColor());
                items.add(stack);
            }
        }
    }

    @Override
    public int getColor(ItemStack stack) {
        CompoundTag nbt = stack.getTagElement("display");
        return nbt != null && nbt.contains("color", 99) ? nbt.getInt("color") : DEFAULT_COLOR;
    }
}
