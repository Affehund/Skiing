package com.affehund.skiing.common.entity;

import com.affehund.skiing.core.config.SkiingConfig;
import com.affehund.skiing.core.network.ControlVehiclePacket;
import com.affehund.skiing.core.network.PacketHandler;
import com.affehund.skiing.core.util.SkiingTags;
import net.minecraft.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.Objects;

public abstract class AbstractControllableEntity extends Entity {
    private static final EntityDataAccessor<Float> VELOCITY = SynchedEntityData.defineId(AbstractControllableEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> DAMAGE_TAKEN = SynchedEntityData.defineId(AbstractControllableEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> TIME_SINCE_HIT = SynchedEntityData.defineId(AbstractControllableEntity.class, EntityDataSerializers.INT);

    protected float deltaRotation;
    private double lerpX;
    private double lerpY;
    private double lerpZ;
    private double lerpYRot;
    private double lerpXRot;
    private int lerpSteps;

    private boolean forwardInput;
    private boolean backwardInput;
    private boolean leftInput;
    private boolean rightInput;

    public AbstractControllableEntity(EntityType entityType, Level level) {
        super(entityType, level);
        this.setDeltaMovement(Vec3.ZERO);
        this.setPos(0.0D, 0.1D, 0.0D);
    }

    protected abstract float getMaxVelocity();

    protected abstract float getMaxReverseVelocity();

    protected abstract float getAcceleration();

    protected abstract ItemStack getItemStack();

    protected abstract float getMaxHealth();

    @Override
    public void tick() {
        if (!level.isClientSide) {
            this.xo = getX();
            this.yo = getY();
            this.zo = getZ();
        }
        super.tick();
        this.tickLerp();

        if (this.getTimeSinceHit() > 0) {
            this.setTimeSinceHit(this.getTimeSinceHit() - 1);
        }

        if (this.getDamageTaken() > 0.0F) {
            this.setDamageTaken(this.getDamageTaken() - 1.0F);
        }

        this.setDeltaMovement(this.getDeltaMovement().x, this.getDeltaMovement().y - (this.isNoGravity() ? 0 : (this.getVelocity() < 0.1 ? 0.25 : -0.1 * Math.log(this.getVelocity()) + 0.03)), this.getDeltaMovement().z);
        this.controlVehicle();
        this.move(MoverType.SELF, this.getDeltaMovement());
        this.checkInsideBlocks();
        this.resetInput();

        // default max speed is 72 km/h; without going downwards ~ 43km/h
/*        if (this.getControllingPassenger() instanceof Player player) {
            player.displayClientMessage(new TextComponent(this.getVelocity() * 20 * 3.6F + " km/h" + " / " + this.getVelocity()), true);
        }*/
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        if (this.isInvulnerableTo(source) || level.isClientSide || this.isRemoved()) return false;

        this.setTimeSinceHit(20);
        this.setDamageTaken(this.getDamageTaken() + 10 * amount);
        this.markHurt();

        this.gameEvent(GameEvent.ENTITY_DAMAGED, source.getEntity());
        boolean isCreativePlayer = source.getEntity() instanceof Player && ((Player)source.getEntity()).getAbilities().instabuild;
        if (isCreativePlayer || this.getDamageTaken() > this.getMaxHealth()) {
            if (!isCreativePlayer && this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                this.dropItem();
            }
            this.discard();
        }
        return true;
    }

    protected void dropItem() {
        ItemStack itemStack = this.getItemStack();
        Objects.requireNonNull(spawnAtLocation(itemStack)).setInvulnerable(true);
    }

    protected void clampRotation(Entity entity) {
        entity.setYBodyRot(this.getYRot());
        float f = Mth.wrapDegrees(entity.getYRot() - this.getYRot());
        float f1 = Mth.clamp(f, -105.0F, 105.0F);
        entity.yRotO += f1 - f;
        entity.setYRot(entity.getYRot() + f1 - f);
        entity.setYHeadRot(entity.getYRot());
    }

    @Override
    public boolean canBeRiddenInWater(Entity rider) {
        return false;
    }

    @Override
    public boolean canCollideWith(@NotNull Entity entity) {
        return canVehicleCollide(this, entity);
    }

    private static boolean canVehicleCollide(Entity ridingEntity, Entity entity) {
        return (entity.canBeCollidedWith() || entity.isPushable()) && !ridingEntity.isPassengerOfSameVehicle(entity);
    }

    @Nonnull
    @Override
    protected Vec3 getRelativePortalPosition(@Nonnull Direction.Axis axis, @Nonnull BlockUtil.FoundRectangle result) {
        return LivingEntity.resetForwardDirectionOfRelativePortalPosition(super.getRelativePortalPosition(axis, result));
    }

    @Override
    protected float getEyeHeight(@NotNull Pose pose, EntityDimensions entityDimensions) {
        return entityDimensions.height;
    }

    @Override
    public void push(@NotNull Entity entity) {
        if (entity instanceof SkiEntity) {
            if (entity.getBoundingBox().minY < this.getBoundingBox().maxY) {
                super.push(entity);
            }
        } else if (entity.getBoundingBox().minY <= this.getBoundingBox().minY) {
            super.push(entity);
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void lerpTo(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
        this.lerpX = x;
        this.lerpY = y;
        this.lerpZ = z;
        this.lerpYRot = yaw;
        this.lerpXRot = pitch;
        this.lerpSteps = 10;
    }

    @Override
    public @NotNull InteractionResult interact(Player player, @NotNull InteractionHand hand) {
        if (player.isSecondaryUseActive() || player.isShiftKeyDown()) return InteractionResult.PASS;
        else if (!this.level.isClientSide)
            return player.startRiding(this) ? InteractionResult.CONSUME : InteractionResult.PASS;
        else {
            return InteractionResult.SUCCESS;
        }
    }

    @Override
    public @NotNull Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void defineSynchedData() {
        entityData.define(VELOCITY, 0F);
        entityData.define(DAMAGE_TAKEN, 0.0F);
        entityData.define(TIME_SINCE_HIT, 0);
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, @Nonnull BlockState state, @Nonnull BlockPos pos) {
        if (!this.isPassenger()) {
            if (onGroundIn) {
                this.fallDistance = 0.0F;
            } else if (!this.level.getFluidState(this.blockPosition().below()).is(FluidTags.WATER) && y < 0.0D) {
                this.fallDistance = (float) (this.fallDistance - y);
            }
        }
    }

    private void tickLerp() {
        if (this.isControlledByLocalInstance()) {
            this.lerpSteps = 0;
            this.setPacketCoordinates(this.getX(), this.getY(), this.getZ());
        }

        if (this.lerpSteps > 0) {
            double d0 = getX() + (lerpX - getX()) / (double) lerpSteps;
            double d1 = getY() + (lerpY - getY()) / (double) lerpSteps;
            double d2 = getZ() + (lerpZ - getZ()) / (double) lerpSteps;
            double d3 = Mth.wrapDegrees(lerpYRot - (double) getYRot());
            setYRot((float) ((double) getYRot() + d3 / (double) lerpSteps));
            setXRot((float) ((double) getXRot() + (lerpXRot - (double) getXRot()) / (double) lerpSteps));
            --lerpSteps;
            setPos(d0, d1, d2);
            setRot(getYRot(), getXRot());
        }
    }

    private void controlVehicle() {
        if (!this.isVehicle()) {
            this.resetInput();
        }

        float calculatedVelocity = this.calcVelocity();
        float maxForwardVelocity = this.getMaxVelocity() * calculatedVelocity;
        float maxBackwardVelocity = this.getMaxReverseVelocity() * calculatedVelocity;
        float velocity = this.slowDown(this.getVelocity());

        if (forwardInput && velocity <= maxForwardVelocity) velocity = Math.min((velocity <= 0 ? 0.1F : velocity) * (1 + this.getAcceleration()), maxForwardVelocity);
        if (backwardInput && velocity >= -maxBackwardVelocity) velocity = Math.max((velocity >= 0 ? -0.1F : velocity) * (1 + this.getAcceleration()), -maxBackwardVelocity);

        this.setVelocity(velocity);

        float rotationVelocity = 0;
        if (Math.abs(velocity) >= SkiingConfig.SkiingCommonConfig.MIN_REQUIRED_VELOCITY_FOR_ROTATION.get()) {
            rotationVelocity = Math.abs(1 / (2 * velocity * velocity));
            rotationVelocity = Mth.clamp(rotationVelocity, 2.0F, 5.0F);
        }

        deltaRotation = 0;

        if (velocity < 0) rotationVelocity = -rotationVelocity;
        if (leftInput) deltaRotation -= rotationVelocity;
        if (rightInput) deltaRotation += rotationVelocity;

        this.setYRot(this.getYRot() + deltaRotation);

        float delta = Math.abs(this.getYRot() - yRotO);
        while (this.getYRot() > 180F) {
            this.setYRot(this.getYRot() - 360F);
            yRotO = this.getYRot() - delta;
        }
        while (this.getYRot() <= -180F) {
            this.setYRot(this.getYRot() + 360F);
            yRotO = delta + this.getYRot();
        }

        this.setDeltaMovement(Math.sin(-this.getYRot() * ((float) Math.PI / 180F)) * this.getVelocity(), this.getDeltaMovement().y, Math.cos(this.getYRot() * ((float) Math.PI / 180F)) * this.getVelocity());
    }

    private float slowDown(float velocity) {
        float deceleration = 0.05F;
        if (velocity < 0) {
            if(velocity > -0.05) return 0;
            return (velocity * (1 - deceleration)) > 0 ? 0 : (velocity * (1 - deceleration));
        } else {
            if(velocity < 0.05) return 0;
            return (velocity * (1 - deceleration)) < 0 ? 0 : (velocity * (1 - deceleration));
        }
    }

    private float calcVelocity() {
        BlockPos blockPos = new BlockPos(getX(), getY() - 0.1D, getZ());
        BlockState blockState = this.level.getBlockState(blockPos);

        if(SkiingTags.Blocks.SNOWY_BLOCKS.contains(blockState.getBlock())) return 0.6F;
        else if(blockState.isAir()) return 1.0F;
        else return 0.1F;
    }

    private void resetInput() {
        this.forwardInput = false;
        this.backwardInput = false;
        this.leftInput = false;
        this.rightInput = false;
    }

    public void updateControls(boolean forward, boolean backward, boolean left, boolean right, Player player) {
        boolean inputChanged = false;

        if (this.forwardInput != forward) {
            this.forwardInput = forward;
            inputChanged = true;
        }

        if (this.backwardInput != backward) {
            this.backwardInput = backward;
            inputChanged = true;
        }

        if (this.leftInput != left) {
            this.leftInput = left;
            inputChanged = true;
        }

        if (this.rightInput != right) {
            this.rightInput = right;
            inputChanged = true;
        }

        if (this.level.isClientSide && inputChanged) {
            PacketHandler.SKIING_CHANNEL.sendToServer(new ControlVehiclePacket(forward, backward, left, right, player));
        }
    }

    @Override
    protected void addPassenger(@Nonnull Entity passenger) {
        super.addPassenger(passenger);
        if (this.isControlledByLocalInstance() && this.lerpSteps > 0) {
            this.lerpSteps = 0;
            this.resetInput();
        }
    }

    @Override
    protected void removePassenger(@Nonnull Entity passenger) {
        super.removePassenger(passenger);
        this.resetInput();
        this.lerpSteps = 0;
        this.deltaRotation = 0;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void onPassengerTurned(@NotNull Entity entity) {
        this.clampRotation(entity);
    }

    @Override
    public Entity getControllingPassenger() {
        return this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean displayFireAnimation() {
        return false;
    }

    @Override
    public boolean isPushable() {
        return true;
    }

    @Override
    public boolean isPickable() {
        return !isRemoved();
    }

    @Override
    public ItemStack getPickedResult(HitResult target) {
        return this.getItemStack();
    }

    public void setVelocity(float velocity) {
        this.entityData.set(VELOCITY, velocity);
    }

    public float getVelocity() {
        return this.entityData.get(VELOCITY);
    }

    public float getDamageTaken() {
        return this.entityData.get(DAMAGE_TAKEN);
    }

    protected void setDamageTaken(float damageTaken) {
        this.entityData.set(DAMAGE_TAKEN, damageTaken);
    }

    public int getTimeSinceHit() {
        return this.entityData.get(TIME_SINCE_HIT);
    }

    private void setTimeSinceHit(int timeSinceHit) {
        this.entityData.set(TIME_SINCE_HIT, timeSinceHit);
    }
}
