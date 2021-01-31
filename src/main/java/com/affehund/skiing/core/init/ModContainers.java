package com.affehund.skiing.core.init;

import com.affehund.skiing.common.container.SkiRackContainer;
import com.affehund.skiing.core.ModConstants;

import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModContainers {
	public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister
			.create(ForgeRegistries.CONTAINERS, ModConstants.MOD_ID);

	public static final RegistryObject<ContainerType<SkiRackContainer>> SKI_RACK_CONTAINER = CONTAINERS
			.register(ModConstants.RegistryStrings.SKI_RACK, () -> IForgeContainerType.create(SkiRackContainer::new));
}
