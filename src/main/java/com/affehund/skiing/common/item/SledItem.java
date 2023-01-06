package com.affehund.skiing.common.item;

import com.affehund.skiing.common.entity.AbstractMultiTextureEntity;
import com.affehund.skiing.core.registry.SkiingEntities;
import net.minecraft.world.entity.EntityType;

public class SledItem extends AbstractMultiTextureItem {
    public SledItem(Properties properties) {
        super(properties);
    }

    @Override
    EntityType<? extends AbstractMultiTextureEntity> getEntityType() {
        return SkiingEntities.SLED_ENTITY.get();
    }
}
