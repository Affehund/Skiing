package com.affehund.skiing.client.render;

import com.affehund.skiing.common.entity.SnowboardEntity;
import com.affehund.skiing.common.item.SnowboardItem;
import com.affehund.skiing.core.init.ModEntities;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SnowboardItemISTER extends ItemStackTileEntityRenderer {
	@Override
	public void renderByItem(ItemStack stack, TransformType transformType, MatrixStack matrixStack,
			IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
		if (stack.getItem() instanceof SnowboardItem) {
			matrixStack.pushPose();
			matrixStack.translate(0.5, 0, 0.5);
			if (transformType == ItemCameraTransforms.TransformType.GUI) {
				matrixStack.mulPose(Vector3f.YP.rotationDegrees(180));
			}

			if (transformType == ItemCameraTransforms.TransformType.FIXED) {
				matrixStack.mulPose(Vector3f.XN.rotationDegrees(180));
			}

			Minecraft mc = Minecraft.getInstance();
			SnowboardEntity snowboard = new SnowboardEntity(ModEntities.SNOWBOARD_ENTITY.get(), mc.level);
			EntityRenderer<? super SnowboardEntity> render = mc.getEntityRenderDispatcher().getRenderer(snowboard);
			if (mc.level != null) {
				snowboard.setLevel(mc.level);
			}
			snowboard.setSnowboardType(SnowboardItem.getSnowboardType(stack));
			render.render(snowboard, 0, mc.getFrameTime(), matrixStack, buffer, combinedLight);
			matrixStack.popPose();
		}
	}
}
