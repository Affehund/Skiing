package com.affehund.skiing.common.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

/**
 * @author Affehund
 *
 */
public abstract class AbstractSkiRackContainer extends Container {

	protected AbstractSkiRackContainer(ContainerType<?> type, int id) {
		super(type, id);
		// TODO Auto-generated constructor stub
	}

	public void addDefaultSlots(PlayerInventory playerInv) {
		// hotbar slots
		for (int i = 0; i < 9; ++i) {
			this.addSlot(new Slot(playerInv, i, 8 + i * 18, 142));
		}

		// player inv slots
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}
	}

	@Override
	public abstract boolean stillValid(PlayerEntity playerIn);

	@Override
	public ItemStack quickMoveStack(PlayerEntity playerIn, int index) {
		final int invSize = 4;
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot != null && slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();
			if (index < invSize) {
				if (!this.moveItemStackTo(itemstack1, invSize, this.slots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.moveItemStackTo(itemstack1, 0, invSize, false)) {
				return ItemStack.EMPTY;
			}

			if (itemstack1.isEmpty()) {
				slot.set(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}
		}
		return itemstack;
	}

}
