package growthcraft.lib.block;

import org.jetbrains.annotations.Nullable;

import growthcraft.apples.init.GrowthcraftApplesBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

public class GrowthcraftLogBlock extends RotatedPillarBlock {
    public GrowthcraftLogBlock() {
        this(getInitProperties(Blocks.OAK_LOG));
    }

    public GrowthcraftLogBlock(Properties properties) {
        super(properties);
    }

    private static Properties getInitProperties(Block material) {
        Properties properties = Properties.copy(material);
        properties.strength(2.0F);
        properties.sound(SoundType.WOOD);
        return properties;
    }

    @Override
    public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return true;
    }

    @Override
    public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 5;
    }

    @Override
    public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 5;
    }
    
    //since vanilla uses a proteced map, to specify the which logs turn into which stripped version, this is the best way i found. 
    //we could also do sub classes or our own map to match logs&stripped logs, but for 4 blocks, this should be enough
    @Override
    public @Nullable BlockState getToolModifiedState(BlockState state, UseOnContext context, ToolAction toolAction, boolean simulate) {
    	if (context.getItemInHand().canPerformAction(ToolActions.AXE_STRIP)) {
    		if(state.is(GrowthcraftApplesBlocks.APPLE_WOOD_LOG.get())) {
    			return GrowthcraftApplesBlocks.APPLE_WOOD_LOG_STRIPPED.get().defaultBlockState().setValue(AXIS, state.getValue(AXIS));
    		}
    		if(state.is(GrowthcraftApplesBlocks.APPLE_WOOD.get())) {
    			return GrowthcraftApplesBlocks.APPLE_WOOD_STRIPPED.get().defaultBlockState().setValue(AXIS, state.getValue(AXIS));
    		}
    	}
    	return super.getToolModifiedState(state, context, toolAction, simulate);
    }

}
