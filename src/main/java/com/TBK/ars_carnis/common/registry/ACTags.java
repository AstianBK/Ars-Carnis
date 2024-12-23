package com.TBK.ars_carnis.common.registry;

import com.TBK.ars_carnis.ArsCarnis;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ACTags {
    public static final TagKey<Item> IS_MEAT = tag("is_meat");
    //public static final TagKey<Item> IS_FUEL_FOR_SMITHING_FURNACE = tag("is_fuel_for_smithing_furnace");



    private static TagKey<Item> tag(String name) {
        return TagKey.create(Registries.ITEM, new ResourceLocation(ArsCarnis.MODID, name));
    }
}
