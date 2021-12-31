package com.affehund.skiing.common.item;

import com.affehund.skiing.Skiing;
import com.affehund.skiing.client.render.MultiTextureBEWLR;
import com.affehund.skiing.common.entity.AbstractMultiTextureEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.IItemRenderProperties;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class AbstractMultiTextureItem extends Item {
    private static final Predicate<Entity> ENTITY_PREDICATE = EntitySelector.NO_SPECTATORS.and(Entity::isPickable);

    public AbstractMultiTextureItem(Properties properties) {
        super(properties);
    }

    abstract EntityType<? extends AbstractMultiTextureEntity> getEntityType();

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        HitResult hitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.ANY);
        if (hitResult.getType() == HitResult.Type.MISS) {
            return InteractionResultHolder.pass(itemstack);
        } else {
            Vec3 vec3d = player.getViewVector(1.0F);
            List<Entity> list = level.getEntities(player, player.getBoundingBox().expandTowards(vec3d.scale(5.0D)).inflate(1.0D), ENTITY_PREDICATE);
            if (!list.isEmpty()) {
                Vec3 vec3d1 = player.getEyePosition(1.0F);

                for (Entity entity : list) {
                    AABB aabb = entity.getBoundingBox().inflate(entity.getPickRadius());
                    if (aabb.contains(vec3d1)) {
                        return InteractionResultHolder.pass(itemstack);
                    }
                }
            }

            if (hitResult.getType() == HitResult.Type.BLOCK) {
                var location = hitResult.getLocation();
                AbstractMultiTextureEntity multiTextureEntity = getEntityType().create(level);

                assert multiTextureEntity != null;
                multiTextureEntity.setPos(location.x(), location.y(), location.z());
                multiTextureEntity.setYRot(player.getYRot());
                multiTextureEntity.yRotO = player.yRotO;
                CompoundTag entityTag = itemstack.getTagElement("EntityTag");
                if (entityTag != null) {
                    multiTextureEntity.readAdditionalSaveData(entityTag);
                }
                if (!level.noCollision(multiTextureEntity, multiTextureEntity.getBoundingBox().inflate(-0.1D))) {
                    return InteractionResultHolder.fail(itemstack);
                } else {
                    if (!level.isClientSide) {
                        level.addFreshEntity(multiTextureEntity);
                        if (!player.getAbilities().instabuild) {
                            itemstack.shrink(1);
                        }
                    }
                    player.awardStat(Stats.ITEM_USED.get(this));
                    return InteractionResultHolder.success(itemstack);
                }
            } else {
                return InteractionResultHolder.pass(itemstack);
            }
        }
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        consumer.accept(new IItemRenderProperties() {
            @Override
            public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                return new MultiTextureBEWLR(getEntityType());
            }
        });
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @Nullable Level level, @NotNull List<Component> componentList, @NotNull TooltipFlag flag) {
        super.appendHoverText(itemStack, level, componentList, flag);
        CompoundTag entityTag = itemStack.getTagElement("EntityTag");

        String block_string = "block.minecraft.oak_planks";
        if (entityTag != null && entityTag.contains("skiing_material")) {
            Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(entityTag.getString("skiing_material")));
            if (block != null) {
                block_string = "block." + Objects.requireNonNull(block.getRegistryName()).toString().replace(":", ".");
            }
        }
        componentList.add(new TranslatableComponent("tooltip." + Skiing.MOD_ID + ".material").append(new TranslatableComponent(block_string)).withStyle(ChatFormatting.GRAY));
    }

    /*
     * /give @p skiing:ski{EntityTag:{skiing_material:"minecraft:spruce_planks"}}
     */
    @Override
    public void fillItemCategory(@NotNull CreativeModeTab creativeModeTab, @NotNull NonNullList<ItemStack> itemStacks) {
        if (allowdedIn(creativeModeTab)) {
            BlockTags.getAllTags().getTagOrEmpty(new ResourceLocation(Skiing.MOD_ID, "skiing_materials")).getValues().forEach(block -> {
                ItemStack itemStack = new ItemStack(this);
                CompoundTag compoundTag = new CompoundTag();
                compoundTag.putString("skiing_material", Objects.requireNonNull(block.getRegistryName()).toString());
                itemStack.addTagElement("EntityTag", compoundTag);
                itemStacks.add(itemStack);
            });
        }
    }
}
