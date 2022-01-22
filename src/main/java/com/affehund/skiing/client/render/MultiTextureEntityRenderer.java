package com.affehund.skiing.client.render;

import com.affehund.skiing.common.entity.AbstractMultiTextureEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.ForgeModelBakery;
import net.minecraftforge.client.model.data.EmptyModelData;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

public class MultiTextureEntityRenderer extends EntityRenderer<AbstractMultiTextureEntity> {
    private final EntityModel<AbstractMultiTextureEntity> model;
    private static final HashMap<Block, ResourceLocation> textureCache = new HashMap<>();
    private static final ResourceLocation DEFAULT_TEXTURE = new ResourceLocation("textures/block/oak_planks.png");

    public MultiTextureEntityRenderer(EntityRendererProvider.Context context, EntityModel<AbstractMultiTextureEntity> model) {
        super(context);
        this.model = model;
    }

    @Override
    public void render(AbstractMultiTextureEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.translate(0.0D, 1.5D, 0.0D);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(90.0F - entityYaw));
        poseStack.mulPose(Vector3f.XP.rotationDegrees(180F));

        float f = (float) entity.getTimeSinceHit() - partialTicks;
        float f1 = entity.getDamageTaken() - partialTicks;
        if (f1 < 0.0F) {
            f1 = 0.0F;
        }

        if (f > 0.0F) {
            poseStack.mulPose(Vector3f.XP
                    .rotationDegrees(Mth.sin(f) * f * f1 / 45.0F));
        }
        this.model.setupAnim(entity, partialTicks, 0.0F, 0.0F, 0.0F, 0.0F);

        VertexConsumer vertexConsumer = buffer.getBuffer(this.model.renderType(this.getTextureLocation(entity)));
        this.model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F,
                1.0F);

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(AbstractMultiTextureEntity entity) {
        Block block = entity.getSkiingMaterial();
        if (textureCache.containsKey(block)) {
            return textureCache.get(block);
        }

        ResourceLocation resourceLocation;
        try {
            ResourceLocation sprite = Minecraft.getInstance().getModelManager().getModel(ForgeModelBakery.getInventoryVariant(Objects.requireNonNull(block.getRegistryName()).toString())).getQuads(block.defaultBlockState(), Direction.UP, new Random(1), EmptyModelData.INSTANCE).get(0).getSprite().getName();
            resourceLocation = new ResourceLocation(sprite.getNamespace(), "textures/" + sprite.getPath() + ".png");
        } catch (IndexOutOfBoundsException | NullPointerException exception) {
            resourceLocation = DEFAULT_TEXTURE;
        }

        textureCache.put(block, resourceLocation);
        return resourceLocation;
    }
}
