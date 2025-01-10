package com.TBK.ars_carnis.common.registry;

import com.TBK.ars_carnis.ArsCarnis;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ACCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ArsCarnis.MODID);

    public static final RegistryObject<CreativeModeTab> BK_MOBS_TAB = TABS.register(ArsCarnis.MODID,()-> CreativeModeTab.builder()
            .icon(()->new ItemStack(ACItems.SPIKE_EMBRYO.get()))
            .title(Component.translatable("itemGroup.ars_carnis"))
            .displayItems((s,a)-> {
                a.accept(ACItems.SPIKE_EMBRYO.get());
                a.accept(ACBlocks.INCUBATION_TUBE.get());
            })
            .build());
}
