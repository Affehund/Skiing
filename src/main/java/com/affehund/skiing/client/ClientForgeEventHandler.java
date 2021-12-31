package com.affehund.skiing.client;

import com.affehund.skiing.Skiing;
import com.affehund.skiing.common.entity.AbstractControllableEntity;
import com.affehund.skiing.common.item.SnowShovelItem;
import com.affehund.skiing.core.util.SkiingUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.Input;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.DrawSelectionEvent;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE, modid = Skiing.MOD_ID)
public class ClientForgeEventHandler {
    @SubscribeEvent
    public static void keyInput(@NotNull MovementInputUpdateEvent event) {
        Player player = event.getPlayer();
        if (player != null) {
            if (player.getVehicle() instanceof AbstractControllableEntity controllableEntity) {
                if (player.equals(controllableEntity.getControllingPassenger())) {
                    Input input = event.getInput();
                    controllableEntity.updateControls(input.up, input.down, input.left, input.right, player);
                }
            }
        }
    }

    @SubscribeEvent
    public static void highlightBlock(DrawSelectionEvent.HighlightBlock event) {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            Level level = player.level;
            ItemStack itemStack = player.getMainHandItem();
            if (!itemStack.isEmpty() && itemStack.getItem() instanceof SnowShovelItem) {
                Camera camera =  Minecraft.getInstance().gameRenderer.getMainCamera();
                BlockPos lookingAtPos = SkiingUtils.getLookingAtBlockHitResult(level, player).getBlockPos();
                LevelRenderer levelRenderer = event.getLevelRenderer();
                PoseStack poseStack = event.getPoseStack();
                VertexConsumer vertexConsumer = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(RenderType.lines());
                Entity viewEntity = camera.getEntity();
                Vec3 cameraPos = camera.getPosition();
                Iterable<BlockPos> blocks = SkiingUtils.getBlocksToBreak(level, player, player.isCrouching() ? 0 : 1);

                if (itemStack.isCorrectToolForDrops(level.getBlockState(lookingAtPos))) {
                    for (BlockPos pos : blocks) {
                        if (level.getWorldBorder().isWithinBounds(pos)) {
                            BlockState state = level.getBlockState(pos);
                            if (itemStack.isCorrectToolForDrops(state)) {
                                poseStack.pushPose();
                                levelRenderer.renderHitOutline(poseStack, vertexConsumer, viewEntity,
                                        cameraPos.x, cameraPos.y, cameraPos.z, pos, level.getBlockState(pos));
                                poseStack.popPose();
                            }
                        }
                    }
                }
            }
        }
    }
}
