package com.affehund.skiing.common.item;

import com.affehund.skiing.Skiing;
import com.affehund.skiing.core.util.SkiingUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SnowShovelItem extends DiggerItem {
    public SnowShovelItem(float attackDamage, float attackSpeed, Tier tier, TagKey<Block> blocks, Properties properties) {
        super(attackDamage, attackSpeed, tier, blocks, properties);
    }

    @Override
    public boolean canAttackBlock(@NotNull BlockState blockState, Level level, @NotNull BlockPos blockPos, Player player) {
        if (player.getMainHandItem().isCorrectToolForDrops(level.getBlockState(blockPos))) {
            SkiingUtils.breakBlocksInRadius(level, player, player.isCrouching() ? 0 : 1);
        }
        return true;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @Nullable Level level, List<Component> componentList, @NotNull TooltipFlag flag) {
        componentList.add(Component.translatable("tooltip." + Skiing.MOD_ID + ".snow_shovel").withStyle(ChatFormatting.GRAY));
    }
}
