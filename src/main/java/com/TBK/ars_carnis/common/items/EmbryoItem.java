package com.TBK.ars_carnis.common.items;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public class EmbryoItem extends Item {
    public final Supplier<? extends EntityType<? extends Mob>> type;
    public EmbryoItem(Properties p_41383_, Supplier<? extends EntityType<? extends Mob>> type) {
        super(p_41383_);
        this.type=type;
    }

    public static int getStage(CompoundTag tag){
        return tag.contains("stage") ? tag.getInt("stage") : 0;
    }

    public static void setStage(CompoundTag tag,int stage){
        tag.putInt("stage",stage);
    }

    public static void grow(ItemStack stack){
        EmbryoItem.setStage(stack.getOrCreateTag(),EmbryoItem.getStage(stack.getOrCreateTag())+1);
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        p_41423_.add(Component.translatable("item.embryo.stage"+getStage(p_41421_.getOrCreateTag())));
        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
    }
}
