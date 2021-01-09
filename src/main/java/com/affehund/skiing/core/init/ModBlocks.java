package com.affehund.skiing.core.init;

import com.affehund.skiing.common.block.SkiRackBlock;
import com.affehund.skiing.core.ModConstants;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModBlocks {
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,
			ModConstants.MOD_ID);
	public static final DeferredRegister<Item> ITEMS = ModItems.ITEMS;

	public static final RegistryObject<Block> SKI_RACK = BLOCKS.register(ModConstants.RegistryStrings.SKI_RACK,
			() -> new SkiRackBlock(Block.Properties.create(Material.WOOD).hardnessAndResistance(2.0f).harvestLevel(0)
					.harvestTool(ToolType.AXE).sound(SoundType.WOOD)));

//	public static final RegistryObject<Block> SKI_RACK = register(
//			ModConstants.RegistryStrings.SKI_RACK, () -> new SkiRackBlock(Block.Properties.create(Material.WOOD)
//					.hardnessAndResistance(2.0f).harvestLevel(0).harvestTool(ToolType.AXE).sound(SoundType.WOOD)),
//			() -> skiRackRenderer());
//
//	private static <T extends Block> RegistryObject<T> register(String name, Supplier<? extends T> sup,
//			Supplier<Callable<ItemStackTileEntityRenderer>> renderMethod) {
//		return register(name, sup, block -> item(block, renderMethod));
//	}
//
//	private static <T extends Block> RegistryObject<T> register(String name, Supplier<? extends T> sup,
//			Function<RegistryObject<T>, Supplier<? extends Item>> itemCreator) {
//		RegistryObject<T> ret = registerNoItem(name, sup);
//		ITEMS.register(name, itemCreator.apply(ret));
//		return ret;
//	}
//
//	private static <T extends Block> RegistryObject<T> registerNoItem(String name, Supplier<? extends T> sup) {
//		return BLOCKS.register(name, sup);
//	}
//
//	private static Supplier<BlockItem> item(final RegistryObject<? extends Block> block,
//			final Supplier<Callable<ItemStackTileEntityRenderer>> renderMethod) {
//		return () -> new SkiRackItem(block.get(),
//				new Item.Properties().group(ModItemGroup.MOD_ITEM_GROUP).setISTER(() -> /*skiRackRenderer()*/ItemStackRenderer::get).maxStackSize(1));
//	}
//	
//	@OnlyIn(Dist.CLIENT)
//	private static Callable<ItemStackTileEntityRenderer> skiRackRenderer() {
//		return () -> new SkiRackISTER<TileEntity>(() -> new SkiRackTileEntity());
//	}

//	@OnlyIn(Dist.CLIENT)
//	private static Callable<ItemStackTileEntityRenderer> skiRackRenderer() {
//		return () -> new SkiRackISTER<TileEntity>(() -> new SkiRackTileEntity());
//	}

//		  @OnlyIn(Dist.CLIENT)
//		  private static Callable<ItemStackTileEntityRenderer> ironShulkerBoxRenderer() {
//		    return () -> new IronShulkerBoxItemStackRenderer(() -> new IronShulkerBoxTileEntity());
//		  }
}
