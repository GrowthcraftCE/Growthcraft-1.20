package growthcraft.milk.block;

import growthcraft.milk.init.GrowthcraftMilkItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ThistleCropBlock extends CropBlock {

    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
            Block.box(5.0F, 0.0F, 5.0F, 11.0F, 4.0F, 11.0F),
            Block.box(5.0F, 0.0F, 5.0F, 11.0F, 9.0F, 11.0F),
            Block.box(5.0F, 0.0F, 5.0F, 11.0F, 9.0F, 11.0F),
            Block.box(5.0F, 0.0F, 5.0F, 11.0F, 9.0F, 11.0F),
            Block.box(4.0F, 0.0F, 4.0F, 12.0F, 15.0F, 12.0F),
            Block.box(4.0F, 0.0F, 4.0F, 12.0F, 15.0F, 12.0F),
            Block.box(4.0F, 0.0F, 4.0F, 12.0F, 15.0F, 12.0F),
            Block.box(2.0F, 0.0F, 2.0F, 14.0F, 16.0F, 14.0F)
    };

    public ThistleCropBlock() {
        this(getInitProperties());
    }

    public ThistleCropBlock(Properties properties) {
        super(properties);
    }

    private static Properties getInitProperties() {
        Properties properties = Properties.copy(Blocks.WHEAT);
        properties.noOcclusion();
        properties.randomTicks();
        properties.instabreak();
        properties.sound(SoundType.CROP);
        return properties;
    }

    @Override
    protected @NotNull ItemLike getBaseSeedId() {
        return GrowthcraftMilkItems.THISTLE_SEED.get();
    }

    /**
     * Overrides the getShape method from CropBlock to dynamically adjust the crop's shape
     * based on its growth stage. This is necessary because SHAPE_BY_AGE is private in the
     * parent class, preventing direct access.
     *
     * @param blockState The current state of the block.
     * @param blockGetter The block access environment.
     * @param blockPos The position of the block in the world.
     * @param context The collision context, used for interaction checks.
     * @return The corresponding VoxelShape for the crop's current age.
     */
    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext context) {
        return SHAPE_BY_AGE[this.getAge(blockState)];
    }

    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.isClientSide) return super.use(blockState, level, blockPos, player, hand, hitResult);

        if (blockState.getValue(AGE) == this.getMaxAge()) {
            LootParams.Builder context = new LootParams.Builder((ServerLevel) level)
                    .withParameter(LootContextParams.ORIGIN, new Vec3(blockPos.getX(), blockPos.getY(), blockPos.getZ()))
                    .withParameter(LootContextParams.BLOCK_STATE, blockState)
                    .withParameter(LootContextParams.THIS_ENTITY, player)
                    .withOptionalParameter(LootContextParams.TOOL, player.getItemInHand(hand));

            List<ItemStack> drops = blockState.getDrops(context);

            for (ItemStack stack : drops) {
                Containers.dropItemStack(level, blockPos.getX(), blockPos.getY(), blockPos.getZ(), stack);
            }

            level.setBlock(blockPos, this.getStateForAge(1), Block.UPDATE_ALL);
        }

        return super.use(blockState, level, blockPos, player, hand, hitResult);
    }

}
