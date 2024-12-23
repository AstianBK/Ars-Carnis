package com.TBK.ars_carnis.common.capability;

import com.TBK.ars_carnis.common.api.ICarnisPlayer;
import com.TBK.ars_carnis.server.network.PacketHandler;
import com.TBK.ars_carnis.server.network.messager.PacketHandlerPowers;
import com.TBK.ars_carnis.server.network.messager.PacketSyncVampireData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class CarnisPlayerCapability implements ICarnisPlayer {
    Player player;
    Level level;
    public int generation=0;
    boolean isVampire=false;
    public int age=0;
    public int growTimerMax=72000;
    public int growTimer=0;
    //public LimbsPartRegeneration limbsPartRegeneration=new LimbsPartRegeneration();
    public int clientDrink=0;
    private int hugeTick=0;


    public static CarnisPlayerCapability get(Player player){
        return ACCapability.getEntityVam(player, CarnisPlayerCapability.class);
    }


    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public int getGeneration() {
        return this.generation;
    }



    public void convert(boolean isVampire){
        this.setIsCarnis(!isVampire);

    }

    public int getMaxBlood(){
        return ((this.age/10)*2)+((11-this.generation)*2);
    }

    @Override
    public void setGeneration(int generation) {
        this.generation=Math.max(generation,1);
    }
    public void setAge(int age){
        this.age=Math.min(age,100);
    }



    @Override
    public void mutate(Player player, Entity target) {

    }


    @Override
    public SkillPlayerCapability getSkillCap(Player player) {
        return SkillPlayerCapability.get(player);
    }

    @Override
    public void tick(Player player) {
        if (this.clientDrink>0){
            this.clientDrink--;
        }
        if(this.growTimer++>=this.growTimerMax){
            this.growTimer=0;
            this.age++;
        }

        if (player.isAlive() && !this.getSkillCap(player).isTransform) {
            boolean flag = this.isSunBurnTick(player);
            if (flag) {
                ItemStack itemstack = player.getItemBySlot(EquipmentSlot.HEAD);
                if (!itemstack.isEmpty()) {
                    if (itemstack.isDamageableItem()) {
                        itemstack.setDamageValue(itemstack.getDamageValue() + player.level().random.nextInt(2));
                        if (itemstack.getDamageValue() >= itemstack.getMaxDamage()) {
                            player.broadcastBreakEvent(EquipmentSlot.HEAD);
                            player.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
                        }
                    }

                    flag = false;
                }

                if (flag) {
                    player.setSecondsOnFire(8);
                }
            }
        }
        /*if(this.noMoreLimbs()){
            player.setPos(player.getX(),player.getY(),player.getZ());
            player.setDeltaMovement(0.0F,0.0F,0.0F);
        }*/
    }

    protected boolean isSunBurnTick(Player player) {
        if (this.level.isDay() && !this.level.isClientSide) {
            float f = player.getLightLevelDependentMagicValue();
            BlockPos blockpos = BlockPos.containing(player.getX(), player.getEyeY(), player.getZ());
            boolean flag = player.isInWaterRainOrBubble() || player.isInPowderSnow || player.wasInPowderSnow;
            if (f > 0.5F && player.level().random.nextFloat() * 30.0F < (f - 0.4F) * 2.0F && !flag && player.level().canSeeSky(blockpos)) {
                return true;
            }
        }

        return false;
    }

    public boolean isDurationEffectTick(int p_19455_, int p_19456_) {
        int k = 100 >> p_19456_;
        if (k > 0) {
            return p_19455_ % k == 0;
        } else {
            return true;
        }
    }
    public int getRegTimer(){
        return (int) (140-140*(0.5F*(this.age/100)+0.36F*(1.0F-this.generation/10.0F)));
    }

    public void syncCap(Player player){
        if(player instanceof ServerPlayer serverPlayer){
            //this.getLimbsPartRegeneration().syncPlayer();
            PacketHandler.sendToPlayer(new PacketSyncVampireData(this.age,this.generation,this.isVampire),serverPlayer);
        }
    }


    @Override
    public boolean isCarnis() {
        return this.isVampire;
    }

    @Override
    public boolean loseBlood(int blood) {
        return false;
    }

    @Override
    public void setIsCarnis(boolean bol) {
        this.isVampire=bol;
    }

    @Override
    public void setPlayer(Player player) {
        this.player=player;
    }

    public void clone(CarnisPlayerCapability capability,Player player,Player newPlayer){
        if(!this.level.isClientSide){
            PacketHandler.sendToPlayer(new PacketHandlerPowers(1, newPlayer,player), (ServerPlayer) player);
        }
        this.initialize(newPlayer);
        //this.setClan(capability.getClan());
        this.setIsCarnis(capability.isVampire);
        this.setGeneration(capability.getGeneration());
        //this.setBlood(0);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag=new CompoundTag();
        tag.putBoolean("isVampire",this.isVampire);
        tag.putInt("age",this.age);
        tag.putInt("generation",this.generation);
        //tag.putString("clan",this.clan.toString());
        //tag.putDouble("blood",this.getBlood());
        /*if(this.getLimbsPartRegeneration().hasRegenerationLimbs()){
            tag.put("limbs",this.getLimbsPartRegeneration().saveNBTData());
        }*/
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.isVampire=nbt.getBoolean("isVampire");
        this.setAge(nbt.getInt("age"));
        this.generation=nbt.getInt("generation");
        /*this.clan=Clan.valueOf(nbt.getString("clan"));
        this.setBlood(nbt.getDouble("blood"));
        if(nbt.contains("limbs",9)){
            this.getLimbsPartRegeneration().loadNBTData(nbt.getList("limbs",10));
        }*/
    }

    public void initialize(Player player) {
        this.setPlayer(player);
        this.level=player.level();
        if(player instanceof ServerPlayer){
            //this.setLimbsPartRegeneration(new LimbsPartRegeneration((ServerPlayer) player));
        }
    }

    public static class VampirePlayerProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundTag> {
        private final LazyOptional<ICarnisPlayer> instance = LazyOptional.of(CarnisPlayerCapability::new);

        @NonNull
        @Override
        public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            return ACCapability.VAMPIRE_CAPABILITY.orEmpty(cap,instance.cast());
        }

        @Override
        public CompoundTag serializeNBT() {
            return instance.orElseThrow(NullPointerException::new).serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            instance.orElseThrow(NullPointerException::new).deserializeNBT(nbt);
        }
    }
}
