package com.TBK.ars_carnis.client.renderer;

import com.TBK.ars_carnis.ArsCarnis;
import com.TBK.ars_carnis.client.model.CarnisOvumModel;
import com.TBK.ars_carnis.server.entity.CarnisOvumEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class CarnisOvumEntityRenderer<T extends CarnisOvumEntity,M extends CarnisOvumModel<T>> extends MobRenderer<T,M> {
    public final ResourceLocation TEXTURE = new ResourceLocation(ArsCarnis.MODID,"textures/entity/carnis_ovum_spina.png");
    public CarnisOvumEntityRenderer(EntityRendererProvider.Context p_174304_) {
        super(p_174304_, (M) new CarnisOvumModel<>(p_174304_.bakeLayer(CarnisOvumModel.LAYER_LOCATION)), 0.0F);
        this.addLayer(new ItemInHandLayer<>(this,p_174304_.getItemInHandRenderer()));
    }

    @Override
    protected void scale(T p_115983_, PoseStack p_115984_, float p_115316_) {
        float f = 0.999F;
        p_115984_.scale(0.999F, 0.999F, 0.999F);
        p_115984_.translate(0.0F, 0.001F, 0.0F);
        float f1 = (float)p_115983_.getSize();
        float f2 = Mth.lerp(p_115316_, p_115983_.oSquish, p_115983_.squish) / (f1 * 0.5F + 1.0F);
        float f3 = 1.0F / (f2 + 1.0F);
        p_115984_.scale(f3 * f1, 1.0F / f3 * f1, f3 * f1);

    }

    @Override
    public ResourceLocation getTextureLocation(T p_114482_) {
        return TEXTURE;
    }
}
