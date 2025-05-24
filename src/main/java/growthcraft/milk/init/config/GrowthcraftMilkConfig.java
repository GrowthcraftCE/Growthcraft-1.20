package growthcraft.milk.init.config;

import java.io.File;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.loading.FMLPaths;

public class GrowthcraftMilkConfig {

    public static final ForgeConfigSpec.Builder SERVER_BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SERVER;

    public static final String SERVER_CONFIG = "growthcraft-milk-server.toml";

    private static final String CATEGORY_CHURN = "churn";
    private static final String CATEGORY_MIXING_VAT = "mixing_vat";
    private static final String CATEGORY_PANCHEON = "pancheon";
    private static final String CATEGORY_LOOT_CHANCES = "loot_modifiers";
    private static final String CATEGORY_WORLDGEN = "worldgen";
    private static final String CATEGORY_CHEESE = "cheese";

    private static ForgeConfigSpec.BooleanValue moduleEnabled;
    private static ForgeConfigSpec.BooleanValue featureEnabledBeverages;

    private static ForgeConfigSpec.BooleanValue churnGuiEnabled;
    private static ForgeConfigSpec.BooleanValue mixingVatGuiEnabled;
    private static ForgeConfigSpec.BooleanValue mixingVatDebugEnabled;

    private static ForgeConfigSpec.BooleanValue mixingVatConsumeActivationItem;
    private static ForgeConfigSpec.BooleanValue pancheonGuiEnabled;
    private static ForgeConfigSpec.BooleanValue stomachLootEnabled;
    private static ForgeConfigSpec.IntValue stomachLootChance;
    private static ForgeConfigSpec.IntValue cheeseAgeTime;

    private static ForgeConfigSpec.BooleanValue cheeseDebugEnabled;

    static {
        initServerConfig(SERVER_BUILDER);
        SERVER = SERVER_BUILDER.build();
    }

    private GrowthcraftMilkConfig() {
        /* Prevent generation of public constructor */
    }

    public static void loadConfig() {
        loadConfig(SERVER, FMLPaths.CONFIGDIR.get().resolve(SERVER_CONFIG).toString());
    }

    public static void loadConfig(ForgeConfigSpec configSpec, String path) {
        final CommentedFileConfig fileConfig = CommentedFileConfig.builder(
                new File(path)).sync().autosave().writingMode(WritingMode.REPLACE).build();

        fileConfig.load();
        configSpec.setConfig(fileConfig);
    }

    public static void initServerConfig(ForgeConfigSpec.Builder specBuilder) {
        // best config flag here....
        moduleEnabled = specBuilder
                .comment("This master-switch lets you disable the entire Rice module - seeds dropping from grass, farming tool and products.")
                .define("_master_switch_.module_enabled", true);
        featureEnabledBeverages = specBuilder
                .comment("In case master-switch is turned off, this exception allows you to still make beverages from this module's fluids.")
                .define("_master_switch_.feature_exception_beverages", true);
        // good stuff done

        // Init Server Side Configuration
        churnGuiEnabled = specBuilder
                .comment("Set to true to allow users to access the Churn GUI.")
                .define(String.format("%s.%s", CATEGORY_CHURN, "guiEnabled"), true);
        mixingVatGuiEnabled = specBuilder
                .comment("Set to true to allow users to access the Mixing Vat GUI.")
                .define(String.format("%s.%s", CATEGORY_MIXING_VAT, "guiEnabled"), true);
        mixingVatDebugEnabled = specBuilder
                .comment("Set to true to add additional logging to debug the missing vat.")
                .define(String.format("%s.%s", CATEGORY_MIXING_VAT, "debugEnabled"), false);
        mixingVatConsumeActivationItem = specBuilder
                .comment("Set to true to allow users to access the Mixing Vat GUI.")
                .define(String.format("%s.%s", CATEGORY_MIXING_VAT, "consumeMixingVatActivator"), false);
        pancheonGuiEnabled = specBuilder
                .comment("Set to true to allow users to access the Pancheon GUI.")
                .define(String.format("%s.%s", CATEGORY_PANCHEON, "guiEnabled"), true);
        stomachLootEnabled = specBuilder
                .comment("Set to true to enable looting of stomach from cows.")
                .define(String.format("%s.%s", CATEGORY_LOOT_CHANCES, "stomachLootEnabled"), true);

        stomachLootChance = specBuilder
                .comment("Chance to loot a stomach from a cow. stomachLootEnabled must be set to true.")
                .defineInRange(String.format("%s.%s", CATEGORY_LOOT_CHANCES, "stomachLootChance"), 5, 0, 100);

        cheeseDebugEnabled = specBuilder
                .comment("Set to true to add additional logging to debug the cheese wheel and curds blocks.")
                .define(String.format("%s.%s", CATEGORY_CHEESE, "debugEnabled"), false);
        
        cheeseAgeTime = specBuilder
		        .comment("Amount of random ticks it takes, for a cheese wheel to age. One random tick happens every ~1min.")
		        .defineInRange(String.format("%s.%s", CATEGORY_CHEESE, "cheeseAgeTime"), 60, 0, 240);

    }

