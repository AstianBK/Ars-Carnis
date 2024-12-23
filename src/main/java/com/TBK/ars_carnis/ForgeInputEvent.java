package com.TBK.ars_carnis;

import com.TBK.ars_carnis.common.registry.ACKeybinds;
import com.TBK.ars_carnis.server.network.PacketHandler;
import com.TBK.ars_carnis.server.network.messager.PacketKeySync;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ArsCarnis.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ForgeInputEvent {
    @SubscribeEvent
    public static void onKeyPress(InputEvent.Key event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;
        onInput(mc, event.getKey(), event.getAction());
    }

    @SubscribeEvent
    public static void onMouseClick(InputEvent.MouseButton event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;
        onInput(mc, event.getButton(), event.getAction());
    }

    private static void onInput(Minecraft mc, int key, int action) {
        if (mc.screen == null && (ACKeybinds.attackKey2.consumeClick() ||
                ACKeybinds.attackKey3.consumeClick() || ACKeybinds.attackKey4.consumeClick()) || (key == 0x52 && action==0)) {
            PacketHandler.sendToServer(new PacketKeySync(key,action,-1));
        }else if(mc.screen==null && ACKeybinds.attackKey1.consumeClick() && mc.hitResult!=null && mc.hitResult.getType()== HitResult.Type.ENTITY){
            PacketHandler.sendToServer(new PacketKeySync(key,action,((EntityHitResult)mc.hitResult).getEntity().getId()));
        }
    }
}
