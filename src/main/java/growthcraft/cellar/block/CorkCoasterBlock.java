package growthcraft.cellar.block;

import growthcraft.cellar.block.entity.CorkCoasterBlockEntity;
import growthcraft.cellar.init.GrowthcraftCellarBlockEntities;
import growthcraft.core.utils.BlockPropertiesUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CorkCoasterBlock extends BaseEntityBlock {
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final BooleanProperty ITEM = BooleanProperty.create("item");
	private static VoxelShape SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 2.0D, 15.0D);
	private static VoxelShape SHAPE_WITH_BOTTLE = Shapes.or(Block.box(5.0D, 0.0D, 5.0D, 11.0D, 16.0D, 11.0D), SHAPE);
	
	public CorkCoasterBlock() {
		this(getInitProperties());
	}
	
	protected CorkCoasterBlock(Properties pProperties) {
		super(pProperties);
	}

	private static Properties getInitProperties() {
		Properties properties = BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS);
        properties.strength(1.5F);
        properties.noOcclusion();
        properties.isValidSpawn(BlockPropertiesUtils::never);
        properties.isRedstoneConductor(BlockPropertiesUtils::never);
        properties.isViewBlocking(BlockPropertiesUtils::never);
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
	
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
    	return state.getValue(ITEM).booleanValue() ? SHAPE_WITH_BOTTLE : SHAPE;
    }
	
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
	        return defaultBlockState()
	        		.setValue(FACING, context.getHorizontalDirection().getOpposite())
	        		.setValue(ITEM, Boolean.FALSE);
    }
    
    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        // allow the coaster to be on solid blocks or at least blocks with solid top center.
        BlockPos belowPos = pPos.below();
        BlockState below = pLevel.getBlockState(belowPos);
        if (below.getBlock() instanceof StairBlock && below.getValue(StairBlock.HALF).equals(Half.BOTTOM)) {
            return false; // logic below fails to get top area of stairs properly, and it's annoying.
        }
        VoxelShape top = below.getFaceOcclusionShape(pLevel, belowPos, Direction.UP);
        boolean belowHas8x8Support = top.min(Direction.Axis.X) <= 4/16d && top.max(Direction.Axis.X) >= 12/16d && top.min(Direction.Axis.Z) <= 4/16d && top.max(Direction.Axis.Z) >= 12/16d;
        return belowHas8x8Support && super.canSurvive(pState, pLevel, pPos);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(FACING).add(ITEM));
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		return GrowthcraftCellarBlockEntities.CORK_COASTER_BLOCK_ENTITY.get().create(pPos, pState);
	}

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }
	
    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand,  BlockHitResult pHit) {
		int slot = 0;
		CorkCoasterBlockEntity blockEntity = (CorkCoasterBlockEntity) pLevel.getBlockEntity(pPos);
        ItemStack itemInHand = pPlayer.getItemInHand(pHand);
         	ItemStack slotStack = blockEntity.getItem(slot);
            if ((!itemInHand.isEmpty() && blockEntity.canPlaceItem(slot, itemInHand)) || !slotStack.isEmpty() && (itemInHand.isEmpty() || blockEntity.canPlaceItem(slot, itemInHand))) {
            	blockEntity.setItem(slot, itemInHand);
            	pPlayer.setItemInHand(pHand, slotStack);

                pState = pState.setValue(ITEM, !blockEntity.isEmpty());
            	pLevel.setBlock(pPos, pState, UPDATE_ALL);
                return InteractionResult.SUCCESS; 
            }
    	return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }
    
    @Override
    public PushReaction getPistonPushReaction(BlockState blockState) {
        return PushReaction.DESTROY;
    }
}
