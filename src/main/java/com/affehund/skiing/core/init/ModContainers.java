package com.affehund.skiing.core.init;

import com.affehund.skiing.common.container.AcaciaSkiRackContainer;
import com.affehund.skiing.common.container.BirchSkiRackContainer;
import com.affehund.skiing.common.container.CrimsonSkiRackContainer;
import com.affehund.skiing.common.container.DarkOakSkiRackContainer;
import com.affehund.skiing.common.container.JungleSkiRackContainer;
import com.affehund.skiing.common.container.OakSkiRackContainer;
import com.affehund.skiing.common.container.SpruceSkiRackContainer;
import com.affehund.skiing.common.container.WarpedSkiRackContainer;
import com.affehund.skiing.core.ModConstants;

import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModContainers {
	public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister
			.create(ForgeRegistries.CONTAINERS, ModConstants.MOD_ID);

	public static final RegistryObject<ContainerType<AcaciaSkiRackContainer>> ACACIA_SKI_RACK_CONTAINER = CONTAINERS
			.register("acacia_" + ModConstants.RegistryStrings.SKI_RACK,
					() -> IForgeContainerType.create(AcaciaSkiRackContainer::new));

	public static final RegistryObject<ContainerType<BirchSkiRackContainer>> BIRCH_SKI_RACK_CONTAINER = CONTAINERS
			.register("birch_" + ModConstants.RegistryStrings.SKI_RACK,
					() -> IForgeContainerType.create(BirchSkiRackContainer::new));

	public static final RegistryObject<ContainerType<CrimsonSkiRackContainer>> CRIMSON_SKI_RACK_CONTAINER = CONTAINERS
			.register("crimson_" + ModConstants.RegistryStrings.SKI_RACK,
					() -> IForgeContainerType.create(CrimsonSkiRackContainer::new));

	public static final RegistryObject<ContainerType<DarkOakSkiRackContainer>> DARK_OAK_SKI_RACK_CONTAINER = CONTAINERS
			.register("dark_oak_" + ModConstants.RegistryStrings.SKI_RACK,
					() -> IForgeContainerType.create(DarkOakSkiRackContainer::new));

	public static final RegistryObject<ContainerType<JungleSkiRackContainer>> JUNGLE_SKI_RACK_CONTAINER = CONTAINERS
			.register("jungle_" + ModConstants.RegistryStrings.SKI_RACK,
					() -> IForgeContainerType.create(JungleSkiRackContainer::new));

	public static final RegistryObject<ContainerType<OakSkiRackContainer>> OAK_SKI_RACK_CONTAINER = CONTAINERS.register(
			"oak_" + ModConstants.RegistryStrings.SKI_RACK, () -> IForgeContainerType.create(OakSkiRackContainer::new));

	public static final RegistryObject<ContainerType<SpruceSkiRackContainer>> SPRUCE_SKI_RACK_CONTAINER = CONTAINERS
			.register("spruce_" + ModConstants.RegistryStrings.SKI_RACK,
					() -> IForgeContainerType.create(SpruceSkiRackContainer::new));

	public static final RegistryObject<ContainerType<WarpedSkiRackContainer>> WARPED_SKI_RACK_CONTAINER = CONTAINERS
			.register("warped_" + ModConstants.RegistryStrings.SKI_RACK,
					() -> IForgeContainerType.create(WarpedSkiRackContainer::new));
}
