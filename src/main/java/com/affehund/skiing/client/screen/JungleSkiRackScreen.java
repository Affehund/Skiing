package com.affehund.skiing.client.screen;

import com.affehund.skiing.common.container.JungleSkiRackContainer;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

/**
 * @author Affehund
 *
 */
public class JungleSkiRackScreen extends AbstractSkiRackScreen<JungleSkiRackContainer> {
	public JungleSkiRackScreen(JungleSkiRackContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
	}
}
