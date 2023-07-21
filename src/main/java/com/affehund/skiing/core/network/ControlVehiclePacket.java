package com.affehund.skiing.core.network;

import com.affehund.skiing.common.entity.AbstractControllableEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.network.NetworkEvent;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;

public class ControlVehiclePacket {
    private final boolean forward, backward, left, right;
    private final UUID uuid;

    public ControlVehiclePacket(FriendlyByteBuf buf) {
        this.forward = buf.readBoolean();
        this.backward = buf.readBoolean();
        this.left = buf.readBoolean();
        this.right = buf.readBoolean();
        this.uuid = buf.readUUID();
    }

    public ControlVehiclePacket(boolean forward, boolean backward, boolean left, boolean right, Player player) {
        this.forward = forward;
        this.backward = backward;
        this.left = left;
        this.right = right;
        this.uuid = player.getUUID();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(forward);
        buf.writeBoolean(backward);
        buf.writeBoolean(left);
        buf.writeBoolean(right);
        buf.writeUUID(uuid);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        if (Dist.CLIENT.isClient()) {
            context.get().enqueueWork(() -> handleUpdateControls(context));
            context.get().setPacketHandled(true);
        }
    }

    public void handleUpdateControls(Supplier<NetworkEvent.Context> context) {
        if (Objects.requireNonNull(context.get().getSender()).getVehicle() instanceof AbstractControllableEntity entity) {
            entity.updateControls(forward, backward, left, right, context.get().getSender());
        }
    }
}
