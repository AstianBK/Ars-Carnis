package com.TBK.ars_carnis.common.skill;

import com.TBK.ars_carnis.common.capability.SkillPlayerCapability;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public abstract class PassiveAbstract extends PartAbstract {
    public PassiveAbstract(String name,int cost) {
        super(name,0,0,0,0,false,false,false,false,true,cost);
    }

    @Override
    public void stopSkillAbstract(SkillPlayerCapability skill) {
        this.removeAttributeModifiers(skill.getPlayer(),skill.getPlayer().getAttributes(),skill.getPlayerVampire().age/5);
    }

    public double getAttributeModifierValue(int p_19457_, AttributeModifier p_19458_) {
        return p_19458_.getAmount() * (double)(p_19457_ + 1);
    }

    @Override
    public void updateAttributes(SkillPlayerCapability player) {
        this.addAttributeModifiers(player.getPlayer(),player.getPlayer().getAttributes(),player.getPlayerVampire().age/5);
    }
}
