package com.affehund.skiing.client.screen;

import com.affehund.skiing.common.container.AbstractSkiRackContainer;
import com.affehund.skiing.core.ModConstants;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author Affehund
 *
 */
@OnlyIn(Dist.CLIENT)
public abstract class AbstractSkiRackScreen<T extends AbstractSkiRackContainer> extends ContainerScreen<T> {

	public static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(ModConstants.MOD_ID,
			"textures/screen/ski_rack_screen.png");

	public AbstractSkiRackScreen(T screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
		this.leftPos = 0;
		this.topPos = 0;
		this.imageWidth = 176;
		this.imageHeight = 166;
	}

	@Override
	public void render(MatrixStack matrixStack, int x, int y, float partialTicks) {
		this.renderBackground(matrixStack);
		super.render(matrixStack, x, y, partialTicks);
		this.renderTooltip(matrixStack, x, y);
	}

	@Override
	protected void renderLabels(MatrixStack matrixStack, int x, int y) {
		String s = this.title.getString();
		this.font.draw(matrixStack, s, this.imageWidth / 2 - this.font.width(s) / 2, 6.0F, 4210752);
		this.font.draw(matrixStack, this.inventory.getDisplayName().getString(), 8.0F, this.imageHeight - 96 + 2,
				4210752);
		this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void renderBg(MatrixStack matrixStack, float partialTicks, int x, int y) {
		RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
		this.minecraft.getTextureManager().bind(BACKGROUND_TEXTURE);
		int relX = (width - imageWidth) / 2;
		int relY = (height - imageHeight) / 2;
		this.blit(matrixStack, relX, relY, 0, 0, this.imageWidth, this.imageHeight);
	}

}
