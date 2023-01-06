package com.affehund.skiing.core.data;

import com.affehund.skiing.Skiing;
import com.affehund.skiing.common.item.SkiStickItem;
import com.affehund.skiing.core.registry.SkiingItems;
import com.affehund.skiing.core.util.SkiingMaterial;
import com.google.gson.JsonObject;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Consumer;

public class RecipeGenerator extends RecipeProvider implements IConditionBuilder {
    public RecipeGenerator(DataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    protected void buildCraftingRecipes(@NotNull Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(SkiingItems.SNOW_SHOVEL.get()).pattern(" b ").pattern(" s ")
                .pattern(" s ").define('b', Items.BUCKET).unlockedBy("has_bucket", has(Items.BUCKET))
                .define('s', Items.STICK).unlockedBy("has_stick", has(Items.STICK)).save(consumer);

        for (SkiingMaterial material : SkiingMaterial.values()) {
            buildSkiRecipe(consumer, material.getBlock(), material.getStairsBlock());
            buildSledRecipe(consumer, material.getBlock(), material.getSlabBlock(), material.getStairsBlock());
            buildSnowboardRecipe(consumer, material.getBlock(), material.getSlabBlock());
        }

        for (DyeColor dyeColor : DyeColor.values()) {
            buildPulloverRecipe(consumer, ForgeRegistries.ITEMS.getValue(new ResourceLocation(dyeColor.getName() + "_wool")), dyeColor);
        }

        for (Item item : ForgeRegistries.ITEMS.getValues().stream().filter(i -> i instanceof SkiStickItem).toList()) {
            buildSkiStickRecipe(consumer, item, ((SkiStickItem) item).getMaterial());
        }
    }

    private void buildSkiRecipe(Consumer<FinishedRecipe> consumer, Block block, Block stairsBlock) {
        String stringifiedBlockName = Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)).getPath();

        CompoundTag subCompoundTag = new CompoundTag();
        subCompoundTag.putString("skiing_material", Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)).toString());
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.put("EntityTag", subCompoundTag);

        ShapedRecipeBuilder.shaped(SkiingItems.SKI_ITEM.get()).pattern("t  ").pattern(" b ").pattern("  b")
                .define('b', block).unlockedBy("has_block", has(block))
                .define('t', stairsBlock).unlockedBy("has_stairs", has(stairsBlock))
                .save(NBTResultFinishedRecipeAdapter.from(consumer, RecipeSerializer.SHAPED_RECIPE, compoundTag),
                        new ResourceLocation(Skiing.MOD_ID, "skis/" + stringifiedBlockName + "_ski"));
    }

    private void buildSledRecipe(Consumer<FinishedRecipe> consumer, Block block, Block slabBlock, Block stairsBlock) {
        String stringifiedBlockName = Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)).getPath();

        CompoundTag subCompoundTag = new CompoundTag();
        subCompoundTag.putString("skiing_material", Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)).toString());
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.put("EntityTag", subCompoundTag);

        Item stick = Items.STICK;

        ShapedRecipeBuilder.shaped(SkiingItems.SLED_ITEM.get()).pattern("cbb").pattern("tss")
                .define('c', stick).unlockedBy("has_stick", has(stick))
                .define('b', block).unlockedBy("has_block", has(block))
                .define('s', slabBlock).unlockedBy("has_slab", has(slabBlock))
                .define('t', stairsBlock).unlockedBy("has_stairs", has(stairsBlock))
                .save(NBTResultFinishedRecipeAdapter.from(consumer, RecipeSerializer.SHAPED_RECIPE, compoundTag),
                        new ResourceLocation(Skiing.MOD_ID, "sleds/" + stringifiedBlockName + "_sled"));
    }

    private void buildSnowboardRecipe(Consumer<FinishedRecipe> consumer, Block block, Block slabBlock) {
        String stringifiedBlockName = Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)).getPath();

        CompoundTag subCompoundTag = new CompoundTag();
        subCompoundTag.putString("skiing_material", Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)).toString());
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.put("EntityTag", subCompoundTag);

        ShapedRecipeBuilder.shaped(SkiingItems.SNOWBOARD_ITEM.get()).pattern("b  ").pattern(" s ").pattern("  b")
                .define('b', block).unlockedBy("has_block", has(block))
                .define('s', slabBlock).unlockedBy("has_slab", has(slabBlock))
                .save(NBTResultFinishedRecipeAdapter.from(consumer, RecipeSerializer.SHAPED_RECIPE, compoundTag),
                        new ResourceLocation(Skiing.MOD_ID, "snowboards/" + stringifiedBlockName + "_snowboard"));
    }

    private void buildPulloverRecipe(Consumer<FinishedRecipe> consumer, ItemLike woolBlock, DyeColor color) {
        CompoundTag subCompoundTag = new CompoundTag();
        subCompoundTag.putInt("color", color.getFireworkColor());
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.put("display", subCompoundTag);
        ShapedRecipeBuilder.shaped(SkiingItems.PULLOVER.get()).pattern("w w").pattern("wgw").pattern("www")
                .define('w', woolBlock).unlockedBy("has_wool_block", has(woolBlock))
                .define('g', Ingredient.of(Items.LIME_DYE, Items.GREEN_DYE))
                .unlockedBy("has_green_dye", has(Items.GREEN_DYE))
                .unlockedBy("has_lime_dye", has(Items.GREEN_DYE))
                .save(NBTResultFinishedRecipeAdapter.from(consumer, RecipeSerializer.SHAPED_RECIPE, compoundTag),
                        new ResourceLocation(Skiing.MOD_ID, "pullovers/" + color.getName() + "_pullover"));
    }

    private void buildSkiStickRecipe(Consumer<FinishedRecipe> consumer, Item item, Block block) {
        ShapedRecipeBuilder.shaped(item, 2).pattern(" b ").pattern(" b ")
                .pattern(" n ").define('b', block).unlockedBy("has_block", has(block))
                .define('n', Items.IRON_NUGGET).unlockedBy("has_nugget", has(Items.IRON_NUGGET))
                .save(consumer, new ResourceLocation(Skiing.MOD_ID, "ski_sticks/" + Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item)).getPath()));
    }

    private record NBTResultFinishedRecipeAdapter(FinishedRecipe finishedRecipe, RecipeSerializer<?> recipeSerializer,
                                                  CompoundTag compoundTag) implements FinishedRecipe {

        public static Consumer<FinishedRecipe> from(Consumer<FinishedRecipe> originalRecipe, RecipeSerializer<?> recipeSerializer, CompoundTag compoundTag) {
            return fr -> originalRecipe.accept(new NBTResultFinishedRecipeAdapter(fr, recipeSerializer, compoundTag));
        }

        @Override
        public void serializeRecipeData(final @NotNull JsonObject json) {
            this.finishedRecipe.serializeRecipeData(json);
            if (null != this.compoundTag) {
                GsonHelper.getAsJsonObject(json, "result").addProperty("nbt", this.compoundTag.toString());
            }
        }

        @Override
        public @NotNull
        ResourceLocation getId() {
            return this.finishedRecipe.getId();
        }

        @Override
        public @NotNull
        RecipeSerializer<?> getType() {
            return this.recipeSerializer;
        }

        @Override
        public JsonObject serializeAdvancement() {
            return this.finishedRecipe.serializeAdvancement();
        }

        @Override
        public ResourceLocation getAdvancementId() {
            return this.finishedRecipe.getAdvancementId();
        }
    }
}
