package com.affehund.skiing.core.compat.jei;

import com.affehund.skiing.Skiing;
import com.affehund.skiing.core.init.SkiingItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@JeiPlugin
public class SkiingJEIPlugin implements IModPlugin {
    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return new ResourceLocation(Skiing.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        registration.useNbtForSubtypes(SkiingItems.PULLOVER.get(), SkiingItems.SKI_ITEM.get(), SkiingItems.SLED_ITEM.get(), SkiingItems.SNOWBOARD_ITEM.get());
        IModPlugin.super.registerItemSubtypes(registration);
    }
}
