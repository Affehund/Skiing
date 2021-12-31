package com.affehund.skiing.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class SkiModel<T extends Entity> extends EntityModel<T> {
	private final ModelPart left_ski;
	private final ModelPart right_ski;

	public SkiModel(ModelPart root) {
		this.left_ski = root.getChild("left_ski");
		this.right_ski = root.getChild("right_ski");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition left_ski = partdefinition.addOrReplaceChild("left_ski", CubeListBuilder.create().texOffs(0, 1).addBox(0.0F, -1.0F, -4.5F, 16.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(11, 7).addBox(-15.5F, -1.75F, -3.5F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(3, 2).addBox(-14.75F, -1.25F, -4.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 1).addBox(-14.0F, -1.0F, -4.5F, 14.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition right_ski = partdefinition.addOrReplaceChild("right_ski", CubeListBuilder.create().texOffs(0, 6).addBox(0.0F, -1.0F, 0.5F, 16.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 6).addBox(-14.0F, -1.0F, 0.5F, 14.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(3, 6).addBox(-14.75F, -1.25F, 1.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(3, 2).addBox(-15.5F, -1.75F, 1.5F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 16, 16);
	}

	@Override
	public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		left_ski.render(poseStack, buffer, packedLight, packedOverlay);
		right_ski.render(poseStack, buffer, packedLight, packedOverlay);
	}
}