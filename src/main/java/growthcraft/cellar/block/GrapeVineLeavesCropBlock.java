package growthcraft.cellar.block;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import growthcraft.cellar.init.GrowthcraftCellarBlocks;
import growthcraft.core.block.RopeBlock;
import growthcraft.core.block.entity.RopeBlockEntity;
import growthcraft.core.init.GrowthcraftBlocks;
import growthcraft.core.init.GrowthcraftTags;
import growthcraft.core.utils.BlockPropertiesUtils;
import growthcraft.lib.block.GrowthcraftCropsRopeBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class GrapeVineLeavesCropBlock extends GrowthcraftCropsRopeBlock {

    protected static final VoxelShape[] CUSTOM_SHAPE_BY_AGE = new VoxelShape[]{
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D)};
    private final GrapeVineFruitBlock grapeVineFruitBlock;

    public GrapeVineLeavesCropBlock(GrapeVineFruitBlock grapeVineFruitBlock) {
        super(getInitProperties());
        this.grapeVineFruitBlock = grapeVineFruitBlock;
    }

    public static Properties getInitProperties() {
        BlockBehaviour.Properties properties = BlockBehaviour.Properties.copy(Blocks.WHEAT);
        properties.noCollission();
        properties.randomTicks();
        properties.strength(0.3F, 0.3F);
        properties.sound(SoundType.CROP);
        properties.isValidSpawn(BlockPropertiesUtils::never);
        properties.isRedstoneConductor(BlockPropertiesUtils::never);
        properties.isSuffocating(BlockPropertiesUtils::never);
        properties.isViewBlocking(BlockPropertiesUtils::never);
        return properties;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext context) {
        return CUSTOM_SHAPE_BY_AGE[state.getValue(AGE)];
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        this.tryGrow(state, level, pos, random.nextInt(2));

        if (state.getValue(AGE) == this.getMaxAge()) {
            Map<BlockPos, BlockState> surroundingBlockStates = new HashMap<>();
            surroundingBlockStates.put(pos.north(), level.getBlockState(pos.north()));
            surroundingBlockStates.put(pos.east(), level.getBlockState(pos.east()));
            surroundingBlockStates.put(pos.south(), level.getBlockState(pos.south()));
            surroundingBlockStates.put(pos.west(), level.getBlockState(pos.west()));

            // Randomly check the surrounding block states.
            List<BlockPos> directions = new ArrayList<>(surroundingBlockStates.keySet());
            Collections.shuffle(directions);

            for (BlockPos direction : directions) {
                if (surroundingBlockStates.get(direction).getBlock() instanceof RopeBlock) {
                    // If the block is a RopeBlock then it will have a RopeBlockEntity.
                    RopeBlockEntity ropeBlockEntity = (RopeBlockEntity) level.getBlockEntity(direction);
                    if (ropeBlockEntity == null || !ropeBlockEntity.hasFenceItemStack()) {
                        // If the RopeBlockEntity does not have a FenceItemStack, then it is a simple rope block.
                        BlockState newBlockState = state.getBlock().defaultBlockState();
                        newBlockState.setValue(NORTH, level.getBlockState(direction.north()).is(GrowthcraftTags.Blocks.ROPE));
                        newBlockState.setValue(EAST, level.getBlockState(direction.east()).is(GrowthcraftTags.Blocks.ROPE));
                        newBlockState.setValue(SOUTH, level.getBlockState(direction.south()).is(GrowthcraftTags.Blocks.ROPE));
                        newBlockState.setValue(WEST, level.getBlockState(direction.west()).is(GrowthcraftTags.Blocks.ROPE));
                        newBlockState.setValue(UP, level.getBlockState(direction.above()).is(GrowthcraftTags.Blocks.ROPE));
                        newBlockState.setValue(DOWN, level.getBlockState(direction.below()).is(GrowthcraftTags.Blocks.ROPE));

                        level.setBlock(direction, newBlockState, Block.UPDATE_ALL);
                        return;
                    }
                }
            }

            if (level.getBlockState(pos.below()).isAir()) {
                // If the below block is air, then spawn a GrapeVineFruitBlock.
                level.setBlock(pos.below(), this.getGrapeVineFruitBlock().defaultBlockState(), Block.UPDATE_ALL);
            }
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        return InteractionResult.PASS;
    }

    public GrapeVineFruitBlock getGrapeVineFruitBlock() {
        return this.grapeVineFruitBlock;
    }
    
    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
    	 for (Direction direction : Direction.values()) {
    		 BlockPos adjacentPos = pPos.relative(direction);
    	     if(pLevel.getBlockState(adjacentPos).is(this)){
    	    	 return true;
    	     }
    	 }
    	return false;
    }
    
    //similar to the GrowthcraftCropsRopeBlock#onRemove. Key Difference, a vine leave can only ever grow on rope, the planted base is a different block
    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
    	super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    	if(!pNewState.is(pState.getBlock())) {
    		pLevel.setBlock(pPos, ((RopeBlock)GrowthcraftBlocks.ROPE_LINEN.get()).getActualBlockState(pLevel, pPos), UPDATE_ALL);
    	}
    }
}
