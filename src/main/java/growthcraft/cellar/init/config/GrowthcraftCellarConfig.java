package growthcraft.cellar.init.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;

public class GrowthcraftCellarConfig {

    public static final ForgeConfigSpec.Builder SERVER_BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SERVER;
    public static final String SERVER_CONFIG = "growthcraft-cellar-server.toml";

    private static ForgeConfigSpec.BooleanValue secondaryAdjunctGrainsAllowed;

    static {
        initLootConfig(SERVER_BUILDER);
        SERVER = SERVER_BUILDER.build();
    }

    private GrowthcraftCellarConfig() {
        /* Disable default public constructor */
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

    public static void initLootConfig(ForgeConfigSpec.Builder builder) {
        builder.push("brewing");
        secondaryAdjunctGrainsAllowed = builder
                .comment("Do we allow rice and corn as adjunct grains")
                .define("allow_additional_adjunct_grains", false);
        builder.pop();
    }

    // values can be pulled via recipe conditions. if this is gray, doesn't mean it is unused.
    public static boolean isSecondaryAdjunctGrainsAllowed() { return secondaryAdjunctGrainsAllowed.get(); }
}
