package growthcraft.milk.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class CheeseWheelProcessedBlock extends BaseCheeseWheel {

    public CheeseWheelProcessedBlock(Cheese variant) {
        super(variant);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (level.isClientSide()) return InteractionResult.CONSUME;

        // handle taking slices
        if (!player.isCrouching() && player.getItemInHand(interactionHand).isEmpty()) {
            BlockState newBlock = takeSlice(blockState, 1);
            player.getInventory().add(getVariant().getSlices(1));
            level.setBlockAndUpdate(blockPos, newBlock);
            if (getSliceCount(newBlock) == 0) level.destroyBlock(blockPos, false);
            return InteractionResult.SUCCESS;
        }

        return super.use(blockState, level, blockPos, player, interactionHand, blockHitResult);
    }
}
