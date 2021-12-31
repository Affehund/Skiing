package com.affehund.skiing.common.entity;

import com.affehund.skiing.common.item.SkiStickItem;
import com.affehund.skiing.core.config.SkiingConfig;
import com.affehund.skiing.core.init.SkiingItems;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class SkiEntity extends AbstractMultiTextureEntity {
    public SkiEntity(EntityType<? extends SkiEntity> entityType, Level level) {
        this(entityType, level, Blocks.OAK_PLANKS);
    }

    public SkiEntity(EntityType<? extends SkiEntity> entityType, Level level, Block skiingMaterial) {
        super(entityType, level);
        super.setSkiingMaterial(skiingMaterial);
    }

    @Override
    public void positionRider(@NotNull Entity passenger) {
        if (!hasPassenger(passenger)) return;
        float f = -0.25F;
        float f1 = (float) ((this.isRemoved() ? 0.01F : this.getPassengersRidingOffset()) + passenger.getMyRidingOffset());
        Vec3 vec3 = (new Vec3(f, 0.0D, 0.0D)).yRot(-getYRot() * ((float) Math.PI / 180F) - ((float) Math.PI / 2F));
        passenger.setPos(getX() + vec3.x, getY() + f1, getZ() + vec3.z);
        passenger.setYRot(passenger.getYRot() + this.deltaRotation);
        passenger.setYHeadRot(passenger.getYHeadRot() + deltaRotation);
        clampRotation(passenger);
    }

    @Override
    public boolean shouldRiderSit() {
        return false;
    }

    @Override
    protected float getMaxVelocity() {
        return (float) (double) SkiingConfig.SkiingCommonConfig.MAX_SKI_VELOCITY.get();
    }

    @Override
    protected float getMaxReverseVelocity() {
        return (float) (double) SkiingConfig.SkiingCommonConfig.MAX_SKI_REVERSE_VELOCITY.get();
    }

    @Override
    protected float getAcceleration() {
        double acceleration = SkiingConfig.SkiingCommonConfig.SKI_ACCELERATION.get();
        double accelerationWithStick = SkiingConfig.SkiingCommonConfig.SKI_ACCELERATION_WITH_STICK.get();
        return this.isHoldingSkiSticks(this.getControllingPassenger()) ? (float) accelerationWithStick : (float) acceleration;
    }

    @Override
    protected float getMaxHealth() {
        return (float) (double) SkiingConfig.SkiingCommonConfig.MAX_SKI_HEALTH.get();
    }

    @Override
    protected Item getItem() {
        return SkiingItems.SKI_ITEM.get();
    }

    private boolean isHoldingSkiSticks(Entity controllingPassenger) {
        if (controllingPassenger instanceof Player player) {
            return player.getOffhandItem().getItem() instanceof SkiStickItem && player.getMainHandItem().getItem() instanceof SkiStickItem;
        }
        return false;
    }
}
