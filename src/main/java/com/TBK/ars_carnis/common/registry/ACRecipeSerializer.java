package com.TBK.ars_carnis.common.registry;

import com.TBK.ars_carnis.ArsCarnis;
import com.TBK.ars_carnis.server.data.recipe.IncubationRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ACRecipeSerializer {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, ArsCarnis.MODID);

    public static final RegistryObject<RecipeSerializer<IncubationRecipe>> INCUBATION_RECIPE = RECIPE_SERIALIZERS.register("incubation_recipe", IncubationRecipe.Serializer::new);

    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, ArsCarnis.MODID);

    public static final RegistryObject<RecipeType<IncubationRecipe>> INCUBATION_RECIPE_TYPE = RECIPE_TYPES.register("incubation_recipe_type",()->new RecipeType<IncubationRecipe>() {
        @Override
        public int hashCode() {
            return super.hashCode();
        }
    });
}
