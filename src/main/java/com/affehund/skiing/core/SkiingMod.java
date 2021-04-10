package com.affehund.skiing.core;

import java.util.List;
import java.util.Objects;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.affehund.skiing.client.render.SkisRenderer;
import com.affehund.skiing.client.render.SnowboardRenderer;
import com.affehund.skiing.client.render.ski_rack.AcaciaSkiRackTESR;
import com.affehund.skiing.client.render.ski_rack.BirchSkiRackTESR;
import com.affehund.skiing.client.render.ski_rack.CrimsonSkiRackTESR;
import com.affehund.skiing.client.render.ski_rack.DarkOakSkiRackTESR;
import com.affehund.skiing.client.render.ski_rack.JungleSkiRackTESR;
import com.affehund.skiing.client.render.ski_rack.OakSkiRackTESR;
import com.affehund.skiing.client.render.ski_rack.SpruceSkiRackTESR;
import com.affehund.skiing.client.render.ski_rack.WarpedSkiRackTESR;
import com.affehund.skiing.client.screen.AcaciaSkiRackScreen;
import com.affehund.skiing.client.screen.BirchSkiRackScreen;
import com.affehund.skiing.client.screen.CrimsonSkiRackScreen;
import com.affehund.skiing.client.screen.DarkOakSkiRackScreen;
import com.affehund.skiing.client.screen.JungleSkiRackScreen;
import com.affehund.skiing.client.screen.OakSkiRackScreen;
import com.affehund.skiing.client.screen.SpruceSkiRackScreen;
import com.affehund.skiing.client.screen.WarpedSkiRackScreen;
import com.affehund.skiing.common.entity.SkisEntity;
import com.affehund.skiing.common.entity.SnowboardEntity;
import com.affehund.skiing.common.item.PulloverItem;
import com.affehund.skiing.common.item.SkisItem;
import com.affehund.skiing.common.item.SnowShovel;
import com.affehund.skiing.core.config.SkiingConfig;
import com.affehund.skiing.core.data.gen.ModDataGeneration;
import com.affehund.skiing.core.event.SnowWorldTickEvent;
import com.affehund.skiing.core.init.ModBiomes;
import com.affehund.skiing.core.init.ModBlocks;
import com.affehund.skiing.core.init.ModContainers;
import com.affehund.skiing.core.init.ModEntities;
import com.affehund.skiing.core.init.ModItemGroup;
import com.affehund.skiing.core.init.ModItems;
import com.affehund.skiing.core.init.ModPaintings;
import com.affehund.skiing.core.init.ModTileEntities;
import com.affehund.skiing.core.init.ModVillagers;
import com.affehund.skiing.core.utils.SnowShovelUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.MovementInput;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.DrawHighlightEvent;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.common.BasicTrade;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;

@Mod(ModConstants.MOD_ID)
@Mod.EventBusSubscriber(modid = ModConstants.MOD_ID, bus = Bus.MOD)
public class SkiingMod {
	public static final Logger LOGGER = LogManager.getLogger();
	public static SkiingMod INSTANCE;

	final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

	public static final String COMMON_CONFIG_NAME = "skiing-common.toml";

	public SkiingMod() {
		LOGGER.debug("Loading up " + ModConstants.NAME + "!");
		modEventBus.addListener(this::commonSetup);
		modEventBus.addListener(this::clientSetup);
		modEventBus.addListener(this::gatherData);
		MinecraftForge.EVENT_BUS.register(this);

		MinecraftForge.EVENT_BUS.register(new SnowWorldTickEvent());

		ModBlocks.BLOCKS.register(modEventBus);
		ModItems.ITEMS.register(modEventBus);
		ModTileEntities.TILE_ENTITIES.register(modEventBus);
		ModContainers.CONTAINERS.register(modEventBus);
		ModEntities.ENTITIES.register(modEventBus);
		ModPaintings.PAINTINGS.register(modEventBus);
		ModVillagers.POINTS_OF_INTEREST.register(modEventBus);
		ModVillagers.PROFESSIONS.register(modEventBus);
		ModBiomes.BIOMES.register(modEventBus);
		ModBiomes.registerBiomes();

		ModLoadingContext.get().registerConfig(Type.COMMON, SkiingConfig.COMMON_CONFIG_SPEC, COMMON_CONFIG_NAME);
	}

