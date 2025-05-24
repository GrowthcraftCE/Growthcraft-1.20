package growthcraft.cellar.block;

import org.jetbrains.annotations.Nullable;

import growthcraft.cellar.init.GrowthcraftCellarBlocks;
import growthcraft.cellar.init.GrowthcraftCellarItems;
import growthcraft.lib.block.GrowthcraftLogBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

public class CorkLog extends GrowthcraftLogBlock{
	public static final BooleanProperty REGROW = BooleanProperty.create("regrow");
	
	@Override
	public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
		if (pRandom.nextInt((int)(25.0F / 2) + 1) == 0){
			pLevel.setBlockAndUpdate(pPos, GrowthcraftCellarBlocks.CORK_WOOD_LOG.get().defaultBlockState().setValue(AXIS, pState.getValue(AXIS)).setValue(REGROW, pState.getValue(REGROW)));
		}
	}

	@Override
	public boolean isRandomlyTicking(BlockState pState) {
		return pState.is(GrowthcraftCellarBlocks.CORK_WOOD_LOG_STRIPPED.get()) && pState.getValue(REGROW);
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> blockStateBuilder) {
	   super.createBlockStateDefinition(blockStateBuilder.add(REGROW));
	}
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext pContext) {
		return this.defaultBlockState().setValue(AXIS, pContext.getClickedFace().getAxis()).setValue(REGROW, false);
	}
	
	@Override
	public @Nullable BlockState getToolModifiedState(BlockState state, UseOnContext context, ToolAction toolAction, boolean simulate) {
	    if (context.getItemInHand().canPerformAction(ToolActions.AXE_STRIP)) {
	    	if(state.is(GrowthcraftCellarBlocks.CORK_WOOD_LOG.get())) {
	    		popResource(context.getLevel(), context.getClickedPos().relative(context.getClickedFace(), 1), new ItemStack(GrowthcraftCellarItems.CORK_BARK.get()));
	    		return GrowthcraftCellarBlocks.CORK_WOOD_LOG_STRIPPED.get().defaultBlockState().setValue(AXIS, state.getValue(AXIS)).setValue(REGROW, state.getValue(REGROW));
	    	}
	    	if(state.is(GrowthcraftCellarBlocks.CORK_WOOD.get())) {
	    		popResource(context.getLevel(), context.getClickedPos().relative(context.getClickedFace(), 1), new ItemStack(GrowthcraftCellarItems.CORK_BARK.get()));
	    		return GrowthcraftCellarBlocks.CORK_WOOD_STRIPPED.get().defaultBlockState().setValue(AXIS, state.getValue(AXIS)).setValue(REGROW, state.getValue(REGROW));
	    	}
	    }
	return super.getToolModifiedState(state, context, toolAction, simulate);
	}
}
