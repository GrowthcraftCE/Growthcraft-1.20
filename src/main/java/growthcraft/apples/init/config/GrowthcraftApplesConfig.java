package growthcraft.apples.init.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;

public class GrowthcraftApplesConfig {

    public static final ForgeConfigSpec.Builder SERVER_BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SERVER;

    public static final String SERVER_CONFIG = "growthcraft-apples-server.toml";

    private static ForgeConfigSpec.BooleanValue moduleEnabled;
    private static ForgeConfigSpec.IntValue appleCheckArea;
    private static ForgeConfigSpec.IntValue maxAppleInArea;
    
    private static final String CATEGORY_APPLE = "apple";

    static {
        initServerConfig(SERVER_BUILDER);
        SERVER = SERVER_BUILDER.build();
    }

    private GrowthcraftApplesConfig() {
        /* Prevent generation of public constructor */
    }

    public static void loadConfig() {
        loadConfig(SERVER, FMLPaths.CONFIGDIR.get().resolve(SERVER_CONFIG).toString());
    }

    private static void loadConfig(ForgeConfigSpec configSpec, String path) {
        final CommentedFileConfig fileConfig = CommentedFileConfig.builder(
                new File(path)).sync().autosave().writingMode(WritingMode.REPLACE).build();

        fileConfig.load();
        configSpec.setConfig(fileConfig);
    }

    private static void initServerConfig(ForgeConfigSpec.Builder builder) {
        builder.push("_master_switch_");
        moduleEnabled = builder
                .comment("This master-switch lets you disable the Apples module - currently just apple trees and wooden blocks. Apples themselves or apple cider are not affected.")
                .define("module_enabled", true);
        builder.pop();  // master_switch
        
        appleCheckArea = builder
        		.comment("Size of area, in which apples can grow on an apple tree. Default: 3 Range 1-10")
        		.defineInRange(String.format("%s.%s", CATEGORY_APPLE, "appleCheckArea"), 3, 1, 10);
        
        maxAppleInArea = builder
        		.comment("amount of apples that can grow in a given area. Default: 2, Range 1-10")
        		.defineInRange(String.format("%s.%s", CATEGORY_APPLE, "maxAppleInArea"), 2, 1, 10);
    }

    ////////////////////////////////////////////////////////

    public static Boolean getModuleEnabled()
    {
        return moduleEnabled.get();
    }
    
    public static int getAppleCheckArea() {
    	return appleCheckArea.get();
    }
    
    public static int getMaxAppleInArea() {
    	return maxAppleInArea.get();
    }
}
