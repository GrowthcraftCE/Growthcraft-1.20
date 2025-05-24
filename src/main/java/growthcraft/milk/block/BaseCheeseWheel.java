package growthcraft.milk.block;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import org.jetbrains.annotations.Nullable;

import growthcraft.apiary.init.GrowthcraftApiaryItems;
import growthcraft.milk.block.entity.CheeseWheelBlockEntity;
import growthcraft.milk.init.GrowthcraftMilkBlockEntities;
import growthcraft.milk.init.GrowthcraftMilkBlocks;
import growthcraft.milk.init.GrowthcraftMilkItems;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.registries.RegistryObject;

public class BaseCheeseWheel extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final IntegerProperty SLICE_COUNT_TOP = IntegerProperty.create("slicestop", 0, 4);
    public static final IntegerProperty SLICE_COUNT_BOTTOM = IntegerProperty.create("slicesbottom", 0, 4);

    public static final VoxelShape BOUNDING_BOX = Block.box(
            1.00F, 0.0F, 1.0F,
            15.0F, 16.0F, 15.0F
    );

    public static final VoxelShape BOUNDING_BOX_HALF = Block.box(
            1.00F, 0.0F, 1.0F,
            15.0F, 8.0F, 15.0F
    );

    private final Cheese variant;


    public BaseCheeseWheel(Cheese variant) {
        super(getInitProperties());
        this.variant = variant;
    }

    public static Properties getInitProperties() {
        Properties properties = Properties.copy(Blocks.CAKE);
        properties.noOcclusion();
        return properties;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return GrowthcraftMilkBlockEntities.CHEESE_WHEEL_BLOCK_ENTITY.get().create(blockPos, blockState);
    }
    
    //randomTick is called once every ~1min. there is no guarantee that its exactly every 60sec.
    //the value of CheeseWheelBlockEntity#maxTick should reflect this. default: 60
    @Override
	public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
    	CheeseWheelBlockEntity blockEntity = (CheeseWheelBlockEntity) pLevel.getBlockEntity(pPos);
    	blockEntity.ageCheese(pLevel, pPos, pState, blockEntity);
    }
    

    @Override
    public RenderShape getRenderShape(BlockState p_60550_) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext context) {
        return blockState.getValue(SLICE_COUNT_TOP) > 0
                ? BOUNDING_BOX
                : BOUNDING_BOX_HALF;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        super.createBlockStateDefinition(stateBuilder.add(FACING, SLICE_COUNT_BOTTOM, SLICE_COUNT_TOP));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {

        int slicesTop = 0;
        int slicesBottom = 4;
        try {
            CompoundTag nbt = Objects.requireNonNull(context.getItemInHand().getTag().getCompound("BlockEntityTag"));
            slicesTop = nbt.getInt("slicestop");
            slicesBottom = nbt.getInt("slicesbottom");
        } catch (NullPointerException ignored) {}

        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(SLICE_COUNT_BOTTOM, slicesBottom)
                .setValue(SLICE_COUNT_TOP, slicesTop);
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState p_60584_) {
        return PushReaction.DESTROY;
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)))
                .setValue(SLICE_COUNT_BOTTOM, state.getValue(SLICE_COUNT_BOTTOM))
                .setValue(SLICE_COUNT_TOP, state.getValue(SLICE_COUNT_TOP));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)))
                .setValue(SLICE_COUNT_BOTTOM, state.getValue(SLICE_COUNT_BOTTOM))
                .setValue(SLICE_COUNT_TOP, state.getValue(SLICE_COUNT_TOP));
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (level.isClientSide()) {
            return InteractionResult.CONSUME;
        }

        CheeseWheelBlockEntity entity = (CheeseWheelBlockEntity) level.getBlockEntity(blockPos);
        assert entity != null; // will only be true if we somehow stack cheese beyond the height limit

        ItemStack item = player.getItemInHand(interactionHand);

        // handle stacking wheels
        if (item.getItem() == this.asItem()) {
            int slices = Optional.ofNullable(item.getTag())
                    .map(tag -> tag.getCompound("BlockEntityTag"))
                    .map(tag -> tag.getInt("slicesbottom"))
                    .orElse(4);
            if (entity.tryAddSlices(slices)) {
                if (!player.isCreative()) item.shrink(1);
                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.PASS;
            }

        // handle picking up wheels
        } else if (player.isCrouching() && entity.tryTakeSlices(4)) {
            player.getInventory().add(new ItemStack(this.asItem()));
            if(entity.getSliceCount() == 0) level.destroyBlock(blockPos, false);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    public Cheese getVariant() {
        return variant;
    }

    public enum Cheese {
        APPENZELLER(
                GrowthcraftMilkItems.CHEESE_APPENZELLER_SLICE,
                GrowthcraftMilkBlocks.APPENZELLER_CHEESE,
                GrowthcraftMilkBlocks.AGED_APPENZELLER_CHEESE
        ),
        ASIAGO(
                GrowthcraftMilkItems.CHEESE_ASIAGO_SLICE,
                GrowthcraftMilkBlocks.ASIAGO_CHEESE,
                GrowthcraftMilkBlocks.AGED_ASIAGO_CHEESE
        ),
        CASU_MARZU(
                GrowthcraftMilkItems.CHEESE_CASU_MARZU_SLICE,
                GrowthcraftMilkBlocks.CASU_MARZU_CHEESE,
                GrowthcraftMilkBlocks.AGED_CASU_MARZU_CHEESE
        ),
        CHEDDAR(
                GrowthcraftMilkItems.CHEESE_CHEDDAR_SLICE,
                GrowthcraftMilkBlocks.CHEDDAR_CHEESE,
                GrowthcraftMilkBlocks.WAXED_CHEDDAR_CHEESE,
                GrowthcraftMilkBlocks.AGED_CHEDDAR_CHEESE,
                GrowthcraftApiaryItems.BEES_WAX_RED
        ),
        EMMENTALER(
                GrowthcraftMilkItems.CHEESE_EMMENTALER_SLICE,
                GrowthcraftMilkBlocks.EMMENTALER_CHEESE,
                GrowthcraftMilkBlocks.AGED_EMMENTALER_CHEESE
        ),
        GORGONZOLA(
                GrowthcraftMilkItems.CHEESE_GORGONZOLA_SLICE,
                GrowthcraftMilkBlocks.GORGONZOLA_CHEESE,
                GrowthcraftMilkBlocks.AGED_GORGONZOLA_CHEESE
        ),
        GOUDA(
                GrowthcraftMilkItems.CHEESE_GOUDA_SLICE,
                GrowthcraftMilkBlocks.GOUDA_CHEESE,
                GrowthcraftMilkBlocks.WAXED_GOUDA_CHEESE,
                GrowthcraftMilkBlocks.AGED_GOUDA_CHEESE,
                GrowthcraftApiaryItems.BEES_WAX
        ),
        MONTEREY(
                GrowthcraftMilkItems.CHEESE_MONTEREY_SLICE,
                GrowthcraftMilkBlocks.MONTEREY_CHEESE,
                GrowthcraftMilkBlocks.WAXED_MONTEREY_CHEESE,
                GrowthcraftMilkBlocks.AGED_MONTEREY_CHEESE,
                GrowthcraftApiaryItems.BEES_WAX_BLACK
        ),
        PARMESAN(
                GrowthcraftMilkItems.CHEESE_PARMESAN_SLICE,
                GrowthcraftMilkBlocks.PARMESAN_CHEESE,
                GrowthcraftMilkBlocks.AGED_PARMESAN_CHEESE
        ),
        PROVOLONE(
                GrowthcraftMilkItems.CHEESE_PROVOLONE_SLICE,
                GrowthcraftMilkBlocks.PROVOLONE_CHEESE,
                GrowthcraftMilkBlocks.WAXED_PROVOLONE_CHEESE,
                GrowthcraftMilkBlocks.AGED_PROVOLONE_CHEESE,
                GrowthcraftApiaryItems.BEES_WAX_WHITE
        );

        Cheese(
                RegistryObject<? extends Item> slice,
                RegistryObject<? extends Block> unprocessed,
                RegistryObject<? extends Block> waxed,
                RegistryObject<? extends Block> aged,
                RegistryObject<? extends Item> wax) {
            this.process = Processing.WAXING;
            this.slice = slice;
            this.wax = wax;
            this.unprocessed = unprocessed;
            this.waxed = waxed;
            this.aged = aged;
        }

        Cheese(
                RegistryObject<? extends Item> slice,
                RegistryObject<? extends Block> unprocessed,
                RegistryObject<? extends Block> aged) {
            this.process = Processing.AGING;
            this.slice = slice;
            this.unprocessed = unprocessed;
            this.aged = aged;
        }

        private final Processing process;
        private final RegistryObject<? extends Item> slice ;
        private RegistryObject<? extends Item> wax;

        private final RegistryObject<? extends Block> unprocessed;
        private final RegistryObject<? extends Block> aged;

        private RegistryObject<? extends Block> waxed;


        public boolean isAgeable() {
            return process == Processing.AGING;
        }

        public boolean isWaxable() {
            return process == Processing.WAXING;
        }

        public Item getWax() {
            return wax.get().asItem();
        }

        public ItemStack getSlices(int count) {
            return new ItemStack(slice.get(), count);
        }

        public Block getUnprocessed() {
            return unprocessed.get();
        }

        public Block getAged() {
            return aged.get();
        }

        public Block getWaxed() {
            return waxed.get();
        }

        public static Stream<Block> allCheeses() {
            return Arrays.stream(Cheese.values())
                .flatMap(cheese -> {
                    Stream<Block> stream = Stream.of(cheese.getUnprocessed(), cheese.getAged());
                    if (cheese.isWaxable()) {
                        stream = Stream.concat(stream, Stream.of(cheese.getWaxed()));
                    }
                    return stream;
                });
        }

        public static Stream<Block> processedCheeses() {
            return Arrays.stream(Cheese.values()).map(Cheese::getAged);
        }

        private enum Processing {
            WAXING,
            AGING
        }
    }
}
