package com.affehund.skiing.common.item;

import java.util.List;
import java.util.function.Predicate;

import com.affehund.skiing.common.entity.SkisEntity;
import com.affehund.skiing.common.entity.SkisEntity.SkisType;
import com.affehund.skiing.core.ModConstants;
import com.affehund.skiing.core.utils.TextUtils;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class SkisItem extends Item {
	private static final Predicate<Entity> ENTITY_PREDICATE = EntityPredicates.NO_SPECTATORS
			.and(Entity::isPickable);

	private static final String NBT_TYPE = "Type";

	public SkisItem(Item.Properties properties) {
		super(properties);
	}

	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack itemstack = playerIn.getItemInHand(handIn);
		RayTraceResult raytraceresult = getPlayerPOVHitResult(worldIn, playerIn, RayTraceContext.FluidMode.ANY);
		if (raytraceresult.getType() == RayTraceResult.Type.MISS) {
			return ActionResult.pass(itemstack);
		} else {
			Vector3d vector3d = playerIn.getViewVector(1.0F);
			List<Entity> list = worldIn.getEntities(playerIn,
					playerIn.getBoundingBox().expandTowards(vector3d.scale(5.0D)).inflate(1.0D), ENTITY_PREDICATE);
			if (!list.isEmpty()) {
				Vector3d vector3d1 = playerIn.getEyePosition(1.0F);
				for (Entity entity : list) {
					AxisAlignedBB axisalignedbb = entity.getBoundingBox()
							.inflate((double) entity.getPickRadius());
					if (axisalignedbb.contains(vector3d1)) {
						return ActionResult.pass(itemstack);
					}
				}
			}

			if (raytraceresult.getType() == RayTraceResult.Type.BLOCK) {
				SkisEntity entity = new SkisEntity(worldIn, raytraceresult.getLocation().x, raytraceresult.getLocation().y,
						raytraceresult.getLocation().z);
				entity.setSkisType(getSkisType(itemstack));
				entity.yRot = playerIn.yRot;
				if (!worldIn.noCollision(entity, entity.getBoundingBox().inflate(-0.1D))) {
					return ActionResult.fail(itemstack);
				} else {
					if (!worldIn.isClientSide) {
						worldIn.addFreshEntity(entity);
						if (!playerIn.abilities.instabuild) {
							itemstack.shrink(1);
						}
					}
					playerIn.awardStat(Stats.ITEM_USED.get(this));
					return ActionResult.sidedSuccess(itemstack, worldIn.isClientSide());
				}
			} else {
				return ActionResult.pass(itemstack);
			}
		}
	}

	public static SkisEntity.SkisType getSkisType(ItemStack stack) {
		try {
			return SkisEntity.SkisType.getByName(stack.getOrCreateTag().getString(NBT_TYPE));
		} catch (Exception e) {
			return SkisType.ACACIA;
		}
	}

	public void setSkisType(ItemStack stack, String type) {
		stack.getOrCreateTag().putString(NBT_TYPE, type);
	}

	@Override
	public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> items) {
		if (allowdedIn(group)) {
			for (SkisEntity.SkisType type : SkisEntity.SkisType.values()) {
				ItemStack stack = new ItemStack(this);
				setSkisType(stack, type.getName());
				items.add(stack);
			}
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		SkisType type = getSkisType(stack);
		IFormattableTextComponent name = type.getPlank().getName();
		tooltip.add(new StringTextComponent(
				TextUtils.addModTranslationToolTip(tooltip, ModConstants.MOD_ID, "type").getString() + ": "
						+ name.getString()).withStyle(TextFormatting.GRAY));
	}
}
