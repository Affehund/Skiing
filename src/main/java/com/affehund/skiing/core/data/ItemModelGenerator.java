package com.affehund.skiing.core.data;

import com.affehund.skiing.Skiing;
import com.affehund.skiing.common.item.AbstractMultiTextureItem;
import com.affehund.skiing.common.item.PulloverItem;
import com.affehund.skiing.common.item.SkiStickItem;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class ItemModelGenerator extends ItemModelProvider {
    private final Set<Item> blacklist = new HashSet<>();

    public ItemModelGenerator(DataGenerator dataGenerator, String modid, ExistingFileHelper existingFileHelper) {
        super(dataGenerator, modid, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        for (ResourceLocation id : ForgeRegistries.ITEMS.getKeys()) {
            Item item = ForgeRegistries.ITEMS.getValue(id);
            if (item != null && Skiing.MOD_ID.equals(id.getNamespace()) && !this.blacklist.contains(item)) {
                if (item instanceof AbstractMultiTextureItem) {
                    bewlrItem(id, item);
                } else if (item instanceof PulloverItem) {
                    overlayItem(id, item);
                } else if (item instanceof SkiStickItem) {
                    skiStickItem(id, (SkiStickItem) item);
                } else if (item instanceof BlockItem) {
                    defaultBlock(id, (BlockItem) item);
                } else {
                    defaultItem(id, item);
                }
            }
        }
    }

    private void bewlrItem(ResourceLocation id, Item item) {
        this.getBuilder(id.getPath())
                .parent(this.getExistingFile(new ResourceLocation(id.getNamespace(), "item/template_bewlr")));
        Skiing.LOGGER.debug("Generated bewlr item model for: {}", item.getRegistryName());
    }

    private void overlayItem(ResourceLocation id, Item item) {
        this.withExistingParent(id.getPath(), "item/handheld")
                .texture("layer0", new ResourceLocation(id.getNamespace(), "item/" + id.getPath())).texture(
                        "layer1", new ResourceLocation(id.getNamespace(), "item/" + id.getPath() + "_overlay"));
        Skiing.LOGGER.debug("Generated overlay item model for: " + item.getRegistryName());
    }

    private void skiStickItem(ResourceLocation id, SkiStickItem item) {
        this.getBuilder(id.getPath()).parent(this.getExistingFile(new ResourceLocation(id.getNamespace(), "item/template_ski_stick")))
                .texture("layer0", new ResourceLocation("block/" + Objects.requireNonNull(item.getMaterial().getRegistryName()).getPath()));
        Skiing.LOGGER.debug("Generated ski stick item model for: " + item.getRegistryName());
    }

    private void defaultBlock(ResourceLocation id, BlockItem item) {
        this.getBuilder(id.getPath()).parent(
                new ModelFile.UncheckedModelFile(new ResourceLocation(id.getNamespace(), "block/" + id.getPath())));
        Skiing.LOGGER.debug("Generated block item model for: " + item.getRegistryName());
    }

    private void defaultItem(ResourceLocation id, Item item) {
        this.withExistingParent(id.getPath(), "item/generated").texture("layer0",
                new ResourceLocation(id.getNamespace(), "item/" + id.getPath()));
        Skiing.LOGGER.debug("Generated item model for: " + item.getRegistryName());
    }
}
