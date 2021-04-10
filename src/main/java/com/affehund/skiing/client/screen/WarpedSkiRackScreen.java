package com.affehund.skiing.client.screen;

import com.affehund.skiing.common.container.WarpedSkiRackContainer;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

/**
 * @author Affehund
 *
 */
public class WarpedSkiRackScreen extends AbstractSkiRackScreen<WarpedSkiRackContainer> {
	public WarpedSkiRackScreen(WarpedSkiRackContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
	}
}