	private void commonSetup(final FMLCommonSetupEvent event) {
		LOGGER.info("Common setup!");
		event.enqueueWork(() -> {
			ModVillagers.initVillagers();
		});
	}

	private void clientSetup(final FMLClientSetupEvent event) {
		LOGGER.info("Client setup!");
		RenderingRegistry.registerEntityRenderingHandler(ModEntities.SKI_ENTITY.get(), SkisRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(ModEntities.SNOWBOARD_ENTITY.get(), SnowboardRenderer::new);
		ScreenManager.registerFactory(ModContainers.ACACIA_SKI_RACK_CONTAINER.get(), AcaciaSkiRackScreen::new);
		ScreenManager.registerFactory(ModContainers.BIRCH_SKI_RACK_CONTAINER.get(), BirchSkiRackScreen::new);
		ScreenManager.registerFactory(ModContainers.CRIMSON_SKI_RACK_CONTAINER.get(), CrimsonSkiRackScreen::new);
		ScreenManager.registerFactory(ModContainers.DARK_OAK_SKI_RACK_CONTAINER.get(), DarkOakSkiRackScreen::new);
		ScreenManager.registerFactory(ModContainers.JUNGLE_SKI_RACK_CONTAINER.get(), JungleSkiRackScreen::new);
		ScreenManager.registerFactory(ModContainers.OAK_SKI_RACK_CONTAINER.get(), OakSkiRackScreen::new);
		ScreenManager.registerFactory(ModContainers.SPRUCE_SKI_RACK_CONTAINER.get(), SpruceSkiRackScreen::new);
		ScreenManager.registerFactory(ModContainers.WARPED_SKI_RACK_CONTAINER.get(), WarpedSkiRackScreen::new);

		ClientRegistry.bindTileEntityRenderer(ModTileEntities.ACACIA_SKI_RACK_TILE_ENTITY.get(),
				AcaciaSkiRackTESR::new);
		ClientRegistry.bindTileEntityRenderer(ModTileEntities.BIRCH_SKI_RACK_TILE_ENTITY.get(), BirchSkiRackTESR::new);
		ClientRegistry.bindTileEntityRenderer(ModTileEntities.CRIMSON_SKI_RACK_TILE_ENTITY.get(),
				CrimsonSkiRackTESR::new);
		ClientRegistry.bindTileEntityRenderer(ModTileEntities.DARK_OAK_SKI_RACK_TILE_ENTITY.get(),
				DarkOakSkiRackTESR::new);
		ClientRegistry.bindTileEntityRenderer(ModTileEntities.JUNGLE_SKI_RACK_TILE_ENTITY.get(),
				JungleSkiRackTESR::new);
		ClientRegistry.bindTileEntityRenderer(ModTileEntities.OAK_SKI_RACK_TILE_ENTITY.get(), OakSkiRackTESR::new);
		ClientRegistry.bindTileEntityRenderer(ModTileEntities.SPRUCE_SKI_RACK_TILE_ENTITY.get(),
				SpruceSkiRackTESR::new);
		ClientRegistry.bindTileEntityRenderer(ModTileEntities.WARPED_SKI_RACK_TILE_ENTITY.get(),
				WarpedSkiRackTESR::new);
	}

	private void gatherData(final GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

		if (event.includeServer()) {
			BlockTagsProvider blockTagsProvider = new ModDataGeneration.BlockTagsGen(generator, ModConstants.MOD_ID,
					existingFileHelper);

			generator.addProvider(blockTagsProvider);
			generator.addProvider(new ModDataGeneration.ItemTagsGen(generator, blockTagsProvider, ModConstants.MOD_ID,
					existingFileHelper));
			generator.addProvider(new ModDataGeneration.RecipeGen(generator));
			generator.addProvider(new ModDataGeneration.LootTablesGen(generator));
		}
		if (event.includeClient()) {
			generator.addProvider(new ModDataGeneration.LanguageGen(generator, ModConstants.MOD_ID, "de_de"));
			generator.addProvider(new ModDataGeneration.LanguageGen(generator, ModConstants.MOD_ID, "en_us"));
			generator.addProvider(
					new ModDataGeneration.ItemModelGen(generator, ModConstants.MOD_ID, existingFileHelper));
			generator.addProvider(
					new ModDataGeneration.BlockStateGen(generator, ModConstants.MOD_ID, existingFileHelper));
		}
	}

	@SubscribeEvent
	public void onSetVillagerTrades(VillagerTradesEvent event) {
		List<VillagerTrades.ITrade> novice = event.getTrades().get(1);
		List<VillagerTrades.ITrade> apprentice = event.getTrades().get(2);
		List<VillagerTrades.ITrade> journeyman = event.getTrades().get(3);
		List<VillagerTrades.ITrade> expert = event.getTrades().get(4);
		List<VillagerTrades.ITrade> master = event.getTrades().get(5);

		if (event.getType() == ModVillagers.SKIS_MERCHANT.get()) {
			novice.add(new BasicTrade(getRandomIntInRange(2, 4), randomPulloverStack(), 20, 10));
			novice.add(new BasicTrade(getRandomIntInRange(2, 4), randomPulloverStack(), 20, 10));
			novice.add(new BasicTrade(getRandomIntInRange(2, 4), new ItemStack(ModItems.SNOW_SHOVEL.get()), 20, 10));

			apprentice.add(new BasicTrade(getRandomIntInRange(1, 3), randomSkisItemStack(), 20, 10));
			apprentice.add(new BasicTrade(getRandomIntInRange(1, 3), randomSkisItemStack(), 20, 10));
			apprentice.add(
					new BasicTrade(getRandomIntInRange(2, 4), new ItemStack(ModItems.SKI_STICK_ITEM.get(), 2), 20, 10));
			apprentice.add(
					new BasicTrade(getRandomIntInRange(2, 4), new ItemStack(ModItems.SNOWBOARD_ITEM.get(), 2), 20, 10));

			journeyman.add(
					new BasicTrade(getRandomIntInRange(2, 4), new ItemStack(ModItems.CHOCOLATE_CUP.get()), 20, 10));
			journeyman.add(new BasicTrade(randomPulloverStack(), new ItemStack(Items.EMERALD, 2), 20, 10, 1f));
			journeyman.add(new BasicTrade(randomPulloverStack(), new ItemStack(Items.EMERALD, 2), 20, 10, 1f));

			expert.add(new BasicTrade(new ItemStack(ModItems.SKI_STICK_ITEM.get(), 2), new ItemStack(Items.EMERALD, 2),
					20, 10, 1f));
			expert.add(new BasicTrade(new ItemStack(ModItems.SNOWBOARD_ITEM.get(), 2), new ItemStack(Items.EMERALD, 2),
					20, 10, 1f));
			expert.add(new BasicTrade(randomSkisItemStack(), new ItemStack(Items.EMERALD), 20, 10, 1f));
			expert.add(new BasicTrade(randomSkisItemStack(), new ItemStack(Items.EMERALD), 20, 10, 1f));

			master.add(new BasicTrade(getRandomIntInRange(2, 4), new ItemStack(Items.SNOWBALL, 16), 20, 10));
			master.add(new BasicTrade(getRandomIntInRange(3, 5), new ItemStack(Items.SNOW_BLOCK, 8), 20, 10));
			master.add(new BasicTrade(new ItemStack(Items.SNOW_BLOCK, 8), new ItemStack(Items.EMERALD, 2), 20, 10, 1f));
		}
	}

	private static int getRandomIntInRange(int minimum, int maximum) {
		int number = minimum + (new Random().nextInt((maximum - minimum) + 1));
		return number;
	}

	private static ItemStack randomPulloverStack() {
		ItemStack pulloverStack = new ItemStack(ModItems.PULLOVER.get());
		((PulloverItem) pulloverStack.getItem()).setColor(pulloverStack,
				DyeColor.values()[new Random().nextInt(DyeColor.values().length)].getFireworkColor());
		return pulloverStack;
	}

	private static ItemStack randomSkisItemStack() {
		ItemStack skisStack = new ItemStack(ModItems.SKIS_ITEM.get());
		SkisEntity.SkisType type = SkisEntity.SkisType.getRandom();
		((SkisItem) skisStack.getItem()).setSkisType(skisStack, type.name());
		return skisStack;
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	@OnlyIn(Dist.CLIENT)
	public void controllSkis(InputUpdateEvent event) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.player != null) {
			Entity riddenEntity = mc.player.getRidingEntity();
			MovementInput movementInput = event.getMovementInput();
			if (riddenEntity instanceof SkisEntity) {
				((SkisEntity) riddenEntity).updateInputs(movementInput.leftKeyDown, movementInput.rightKeyDown,
						movementInput.forwardKeyDown, movementInput.backKeyDown,
						mc.gameSettings.keyBindSprint.isKeyDown(), movementInput.jump);
			}
			if (riddenEntity instanceof SnowboardEntity) {
				((SnowboardEntity) riddenEntity).updateInputs(movementInput.leftKeyDown, movementInput.rightKeyDown,
						movementInput.forwardKeyDown, movementInput.backKeyDown,
						mc.gameSettings.keyBindSprint.isKeyDown(), movementInput.jump);
			}
		}
	}

