package com.TBK.ars_carnis.common.api;

import com.TBK.ars_carnis.common.capability.SkillPlayerCapability;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.INBTSerializable;

public interface ICarnisPlayer extends INBTSerializable<CompoundTag> {
    boolean isCarnis();
    boolean loseBlood(int blood);
    void setIsCarnis(boolean bol);
    void setPlayer(Player player);
    Player getPlayer();
    int getGeneration();
    void setGeneration(int generation);
    void mutate(Player player, Entity target);
    SkillPlayerCapability getSkillCap(Player player);
    void tick(Player player);
}
