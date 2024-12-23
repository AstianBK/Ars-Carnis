package com.TBK.ars_carnis.client.layers;

import com.TBK.ars_carnis.ArsCarnis;
import com.TBK.ars_carnis.client.ClientProxy;
import com.TBK.ars_carnis.client.model.ArmCarnisModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class UpgradeCarnisLayer<T extends LivingEntity,M extends HumanoidModel<T>> extends RenderLayer<T,M> {
    private final ResourceLocation TEXTURE = new ResourceLocation(ArsCarnis.MODID,"textures/part_upgrade/arms/arm_spike.png");

    private final ArmCarnisModel<?> model;
    public UpgradeCarnisLayer(RenderLayerParent<T, M> p_117346_) {
        super(p_117346_);
        this.model=new ArmCarnisModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(ArmCarnisModel.LAYER_LOCATION));
    }

    @Override
    public void render(PoseStack p_117349_, MultiBufferSource p_117350_, int p_117351_, T p_117352_, float p_117353_, float p_117354_, float p_117355_, float p_117356_, float p_117357_, float p_117358_) {
        p_117349_.pushPose();
        this.model.copyOfLimbs(this.getParentModel().leftArm);
        p_117349_.scale(1.01F,1.01F,1.01F);
        this.model.renderToBuffer(p_117349_,p_117350_.getBuffer(RenderType.entityTranslucent(TEXTURE)),p_117351_, OverlayTexture.NO_OVERLAY,1.0F,1.0F,1.0F,1.0F);
        p_117349_.popPose();
    }
}
