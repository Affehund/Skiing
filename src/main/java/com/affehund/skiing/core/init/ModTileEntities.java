package com.affehund.skiing.core.init;

import com.affehund.skiing.common.tile.SkiRackTileEntity;
import com.affehund.skiing.core.ModConstants;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModTileEntities {
	public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister
			.create(ForgeRegistries.TILE_ENTITIES, ModConstants.MOD_ID);
	
	public static final RegistryObject<TileEntityType<SkiRackTileEntity>> SKI_RACK_TILE_ENTITY = TILE_ENTITIES
			.register(ModConstants.RegistryStrings.SKI_RACK, () -> TileEntityType.Builder
					.create(SkiRackTileEntity::new, ModBlocks.SKI_RACK.get()).build(null));
}
