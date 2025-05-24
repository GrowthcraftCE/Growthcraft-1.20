package growthcraft.milk.block;

import growthcraft.core.utils.BlockPropertiesUtils;
import growthcraft.lib.utils.BlockStateUtils;
import growthcraft.milk.GrowthcraftMilk;
import growthcraft.milk.block.entity.MixingVatBlockEntity;
import growthcraft.milk.init.GrowthcraftMilkBlockEntities;
import growthcraft.milk.init.GrowthcraftMilkFluids;
import growthcraft.milk.init.config.GrowthcraftMilkConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static net.minecraft.world.phys.shapes.BooleanOp.OR;

public class MixingVatBlock extends BaseEntityBlock {
    public static final BooleanProperty LIT = BooleanProperty.create("lit");

    private final VoxelShape[] VOXEL_SHAPES = new VoxelShape[]{
            Block.box(1, 0, 1, 15, 15, 2),
            Block.box(1, 0, 2, 2, 15, 14),
            Block.box(1, 0, 14, 15, 15, 15),
            Block.box(14, 0, 2, 15, 15, 14),
            Block.box(2, 0, 2, 14, 1, 14),
            Block.box(11, 0, 0, 13, 3, 1),
            Block.box(3, 0, 0, 5, 3, 1),
            Block.box(0, 0, 11, 1, 3, 13),
            Block.box(0, 0, 3, 1, 3, 5),
            Block.box(11, 0, 15, 13, 3, 16),
            Block.box(3, 0, 15, 5, 3, 16),
            Block.box(15, 0, 3, 16, 3, 5),
            Block.box(15, 0, 11, 16, 3, 13),
            Block.box(2, 12, 2, 14, 14, 14)
    };

    public MixingVatBlock() {
        this(getInitProperties());
    }

