package com.TBK.ars_carnis.client.gui;

import com.TBK.ars_carnis.common.registry.ACMenuType;
import com.TBK.ars_carnis.common.registry.ACRecipeSerializer;
import com.TBK.ars_carnis.common.registry.ACTags;
import com.TBK.ars_carnis.server.data.recipe.IncubationRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class IncubationTubeContainerMenu extends RecipeBookMenu<Container> {
    private final Container container;
    private final ContainerData data;
    protected final Level level;
    protected final RecipeType<IncubationRecipe> recipeType;
    public IncubationTubeContainerMenu(int p_38963_, Inventory p_38964_, FriendlyByteBuf buf) {
        this(p_38963_,p_38964_,new SimpleContainer(5),new SimpleContainerData(4));
    }
    public IncubationTubeContainerMenu(int p_38852_, Inventory p_38970_, Container p_38971_, ContainerData p_38972_) {
        super(ACMenuType.FURNACE_MENU.get(), p_38852_);
        this.recipeType= ACRecipeSerializer.INCUBATION_RECIPE_TYPE.get();
        checkContainerSize(p_38971_, 5);
        checkContainerDataCount(p_38972_, 4);
        this.container = p_38971_;
        this.data = p_38972_;
        this.level = p_38970_.player.level();
        this.addSlot(new Slot(p_38971_, 0, 62, 79));
        this.addSlot(new Slot(p_38971_, 1, 80, 79));
        this.addSlot(new Slot(p_38971_, 2, 98, 79));

        this.addSlot(new Slot( p_38971_, 3, 30, 28));

        this.addSlot(new Slot(p_38971_, 4, 128, 28));


        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(p_38970_, j + i * 9 + 9, 8 + j * 18, 112 + i * 18));
            }
        }

        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(p_38970_, k, 8 + k * 18, 170));
        }

        this.addDataSlots(p_38972_);
    }




    @Override
    public void fillCraftSlotsStackedContents(StackedContents p_38976_) {
        if (this.container instanceof StackedContentsCompatible) {
            ((StackedContentsCompatible)this.container).fillStackedContents(p_38976_);
        }

    }

    public boolean isValidSlotIndex(int p_207776_) {
        return p_207776_ == -1 || p_207776_ == -999 || p_207776_ < this.slots.size();
    }

    public void clearCraftingContent() {
        this.getSlot(0).set(ItemStack.EMPTY);
        this.getSlot(1).set(ItemStack.EMPTY);
        this.getSlot(2).set(ItemStack.EMPTY);

        //this.getSlot(7).set(ItemStack.EMPTY);
    }

    public boolean recipeMatches(Recipe<? super Container> p_38980_) {
        return p_38980_.matches(this.container, this.level);
    }


    @Override
    public int getResultSlotIndex() {
        return 4;
    }

    @Override
    public void slotsChanged(Container p_38868_) {
        super.slotsChanged(p_38868_);
    }

    @Override
    public int getGridWidth() {
        return 1;
    }

    @Override
    public int getGridHeight() {
        return 1;
    }

    @Override
    public int getSize() {
        return 5;
    }

    @Override
    public RecipeBookType getRecipeBookType() {
        return null;
    }

    @Override
    public boolean shouldMoveToInventory(int p_150635_) {
        return p_150635_ != 3;
    }


    protected boolean canSmelt(ItemStack p_38978_) {
        return this.level.getRecipeManager().getRecipeFor(ACRecipeSerializer.INCUBATION_RECIPE_TYPE.get(), new SimpleContainer(p_38978_), this.level).isPresent();
    }

    protected boolean isFuel(ItemStack p_38989_) {
        return net.minecraftforge.common.ForgeHooks.getBurnTime(p_38989_, ACRecipeSerializer.INCUBATION_RECIPE_TYPE.get()) > 0;
    }

    public int getBurnProgress() {
        int i = this.data.get(2);
        int j = this.data.get(3);
        return j != 0 && i != 0 ? i * 51 / j : 0;
    }

    public int getLitProgress() {
        int i = this.data.get(1);
        if (i == 0) {
            i = 200;
        }

        return this.data.get(0) * 51 / i;
    }

    public boolean isLit() {
        return this.data.get(0) > 0;
    }
    @Override
    public ItemStack quickMoveStack(Player p_38986_, int p_38987_) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(p_38987_);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (p_38987_ == 4) {
                if (!this.moveItemStackTo(itemstack1, 5, 41, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(itemstack1, itemstack);
            } else if (p_38987_ != 1 && p_38987_ != 0 && p_38987_ != 2 && p_38987_ != 3) {
                if (this.canSmelt(itemstack1)) {
                    if (!this.moveItemStackTo(itemstack1, 0, 3, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.isFuel(itemstack1)) {
                    if (!this.moveItemStackTo(itemstack1, 3, 4, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                else if (p_38987_ >= 5 && p_38987_ < 32) {
                if (!this.moveItemStackTo(itemstack1, 32, 41, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (p_38987_ >= 31 && p_38987_ < 41 && !this.moveItemStackTo(itemstack1, 5, 32, false)) {
                return ItemStack.EMPTY;
            }
        } else if (!this.moveItemStackTo(itemstack1, 5, 41, false)) {
            return ItemStack.EMPTY;
        }

        if (itemstack1.isEmpty()) {
            slot.setByPlayer(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }

        if (itemstack1.getCount() == itemstack.getCount()) {
            return ItemStack.EMPTY;
        }

        slot.onTake(p_38986_, itemstack1);
        }
        return itemstack;
    }

    private boolean isTool(ItemStack itemstack1) {
        return true;
    }


    @Override
    public boolean stillValid(Player p_38974_) {
        return this.container.stillValid(p_38974_);
    }
}
