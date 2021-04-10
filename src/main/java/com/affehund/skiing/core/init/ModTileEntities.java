package com.affehund.skiing.core.init;

import com.affehund.skiing.common.tile.AcaciaSkiRackTileEntity;
import com.affehund.skiing.common.tile.BirchSkiRackTileEntity;
import com.affehund.skiing.common.tile.CrimsonSkiRackTileEntity;
import com.affehund.skiing.common.tile.DarkOakSkiRackTileEntity;
import com.affehund.skiing.common.tile.JungleSkiRackTileEntity;
import com.affehund.skiing.common.tile.OakSkiRackTileEntity;
import com.affehund.skiing.common.tile.SpruceSkiRackTileEntity;
import com.affehund.skiing.common.tile.WarpedSkiRackTileEntity;
import com.affehund.skiing.core.ModConstants;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModTileEntities {
	public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister
			.create(ForgeRegistries.TILE_ENTITIES, ModConstants.MOD_ID);

	public static final RegistryObject<TileEntityType<AcaciaSkiRackTileEntity>> ACACIA_SKI_RACK_TILE_ENTITY = TILE_ENTITIES
			.register("acacia_" + ModConstants.RegistryStrings.SKI_RACK, () -> TileEntityType.Builder
					.create(AcaciaSkiRackTileEntity::new, ModBlocks.ACACIA_SKI_RACK_BLOCK.get()).build(null));

	public static final RegistryObject<TileEntityType<BirchSkiRackTileEntity>> BIRCH_SKI_RACK_TILE_ENTITY = TILE_ENTITIES
			.register("birch_" + ModConstants.RegistryStrings.SKI_RACK, () -> TileEntityType.Builder
					.create(BirchSkiRackTileEntity::new, ModBlocks.BIRCH_SKI_RACK_BLOCK.get()).build(null));

	public static final RegistryObject<TileEntityType<CrimsonSkiRackTileEntity>> CRIMSON_SKI_RACK_TILE_ENTITY = TILE_ENTITIES
			.register("crimson_" + ModConstants.RegistryStrings.SKI_RACK, () -> TileEntityType.Builder
					.create(CrimsonSkiRackTileEntity::new, ModBlocks.CRIMSON_SKI_RACK_BLOCK.get()).build(null));

	public static final RegistryObject<TileEntityType<DarkOakSkiRackTileEntity>> DARK_OAK_SKI_RACK_TILE_ENTITY = TILE_ENTITIES
			.register("dark_oak_" + ModConstants.RegistryStrings.SKI_RACK, () -> TileEntityType.Builder
					.create(DarkOakSkiRackTileEntity::new, ModBlocks.DARK_OAK_SKI_RACK_BLOCK.get()).build(null));

	public static final RegistryObject<TileEntityType<JungleSkiRackTileEntity>> JUNGLE_SKI_RACK_TILE_ENTITY = TILE_ENTITIES
			.register("jungle_" + ModConstants.RegistryStrings.SKI_RACK, () -> TileEntityType.Builder
					.create(JungleSkiRackTileEntity::new, ModBlocks.JUNGLE_SKI_RACK_BLOCK.get()).build(null));

	public static final RegistryObject<TileEntityType<OakSkiRackTileEntity>> OAK_SKI_RACK_TILE_ENTITY = TILE_ENTITIES
			.register("oak_" + ModConstants.RegistryStrings.SKI_RACK, () -> TileEntityType.Builder
					.create(OakSkiRackTileEntity::new, ModBlocks.OAK_SKI_RACK_BLOCK.get()).build(null));

	public static final RegistryObject<TileEntityType<SpruceSkiRackTileEntity>> SPRUCE_SKI_RACK_TILE_ENTITY = TILE_ENTITIES
			.register("spruce_" + ModConstants.RegistryStrings.SKI_RACK, () -> TileEntityType.Builder
					.create(SpruceSkiRackTileEntity::new, ModBlocks.SPRUCE_SKI_RACK_BLOCK.get()).build(null));

	public static final RegistryObject<TileEntityType<WarpedSkiRackTileEntity>> WARPED_SKI_RACK_TILE_ENTITY = TILE_ENTITIES
			.register("warped_" + ModConstants.RegistryStrings.SKI_RACK, () -> TileEntityType.Builder
					.create(WarpedSkiRackTileEntity::new, ModBlocks.WARPED_SKI_RACK_BLOCK.get()).build(null));
}
