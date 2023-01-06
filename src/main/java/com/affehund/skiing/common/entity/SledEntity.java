package com.affehund.skiing.common.entity;

import com.affehund.skiing.core.config.SkiingConfig;
import com.affehund.skiing.core.registry.SkiingItems;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class SledEntity extends AbstractMultiTextureEntity {
    public SledEntity(EntityType<? extends SledEntity> entityType, Level level) {
        this(entityType, level, Blocks.OAK_PLANKS);
    }

    public SledEntity(EntityType<? extends SledEntity> entityType, Level level, Block skiingMaterial) {
        super(entityType, level);
        super.setSkiingMaterial(skiingMaterial);
        this.maxUpStep = (float) (double) SkiingConfig.SkiingCommonConfig.MAX_SLED_UP_STEP.get();
    }

    @Override
    public void tick() {
        super.tick();
        BlockState blockState = this.level.getBlockState(this.blockPosition());
        if (!blockState.isAir() && blockState.getBlock() instanceof SnowLayerBlock && blockState.getValue(SnowLayerBlock.LAYERS) > 0) {
            this.setPos(this.getX(), this.getY() + 0.1F, this.getZ());
        }
    }

    @Override
    public void positionRider(@NotNull Entity passenger) {
        if (!this.hasPassenger(passenger)) return;
        float f = -0.5F;
        float f1 = (float) ((this.isRemoved() ? 0.01F : this.getPassengersRidingOffset()) + passenger.getMyRidingOffset() - 0.125F);
        Vec3 vec3 = (new Vec3(f, 0.0D, 0.0D)).yRot(-this.getYRot() * ((float) Math.PI / 180F) - ((float) Math.PI / 2F));
        passenger.setPos(this.getX() + vec3.x, this.getY() + f1, this.getZ() + vec3.z);
        passenger.setYRot(passenger.getYRot() + this.deltaRotation);
        passenger.setYHeadRot(passenger.getYHeadRot() + deltaRotation);
        this.clampRotation(passenger);
    }

    @Override
    protected float getMaxVelocity() {
        return (float) (double) SkiingConfig.SkiingCommonConfig.MAX_SLED_VELOCITY.get();
    }

    @Override
    protected float getMaxReverseVelocity() {
        return (float) (double) SkiingConfig.SkiingCommonConfig.MAX_SLED_REVERSE_VELOCITY.get();
    }

    @Override
    protected float getAcceleration() {
        return (float) (double) SkiingConfig.SkiingCommonConfig.SLED_ACCELERATION.get();
    }

    @Override
    protected float getMaxHealth() {
        return (float) (double) SkiingConfig.SkiingCommonConfig.MAX_SLED_HEALTH.get();
    }

    @Override
    protected Item getItem() {
        return SkiingItems.SLED_ITEM.get();
    }
}
