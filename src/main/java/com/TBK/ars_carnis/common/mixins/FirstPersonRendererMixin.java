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
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public class FirstPersonRendererMixin {
    private ArmCarnisModel<Player> modelLeft;
    private ArmCarnisModel<Player> modelRight;

    private final ResourceLocation TEXTURE = new ResourceLocation(ArsCarnis.MODID,"textures/part_upgrade/arms/arm_spike.png");

    @Inject(method = "renderRightHand",at = @At("HEAD"),cancellable = true)
    private void cancelRenderHandR(PoseStack p_117771_, MultiBufferSource p_117772_, int p_117773_, AbstractClientPlayer p_117774_, CallbackInfo ci){
        if(SkillPlayerCapability.get(p_117774_).getHotBarSkill().getForName("spike_right") instanceof ArmsSpike armsSpike){
            if( ((Object)this) instanceof LivingEntityRenderer<?,?> renderer && renderer.getModel() instanceof PlayerModel playerModel){
                float partialTicks=Minecraft.getInstance().getPartialTick();

                if(modelRight==null){
                    this.modelRight=new ArmCarnisModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(ArmCarnisModel.LAYER_LOCATION));
                }

                RenderType renderType=RenderType.entityTranslucent(TEXTURE);
                modelRight.setupAnim((Player)p_117774_,0.0F,0.0F,p_117774_.tickCount+partialTicks,0.0F,0.0F);
                modelRight.renderToBuffer(p_117771_,p_117772_.getBuffer(renderType),p_117773_, OverlayTexture.NO_OVERLAY,1.0F,1.0F,1.0F,1.0F);
                ci.cancel();
            }
        }
    }

    @Inject(method = "renderLeftHand",at = @At("HEAD"),cancellable = true)
    private void cancelRenderHandL(PoseStack p_117771_, MultiBufferSource p_117772_, int p_117773_, AbstractClientPlayer p_117774_, CallbackInfo ci){
        if(SkillPlayerCapability.get(p_117774_).getHotBarSkill().getForName("spike_left") instanceof ArmsSpike armsSpike){
            if( ((Object)this) instanceof LivingEntityRenderer<?,?> renderer && renderer.getModel() instanceof PlayerModel playerModel){
                float partialTicks=Minecraft.getInstance().getPartialTick();

                if(modelLeft==null){
                    this.modelLeft=new ArmCarnisModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(ArmCarnisModel.LAYER_LOCATION));
                }
                RenderType renderType = RenderType.entityTranslucent(TEXTURE);

                //model1.copyOfLimbs(playerModel.leftArm);
                modelLeft.setupAnim((Player)p_117774_,0.0F,0.0F,p_117774_.tickCount+partialTicks,0.0F,0.0F);
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
