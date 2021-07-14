package com.affehund.skiing.common.entity;

import javax.annotation.Nonnull;

import com.affehund.skiing.common.item.SnowboardItem;
import com.affehund.skiing.core.config.SkiingConfig;
import com.affehund.skiing.core.data.gen.ModTags;
import com.affehund.skiing.core.init.ModEntities;
import com.affehund.skiing.core.init.ModItems;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.TeleportationRepositioner;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.network.NetworkHooks;

public class SnowboardEntity extends Entity {

	// data
	private static final DataParameter<Float> DAMAGE_TAKEN = EntityDataManager.defineId(SnowboardEntity.class,
			DataSerializers.FLOAT);
	private static final DataParameter<Boolean> FLYING = EntityDataManager.defineId(SnowboardEntity.class,
			DataSerializers.BOOLEAN);
	private static final DataParameter<Integer> FORWARD_DIRECTION = EntityDataManager.defineId(SnowboardEntity.class,
			DataSerializers.INT);
	private static final DataParameter<Integer> SNOWBOARD_TYPE = EntityDataManager.defineId(SnowboardEntity.class,
			DataSerializers.INT);
	private static final DataParameter<Integer> TIME_SINCE_HIT = EntityDataManager.defineId(SnowboardEntity.class,
			DataSerializers.INT);

	// private values
	private float deltaRotation;
	private boolean flyDown;
	private int lerpSteps;
	private double lerpX;
	private double lerpY;
	private double lerpZ;
	private double lerpYaw;
	private double lerpPitch;
	private boolean leftInputDown;
	private boolean rightInputDown;
	private boolean forwardInputDown;
	private boolean backInputDown;
	private boolean boostDown;
	private SnowboardEntity.Status status;

	public SnowboardEntity(EntityType<SnowboardEntity> entityType, World world) {
		super(entityType, world);
		this.maxUpStep = 1.0F;
		this.blocksBuilding = true;
	}

	public SnowboardEntity(World world, double x, double y, double z) {
		this(ModEntities.SNOWBOARD_ENTITY.get(), world);
		this.setPos(x, y, z);
		this.setDeltaMovement(Vector3d.ZERO);
		this.xo = x;
		this.yo = y;
		this.zo = z;
	}

	@Override
	protected void defineSynchedData() {
		this.entityData.define(DAMAGE_TAKEN, 0.0F);
		this.entityData.define(FLYING, false);
		this.entityData.define(FORWARD_DIRECTION, 1);
		this.entityData.define(SNOWBOARD_TYPE, SnowboardEntity.SnowboardType.ACACIA.ordinal());
		this.entityData.define(TIME_SINCE_HIT, 0);
	}

	@Override
	public ActionResultType interact(PlayerEntity player, Hand hand) {
		if (player.isSecondaryUseActive()) {
			return ActionResultType.PASS;
		} else {
			if (!this.level.isClientSide) {
				return player.startRiding(this) ? ActionResultType.CONSUME : ActionResultType.PASS;
			} else {
				return ActionResultType.SUCCESS;
			}
		}
	}

	@Override
	public Entity getControllingPassenger() {
		return this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
	}

	@Override
	public void positionRider(Entity passenger) {
		if (this.hasPassenger(passenger) || passenger instanceof PlayerEntity) {
			passenger.setPose(Pose.STANDING);
			@SuppressWarnings("deprecation")
			float f1 = (float) ((this.removed ? (double) 0.01F : this.getPassengersRidingOffset()) + passenger.getMyRidingOffset());

			Vector3d vector3d = (new Vector3d(0.0D, 0.0D, 0.0D))
					.yRot(-this.yRot * ((float) Math.PI / 180F) - ((float) Math.PI / 2F));
			passenger.setPos(this.getX() + vector3d.x, this.getY() + f1, this.getZ() + vector3d.z);
			passenger.yRot += this.deltaRotation;
			passenger.setYHeadRot(passenger.getYHeadRot() + this.deltaRotation);
			this.applyYawToEntity(passenger);
		}
	}

	@Override
	protected void addPassenger(@Nonnull Entity passenger) {
		super.addPassenger(passenger);
		if (this.isControlledByLocalInstance() && this.lerpSteps > 0) {
			this.lerpSteps = 0;
			this.forwardInputDown = false;
			this.absMoveTo(this.lerpX, this.lerpY, this.lerpZ, (float) this.lerpYaw,
					(float) this.lerpPitch);
		}
	}

	@Override
	protected void removePassenger(@Nonnull Entity passenger) {
		super.removePassenger(passenger);
		this.forwardInputDown = false;
		this.backInputDown = false;
		this.rightInputDown = false;
		this.leftInputDown = false;
		this.deltaRotation = 0;
		this.entityData.set(FLYING, false);
	}

