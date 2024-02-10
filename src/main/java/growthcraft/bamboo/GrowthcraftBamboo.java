package growthcraft.bamboo;

import growthcraft.bamboo.init.GrowthcraftBambooBlockEntities;
import growthcraft.bamboo.init.GrowthcraftBambooBlocks;
import growthcraft.bamboo.init.GrowthcraftBambooItems;
import growthcraft.bamboo.init.config.GrowthcraftBambooConfig;
import growthcraft.bamboo.shared.Reference;
import growthcraft.core.init.GrowthcraftCreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Mod(Reference.MODID)
@Mod.EventBusSubscriber(modid = Reference.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GrowthcraftBamboo {
    public static final Logger LOGGER = LogManager.getLogger(Reference.MODID);

    public GrowthcraftBamboo() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::setup);
        modEventBus.addListener(this::clientSetupEvent);
        modEventBus.addListener(this::buildCreativeTabContents);

        // Config
        GrowthcraftBambooConfig.loadConfig();

        // Blocks, Items, Fluids, Block Entities, Containers
        GrowthcraftBambooBlocks.BLOCKS.register(modEventBus);
        GrowthcraftBambooItems.ITEMS.register(modEventBus);
        GrowthcraftBambooBlockEntities.BLOCK_ENTITIES.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
    }

    public void buildCreativeTabContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTab() == GrowthcraftCreativeModeTabs.CREATIVE_TAB.get()) {
            GrowthcraftBambooItems.ITEMS.getEntries().forEach(itemRegistryObject -> {
                if (!GrowthcraftBambooItems.excludeItemRegistry(itemRegistryObject.getId())) {
                    event.accept(new ItemStack(itemRegistryObject.get()));
                }
            });
        }
    }

    private void clientSetupEvent(final FMLClientSetupEvent event) {
        // Do nothing for now ...
    }

    private void setup(final FMLCommonSetupEvent event) {
        GrowthcraftBambooItems.registerCompostables();
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info(String.format("%s starting up ...", Reference.NAME));
    }

}
