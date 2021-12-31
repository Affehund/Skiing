package com.affehund.skiing.common.item;

import com.affehund.skiing.common.entity.AbstractMultiTextureEntity;
import com.affehund.skiing.core.init.SkiingEntities;
import net.minecraft.world.entity.EntityType;

public class SkiItem extends AbstractMultiTextureItem {
    public SkiItem(Properties properties) {
        super(properties);
    }

    @Override
    EntityType<? extends AbstractMultiTextureEntity> getEntityType() {
        return SkiingEntities.SKI_ENTITY.get();
    }
}
