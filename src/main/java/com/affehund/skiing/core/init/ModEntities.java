package com.affehund.skiing.core.init;

import com.affehund.skiing.common.entity.SkisEntity;
import com.affehund.skiing.common.entity.SnowboardEntity;
import com.affehund.skiing.core.ModConstants;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEntities {
	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES,
			ModConstants.MOD_ID);

	public static final RegistryObject<EntityType<SkisEntity>> SKI_ENTITY = ENTITIES.register(
			ModConstants.RegistryStrings.SKIS_ENTITY,
			() -> EntityType.Builder.<SkisEntity>create(SkisEntity::new, EntityClassification.AMBIENT)
					.size(1.875f, 0.3f)
					.build(new ResourceLocation(ModConstants.MOD_ID, ModConstants.RegistryStrings.SKIS_ENTITY)
							.toString()));

	public static final RegistryObject<EntityType<SnowboardEntity>> SNOWBOARD_ENTITY = ENTITIES.register(
			ModConstants.RegistryStrings.SNOWBOARD_ENTITY,
			() -> EntityType.Builder.<SnowboardEntity>create(SnowboardEntity::new, EntityClassification.AMBIENT)
					.size(1.75f, 0.1875f)
					.build(new ResourceLocation(ModConstants.MOD_ID, ModConstants.RegistryStrings.SNOWBOARD_ENTITY)
							.toString()));
}
