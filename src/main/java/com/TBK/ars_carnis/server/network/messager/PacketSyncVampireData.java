package com.TBK.ars_carnis.server.network.messager;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSyncVampireData {
    private final boolean isVampire;
    private final int age;
    private final int generation;
    public PacketSyncVampireData(int age,int generation,boolean isVampire) {
        this.isVampire = isVampire;
        this.age=age;
        this.generation=generation;
    }

    public PacketSyncVampireData(FriendlyByteBuf buf) {
        this.isVampire=buf.readBoolean();
        this.age=buf.readInt();
        this.generation=buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBoolean(this.isVampire);
        buf.writeInt(this.age);
        buf.writeInt(this.generation);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(this::sync);
        return true;
    }
    @OnlyIn(Dist.CLIENT)
    public void sync(){
        Minecraft mc = Minecraft.getInstance();
        //VampirePlayerCapability cap = VampirePlayerCapability.get(mc.player);
        //assert cap!=null;
        //cap.setIsVampire(this.isVampire);
        //cap.age=this.age;
        //cap.setGeneration(this.generation);

    }
}
