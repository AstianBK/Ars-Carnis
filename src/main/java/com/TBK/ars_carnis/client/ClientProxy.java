package com.TBK.ars_carnis.client;

import com.TBK.ars_carnis.common.Proxy;
import net.minecraftforge.common.MinecraftForge;


public class ClientProxy extends Proxy {
    public static float DIVISOR_DEL_PARTIAL_TICK=200.0f;
    public static float armAnim=0.0F;
    public static float armAnim0=0.0F;
    @Override
    public void init() {
        MinecraftForge.EVENT_BUS.register(new ClientEvents());
    }
}
