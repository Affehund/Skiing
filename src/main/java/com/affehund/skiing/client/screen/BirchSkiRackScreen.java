package com.affehund.skiing.client.screen;

import com.affehund.skiing.common.container.BirchSkiRackContainer;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

/**
 * @author Affehund
 *
 */
public class BirchSkiRackScreen extends AbstractSkiRackScreen<BirchSkiRackContainer> {
	public BirchSkiRackScreen(BirchSkiRackContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
	}
}
