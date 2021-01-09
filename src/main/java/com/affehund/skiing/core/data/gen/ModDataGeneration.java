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

import com.affehund.skiing.common.block.SkiRackBlock;
import com.affehund.skiing.common.entity.SkisEntity;
import com.affehund.skiing.common.item.PulloverItem;
import com.affehund.skiing.common.item.SkiRackItem;
import com.affehund.skiing.common.item.SkisItem;
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
				add(ModItemGroup.MOD_ITEM_GROUP.getGroupName().getString(), "Skiing");
				add(ModEntities.SKI_ENTITY.get(), "Skier");
				add(ModItems.CHOCOLATE_CUP.get(), "Kakao Tasse");
				add(ModItems.SNOW_SHOVEL.get(), "Schnee Schaufel");
				add(ModItems.PULLOVER.get(), "Pullover");
				add(ModBlocks.SKI_RACK.get(), "Ski-Ständer");
				add("container." + ModConstants.MOD_ID + ModBlocks.SKI_RACK.get(), "Ski-Ständer");
				add("tile." + ModConstants.MOD_ID + ModConstants.RegistryStrings.SKI_RACK, "Ski-Ständer");
				addToolTip(modID, "snow_shovel",
						"Mit diesem Gegenstand kannst du eine 3x3-Fläche aus Schnee auf einmal abbauen.");
				addToolTip(modID, "type", "Typ");
				addVillager(modID, ModVillagers.SKIS_MERCHANT.get(), "Ski Händler");
				break;
			case "en_us":
				add("_comment", "Translation (en_us) by Affehund");
				add(ModItems.SKI_STICK_ITEM.get(), "Ski Stick");
				add(ModItems.SKIS_ITEM.get(), "Skis");
				add(ModItemGroup.MOD_ITEM_GROUP.getGroupName().getString(), "Skiing");
				add(ModEntities.SKI_ENTITY.get(), "Skis");
				add(ModItems.CHOCOLATE_CUP.get(), "Chocolate Cup");
				add(ModItems.SNOW_SHOVEL.get(), "Snow Shovel");
				add(ModItems.PULLOVER.get(), "Pullover");
				add(ModBlocks.SKI_RACK.get(), "Ski Rack");
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
			this.blacklist.add(ModItems.SKIS_RACK.get());
			
			
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
			if (item instanceof SkisItem || item instanceof SkiRackItem) {
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
		protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
			for (SkisEntity.SkisType type : SkisEntity.SkisType.values()) {
				addSkisRecipe(consumer, type);
			}

			for (SkiRackBlock.SkiRackType type : SkiRackBlock.SkiRackType.values()) {
				addRackRecipe(consumer, type);
			}

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

			ShapelessRecipeBuilder.shapelessRecipe(ModItems.CHOCOLATE_CUP.get()).addIngredient(Items.MILK_BUCKET)
					.addCriterion("has_milk", hasItem(Items.MILK_BUCKET)).addIngredient(Items.BUCKET)
					.addCriterion("has_bucket", hasItem(Items.BUCKET)).addIngredient(Items.COCOA_BEANS)
					.addCriterion("has_cocoa", hasItem(Items.COCOA_BEANS)).addIngredient(Items.COCOA_BEANS)
					.build(consumer);

			ShapedRecipeBuilder.shapedRecipe(ModItems.SNOW_SHOVEL.get()).patternLine(" b ").patternLine(" s ")
					.patternLine(" s ").key('b', Items.BUCKET).addCriterion("has_bucket", hasItem(Items.BUCKET))
					.key('s', Items.STICK).addCriterion("has_stick", hasItem(Items.STICK)).build(consumer);

			ShapedRecipeBuilder.shapedRecipe(ModItems.SKI_STICK_ITEM.get()).patternLine(" s ").patternLine(" s ")
					.patternLine(" n ").key('s', Items.STICK).addCriterion("has_stick", hasItem(Items.STICK))
					.key('n', Items.IRON_NUGGET).addCriterion("has_nugget", hasItem(Items.IRON_NUGGET)).build(consumer);

		}

		protected void addRackRecipe(Consumer<IFinishedRecipe> consumer, SkiRackBlock.SkiRackType type) {
			IItemProvider plank = type.getPlank().asItem();
			IItemProvider slab = type.getSlab().asItem();
			IItemProvider result = ModBlocks.SKI_RACK.get().asItem();
			String name = type.getName();
			CompoundNBT customNbt = new CompoundNBT();
			customNbt.putString("Type", name);
			ShapedRecipeBuilder.shapedRecipe(result).patternLine("p p").patternLine("pcp").patternLine("sss")
					.key('p', plank).addCriterion("has_plank", hasItem(plank)).key('c', Tags.Items.CHESTS_WOODEN)
					.addCriterion("has_chest", hasItem(Tags.Items.CHESTS_WOODEN)).key('s', slab)
					.addCriterion("has_slab", hasItem(slab))
					.build(NBTResultFinishedRecipeAdapter.from(consumer, IRecipeSerializer.CRAFTING_SHAPED, customNbt),
							new ResourceLocation(ModConstants.MOD_ID, result.toString() + "_from_" + type.getName()));
		}

		protected void addSkisRecipe(Consumer<IFinishedRecipe> consumer, SkisEntity.SkisType type) {
			IItemProvider plank = type.getPlank().asItem();
			IItemProvider result = type.getItem();
			String name = type.getName();
			CompoundNBT customNbt = new CompoundNBT();
			customNbt.putString("Type", name);
			ShapedRecipeBuilder.shapedRecipe(result).patternLine("p  ").patternLine(" p ").patternLine("  p")
					.key('p', plank).addCriterion("has_plank", hasItem(plank))
					.build(NBTResultFinishedRecipeAdapter.from(consumer, IRecipeSerializer.CRAFTING_SHAPED, customNbt),
							new ResourceLocation(ModConstants.MOD_ID, result.toString() + "_from_" + type.toString()));
		}

		protected void addPulloverRecipe(Consumer<IFinishedRecipe> consumer, IItemProvider input, IItemProvider result,
				DyeColor color) {
			CompoundNBT subNbt = new CompoundNBT();
			subNbt.putInt("color", color.getFireworkColor());
			CompoundNBT customNbt = new CompoundNBT();
			customNbt.put("display", subNbt);
			ShapedRecipeBuilder.shapedRecipe(result).patternLine("w w").patternLine("wgw").patternLine("www")
					.key('w', input).key('g', Ingredient.fromItems(Items.LIME_DYE, Items.GREEN_DYE))
					.addCriterion("has_item", hasItem(input))
					.build(NBTResultFinishedRecipeAdapter.from(consumer, IRecipeSerializer.CRAFTING_SHAPED, customNbt),
							new ResourceLocation(ModConstants.MOD_ID,
									result.asItem().toString() + "_from_" + input.asItem().toString()));
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
			public void serialize(final JsonObject json) {
				this.recipe.serialize(json);
				if (null != this.data) {
					JSONUtils.getJsonObject(json, "result").addProperty("nbt", this.data.toString());
				}
			}

			@Override
			public ResourceLocation getID() {
				return this.recipe.getID();
			}

			@Override
			public IRecipeSerializer<?> getSerializer() {
				return this.serializer;
			}

			@Override
			public JsonObject getAdvancementJson() {
				return this.recipe.getAdvancementJson();
			}

			@Override
			public ResourceLocation getAdvancementID() {
				return this.recipe.getAdvancementID();
			}
		}
	}

	public static final class BlockTagsGen extends BlockTagsProvider {

		public BlockTagsGen(DataGenerator gen, String modID, ExistingFileHelper existingFileHelper) {
			super(gen, modID, existingFileHelper);
		}

		@Override
		protected void registerTags() {
			getOrCreateBuilder(ModTags.Blocks.SNOWY_BLOCKS).add(Blocks.BLUE_ICE, Blocks.FROSTED_ICE, Blocks.ICE,
					Blocks.PACKED_ICE, Blocks.SNOW, Blocks.SNOW_BLOCK);
		}
	}

	public static final class ItemTagsGen extends ItemTagsProvider {

		public ItemTagsGen(DataGenerator gen, BlockTagsProvider provider, String modID,
				ExistingFileHelper existingFileHelper) {
			super(gen, provider, modID, existingFileHelper);
		}

		@Override
		protected void registerTags() {
			getOrCreateBuilder(ModTags.Items.WOOL).add(Items.BLACK_WOOL).add(Items.BLUE_WOOL).add(Items.BROWN_WOOL)
					.add(Items.CYAN_WOOL).add(Items.GRAY_WOOL).add(Items.GREEN_WOOL).add(Items.LIGHT_BLUE_WOOL)
					.add(Items.LIGHT_GRAY_WOOL).add(Items.LIME_WOOL).add(Items.MAGENTA_WOOL).add(Items.ORANGE_WOOL)
					.add(Items.PINK_WOOL).add(Items.PURPLE_WOOL).add(Items.RED_WOOL).add(Items.WHITE_WOOL)
					.add(Items.YELLOW_WOOL);

			getOrCreateBuilder(ModTags.Items.PLANKS).add(Items.ACACIA_PLANKS).add(Items.BIRCH_PLANKS)
					.add(Items.CRIMSON_PLANKS).add(Items.DARK_OAK_PLANKS).add(Items.JUNGLE_PLANKS).add(Items.OAK_PLANKS)
					.add(Items.SPRUCE_PLANKS).add(Items.WARPED_PLANKS);
		}
	}

	public static final class LootTablesGen extends LootTableProvider {

		public LootTablesGen(DataGenerator dataGeneratorIn) {
			super(dataGeneratorIn);
			this.generator = dataGeneratorIn;
		}

		protected void addTables() {
			lootTables.put(ModBlocks.SKI_RACK.get(), createStandardTable("ski_rack", ModBlocks.SKI_RACK.get()));
		}

		private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
		protected final Map<Block, LootTable.Builder> lootTables = new HashMap<>();
		private final DataGenerator generator;

		protected LootTable.Builder createStandardTable(String name, Block block) {
			LootPool.Builder builder = LootPool.builder().name(name).rolls(ConstantRange.of(1))
					.addEntry(ItemLootEntry.builder(block)
							.acceptFunction(CopyName.builder(CopyName.Source.BLOCK_ENTITY))
							.acceptFunction(CopyNbt.builder(CopyNbt.Source.BLOCK_ENTITY).addOperation("inv",
									"BlockEntityTag.inv", CopyNbt.Action.REPLACE))
							.acceptFunction(SetContents.builderIn().addLootEntry(
									DynamicLootEntry.func_216162_a(new ResourceLocation("minecraft", "contents")))));
			return LootTable.builder().addLootPool(builder);
		}

		protected LootTable.Builder createStandardTable(String name, Item item) {
			LootPool.Builder builder = LootPool.builder().name(name).rolls(ConstantRange.of(1))
					.addEntry(ItemLootEntry.builder(item).acceptFunction(CopyName.builder(CopyName.Source.BLOCK_ENTITY))
							.acceptFunction(CopyNbt.builder(CopyNbt.Source.BLOCK_ENTITY).addOperation("inv",
									"BlockEntityTag.inv", CopyNbt.Action.REPLACE))
							.acceptFunction(SetContents.builderIn().addLootEntry(
									DynamicLootEntry.func_216162_a(new ResourceLocation("minecraft", "contents")))));
			return LootTable.builder().addLootPool(builder);
		}

		@Override
		public void act(DirectoryCache cache) {
			addTables();

			Map<ResourceLocation, LootTable> tables = new HashMap<>();
			for (Map.Entry<Block, LootTable.Builder> entry : lootTables.entrySet()) {
				tables.put(entry.getKey().getLootTable(),
						entry.getValue().setParameterSet(LootParameterSets.BLOCK).build());
			}
			writeTables(cache, tables);
		}

		private void writeTables(DirectoryCache cache, Map<ResourceLocation, LootTable> tables) {
			Path outputFolder = this.generator.getOutputFolder();
			tables.forEach((key, lootTable) -> {
				Path path = outputFolder
						.resolve("data/" + key.getNamespace() + "/loot_tables/" + key.getPath() + ".json");
				try {
					IDataProvider.save(GSON, cache, LootTableManager.toJson(lootTable), path);
					SkiingMod.LOGGER.debug("Creating loot table for " + key.getPath());
				} catch (IOException e) {
					SkiingMod.LOGGER.error("Couldn't write loot table {}", path, e);
				}
			});
		}
	}
}
