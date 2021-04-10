package com.affehund.skiing.common.container;

import java.util.Objects;

import com.affehund.skiing.common.container.slot.IsItemValidSlot;
import com.affehund.skiing.common.tile.BirchSkiRackTileEntity;
import com.affehund.skiing.core.init.ModBlocks;
import com.affehund.skiing.core.init.ModContainers;
import com.affehund.skiing.core.init.ModItems;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;

/**
 * @author Affehund
 *
 */
public class BirchSkiRackContainer extends AbstractSkiRackContainer {

	public final BirchSkiRackTileEntity tileEntity;
	private final IWorldPosCallable canInteractWithCallable;

	public BirchSkiRackContainer(final int windowId, final PlayerInventory playerInventory,
			final BirchSkiRackTileEntity tileEntityIn) {
		super(ModContainers.BIRCH_SKI_RACK_CONTAINER.get(), windowId);
		this.tileEntity = tileEntityIn;
		this.canInteractWithCallable = IWorldPosCallable.of(tileEntityIn.getWorld(), tileEntityIn.getPos());

		// container slots
		int index = 0;
		for (int j = 0; j < 2; ++j) {
			this.addSlot(new IsItemValidSlot(tileEntityIn, index++, 62 + j * 36, 28, ModItems.SKIS_ITEM.get()));
		}

		for (int j = 0; j < 2; ++j) {
			this.addSlot(
					new IsItemValidSlot(tileEntityIn, index++, 62 + j * 36, 28 + 18, ModItems.SKI_STICK_ITEM.get()));
		}

		this.addDefaultSlots(playerInventory);
	}

	public BirchSkiRackContainer(final int windowId, final PlayerInventory playerInv, final PacketBuffer data) {
		this(windowId, playerInv, getTileEntity(playerInv, data));
	}

	private static BirchSkiRackTileEntity getTileEntity(final PlayerInventory playerInv, final PacketBuffer data) {
		Objects.requireNonNull(playerInv, "playerInv cannot be null");
		Objects.requireNonNull(data, "data cannot be null");
		final TileEntity tileAtPos = playerInv.player.world.getTileEntity(data.readBlockPos());
		if (tileAtPos instanceof BirchSkiRackTileEntity) {
			return (BirchSkiRackTileEntity) tileAtPos;
		}
		throw new IllegalStateException("TileEntity is not correct " + tileAtPos);
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {
		return isWithinUsableDistance(canInteractWithCallable, playerIn, ModBlocks.BIRCH_SKI_RACK_BLOCK.get());
	}

}
