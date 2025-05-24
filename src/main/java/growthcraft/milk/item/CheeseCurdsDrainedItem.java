package growthcraft.milk.item;

import growthcraft.lib.item.GrowthcraftItem;
import growthcraft.milk.init.GrowthcraftMilkItems;
import net.minecraft.world.item.ItemStack;

import java.awt.*;

public class CheeseCurdsDrainedItem extends GrowthcraftItem {

    private final int color;

    public CheeseCurdsDrainedItem(Color color) {
        super();
        this.color = color.getRGB();
    }

    public int getColor() {
        return this.color;
    }

    public int getColor(int colorIndex) {
        return colorIndex == 0 ? this.color : 0xFFFFFF;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemStack)
    {
        return GrowthcraftMilkItems.CHEESE_CLOTH.get().getDefaultInstance();
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack)
    {
        return true;
    }
}
