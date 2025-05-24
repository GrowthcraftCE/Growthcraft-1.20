package growthcraft.cellar.block;

import growthcraft.cellar.init.GrowthcraftCellarBlocks;
import growthcraft.cellar.init.GrowthcraftCellarItems;
import growthcraft.core.block.RopeBlock;
import growthcraft.core.init.GrowthcraftTags;
import growthcraft.lib.block.GrowthcraftCropsRopeBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HopsCropBlock extends GrowthcraftCropsRopeBlock {

    protected static final VoxelShape SHAPE_SEEDLING = Block.box(6.0D, 0.0D, 6.0D, 10.0D, 5.0D, 10.0D);
    protected static final VoxelShape SHAPE_SEEDLING_AND_ROPE = Shapes.or(KNOT_BOUNDING_BOX, SHAPE_SEEDLING);
    protected static final VoxelShape SHAPE_POST = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 16.0D, 12.0D);

    public HopsCropBlock() {
        super();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        if (state.getValue(AGE) < 4) {
            return SHAPE_SEEDLING;
        }
        else {
            return SHAPE_POST;
        }
    }

    @Override
    public @NotNull ItemStack getCloneItemStack(BlockGetter blockGetter, BlockPos blockPos, BlockState state) {
        return new ItemStack(this.getBaseSeedId());
    }

    protected ItemLike getBaseSeedId() {
        return GrowthcraftCellarItems.HOPS_SEED.get();
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.isClientSide) {
            return InteractionResult.PASS;
        }

        if (state.getValue(AGE) == this.getMaxAge()) {
            // Use the loot table to determine drops.
            LootParams.Builder context = new LootParams.Builder((ServerLevel) level)
                    .withParameter(LootContextParams.ORIGIN, new Vec3(pos.getX(), pos.getY(), pos.getZ()))
                    .withParameter(LootContextParams.BLOCK_STATE, state)
                    .withParameter(LootContextParams.THIS_ENTITY, player)
                    .withOptionalParameter(LootContextParams.TOOL, player.getItemInHand(hand));

            List<ItemStack> drops = state.getDrops(context);

            for (ItemStack stack : drops) {
                Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack);
            }

            // Hops grow fast, so simply decrease the age by 1
            level.setBlock(pos, this.getStateForAge(level, pos, this.getMaxAge() - 1), Block.UPDATE_ALL);
        }

        return super.use(state, level, pos, player, hand, hitResult);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.randomTick(state, level, pos, random);
        this.tryGrowNewHopsVine(level, pos);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state, boolean isClientSide) {
        return super.isValidBonemealTarget(level, pos, state, isClientSide) || level.getBlockState(pos.above()).getBlock() instanceof RopeBlock;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        super.performBonemeal(level, random, pos, state);
        this.tryGrowNewHopsVine(level, pos);
    }

    private void tryGrowNewHopsVine(ServerLevel level, BlockPos pos) {
        BlockState blockState = level.getBlockState(pos);
        BlockState blockStateAbove = level.getBlockState(pos.above());

        if (this.isMaxAge(blockState)
                && blockStateAbove.is(GrowthcraftTags.Blocks.ROPE)
                && !(blockStateAbove.getBlock() instanceof HopsCropBlock)) {
            level.setBlock(pos.above(), GrowthcraftCellarBlocks.HOPS_VINE.get().defaultBlockState(), Block.UPDATE_ALL);
        }
    }

}
