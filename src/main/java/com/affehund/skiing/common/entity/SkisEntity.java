package com.affehund.skiing.common.entity;

import javax.annotation.Nonnull;

import com.affehund.skiing.common.item.SkisItem;
import com.affehund.skiing.core.data.gen.ModTags;
import com.affehund.skiing.core.init.ModEntities;
import com.affehund.skiing.core.init.ModItems;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.network.NetworkHooks;

public class SkisEntity extends Entity {

	// data
	private static final DataParameter<Integer> TIME_SINCE_HIT = EntityDataManager.createKey(SkisEntity.class,
			DataSerializers.VARINT);
	private static final DataParameter<Float> DAMAGE_TAKEN = EntityDataManager.createKey(SkisEntity.class,
			DataSerializers.FLOAT);
	private static final DataParameter<Integer> FORWARD_DIRECTION = EntityDataManager.createKey(SkisEntity.class,
			DataSerializers.VARINT);
	private static final DataParameter<Integer> STEP_COOLDOWN = EntityDataManager.createKey(SkisEntity.class,
			DataSerializers.VARINT);
	private static final DataParameter<Boolean> FLYING = EntityDataManager.createKey(SkisEntity.class,
			DataSerializers.BOOLEAN);
	private static final DataParameter<Integer> SKIS_TYPE = EntityDataManager.createKey(SkisEntity.class,
			DataSerializers.VARINT);

	// private values
	private float deltaRotation;
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
	private boolean flyDown;
	private SkisEntity.Status status;

	public SkisEntity(EntityType<SkisEntity> entityType, World world) {
		super(entityType, world);
		this.preventEntitySpawning = true;
	}

	public SkisEntity(World world, double x, double y, double z) {
		this(ModEntities.SKI_ENTITY.get(), world);
		this.setPosition(x, y, z);
		this.setMotion(Vector3d.ZERO);
		this.prevPosX = x;
		this.prevPosY = y;
		this.prevPosZ = z;
	}

