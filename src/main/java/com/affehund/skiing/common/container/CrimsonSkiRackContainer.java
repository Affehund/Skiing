package com.affehund.skiing.common.container;

import java.util.Objects;

import com.affehund.skiing.common.container.slot.IsItemValidSlot;
import com.affehund.skiing.common.tile.CrimsonSkiRackTileEntity;
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
public class CrimsonSkiRackContainer extends AbstractSkiRackContainer {

	public final CrimsonSkiRackTileEntity tileEntity;
	private final IWorldPosCallable canInteractWithCallable;

	public CrimsonSkiRackContainer(final int windowId, final PlayerInventory playerInventory,
			final CrimsonSkiRackTileEntity tileEntityIn) {
		super(ModContainers.CRIMSON_SKI_RACK_CONTAINER.get(), windowId);
		this.tileEntity = tileEntityIn;
		this.canInteractWithCallable = IWorldPosCallable.create(tileEntityIn.getLevel(), tileEntityIn.getBlockPos());

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

	public CrimsonSkiRackContainer(final int windowId, final PlayerInventory playerInv, final PacketBuffer data) {
		this(windowId, playerInv, getTileEntity(playerInv, data));
	}

	private static CrimsonSkiRackTileEntity getTileEntity(final PlayerInventory playerInv, final PacketBuffer data) {
		Objects.requireNonNull(playerInv, "playerInv cannot be null");
		Objects.requireNonNull(data, "data cannot be null");
		final TileEntity tileAtPos = playerInv.player.level.getBlockEntity(data.readBlockPos());
		if (tileAtPos instanceof CrimsonSkiRackTileEntity) {
			return (CrimsonSkiRackTileEntity) tileAtPos;
		}
		throw new IllegalStateException("TileEntity is not correct " + tileAtPos);
	}

	@Override
	public boolean stillValid(PlayerEntity playerIn) {
		return stillValid(canInteractWithCallable, playerIn, ModBlocks.CRIMSON_SKI_RACK_BLOCK.get());
	}

}
