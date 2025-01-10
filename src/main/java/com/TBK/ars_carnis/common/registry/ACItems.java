package com.TBK.ars_carnis.common.registry;

import com.TBK.ars_carnis.ArsCarnis;
import com.TBK.ars_carnis.common.items.EmbryoItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ACItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ArsCarnis.MODID);

    public static final RegistryObject<Item> SPIKE_EMBRYO = ITEMS.register("spike_embryo",
            ()->new EmbryoItem(new Item.Properties().stacksTo(1).durability(1000),ACEntityType.CARNIS_SPINA));

}
