package com.TBK.ars_carnis.common.mixins;

import com.TBK.ars_carnis.ArsCarnis;
import com.TBK.ars_carnis.client.model.ArmCarnisModel;
import com.TBK.ars_carnis.common.capability.SkillPlayerCapability;
import com.TBK.ars_carnis.common.skill.ArmsAbstract;
import com.TBK.ars_carnis.common.skill.ArmsSpike;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public class FirstPersonRendererMixin {
    private ArmCarnisModel<?> modelLeft;
    private ArmCarnisModel<?> modelRight;

    private final ResourceLocation TEXTURE = new ResourceLocation(ArsCarnis.MODID,"textures/part_upgrade/arms/arm_spike.png");

    @Inject(method = "renderRightHand",at = @At("TAIL"),cancellable = true)
    private void cancelRenderHandR(PoseStack p_117771_, MultiBufferSource p_117772_, int p_117773_, AbstractClientPlayer p_117774_, CallbackInfo ci){
        if(SkillPlayerCapability.get(p_117774_).getHotBarSkill().getForName("spike") instanceof ArmsSpike armsSpike && armsSpike.arm== ArmsAbstract.Arm.RIGHT){
            if( ((Object)this) instanceof LivingEntityRenderer<?,?> renderer && renderer.getModel() instanceof PlayerModel playerModel){
                int duration=SkillPlayerCapability.get(p_117774_).getActiveEffectDuration().getRemainingDurationsForSkill("spike");
                float tick=(float) duration/5.0F;
                if(modelRight==null){
                    this.modelRight=new ArmCarnisModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(ArmCarnisModel.LAYER_LOCATION));
                }
                RenderType renderType=RenderType.entityTranslucent(TEXTURE);
                p_117771_.pushPose();
                modelRight.copyOfLimbs(playerModel.leftArm);
                playerModel.setAllVisible(false);
                //p_117771_.translate(-1F,0.0F,0.0F);
                p_117771_.mulPose(Axis.XP.rotation(30.0F*tick));
                p_117771_.scale(1.1F,1.1F,1.1F);
                modelRight.renderToBuffer(p_117771_,p_117772_.getBuffer(renderType),p_117773_, OverlayTexture.NO_OVERLAY,1.0F,1.0F,1.0F,1.0F);
                p_117771_.popPose();
            }
        }
    }

    @Inject(method = "renderLeftHand",at = @At("HEAD"),cancellable = true)
    private void cancelRenderHandL(PoseStack p_117771_, MultiBufferSource p_117772_, int p_117773_, AbstractClientPlayer p_117774_, CallbackInfo ci){
        if(SkillPlayerCapability.get(p_117774_).getHotBarSkill().getForName("spike") instanceof ArmsSpike armsSpike && armsSpike.arm== ArmsAbstract.Arm.LEFT){
            if( ((Object)this) instanceof LivingEntityRenderer<?,?> renderer && renderer.getModel() instanceof PlayerModel playerModel){
                int duration = SkillPlayerCapability.get(p_117774_).getActiveEffectDuration().getRemainingDurationsForSkill("spike");
                float partialTicks=Minecraft.getInstance().getPartialTick();
                float normalizedDuration = 1.0F - ((float)duration / 5F);
                normalizedDuration += partialTicks / 1500.0F;
                normalizedDuration=Mth.clamp(normalizedDuration,0.0F,1.0F);
                float tick = easeIn(normalizedDuration);

                if(modelLeft==null){
                    this.modelLeft=new ArmCarnisModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(ArmCarnisModel.LAYER_LOCATION));
                }
                RenderType renderType = RenderType.entityTranslucent(TEXTURE);

                //model1.copyOfLimbs(playerModel.leftArm);
                if(duration>0){
                    //ArsCarnis.LOGGER.debug("Xrot :" + playerModel.leftArm.xRot + " y :"+playerModel.leftArm.y + "z :"+playerModel.leftArm.z);
                    modelLeft.leftArm.xRot =  Mth.lerp(tick,  -(float) (Math.PI/4),(float) Math.PI);
                    modelLeft.leftArm.y = Mth.lerp(tick, 10 + 3.0F, 10);
                    modelLeft.leftArm.z = Mth.lerp(tick, 5 - 10.0F, 5);
                    ArsCarnis.LOGGER.debug("Xrot :" + modelLeft.leftArm.xRot + " y :"+modelLeft.leftArm.y + "z :"+modelLeft.leftArm.z);
                }else {
                    modelLeft.leftArm.xRot = -(float) (Math.PI/4);
                    modelLeft.leftArm.y = 10;
                    modelLeft.leftArm.z = 5;

                }
                modelLeft.renderToBuffer(p_117771_, p_117772_.getBuffer(renderType), p_117773_, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
                ci.cancel();
            }
        }
    }


    private static float cubicEaseInOut(float x) {
        return x < 0.5F ? 4.0F * x * x * x : 1.0F - (float) Math.pow(-2.0F * x + 2.0F, 3) / 2.0F;
    }

    private static float quintEaseOut(float x) {
        return 1.0F - (float) Math.pow(1.0F - x, 5);
    }

    private static float smoothstep(float t) {
        return t * t * t * (t * (t * 6.0f - 15.0f) + 10.0f);
    }

    private static float easeIn(float t) {
        return t * t;
    }

    private static float lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }

    private static float easeInOut(float t) {
        if (t < 0.5f) {
            return 2 * t * t;
        } else {
            return 1 - (float) Math.pow(-2 * t + 2, 2) / 2;
        }
    }
}
