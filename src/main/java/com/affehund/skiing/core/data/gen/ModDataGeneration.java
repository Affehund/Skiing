package com.affehund.skiing.core.data.gen;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.affehund.skiing.common.entity.SkisEntity;
import com.affehund.skiing.common.entity.SnowboardEntity;
import com.affehund.skiing.common.item.PulloverItem;
import com.affehund.skiing.common.item.SkisItem;
import com.affehund.skiing.common.item.SnowboardItem;
import com.affehund.skiing.core.ModConstants;
import com.affehund.skiing.core.SkiingMod;
import com.affehund.skiing.core.init.ModBlocks;
import com.affehund.skiing.core.init.ModEntities;
import com.affehund.skiing.core.init.ModItemGroup;
import com.affehund.skiing.core.init.ModItems;
import com.affehund.skiing.core.init.ModVillagers;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.data.LootTableProvider;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.loot.ConstantRange;
import net.minecraft.loot.DynamicLootEntry;
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTableManager;
import net.minecraft.loot.functions.CopyName;
import net.minecraft.loot.functions.CopyNbt;
import net.minecraft.loot.functions.SetContents;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.ForgeRegistries;

public class ModDataGeneration {
	private static final Logger DATAGEN_LOGGER = LogManager.getLogger();

	public static final class LanguageGen extends LanguageProvider {
		public final String modID;

		public LanguageGen(DataGenerator gen, String modid, String locale) {
			super(gen, ModConstants.MOD_ID, locale);
			this.modID = modid;
		}

		@Override
		protected void addTranslations() {
			String locale = this.getName().replace("Languages: ", "");
			switch (locale) {
			case "de_de":
				add("_comment", "Translation (de_de) by Affehund");
				add(ModItems.SKI_STICK_ITEM.get(), "Skistock");
				add(ModItems.SKIS_ITEM.get(), "Skier");
				add(ModItems.SNOWBOARD_ITEM.get(), "Snowboard");
				add(ModItemGroup.MOD_ITEM_GROUP.getDisplayName().getString(), "Skiing");
				add(ModEntities.SKI_ENTITY.get(), "Skier");
				add(ModEntities.SNOWBOARD_ENTITY.get(), "Snowboard");
				add(ModItems.CHOCOLATE_CUP.get(), "Kakao Tasse");
				add(ModItems.SNOW_SHOVEL.get(), "Schnee Schaufel");
				add(ModItems.PULLOVER.get(), "Pullover");

				add(ModBlocks.ACACIA_SKI_RACK_BLOCK.get(), "Akazienholz-Skist�nder");
				add(ModBlocks.BIRCH_SKI_RACK_BLOCK.get(), "Birkenholz-Skist�nder");
				add(ModBlocks.CRIMSON_SKI_RACK_BLOCK.get(), "Karmesinholz-Skist�nder");
				add(ModBlocks.DARK_OAK_SKI_RACK_BLOCK.get(), "Schwarzeichenholz-Skist�nder");
				add(ModBlocks.JUNGLE_SKI_RACK_BLOCK.get(), "Tropenholz-Skist�nder");
				add(ModBlocks.OAK_SKI_RACK_BLOCK.get(), "Eichen-Skist�nder");
				add(ModBlocks.SPRUCE_SKI_RACK_BLOCK.get(), "Fichten-Skist�nder");
				add(ModBlocks.WARPED_SKI_RACK_BLOCK.get(), "Wirrholz-Skist�nder");

				addToolTip(modID, "snow_shovel",
						"Mit diesem Gegenstand kannst du eine 3x3-Fl�che aus Schnee auf einmal abbauen.");
				addToolTip(modID, "type", "Typ");

				addVillager(modID, ModVillagers.SKIS_MERCHANT.get(), "Ski H�ndler");
				break;
			case "en_us":
				add("_comment", "Translation (en_us) by Affehund");
				add(ModItems.SKI_STICK_ITEM.get(), "Ski Stick");
				add(ModItems.SKIS_ITEM.get(), "Skis");
				add(ModItems.SNOWBOARD_ITEM.get(), "Snowboard");
				add(ModItemGroup.MOD_ITEM_GROUP.getDisplayName().getString(), "Skiing");
				add(ModEntities.SKI_ENTITY.get(), "Skis");
				add(ModEntities.SNOWBOARD_ENTITY.get(), "Snowboard");
				add(ModItems.CHOCOLATE_CUP.get(), "Chocolate Cup");
				add(ModItems.SNOW_SHOVEL.get(), "Snow Shovel");
				add(ModItems.PULLOVER.get(), "Pullover");

				add(ModBlocks.ACACIA_SKI_RACK_BLOCK.get(), "Acacia Ski Rack");
				add(ModBlocks.BIRCH_SKI_RACK_BLOCK.get(), "Birch Ski Rack");
				add(ModBlocks.CRIMSON_SKI_RACK_BLOCK.get(), "Crimson Ski Rack");
				add(ModBlocks.DARK_OAK_SKI_RACK_BLOCK.get(), "Dark Oak Ski Rack");
				add(ModBlocks.JUNGLE_SKI_RACK_BLOCK.get(), "Jungle Ski Rack");
				add(ModBlocks.OAK_SKI_RACK_BLOCK.get(), "Oak Ski Rack");
				add(ModBlocks.SPRUCE_SKI_RACK_BLOCK.get(), "Spruce Ski Rack");
				add(ModBlocks.WARPED_SKI_RACK_BLOCK.get(), "Warped Ski Rack");

				addToolTip(modID, "snow_shovel", "With this item you can break a 3x3 area of snow at once.");
				addToolTip(modID, "type", "Type");
				addVillager(modID, ModVillagers.SKIS_MERCHANT.get(), "Skis Merchant");
				break;
			}
		}

