package growthcraft.milk.init;

import growthcraft.milk.init.config.GrowthcraftMilkConfig;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.security.SecureRandom;
import java.util.Collection;

public class LivingDropLootModifier {
    @SubscribeEvent
    public void onLivingDropsEvent(LivingDropsEvent event) {
        LivingEntity entity = event.getEntity();
        Level level = entity.level();

        if (!level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
            return;
        }

        if (entity instanceof Cow) {
            onCowLivingDrops(event, entity);
        }
    }

    private void onCowLivingDrops(LivingDropsEvent event, LivingEntity cow) {
        if (GrowthcraftMilkConfig.getModuleEnabled() && GrowthcraftMilkConfig.isStomachLootingEnabled())
        {
            int chance = GrowthcraftMilkConfig.getStomachLootChance();
            if (event.getEntity().level().getRandom().nextInt(100) <= chance)
            {
                event.getDrops().add(
                        new ItemEntity(
                                event.getEntity().level(),
                                cow.getX(), cow.getY(), cow.getZ(),
                                new ItemStack(GrowthcraftMilkItems.STOMACH.get())
                        )
                );
            }
        }
    }
}
