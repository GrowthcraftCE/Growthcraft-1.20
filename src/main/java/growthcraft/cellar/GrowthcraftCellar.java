package growthcraft.cellar;

import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import growthcraft.cellar.init.GrowthcraftCellarBlockEntities;
import growthcraft.cellar.init.GrowthcraftCellarBlocks;
import growthcraft.cellar.init.GrowthcraftCellarFluids;
import growthcraft.cellar.init.GrowthcraftCellarItems;
import growthcraft.cellar.init.GrowthcraftCellarMenus;
import growthcraft.cellar.init.GrowthcraftCellarRecipes;
import growthcraft.cellar.init.client.GrowthcraftCellarBlockEntityRenderers;
import growthcraft.cellar.init.client.GrowthcraftCellarBlockRenderers;
import growthcraft.cellar.init.config.GrowthcraftCellarConfig;
import growthcraft.cellar.lib.networking.GrowthcraftCellarMessages;
import growthcraft.cellar.screen.BrewKettleScreen;
import growthcraft.cellar.screen.CultureJarScreen;
import growthcraft.cellar.screen.FermentationBarrelScreen;
import growthcraft.cellar.screen.FruitPressScreen;
import growthcraft.cellar.screen.RoasterScreen;
import growthcraft.cellar.shared.Reference;
import growthcraft.core.init.GrowthcraftCreativeModeTabs;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;


@Mod(Reference.MODID)
@Mod.EventBusSubscriber(modid = Reference.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GrowthcraftCellar {
    public static final Logger LOGGER = LogManager.getLogger(Reference.MODID);
//    private static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIERS = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, Reference.MODID);
//    private static final RegistryObject<Codec<? extends IGlobalLootModifier>> GLMSerializer1 = LOOT_MODIFIERS.register("bottles_in_loot_chests", LootModifierForBottlesInChests.CODEC);

    public GrowthcraftCellar() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::setup);
        modEventBus.addListener(this::clientSetupEvent);
        modEventBus.addListener(this::buildCreativeTabContents);
        modEventBus.addListener(this::onRegisterRenderers);
        modEventBus.addListener(this::enqueueIMC);

        // Config
        GrowthcraftCellarConfig.loadConfig();

        // Blocks, Items, Fluids, Block Entities, Containers
        GrowthcraftCellarBlocks.BLOCKS.register(modEventBus);
        GrowthcraftCellarItems.ITEMS.register(modEventBus);
        GrowthcraftCellarBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        GrowthcraftCellarFluids.FLUID_TYPES.register(modEventBus);
        GrowthcraftCellarFluids.FLUIDS.register(modEventBus);
        GrowthcraftCellarMenus.MENUS.register(modEventBus);
//        GrowthcraftCellarGlobalLootModifier.register(modEventBus);

        GrowthcraftCellarRecipes.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
    }


    private void clientSetupEvent(final FMLClientSetupEvent event) {
        GrowthcraftCellarBlockRenderers.setRenderLayers();

        MenuScreens.register(
                GrowthcraftCellarMenus.BREW_KETTLE_MENU.get(),
                BrewKettleScreen::new
        );

        MenuScreens.register(
                GrowthcraftCellarMenus.CULTURE_JAR_MENU.get(),
                CultureJarScreen::new
        );

        MenuScreens.register(
                GrowthcraftCellarMenus.FERMENTATION_BARREL_MENU.get(),
                FermentationBarrelScreen::new
        );

        MenuScreens.register(
                GrowthcraftCellarMenus.FRUIT_PRESS_MENU.get(),
                FruitPressScreen::new
        );

        MenuScreens.register(
                GrowthcraftCellarMenus.ROASTER_MENU.get(),
                RoasterScreen::new)
        ;
    }

    private void setup(final FMLCommonSetupEvent event) {
        GrowthcraftCellarMessages.register();
        GrowthcraftCellarItems.registerCompostables();
    }

    public void buildCreativeTabContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTab() == GrowthcraftCreativeModeTabs.CREATIVE_TAB.get()) {
            GrowthcraftCellarItems.ITEMS.getEntries().forEach(itemRegistryObject -> {
                if (!GrowthcraftCellarItems.excludeItemRegistry(itemRegistryObject.getId())) {
                    event.accept(new ItemStack(itemRegistryObject.get()));
                }
            });
        }
    }

    //// support for CarryOn, etc.
    private void enqueueIMC(final InterModEnqueueEvent event) {
        InterModComms.sendTo("carryon", "blacklistBlock", () -> Reference.MODID + ":" + Reference.UnlocalizedName.FRUIT_PRESS);
        InterModComms.sendTo("carryon", "blacklistBlock", () -> Reference.MODID + ":" + Reference.UnlocalizedName.FRUIT_PRESS_PISTON);
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("Growthcraft Cellar starting up ...");
    }

    public void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        GrowthcraftCellarBlockEntityRenderers.register(event);
    }
}