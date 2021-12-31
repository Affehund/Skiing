package com.affehund.skiing.core.data;

import com.affehund.skiing.Skiing;
import com.affehund.skiing.core.init.SkiingVillagers;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.ForgeRegistries;
import org.codehaus.plexus.util.StringUtils;

import java.util.Objects;

public class LanguageGenerator extends LanguageProvider {
    private final String modid;

    public LanguageGenerator(DataGenerator dataGenerator, String modid) {
        super(dataGenerator, modid, "en_us");
        this.modid = modid;
    }

    @Override
    protected void addTranslations() {
        add("_comment", "Translation (en_us) by Affehund");
        ForgeRegistries.ITEMS.getValues()
                .stream().filter(i -> (i != null && Objects.requireNonNull(i.getRegistryName()).getNamespace().equals(Skiing.MOD_ID)))
                .forEach(item -> {
                    String name = StringUtils.capitaliseAllWords(item.getRegistryName().getPath().replace("_", " "));
                    add(item, name);
                });
        ForgeRegistries.ENTITIES.getValues()
                .stream().filter(i -> (i != null && Objects.requireNonNull(i.getRegistryName()).getNamespace().equals(Skiing.MOD_ID)))
                .forEach(entity -> {
                    String name = StringUtils.capitaliseAllWords(entity.getRegistryName().getPath().replace("_", " "));
                    add(entity, name);
                });

        add("tooltip." + Skiing.MOD_ID + ".material", "Material: ");
        add("tooltip." + Skiing.MOD_ID + ".snow_shovel", "Mines a 3x3 area of snow at once.");
        add("entity.minecraft.villager." + this.modid + "." + SkiingVillagers.SKI_MERCHANT.get().getName(), "Ski Merchant");
        add(Skiing.SKIING_TAB.getDisplayName().getString(), "Skiing");
    }
}
