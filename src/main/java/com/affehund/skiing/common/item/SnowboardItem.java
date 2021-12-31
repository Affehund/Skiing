package com.affehund.skiing.common.item;

import com.affehund.skiing.common.entity.AbstractMultiTextureEntity;
import com.affehund.skiing.core.init.SkiingEntities;
import net.minecraft.world.entity.EntityType;

public class SnowboardItem extends AbstractMultiTextureItem {
    public SnowboardItem(Properties properties) {
        super(properties);
    }

    @Override
    EntityType<? extends AbstractMultiTextureEntity> getEntityType() {
        return SkiingEntities.SNOWBOARD_ENTITY.get();
    }
}
