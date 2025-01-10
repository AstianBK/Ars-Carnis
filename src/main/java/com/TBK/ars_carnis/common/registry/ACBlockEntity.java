package com.TBK.ars_carnis.common.registry;

import com.TBK.ars_carnis.ArsCarnis;
import com.TBK.ars_carnis.common.block_entity.IncubationTubeEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ACBlockEntity {
    public static final DeferredRegister<BlockEntityType<?>> BLOCKS_ENTITY =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ArsCarnis.MODID);

    public static final RegistryObject<BlockEntityType<IncubationTubeEntity>> INCUBATION_TUBE_ENTITY =
            BLOCKS_ENTITY.register("incubation_tube_entity", () ->
                    BlockEntityType.Builder.of(IncubationTubeEntity::new,
                            ACBlocks.INCUBATION_TUBE.get()).build(null));
}
