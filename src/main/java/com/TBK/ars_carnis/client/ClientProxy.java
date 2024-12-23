package com.TBK.ars_carnis.client;

import com.TBK.ars_carnis.common.Proxy;
import net.minecraftforge.common.MinecraftForge;


public class ClientProxy extends Proxy {

    @Override
    public void init() {
        MinecraftForge.EVENT_BUS.register(new ClientEvents());
    }
}