	@Override
	public void tick() {
		this.updateStatus();

		if (this.onGround) {
			this.entityData.set(FLYING, false);
		}

		if (this.getTimeSinceHit() > 0) {
			this.setTimeSinceHit(this.getTimeSinceHit() - 1);
		}

		if (this.getDamageTaken() > 0.0F) {
			this.setDamageTaken(this.getDamageTaken() - 1.0F);
		}

		this.tickLerp();
		if (this.isControlledByLocalInstance()) {
			this.controlEntity();
			this.updateMotion();
			this.move(MoverType.SELF, this.getDeltaMovement());
		} else {
			this.setDeltaMovement(Vector3d.ZERO);
		}
		this.checkInsideBlocks();
	}

	private void controlEntity() {
		if (this.isVehicle()) {
			float f = 0;
			if (this.leftInputDown) {
				--this.deltaRotation;
			}

			if (this.rightInputDown) {
				++this.deltaRotation;
			}

			if (this.rightInputDown != this.leftInputDown && !this.forwardInputDown && !this.backInputDown) {
				f += 0.005F;
			}

			this.yRot += this.deltaRotation;
			if (this.forwardInputDown) {
				f += this.boostDown && !this.entityData.get(FLYING) && this.onGround ? 0.06f : 0.04f;
				if (status == Status.ON_SNOW)
					this.level.addParticle(ParticleTypes.ITEM_SNOWBALL, this.getX() + this.random.nextFloat(),
							this.getY() + 0.5D, this.getZ() + this.random.nextFloat(), 0.0D, 0.0D, 0.0D);
			}

			if (this.backInputDown) {
				f -= 0.005F;
			}

			if (this.flyDown && this.getDeltaMovement().multiply(1, 0, 1).lengthSqr() > 0.4 * 0.4) {
				this.setDeltaMovement(this.getDeltaMovement().add(0, 0.75, 0));
				this.setDeltaMovement(this.getDeltaMovement().multiply(0.8, 1, 0.8));
				this.entityData.set(FLYING, true);
			} else {
				this.setDeltaMovement(this.getDeltaMovement().add(MathHelper.sin(-this.yRot * ((float) Math.PI / 180)) * f, 0,
						MathHelper.cos(this.yRot * ((float) Math.PI / 180)) * f));
			}
		}
	}

	private void tickLerp() {
		if (this.isControlledByLocalInstance()) {
			this.lerpSteps = 0;
			this.setPacketCoordinates(this.getX(), this.getY(), this.getZ());
		}

		if (this.lerpSteps > 0) {
			double d0 = this.getX() + (this.lerpX - this.getX()) / this.lerpSteps;
			double d1 = this.getY() + (this.lerpY - this.getY()) / this.lerpSteps;
			double d2 = this.getZ() + (this.lerpZ - this.getZ()) / this.lerpSteps;
			double d3 = MathHelper.wrapDegrees(this.lerpYaw - this.yRot);
			this.yRot = (float) (this.yRot + d3 / this.lerpSteps);
			this.xRot = (float) (this.xRot + (this.lerpPitch - this.xRot) / this.lerpSteps);
			--this.lerpSteps;
			this.setPos(d0, d1, d2);
			this.setRot(this.yRot, this.xRot);
		}
	}

	private void updateMotion() {
		double momentum = 0.25D;
		double falling = this.isNoGravity() ? 0 : -0.02;

		switch (this.status) {
		case IN_AIR:
			momentum = this.entityData.get(FLYING) ? SkiingConfig.COMMON_CONFIG.IN_AIR_FLYING_MOMENTUM.get()
					: SkiingConfig.COMMON_CONFIG.IN_AIR_MOMENTUM.get();
			break;
		case IN_WATER:
			momentum = SkiingConfig.COMMON_CONFIG.IN_WATER_MOMENTUM.get();
			break;

		case ON_LAND:
			if (!SkiingConfig.COMMON_CONFIG.CROSS_SKIING.get()) {
				momentum = SkiingConfig.COMMON_CONFIG.ON_LAND_MOMENTUM.get();
				break;
			}
		case ON_SNOW:
			momentum = SkiingConfig.COMMON_CONFIG.ON_SNOW_MOMENTUM.get();
			break;
		default:
			momentum = SkiingConfig.COMMON_CONFIG.DEFAULT_MOMENTUM.get();
			break;
		}

		Vector3d motion = this.getDeltaMovement();
		motion = motion.add(0, falling, 0);
		if (this.entityData.get(FLYING)) {
			motion = new Vector3d(motion.x, Math.max(motion.y + falling, -0.03), motion.z);
		} else {
			motion = motion.add(0, falling, 0);
		}
		motion = motion.multiply(momentum, 1, momentum);
		this.setDeltaMovement(motion);
		this.deltaRotation *= momentum;
	}

