package com.TBK.ars_carnis.common.capability;

import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;

public class ACCapability {
    public static final Capability<CarnisPlayerCapability> VAMPIRE_CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});
    public static final Capability<SkillPlayerCapability> POWER_CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});
    //public static final Capability<AnimationPlayerCapability> ANIMATION_CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});


    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(SkillPlayerCapability.class);
        event.register(CarnisPlayerCapability.class);
        //event.register(BiterEntityCap.class);
        //event.register(AnimationPlayerCapability.class);
    }

    @SuppressWarnings("unchecked")
    public static <T extends SkillPlayerCapability> T getEntityCap(Entity entity, Class<T> type) {
        if (entity != null) {
            SkillPlayerCapability entitypatch = entity.getCapability(ACCapability.POWER_CAPABILITY).orElse(null);

            if (entitypatch != null && type.isAssignableFrom(entitypatch.getClass())) {
                return (T)entitypatch;
            }
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T extends CarnisPlayerCapability> T getEntityVam(Entity entity, Class<T> type) {
        if (entity != null) {
            CarnisPlayerCapability entitypatch = entity.getCapability(ACCapability.VAMPIRE_CAPABILITY).orElse(null);

            if (entitypatch != null && type.isAssignableFrom(entitypatch.getClass())) {
                return (T)entitypatch;
            }
        }

        return null;
    }


    /*@SuppressWarnings("unchecked")
    public static <T extends AnimationPlayerCapability> T getEntityPatch(Entity entity, Class<T> type) {
        if (entity != null) {
            AnimationPlayerCapability entitypatch = entity.getCapability(PwCapability.ANIMATION_CAPABILITY).orElse(null);

            if (entitypatch != null && type.isAssignableFrom(entitypatch.getClass())) {
                return (T)entitypatch;
            }
        }

        return null;
    }*/
}
