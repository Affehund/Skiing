package com.affehund.skiing.client.screen;

import com.affehund.skiing.common.container.DarkOakSkiRackContainer;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

/**
 * @author Affehund
 *
 */
public class DarkOakSkiRackScreen extends AbstractSkiRackScreen<DarkOakSkiRackContainer> {
	public DarkOakSkiRackScreen(DarkOakSkiRackContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
	}
}
