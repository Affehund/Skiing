package com.affehund.skiing.common.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class SkiStickItem extends Item {
    private final Block block;
    public SkiStickItem(Properties properties, Block block) {
        super(properties);
        this.block = block;
    }

    public Block getMaterial() {
        return block;
    }
}