    public MixingVatBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(LIT, false));

    }

    private static Properties getInitProperties() {
        Properties properties = Properties.copy(Blocks.STONE);
        properties.noOcclusion();
        properties.sound(SoundType.METAL);
        properties.isRedstoneConductor(BlockPropertiesUtils::never);
        properties.isViewBlocking(BlockPropertiesUtils::never);
        return properties;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        super.createBlockStateDefinition(stateBuilder.add(LIT));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(LIT, BlockStateUtils.isHeatedFromBelow(context.getLevel(), context.getClickedPos()));
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext context) {
        return Arrays.stream(VOXEL_SHAPES).reduce((v1, v2) -> Shapes.join(v1, v2, OR)).get();
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return GrowthcraftMilkBlockEntities.MIXING_VAT_BLOCK_ENTITY.get().create(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (!level.isClientSide) {
            return createTickerHelper(
                    blockEntityType,
                    GrowthcraftMilkBlockEntities.MIXING_VAT_BLOCK_ENTITY.get(),
                    (worldLevel, pos, blockState, blockEntity) -> (blockEntity).tick()
            );
        }

        return null;
    }

    @Override
    @ParametersAreNonnullByDefault
    public @NotNull InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult hitResult) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        MixingVatBlockEntity blockEntity = (MixingVatBlockEntity) level.getBlockEntity(blockPos);

        assert blockEntity != null;
        FluidTank inputFluidTank = blockEntity.getFluidTank("input");
        FluidTank reagentFluidTank = blockEntity.getFluidTank("reagent");

        if (GrowthcraftMilkConfig.isMixingDebugEnabled()) {
            List<String> recipeIds = blockEntity.getMatchingItemRecipes()
                    .stream()
                    .map(recipe -> recipe.getId().toString())
                    .collect(Collectors.toList());

            recipeIds.addAll(
                    blockEntity.getMatchingFluidRecipes()
                            .stream()
                            .map(recipe -> recipe.getId().toString())
                            .toList()
            );

            String formattedRecipeIds = recipeIds.isEmpty()
                    ? "None"
                    : recipeIds.stream()
                    .map(id -> "\t" + id) // Prefix each ID with a tab
                    .collect(Collectors.joining("\n")); // Join with newlines

            GrowthcraftMilk.LOGGER.warn(String.format(
                    "Mixing Vat Debugging is Enabled\nMixing Vat [%d, %d, %d] (heated = %s, clock = %d/%d)\nActivated with %s (Activation Tools = [%s, %s]).\nBlock Inventory: \n\tItems [%s, %s, %s, %s], \n\tFluids [%s(%dmb), %s(%dmb)].\nMatching Recipes:\n%s",
                    blockPos.getX(), blockPos.getY(), blockPos.getZ(), blockEntity.isHeated(),
                    blockEntity.getTickClock("current"), blockEntity.getTickClock("max"),
                    player.getItemInHand(interactionHand), blockEntity.getActivationTool().toString(),
                    blockEntity.getResultActivationTool().toString(),
                    blockEntity.getInventoryHandler().getStackInSlot(0), blockEntity.getInventoryHandler().getStackInSlot(1),
                    blockEntity.getInventoryHandler().getStackInSlot(2), blockEntity.getInventoryHandler().getStackInSlot(3),
                    blockEntity.getFluidTank(0).getFluid().getFluid().getFluidType(), blockEntity.getFluidTank(0).getFluidAmount(),
                    blockEntity.getFluidTank(1).getFluid().getFluid().getFluidType(), blockEntity.getFluidTank(1).getFluidAmount(),
                    formattedRecipeIds
            ));
        }


        // Try to do fluid handling first.
        if (player.getItemInHand(interactionHand)
                .getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM)
                .isPresent()
        ) {
            boolean fluidInteractionResult = false;

            if (player.getItemInHand(interactionHand).getItem() == Items.BUCKET) {
                // Then we are trying to drain the tanks.
                if (!reagentFluidTank.isEmpty()) {
                    fluidInteractionResult = FluidUtil.interactWithFluidHandler(player, interactionHand, level, blockPos, Direction.NORTH);
                } else if (!inputFluidTank.isEmpty()) {
                    fluidInteractionResult = FluidUtil.interactWithFluidHandler(player, interactionHand, level, blockPos, Direction.UP);
                }
            } else if (player.getItemInHand(interactionHand).getItem() == Items.MILK_BUCKET) {
                int capacity = blockEntity.getFluidTank(0).getCapacity();
                int amount = blockEntity.getFluidTank(0).getFluidAmount();
                int remainingFill = capacity - amount;

                if (blockEntity.getFluidTank(0).isEmpty()
                        || (remainingFill >= 1000
                        && blockEntity.getFluidStackInTank(0).getFluid().getFluidType() == GrowthcraftMilkFluids.MILK.source.get().getFluidType())
                ) {
                    FluidStack fluidStack = new FluidStack(GrowthcraftMilkFluids.MILK.source.get().getSource(), 1000);
                    blockEntity.getFluidTank(0).fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
                    player.setItemInHand(interactionHand, new ItemStack(Items.BUCKET));
                }
            } else {
                // Try and fill the input tank first.
                if (inputFluidTank.getFluidAmount() < inputFluidTank.getCapacity()) {
                    fluidInteractionResult = FluidUtil.interactWithFluidHandler(player, interactionHand, level, blockPos, Direction.UP);
                } else if (reagentFluidTank.getFluidAmount() < reagentFluidTank.getCapacity()) {
                    fluidInteractionResult = FluidUtil.interactWithFluidHandler(player, interactionHand, level, blockPos, Direction.NORTH);
                }
            }

            return fluidInteractionResult ? InteractionResult.SUCCESS : InteractionResult.FAIL;
        } else if (!player.getItemInHand(interactionHand).isEmpty()
                && (player.getItemInHand(interactionHand).is(blockEntity.getActivationTool().getItem())
                || player.getItemInHand(interactionHand).is(blockEntity.getResultActivationTool().getItem()))
        ) {
            // Try and activate the recipe.
            if (blockEntity.activateRecipe(player.getItemInHand(interactionHand))) {
                if (GrowthcraftMilkConfig.isConsumeMixingVatActivator())
                    player.getItemInHand(interactionHand).shrink(1);
                return InteractionResult.SUCCESS;
            }

            // Process the Cheese Curds extraction.
            if (!blockEntity.getInventoryHandler().getStackInSlot(3).isEmpty()
                    && blockEntity.activateResult(player, player.getItemInHand(interactionHand))) {
                player.getItemInHand(interactionHand).shrink(1);
                return InteractionResult.SUCCESS;
            }

        } else {
            // Otherwise, try and interact with the GUI.
            try {
                // Play sound
                blockEntity.playSound("open");
                // Open the GUI
                NetworkHooks.openScreen(((ServerPlayer) player), blockEntity, blockPos);
                return InteractionResult.SUCCESS;
            } catch (Exception ex) {
                GrowthcraftMilk.LOGGER.error(
                        String.format("%s unable to open MixingVatBlockEntity GUI at %s.",
                                player.getDisplayName().getString(),
                                blockPos)
                );
                GrowthcraftMilk.LOGGER.error(ex.getMessage());
                GrowthcraftMilk.LOGGER.error(ex.fillInStackTrace());
                return InteractionResult.FAIL;
            }
        }
        return InteractionResult.SUCCESS;
    }
}
