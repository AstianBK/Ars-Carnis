package com.TBK.ars_carnis.common.registry;

import com.TBK.ars_carnis.ArsCarnis;
import com.TBK.ars_carnis.server.entity.CarnisOvumEntity;
import com.TBK.ars_carnis.server.entity.CarnisSpinaEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ACEntityType {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, ArsCarnis.MODID);

    public static final RegistryObject<EntityType<CarnisOvumEntity>> CARNIS_OVUM =
            ENTITY_TYPES.register("carnis_ovum",
                    () -> EntityType.Builder.of(CarnisOvumEntity::new, MobCategory.MONSTER)
                            .sized(1.0f, 1.0f)
                            .build(new ResourceLocation(ArsCarnis.MODID, "carnis_ovum").toString()));

    public static final RegistryObject<EntityType<CarnisSpinaEntity>> CARNIS_SPINA =
            ENTITY_TYPES.register("carnis_spina",
                    () -> EntityType.Builder.of(CarnisSpinaEntity::new, MobCategory.MONSTER)
                            .sized(1.0f, 2.0f)
                            .build(new ResourceLocation(ArsCarnis.MODID, "carnis_spina").toString()));

}
