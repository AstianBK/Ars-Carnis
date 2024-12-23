package com.TBK.ars_carnis.server.entity;

import com.TBK.ars_carnis.ArsCarnis;
import com.TBK.ars_carnis.common.registry.ACEntityType;
import com.TBK.ars_carnis.common.registry.ACTags;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class CarnisOvumEntity extends Mob {
    public static final EntityDataAccessor<Integer> PHASE =
            SynchedEntityData.defineId(CarnisOvumEntity.class, EntityDataSerializers.INT);

    static final Predicate<ItemEntity> ALLOWED_ITEMS = (p_289438_) -> {
        return !p_289438_.hasPickUpDelay() && p_289438_.isAlive() && p_289438_.getItem().is(ACTags.IS_MEAT);
    };
    public final AnimationState eat=new AnimationState();
    public final AnimationState idle=new AnimationState();
    public int meatEarn=0;
    private int ticksSinceEaten;
    public float oSquish=0.0F;
    public float squish=0.0F;
    public float targetSquish;
    private int idleAnimationTimeout;

    public CarnisOvumEntity(EntityType<? extends Mob> p_21368_, Level p_21369_) {
        super(p_21368_, p_21369_);
        this.setCanPickUpLoot(true);

    }

    public static AttributeSupplier setAttributes() {
        return TamableAnimal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.FOLLOW_RANGE, 30.D)
                .add(Attributes.MOVEMENT_SPEED, 0.2d)
                .add(Attributes.FLYING_SPEED,0.25D)
                .add(Attributes.ATTACK_DAMAGE,2.0D)
                .build();

    }

    public int getMaxHeadXRot() {
        return 0;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(4,new LarvaCarnisSearchForItemsGoal());
        //this.goalSelector.addGoal(6,new LarvaCarnisEntityRandomDirectionGoal(this));
    }
    public float getSize(){
        return 1.0F+1.0F*this.getIdPhase();
    }

    public int getIdPhase(){
        return this.entityData.get(PHASE);
    }

    public void setIdPhase(int id){
        this.entityData.set(PHASE,id);
        int i = Mth.clamp(id, 1, 127);
        this.reapplyPosition();
        this.refreshDimensions();
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue((double)(i * i));
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue((double)(0.2F + 0.1F * (float)i));
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue((double)i);
        this.setHealth(this.getMaxHealth());


        this.xpReward = i;
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> p_21104_) {
        super.onSyncedDataUpdated(p_21104_);
        if(p_21104_.equals(PHASE)){
            this.refreshDimensions();
            this.setYRot(this.yHeadRot);
            this.yBodyRot = this.yHeadRot;
            if (this.isInWater() && this.random.nextInt(20) == 0) {
                this.doWaterSplashEffect();
            }
        }
    }

    public GrowPhase getPhase(){
        return GrowPhase.byId(this.getIdPhase() & 255);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(PHASE,0);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag p_21484_) {
        super.addAdditionalSaveData(p_21484_);
        p_21484_.putInt("idPhase",this.getIdPhase());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag p_21450_) {
        super.readAdditionalSaveData(p_21450_);
        this.setIdPhase(p_21450_.getInt("idPhase"));
    }

    public boolean canTakeItem(ItemStack p_28552_) {
        EquipmentSlot equipmentslot = Mob.getEquipmentSlotForItem(p_28552_);
        if (!this.getItemBySlot(equipmentslot).isEmpty()) {
            return false;
        } else {
            return equipmentslot == EquipmentSlot.MAINHAND && super.canTakeItem(p_28552_);
        }
    }

    @Override
    public void tick() {
        this.squish += (this.targetSquish - this.squish) * 0.5F;
        this.oSquish = this.squish;
        super.tick();
        if(!this.level().isClientSide && this.isEffectiveAi() && this.isAlive()){
            ItemStack itemstack = this.getItemBySlot(EquipmentSlot.MAINHAND);
            if (this.canEat(itemstack)) {
                ++this.ticksSinceEaten;
                this.getNavigation().stop();
                if (this.ticksSinceEaten > 30) {
                    ItemStack itemstack1 = itemstack.finishUsingItem(this.level(), this);
                    if (!itemstack1.isEmpty()) {
                        this.setItemSlot(EquipmentSlot.MAINHAND, itemstack1);
                    }
                    this.meatEarn++;
                    this.level().broadcastEntityEvent(this,(byte) 4);
                    this.ticksSinceEaten = 0;
                } else if (this.ticksSinceEaten > 0 && this.random.nextFloat() < 0.1F) {
                    this.playSound(this.getEatingSound(itemstack), 1.0F, 1.0F);
                    this.level().broadcastEntityEvent(this, (byte)8);
                }
            }
        }

        if(this.meatEarn>=5){
            if(this.getIdPhase()<2){
                this.grow();
                this.addEffect(new MobEffectInstance(MobEffects.REGENERATION,100,1));
            }else {
                if(!this.level().isClientSide){
                    this.hatching();
                }
            }
            this.meatEarn=0;
        }

        if(this.level().isClientSide){
            this.setupAnimationStates();
        }

        this.decreaseSquish();
    }

    private void hatching() {
        CarnisSpinaEntity spinaEntity = ACEntityType.CARNIS_SPINA.get().create(this.level());
        if(spinaEntity!=null){
            spinaEntity.setPos(this.position());
            this.level().addFreshEntity(spinaEntity);
            this.level().broadcastEntityEvent(this,(byte) 60);
        }
        this.remove(RemovalReason.DISCARDED);
    }

    protected void updateWalkAnimation(float p_268362_) {
        float f;
        if (this.getPose() == Pose.STANDING) {
            f = Math.min(p_268362_ * 6.0F, 1.0F);
        } else {
            this.idleAnimationTimeout=1;
            f = 0.0F;
        }

        this.walkAnimation.update(f, 0.2F);
    }

    @Override
    public boolean canBeAffected(MobEffectInstance p_21197_) {
        return super.canBeAffected(p_21197_) && p_21197_.getEffect() !=MobEffects.HUNGER;
    }

    private void setupAnimationStates() {
        if (this.idleAnimationTimeout <= 0 && this.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
            this.idleAnimationTimeout = 40;
            this.idle.start(this.tickCount);
            this.eat.stop();
        } else {
            --this.idleAnimationTimeout;
        }
    }

    public boolean canEat(ItemStack stack){
        return stack.is(ACTags.IS_MEAT);
    }

    protected void decreaseSquish() {
        this.targetSquish *= 0.6F;
    }

    @Override
    public void handleEntityEvent(byte p_21375_) {
        if(p_21375_==4){
            this.meatEarn++;
        }else if (p_21375_==8) {
            this.eat.start(this.tickCount);
            this.idle.stop();
            this.idleAnimationTimeout=30;
            ItemStack itemstack = this.getItemBySlot(EquipmentSlot.MAINHAND);
            if (!itemstack.isEmpty()) {
                for(int i = 0; i < 8; ++i) {
                    Vec3 vec3 = (new Vec3(((double)this.random.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D)).xRot(-this.getXRot() * ((float)Math.PI / 180F)).yRot(-this.getYRot() * ((float)Math.PI / 180F));
                    this.level().addParticle(new ItemParticleOption(ParticleTypes.ITEM, itemstack), this.getX() + this.getLookAngle().x / 2.0D, this.getY(), this.getZ() + this.getLookAngle().z / 2.0D, vec3.x, vec3.y + 0.05D, vec3.z);
                }
            }
        }if (p_21375_==60){
            Minecraft mc = Minecraft.getInstance();
            for(int i = 0; i < 16; ++i) {
                double d0 = (double)(this.random.nextFloat() * 2.0F - 1.0F);
                double d1 = (double)(this.random.nextFloat() * 2.0F - 1.0F);
                double d2 = (double)(this.random.nextFloat() * 2.0F - 1.0F);
                if (!(d0 * d0 + d1 * d1 + d2 * d2 > 1.0D)) {
                    double d3 = this.getX(d0 / 4.0D);
                    double d4 = this.getY(0.5D + d1 / 4.0D);
                    double d5 = this.getZ(d2 / 4.0D);
                    mc.particleEngine.createParticle(ParticleTypes.SMOKE, d3, d4, d5, d0, d1 + 0.2D, d2).setColor(1.0F,0.0F,0.0F);
                }
            }
        }else  {
            super.handleEntityEvent(p_21375_);
        }
    }

    private void grow() {
        ArsCarnis.LOGGER.debug("Crecio a la :"+( GrowPhase.byId((this.getIdPhase()+1) & 255)) );
        this.setIdPhase(this.getIdPhase()+1);
    }

    public boolean canHoldItem(ItemStack p_28578_) {
        Item item = p_28578_.getItem();
        ItemStack itemstack = this.getItemBySlot(EquipmentSlot.MAINHAND);
        return itemstack.isEmpty() || this.ticksSinceEaten > 0 && this.canEat(itemstack);
    }

    private void spitOutItem(ItemStack p_28602_) {
        if (!p_28602_.isEmpty() && !this.level().isClientSide) {
            ItemEntity itementity = new ItemEntity(this.level(), this.getX() + this.getLookAngle().x, this.getY() + 1.0D, this.getZ() + this.getLookAngle().z, p_28602_);
            itementity.setPickUpDelay(40);
            itementity.setThrower(this.getUUID());
            this.playSound(SoundEvents.FOX_SPIT, 1.0F, 1.0F);
            this.level().addFreshEntity(itementity);
        }
    }

    private void dropItemStack(ItemStack p_28606_) {
        ItemEntity itementity = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), p_28606_);
        this.level().addFreshEntity(itementity);
    }

    private boolean canMove() {
        return true;
    }

    protected void pickUpItem(ItemEntity p_28514_) {
        ItemStack itemstack = p_28514_.getItem();
        if (this.canHoldItem(itemstack)) {
            int i = itemstack.getCount();
            if (i > 1) {
                this.dropItemStack(itemstack.split(i - 1));
            }

            this.spitOutItem(this.getItemBySlot(EquipmentSlot.MAINHAND));
            this.onItemPickup(p_28514_);
            this.setItemSlot(EquipmentSlot.MAINHAND, itemstack.split(1));
            this.setGuaranteedDrop(EquipmentSlot.MAINHAND);
            this.take(p_28514_, itemstack.getCount());
            p_28514_.discard();
            this.ticksSinceEaten = 0;
        }

    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_21434_, DifficultyInstance p_21435_, MobSpawnType p_21436_, @Nullable SpawnGroupData p_21437_, @Nullable CompoundTag p_21438_) {
        this.setIdPhase(0);
        return super.finalizeSpawn(p_21434_, p_21435_, p_21436_, p_21437_, p_21438_);
    }

    protected void jumpFromGround() {
        Vec3 vec3 = this.getDeltaMovement();
        this.setDeltaMovement(vec3.x, (double)this.getJumpPower(), vec3.z);
        this.hasImpulse = true;
    }

    private boolean isTiny() {
        return this.getPhase()==GrowPhase.BABY;
    }

    public EntityDimensions getDimensions(Pose p_33597_) {
        return super.getDimensions(p_33597_).scale(this.getSize());
    }

    public void refreshDimensions() {
        double d0 = this.getX();
        double d1 = this.getY();
        double d2 = this.getZ();
        super.refreshDimensions();
        this.setPos(d0, d1, d2);
    }
    class LarvaCarnisSearchForItemsGoal extends Goal {
        public LarvaCarnisSearchForItemsGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        public boolean canUse() {
            if (!CarnisOvumEntity.this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty()) {
                return false;
            } else if (CarnisOvumEntity.this.getTarget() == null && CarnisOvumEntity.this.getLastHurtByMob() == null) {
                if (!CarnisOvumEntity.this.canMove()) {
                    return false;
                } else if (CarnisOvumEntity.this.getRandom().nextInt(reducedTickDelay(10)) != 0) {
                    return false;
                } else {
                    List<ItemEntity> list = CarnisOvumEntity.this.level().getEntitiesOfClass(ItemEntity.class, CarnisOvumEntity.this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), CarnisOvumEntity.ALLOWED_ITEMS);
                    return !list.isEmpty() && CarnisOvumEntity.this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty();
                }
            } else {
                return false;
            }
        }

        public void tick() {
            List<ItemEntity> list = CarnisOvumEntity.this.level().getEntitiesOfClass(ItemEntity.class, CarnisOvumEntity.this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), CarnisOvumEntity.ALLOWED_ITEMS);
            ItemStack itemstack = CarnisOvumEntity.this.getItemBySlot(EquipmentSlot.MAINHAND);
            if (itemstack.isEmpty() && !list.isEmpty()) {
                BlockPos pos=list.get(0).blockPosition();
                Path path=CarnisOvumEntity.this.navigation.createPath(pos.getX(),pos.getY(),pos.getZ(),0);
                CarnisOvumEntity.this.getNavigation().moveTo(path, (double)0.5F);
            }

        }

        public void start() {
            List<ItemEntity> list = CarnisOvumEntity.this.level().getEntitiesOfClass(ItemEntity.class, CarnisOvumEntity.this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), CarnisOvumEntity.ALLOWED_ITEMS);
            if (!list.isEmpty()) {
                BlockPos pos=list.get(0).blockPosition();
                Path path=CarnisOvumEntity.this.navigation.createPath(pos.getX(),pos.getY(),pos.getZ(),0);
                CarnisOvumEntity.this.getNavigation().moveTo(path, (double)0.5F);
            }

        }
    }

    public enum GrowPhase{
        BABY(0),
        TEEN(1),
        ADULT(2);
        private static final GrowPhase[] BY_ID = Arrays.stream(values()).sorted(Comparator.comparingInt(GrowPhase::getId)).toArray(GrowPhase[]::new);

        public final int id;
        GrowPhase(int id){
            this.id=id;
        }

        public int getId(){
            return this.id;
        }

        public static GrowPhase byId(int p_30987_) {
            return BY_ID[p_30987_ % BY_ID.length];
        }
    }
}
