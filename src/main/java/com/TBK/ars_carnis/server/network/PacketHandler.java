package com.TBK.ars_carnis.server.network;

import com.TBK.ars_carnis.ArsCarnis;
import com.TBK.ars_carnis.server.network.messager.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {
    private static final String PROTOCOL_VERSION = "1.0";
    public static SimpleChannel MOD_CHANNEL;

    public static void registerMessages() {
        int index = 0;
        SimpleChannel channel= NetworkRegistry.ChannelBuilder.named(
                        new ResourceLocation(ArsCarnis.MODID, "messages"))
                .networkProtocolVersion(()-> PROTOCOL_VERSION)
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        MOD_CHANNEL=channel;


        channel.registerMessage(index++, PacketSyncCooldown.class, PacketSyncCooldown::write,
                PacketSyncCooldown::new, PacketSyncCooldown::handle);

        channel.messageBuilder(PacketConvertVampire.class,index++)
                .encoder(PacketConvertVampire::toBytes)
                .decoder(PacketConvertVampire::new)
                .consumerNetworkThread(PacketConvertVampire::handle).add();

        channel.registerMessage(index++, PacketSyncDurationEffect.class, PacketSyncDurationEffect::write,
                PacketSyncDurationEffect::new, PacketSyncDurationEffect::handle);

        channel.messageBuilder(PacketSyncVampireData.class,index++)
                .encoder(PacketSyncVampireData::toBytes)
                .decoder(PacketSyncVampireData::new)
                .consumerNetworkThread(PacketSyncVampireData::handle).add();


        /*channel.messageBuilder(PacketSyncLimbRegeneration.class,index++)
                .encoder(PacketSyncLimbRegeneration::toBytes)
                .decoder(PacketSyncLimbRegeneration::new)
                .consumerNetworkThread(PacketSyncLimbRegeneration::handle).add();*/



        channel.messageBuilder(PacketRemoveActiveEffect.class,index++)
                .encoder(PacketRemoveActiveEffect::toBytes)
                .decoder(PacketRemoveActiveEffect::new)
                .consumerNetworkThread(PacketRemoveActiveEffect::handle).add();

        channel.registerMessage(index++, PacketKeySync.class, PacketKeySync::write,
                PacketKeySync::new, PacketKeySync::handle);

        channel.registerMessage(index++, PacketSyncBloodEntity.class, PacketSyncBloodEntity::write,
                PacketSyncBloodEntity::new, PacketSyncBloodEntity::handle);


        channel.messageBuilder(PacketSyncBlood.class,index++)
                .encoder(PacketSyncBlood::write)
                .decoder(PacketSyncBlood::new)
                .consumerNetworkThread(PacketSyncBlood::handle).add();

        channel.messageBuilder(PacketSyncBiteTarget.class,index++)
                .encoder(PacketSyncBiteTarget::write)
                .decoder(PacketSyncBiteTarget::new)
                .consumerNetworkThread(PacketSyncBiteTarget::handle).add();

        channel.registerMessage(index++, PacketHandlerParticles.class, PacketHandlerParticles::write,
                PacketHandlerParticles::new, PacketHandlerParticles::handle);

        channel.registerMessage(index++, PacketHandlerPowers.class, PacketHandlerPowers::write,
                PacketHandlerPowers::new, PacketHandlerPowers::handle);


        channel.messageBuilder(PacketSyncPosHotBar.class,index++)
                .encoder(PacketSyncPosHotBar::write)
                .decoder(PacketSyncPosHotBar::new)
                .consumerNetworkThread(PacketSyncPosHotBar::handle).add();


        channel.messageBuilder(PacketActiveEffect.class,index++)
                .encoder(PacketActiveEffect::toBytes)
                .decoder(PacketActiveEffect::new)
                .consumerNetworkThread(PacketActiveEffect::handle).add();



    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        MOD_CHANNEL.send(PacketDistributor.PLAYER.with(() -> player),message);
    }

    public static <MSG> void sendToServer(MSG message) {
        MOD_CHANNEL.sendToServer(message);
    }

    public static <MSG> void sendToAllTracking(MSG message, LivingEntity entity) {
        MOD_CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), message);
    }
}