	@SuppressWarnings("resource")
	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onBlockHighlight(DrawHighlightEvent.HighlightBlock event) {
		PlayerEntity player = Minecraft.getInstance().player;
		if (player == null) {
			return;
		}
		World world = player.world;
		ItemStack tool = player.getHeldItemMainhand();
		if (!tool.isEmpty()) {
			if (tool.getItem() instanceof SnowShovel) {
				int radius = SnowShovel.getRadius();
				ActiveRenderInfo renderInfo = Minecraft.getInstance().gameRenderer.getActiveRenderInfo();
				Iterable<BlockPos> extraBlocks = SnowShovelUtils.getBlocksToBreak(world, player, radius);

				BlockPos lookingAtPos = SnowShovelUtils.getLookingAtBlockRayTrace(world, player).getPos();

				WorldRenderer worldRender = event.getContext();
				MatrixStack matrix = event.getMatrix();
				IVertexBuilder vertexBuilder = worldRender.renderTypeTextures.getBufferSource()
						.getBuffer(RenderType.getLines());
				Entity viewEntity = renderInfo.getRenderViewEntity();

				Vector3d vector3d = renderInfo.getProjectedView();
				double d0 = vector3d.getX();
				double d1 = vector3d.getY();
				double d2 = vector3d.getZ();
				matrix.push();

				if (tool.canHarvestBlock(world.getBlockState(lookingAtPos))) {
					for (BlockPos pos : extraBlocks) {
						if (world.getWorldBorder().contains(pos)) {
							BlockState state = world.getBlockState(pos);
							if (tool.canHarvestBlock(state)) {
								worldRender.drawSelectionBox(matrix, vertexBuilder, viewEntity, d0, d1, d2, pos,
										world.getBlockState(pos));
							}
						}

					}
				}
				matrix.pop();
			}
		}
	}

	@SubscribeEvent
	public static void onRegisterItems(RegistryEvent.Register<Item> event) {
		final IForgeRegistry<Item> registry = event.getRegistry();

		ModBlocks.BLOCKS.getEntries().stream().filter(block -> !(block.get() instanceof FlowingFluidBlock))
				.map(RegistryObject::get).forEach(block -> {
					final Item.Properties properties = new Item.Properties().group(ModItemGroup.MOD_ITEM_GROUP);
					final BlockItem blockItem = new BlockItem(block, properties);
					blockItem.setRegistryName(Objects.requireNonNull(block.getRegistryName()));
					registry.register(blockItem);
				});
		LOGGER.debug("Registered block items!");
	}

	public static ResourceLocation modResourceLocation(String path) {
		return new ResourceLocation(ModConstants.MOD_ID, path);
	}
}
