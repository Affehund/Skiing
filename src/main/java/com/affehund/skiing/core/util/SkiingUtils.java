package com.affehund.skiing.core.util;

import com.affehund.skiing.common.item.PulloverItem;
import com.affehund.skiing.common.item.SkiStickItem;
import com.affehund.skiing.core.registry.SkiingItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class SkiingUtils {
    public static void breakBlocksInRadius(Level level, Player player, int radius) {
        if (!level.isClientSide) {
            List<BlockPos> blocksToBreak = getBlocksToBreak(level, player, radius);
            ItemStack itemStack = player.getMainHandItem();
            int silktouch = itemStack.getEnchantmentLevel(Enchantments.SILK_TOUCH);
            int fortune = itemStack.getEnchantmentLevel(Enchantments.BLOCK_FORTUNE);
            for (BlockPos pos : blocksToBreak) {
                BlockState blockState = level.getBlockState(pos);
                if (itemStack.getItem().isCorrectToolForDrops(blockState)) {
                    if (player.getAbilities().instabuild) {
                        if (blockState.onDestroyedByPlayer(level, pos, player, true, blockState.getFluidState())) {
                            blockState.getBlock().destroy(level, pos, blockState);
                        }
                    } else {
                        itemStack.getItem().mineBlock(itemStack, level, blockState, pos, player);
                        BlockEntity blockEntity = level.getBlockEntity(pos);
                        blockState.getBlock().destroy(level, pos, blockState);
                        blockState.getBlock().playerDestroy(level, player, pos, blockState, blockEntity, itemStack);
                        blockState.getBlock().popExperience((ServerLevel) level, pos,
                                blockState.getBlock().getExpDrop(blockState, level, RandomSource.create(), pos, fortune, silktouch));
                    }
                    level.removeBlock(pos, false);
                    level.levelEvent(2001, pos, Block.getId(blockState));
                    ((ServerPlayer) player).connection.send(new ClientboundBlockUpdatePacket(level, pos));
                }
            }
        }
    }

    public static List<BlockPos> getBlocksToBreak(Level level, Player player, int radius) {
        ArrayList<BlockPos> blockPosList = new ArrayList<>();
        BlockHitResult hitResult = getLookingAtBlockHitResult(level, player);

        if (hitResult.getType() == HitResult.Type.BLOCK) {
            Direction.Axis axis = hitResult.getDirection().getAxis();
            ArrayList<BlockPos> positions = new ArrayList<>();

            for (int x = -radius; x <= radius; x++) {
                for (int y = -radius; y <= radius; y++) {
                    for (int z = -radius; z <= radius; z++) {
                        positions.add(new BlockPos(x, y, z));
                    }
                }
            }

            BlockPos originPos = hitResult.getBlockPos();

            for (BlockPos pos : positions) {
                if (axis == Direction.Axis.Y) {
                    if (pos.getY() == 0) {
                        blockPosList.add(originPos.offset(pos));
                    }
                } else if (axis == Direction.Axis.X) {
                    if (pos.getX() == 0) {
                        blockPosList.add(originPos.offset(pos));
                    }
                } else if (axis == Direction.Axis.Z) {
                    if (pos.getZ() == 0) {
                        blockPosList.add(originPos.offset(pos));
                    }
                }
            }
            blockPosList.remove(originPos);

            blockPosList.removeIf(newPos -> (level.getBlockState(newPos).getBlock() != level
                    .getBlockState(originPos).getBlock()));
        }
        return blockPosList;
    }

    public static BlockHitResult getLookingAtBlockHitResult(Level level, Player player) {
        Vec3 eyePosition = player.getEyePosition(1);
        Vec3 rotation = player.getViewVector(1);
        Vec3 combined = eyePosition.add(rotation.x * 5, rotation.y * 5, rotation.z * 5);

        return level.clip(new ClipContext(eyePosition, combined,
                ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));
    }

    public static ItemStack getRandomPullover() {
        ItemStack stack = new ItemStack(SkiingItems.PULLOVER.get());
        ((PulloverItem) stack.getItem()).setColor(stack, DyeColor.values()[new Random().nextInt(DyeColor.values().length)].getFireworkColor());
        return stack;
    }

    public static ItemStack getRandomVehicle(Item item) {
        ItemStack stack = new ItemStack(item);
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putString("skiing_material", Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(SkiingMaterial.getRandom().getBlock())).toString());
        stack.addTagElement("EntityTag", compoundTag);
        return stack;
    }

    public static Item getRandomSkiStick() {
        List<Item> skiSticks = ForgeRegistries.ITEMS.getValues().stream().filter(i -> i instanceof SkiStickItem).toList();
        return skiSticks.get(new Random().nextInt(skiSticks.size()));
    }

}
