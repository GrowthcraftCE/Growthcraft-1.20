package growthcraft.apiary.init;

import growthcraft.apiary.shared.Reference;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

public class GrowthcraftApiaryTags {

    public static void init() {
        Blocks.init();
        Items.init();
        Fluids.init();
        EntityTypes.init();
    }

    public static class Blocks {

        public static void init() {
            // Do nothing, simply instantiate static variables
        }

        //public static final TagKey<Block> HEATSOURCES = tag(Reference.UnlocalizedName.TAG_HEATSOURCES);

        private static TagKey<Block> tag(String name) {
            return BlockTags.create(new ResourceLocation(Reference.MODID, name));
        }
    }

    public static class Items {

        public static void init() {
            // Do nothing, simply instantiate static variables
        }

        public static final TagKey<Item> BEE = tag(Reference.UnlocalizedName.BEE);
        public static final TagKey<Item> BEES_WAX = tag(Reference.UnlocalizedName.BEES_WAX);
        public static final TagKey<Item> HONEY_COMB = tag(Reference.UnlocalizedName.HONEY_COMB);

        private static TagKey<Item> tag(String name) {
            return ItemTags.create(new ResourceLocation(Reference.MODID, name));
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

    public static class EntityTypes {

        private static void init() {
            // Do nothing, simply instantiate static variables
        }

        //public static final TagKey<EntityType<?>> MILKABLE = tag("milkable");

        //private static TagKey<EntityType<?>> tag(String name) {
        //    return TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(Reference.MODID, name));
        //}
    }
}
