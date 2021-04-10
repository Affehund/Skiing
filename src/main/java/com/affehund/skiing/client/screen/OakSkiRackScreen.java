package com.affehund.skiing.client.screen;

import com.affehund.skiing.common.container.OakSkiRackContainer;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

/**
 * @author Affehund
 *
 */
public class OakSkiRackScreen extends AbstractSkiRackScreen<OakSkiRackContainer> {
	public OakSkiRackScreen(OakSkiRackContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
	}
}
