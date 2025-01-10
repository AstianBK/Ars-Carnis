package com.TBK.ars_carnis.server.data.recipe;

import com.TBK.ars_carnis.common.registry.ACRecipeSerializer;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public class IncubationRecipe implements Recipe<Container> {
    protected final RecipeType<?> type;
    protected final ResourceLocation id;
    
    protected final float experience;
    protected final int cookingTime;
    public final ItemStack addition1;
    public final ItemStack addition2;
    protected final ItemStack addition3;

    public IncubationRecipe(ResourceLocation p_249379_, ItemStack addition1, ItemStack addition2, ItemStack addition3, float p_252165_, int p_250256_) {
        this.type=ACRecipeSerializer.INCUBATION_RECIPE_TYPE.get();
        this.id=p_249379_;
        this.addition1=addition1;
        this.addition2=addition2;
        this.addition3=addition3;
        this.cookingTime=p_250256_;
        this.experience=p_252165_;
    }

    @Override
    public boolean matches(Container p_44002_, Level p_44003_) {
        return true;
    }

    @Override
    public ItemStack assemble(Container p_44001_, RegistryAccess p_267165_) {
        return null;
    }

    @Override
    public boolean canCraftInDimensions(int p_43999_, int p_44000_) {
        return true;
    }

    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> nonnulllist = NonNullList.create();
        nonnulllist.add(Ingredient.of(this.addition1));
        nonnulllist.add(Ingredient.of(this.addition2));
        nonnulllist.add(Ingredient.of(this.addition3));
        return nonnulllist;
    }
    @Override
    public ItemStack getResultItem(RegistryAccess p_267052_) {
        return ItemStack.EMPTY;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ACRecipeSerializer.INCUBATION_RECIPE.get();
    }

    @Override
    public RecipeType<?> getType() {
        return this.type;
    }

    public int getIncubationTime(){
        return this.cookingTime;
    }

    public static class Serializer implements RecipeSerializer<IncubationRecipe> {
        public IncubationRecipe fromJson(ResourceLocation p_44562_, JsonObject p_44563_) {
            ItemStack nutrition1 = getItemForJson(p_44563_, "nutrition1");
            ItemStack nutrition2 = getItemForJson(p_44563_, "nutrition2");
            ItemStack nutrition3 = getItemForJson(p_44563_,"nutrition3");
            float f = GsonHelper.getAsFloat(p_44563_, "experience", 0.0F);
            int i = GsonHelper.getAsInt(p_44563_, "incubation_time", 200);

            return new IncubationRecipe(p_44562_, nutrition1, nutrition2, nutrition3,f,i);
        }

        private ItemStack getItemForJson(JsonObject p_44563_,String name) {
            if (!p_44563_.has(name)) throw new com.google.gson.JsonSyntaxException("Missing result, expected to find a string or object");
            ItemStack itemstack;
            if (p_44563_.get(name).isJsonObject()) itemstack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(p_44563_, name));
            else {
                String s1 = GsonHelper.getAsString(p_44563_, name);
                ResourceLocation resourcelocation = new ResourceLocation(s1);
                itemstack = new ItemStack(BuiltInRegistries.ITEM.getOptional(resourcelocation).orElseThrow(() -> {
                    return new IllegalStateException("Item: " + s1 + " does not exist");
                }));
            }
            return itemstack;
        }

        public IncubationRecipe fromNetwork(ResourceLocation p_44565_, FriendlyByteBuf p_44566_) {
            ItemStack ingredient1 = p_44566_.readItem();
            ItemStack ingredient2 = p_44566_.readItem();
            ItemStack ingredient3 = p_44566_.readItem();
            int incubationTime = p_44566_.readInt();
            float exp = p_44566_.readFloat();
            return new IncubationRecipe(p_44565_, ingredient1, ingredient2,ingredient3,exp, incubationTime);
        }

        public void toNetwork(FriendlyByteBuf p_44553_, IncubationRecipe p_44554_) {
            p_44553_.writeItem(p_44554_.addition1);
            p_44553_.writeItem(p_44554_.addition2);
            p_44553_.writeItem(p_44554_.addition3);

            p_44553_.writeInt(p_44554_.cookingTime);
            p_44553_.writeFloat(p_44554_.experience);
        }
    }
}
