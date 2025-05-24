package growthcraft.milk.block.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.ParametersAreNonnullByDefault;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import growthcraft.milk.GrowthcraftMilk;
import growthcraft.milk.block.BaseCheeseWheel;
import growthcraft.milk.block.CheeseWheelAgeableBlock;
import growthcraft.milk.block.CheeseWheelBlock;
import growthcraft.milk.init.GrowthcraftMilkBlockEntities;
import growthcraft.milk.init.config.GrowthcraftMilkConfig;
import growthcraft.milk.recipe.CheesePressRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class CheeseWheelBlockEntity extends BlockEntity {
    private boolean aged;
    private int sliceCountTop;
    private int sliceCountBottom;

    private int tickClock;
    private int tickMax = GrowthcraftMilkConfig.getCheeseAgeTickTime();

    private Component customName;

    public CheeseWheelBlockEntity (BlockPos blockPos, BlockState blockState) {
        this(GrowthcraftMilkBlockEntities.CHEESE_WHEEL_BLOCK_ENTITY.get(), blockPos, blockState);
    }

    public CheeseWheelBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
        this.tickClock = 0;
        this.aged = true;
        this.sliceCountBottom = 4;
        this.sliceCountTop = 0;
    }

    public void ageCheese(Level level, BlockPos blockPos, BlockState blockState, CheeseWheelBlockEntity blockEntity) {
        if (level.isClientSide()) {
            return;
        }
        if (blockState.getBlock() instanceof CheeseWheelAgeableBlock block) {
            if (this.tickClock < tickMax) {
                this.tickClock++;
            } else if (tickMax != -1) {
                // Age the cheese
                this.level.setBlock(blockPos, block.getVariant().getAged().withPropertiesOf(blockState),
                        Block.UPDATE_ALL_IMMEDIATE);
                // Transfer nbt data from this blockEntity to the new block entity, this is kind of a hack
                BlockEntity newEntity = level.getBlockEntity(blockPos);
                if (newEntity == null) {
                    GrowthcraftMilk.LOGGER.warn(("Aging cheese at %s failed, block entity of new block was null")
                            .formatted(blockPos.toString()));
                    return;
                }
                CompoundTag nbt = this.serializeNBT();
                newEntity.deserializeNBT(nbt);
            } else {
                // This is probably broken cheese that was aged before 9.0.6
                // lets start the aging process over again, and this time do it properly
                this.tickMax = GrowthcraftMilkConfig.getCheeseAgeTickTime();
                this.tickClock = 0;
            }
        }
    }

    @Deprecated
    private void setBlockState(int bottomSlices, int topSlices) {
        this.level.setBlock(
                this.getBlockPos(),
                this.getBlockState()
                    .setValue(CheeseWheelBlock.SLICE_COUNT_TOP, topSlices)
                    .setValue(CheeseWheelBlock.SLICE_COUNT_BOTTOM, bottomSlices),
                Block.UPDATE_ALL_IMMEDIATE
        );
    }

    public void updateBlockState() {
        level.setBlock(
                getBlockPos(),
                getBlockState()
                        .setValue(BaseCheeseWheel.SLICE_COUNT_TOP, sliceCountTop)
                        .setValue(BaseCheeseWheel.SLICE_COUNT_BOTTOM, sliceCountBottom),
                Block.UPDATE_ALL_IMMEDIATE
        );
    }

    @Deprecated
    public boolean canTakeSlice() {
        return this.aged && this.getSliceCount() > 0;
    }

    @Deprecated
    public ItemStack takeSlice() {
            List<CheesePressRecipe> cheesePressRecipes = this.getMatchingRecipes(
                    new ItemStack(this.getBlockState().getBlock().asItem())
            );

            CheesePressRecipe recipe = cheesePressRecipes.isEmpty() ? null : cheesePressRecipes.get(0);

            if (recipe != null) {
                this.takeSlice(1);
                return recipe.getSliceItemStack().copy();
            }

        return null;
    }

    @Deprecated
    private List<CheesePressRecipe> getMatchingRecipes(ItemStack itemStack) {
        List<CheesePressRecipe> matchingRecipes = new ArrayList<>();

        List<CheesePressRecipe> recipes = level.getRecipeManager().getAllRecipesFor(
                CheesePressRecipe.Type.INSTANCE
        );

        for (CheesePressRecipe recipe : recipes) {
            if (recipe.matchesOutput(itemStack)) {
                matchingRecipes.add(recipe);
            }
        }
        return matchingRecipes;
    }

    public void takeSlice(int count) {
        if (this.sliceCountTop >= count) {
            this.sliceCountTop -= count;
        } else if (this.sliceCountBottom >= count - sliceCountTop) {
            sliceCountBottom -= count - sliceCountTop;
            sliceCountTop = 0;
        }
        this.setBlockState(this.sliceCountBottom, this.sliceCountTop);
    }

    public boolean tryTakeSlices(int slices) {
        if (getSliceCount() < slices) {
            return false;
        }
        takeSlice(slices);
        return true;
    }

    public boolean canAddSlice(int count) {
        return getSliceCount() + count <= 8;
    }

    public void addSlice(int slices) {
        int newTotal = sliceCountTop + sliceCountBottom + slices;
        if (newTotal > 4) {
            sliceCountBottom = 4;
            sliceCountTop = newTotal - 4;
        } else {
            sliceCountBottom = newTotal;
            sliceCountTop = 0;
        }
    }

    public boolean tryAddSlices(int slices) {
        if (!canAddSlice(slices)) {
            return false;
        }
        addSlice(slices);
        updateBlockState();
        return true;
    }

    public int getSliceCount() {
        return this.sliceCountTop + this.sliceCountBottom;
    }

    public int getWheelCount() {
        return this.getSliceCount() / 4;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        return this.serializeNBT();
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        this.load(tag);
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);
        this.tickClock = nbt.getInt("CurrentProcessTicks");
        this.tickMax = nbt.getInt("MaxProcessTicks");
        this.sliceCountTop = nbt.getInt("slicestop");
        this.sliceCountBottom = nbt.getInt("slicesbottom");
        this.aged = nbt.getBoolean("aged");

        if (nbt.contains("CustomName", 8)) {
            this.customName = Component.Serializer.fromJson(nbt.getString("CustomName"));
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void saveAdditional(CompoundTag nbt) {
        nbt.putInt("CurrentProcessTicks", this.tickClock);
        nbt.putInt("MaxProcessTicks", this.tickMax);
        nbt.putInt("slicestop", this.sliceCountTop);
        nbt.putInt("slicesbottom", this.sliceCountBottom);
        nbt.putBoolean("aged", this.aged);

        if (this.customName != null) {
            nbt.putString("CustomName", Component.Serializer.toJson(this.customName));
        }

        super.saveAdditional(nbt);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        this.load(Objects.requireNonNull(pkt.getTag()));
    }

    @Override
    public void onLoad() {
        super.onLoad();
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
    }
}
