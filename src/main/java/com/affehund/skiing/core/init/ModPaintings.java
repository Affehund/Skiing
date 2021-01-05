package com.affehund.skiing.core.init;

import com.affehund.skiing.core.ModConstants;

import net.minecraft.entity.item.PaintingType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModPaintings {
	public static final DeferredRegister<PaintingType> PAINTINGS = DeferredRegister
			.create(ForgeRegistries.PAINTING_TYPES, ModConstants.MOD_ID);
	
	public static final RegistryObject<PaintingType> SKIING_PAINTING = PAINTINGS
			.register(ModConstants.RegistryStrings.SKIING_PAINTING, () -> new PaintingType(64, 64));
}
