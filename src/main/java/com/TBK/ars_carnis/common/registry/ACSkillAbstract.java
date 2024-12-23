package com.TBK.ars_carnis.common.registry;

import com.TBK.ars_carnis.ArsCarnis;
import com.TBK.ars_carnis.common.skill.ArmsSpike;
import com.TBK.ars_carnis.common.skill.PartAbstract;
import com.google.common.collect.Maps;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public class ACSkillAbstract {
    public static Map<ResourceLocation, PartAbstract> POWERS= Maps.newHashMap();

    public static PartAbstract register(ResourceLocation name, PartAbstract power){
        return POWERS.put(name,power);
    }

    public static ArmsSpike SPIKE_ARM =new ArmsSpike();
    //public static BloodSlash BLOOD_SLASH=new BloodSlash();
    //public static BloodOrb BLOOD_ORB=new BloodOrb();
    //public static BatForm TRANSFORM_BAT=new BatForm();
    public static void init(){
        register(new ResourceLocation(ArsCarnis.MODID,"spike_arm"), SPIKE_ARM);
        /*register(new ResourceLocation(Sanguinaire.MODID,"blood_slash"),BLOOD_SLASH);
        register(new ResourceLocation(Sanguinaire.MODID,"blood_orb"),BLOOD_ORB);
        register(new ResourceLocation(Sanguinaire.MODID,"transform_bat"),TRANSFORM_BAT);*/
    }

    public static PartAbstract getSkillAbstractForName(String name){
        ResourceLocation resourceLocation=new ResourceLocation(ArsCarnis.MODID,name);
        return POWERS.get(resourceLocation)!=null ? POWERS.get(resourceLocation) : PartAbstract.NONE;
    }

    public static PartAbstract getSkillAbstractForLocation(ResourceLocation resourceLocation){
        return POWERS.get(resourceLocation)!=null ? POWERS.get(resourceLocation) : PartAbstract.NONE;
    }
}