		public void addToolTip(String modID, String string, String value) {
			add(modID + ".tooltip." + string, value);
		}

		public void addVillager(String modID, VillagerProfession villagerProfession, String name) {
			add("entity.minecraft.villager." + modID + "." + villagerProfession.toString(), name);
		}
	}

	public static final class BlockStateGen extends BlockStateProvider {
		public BlockStateGen(DataGenerator gen, String modid, ExistingFileHelper exFileHelper) {
			super(gen, modid, exFileHelper);
		}

		@Override
		protected void registerStatesAndModels() {
		}

		public void singleTextureBlock(Block block, String model, String textureName) {
			simpleBlock(block);
			DATAGEN_LOGGER.debug("Generated block model for: " + model);
		}
	}

	public static final class ItemModelGen extends ItemModelProvider {
		private final Set<Item> blacklist = new HashSet<>();

		public ItemModelGen(DataGenerator gen, String modid, ExistingFileHelper existingFileHelper) {
			super(gen, modid, existingFileHelper);
		}

		@Override
		protected void registerModels() {
			this.blacklist.add(ModItems.SKI_STICK_ITEM.get());
			for (ResourceLocation id : ForgeRegistries.ITEMS.getKeys()) {
				Item item = ForgeRegistries.ITEMS.getValue(id);
				if (item != null && ModConstants.MOD_ID.equals(id.getNamespace()) && !this.blacklist.contains(item)) {
					if (item instanceof BlockItem) {
						this.defaultBlock(id, (BlockItem) item);
					} else {
						this.defaultItem(id, item);
					}
				}
			}
		}

		protected void defaultItem(ResourceLocation id, Item item) {
			if (item instanceof SkisItem) {
				this.getBuilder(id.getPath())
						.parent(this.getExistingFile(new ResourceLocation(id.getNamespace(), "item/ister_template")));
			} else if (item instanceof SnowboardItem) {
				this.getBuilder(id.getPath())
						.parent(this.getExistingFile(new ResourceLocation(id.getNamespace(), "item/ister_template")));
			} else if (item instanceof PulloverItem) {
				this.withExistingParent(id.getPath(), "item/handheld")
						.texture("layer0", new ResourceLocation(id.getNamespace(), "item/" + id.getPath())).texture(
								"layer1", new ResourceLocation(id.getNamespace(), "item/" + id.getPath() + "_overlay"));
			} else
				this.withExistingParent(id.getPath(), "item/generated").texture("layer0",
						new ResourceLocation(id.getNamespace(), "item/" + id.getPath()));
			DATAGEN_LOGGER.debug("Generated item model for: " + item.getRegistryName());
		}

