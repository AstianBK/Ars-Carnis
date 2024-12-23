package com.TBK.ars_carnis.server.entity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.level.Level;

public class CarnisSpinaEntity extends PathfinderMob {
    public static final EntityDataAccessor<Boolean> CHARGING =
            SynchedEntityData.defineId(CarnisSpinaEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> ATTACKING =
            SynchedEntityData.defineId(CarnisSpinaEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Integer> PHASE =
            SynchedEntityData.defineId(CarnisSpinaEntity.class, EntityDataSerializers.INT);

    public final AnimationState idle=new AnimationState();
    public final AnimationState attack1=new AnimationState();
    public final AnimationState attack2=new AnimationState();
    public final AnimationState attack_double=new AnimationState();

    public final AnimationState jaw=new AnimationState();
    private int idleAnimationTimeout;
    public int attackTimer;

    public CarnisSpinaEntity(EntityType<? extends PathfinderMob> p_21368_, Level p_21369_) {
        super(p_21368_, p_21369_);
    }
    public static AttributeSupplier setAttributes() {
        return TamableAnimal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.FOLLOW_RANGE, 45.D)
                .add(Attributes.MOVEMENT_SPEED, 0.2d)
                .add(Attributes.FLYING_SPEED,0.25D)
                .add(Attributes.ATTACK_DAMAGE,5.0D)
                .build();

    }
    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(5,new RandomStrollGoal(this,1.0D));
        this.goalSelector.addGoal(3,new SpinaAttackGoal(this,2.0D,false));
        this.targetSelector.addGoal(4,new NearestAttackableTargetGoal<>(this,LivingEntity.class,false));
    }

    @Override
    public void tick() {
        super.tick();
        if(this.isAttacking()){
            this.attackTimer--;
            if(this.attackTimer<=0){
                this.setIsAttacking(false);
            }
        }
        if(this.level().isClientSide){
            this.setupAnimationStates();
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ATTACKING,false);
    }

    protected void updateWalkAnimation(float p_268362_) {
        float f;
        if (this.getPose() == Pose.STANDING) {
            f = Math.min(p_268362_ * 6.0F, 1.0F);
        } else {
            this.idleAnimationTimeout=1;
            this.idle.stop();
            f = 0.0F;
        }

        this.walkAnimation.update(f, 0.2F);
    }

    private void setupAnimationStates() {
        if (this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = 40;
            this.idle.start(this.tickCount);
            this.attack1.stop();
            this.attack2.stop();
            this.attack_double.stop();
        } else {
            --this.idleAnimationTimeout;
        }

        if(this.isAggressive()){
            this.jaw.startIfStopped(this.tickCount);
        }else {
            this.jaw.stop();
        }
        
    }



    @Override
    public void handleEntityEvent(byte p_21807_) {
        if(p_21807_==4){
            this.attack1.start(this.tickCount);
            this.idle.stop();
            this.idleAnimationTimeout=20;
        }else if(p_21807_==61){
            this.attack2.start(this.tickCount);
            this.idle.stop();
            this.idleAnimationTimeout=20;
        }else if(p_21807_==62){
            this.attack_double.start(this.tickCount);
            this.idle.stop();
            this.idleAnimationTimeout=20;
        }else {
            super.handleEntityEvent(p_21807_);
        }
    }

    public boolean isAttacking(){
        return this.entityData.get(ATTACKING);
    }

    public void setIsAttacking(boolean attacking){
        this.entityData.set(ATTACKING,attacking);
        this.attackTimer = attacking ? 20 : 0 ;
    }

    public void playAttack(int attack){
        switch (attack){
            case 0->{
                this.level().broadcastEntityEvent(this,(byte) 4);
            }
            case 1->{
                this.level().broadcastEntityEvent(this,(byte) 61);
            }
            case 2->{
                this.level().broadcastEntityEvent(this,(byte) 62);
            }
        }
    }


    class SpinaAttackGoal extends MeleeAttackGoal {

        public SpinaAttackGoal(PathfinderMob p_25552_, double p_25553_, boolean p_25554_) {
            super(p_25552_, p_25553_, p_25554_);
        }

        @Override
        public void tick() {
            super.tick();
        }

        @Override
        protected void checkAndPerformAttack(LivingEntity p_25557_, double p_25558_) {
            double d0 = this.getAttackReachSqr(p_25557_);
            if (p_25558_ <= d0 && this.getTicksUntilNextAttack()<=0 && CarnisSpinaEntity.this.attackTimer<=0) {
                this.resetAttackCooldown();
                this.mob.swing(InteractionHand.MAIN_HAND);
                this.mob.doHurtTarget(p_25557_);
            }
        }


        @Override
        protected void resetAttackCooldown() {
            super.resetAttackCooldown();
            CarnisSpinaEntity.this.setIsAttacking(true);
            int value = CarnisSpinaEntity.this.level().random.nextInt(0,3);
            if(!CarnisSpinaEntity.this.level().isClientSide){
                CarnisSpinaEntity.this.playAttack(value);
            }
        }
    }
}
