//// Made with Blockbench 3.7.4
//// Exported for Minecraft version 1.15
//// Paste this class into your mod and generate all required imports
//
//
//public class custom_model extends EntityModel<Entity> {
//	private final ModelRenderer left;
//	private final ModelRenderer right;
//
//	public custom_model() {
//		textureWidth = 128;
//		textureHeight = 128;
//
//		left = new ModelRenderer(this);
//		left.setRotationPoint(0.0F, 24.0F, 0.0F);
//		left.setTextureOffset(0, 0).addBox(-12.0F, -3.0F, -7.0F, 27.0F, 3.0F, 5.0F, 0.0F, false);
//		left.setTextureOffset(0, 0).addBox(-14.0F, -3.5F, -6.5F, 2.0F, 3.0F, 4.0F, 0.0F, false);
//		left.setTextureOffset(0, 0).addBox(-15.0F, -4.0F, -6.0F, 1.0F, 3.0F, 3.0F, 0.0F, false);
//
//		right = new ModelRenderer(this);
//		right.setRotationPoint(0.0F, 24.0F, 0.0F);
//		right.setTextureOffset(0, 5).addBox(-12.0F, -3.0F, 2.0F, 27.0F, 3.0F, 5.0F, 0.0F, false);
//		right.setTextureOffset(0, 0).addBox(-14.0F, -3.5F, 2.5F, 2.0F, 3.0F, 4.0F, 0.0F, false);
//		right.setTextureOffset(0, 0).addBox(-15.0F, -4.0F, 3.0F, 1.0F, 3.0F, 3.0F, 0.0F, false);
//	}
//
//	@Override
//	public void setRotationAngles(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
//		//previously the render function, render code was moved to a method below
//	}
//
//	@Override
//	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
//		left.render(matrixStack, buffer, packedLight, packedOverlay);
//		right.render(matrixStack, buffer, packedLight, packedOverlay);
//	}
//
//	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
//		modelRenderer.rotateAngleX = x;
//		modelRenderer.rotateAngleY = y;
//		modelRenderer.rotateAngleZ = z;
//	}
//}