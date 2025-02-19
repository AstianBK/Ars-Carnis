package com.TBK.ars_carnis.common.skill;

import com.TBK.ars_carnis.ArsCarnis;
import com.TBK.ars_carnis.client.ClientProxy;
import com.TBK.ars_carnis.common.capability.SkillPlayerCapability;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.LivingEntity;

public class ArmsSpike extends ArmsAbstract{
    public AnimationState attack=new AnimationState();

    public ArmsSpike() {
        super("spike", 15, 1, 10, 1, true, false);
    }


    @Override
    public Arm getArm() {
        return this.arm;
    }

    @Override
    public ArmsSpike setArm(Arm arm){
        this.arm=arm;
        this.name="spike_"+arm.name().toLowerCase();
        return this;
    }


    @Override
    public void tick(SkillPlayerCapability player) {
        super.tick(player);
    }


    public float getAnimProgress(float partialTick){
        return Mth.lerp(partialTick,ClientProxy.armAnim0,ClientProxy.armAnim);
    }

    @Override
    public void stopSkillAbstract(SkillPlayerCapability skill) {
        super.stopSkillAbstract(skill);
        this.attack.stop();
        ArsCarnis.LOGGER.debug("El poder Paro");
    }

    @Override
    public void startSkillAbstract(SkillPlayerCapability skill) {
        ArsCarnis.LOGGER.debug("El poder fue usado");
        attack.start(skill.getPlayer().tickCount);
        skill.getPlayer().level().getEntitiesOfClass(LivingEntity.class,skill.getPlayer().getBoundingBox().inflate(30.0D),e->e!=skill.getPlayer()).forEach(e->e.hurt(e.damageSources().playerAttack(skill.getPlayer()),5.0F));
        super.startSkillAbstract(skill);
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        CompoundTag tag1=super.save(tag);
        tag1.putString("arm",this.arm.name());
        return tag1;
    }

    @Override
    public void read(CompoundTag tag) {
        super.read(tag);
        this.arm=Arm.valueOf(tag.getString("arm"));
    }
}
