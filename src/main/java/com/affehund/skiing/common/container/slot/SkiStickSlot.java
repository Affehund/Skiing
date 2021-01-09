package com.affehund.skiing.common.container.slot;

import com.affehund.skiing.core.init.ModItems;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class SkiStickSlot extends Slot {

	public SkiStickSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
	}
	
	@Override
	public boolean isItemValid(ItemStack stack) {
		return stack.getItem() == ModItems.SKI_STICK_ITEM.get();
	}
}
