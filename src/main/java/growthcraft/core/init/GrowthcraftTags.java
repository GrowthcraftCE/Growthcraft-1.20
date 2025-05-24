package growthcraft.core.init;

import growthcraft.core.shared.Reference;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

public class GrowthcraftTags {
    public static void init() {
        Blocks.init();
        Items.init();
        Fluids.init();
    }

    private GrowthcraftTags() {
        /* Private constructor to hide the implicit public one. */
    }

    public static class Blocks {

        private static void init(){
            // Do nothing, simply instantiate static variables
        }

        public static final TagKey<Block> HEATSOURCES = tag(Reference.UnlocalizedName.TAG_HEATSOURCES);

        public static final TagKey<Block> ROPE = tag(Reference.UnlocalizedName.TAG_ROPE);
        public static final TagKey<Block> SALT = tag(Reference.UnlocalizedName.TAG_SALT);
        
        //Serene Seasons Tags, we have to define them here, since they dont have them setup in 1.20.1 for datagen
        public static final TagKey<Block> SPRING_CROPS = sereneSeasonsTag(Reference.UnlocalizedName.SPRING_CROPS);
        public static final TagKey<Block> SUMMER_CROPS = sereneSeasonsTag(Reference.UnlocalizedName.SUMMER_CROPS);
        public static final TagKey<Block> AUTUMN_CROPS = sereneSeasonsTag(Reference.UnlocalizedName.AUTUMN_CROPS);
        public static final TagKey<Block> WINTER_CROPS = sereneSeasonsTag(Reference.UnlocalizedName.WINTER_CROPS);
        
        private static TagKey<Block> tag(String name) {
            return BlockTags.create(new ResourceLocation(Reference.MODID, name));
        }
        private static TagKey<Block> sereneSeasonsTag(String name) {
            return BlockTags.create(new ResourceLocation("sereneseasons", name));
        }
    }

    public static class Items {

        private static void init(){
            // Do nothing, simply instantiate static variables
        }

        public static final TagKey<Item> ROASTER_WRENCH = tag(Reference.UnlocalizedName.TAG_ROASTER_WRENCH);

        public static final TagKey<Item> SALT = tag(Reference.UnlocalizedName.TAG_SALT);
        public static final TagKey<Item> SALT_FORGE = forgeTag(Reference.UnlocalizedName.TAG_SALT);
        public static final TagKey<Item> DUSTS_SALT = forgeTag(Reference.UnlocalizedName.DUSTS_SALT);
        public static final TagKey<Item> TAG_KNIFE = forgeTag(Reference.UnlocalizedName.TAG_KNIVES);
        
        //Serene Seasons Tags, we have to define them here, since they dont have them setup in 1.20.1 for datagen
        public static final TagKey<Item> SPRING_CROPS = sereneSeasonsTag(Reference.UnlocalizedName.SPRING_CROPS);
        public static final TagKey<Item> SUMMER_CROPS = sereneSeasonsTag(Reference.UnlocalizedName.SUMMER_CROPS);
        public static final TagKey<Item> AUTUMN_CROPS = sereneSeasonsTag(Reference.UnlocalizedName.AUTUMN_CROPS);
        public static final TagKey<Item> WINTER_CROPS = sereneSeasonsTag(Reference.UnlocalizedName.WINTER_CROPS);

        private static TagKey<Item> tag(String name) {
            return ItemTags.create(new ResourceLocation(Reference.MODID, name));
        }
        private static TagKey<Item> forgeTag(String name) {
            return ItemTags.create(new ResourceLocation("forge", name));
        }
        private static TagKey<Item> sereneSeasonsTag(String name) {
            return ItemTags.create(new ResourceLocation("sereneseasons", name));
        }
    }

    public static class Fluids {
        private static void init() {
            // Do nothing, simply instantiate static variables
        }

        private static TagKey<Fluid> tag(String name) {
            return FluidTags.create(new ResourceLocation(Reference.MODID, name));
        }
    }
}
