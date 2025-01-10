package com.TBK.ars_carnis.common.registry;

import com.TBK.ars_carnis.ArsCarnis;
import com.TBK.ars_carnis.common.items.EmbryoItem;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;

public class ACItemProperties {
    public static void register() {
        ItemProperties.register(ACItems.SPIKE_EMBRYO.get(), new ResourceLocation(ArsCarnis.MODID, "stage"), (p_239425_0_, p_239425_1_, p_239425_2_, intIn) -> {
            return p_239425_2_ != null  ?  EmbryoItem.getStage(p_239425_0_.getOrCreateTag()) : 0.0F;
        });
    }
}
