package com.affehund.skiing.client.screen;

import com.affehund.skiing.common.container.CrimsonSkiRackContainer;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

/**
 * @author Affehund
 *
 */
public class CrimsonSkiRackScreen extends AbstractSkiRackScreen<CrimsonSkiRackContainer> {
	public CrimsonSkiRackScreen(CrimsonSkiRackContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
	}
}
