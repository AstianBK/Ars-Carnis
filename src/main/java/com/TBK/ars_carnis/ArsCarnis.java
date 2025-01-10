package com.TBK.ars_carnis;

import com.TBK.ars_carnis.client.ClientProxy;
import com.TBK.ars_carnis.client.gui.IncubationTubeScreenMenu;
import com.TBK.ars_carnis.client.renderer.CarnisOvumEntityRenderer;
import com.TBK.ars_carnis.client.renderer.CarnisSpinaEntityRenderer;
import com.TBK.ars_carnis.common.Proxy;
import com.TBK.ars_carnis.common.registry.*;
import com.TBK.ars_carnis.server.network.PacketHandler;
import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ArsCarnis.MODID)
public class ArsCarnis
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "ars_carnis";
    public static Proxy PROXY = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> Proxy::new);
    public static final Logger LOGGER = LogUtils.getLogger();

    public ArsCarnis()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        MinecraftForge.EVENT_BUS.register(this);
        ACEntityType.ENTITY_TYPES.register(modEventBus);
        ACBlocks.BLOCKS.register(modEventBus);
        ACBlockEntity.BLOCKS_ENTITY.register(modEventBus);
        ACItems.ITEMS.register(modEventBus);
        ACMenuType.MENU_TYPE.register(modEventBus);
        ACRecipeSerializer.RECIPE_SERIALIZERS.register(modEventBus);
        ACRecipeSerializer.RECIPE_TYPES.register(modEventBus);
        ACSkillAbstract.init();
        ACCreativeTabs.TABS.register(modEventBus);
        PacketHandler.registerMessages();
        modEventBus.addListener(this::clientSetup);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT,()->()->{
            modEventBus.addListener(this::registerRenderers);
        });
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }


    @OnlyIn(Dist.CLIENT)
    private void registerRenderers(FMLCommonSetupEvent event){
        EntityRenderers.register(ACEntityType.CARNIS_OVUM.get(), CarnisOvumEntityRenderer::new);
        EntityRenderers.register(ACEntityType.CARNIS_SPINA.get(), CarnisSpinaEntityRenderer::new);
        MenuScreens.register(ACMenuType.FURNACE_MENU.get(), IncubationTubeScreenMenu::new);
    }


    private void clientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> PROXY.init());
    }

}
