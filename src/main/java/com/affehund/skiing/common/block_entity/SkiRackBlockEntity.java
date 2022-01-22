package com.affehund.skiing.common.block_entity;

import com.affehund.skiing.common.item.SkiItem;
import com.affehund.skiing.common.item.SnowboardItem;
import com.affehund.skiing.core.init.SkiingBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Clearable;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class SkiRackBlockEntity extends BlockEntity implements Clearable {
    private final NonNullList<ItemStack> items = NonNullList.withSize(4, ItemStack.EMPTY);

    public SkiRackBlockEntity(BlockPos pos, BlockState state) {
        super(SkiingBlockEntities.SKI_RACK_BLOCK_ENTITY.get(), pos, state);
    }

    @Override
    public void load(@NotNull CompoundTag compoundTag) {
        super.load(compoundTag);
        this.items.clear();
        ContainerHelper.loadAllItems(compoundTag, this.items);
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        ContainerHelper.saveAllItems(compoundTag, this.items);
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag compoundtag = new CompoundTag();
        ContainerHelper.saveAllItems(compoundtag, this.items, true);
        return compoundtag;
    }

    @Override
    public void setChanged() {
        super.setChanged();
        assert this.level != null;
        this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(),
                3);
    }

    public boolean addItem(ItemStack itemStackIn) {
        for (int i = 0; i < 2; ++i) {
            ItemStack itemstack = this.items.get(i);
            if (itemstack.isEmpty() && (itemStackIn.getItem() instanceof SkiItem || itemStackIn.getItem() instanceof SnowboardItem)) {
                this.items.set(i, itemStackIn.split(1));
                this.setChanged();
                return true;
            }
        }
        return false;
    }

    public ItemStack getItem(int index) {
        return this.items.get(index);
    }

    public NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    public void clearContent() {
        this.items.clear();
    }

    @Override
    public void onDataPacket(Connection connection, ClientboundBlockEntityDataPacket packet) {
        this.load(Objects.requireNonNull(packet.getTag()));
        super.onDataPacket(connection, packet);
    }
}