package com.affehund.skiing.core.utils;

import java.util.List;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

public class TextUtils {
	public static void addModTranslationToolTip(List<ITextComponent> tooltip, String modID, String text) {
		tooltip.add(new TranslationTextComponent(modID + ".tooltip." + text));
	}
	
	public static void addModTranslationToolTip(List<ITextComponent> tooltip, String modID, String text, TextFormatting... formats) {
		tooltip.add(new TranslationTextComponent(modID + ".tooltip." + text).mergeStyle(formats));
	}
}
