package com.affehund.skiing.core.init;

import com.affehund.skiing.client.render.SkisItemISTER;
import com.affehund.skiing.client.render.SnowboardItemISTER;
import com.affehund.skiing.common.item.PulloverItem;
import com.affehund.skiing.common.item.PulloverMaterial;
import com.affehund.skiing.common.item.SkisItem;
import com.affehund.skiing.common.item.SnowShovel;
import com.affehund.skiing.common.item.SnowboardItem;
import com.affehund.skiing.core.ModConstants;
import com.affehund.skiing.core.utils.DrinkableItem;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemTier;
import net.minecraft.item.Items;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,
			ModConstants.MOD_ID);

	public static final RegistryObject<Item> CHOCOLATE_CUP = ITEMS.register(ModConstants.RegistryStrings.CHOCOLATE_CUP,
			() -> new DrinkableItem(new Item.Properties().stacksTo(1).tab(ModItemGroup.MOD_ITEM_GROUP)
					.food(new Food.Builder().nutrition(6).saturationMod(0.3F).build()), Items.BUCKET, true, true));

	public static final RegistryObject<Item> PULLOVER = ITEMS.register(ModConstants.RegistryStrings.PULLOVER,
			() -> new PulloverItem(PulloverMaterial.PULLOVER, EquipmentSlotType.CHEST,
					new Item.Properties().stacksTo(1).tab(ModItemGroup.MOD_ITEM_GROUP)));

	public static final RegistryObject<Item> SNOW_SHOVEL = ITEMS.register(ModConstants.RegistryStrings.ITEM_SNOW_SHOVEL,
			() -> new SnowShovel(ItemTier.IRON, 1.5F, -3.0F,
					new Item.Properties().durability(1561).tab(ModItemGroup.MOD_ITEM_GROUP)));

	public static final RegistryObject<Item> SKIS_ITEM = ITEMS.register(ModConstants.RegistryStrings.SKIS_ITEM,
			() -> new SkisItem(new Item.Properties().setISTER(() -> SkisItemISTER::new).stacksTo(1)
					.tab(ModItemGroup.MOD_ITEM_GROUP)));

	public static final RegistryObject<Item> SNOWBOARD_ITEM = ITEMS
			.register(ModConstants.RegistryStrings.SNOWBOARD_ITEM, () -> new SnowboardItem(new Item.Properties()
					.setISTER(() -> SnowboardItemISTER::new).stacksTo(1).tab(ModItemGroup.MOD_ITEM_GROUP)));

	public static final RegistryObject<Item> SKI_STICK_ITEM = ITEMS.register(
			ModConstants.RegistryStrings.SKI_STICK_ITEM,
			() -> new Item(new Item.Properties().stacksTo(2).tab(ModItemGroup.MOD_ITEM_GROUP)));
}
