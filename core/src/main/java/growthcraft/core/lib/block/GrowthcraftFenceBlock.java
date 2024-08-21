package growthcraft.core.lib.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.SoundType;

public class GrowthcraftFenceBlock extends FenceBlock {
    public GrowthcraftFenceBlock() {
        this(getInitProperties(Blocks.OAK_FENCE));
    }

    public GrowthcraftFenceBlock(Properties properties) {
        super(properties);
    }

    private static Properties getInitProperties(Block material) {
        Properties properties = Properties.copy(material);
        properties.mapColor(material.defaultMapColor());
        properties.sound(SoundType.WOOD);
        properties.strength(2.0F, 3.0F);
        return properties;
    }
}