	@Override
	protected void registerData() {
		this.dataManager.register(TIME_SINCE_HIT, 0);
		this.dataManager.register(DAMAGE_TAKEN, 0.0F);
		this.dataManager.register(FORWARD_DIRECTION, 1);
		this.dataManager.register(STEP_COOLDOWN, 0);
		this.dataManager.register(FLYING, false);
		this.dataManager.register(SKIS_TYPE, SkisEntity.SkisType.ACACIA.ordinal());
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

			float f = -0.25F;
			@SuppressWarnings("deprecation")
			float f1 = (float) ((this.removed ? (double) 0.01F : this.getMountedYOffset()) + passenger.getYOffset());

			Vector3d vector3d = (new Vector3d(f, 0.0D, 0.0D))
					.rotateYaw(-this.rotationYaw * ((float) Math.PI / 180F) - ((float) Math.PI / 2F));
			passenger.setPosition(this.getPosX() + vector3d.x, this.getPosY() + (double) f1,
					this.getPosZ() + vector3d.z);
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
			this.updateStep();
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
					this.world.addParticle(ParticleTypes.ITEM_SNOWBALL, this.getPosX() + (double) this.rand.nextFloat(),
							this.getPosY() + 0.5D, this.getPosZ() + (double) this.rand.nextFloat(), 0.0D, 0.0D, 0.0D);
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
			double d0 = this.getPosX() + (this.lerpX - this.getPosX()) / (double) this.lerpSteps;
			double d1 = this.getPosY() + (this.lerpY - this.getPosY()) / (double) this.lerpSteps;
			double d2 = this.getPosZ() + (this.lerpZ - this.getPosZ()) / (double) this.lerpSteps;
			double d3 = MathHelper.wrapDegrees(this.lerpYaw - (double) this.rotationYaw);
			this.rotationYaw = (float) ((double) this.rotationYaw + d3 / (double) this.lerpSteps);
			this.rotationPitch = (float) ((double) this.rotationPitch
					+ (this.lerpPitch - (double) this.rotationPitch) / (double) this.lerpSteps);
			--this.lerpSteps;
			this.setPosition(d0, d1, d2);
			this.setRotation(this.rotationYaw, this.rotationPitch);
		}
	}

	private boolean isHoldingSkiSticks(Entity ridingEntity) {
		if (ridingEntity instanceof PlayerEntity) {
			PlayerEntity playerEntity = (PlayerEntity) ridingEntity;
			if ((playerEntity.getHeldItemOffhand().getItem() == ModItems.SKI_STICK_ITEM.get()
					&& playerEntity.getHeldItemMainhand().getItem() == ModItems.SKI_STICK_ITEM.get())) {
				return true;
			}
		}
		return false;
	}

	private void updateMotion() {
		float momentum = 0.25F;
		double falling = this.hasNoGravity() ? 0 : -0.02;

		switch (this.status) {
		case IN_WATER:
			momentum = 0.2f;
			break;
		case ON_LAND:
			momentum = 0.4f;
			break;
		case ON_SNOW:
			momentum = isHoldingSkiSticks(this.getControllingPassenger()) ? 0.92F : 0.85F;
			break;
		case IN_AIR:
			momentum = this.dataManager.get(FLYING) ? 0.9F : 0.85F;
			break;
		default:
			momentum = 0.4f;
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

	private void updateStep() {
		if (this.dataManager.get(STEP_COOLDOWN) > 0) {
			if (this.onGround) {
				this.dataManager.set(STEP_COOLDOWN, 0);
			} else {
				this.dataManager.set(STEP_COOLDOWN, this.dataManager.get(STEP_COOLDOWN) - 1);
			}
			this.setMotion(this.getMotion().mul(1, 0, 1));
		} else if (this.status == Status.ON_SNOW && this.onGround) {
			Vector3d lookVec = this.getLookVec().normalize();
			Vector3d motion = this.getMotion().mul(1, 0, 1).normalize();

			if (motion.lengthSquared() > 0) {
				double angle = Math.abs(Math.acos(lookVec.dotProduct(motion)));
				if (angle < Math.toRadians(60)) {
					double mul = (1 / Math.max(Math.abs(motion.x), Math.abs(motion.z)));
					if (Double.isFinite(mul)) {
						motion = motion.mul(mul, mul, mul);
						Vector3d start = new Vector3d(this.getPosX(), this.getPosY() + 0.01, this.getPosZ());
						Vector3d end = new Vector3d(start.x + motion.x, start.y, start.z + motion.z);
						BlockRayTraceResult result = this.world.rayTraceBlocks(new RayTraceContext(start, end,
								RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this));
						if (result.getType() == RayTraceResult.Type.BLOCK) {
							BlockPos pos = result.getPos();
							BlockState state = this.world.getBlockState(pos);
							BlockState stateUp = this.world.getBlockState(pos.up());
							if ((ModTags.Blocks.SNOWY_BLOCKS.contains(state.getBlock())
									|| ModTags.Blocks.SNOWY_BLOCKS.contains(stateUp.getBlock()))
									&& stateUp.getCollisionShape(this.world, pos.up()).isEmpty()) {
								VoxelShape shape = state.getCollisionShape(this.world, pos);
								Vector3d hitStart = new Vector3d(result.getHitVec().x, pos.getY() + 1.2,
										result.getHitVec().z);
								Vector3d hitEnd = new Vector3d(result.getHitVec().x, pos.getY(), result.getHitVec().z);
								BlockRayTraceResult target = shape.rayTrace(hitStart, hitEnd, pos);
								if (target != null) {
									Vector3d targetVec = target.getHitVec();
									this.dataManager.set(STEP_COOLDOWN, 5);
									this.setPosition(this.getPosX(), Math.max(this.getPosY(), targetVec.y + 0.2),
											this.getPosZ());
								}
							}
						}
					}
				}
			}
		}
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
					this.onLivingFall(this.fallDistance, 1);
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
					((SkisItem) stack.getItem()).setSkisType(stack, this.getSkisType().name());
					this.entityDropItem(stack);
				}
				this.remove();
			}
			return true;
		} else {
			return true;
		}
	}

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

	public void setSkisType(SkisEntity.SkisType type) {
		this.dataManager.set(SKIS_TYPE, type.ordinal());
	}

	@Nonnull
	public SkisType getSkisType() {
		SkisType[] types = SkisType.values();
		int type = this.dataManager.get(SKIS_TYPE);
		if (type < 0 || type >= types.length) {
			return SkisType.ACACIA;
		} else {
			return types[type];
		}
	}

	public static enum SkisType {
		ACACIA(Items.ACACIA_PLANKS, new ResourceLocation("minecraft", "textures/block/acacia_planks.png")),
		BIRCH(Items.BIRCH_PLANKS, new ResourceLocation("minecraft", "textures/block/birch_planks.png")),
		CRIMSON(Items.CRIMSON_PLANKS, new ResourceLocation("minecraft", "textures/block/crimson_planks.png")),
		DARK_OAK(Items.DARK_OAK_PLANKS, new ResourceLocation("minecraft", "textures/block/dark_oak_planks.png")),
		JUNGLE(Items.JUNGLE_PLANKS, new ResourceLocation("minecraft", "textures/block/jungle_planks.png")),
		OAK(Items.OAK_PLANKS, new ResourceLocation("minecraft", "textures/block/oak_planks.png")),
		SPRUCE(Items.SPRUCE_PLANKS, new ResourceLocation("minecraft", "textures/block/spruce_planks.png")),
		WARPED(Items.WARPED_PLANKS, new ResourceLocation("minecraft", "textures/block/warped_planks.png"));

		public final Item item;
		public final Item material;
		public final ResourceLocation texture;

		SkisType(Item material, ResourceLocation texture) {
			this.item = ModItems.SKIS_ITEM.get();
			this.material = material;
			this.texture = texture;
		}

		public static SkisType getRandom() {
			return values()[(int) (Math.random() * values().length)];
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
		if (entityIn instanceof SkisEntity) {
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
		nbt.putString("Type", this.getSkisType().name());
	}

	@Override
	protected void readAdditional(CompoundNBT nbt) {
		if (nbt.contains("Type", Constants.NBT.TAG_STRING)) {
			this.setSkisType(SkisType.valueOf(nbt.getString("Type")));
		}
	}

	private static enum Status {
		IN_WATER, ON_SNOW, ON_LAND, IN_AIR;
	}
}