		protected void defaultBlock(ResourceLocation id, BlockItem item) {
			this.getBuilder(id.getPath()).parent(
					new ModelFile.UncheckedModelFile(new ResourceLocation(id.getNamespace(), "block/" + id.getPath())));
			DATAGEN_LOGGER.debug("Generated block item model for: " + item.getRegistryName());
		}
	}

	public static final class RecipeGen extends RecipeProvider implements IConditionBuilder {
		public RecipeGen(DataGenerator gen) {
			super(gen);
		}

		@Override
		protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer) {
			for (SkisEntity.SkisType type : SkisEntity.SkisType.values()) {
				addSkisRecipe(consumer, type);
			}

			for (SnowboardEntity.SnowboardType type : SnowboardEntity.SnowboardType.values()) {
				addSnowboardRecipe(consumer, type);
			}

			addSkiRackRecipe(consumer, ModBlocks.ACACIA_SKI_RACK_BLOCK.get(), Blocks.ACACIA_PLANKS, Blocks.ACACIA_SLAB);
			addSkiRackRecipe(consumer, ModBlocks.BIRCH_SKI_RACK_BLOCK.get(), Blocks.BIRCH_PLANKS, Blocks.BIRCH_SLAB);
			addSkiRackRecipe(consumer, ModBlocks.CRIMSON_SKI_RACK_BLOCK.get(), Blocks.CRIMSON_PLANKS,
					Blocks.CRIMSON_SLAB);
			addSkiRackRecipe(consumer, ModBlocks.DARK_OAK_SKI_RACK_BLOCK.get(), Blocks.DARK_OAK_PLANKS,
					Blocks.DARK_OAK_SLAB);
			addSkiRackRecipe(consumer, ModBlocks.JUNGLE_SKI_RACK_BLOCK.get(), Blocks.JUNGLE_PLANKS, Blocks.JUNGLE_SLAB);
			addSkiRackRecipe(consumer, ModBlocks.OAK_SKI_RACK_BLOCK.get(), Blocks.OAK_PLANKS, Blocks.OAK_SLAB);
			addSkiRackRecipe(consumer, ModBlocks.SPRUCE_SKI_RACK_BLOCK.get(), Blocks.SPRUCE_PLANKS, Blocks.SPRUCE_SLAB);
			addSkiRackRecipe(consumer, ModBlocks.WARPED_SKI_RACK_BLOCK.get(), Blocks.WARPED_PLANKS, Blocks.WARPED_SLAB);

			addPulloverRecipe(consumer, Items.BLACK_WOOL, ModItems.PULLOVER.get(), DyeColor.BLACK);
			addPulloverRecipe(consumer, Items.BLUE_WOOL, ModItems.PULLOVER.get(), DyeColor.BLUE);
			addPulloverRecipe(consumer, Items.BROWN_WOOL, ModItems.PULLOVER.get(), DyeColor.BROWN);
			addPulloverRecipe(consumer, Items.CYAN_WOOL, ModItems.PULLOVER.get(), DyeColor.CYAN);
			addPulloverRecipe(consumer, Items.GRAY_WOOL, ModItems.PULLOVER.get(), DyeColor.GRAY);
			addPulloverRecipe(consumer, Items.GREEN_WOOL, ModItems.PULLOVER.get(), DyeColor.GREEN);
			addPulloverRecipe(consumer, Items.LIGHT_BLUE_WOOL, ModItems.PULLOVER.get(), DyeColor.LIGHT_BLUE);
			addPulloverRecipe(consumer, Items.LIGHT_GRAY_WOOL, ModItems.PULLOVER.get(), DyeColor.LIGHT_GRAY);
			addPulloverRecipe(consumer, Items.LIME_WOOL, ModItems.PULLOVER.get(), DyeColor.LIME);
			addPulloverRecipe(consumer, Items.MAGENTA_WOOL, ModItems.PULLOVER.get(), DyeColor.MAGENTA);
			addPulloverRecipe(consumer, Items.ORANGE_WOOL, ModItems.PULLOVER.get(), DyeColor.ORANGE);
			addPulloverRecipe(consumer, Items.PINK_WOOL, ModItems.PULLOVER.get(), DyeColor.PINK);
			addPulloverRecipe(consumer, Items.PURPLE_WOOL, ModItems.PULLOVER.get(), DyeColor.PURPLE);
			addPulloverRecipe(consumer, Items.RED_WOOL, ModItems.PULLOVER.get(), DyeColor.RED);
			addPulloverRecipe(consumer, Items.WHITE_WOOL, ModItems.PULLOVER.get(), DyeColor.WHITE);
			addPulloverRecipe(consumer, Items.YELLOW_WOOL, ModItems.PULLOVER.get(), DyeColor.YELLOW);

			ShapelessRecipeBuilder.shapeless(ModItems.CHOCOLATE_CUP.get()).requires(Items.MILK_BUCKET)
					.unlockedBy("has_milk", has(Items.MILK_BUCKET)).requires(Items.BUCKET)
					.unlockedBy("has_bucket", has(Items.BUCKET)).requires(Items.COCOA_BEANS)
					.unlockedBy("has_cocoa", has(Items.COCOA_BEANS)).requires(Items.COCOA_BEANS)
					.save(consumer);

			ShapedRecipeBuilder.shaped(ModItems.SNOW_SHOVEL.get()).pattern(" b ").pattern(" s ")
					.pattern(" s ").define('b', Items.BUCKET).unlockedBy("has_bucket", has(Items.BUCKET))
					.define('s', Items.STICK).unlockedBy("has_stick", has(Items.STICK)).save(consumer);

			ShapedRecipeBuilder.shaped(ModItems.SKI_STICK_ITEM.get()).pattern(" s ").pattern(" s ")
					.pattern(" n ").define('s', Items.STICK).unlockedBy("has_stick", has(Items.STICK))
					.define('n', Items.IRON_NUGGET).unlockedBy("has_nugget", has(Items.IRON_NUGGET)).save(consumer);
		}

