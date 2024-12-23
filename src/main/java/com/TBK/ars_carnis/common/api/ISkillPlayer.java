package com.TBK.ars_carnis.common.api;

import com.TBK.ars_carnis.common.skill.PartAbstract;
import com.TBK.ars_carnis.common.skill.SkillAbstracts;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;

import java.util.Map;

public interface ISkillPlayer extends INBTSerializable<CompoundTag> {
    Player getPlayer();
    void setPlayer(Player player);
    PartAbstract getSelectSkill();
    PartAbstract getSkillForHotBar(int pos);
    int getCooldownSkill();
    int getCastingTimer();
    int getStartTime();
    boolean lastUsingSkill();
    PartAbstract getLastUsingSkill();
    void setLastUsingSkill(PartAbstract power);
    void tick(Player player);
    void onJoinGame(Player player, EntityJoinLevelEvent event);
    void handledSkill(PartAbstract power);
    public void stopSkill(PartAbstract power);
    void handledPassive(Player player, PartAbstract power);
    boolean canUseSkill(PartAbstract skillAbstract);
    Map<Integer, PartAbstract> getPassives();
    SkillAbstracts getHotBarSkill();
    void syncSkill(Player player);
    void upSkill();
    void downSkill();
    void startCasting(Player player);

}
