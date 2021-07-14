package com.affehund.skiing.common.tile;

import com.affehund.skiing.common.container.OakSkiRackContainer;
import com.affehund.skiing.core.init.ModBlocks;
import com.affehund.skiing.core.init.ModTileEntities;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * @author Affehund
 *
 */
public class OakSkiRackTileEntity extends AbstractSkiRackTileEntity {

	public OakSkiRackTileEntity(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
	}

	public OakSkiRackTileEntity() {
		this(ModTileEntities.OAK_SKI_RACK_TILE_ENTITY.get());
	}

	@Override
	protected Container createMenu(int id, PlayerInventory playerInventory) {
		return new OakSkiRackContainer(id, playerInventory, this);
	}

	@Override
	protected ITextComponent getDefaultName() {
		return new TranslationTextComponent(ModBlocks.OAK_SKI_RACK_BLOCK.get().getDescriptionId());
	}

}