		protected void addSkiRackRecipe(Consumer<IFinishedRecipe> consumer, Block rack, Block plank, Block slab) {
			String folder = "ski_racks/";
			ShapedRecipeBuilder.shaped(rack).pattern("p p").pattern("pcp").pattern("sss")
					.define('p', plank).unlockedBy("has_plank", has(plank)).define('c', Tags.Items.CHESTS_WOODEN)
					.unlockedBy("has_chest", has(Tags.Items.CHESTS_WOODEN)).define('s', slab)
					.unlockedBy("has_slab", has(slab))
					.save(consumer, SkiingMod.modResourceLocation(folder + rack.asItem().toString()));
		}

		protected void addSkisRecipe(Consumer<IFinishedRecipe> consumer, SkisEntity.SkisType type) {
			String folder = "skis/";
			IItemProvider plank = type.getPlank().asItem();
			IItemProvider result = type.getItem();
			String name = type.getName();
			CompoundNBT customNbt = new CompoundNBT();
			customNbt.putString("Type", name);
			ShapedRecipeBuilder.shaped(result).pattern("p  ").pattern(" p ").pattern("  p")
					.define('p', plank).unlockedBy("has_plank", has(plank))
					.save(NBTResultFinishedRecipeAdapter.from(consumer, IRecipeSerializer.SHAPED_RECIPE, customNbt),
							SkiingMod.modResourceLocation(folder + result.toString() + "_from_" + type.toString()));
		}

		protected void addSnowboardRecipe(Consumer<IFinishedRecipe> consumer, SnowboardEntity.SnowboardType type) {
			String folder = "snowboards/";
			IItemProvider plank = type.getPlank().asItem();
			IItemProvider slab = type.getSlab().asItem();
			IItemProvider result = type.getItem();
			String name = type.getName();
			CompoundNBT customNbt = new CompoundNBT();
			customNbt.putString("Type", name);
			ShapedRecipeBuilder.shaped(result).pattern("sps").define('p', plank).define('s', slab)
					.unlockedBy("has_slab", has(plank)).unlockedBy("has_plank", has(plank))
					.save(NBTResultFinishedRecipeAdapter.from(consumer, IRecipeSerializer.SHAPED_RECIPE, customNbt),
							SkiingMod.modResourceLocation(folder + result.toString() + "_from_" + type.toString()));
		}

