package com.TBK.ars_carnis.common.skill;

import com.TBK.ars_carnis.common.manager.SkillAbstractInstance;
import com.google.common.collect.Maps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

import java.util.Collection;
import java.util.Map;

public class SkillAbstracts {
    public Map<Integer, SkillAbstractInstance> powers= Maps.newHashMap();

    public SkillAbstracts(Map<Integer, SkillAbstractInstance> powers){
        this.powers=powers;
    }

    public SkillAbstracts(CompoundTag tag){
        if(tag.contains("skills",9)){
            ListTag listTag = tag.getList("skills",10);
            for(int i = 0 ; i<listTag.size() ; i++){
                CompoundTag tag1=listTag.getCompound(i);
                if(tag1.contains("name")){
                    int pos=tag1.getInt("pos");
                    PartAbstract power = new PartAbstract(tag1);
                    this.powers.put(pos, new SkillAbstractInstance(power,0));
                }
            }
        }
    }

    public void save(CompoundTag tag){
        ListTag listtag = new ListTag();
        for (int i=1;i<this.powers.size()+1;i++){
            if(this.powers.get(i)!=null){
                PartAbstract power=this.powers.get(i).getSkillAbstract();
                CompoundTag tag1=new CompoundTag();
                tag1.putString("name",power.name);
                tag1.putInt("pos",i);
                power.save(tag1);
                listtag.add(tag1);
            }
        }
        if(!listtag.isEmpty()){
            tag.put("skills",listtag);
        }
    }

    public PartAbstract getForName(String name){
        PartAbstract power = PartAbstract.NONE;
        for (SkillAbstractInstance powerInstance:this.getSkills()){
            PartAbstract power1=powerInstance.getSkillAbstract();
            if(power1.name.equals(name)){
                power=power1;
            }
        }
        return power;
    }

    public boolean hasSkillAbstract(String id){
        return this.getForName(id)!=null;
    }

    public void addSkillAbstracts(int pos, PartAbstract power){
        this.powers.put(pos,new SkillAbstractInstance(power,0));
    }

    public Collection<SkillAbstractInstance> getSkills() {
        return this.powers.values();
    }

    public PartAbstract get(int pos){
        return this.powers.get(pos).getSkillAbstract();
    }
}
