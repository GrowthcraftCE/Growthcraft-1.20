package growthcraft.core.init.config;

import com.google.gson.JsonObject;
import growthcraft.apiary.init.config.GrowthcraftApiaryConfig;
import growthcraft.apples.init.config.GrowthcraftApplesConfig;
import growthcraft.core.shared.Reference;
import growthcraft.milk.init.config.GrowthcraftMilkConfig;
import growthcraft.rice.init.config.GrowthcraftRiceConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;

/* *
* This is to be used in recipes and worlgen jsons to turn off things player does not want.
* It's a normal forge condition, like forge:mod_loaded
*
* Will be registrated as growthcraft:feature_enabled    (type)
* Value is called "feature" and can be "apples", "apiary", etc... - those are module names. if one is used (without slash), master switch will be considered.
* Also values can be something like "apiary/mead". In this case, condition passes if apiary module is enabled OR if apiary is disabled (master switch) but mead exception is enabled in config.
* Full list of sub-features:    "apiary/mead", "apiary/beverages" (alias),   "apiary/basic_wax",   "rice/beverages",   "milk/beverages"
*  */
public class OptionalFeatureCondition implements ICondition {
    public static final ResourceLocation DEFAULT_ID = new ResourceLocation(Reference.MODID, "feature_enabled"); // normally should be unknown to this, but we'll only have one type and we need to instantiate manually for datagen. doesn't matter 1.21 will do away with this.
    private final String feature;
    private final ResourceLocation conditionId; // internal; removed in 1.21

    public OptionalFeatureCondition(String feature) {
        this(DEFAULT_ID, feature);
    }

    private OptionalFeatureCondition(ResourceLocation id, String feature) {
        this.conditionId = id;
        this.feature = feature;
    }

    @Override
    public ResourceLocation getID() { return this.conditionId; }

    @Override
    public boolean test(IContext context) {
        return testModuleOrModuleFeature(this.feature);
    }

    public static boolean testModuleOrModuleFeature(String feature) {
        if (feature.equals("rice")) {
            return GrowthcraftRiceConfig.getModuleEnabled();
        }
        else if (feature.equals("rice/beverages")) {
            return GrowthcraftRiceConfig.getModuleEnabled() || GrowthcraftRiceConfig.getFeatureEnabledBeverages();
        }
        else if (feature.equals("apples")) {
            return GrowthcraftApplesConfig.getModuleEnabled();
        }
        else if (feature.equals("apiary")) {
            return GrowthcraftApiaryConfig.getModuleEnabled();
        }
        else if (feature.equals("apiary/mead") || feature.equals("apiary/beverages")) { // we have an alias here
            return GrowthcraftApiaryConfig.getModuleEnabled() || GrowthcraftApiaryConfig.getFeatureEnabledBeverages();
        }
        else if (feature.equals("apiary/basic_wax")) {
            return GrowthcraftApiaryConfig.getModuleEnabled() || GrowthcraftApiaryConfig.getFeatureEnabledBasicWax();
        }
        else if (feature.equals("cellar")) {
            return true; // NYI
        }
        else if (feature.equals("grapes")) {
            return true; // NYI. not sure if we'll be able to disable them separately from cellar. GLMs can be separate easily.
        }
        else if (feature.equals("milk")) {
            return GrowthcraftMilkConfig.getModuleEnabled();
        }
        else if (feature.equals("milk/beverages")) {
            return GrowthcraftMilkConfig.getModuleEnabled() || GrowthcraftMilkConfig.getFeatureEnabledBeverages();
        }
        else if (feature.equals("bamboo")) {
            return true; // NYI
        }
        else {
            return false;
        }
    }

    /////////////////////////////////////////////////////

    public static class Serializer implements IConditionSerializer<OptionalFeatureCondition> {
        private final ResourceLocation conditionId;

        public Serializer(ResourceLocation id)
        {
            this.conditionId = id;
        }

        @Override
        public void write(JsonObject json, OptionalFeatureCondition condition) {
            json.addProperty("feature", condition.feature);
        }

        @Override
        public OptionalFeatureCondition read(JsonObject json) {
            return new OptionalFeatureCondition(this.conditionId, json.getAsJsonPrimitive("feature").getAsString());
        }

        @Override
        public ResourceLocation getID() {
            return this.conditionId;
        }
    }
}
