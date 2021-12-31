package com.affehund.skiing.client;

import com.affehund.skiing.Skiing;
import com.affehund.skiing.client.model.SkiModel;
import com.affehund.skiing.client.model.SledModel;
import com.affehund.skiing.client.model.SnowboardModel;
import com.affehund.skiing.client.render.MultiTextureEntityRenderer;
import com.affehund.skiing.client.render.SkiRackBlockEntityRenderer;
import com.affehund.skiing.common.item.PulloverItem;
import com.affehund.skiing.core.init.SkiingBlockEntities;
import com.affehund.skiing.core.init.SkiingEntities;
import com.affehund.skiing.core.init.SkiingItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD, modid = Skiing.MOD_ID)
public class ClientEventHandler {
    public static final ModelLayerLocation SKI_LAYER = new ModelLayerLocation(new ResourceLocation(Skiing.MOD_ID, "ski_model"), "main");
    public static final ModelLayerLocation SLED_LAYER = new ModelLayerLocation(new ResourceLocation(Skiing.MOD_ID, "sled_model"), "main");
    public static final ModelLayerLocation SNOWBOARD_LAYER = new ModelLayerLocation(new ResourceLocation(Skiing.MOD_ID, "snowboard_model"), "main");

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(SKI_LAYER, SkiModel::createBodyLayer);
        event.registerLayerDefinition(SLED_LAYER, SledModel::createBodyLayer);
        event.registerLayerDefinition(SNOWBOARD_LAYER, SnowboardModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        EntityModelSet entityModelSet = Minecraft.getInstance().getEntityModels();
        event.registerEntityRenderer(SkiingEntities.SKI_ENTITY.get(), context -> new MultiTextureEntityRenderer(context, new SkiModel<>(entityModelSet.bakeLayer(ClientEventHandler.SKI_LAYER))));
        event.registerEntityRenderer(SkiingEntities.SLED_ENTITY.get(), context -> new MultiTextureEntityRenderer(context, new SledModel<>(entityModelSet.bakeLayer(ClientEventHandler.SLED_LAYER))));
        event.registerEntityRenderer(SkiingEntities.SNOWBOARD_ENTITY.get(), context -> new MultiTextureEntityRenderer(context, new SnowboardModel<>(entityModelSet.bakeLayer(ClientEventHandler.SNOWBOARD_LAYER))));
        event.registerBlockEntityRenderer(SkiingBlockEntities.SKI_RACK_BLOCK_ENTITY.get(), context -> new SkiRackBlockEntityRenderer());
    }

    @SubscribeEvent
    public static void registerItemColors(ColorHandlerEvent.Item event) {
        event.getItemColors().register((stack, layer) -> layer != 0 ? -1 : ((PulloverItem) stack.getItem())
                .getColor(stack), SkiingItems.PULLOVER.get());
    }
}
