package com.TBK.ars_carnis.server;

import com.TBK.ars_carnis.ArsCarnis;
import com.TBK.ars_carnis.common.registry.ACEntityType;
import com.TBK.ars_carnis.server.entity.CarnisOvumEntity;
import com.TBK.ars_carnis.server.entity.CarnisSpinaEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = ArsCarnis.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ServerEvents {
    @SubscribeEvent
    public static void registerAttribute(EntityAttributeCreationEvent event) {
        SpawnPlacements.register(ACEntityType.CARNIS_OVUM.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Mob::checkMobSpawnRules);

        //event.put(CVNEntityType.ELDER_HARPY.get(), ElderHarpyEntity.setAttributes());
        event.put(ACEntityType.CARNIS_OVUM.get(), CarnisOvumEntity.setAttributes());
        event.put(ACEntityType.CARNIS_SPINA.get(), CarnisSpinaEntity.setAttributes());

    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        //event.enqueueWork(BKItemProperties::register);
    }
}
