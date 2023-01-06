package com.affehund.skiing.common.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract class AbstractMultiTextureEntity extends AbstractControllableEntity {
    private static final EntityDataAccessor<String> SKIING_MATERIAL = SynchedEntityData.defineId(AbstractMultiTextureEntity.class, EntityDataSerializers.STRING);
    private Block skiing_material;

    public AbstractMultiTextureEntity(EntityType entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(SKIING_MATERIAL, Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(Blocks.OAK_PLANKS)).toString());
    }

    @Override
    public void onSyncedDataUpdated(@NotNull EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (SKIING_MATERIAL.equals(key) && level.isClientSide()) {
            Block block = ForgeRegistries.BLOCKS.getValue((new ResourceLocation(entityData.get(SKIING_MATERIAL))));
            skiing_material = block == null ? Blocks.OAK_PLANKS : block;
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag nbt) {
        nbt.putString("skiing_material", entityData.get(SKIING_MATERIAL));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        if (nbt.contains("skiing_material")) {
            setSkiingMaterial(nbt.getString("skiing_material"));
        }
    }

    @Override
    protected ItemStack getItemStack() {
        ItemStack itemStack = getItem().getDefaultInstance();
        CompoundTag compound = new CompoundTag();
        addAdditionalSaveData(compound);
        itemStack.addTagElement("EntityTag", compound);
        return itemStack;
    }

    protected abstract Item getItem();

    public Block getSkiingMaterial() {
        return skiing_material;
    }

    public void setSkiingMaterial(String material) {
        entityData.set(SKIING_MATERIAL, material);
        Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(material));
        skiing_material = block == null ? Blocks.OAK_PLANKS : block;
    }

    public void setSkiingMaterial(Block material) {
        entityData.set(SKIING_MATERIAL, Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(material)).toString());
        skiing_material = material;
    }
}
