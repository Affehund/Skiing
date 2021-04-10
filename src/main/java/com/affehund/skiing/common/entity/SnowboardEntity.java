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
	private static final DataParameter<Float> DAMAGE_TAKEN = EntityDataManager.createKey(SnowboardEntity.class,
			DataSerializers.FLOAT);
	private static final DataParameter<Boolean> FLYING = EntityDataManager.createKey(SnowboardEntity.class,
			DataSerializers.BOOLEAN);
	private static final DataParameter<Integer> FORWARD_DIRECTION = EntityDataManager.createKey(SnowboardEntity.class,
			DataSerializers.VARINT);
	private static final DataParameter<Integer> SNOWBOARD_TYPE = EntityDataManager.createKey(SnowboardEntity.class,
			DataSerializers.VARINT);
	private static final DataParameter<Integer> TIME_SINCE_HIT = EntityDataManager.createKey(SnowboardEntity.class,
			DataSerializers.VARINT);

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
		this.stepHeight = 1.0F;
		this.preventEntitySpawning = true;
	}

	public SnowboardEntity(World world, double x, double y, double z) {
		this(ModEntities.SNOWBOARD_ENTITY.get(), world);
		this.setPosition(x, y, z);
		this.setMotion(Vector3d.ZERO);
		this.prevPosX = x;
		this.prevPosY = y;
		this.prevPosZ = z;
	}

	@Override
	protected void registerData() {
		this.dataManager.register(DAMAGE_TAKEN, 0.0F);
		this.dataManager.register(FLYING, false);
		this.dataManager.register(FORWARD_DIRECTION, 1);
		this.dataManager.register(SNOWBOARD_TYPE, SnowboardEntity.SnowboardType.ACACIA.ordinal());
		this.dataManager.register(TIME_SINCE_HIT, 0);
	}

	@Override
	public ActionResultType processInitialInteract(PlayerEntity player, Hand hand) {
		if (player.isSecondaryUseActive()) {
			return ActionResultType.PASS;
		} else {
			if (!this.world.isRemote) {
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
	public void updatePassenger(Entity passenger) {
		if (this.isPassenger(passenger) || passenger instanceof PlayerEntity) {
			passenger.setPose(Pose.STANDING);
			@SuppressWarnings("deprecation")
			float f1 = (float) ((this.removed ? (double) 0.01F : this.getMountedYOffset()) + passenger.getYOffset());

			Vector3d vector3d = (new Vector3d(0.0D, 0.0D, 0.0D))
					.rotateYaw(-this.rotationYaw * ((float) Math.PI / 180F) - ((float) Math.PI / 2F));
			passenger.setPosition(this.getPosX() + vector3d.x, this.getPosY() + f1, this.getPosZ() + vector3d.z);
			passenger.rotationYaw += this.deltaRotation;
			passenger.setRotationYawHead(passenger.getRotationYawHead() + this.deltaRotation);
			this.applyYawToEntity(passenger);
		}
	}

	@Override
	protected void addPassenger(@Nonnull Entity passenger) {
		super.addPassenger(passenger);
		if (this.canPassengerSteer() && this.lerpSteps > 0) {
			this.lerpSteps = 0;
			this.forwardInputDown = false;
			this.setPositionAndRotation(this.lerpX, this.lerpY, this.lerpZ, (float) this.lerpYaw,
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
		this.dataManager.set(FLYING, false);
	}

	@Override
	public void tick() {
		this.updateStatus();

		if (this.onGround) {
			this.dataManager.set(FLYING, false);
		}

		if (this.getTimeSinceHit() > 0) {
			this.setTimeSinceHit(this.getTimeSinceHit() - 1);
		}

		if (this.getDamageTaken() > 0.0F) {
			this.setDamageTaken(this.getDamageTaken() - 1.0F);
		}

		this.tickLerp();
		if (this.canPassengerSteer()) {
			this.controlEntity();
			this.updateMotion();
			this.move(MoverType.SELF, this.getMotion());
		} else {
			this.setMotion(Vector3d.ZERO);
		}
		this.doBlockCollisions();
	}

	private void controlEntity() {
		if (this.isBeingRidden()) {
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

			this.rotationYaw += this.deltaRotation;
			if (this.forwardInputDown) {
				f += this.boostDown && !this.dataManager.get(FLYING) && this.onGround ? 0.06f : 0.04f;
				if (status == Status.ON_SNOW)
					this.world.addParticle(ParticleTypes.ITEM_SNOWBALL, this.getPosX() + this.rand.nextFloat(),
							this.getPosY() + 0.5D, this.getPosZ() + this.rand.nextFloat(), 0.0D, 0.0D, 0.0D);
			}

			if (this.backInputDown) {
				f -= 0.005F;
			}

			if (this.flyDown && this.getMotion().mul(1, 0, 1).lengthSquared() > 0.4 * 0.4) {
				this.setMotion(this.getMotion().add(0, 0.75, 0));
				this.setMotion(this.getMotion().mul(0.8, 1, 0.8));
				this.dataManager.set(FLYING, true);
			} else {
				this.setMotion(this.getMotion().add(MathHelper.sin(-this.rotationYaw * ((float) Math.PI / 180)) * f, 0,
						MathHelper.cos(this.rotationYaw * ((float) Math.PI / 180)) * f));
			}
		}
	}

	private void tickLerp() {
		if (this.canPassengerSteer()) {
			this.lerpSteps = 0;
			this.setPacketCoordinates(this.getPosX(), this.getPosY(), this.getPosZ());
		}

		if (this.lerpSteps > 0) {
			double d0 = this.getPosX() + (this.lerpX - this.getPosX()) / this.lerpSteps;
			double d1 = this.getPosY() + (this.lerpY - this.getPosY()) / this.lerpSteps;
			double d2 = this.getPosZ() + (this.lerpZ - this.getPosZ()) / this.lerpSteps;
			double d3 = MathHelper.wrapDegrees(this.lerpYaw - this.rotationYaw);
			this.rotationYaw = (float) (this.rotationYaw + d3 / this.lerpSteps);
			this.rotationPitch = (float) (this.rotationPitch + (this.lerpPitch - this.rotationPitch) / this.lerpSteps);
			--this.lerpSteps;
			this.setPosition(d0, d1, d2);
			this.setRotation(this.rotationYaw, this.rotationPitch);
		}
	}

	private void updateMotion() {
		double momentum = 0.25D;
		double falling = this.hasNoGravity() ? 0 : -0.02;

		switch (this.status) {
		case IN_AIR:
			momentum = this.dataManager.get(FLYING) ? SkiingConfig.COMMON_CONFIG.IN_AIR_FLYING_MOMENTUM.get()
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

		Vector3d motion = this.getMotion();
		motion = motion.add(0, falling, 0);
		if (this.dataManager.get(FLYING)) {
			motion = new Vector3d(motion.x, Math.max(motion.y + falling, -0.03), motion.z);
		} else {
			motion = motion.add(0, falling, 0);
		}
		motion = motion.mul(momentum, 1, momentum);
		this.setMotion(motion);
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
		BlockState blockState = this.world.getBlockState(this.getPosition());
		if (blockState.isAir()) {
			blockState = this.world.getBlockState(this.getPositionUnderneath());
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
	protected void updateFallState(double y, boolean onGroundIn, @Nonnull BlockState state, @Nonnull BlockPos pos) {
		if (!this.isPassenger()) {
			if (onGroundIn) {
				if (this.fallDistance > 3) {
					if (this.status != SnowboardEntity.Status.ON_LAND) {
						this.fallDistance = 0.0F;
						return;
					}
					this.onLivingFall(this.fallDistance, 1.0F);
				}
				this.fallDistance = 0.0F;
			} else if (!this.world.getFluidState(this.getPosition().down()).isTagged(FluidTags.WATER) && y < 0.0D) {
				this.fallDistance = (float) (this.fallDistance - y);
			}
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (this.isInvulnerableTo(source)) {
			return false;
		} else if (!this.world.isRemote && !this.removed) {
			this.setForwardDirection(-this.getForwardDirection());
			this.setTimeSinceHit(10);
			this.setDamageTaken(this.getDamageTaken() + amount * 10.0F);
			this.markVelocityChanged();
			boolean isCreative = source.getTrueSource() instanceof PlayerEntity
					&& ((PlayerEntity) source.getTrueSource()).abilities.isCreativeMode;
			if (isCreative || this.getDamageTaken() > 40.0F) {
				if (!isCreative && this.world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
					ItemStack stack = new ItemStack(ModItems.SKIS_ITEM.get());
					((SnowboardItem) stack.getItem()).setSnowboardType(stack, this.getSnowboardType().getName());
					this.entityDropItem(stack);
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
	public void performHurtAnimation() {
		this.setForwardDirection(-this.getForwardDirection());
		this.setTimeSinceHit(10);
		this.setDamageTaken(this.getDamageTaken() * 11.0F);
	}

	public float getDamageTaken() {
		return this.dataManager.get(DAMAGE_TAKEN);
	}

	protected void setDamageTaken(float damageTaken) {
		this.dataManager.set(DAMAGE_TAKEN, damageTaken);
	}

	public int getForwardDirection() {
		return this.dataManager.get(FORWARD_DIRECTION);
	}

	private void setForwardDirection(int forward) {
		this.dataManager.set(FORWARD_DIRECTION, forward);
	}

	public int getTimeSinceHit() {
		return this.dataManager.get(TIME_SINCE_HIT);
	}

	private void setTimeSinceHit(int timeSinceHit) {
		this.dataManager.set(TIME_SINCE_HIT, timeSinceHit);
	}

	public boolean isFlying() {
		return this.dataManager.get(FLYING);
	}

	public void setFlying(boolean flying) {
		this.dataManager.set(FLYING, flying);
	}

	public void setSnowboardType(SnowboardEntity.SnowboardType type) {
		this.dataManager.set(SNOWBOARD_TYPE, type.ordinal());
	}

	@Nonnull
	public SnowboardType getSnowboardType() {
		SnowboardType[] types = SnowboardType.values();
		int type = this.dataManager.get(SNOWBOARD_TYPE);
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
	public boolean canCollide(Entity entity) {
		return checkCollisionWithEntity(this, entity);
	}

	private static boolean checkCollisionWithEntity(Entity ridingEntity, Entity entity) {
		return (entity.func_241845_aY() || entity.canBePushed()) && !ridingEntity.isRidingSameEntity(entity);
	}

	@Nonnull
	@Override
	protected Vector3d func_241839_a(@Nonnull Direction.Axis axis, @Nonnull TeleportationRepositioner.Result result) {
		return LivingEntity.func_242288_h(super.func_241839_a(axis, result));
	}

	@Override
	public void applyEntityCollision(Entity entityIn) {
		if (entityIn instanceof SnowboardEntity) {
			if (entityIn.getBoundingBox().minY < this.getBoundingBox().maxY) {
				super.applyEntityCollision(entityIn);
			}
		} else if (entityIn.getBoundingBox().minY <= this.getBoundingBox().minY) {
			super.applyEntityCollision(entityIn);
		}
	}

	@Override
	public boolean func_241845_aY() {
		return true;
	}

	@Override
	public boolean canBePushed() {
		return true;
	}

	@Override
	public double getMountedYOffset() {
		return 0.45D;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean canBeCollidedWith() {
		return !this.removed;
	}

	@Nonnull
	@Override
	public Direction getAdjustedHorizontalFacing() {
		return this.getHorizontalFacing().rotateY();
	}

	private void applyYawToEntity(Entity entityToUpdate) {
		entityToUpdate.setRenderYawOffset(this.rotationYaw);
		float f = MathHelper.wrapDegrees(entityToUpdate.rotationYaw - this.rotationYaw);
		float f1 = MathHelper.clamp(f, -105.0F, 105.0F);
		entityToUpdate.prevRotationYaw += f1 - f;
		entityToUpdate.rotationYaw += f1 - f;
		entityToUpdate.setRotationYawHead(entityToUpdate.rotationYaw);
	}

	@Override
	public boolean shouldRiderSit() {
		return false;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch,
			int posRotationIncrements, boolean teleport) {
		this.lerpX = x;
		this.lerpY = y;
		this.lerpZ = z;
		this.lerpYaw = yaw;
		this.lerpPitch = pitch;
		this.lerpSteps = 10;
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	protected void writeAdditional(CompoundNBT nbt) {
		nbt.putString("Type", this.getSnowboardType().name());
	}

	@Override
	protected void readAdditional(CompoundNBT nbt) {
		if (nbt.contains("Type", Constants.NBT.TAG_STRING)) {
			this.setSnowboardType(SnowboardType.valueOf(nbt.getString("Type")));
		}
	}

	private static enum Status {
		IN_AIR, IN_WATER, ON_LAND, ON_SNOW;
	}
}
