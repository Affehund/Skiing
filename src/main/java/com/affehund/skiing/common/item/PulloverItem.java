package com.affehund.skiing.common.item;

import javax.annotation.Nullable;

import com.affehund.skiing.core.ModConstants;

import net.minecraft.entity.Entity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.DyeColor;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.IDyeableArmorItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class PulloverItem extends ArmorItem implements IDyeableArmorItem {

	public static final int DEFAULT_COLOR = 0xFFFFFF;

	public PulloverItem(IArmorMaterial material, EquipmentSlotType slot, Properties builder) {
		super(material, slot, builder);
	}

	
	@Override
	public boolean isEnchantable(ItemStack stack) {
		return false;
	}

	@Nullable
	@Override
	@OnlyIn(Dist.CLIENT)
	public final String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
		String modelType = type != null ? "_overlay" : "";
		return ModConstants.ARMOR_PATH + this.getRegistryName().getPath() + "_model" + modelType + ".png";
	}

	@Override
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
		if (isInGroup(group)) {
			for (DyeColor color : DyeColor.values()) {
				ItemStack stack = new ItemStack(this);
				setColor(stack, color.getFireworkColor());
				items.add(stack);
			}
		}
	}

	@Override
	public int getColor(ItemStack stack) {
		CompoundNBT nbt = stack.getChildTag("display");
		return nbt != null && nbt.contains("color", 99) ? nbt.getInt("color") : DEFAULT_COLOR;
	}
}
