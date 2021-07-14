package com.affehund.skiing.common.item;

import java.util.List;
import java.util.Set;

import com.affehund.skiing.core.ModConstants;
import com.affehund.skiing.core.utils.TextUtils;
import com.affehund.skiing.core.utils.SnowShovelUtils;
import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

public class SnowShovel extends ToolItem {

	private static final Set<Block> effectiveBlocksOn = Sets.newHashSet(Blocks.SNOW, Blocks.SNOW_BLOCK);

	public SnowShovel(IItemTier tier, float attackDamageIn, float attackSpeedIn, Item.Properties builderIn) {
		super(attackDamageIn, attackSpeedIn, tier, effectiveBlocksOn,
				builderIn.addToolType(ToolType.SHOVEL, tier.getLevel()));
	}

	public boolean isCorrectToolForDrops(BlockState blockIn) {
		return blockIn.is(Blocks.SNOW) || blockIn.is(Blocks.SNOW_BLOCK);
	}

	private static int radius = 1;

	@Override
	public boolean canAttackBlock(BlockState state, World world, BlockPos pos, PlayerEntity player) {
		if (player.isCrouching()) {
			radius = 0;
		}
		if (player.getMainHandItem().isCorrectToolForDrops(world.getBlockState(pos))) {
			SnowShovelUtils.breakBlocksInRadius(world, player, radius);
		}
		return true;
	}

	public static int getRadius() {
		return radius;
	}

	@Override
	public void appendHoverText(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		tooltip.add(
				TextUtils.addModTranslationToolTip(tooltip, ModConstants.MOD_ID, "snow_shovel").withStyle(TextFormatting.GRAY));
	}
}
