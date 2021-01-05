package com.affehund.skiing.core.init;

import java.util.function.Supplier;

import javax.annotation.Nonnull;

import com.affehund.skiing.core.ModConstants;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ModItemGroup {
//	public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS,
//			ModConstants.MOD_ID);
//
//	public static final Lazy<SoundEvent> WINTER_DISC_LAZY = Lazy.of(() -> new SoundEvent(
//			new ResourceLocation(ModConstants.MOD_ID, ModConstants.RegistryStrings.LAZY_SOUND_ITEM_WINTER_DISC)));
//
//	public static final RegistryObject<SoundEvent> TUTORIAL_DISC = SOUNDS
//			.register(ModConstants.RegistryStrings.SOUND_ITEM_WINTER_DISC, WINTER_DISC_LAZY);
//
//	@SuppressWarnings("deprecation")
//	public static final RegistryObject<Item> WINTER_DISC = ITEMS.register(ModConstants.RegistryStrings.ITEM_WINTER_DISC,
//			() -> new MusicDiscItem(1, WINTER_DISC_LAZY.get(),
//					new Item.Properties().maxStackSize(1).rarity(Rarity.RARE).group(ModRegistration.MOD_ITEM_GROUP)));

	public static final ItemGroup MOD_ITEM_GROUP = new CustomItemGroup(ModConstants.MOD_ID,
			() -> new ItemStack(ModItems.SKIS_ITEM.get()), ModConstants.RegistryStrings.MOD_ITEM_GROUP);

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
		public ItemStack createIcon() {
			return iconSupplier.get();
		}
	}
}