    /**
     * Checks if the Churn GUI is enabled.
     * TODO: This is intended to be able to implement a hard mode. Will require non-GUI
     *       block interaction to be implemented.
     *
     * @return true if the Churn GUI is enabled, false otherwise.
     */
    public static boolean isChurnGuiEnabled() {
        return churnGuiEnabled.get();
    }

    /**
     * Checks if the Pancheon GUI is enabled.
     * TODO: This is intended to be able to implement a hard mode. Will require non-GUI
     *       block interaction to be implemented.
     *
     * @return true if the Pancheon GUI is enabled, false otherwise.
     */
    public static boolean isPancheonGuiEnabled() {
        return pancheonGuiEnabled.get();
    }

    /**
     * Checks if the Mixing Vat GUI is enabled.
     * TODO: This is intended to be able to implement a hard mode. Will require non-GUI
     *       block interaction to be implemented.
     *
     * @return true if the Mixing Vat GUI is enabled, false otherwise.
     */
    public static boolean isMixingVatGuiEnabled() {
        return mixingVatGuiEnabled.get();
    }

    /**
     * Checks if the debugging mode for the Mixing Vat is enabled.
     *
     * @return true if the debugging mode is enabled, false otherwise.
     */
    public static boolean isMixingDebugEnabled() {
        return mixingVatDebugEnabled.get();
    }

    /**
     * Checks if the consume mixing vat activator feature is enabled.
     *
     * @return true if the consume mixing vat activator is enabled, false otherwise.
     */
    public static boolean isConsumeMixingVatActivator() {
        return mixingVatConsumeActivationItem.get();
    }

    /**
     * Checks if looting of stomach from cows is enabled. If disabled, then rennet is only
     * made from thistle.
     *
     * @return true if stomach looting is enabled, false otherwise.
     */
    public static boolean isStomachLootingEnabled() {
        return stomachLootEnabled.get();
    }

    /**
     * Retrieves the chance to loot a stomach from a cow. If stomachLootEnabled is false
     * then automatically sets it to 0 chance.
     *
     * @return The chance to loot a stomach as an integer value, ranging from 0 to 100.
     */
    public static int getStomachLootChance() {
        return Boolean.TRUE.equals(stomachLootEnabled.get()) ? stomachLootChance.get() : 0;
    }

    /**
     * Checks if the debugging mode for the cheese wheel and curds blocks is enabled.
     *
     * @return true if the debugging mode is enabled, false otherwise.
     */
    public static boolean isCheeseDebugEnabled() {
        return cheeseDebugEnabled.get();
    }

    /**
     * exception: allow beverages even if the module is disabled
     */
    public static boolean getFeatureEnabledBeverages() { return featureEnabledBeverages.get(); }
    
    /**
     * Retrieves the number of random ticks it takes, for a cheese wheel to age. 
     *
     * @return the amount of random ticks as an integer value, ranging from 0 to 240.
     */
    public static int getCheeseAgeTickTime() {
        return cheeseAgeTime.get();
    }

    /**
     * is the whole module disabled
     */
    public static boolean getModuleEnabled() { return moduleEnabled.get(); }
}
