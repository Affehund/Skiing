package com.affehund.skiing.core.init;

import java.util.function.Supplier;

import javax.annotation.Nonnull;

import com.affehund.skiing.core.ModConstants;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ModItemGroup {
	public static final ItemGroup MOD_ITEM_GROUP = new CustomItemGroup(ModConstants.MOD_ID,
			() -> new ItemStack(ModItems.SKIS_ITEM.get()), ModConstants.MOD_ID);

	public static final class CustomItemGroup extends ItemGroup {
		@Nonnull
		private final Supplier<ItemStack> iconSupplier;

		public CustomItemGroup(@Nonnull final String name, @Nonnull final Supplier<ItemStack> iconSupplier,
				String string) {
			super(string);
			this.iconSupplier = iconSupplier;
		}

		@Override
		@Nonnull
		public ItemStack makeIcon() {
			return iconSupplier.get();
		}
	}
}
