package com.affehund.skiing.common.item;

import java.util.List;

import com.affehund.skiing.common.block.SkiRackBlock;
import com.affehund.skiing.common.tile.SkiRackTileEntity;
import com.affehund.skiing.core.ModConstants;
import com.affehund.skiing.core.utils.TextUtils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class SkiRackItem extends BlockItem {

	private final String NBT_TYPE = "Type";

	public static final SkiRackBlock.SkiRackType DEFAULT_TYPE = SkiRackBlock.SkiRackType.ACACIA;

	public SkiRackItem(Block blockIn, Properties properties) {
		super(blockIn, properties);
	}

	public SkiRackBlock.SkiRackType getRackType(ItemStack stack) {
		CompoundNBT tag = stack.getOrCreateTag();
		try {
			CompoundNBT blockEntityTag = stack.getOrCreateChildTag("BlockEntityTag");
			if (blockEntityTag != null && blockEntityTag.contains(NBT_TYPE)) {
				return SkiRackBlock.SkiRackType.getByName(tag.getString(NBT_TYPE));
			}
			
			if (tag != null && tag.contains(NBT_TYPE)) {
				return SkiRackBlock.SkiRackType.getByName(tag.getString(NBT_TYPE));
			}
		} catch (Exception e) {
			
		}
		tag.putString(NBT_TYPE, DEFAULT_TYPE.getName());
		return DEFAULT_TYPE;

	}

	public SkiRackBlock.SkiRackType getRackTagType(ItemStack stack) {
		CompoundNBT tag = stack.getOrCreateTag();
		try {
			return SkiRackBlock.SkiRackType.getByName(stack.getOrCreateTag().getString(NBT_TYPE));
		} catch (Exception e) {
			tag.putString(NBT_TYPE, DEFAULT_TYPE.getName());
			return DEFAULT_TYPE;
		}
	}

	public void setRackType(ItemStack stack, String type) {
		stack.getOrCreateTag().putString(NBT_TYPE, type);
		stack.getOrCreateChildTag("BlockEntityTag").putString(NBT_TYPE, type);
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		String type = getRackType(stack).getName();
		tooltip.add(new StringTextComponent(
				TextUtils.addModTranslationToolTip(tooltip, ModConstants.MOD_ID, "type").getString() + ": " + type)
						.mergeStyle(TextFormatting.GRAY));
	}

	@Override
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
		if (isInGroup(group)) {
			for (SkiRackBlock.SkiRackType type : SkiRackBlock.SkiRackType.values()) {
				ItemStack stack = new ItemStack(this);
				setRackType(stack, type.getName());
				items.add(stack);
			}
		}
	}

	@Override
	protected boolean onBlockPlaced(BlockPos pos, World worldIn, PlayerEntity player, ItemStack stack,
			BlockState state) {
		boolean success = super.onBlockPlaced(pos, worldIn, player, stack, state);
		if (success) {
			SkiRackTileEntity.get(worldIn, pos).ifPresent(tile -> {
				tile.setType(getTypeIfTile(stack));
			});
		}
		return success;
	}

	public static SkiRackBlock.SkiRackType getTypeIfTile(ItemStack stack) {
		Item item = stack.getItem();
		return item instanceof SkiRackItem ? ((SkiRackItem) item).getRackType(stack) : DEFAULT_TYPE;
	}
}
