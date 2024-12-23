package com.TBK.ars_carnis.server.network.messager;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSyncBlood {
    private final double blood;
    public PacketSyncBlood(FriendlyByteBuf buf) {
        this.blood =buf.readDouble();
    }

    public PacketSyncBlood(double blood) {
        this.blood = blood;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeDouble(this.blood);
    }
    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(()->{
            ServerPlayer player=context.get().getSender();
            assert player!=null;
            sync(player);
        });
        context.get().setPacketHandled(true);
    }
    public void sync(ServerPlayer player){
        //player.getAttribute(SGAttribute.BLOOD).setBaseValue(this.blood);
    }
}