		protected void addPulloverRecipe(Consumer<IFinishedRecipe> consumer, IItemProvider input, IItemProvider result,
				DyeColor color) {
			String folder = "pullovers/";
			CompoundNBT subNbt = new CompoundNBT();
			subNbt.putInt("color", color.getFireworkColor());
			CompoundNBT customNbt = new CompoundNBT();
			customNbt.put("display", subNbt);
			ShapedRecipeBuilder.shaped(result).pattern("w w").pattern("wgw").pattern("www")
					.define('w', input).define('g', Ingredient.of(Items.LIME_DYE, Items.GREEN_DYE))
					.unlockedBy("has_item", has(input))
					.save(NBTResultFinishedRecipeAdapter.from(consumer, IRecipeSerializer.SHAPED_RECIPE, customNbt),
							SkiingMod.modResourceLocation(
									folder + result.asItem().toString() + "_from_" + input.asItem().toString()));
		}

//		private static class CustomNBTIngredient extends NBTIngredient {
//
//			protected CustomNBTIngredient(ItemStack stack) {
//				super(stack);
//			}
//		}

		protected static class NBTResultFinishedRecipeAdapter implements IFinishedRecipe {
			protected final IFinishedRecipe recipe;
			protected final IRecipeSerializer<?> serializer;
			private final CompoundNBT data;

			protected NBTResultFinishedRecipeAdapter(final IFinishedRecipe recipe,
					final IRecipeSerializer<?> serializer, CompoundNBT data) {

				this.recipe = recipe;
				this.serializer = serializer;
				this.data = data;
			}

			public static Consumer<IFinishedRecipe> from(final Consumer<IFinishedRecipe> originalRecipe,
					final IRecipeSerializer<?> serializer, final CompoundNBT data) {
				return fr -> originalRecipe.accept(new NBTResultFinishedRecipeAdapter(fr, serializer, data));
			}

			public static Consumer<IFinishedRecipe> from(final Consumer<IFinishedRecipe> originalRecipe,
					final IRecipeSerializer<?> serializer, final Consumer<CompoundNBT> data) {
				final CompoundNBT nbt = new CompoundNBT();
				data.accept(nbt);
				return from(originalRecipe, serializer, nbt);
			}

			@Override
			public void serializeRecipeData(final JsonObject json) {
				this.recipe.serializeRecipeData(json);
				if (null != this.data) {
					JSONUtils.getAsJsonObject(json, "result").addProperty("nbt", this.data.toString());
				}
			}

			@Override
			public ResourceLocation getId() {
				return this.recipe.getId();
			}

			@Override
			public IRecipeSerializer<?> getType() {
				return this.serializer;
			}

			@Override
			public JsonObject serializeAdvancement() {
				return this.recipe.serializeAdvancement();
			}

