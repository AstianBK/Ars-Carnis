package com.TBK.ars_carnis;

import com.TBK.ars_carnis.client.model.ArmCarnisModel;
import com.TBK.ars_carnis.common.capability.ACCapability;
import com.TBK.ars_carnis.common.capability.CarnisPlayerCapability;
import com.TBK.ars_carnis.common.capability.SkillPlayerCapability;
import com.TBK.ars_carnis.common.skill.ArmsAbstract;
import com.TBK.ars_carnis.common.skill.ArmsSpike;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderArmEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber()
public class ModBusEvent {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ArsCarnis.MODID,"textures/part_upgrade/arms/arm_spike.png");

    @SubscribeEvent
    public static void onJoinGame(EntityJoinLevelEvent event) {
        if(event.getEntity() instanceof Player){
            SkillPlayerCapability cap = ACCapability.getEntityCap(event.getEntity(), SkillPlayerCapability.class);
            if(cap!=null){
                cap.onJoinGame((Player) event.getEntity(),event);
            }
            CarnisPlayerCapability cap1 = ACCapability.getEntityVam(event.getEntity(), CarnisPlayerCapability.class);
            if(cap1!=null){
                cap1.syncCap((Player) event.getEntity());
            }
        }else if (event.getEntity() instanceof LivingEntity){
            
        }
    }
    @SubscribeEvent
    public static void renderHand(RenderArmEvent event) {
        boolean flag = event.getArm() == HumanoidArm.LEFT;
        float f = flag ? 1.0F : -1.0F;
        if(SkillPlayerCapability.get(event.getPlayer()).getHotBarSkill().getForName("spike") instanceof ArmsSpike armsSpike && armsSpike.arm == ArmsAbstract.Arm.LEFT){
            if(event.getArm().equals(HumanoidArm.RIGHT)){
                event.getPoseStack().pushPose();
                int duration = SkillPlayerCapability.get(event.getPlayer()).getActiveEffectDuration().getRemainingDurationsForSkill("spike");
                float partialTicks=Minecraft.getInstance().getPartialTick();
                float normalizedDuration = 1.0F - ((float)duration / 5F);
                normalizedDuration += partialTicks / 1500.0F;
                normalizedDuration= Mth.clamp(normalizedDuration,0.0F,1.0F);
                float tick = easeIn(normalizedDuration);

                ArmCarnisModel<?> modelLeft=new ArmCarnisModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(ArmCarnisModel.LAYER_LOCATION));

                RenderType renderType = RenderType.entityTranslucent(TEXTURE);

                //model1.copyOfLimbs(playerModel.leftArm);
                if(duration>0){
                    event.getPoseStack().translate(0.0F,0.0f,-2.0f);
                    //ArsCarnis.LOGGER.debug("Xrot :" + playerModel.leftArm.xRot + " y :"+playerModel.leftArm.y + "z :"+playerModel.leftArm.z);
                    modelLeft.leftArm.xRot =  Mth.lerp(tick,  -(float) (Math.PI/4),(float) Math.PI);
                    modelLeft.leftArm.y = Mth.lerp(tick, 10 + 3.0F, 10);
                    modelLeft.leftArm.z = Mth.lerp(tick, 5 - 10.0F, 5);
                    ArsCarnis.LOGGER.debug("Xrot :" + modelLeft.leftArm.xRot + " y :"+modelLeft.leftArm.y + "z :"+modelLeft.leftArm.z);
                }else {
                    modelLeft.leftArm.xRot = (float) Math.PI;
                    modelLeft.leftArm.y = 10;
                    modelLeft.leftArm.z = 5;
                }

                modelLeft.renderToBuffer(event.getPoseStack(), event.getMultiBufferSource().getBuffer(renderType), event.getPackedLight(), OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
                event.getPoseStack().popPose();

            }
        }

    }

    private static float easeIn(float t) {
        return t * t;
    }
    @SubscribeEvent
    public static void onRightClick(PlayerInteractEvent.RightClickItem event){
        ItemStack stack=event.getEntity().getItemInHand(event.getHand());
        if(stack.is(Items.STICK) && !event.getEntity().isShiftKeyDown() && !event.getLevel().isClientSide){
            CarnisPlayerCapability cap = ACCapability.getEntityVam(event.getEntity(), CarnisPlayerCapability.class);
            if(cap!=null){
                boolean isVampire=cap.isCarnis();
                event.getEntity().sendSystemMessage(Component.nullToEmpty(cap.isCarnis() ?  "Te convertiste en Humano Cuck" :"Te convertiste en Mutante Gigachad" ));
                cap.convert(isVampire);
            }
        }else if(stack.is(Items.STICK) && event.getEntity().isShiftKeyDown()){
            CarnisPlayerCapability cap = ACCapability.getEntityVam(event.getEntity(), CarnisPlayerCapability.class);
            if(cap!=null){
                cap.age++;
                event.getEntity().sendSystemMessage(Component.nullToEmpty(String.valueOf(cap.age)));
            }
        }

        if(stack.is(Items.STICK)){
            event.getEntity().startUsingItem(InteractionHand.OFF_HAND);
        }

    }

    @SubscribeEvent
    public static void onTick(LivingEvent.LivingTickEvent event){
        if(event.getEntity() instanceof Player){
            CarnisPlayerCapability vampismo = ACCapability.getEntityVam(event.getEntity(), CarnisPlayerCapability.class);
            SkillPlayerCapability cap = ACCapability.getEntityCap(event.getEntity(), SkillPlayerCapability.class);
            if(cap!=null && event.getEntity().isAlive()){
                cap.tick((Player) event.getEntity());
            }
            if(vampismo!=null && event.getEntity().isAlive() && vampismo.isCarnis()){
                vampismo.tick((Player) event.getEntity());
            }
        }else {
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @SubscribeEvent
    public static void attachEntityCapability(AttachCapabilitiesEvent<Entity> event) {
        if(event.getObject() instanceof Player player){
            CarnisPlayerCapability oldVamp = ACCapability.getEntityVam(event.getObject(), CarnisPlayerCapability.class);
            SkillPlayerCapability oldCap = ACCapability.getEntityCap(event.getObject(), SkillPlayerCapability.class);

            if (oldVamp == null) {
                CarnisPlayerCapability.VampirePlayerProvider prov = new CarnisPlayerCapability.VampirePlayerProvider();
                CarnisPlayerCapability cap=prov.getCapability(ACCapability.VAMPIRE_CAPABILITY).orElse(null);
                cap.initialize(player);
                event.addCapability(new ResourceLocation(ArsCarnis.MODID, "carnis_cap"), prov);
            }

            if (oldCap == null) {
                SkillPlayerCapability.SkillPlayerProvider prov = new SkillPlayerCapability.SkillPlayerProvider();
                SkillPlayerCapability cap=prov.getCapability(ACCapability.POWER_CAPABILITY).orElse(null);
                cap.init(player);
                event.addCapability(new ResourceLocation(ArsCarnis.MODID, "skill_cap"), prov);
            }
        }else if(event.getObject() instanceof LivingEntity living){

        }

        /*AnimationPlayerCapability oldPatch=ACCapability.getEntityPatch(event.getObject(), AnimationPlayerCapability.class);
        if (oldPatch==null){
            AnimationPlayerCapability.AnimationPlayerProvider prov = new AnimationPlayerCapability.AnimationPlayerProvider();
            AnimationPlayerCapability getSkillCap=prov.getCapability(ACCapability.ANIMATION_CAPABILITY).orElse(null);
            if(event.getObject() instanceof Player player){
                getSkillCap.initialize(player);
                event.addCapability(new ResourceLocation(ArsCarnis.MODID, "animation_patch"), prov);
            }
        }*/
    }
    @SubscribeEvent
    public static void clonePlayer(PlayerEvent.Clone event){
        Player player=event.getOriginal();
        Player newPlayer=event.getEntity();
        player.reviveCaps();
        CarnisPlayerCapability cap=CarnisPlayerCapability.get(player);
        CarnisPlayerCapability newCap=CarnisPlayerCapability.get(newPlayer);
        newCap.clone(cap,player,newPlayer);
        player.invalidateCaps();
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void renderHandEvent(RenderHandEvent event){
        if(Minecraft.getInstance().player!=null){
            CarnisPlayerCapability cap=CarnisPlayerCapability.get(Minecraft.getInstance().player);
            if(cap!=null && cap.isCarnis()){
                event.setCanceled(cap.getSkillCap(Minecraft.getInstance().player).isTransform);
            }
        }
    }

    @SubscribeEvent
    public static void deathEntity(LivingDeathEvent event){
        LivingEntity target=event.getEntity();
        /*if(target instanceof Player player){
            CarnisPlayerCapability cap=CarnisPlayerCapability.get(player);
            if(cap!=null && cap.isVampire() && cap.canRevive()){
                if (!cap.noMoreLimbs()){
                    player.setHealth(1.0F);
                    cap.loseBlood(4);
                    player.setInvulnerable(true);
                    event.setCanceled(true);
                    loseBody(cap,player);
                    player.level().playSound(player,player,SGSounds.VAMPIRE_RESURRECT.get(), SoundSource.PLAYERS,1.0F,1.0F);
                }
            }
        }
        if (event.getSource().getDirectEntity() instanceof BloodOrbProjetile orb){
            if(!orb.level().isClientSide){
                LivingEntity collateral=target.level().getNearestEntity(LivingEntity.class, TargetingConditions.DEFAULT.selector(e->e!=orb.getOwner()), null,target.getX(),target.getY(),target.getZ(),target.getBoundingBox().inflate(10.0D));
                if(collateral!=null){
                    BloodOrbProjetile projetile=new BloodOrbProjetile(collateral.level(),((LivingEntity)orb.getOwner()),Math.max(orb.getPowerLevel()-1,0));
                    projetile.setPos(target.getEyePosition());
                    Vec3 delta=collateral.getEyePosition().subtract(target.getEyePosition());
                    projetile.shoot(delta.x,delta.y,delta.z,1.0F,1.0F);
                    collateral.level().addFreshEntity(projetile);
                }
            }
        }*/
    }

    @SubscribeEvent
    public static void canAffectedEffect(MobEffectEvent.Applicable event){
        LivingEntity target=event.getEntity();
        /*if(target instanceof Player player){
            MobEffect effect=event.getEffectInstance().getEffect();
            if(Util.isVampire(player) && (effect== MobEffects.WITHER || effect==MobEffects.POISON)){
                event.setCanceled(true);
            }
        }*/

    }

    @SubscribeEvent
    public static void drownEvent(LivingDrownEvent event){
        LivingEntity living = event.getEntity();
        /*if(living instanceof Player player && Util.isVampire(player)){
            event.setCanceled(true);
        }*/
    }


    public static void loseBody(CarnisPlayerCapability cap,Player player){
        int timer=cap.getRegTimer();
        /*cap.losePart("head",new RegenerationInstance(timer),player);
        cap.losePart("body",new RegenerationInstance(timer),player);
        cap.losePart("right_arm",new RegenerationInstance(timer),player);
        cap.losePart("left_arm",new RegenerationInstance(timer),player);
        cap.losePart("right_leg",new RegenerationInstance(timer),player);
        cap.losePart("left_leg",new RegenerationInstance(timer),player);*/
    }

    @SubscribeEvent
    public static void healPlayer(LivingHealEvent event){
        if(event.getEntity() instanceof Player player){
            CarnisPlayerCapability cap=CarnisPlayerCapability.get(player);
            if(cap!=null && cap.isCarnis()){
                event.setCanceled(true);
            }
        }
    }
    @SubscribeEvent
    public static void hurtPlayer(LivingHurtEvent event){
        LivingEntity living=event.getEntity();

    }
}
