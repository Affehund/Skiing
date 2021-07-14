package com.affehund.skiing.client.render.ski_rack;

import com.affehund.skiing.common.block.AbstractSkiRackBlock;
import com.affehund.skiing.common.tile.AbstractSkiRackTileEntity;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.vector.Vector3f;

/**
 * @author Affehund
 *
 */
public class AbstractSkiRackTESR<T extends AbstractSkiRackTileEntity> extends TileEntityRenderer<T> {
	public AbstractSkiRackTESR(TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn);
	}

	@Override
	public void render(T tile, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight,
			int combinedOverlay) {
		NonNullList<ItemStack> items = tile.getItems();
		BlockState state = tile.getTileEntity().getBlockState();
		if (items.size() > 0) {

			matrixStack.pushPose();
			matrixStack.scale(0.9f, 1.5f, 0.9f);
			matrixStack.translate(0f, 0f, 0f);

			Direction direction = state.getValue(AbstractSkiRackBlock.DIRECTION);
			switch (direction) {
			case NORTH:
				break;
			case EAST:
				matrixStack.mulPose(Vector3f.YP.rotationDegrees(270f));
				matrixStack.translate(0f, 0f, -1.1f);
				break;

			case SOUTH:
				matrixStack.mulPose(Vector3f.YP.rotationDegrees(180f));
				matrixStack.translate(-1.1f, 0f, -1.1f);
				break;
			case WEST:
				matrixStack.mulPose(Vector3f.YP.rotationDegrees(90f));
				matrixStack.translate(-1.1f, 0f, 0f);
				break;

			default:
				break;
			}
			matrixStack.translate(0.5f, 0.5f, 0.72f);
			for (int i = 0; i < items.size(); i++) {
				ItemStack stack = tile.getItem(i);
				if (i < 2) {
					if (i == 0) {
						matrixStack.translate(0.3f, 0f, 0f);
					} else
						matrixStack.translate(-0.5f, 0f, 0f);
					renderItem(stack, partialTicks, matrixStack, buffer, combinedLight);
				}
			}
			matrixStack.popPose();
		}
	}

	private void renderItem(ItemStack stack, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer,
			int combinedLight) {
		Minecraft.getInstance().getItemRenderer().renderStatic(stack, TransformType.FIXED, combinedLight,
				OverlayTexture.NO_OVERLAY, matrixStack, buffer);
	}
}
