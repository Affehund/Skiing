package com.affehund.skiing.client.render;

import java.util.function.Supplier;

import com.affehund.skiing.common.block.SkiRackBlock;
import com.affehund.skiing.common.item.SkiRackItem;
import com.affehund.skiing.common.tile.SkiRackTileEntity;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.common.util.Constants;

@OnlyIn(Dist.CLIENT)
public class SkiRackISTER<T extends TileEntity> extends ItemStackTileEntityRenderer {
	private final Supplier<T> te;

	public SkiRackISTER(Supplier<T> te) {
		this.te = te;
	}

	@Override
	public void func_239207_a_(ItemStack stack, TransformType transformType, MatrixStack matrixStack,
			IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
		if (stack.getItem() instanceof SkiRackItem) {
			Minecraft mc = Minecraft.getInstance();
			SkiRackItem skiRackItem = (SkiRackItem) stack.getItem();
			TileEntity tile = te.get();
			Block block = Block.getBlockFromItem(skiRackItem);
			BlockState state = block.getDefaultState();

			TileEntityRenderer<TileEntity> renderer = TileEntityRendererDispatcher.instance.getRenderer(tile);
			if (renderer != null) {

				if (mc.world != null) {
					tile.setWorldAndPos(mc.world, BlockPos.ZERO);
				}
				tile.cachedBlockState = state;

				CompoundNBT nbt = stack.getTag();
				if (tile instanceof SkiRackTileEntity && block instanceof SkiRackBlock) {
					if (nbt != null && nbt.contains("Type")) {
						String typeTag = nbt.getString("Type");
						state = block.getDefaultState().getBlockState().with(SkiRackBlock.TYPE,
								SkiRackBlock.SkiRackType.getByName(typeTag));
					}
				}

				if (nbt != null && nbt.contains("BlockEntityTag", Constants.NBT.TAG_COMPOUND)) {
					CompoundNBT blockTag = nbt.getCompound("BlockEntityTag");
					tile.read(state, blockTag);
				}
				
				if (mc.world != null) {
					tile.setWorldAndPos(mc.world, BlockPos.ZERO);
				}
				tile.cachedBlockState = state;

				matrixStack.push();
				if (state.getRenderType() != BlockRenderType.ENTITYBLOCK_ANIMATED) {
					mc.getBlockRendererDispatcher().renderBlock(state, matrixStack, buffer, combinedLight,
							combinedOverlay, EmptyModelData.INSTANCE);
				}
				renderer.render(tile, mc.getRenderPartialTicks(), matrixStack, buffer, combinedLight, combinedOverlay);
				matrixStack.pop();
			}
		}
	}
}