			@Override
			public ResourceLocation getAdvancementId() {
				return this.recipe.getAdvancementId();
			}
		}
	}

	public static final class BlockTagsGen extends BlockTagsProvider {

		public BlockTagsGen(DataGenerator gen, String modID, ExistingFileHelper existingFileHelper) {
			super(gen, modID, existingFileHelper);
		}

		@Override
		protected void addTags() {
			tag(ModTags.Blocks.SNOWY_BLOCKS).add(Blocks.BLUE_ICE, Blocks.FROSTED_ICE, Blocks.ICE,
					Blocks.PACKED_ICE, Blocks.SNOW, Blocks.SNOW_BLOCK);
		}
	}

	public static final class ItemTagsGen extends ItemTagsProvider {

		public ItemTagsGen(DataGenerator gen, BlockTagsProvider provider, String modID,
				ExistingFileHelper existingFileHelper) {
			super(gen, provider, modID, existingFileHelper);
		}

		@Override
		protected void addTags() {
			tag(ModTags.Items.WOOL).add(Items.BLACK_WOOL).add(Items.BLUE_WOOL).add(Items.BROWN_WOOL)
					.add(Items.CYAN_WOOL).add(Items.GRAY_WOOL).add(Items.GREEN_WOOL).add(Items.LIGHT_BLUE_WOOL)
					.add(Items.LIGHT_GRAY_WOOL).add(Items.LIME_WOOL).add(Items.MAGENTA_WOOL).add(Items.ORANGE_WOOL)
					.add(Items.PINK_WOOL).add(Items.PURPLE_WOOL).add(Items.RED_WOOL).add(Items.WHITE_WOOL)
					.add(Items.YELLOW_WOOL);
		}
	}

	public static final class LootTablesGen extends LootTableProvider {

		public LootTablesGen(DataGenerator dataGeneratorIn) {
			super(dataGeneratorIn);
			this.generator = dataGeneratorIn;
		}

		protected void addTables() {
			createStandardBlockTable(ModBlocks.ACACIA_SKI_RACK_BLOCK.get());
			createStandardBlockTable(ModBlocks.BIRCH_SKI_RACK_BLOCK.get());
			createStandardBlockTable(ModBlocks.CRIMSON_SKI_RACK_BLOCK.get());
			createStandardBlockTable(ModBlocks.DARK_OAK_SKI_RACK_BLOCK.get());
			createStandardBlockTable(ModBlocks.JUNGLE_SKI_RACK_BLOCK.get());
			createStandardBlockTable(ModBlocks.OAK_SKI_RACK_BLOCK.get());
			createStandardBlockTable(ModBlocks.SPRUCE_SKI_RACK_BLOCK.get());
			createStandardBlockTable(ModBlocks.WARPED_SKI_RACK_BLOCK.get());
		}

		protected void createStandardBlockTable(Block block) {
			lootTables.put(block, createStandardTable(block.getRegistryName().toString(), block));
		}

		private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
		protected final Map<Block, LootTable.Builder> lootTables = new HashMap<>();
		private final DataGenerator generator;

		protected LootTable.Builder createStandardTable(String name, Block block) {
			LootPool.Builder builder = LootPool.lootPool().name(name).setRolls(ConstantRange.exactly(1))
					.add(ItemLootEntry.lootTableItem(block)
							.apply(CopyName.copyName(CopyName.Source.BLOCK_ENTITY))
							.apply(CopyNbt.copyData(CopyNbt.Source.BLOCK_ENTITY).copy("inv",
									"BlockEntityTag.inv", CopyNbt.Action.REPLACE))
							.apply(SetContents.setContents().withEntry(
									DynamicLootEntry.dynamicEntry(new ResourceLocation("minecraft", "contents")))));
			return LootTable.lootTable().withPool(builder);
		}

		protected LootTable.Builder createStandardTable(String name, Item item) {
			LootPool.Builder builder = LootPool.lootPool().name(name).setRolls(ConstantRange.exactly(1))
					.add(ItemLootEntry.lootTableItem(item).apply(CopyName.copyName(CopyName.Source.BLOCK_ENTITY))
							.apply(CopyNbt.copyData(CopyNbt.Source.BLOCK_ENTITY).copy("inv",
									"BlockEntityTag.inv", CopyNbt.Action.REPLACE))
							.apply(SetContents.setContents().withEntry(
									DynamicLootEntry.dynamicEntry(new ResourceLocation("minecraft", "contents")))));
			return LootTable.lootTable().withPool(builder);
		}

		@Override
		public void run(DirectoryCache cache) {
			addTables();

			Map<ResourceLocation, LootTable> tables = new HashMap<>();
			for (Map.Entry<Block, LootTable.Builder> entry : lootTables.entrySet()) {
				tables.put(entry.getKey().getLootTable(),
						entry.getValue().setParamSet(LootParameterSets.BLOCK).build());
			}
			writeTables(cache, tables);
		}

		private void writeTables(DirectoryCache cache, Map<ResourceLocation, LootTable> tables) {
			Path outputFolder = this.generator.getOutputFolder();
			tables.forEach((key, lootTable) -> {
				Path path = outputFolder
						.resolve("data/" + key.getNamespace() + "/loot_tables/" + key.getPath() + ".json");
				try {
					IDataProvider.save(GSON, cache, LootTableManager.serialize(lootTable), path);
					SkiingMod.LOGGER.debug("Creating loot table for " + key.getPath());
				} catch (IOException e) {
					SkiingMod.LOGGER.error("Couldn't write loot table {}", path, e);
				}
			});
		}
	}
}
