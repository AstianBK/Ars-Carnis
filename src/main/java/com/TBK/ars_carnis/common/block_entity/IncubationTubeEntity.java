package com.TBK.ars_carnis.common.block_entity;

import com.TBK.ars_carnis.ArsCarnis;
import com.TBK.ars_carnis.client.gui.IncubationTubeContainerMenu;
import com.TBK.ars_carnis.common.blocks.IncubationTubeBlock;
import com.TBK.ars_carnis.common.items.EmbryoItem;
import com.TBK.ars_carnis.common.registry.ACBlockEntity;
import com.TBK.ars_carnis.common.registry.ACEntityType;
import com.TBK.ars_carnis.common.registry.ACRecipeSerializer;
import com.TBK.ars_carnis.server.data.recipe.IncubationRecipe;
import com.TBK.ars_carnis.server.entity.CarnisOvumEntity;
import com.TBK.ars_carnis.server.entity.CarnisSpinaEntity;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class IncubationTubeEntity extends BaseContainerBlockEntity implements WorldlyContainer {
    public NonNullList<ItemStack> items=NonNullList.withSize(5, ItemStack.EMPTY);
    public int litTime;
    public int litDuration;
    public int incubationProgress;
    public int incubationTotalTime;

    private final Object2IntOpenHashMap<ResourceLocation> recipesUsed = new Object2IntOpenHashMap<>();
    public final RecipeManager.CachedCheck<Container, ? extends IncubationRecipe> quickCheck;


    protected final ContainerData dataAccess = new ContainerData() {
        public int get(int p_58431_) {
            switch (p_58431_) {
                case 0:
                    return IncubationTubeEntity.this.litTime;
                case 1:
                    return IncubationTubeEntity.this.litDuration;
                case 2:
                    return IncubationTubeEntity.this.incubationProgress;
                case 3:
                    return IncubationTubeEntity.this.incubationTotalTime;
                default:
                    return 0;
            }
        }

        public void set(int p_58433_, int p_58434_) {
            switch (p_58433_) {
                case 0:
                    IncubationTubeEntity.this.litTime = p_58434_;
                    break;
                case 1:
                    IncubationTubeEntity.this.litDuration = p_58434_;
                    break;
                case 2:
                    IncubationTubeEntity.this.incubationProgress = p_58434_;
                    break;
                case 3:
                    IncubationTubeEntity.this.incubationTotalTime = p_58434_;
            }

        }

        public int getCount() {
            return 4;
        }
    };
    public IncubationTubeEntity(BlockPos p_154992_, BlockState p_154993_) {
        super(ACBlockEntity.INCUBATION_TUBE_ENTITY.get(), p_154992_, p_154993_);
        this.quickCheck = RecipeManager.createCheck((RecipeType)ACRecipeSerializer.INCUBATION_RECIPE_TYPE.get());
        //this.items= NonNullList.withSize(8, ItemStack.EMPTY);
    }
    public static void serverTicks(Level p_155014_, BlockPos p_155015_, BlockState p_155016_, IncubationTubeEntity p_155017_) {
        boolean flag = p_155017_.isLit();
        boolean flag1 = false;
        if (p_155017_.isLit()) {
            --p_155017_.litTime;
        }

        ItemStack itemstack = p_155017_.items.get(0);
        ItemStack itemstack1 = p_155017_.items.get(1);
        ItemStack itemstack2 = p_155017_.items.get(2);

        ItemStack embryo =  p_155017_.items.get(4);
        boolean flag2 = p_155017_.fullSlotAddition(p_155017_.items);
        boolean flag3 = !itemstack.isEmpty() && !itemstack1.isEmpty() && !itemstack2.isEmpty();
        if (p_155017_.isLit() || flag3 && flag2) {
            Recipe<?> recipe;
            if (flag2) {
                recipe = p_155017_.quickCheck.getRecipeFor(p_155017_, p_155014_).orElse(null);
            } else {
                recipe = null;
            }

            int i = p_155017_.getMaxStackSize();
            if (!p_155017_.isLit() && p_155017_.canBurn(p_155014_.registryAccess(), recipe, p_155017_.items, i)) {
                p_155017_.litTime = 500;
                p_155017_.litDuration = p_155017_.litTime;
                if (p_155017_.isLit()) {
                    flag1 = true;
                    if (flag3) {
                        itemstack.setCount(0);
                        itemstack1.setCount(0);
                        itemstack2.setCount(0);
                        p_155017_.incubationProgress = 0;
                        p_155017_.incubationTotalTime = getTotalCookTime(p_155014_, p_155017_);
                        p_155017_.setRecipeUsed(recipe);
                    }
                }
            }

            if (p_155017_.isLit()) {
                ++p_155017_.incubationProgress;
                ArsCarnis.LOGGER.debug("Incubation Progress :"+p_155017_.incubationProgress);
                if (p_155017_.incubationProgress == p_155017_.incubationTotalTime) {
                    if (p_155017_.burn(p_155014_.registryAccess(), recipe, p_155017_.items, i,p_155017_)) {
                        //embryo.setDamageValue(embryo.getDamageValue()-1);
                    }
                    flag1 = true;
                }
            } else {
                p_155017_.incubationProgress = 0;
            }
        } else if (!p_155017_.isLit() && p_155017_.incubationProgress > 0) {
            p_155017_.incubationProgress = Mth.clamp(p_155017_.incubationProgress - 2, 0, p_155017_.incubationTotalTime);
        }

        if (flag != p_155017_.isLit()) {
            flag1 = true;
            p_155016_ = p_155016_.setValue(IncubationTubeBlock.LIT, Boolean.valueOf(p_155017_.isLit()));
            p_155014_.setBlock(p_155015_, p_155016_, 3);
        }

        if (flag1) {
            setChanged(p_155014_, p_155015_, p_155016_);
        }
        
         
    }


    public static int getTotalCookTime(Level p_222693_, IncubationTubeEntity p_222694_) {
        return p_222694_.quickCheck.getRecipeFor(p_222694_, p_222693_).map(IncubationRecipe::getIncubationTime).orElse(500);
    }

    public void setRecipeUsed(@Nullable Recipe<?> p_58345_) {
        if (p_58345_ != null) {
            ResourceLocation resourcelocation = p_58345_.getId();
            this.recipesUsed.addTo(resourcelocation, 1);
        }

    }

    public boolean isLit() {
        return this.litTime>0;
    }
    @Override
    public int getContainerSize() {
        return 5;
    }

    public boolean isEmpty() {
        for(ItemStack itemstack : this.items) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    public ItemStack getItem(int p_58328_) {
        return this.items.get(p_58328_);
    }

    public ItemStack removeItem(int p_58330_, int p_58331_) {
        return ContainerHelper.removeItem(this.items, p_58330_, p_58331_);
    }

    public ItemStack removeItemNoUpdate(int p_58387_) {
        return ContainerHelper.takeItem(this.items, p_58387_);
    }

    public void setItem(int p_58333_, ItemStack p_58334_) {
        ItemStack itemstack = this.items.get(p_58333_);
        boolean flag = !p_58334_.isEmpty() && ItemStack.isSameItemSameTags(itemstack, p_58334_);
        this.items.set(p_58333_, p_58334_);
        if (p_58334_.getCount() > this.getMaxStackSize()) {
            p_58334_.setCount(this.getMaxStackSize());
        }

        if (p_58333_ == 0 && !flag) {
            this.incubationTotalTime = getTotalCookTime(this.level, this);
            this.incubationProgress = 0;
            this.setChanged();
        }

    }

    public boolean stillValid(Player p_58340_) {
        return Container.stillValidBlockEntity(this, p_58340_);
    }

    public boolean canPlaceItem(int p_58389_, ItemStack p_58390_) {
        if (p_58389_ == 4) {
            return true;
        } else if (p_58389_ != 0 && p_58389_ !=1 && p_58389_!=2) {
            return true;
        } else {
            ItemStack itemstack = this.items.get(p_58389_);
            return net.minecraftforge.common.ForgeHooks.getBurnTime(p_58390_,ACRecipeSerializer.INCUBATION_RECIPE_TYPE.get()) > 0 || p_58390_.is(Items.BUCKET) && !itemstack.is(Items.BUCKET);
        }
    }

    public boolean fullSlotAddition(NonNullList<ItemStack> list){
        return !list.get(4).isEmpty();
    }


    public boolean fullSlot(NonNullList<ItemStack> list){
        return fullSlotAddition(list) ;
    }
    public boolean canBurn(RegistryAccess p_266924_, @Nullable Recipe<?> p_155006_, NonNullList<ItemStack> p_155007_, int p_155008_) {
        if (fullSlot(p_155007_) && p_155006_ != null) {
            ItemStack itemstack = p_155007_.get(4);
            if (itemstack.isEmpty()) {
                return false;
            } else {
                NonNullList<Ingredient> ingredients=p_155006_.getIngredients();
                ItemStack itemstack1 = p_155007_.get(0);
                ItemStack itemstack2 = p_155007_.get(1);
                ItemStack itemstack3 = p_155007_.get(2);
                return test(ingredients.get(0),itemstack1) && test(ingredients.get(1),itemstack2) && test(ingredients.get(2),itemstack3);
            }
        } else {
            return false;
        }
    }

    public boolean test(Ingredient ingredient,ItemStack itemStack){
        if (ingredient.test(itemStack)){
            for (ItemStack stack:ingredient.getItems()){
                return stack.getCount()==itemStack.getCount();
            }
        }
        return false;
    }

    public boolean burn(RegistryAccess p_266740_, @Nullable Recipe<?> p_266780_, NonNullList<ItemStack> p_267073_, int p_267157_,IncubationTubeEntity incubationTube) {
        ItemStack embryo = p_267073_.get(4);

        if(embryo.getItem() instanceof EmbryoItem){
            if(EmbryoItem.getStage(embryo.getOrCreateTag())>1){
                if(this.level!=null && !this.level.isClientSide){
                    ArsCarnis.LOGGER.debug("Se escapo el embrion.");
                    CarnisOvumEntity carnisSpina= new CarnisOvumEntity(ACEntityType.CARNIS_OVUM.get(),this.level);
                    carnisSpina.setIdPhase(0);
                    carnisSpina.setPos(this.worldPosition.getCenter().add(-1.0D,0.0D,0.0D));
                    this.level.addFreshEntity(carnisSpina);
                    embryo.shrink(1);
                }
            }else{
                EmbryoItem.grow(embryo);
            }
        }
        return true;

    }

    protected int getBurnDuration(ItemStack p_58343_) {
        if (p_58343_.isEmpty()) {
            return 0;
        } else {
            Item item = p_58343_.getItem();
            return net.minecraftforge.common.ForgeHooks.getBurnTime(p_58343_, ACRecipeSerializer.INCUBATION_RECIPE_TYPE.get());
        }
    }

    @Override
    protected Component getDefaultName() {
        return Component.literal("Incubation");
    }

    @Override
    protected AbstractContainerMenu createMenu(int p_58627_, Inventory p_58628_) {
        return new IncubationTubeContainerMenu(p_58627_,p_58628_,this,this.dataAccess);
    }

    @Override
    public void clearContent() {

    }

    @Override
    public int[] getSlotsForFace(Direction p_19238_) {
        return new int[0];
    }

    @Override
    public boolean canPlaceItemThroughFace(int p_19235_, ItemStack p_19236_, @org.jetbrains.annotations.Nullable Direction p_19237_) {
        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int p_19239_, ItemStack p_19240_, Direction p_19241_) {
        return false;
    }

    public void load(CompoundTag p_155025_) {
        super.load(p_155025_);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(p_155025_, this.items);
        this.litTime = p_155025_.getInt("BurnTime");
        this.incubationProgress = p_155025_.getInt("incubationTime");
        this.incubationTotalTime = p_155025_.getInt("incubationTimeTotal");
        this.litDuration = this.getBurnDuration(this.items.get(1));
        CompoundTag compoundtag = p_155025_.getCompound("RecipesUsed");

        for(String s : compoundtag.getAllKeys()) {
            this.recipesUsed.put(new ResourceLocation(s), compoundtag.getInt(s));
        }

    }

    protected void saveAdditional(CompoundTag p_187452_) {
        super.saveAdditional(p_187452_);
        p_187452_.putInt("BurnTime", this.litTime);
        p_187452_.putInt("incubationTime", this.incubationProgress);
        p_187452_.putInt("incubationTimeTotal", this.incubationTotalTime);
        ContainerHelper.saveAllItems(p_187452_, this.items);
        CompoundTag compoundtag = new CompoundTag();
        this.recipesUsed.forEach((p_187449_, p_187450_) -> {
            compoundtag.putInt(p_187449_.toString(), p_187450_);
        });
        p_187452_.put("RecipesUsed", compoundtag);
    }
}
