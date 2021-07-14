package com.affehund.skiing.client.render;

import com.affehund.skiing.client.model.SnowboardModel;
import com.affehund.skiing.common.entity.SnowboardEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SnowboardRenderer extends EntityRenderer<SnowboardEntity> {
	protected final SnowboardModel model = new SnowboardModel();

	public SnowboardRenderer(EntityRendererManager renderManager) {
		super(renderManager);
	}

	@Override
	public void render(SnowboardEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn,
			IRenderTypeBuffer bufferIn, int packedLightIn) {
		matrixStackIn.pushPose();

		matrixStackIn.translate(0.0D, 1.5D, 0.0D);
		matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(90.0F - entityYaw));
		matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(180F));
		float f = entityIn.getTimeSinceHit() - partialTicks;
		float f1 = entityIn.getDamageTaken() - partialTicks;
		if (f1 < 0.0F) {
			f1 = 0.0F;
		}

		if (f > 0.0F) {
			matrixStackIn.mulPose(
					Vector3f.XP.rotationDegrees(MathHelper.sin(f) * f * f1 / 45.0F * entityIn.getForwardDirection()));
		}

		this.model.setupAnim(entityIn, partialTicks, 0.0F, 0.0F, 0.0F, 0.0F);

		IVertexBuilder ivertexbuilder = bufferIn.getBuffer(this.model.renderType(this.getTextureLocation(entityIn)));
		this.model.renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F,
				1.0F);

		matrixStackIn.popPose();
		super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	@Override
	public ResourceLocation getTextureLocation(SnowboardEntity entity) {
		return entity.getSnowboardType().getTexture();
	}
}
