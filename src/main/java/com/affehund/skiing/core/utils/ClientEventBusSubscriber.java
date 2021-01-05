package com.affehund.skiing.core.utils;

import com.affehund.skiing.common.item.PulloverItem;
import com.affehund.skiing.core.ModConstants;
import com.affehund.skiing.core.init.ModItems;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = ModConstants.MOD_ID, bus = Bus.MOD, value = Dist.CLIENT)
public class ClientEventBusSubscriber {
	@SubscribeEvent
	public static void onColorHandlerEvent(ColorHandlerEvent.Item event) {
		event.getItemColors().register(
				(stack, layer) -> layer != 0 ? -1 : ((PulloverItem) stack.getItem()).getColor(stack),
				ModItems.PULLOVER.get());
	}
}
