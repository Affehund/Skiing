package com.affehund.skiing.core.utils;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.world.World;

public class DrinkableItem extends Item {

	private final Item returnItem;
	private final boolean cureEffects;
	private final boolean addConsumeTrigger;

	public DrinkableItem(Item.Properties properties, Item returnItem, boolean cureEffects, boolean addConsumeTrigger) {
		super(properties);
		this.returnItem = returnItem;
		this.cureEffects = cureEffects;
		this.addConsumeTrigger = addConsumeTrigger;
	}

	/**
	 * Called when the player finishes using this Item (E.g. finishes eating.). Not
	 * called when the player stops using the Item before the action is complete.
	 */
	public ItemStack finishUsingItem(ItemStack stack, World worldIn, LivingEntity entityLiving) {
		ItemStack itemStack = super.finishUsingItem(stack, worldIn, entityLiving);

		if (cureEffects && !worldIn.isClientSide) {
			entityLiving.removeAllEffects();
		}

		if (addConsumeTrigger && entityLiving instanceof ServerPlayerEntity) {
			ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) entityLiving;
			CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayerEntity, itemStack);
			serverPlayerEntity.awardStat(Stats.ITEM_USED.get(this));
		}

		return entityLiving instanceof PlayerEntity && ((PlayerEntity) entityLiving).abilities.instabuild
				? itemStack
				: new ItemStack(returnItem);

	}
}
