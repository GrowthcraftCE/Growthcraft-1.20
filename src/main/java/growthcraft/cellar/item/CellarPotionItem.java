package growthcraft.cellar.item;

import java.util.List;

import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import growthcraft.lib.item.GrowthcraftItem;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

public class CellarPotionItem extends GrowthcraftItem {

    private static final int DRINK_DURATION = 32;

    public CellarPotionItem() {
        super(16);
    }

    // TODO[8]: Review custom potions for 1.19
    @Override
    public @NotNull ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        Player player = livingEntity instanceof Player ? (Player) livingEntity : null;
        if(player instanceof ServerPlayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer) player, stack);
        }

        if(!level.isClientSide) {
            CompoundTag compoundTag = stack.getTag();
            if(compoundTag != null && compoundTag.contains("CustomPotionEffects", 9)) {
                ListTag customPotionEffects = compoundTag.getList("CustomPotionEffects", 10);

                for(int i = 0; i < customPotionEffects.size(); i++) {
                    CompoundTag tag = customPotionEffects.getCompound(i);
                    MobEffectInstance effect = MobEffectInstance.load(tag);
                    player.addEffect(effect);
                }
            }
        }

        if(player != null) {
            player.awardStat(Stats.ITEM_USED.get(this));
            if(!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }

        if(player == null || !player.getAbilities().instabuild) {
            if(stack.isEmpty()) {
                return new ItemStack(Items.GLASS_BOTTLE);
            }
            if(player != null) {
                player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE));
            }
        }

        livingEntity.gameEvent(GameEvent.DRINK);
        return stack;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 32;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        return ItemUtils.startUsingInstantly(level, player, hand);
    }

    @Override
    public @NotNull String getDescriptionId(ItemStack stack) {
        return this.getDescriptionId() + ".effect.empty";  // barrel interaction will override in (barrel recipe class)
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag tooltipFlag) {
        PotionUtils.addPotionTooltip(stack, tooltip, 1.0F);
    }

    @Override
    public int getColor(ItemStack itemStack, int layer) {
    	CompoundTag tag = itemStack.getTag();
        if (layer == 0)
        {
            // layer == 0 means liquid
            return tag != null ? itemStack.getTag().getInt("color") : 0xFFFFFF; // no reasonable color if one is missing.
        }
        else
        {
            // layer == 1 means foam
            return 0xFFFFFF;
        }
    }
}
