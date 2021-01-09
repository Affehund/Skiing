package com.affehund.skiing.core.utils;

import java.util.List;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

public class TextUtils {
	public static TranslationTextComponent addModTranslationToolTip(List<ITextComponent> tooltip, String modID, String text) {
		return new TranslationTextComponent(modID + ".tooltip." + text);
	}
	
	public static TranslationTextComponent addModTranslationToolTip(List<ITextComponent> tooltip, String modID, String text, TextFormatting... formats) {
		return new TranslationTextComponent(modID + ".tooltip." + text);
	}
}