	public void updateInputs(boolean leftInputDown, boolean rightInputDown, boolean forwardInputDown,
			boolean backInputDown, boolean boostDown, boolean flyDown) {
		this.leftInputDown = leftInputDown;
		this.rightInputDown = rightInputDown;
		this.forwardInputDown = forwardInputDown;
		this.backInputDown = backInputDown;
		this.boostDown = boostDown;
		this.flyDown = flyDown;
	}

	@SuppressWarnings("deprecation")
	private void updateStatus() {
		BlockState blockState = this.level.getBlockState(this.blockPosition());
		if (blockState.isAir()) {
			blockState = this.level.getBlockState(this.getBlockPosBelowThatAffectsMyMovement());
		}
		if (this.isInWater()) {
			this.status = Status.IN_WATER;
		} else if (blockState.isAir()) {
			this.status = Status.IN_AIR;
		} else if (ModTags.Blocks.SNOWY_BLOCKS.contains(blockState.getBlock())) {
			this.status = Status.ON_SNOW;
		} else {
			this.status = Status.ON_LAND;
		}
	}

	@Override
	protected void checkFallDamage(double y, boolean onGroundIn, @Nonnull BlockState state, @Nonnull BlockPos pos) {
		if (!this.isPassenger()) {
			if (onGroundIn) {
				if (this.fallDistance > 3) {
					if (this.status != SnowboardEntity.Status.ON_LAND) {
						this.fallDistance = 0.0F;
						return;
					}
					this.causeFallDamage(this.fallDistance, 1.0F);
				}
				this.fallDistance = 0.0F;
			} else if (!this.level.getFluidState(this.blockPosition().below()).is(FluidTags.WATER) && y < 0.0D) {
				this.fallDistance = (float) (this.fallDistance - y);
			}
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (this.isInvulnerableTo(source)) {
			return false;
		} else if (!this.level.isClientSide && !this.removed) {
			this.setForwardDirection(-this.getForwardDirection());
			this.setTimeSinceHit(10);
			this.setDamageTaken(this.getDamageTaken() + amount * 10.0F);
			this.markHurt();
			boolean isCreative = source.getEntity() instanceof PlayerEntity
					&& ((PlayerEntity) source.getEntity()).abilities.instabuild;
			if (isCreative || this.getDamageTaken() > 40.0F) {
				if (!isCreative && this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
					ItemStack stack = new ItemStack(ModItems.SNOWBOARD_ITEM.get());
					((SnowboardItem) stack.getItem()).setSnowboardType(stack, this.getSnowboardType().getName());
					this.spawnAtLocation(stack);
				}
				this.remove();
			}
			return true;
		} else {
			return true;
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateHurt() {
		this.setForwardDirection(-this.getForwardDirection());
		this.setTimeSinceHit(10);
		this.setDamageTaken(this.getDamageTaken() * 11.0F);
	}

	public float getDamageTaken() {
		return this.entityData.get(DAMAGE_TAKEN);
	}

	protected void setDamageTaken(float damageTaken) {
		this.entityData.set(DAMAGE_TAKEN, damageTaken);
	}

	public int getForwardDirection() {
		return this.entityData.get(FORWARD_DIRECTION);
	}

	private void setForwardDirection(int forward) {
		this.entityData.set(FORWARD_DIRECTION, forward);
	}

	public int getTimeSinceHit() {
		return this.entityData.get(TIME_SINCE_HIT);
	}

	private void setTimeSinceHit(int timeSinceHit) {
		this.entityData.set(TIME_SINCE_HIT, timeSinceHit);
	}

	public boolean isFlying() {
		return this.entityData.get(FLYING);
	}

	public void setFlying(boolean flying) {
		this.entityData.set(FLYING, flying);
	}

	public void setSnowboardType(SnowboardEntity.SnowboardType type) {
		this.entityData.set(SNOWBOARD_TYPE, type.ordinal());
	}

	@Nonnull
	public SnowboardType getSnowboardType() {
		SnowboardType[] types = SnowboardType.values();
		int type = this.entityData.get(SNOWBOARD_TYPE);
		if (type < 0 || type >= types.length) {
			return SnowboardType.ACACIA;
		} else {
			return types[type];
		}
	}

	public static enum SnowboardType {
		ACACIA("acacia", Blocks.ACACIA_PLANKS, Blocks.ACACIA_SLAB,
				new ResourceLocation("textures/block/acacia_planks.png")),
		BIRCH("birch", Blocks.BIRCH_PLANKS, Blocks.BIRCH_SLAB, new ResourceLocation("textures/block/birch_planks.png")),
		CRIMSON("crimson", Blocks.CRIMSON_PLANKS, Blocks.CRIMSON_SLAB,
				new ResourceLocation("textures/block/crimson_planks.png")),
		DARK_OAK("dark_oak", Blocks.DARK_OAK_PLANKS, Blocks.DARK_OAK_SLAB,
				new ResourceLocation("textures/block/dark_oak_planks.png")),
		JUNGLE("jungle", Blocks.JUNGLE_PLANKS, Blocks.JUNGLE_SLAB,
				new ResourceLocation("textures/block/jungle_planks.png")),
		OAK("oak", Blocks.OAK_PLANKS, Blocks.OAK_SLAB, new ResourceLocation("textures/block/oak_planks.png")),
		SPRUCE("spruce", Blocks.SPRUCE_PLANKS, Blocks.SPRUCE_SLAB,
				new ResourceLocation("textures/block/spruce_planks.png")),
		WARPED("warped", Blocks.WARPED_PLANKS, Blocks.WARPED_SLAB,
				new ResourceLocation("textures/block/warped_planks.png"));

		private final Item item;
		private final String name;
		private final Block plank;
		private final Block slab;
		private final ResourceLocation texture;

		SnowboardType(String name, Block plank, Block slab, ResourceLocation texture) {
			this.item = ModItems.SNOWBOARD_ITEM.get();
			this.name = name;
			this.plank = plank;
			this.slab = slab;
			this.texture = texture;
		}

		public Item getItem() {
			return this.item;
		}

		public String getName() {
			return this.name;
		}

		@Override
		public String toString() {
			return this.name;
		}

		public Block getPlank() {
			return this.plank;
		}

		public Block getSlab() {
			return this.slab;
		}

		public ResourceLocation getTexture() {
			return this.texture;
		}

		public static SnowboardType getRandom() {
			return values()[(int) (Math.random() * values().length)];
		}

		public static SnowboardType getByName(String name) {
			return SnowboardType.valueOf(name.toUpperCase());
		}
	}

	@Override
	public boolean canCollideWith(Entity entity) {
		return checkCollisionWithEntity(this, entity);
	}

	private static boolean checkCollisionWithEntity(Entity ridingEntity, Entity entity) {
		return (entity.canBeCollidedWith() || entity.isPushable()) && !ridingEntity.isPassengerOfSameVehicle(entity);
	}

	@Nonnull
	@Override
	protected Vector3d getRelativePortalPosition(@Nonnull Direction.Axis axis, @Nonnull TeleportationRepositioner.Result result) {
		return LivingEntity.resetForwardDirectionOfRelativePortalPosition(super.getRelativePortalPosition(axis, result));
	}

	@Override
	public void push(Entity entityIn) {
		if (entityIn instanceof SnowboardEntity) {
			if (entityIn.getBoundingBox().minY < this.getBoundingBox().maxY) {
				super.push(entityIn);
			}
		} else if (entityIn.getBoundingBox().minY <= this.getBoundingBox().minY) {
			super.push(entityIn);
		}
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	@Override
	public boolean isPushable() {
		return true;
	}

	@Override
	public double getPassengersRidingOffset() {
		return 0.45D;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isPickable() {
		return !this.removed;
	}

	@Nonnull
	@Override
	public Direction getMotionDirection() {
		return this.getDirection().getClockWise();
	}

	private void applyYawToEntity(Entity entityToUpdate) {
		entityToUpdate.setYBodyRot(this.yRot);
		float f = MathHelper.wrapDegrees(entityToUpdate.yRot - this.yRot);
		float f1 = MathHelper.clamp(f, -105.0F, 105.0F);
		entityToUpdate.yRotO += f1 - f;
		entityToUpdate.yRot += f1 - f;
		entityToUpdate.setYHeadRot(entityToUpdate.yRot);
	}

	@Override
	public boolean shouldRiderSit() {
		return false;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void lerpTo(double x, double y, double z, float yaw, float pitch,
			int posRotationIncrements, boolean teleport) {
		this.lerpX = x;
		this.lerpY = y;
		this.lerpZ = z;
		this.lerpYaw = yaw;
		this.lerpPitch = pitch;
		this.lerpSteps = 10;
	}

	@Override
	public IPacket<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	protected void addAdditionalSaveData(CompoundNBT nbt) {
		nbt.putString("Type", this.getSnowboardType().name());
	}

	@Override
	protected void readAdditionalSaveData(CompoundNBT nbt) {
		if (nbt.contains("Type", Constants.NBT.TAG_STRING)) {
			this.setSnowboardType(SnowboardType.valueOf(nbt.getString("Type")));
		}
	}

	private static enum Status {
		IN_AIR, IN_WATER, ON_LAND, ON_SNOW;
	}
}
