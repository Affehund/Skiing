package com.affehund.skiing.common.container.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class IsItemValidSlot extends Slot {
	
	public final Item itemToCheck;

	public IsItemValidSlot(IInventory inventoryIn, int index, int xPosition, int yPosition, Item itemToCheck) {
		super(inventoryIn, index, xPosition, yPosition);
		this.itemToCheck = itemToCheck;
	}
	
	@Override
	public boolean mayPlace(ItemStack stack) {
		return stack.getItem() == itemToCheck;
	}
}
