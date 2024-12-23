package com.TBK.ars_carnis.common.skill;

import com.TBK.ars_carnis.common.capability.SkillPlayerCapability;
import com.TBK.ars_carnis.common.registry.ACSkillAbstract;
import com.google.common.collect.Maps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PartAbstract {
    public static PartAbstract NONE = new PartAbstract("",0,0, 0, 0, false, false, false, false,false,0);
    private final String descriptionId;
    private List<UUID> uuidTargets=new ArrayList<>();
    private List<LivingEntity> targets=new ArrayList<>();

    public int cooldown;
    public int duration;
    public int cooldownTimer=0;
    public int lauchTime;
    public int castingDuration;
    public boolean instantUse;
    public boolean isTransform;
    public boolean isPassive;
    public boolean isCasting;
    public boolean canReActive;
    public int level;
    public int costBloodBase;
    public String name;
    public CompoundTag tag;
    public Map<Attribute, AttributeModifier> attributeModifierMap= Maps.newHashMap();
    public PartAbstract(String name, int duration, int castingDuration, int cooldown, int lauchTime,
                        boolean instantUse, boolean isTransform, boolean canReActive, boolean isCasting, boolean isPassive, int costBloodBase){
        this.castingDuration=castingDuration;
        this.cooldown=cooldown;
        this.lauchTime =lauchTime;
        this.instantUse=instantUse;
        this.isTransform=isTransform;
        this.isCasting = isCasting;
        this.level=1;
        this.duration=duration;
        this.name=name;
        this.isPassive=isPassive;
        this.costBloodBase=costBloodBase;
        this.canReActive=canReActive;
        this.descriptionId= Component.translatable("part."+name).getString();
    }

    public PartAbstract(CompoundTag tag){
        PartAbstract part = ACSkillAbstract.getSkillAbstractForName(tag.getString("name"));
        this.name= part.name;
        this.cooldown= part.cooldown;
        this.castingDuration= part.castingDuration;
        this.lauchTime= part.lauchTime;
        this.instantUse= part.instantUse;
        this.isTransform= part.isTransform;
        this.isCasting = part.isCasting;
        this.descriptionId= part.descriptionId;
        this.isPassive= part.isPassive;
        this.costBloodBase= part.costBloodBase;
        this.duration= part.duration;
        this.read(tag);
    }
    public PartAbstract(FriendlyByteBuf buf){
        this.name= buf.readUtf();
        this.cooldown= buf.readInt();
        this.castingDuration= buf.readInt();
        this.lauchTime= buf.readInt();
        this.instantUse= buf.readBoolean();
        this.isTransform=buf.readBoolean();
        this.isCasting = buf.readBoolean();
        this.isPassive=buf.readBoolean();
        this.costBloodBase=buf.readInt();
        this.duration=buf.readInt();
        this.descriptionId= Component.translatable("skill."+name).getString();
    }


    public boolean isCanReActive(){
        return this.canReActive;
    }
    public void addTarget(LivingEntity target){
        this.targets.add(target);
        this.uuidTargets.add(target.getUUID());
    }
    public List<LivingEntity> getTargets(){
        return this.targets;
    }
    public void tick(SkillPlayerCapability player){
        this.effectSkillAbstractForTick(player);
    }

    public void effectSkillAbstractForTick(SkillPlayerCapability player) {
        this.updateAttributes(player);
    }

    public int getCostBloodBase() {
        return this.costBloodBase;
    }

    public void effectPassiveForTick(Player player) {

    }

    public void updateAttributes(SkillPlayerCapability player){

    }
    public boolean isCasting() {
        return this.isCasting;
    }

    public boolean isInstantUse() {
        return this.instantUse;
    }

    public boolean isTransform() {
        return this.isTransform;
    }

    public  int getCooldownForLevel(){
        return 0;
    }

    public void startSkillAbstract(SkillPlayerCapability skill) {

    }
    public void stopSkillAbstract(SkillPlayerCapability skill){
        this.getTargets().clear();
        skill.getCooldowns().addCooldown(this,this.cooldown);
    }

    public void setCooldownTimer(int cooldownTimer) {
        this.cooldownTimer = cooldownTimer;
    }
    public CompoundTag save(CompoundTag tag){
        if(this.tag==null){
            this.tag=tag;
        }
        tag.putInt("duration",this.duration);
        tag.putInt("cooldownTimer",this.cooldownTimer);
        return tag;
    }

    public void read(CompoundTag tag){
        if(tag==null){
            tag=new CompoundTag();
        }
        this.duration=tag.getInt("duration");
        this.setCooldownTimer(tag.getInt("cooldownTimer"));
        this.tag=tag;
    }

    public void addAttributeModifiers(LivingEntity p_19478_, AttributeMap p_19479_, int p_19480_) {
        for(Map.Entry<Attribute, AttributeModifier> entry : this.attributeModifierMap.entrySet()) {
            AttributeInstance attributeinstance = p_19479_.getInstance(entry.getKey());
            if (attributeinstance != null) {
                AttributeModifier attributemodifier = entry.getValue();
                attributeinstance.removeModifier(attributemodifier);
                attributeinstance.addPermanentModifier(new AttributeModifier(attributemodifier.getId(), this.descriptionId + " " + p_19480_, this.getAttributeModifierValue(p_19480_, attributemodifier), attributemodifier.getOperation()));
            }
        }

    }

    public PartAbstract addAttributeModifier(Attribute p_19473_, String p_19474_, double p_19475_, AttributeModifier.Operation p_19476_) {
        AttributeModifier attributemodifier = new AttributeModifier(UUID.fromString(p_19474_),this.name, p_19475_, p_19476_);
        this.attributeModifierMap.put(p_19473_, attributemodifier);
        return this;
    }

    public double getAttributeModifierValue(int p_19457_, AttributeModifier p_19458_) {
        return p_19458_.getAmount() * (double)(p_19457_ + 1);
    }

    public Map<Attribute, AttributeModifier> getAttributeModifiers() {
        return this.attributeModifierMap;
    }

    public void removeAttributeModifiers(LivingEntity p_19469_, AttributeMap p_19470_, int p_19471_) {
        for(Map.Entry<Attribute, AttributeModifier> entry : this.attributeModifierMap.entrySet()) {
            AttributeInstance attributeinstance = p_19470_.getInstance(entry.getKey());
            if (attributeinstance != null) {
                attributeinstance.removeModifier(entry.getValue());
            }
        }

    }

    @Override
    public boolean equals(Object obj) {
        return ((PartAbstract) obj).name.equals(this.name);
    }
}
