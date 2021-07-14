package com.affehund.skiing.common.tile;

import com.affehund.skiing.common.item.SkisItem;
import com.affehund.skiing.core.init.ModItems;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.util.Constants;

/**
 * @author Affehund
 *
 */
public abstract class AbstractSkiRackTileEntity extends LockableLootTileEntity {
	protected NonNullList<ItemStack> items = NonNullList.withSize(4, ItemStack.EMPTY);

	public AbstractSkiRackTileEntity(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
	}

	@Override
	public void load(BlockState blockState, CompoundNBT compound) {
		super.load(blockState, compound);
		this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(compound, this.items);
	}

	@Override
	public CompoundNBT save(CompoundNBT compound) {
		super.save(compound);
		ItemStackHelper.saveAllItems(compound, this.items);
		return compound;
	}

	@Override
	public NonNullList<ItemStack> getItems() {
		return this.items;
	}

	@Override
	public void setItems(NonNullList<ItemStack> itemsIn) {
		this.items = itemsIn;
	}

	@Override
	public void setChanged() {
		super.setChanged();
		this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(),
				Constants.BlockFlags.BLOCK_UPDATE);
	}

	@Override
	public int getContainerSize() {
		return this.items.size();
	}

	@Override
	public boolean isEmpty() {
		for (ItemStack stack : this.items) {
			if (!stack.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	public boolean addItem(ItemStack itemStackIn) {
		for (int i = 0; i < 2; ++i) {
			ItemStack itemstack = this.items.get(i);
			if (itemstack.isEmpty()) {
				this.items.set(i, itemStackIn.split(1));
				this.setChanged();
				return true;
			}
		}
		return false;
	}

	@Override
	public ItemStack getItem(int index) {
		return this.items.get(index);
	}

	@Override
	public ItemStack removeItem(int index, int count) {
		return ItemStackHelper.removeItem(this.items, index, count);
	}

	@Override
	public ItemStack removeItemNoUpdate(int index) {
		return ItemStackHelper.takeItem(this.items, index);
	}

	@Override
	public void setItem(int index, ItemStack stack) {
		ItemStack itemStack = this.items.get(index);
		boolean flag = !stack.isEmpty() && stack.sameItem(itemStack)
				&& ItemStack.tagMatches(stack, itemStack);
		this.items.set(index, stack);
		if (stack.getCount() > this.getMaxStackSize()) {
			stack.setCount(this.getMaxStackSize());
		}

		if (!flag) {
			this.setChanged();
		}
	}

	@Override
	public boolean canPlaceItem(int index, ItemStack stack) {
		if ((index == 0 || index == 1) && !(stack.getItem() instanceof SkisItem)) {
			return false;
		} else if ((index == 2 || index == 3) && stack.getItem() != ModItems.SKI_STICK_ITEM.get()) {
			return false;
		}
		return !stack.isDamaged();
	}

	@Override
	public void clearContent() {
		this.items.clear();
	}

	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		CompoundNBT nbt = new CompoundNBT();
		this.save(nbt);
		return new SUpdateTileEntityPacket(this.getBlockPos(), 1, nbt);
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		this.load(getBlockState(), pkt.getTag());
	}

	@Override
	public CompoundNBT getUpdateTag() {
		return this.save(new CompoundNBT());
	}

	@Override
	public void handleUpdateTag(BlockState blockState, CompoundNBT tag) {
		this.load(blockState, tag);
	}

	@Override
	protected abstract Container createMenu(int id, PlayerInventory playerInventory);

	@Override
	protected abstract ITextComponent getDefaultName();
}
