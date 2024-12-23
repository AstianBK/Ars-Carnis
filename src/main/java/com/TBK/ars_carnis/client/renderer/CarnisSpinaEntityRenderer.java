package com.TBK.ars_carnis.client.renderer;

import com.TBK.ars_carnis.ArsCarnis;
import com.TBK.ars_carnis.client.model.CarnisSpinaEntityModel;
import com.TBK.ars_carnis.server.entity.CarnisSpinaEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class CarnisSpinaEntityRenderer<T extends CarnisSpinaEntity,M extends CarnisSpinaEntityModel<T>> extends MobRenderer<T,M> {
    public final ResourceLocation TEXTURE = new ResourceLocation(ArsCarnis.MODID,"textures/entity/carnis_spina/carnis_spina.png");
    public CarnisSpinaEntityRenderer(EntityRendererProvider.Context p_174304_) {
        super(p_174304_, (M) new CarnisSpinaEntityModel<>(p_174304_.bakeLayer(CarnisSpinaEntityModel.LAYER_LOCATION)), 0.0F);
    }

    @Override
    public ResourceLocation getTextureLocation(T p_114482_) {
        return TEXTURE;
    }
}
