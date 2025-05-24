package growthcraft.milk.block;

import growthcraft.core.init.GrowthcraftItems;
import growthcraft.core.init.GrowthcraftTags;
import growthcraft.milk.GrowthcraftMilk;
import growthcraft.milk.block.entity.CheesePressBlockEntity;
import growthcraft.milk.init.GrowthcraftMilkBlockEntities;
import growthcraft.milk.init.GrowthcraftMilkItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class CheesePressBlock extends BaseEntityBlock {
    //TODO[63]: Implement CheesePressBlock

    public static final IntegerProperty ROTATION = IntegerProperty.create("rotation", 0, 7);

    public static final VoxelShape BOUNDING_BOX = Block.box(
            0.02F, 0.0F, 0.02F,
            15.98F, 15.98F, 15.98
    );

    public CheesePressBlock() {
        this(getInitProperties());
    }

    protected CheesePressBlock(Properties properties) {
        super(properties);
    }

    private static Properties getInitProperties() {
        Properties properties = Properties.copy(Blocks.CHEST);
        properties.noOcclusion();
        return properties;
    }

    @Override
    public RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext context) {
        return BOUNDING_BOX;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        super.createBlockStateDefinition(stateBuilder.add(ROTATION));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(ROTATION, 0);
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState p_60584_) {
        return PushReaction.DESTROY;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return GrowthcraftMilkBlockEntities.CHEESE_PRESS_BLOCK_ENTITY.get().create(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> entityType) {
        if (level.isClientSide) {
            return blockState.getValue(ROTATION) == 7
                    ? createTickerHelper(
                            entityType,
                            GrowthcraftMilkBlockEntities.CHEESE_PRESS_BLOCK_ENTITY.get(),
                            CheesePressBlockEntity::particleTick)
                    : null;
        } else {
            return createTickerHelper(
                    entityType,
                    GrowthcraftMilkBlockEntities.CHEESE_PRESS_BLOCK_ENTITY.get(),
                    (worldLevel, pos, state, blockEntity) -> (blockEntity).tick()
            );
        }
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        CheesePressBlockEntity blockEntity = (CheesePressBlockEntity) level.getBlockEntity(blockPos);
        if (blockEntity == null) return InteractionResult.FAIL;
        // wrench first
        if (player.getItemInHand(interactionHand).is(GrowthcraftTags.Items.ROASTER_WRENCH)) {
            if (level.isClientSide) return InteractionResult.SUCCESS; // we handle things on server. this prevents double handling.
            if (!player.isCrouching()) {
                // Then tighten the cheese press.
                level.playSound(null, blockPos, SoundEvents.CHAIN_PLACE, SoundSource.BLOCKS);
                level.setBlock(blockPos, blockState.setValue(ROTATION, blockEntity.doRotation(true)), Block.UPDATE_ALL_IMMEDIATE);
            } else {
                // Then loosen the cheese press and extract the result item.
                level.playSound(null, blockPos, SoundEvents.CHAIN_BREAK, SoundSource.BLOCKS);
                level.setBlock(blockPos, blockState.setValue(ROTATION, blockEntity.doRotation(false)), Block.UPDATE_ALL_IMMEDIATE);
            }
            return InteractionResult.CONSUME;
        }
        // confirm that it's open.  todo: issue: isOpen is always false on client side; should fix that instead of working around.
        if (!blockEntity.isOpen()) {
            return InteractionResult.PASS;
        }
        // Insert one count of the item being held.
        if (!player.getItemInHand(interactionHand).isEmpty()
                        && blockEntity.getItemStackHandler().getStackInSlot(0).isEmpty() && blockEntity.getItemStackHandler().getStackInSlot(1).isEmpty()) {
            if (player.getItemInHand(interactionHand).is(GrowthcraftMilkItems.CHEESE_CLOTH.get())) {
                return InteractionResult.PASS; // we don't really need any special handling for cheese cloth, but this is likely a double-click so let's block it to prevent confusion with empty cloth being inside of cheese press.
            }
            ItemStack itemStackToInsert = player.getItemInHand(interactionHand).copy();
            itemStackToInsert.setCount(1);
            ItemStack remainingItem = itemStackToInsert.getCraftingRemainingItem();
            blockEntity.getItemStackHandler().insertItem(0, itemStackToInsert, false);
            player.getItemInHand(interactionHand).shrink(1);
            addToPlayer(player, remainingItem, blockPos);      // give them 1 piece of cloth or a bucket or whatever
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        // Extract item from cheese press
        if (!blockEntity.getItemStackHandler().getStackInSlot(0).isEmpty() || !blockEntity.getItemStackHandler().getStackInSlot(1).isEmpty()) {
            if (level.isClientSide()) {
                return InteractionResult.SUCCESS; // don't really need this but isOpen fails on client currently.
            }
            ItemStack itemStackInPress = blockEntity.getItemStackHandler().getStackInSlot(0).copy();
            if (itemStackInPress.isEmpty()) {
                itemStackInPress = blockEntity.getItemStackHandler().getStackInSlot(1).copy();
            }

            if (!itemStackInPress.isEmpty()) {
                boolean giveItem = false;
                ItemStack containerItemStack = itemStackInPress.getCraftingRemainingItem();
                if (player.getItemInHand(interactionHand).isEmpty() && containerItemStack.isEmpty()) {
                    // no container item (cloth/bucket), hand empty
                    giveItem = true;
                }
                else if (!containerItemStack.isEmpty() && !ItemStack.isSameItem(containerItemStack, player.getItemInHand(interactionHand))) {
                    // hand empty (or wrong item) but we need cloth or bucket
                    Component containerText = containerItemStack.getHoverName().copy().withStyle(Style.EMPTY.withColor(0xffffff88));
                    Component message = Component.translatable("message.growthcraft_milk.get_using_item", containerText).withStyle(Style.EMPTY.withColor(0xffbb9944));
                    player.displayClientMessage(message, true);
                    giveItem = false;
                }
                else if (!player.getItemInHand(interactionHand).isEmpty() && containerItemStack.isEmpty()) {
                    // hand not empty but no need for cloth/bucket
                    Component handText = Component.translatable("message.growthcraft_milk.get_using_item_empty_hand").withStyle(Style.EMPTY.withColor(0xffdddd88));
                    Component message = Component.translatable("message.growthcraft_milk.get_using_item", handText).withStyle(Style.EMPTY.withColor(0xffbb9944));
                    player.displayClientMessage(message, true);
                    giveItem = false;
                }
                else {
                    // hand not empty and matches
                    player.getItemInHand(interactionHand).shrink(1);
                    giveItem = true;
                }
                if (giveItem) {
                    addToPlayer(player, itemStackInPress, blockPos);
                    blockEntity.getItemStackHandler().setStackInSlot(0, ItemStack.EMPTY);
                    blockEntity.getItemStackHandler().setStackInSlot(1, ItemStack.EMPTY);
                    level.sendBlockUpdated(blockPos, blockState, blockState, Block.UPDATE_CLIENTS);
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide());
        }
        return InteractionResult.PASS;
    }

    private void addToPlayer(Player player, ItemStack item, BlockPos pressLocation) {
        if (!player.level().isClientSide()) {
            if (!player.addItem(item)) {
                ItemEntity itemEntity = new ItemEntity(player.level(), pressLocation.getX() + 0.5d, pressLocation.getY() + 1.5d, pressLocation.getZ() + 0.5d, item);
                player.level().addFreshEntity(itemEntity);
            }
        }
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState newBlockState, boolean isMoving) {
        if (blockState.getBlock() != newBlockState.getBlock()) {
            try {
                if (level.getBlockEntity(blockPos) instanceof CheesePressBlockEntity blockEntity) {
                    blockEntity.dropItems(); // ...maybe drop. we will drop standalone things but not items held in buckets, bowls, cloth...
                }
            } catch (Exception ex) {
                GrowthcraftMilk.LOGGER.error(String.format("Invalid blockEntity type at %s, expected RoasterBlockEntity", blockPos));
            }
        }
        super.onRemove(blockState, level, blockPos, newBlockState, isMoving);
    }

    public static void makeParticles(Level level, BlockPos blockPos, BlockState blockState) {
        RandomSource randomSource = level.getRandom();

        CheesePressBlockEntity blockEntity = (CheesePressBlockEntity) level.getBlockEntity(blockPos);
        if(blockEntity == null) return;

        if (blockState.getValue(ROTATION) == 7 && blockEntity.getTickClock("current") > 0) {
            double d0 = blockPos.getX() + randomSource.nextDouble();
            double d1 = blockPos.getY() - 0.05D;
            double d2 = blockPos.getZ() + randomSource.nextDouble();

            level.addParticle(ParticleTypes.FALLING_HONEY, d0, d1, d2, 0.0D, 0.0D, 0.0D);
        }

    }
}
