package com.affehund.skiing.client.render;

import com.affehund.skiing.common.block.SkiRackBlock;
import com.affehund.skiing.common.block_entity.SkiRackBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class SkiRackBlockEntityRenderer implements BlockEntityRenderer<SkiRackBlockEntity> {
    public SkiRackBlockEntityRenderer() {}

    @Override
    public void render(SkiRackBlockEntity blockEntity, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        NonNullList<ItemStack> items = blockEntity.getItems();
        BlockState state = blockEntity.getBlockState();
        if (items.size() > 0) {

            poseStack.pushPose();

            Direction direction = state.getValue(SkiRackBlock.FACING);
            switch (direction) {
                case EAST -> {
                    poseStack.mulPose(Vector3f.YP.rotationDegrees(270f));
                    poseStack.translate(0f, 0f, -1f);
                }
                case SOUTH -> {
                    poseStack.mulPose(Vector3f.YP.rotationDegrees(180f));
                    poseStack.translate(-1f, 0f, -1f);
                }
                case WEST -> {
                    poseStack.mulPose(Vector3f.YP.rotationDegrees(90f));
                    poseStack.translate(-1f, 0f, 0f);
                }
                default -> {
                }
            }
            poseStack.translate(0.5f, 0.5f, 0.72f);
            for (int i = 0; i < items.size(); i++) {
                ItemStack stack = blockEntity.getItem(i);
                if (i < 2) {
                    if (i == 0) {
                        poseStack.translate(0.3f, 0f, 0f);
                    } else
                        poseStack.translate(-0.5f, 0f, 0f);
                        Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemTransforms.TransformType.FIXED, combinedLight, combinedOverlay, poseStack, buffer, OverlayTexture.NO_OVERLAY);
                }
            }
            poseStack.popPose();
        }
    }
}
