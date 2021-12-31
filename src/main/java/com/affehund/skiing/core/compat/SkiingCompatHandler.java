package com.affehund.skiing.core.compat;

import com.affehund.skiing.Skiing;
import net.minecraftforge.fml.ModList;

public class SkiingCompatHandler {
    public static final String JEI = "jei";
    public static final String PATCHOULI = "patchouli";

    public static void initCompats() {
        if (isModLoaded(JEI)) {
            Skiing.LOGGER.debug("{} is loaded", JEI);
        }

        if (isModLoaded(PATCHOULI)) {
            Skiing.LOGGER.debug("{} is loaded", PATCHOULI);
        }
    }

    public static boolean isModLoaded(String modid) {
        return ModList.get() != null && ModList.get().getModContainerById(modid).isPresent();
    }
}
