package com.affehund.skiing.core.network;

import com.affehund.skiing.Skiing;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {
    private static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel SKIING_CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Skiing.MOD_ID, "main_channel"), () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

    public static void registerMessages() {
        SKIING_CHANNEL.registerMessage(1, ControlVehiclePacket.class, ControlVehiclePacket::encode, ControlVehiclePacket::new,
                ControlVehiclePacket::handle);
    }
}
