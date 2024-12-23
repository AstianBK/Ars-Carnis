package com.TBK.ars_carnis.client;

import com.TBK.ars_carnis.ArsCarnis;
import com.TBK.ars_carnis.client.layers.UpgradeCarnisLayer;
import com.TBK.ars_carnis.client.model.ArmCarnisModel;
import com.TBK.ars_carnis.client.model.CarnisOvumModel;
import com.TBK.ars_carnis.client.model.CarnisSpinaEntityModel;
import com.TBK.ars_carnis.common.capability.SkillPlayerCapability;
import com.TBK.ars_carnis.common.skill.ArmsAbstract;
import com.TBK.ars_carnis.common.skill.ArmsSpike;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.client.event.RenderArmEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ArsCarnis.MODID, bus = Mod.EventBusSubscriber.Bus.MOD,value = Dist.CLIENT)
public class ClientEvent {
    private static final ResourceLocation TEXTURE = new ResourceLocation(ArsCarnis.MODID,"textures/part_upgrade/arms/arm_spike.png");


    @SubscribeEvent
    public static void registerLayerDefinition(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(CarnisOvumModel.LAYER_LOCATION, CarnisOvumModel::createBodyLayer);
        event.registerLayerDefinition(ArmCarnisModel.LAYER_LOCATION, ArmCarnisModel::createBodyLayer);
        event.registerLayerDefinition(CarnisSpinaEntityModel.LAYER_LOCATION, CarnisSpinaEntityModel::createBodyLayer);
    }



    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.AddLayers event){
        event.getSkins().forEach(s -> {
            event.getSkin(s).addLayer(new UpgradeCarnisLayer(event.getSkin(s)));
            //event.getSkin(s).addLayer(new LivingProtectionLayer(event.getSkin(s)));
        });
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    @OnlyIn(Dist.CLIENT)
    public static void registerParticleFactories(RegisterParticleProvidersEvent event) {

    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    @OnlyIn(Dist.CLIENT)
    public static void registerArmorRenderers(EntityRenderersEvent.AddLayers event){

    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    @OnlyIn(Dist.CLIENT)
    public static void registerGui(RegisterGuiOverlaysEvent event){
    }
}
