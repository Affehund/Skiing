package com.affehund.skiing.client.screen;

import com.affehund.skiing.common.container.AcaciaSkiRackContainer;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

/**
 * @author Affehund
 *
 */
public class AcaciaSkiRackScreen extends AbstractSkiRackScreen<AcaciaSkiRackContainer> {
	public AcaciaSkiRackScreen(AcaciaSkiRackContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
	}
}
