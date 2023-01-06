package com.affehund.skiing.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class SnowboardModel<T extends Entity> extends EntityModel<T> {
    private final ModelPart snowboard;

    public SnowboardModel(ModelPart root) {
        this.snowboard = root.getChild("snowboard");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition snowboard = partdefinition.addOrReplaceChild("snowboard", CubeListBuilder.create().texOffs(0, 0).addBox(-14.0F, -1.75F, -2.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(12.0F, -1.25F, -3.0F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-13.0F, -1.25F, -3.0F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 2).addBox(-12.0F, -1.0F, -3.5F, 12.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(13.0F, -1.75F, -2.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition cube_r1 = snowboard.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 6).addBox(-5.5F, -1.0F, -3.5F, 12.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.5F, 0.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

        return LayerDefinition.create(meshdefinition, 16, 16);
    }

    @Override
    public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        snowboard.render(poseStack, buffer, packedLight, packedOverlay);
    }
}