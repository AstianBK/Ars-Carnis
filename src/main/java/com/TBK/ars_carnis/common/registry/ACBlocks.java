package com.TBK.ars_carnis.common.registry;

import com.TBK.ars_carnis.ArsCarnis;
import com.TBK.ars_carnis.common.blocks.IncubationTubeBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ACBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, ArsCarnis.MODID);

    public static final RegistryObject<Block> INCUBATION_TUBE = registerBlock("incubation_tube",
            () -> new IncubationTubeBlock(BlockBehaviour.Properties.copy(Blocks.BLAST_FURNACE)));
    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block){
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends  Block> void registerBlockItem(String name, RegistryObject<T> block) {
        ACItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }
}
