package com.affehund.skiing.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class SledModel<T extends Entity> extends EntityModel<T> {
    private final ModelPart seat;
    private final ModelPart skid;
    private final ModelPart scaffold;

    public SledModel(ModelPart root) {
        this.seat = root.getChild("seat");
        this.skid = root.getChild("skid");
        this.scaffold = root.getChild("scaffold");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition seat = partdefinition.addOrReplaceChild("seat", CubeListBuilder.create().texOffs(0, 2).addBox(-12.5F, -7.0F, 5.0F, 13.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 6).addBox(0.5F, -7.0F, 5.0F, 13.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 4).addBox(0.5F, -7.0F, -7.0F, 13.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 10).addBox(-12.5F, -7.0F, -7.0F, 13.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-6.5F, -7.0F, 1.0F, 20.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-6.5F, -7.0F, -3.0F, 20.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition skid = partdefinition.addOrReplaceChild("skid", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition right_skid = skid.addOrReplaceChild("right_skid", CubeListBuilder.create().texOffs(0, 4).addBox(-8.5F, -1.0F, 5.0F, 11.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 2).addBox(2.5F, -1.0F, 5.0F, 11.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-10.5F, -1.75F, 5.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-11.5F, -2.5F, 5.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-12.5F, -4.25F, 5.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-13.5F, -9.0F, 5.0F, 1.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition left_skid = skid.addOrReplaceChild("left_skid", CubeListBuilder.create().texOffs(0, 2).addBox(-8.5F, -1.0F, -7.0F, 11.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 2).addBox(2.5F, -1.0F, -7.0F, 11.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-10.5F, -1.75F, -7.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-11.5F, -2.5F, -7.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-12.5F, -4.25F, -7.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-13.5F, -9.0F, -7.0F, 1.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition scaffold = partdefinition.addOrReplaceChild("scaffold", CubeListBuilder.create().texOffs(0, 0).addBox(-13.5F, -8.5F, -5.0F, 1.0F, 1.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition right_scaffold = scaffold.addOrReplaceChild("right_scaffold", CubeListBuilder.create().texOffs(0, 0).addBox(9.5F, -5.0F, 5.0F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(2.5F, -5.0F, 5.0F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-4.5F, -5.0F, 5.0F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition middle_scaffold = scaffold.addOrReplaceChild("middle_scaffold", CubeListBuilder.create().texOffs(0, 0).addBox(9.5F, -6.0F, -7.5F, 1.0F, 1.0F, 15.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(2.5F, -6.0F, -7.5F, 1.0F, 1.0F, 15.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-4.5F, -6.0F, -7.5F, 1.0F, 1.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition left_scaffold = scaffold.addOrReplaceChild("left_scaffold", CubeListBuilder.create().texOffs(0, 0).addBox(9.5F, -5.0F, -7.0F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(2.5F, -5.0F, -7.0F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-4.5F, -5.0F, -7.0F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 16, 16);
    }

    @Override
    public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        seat.render(poseStack, buffer, packedLight, packedOverlay);
        skid.render(poseStack, buffer, packedLight, packedOverlay);
        scaffold.render(poseStack, buffer, packedLight, packedOverlay);
    }
}