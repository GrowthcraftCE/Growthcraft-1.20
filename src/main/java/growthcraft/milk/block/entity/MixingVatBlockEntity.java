package growthcraft.milk.block.entity;

import growthcraft.lib.block.entity.GrowthcraftFluidTank;
import growthcraft.lib.utils.BlockStateUtils;
import growthcraft.lib.utils.DirectionUtils;
import growthcraft.milk.init.GrowthcraftMilkBlockEntities;
import growthcraft.milk.lib.networking.GrowthcraftMilkMessages;
import growthcraft.milk.lib.networking.packet.MixingVatFluidSyncPacket;
import growthcraft.milk.recipe.MixingVatFluidRecipe;
import growthcraft.milk.recipe.MixingVatItemRecipe;
import growthcraft.milk.screen.container.MixingVatMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static growthcraft.cellar.block.CultureJarBlock.LIT;

public class MixingVatBlockEntity extends BlockEntity implements BlockEntityTicker<MixingVatBlockEntity>, MenuProvider {
    private int tickClock = 0;
    private int tickMax = -1;
    private boolean activated = false;
    private ItemStack activationTool = ItemStack.EMPTY;
    private boolean requiresHeatSource = true;

    private ItemStack resultActivationTool = ItemStack.EMPTY;

    protected final ContainerData data;

    private Component customName;

    private static class MixingVatItemStackHandler extends ItemStackHandler
    {
        private MixingVatItemStackHandler() {
            super(4);
        }
        private BlockEntity owner;

        private void setOwner(BlockEntity owner) {
            this.owner = owner;
        }

        @Override
        protected void onContentsChanged(int slot) {
            owner.setChanged();
        }

        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }

        @Override
        public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate)
        {
            if (amount == 0)  return ItemStack.EMPTY;
            validateSlotIndex(slot);
            ItemStack existing = this.stacks.get(slot);
            if (existing.isEmpty()) return ItemStack.EMPTY; // up to here is just copied.
            if (slot == 3 && existing.hasCraftingRemainingItem()) return ItemStack.EMPTY; // this is why we override.
            return super.extractItem(slot, amount, simulate);
        }

        public @NotNull ItemStack extractItemWithProperActivationItem(int slot, int amount, boolean simulate) {
            if (slot != 3) throw new RuntimeException("Slot " + slot + " not result slot.");
            return super.extractItem(slot, amount, simulate);
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack)
        {
            if (slot == 3) return false; // no insertion into result slot
            return super.isItemValid(slot, stack);
        }
    };
    private final MixingVatItemStackHandler itemStackHandler = new MixingVatItemStackHandler();
    private LazyOptional<IItemHandler> inventoryHandler = LazyOptional.empty();

    private final GrowthcraftFluidTank FLUID_TANK_INPUT = new GrowthcraftFluidTank(4000) {
        @Override
        public void onContentsChanged() {
            setChanged();
            if (level != null && !level.isClientSide) {
                GrowthcraftMilkMessages.sendToClients(new MixingVatFluidSyncPacket(0, this.fluid, worldPosition));
            }
        }
    };

    private final GrowthcraftFluidTank FLUID_TANK_OUTPUT = new GrowthcraftFluidTank(1000) {
        @Override
        public void onContentsChanged() {
            setChanged();
            if (level != null && !level.isClientSide) {
                GrowthcraftMilkMessages.sendToClients(new MixingVatFluidSyncPacket(1, this.fluid, worldPosition));
            }
        }
    };

    private LazyOptional<IFluidHandler> inputFluidHandler = LazyOptional.empty();
    private LazyOptional<IFluidHandler> reagentFluidHandler = LazyOptional.empty();

    public MixingVatBlockEntity(BlockPos blockPos, BlockState state) {
        super(GrowthcraftMilkBlockEntities.MIXING_VAT_BLOCK_ENTITY.get(), blockPos, state);

        this.FLUID_TANK_INPUT.allowAnyFluid(true);
        this.FLUID_TANK_OUTPUT.allowAnyFluid(true);
        this.itemStackHandler.setOwner(this);

        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> MixingVatBlockEntity.this.tickClock;
                    case 1 -> MixingVatBlockEntity.this.tickMax;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> MixingVatBlockEntity.this.tickClock = value;
                    case 1 -> MixingVatBlockEntity.this.tickMax = value;
                    default -> {
                        // Do Nothing
                    }
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public @NotNull Component getDisplayName() {
        return this.customName != null
                ? this.customName
                : Component.translatable("container.growthcraft_milk.mixing_vat");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, @NotNull Inventory inventory, @NotNull Player player) {
        return new MixingVatMenu(containerId, inventory, this, this.data);
    }

    public boolean canActivateProcessing(ItemStack itemStack) {
        List<MixingVatFluidRecipe> fluidRecipes = this.getMatchingFluidRecipes();
        MixingVatFluidRecipe fluidRecipe = fluidRecipes.isEmpty() ? null : fluidRecipes.get(0);

        if(fluidRecipe != null) {
            return fluidRecipe.getActivationTool().is(itemStack.getItem());
        }

        List<MixingVatItemRecipe> itemRecipes = this.getMatchingItemRecipes();
        MixingVatItemRecipe itemRecipe = itemRecipes.isEmpty() ? null : itemRecipes.get(0);

        if(itemRecipe != null) {
            // Then we have an Item recipe.
            return itemRecipe.getActivationTool().is(itemStack.getItem());
        }

        return false;
    }

    public boolean canActivateResultItem(ItemStack itemStack) {
        List<MixingVatItemRecipe> itemRecipes = this.getMatchingItemRecipes();
        MixingVatItemRecipe itemRecipe = itemRecipes.isEmpty() ? null : itemRecipes.get(0);

        if(itemRecipe != null) {
            // Then we have an Item recipe.
            return itemRecipe.getResultActivationTool().is(itemStack.getItem());
        }
        return false;
    }

    public void tick() {
        if (this.getLevel() != null) {
            this.tick(this.getLevel(), this.getBlockPos(), this.getBlockState(), this);
        }
    }

    @Override
    public void tick(Level level, BlockPos blockPos, BlockState blockState, MixingVatBlockEntity mixingVatBlockEntity) {
        if (level.isClientSide) return;

        // First check for a MixingVatFluidRecipe
        List<MixingVatFluidRecipe> fluidRecipes = this.getMatchingFluidRecipes();
        MixingVatFluidRecipe fluidRecipe = fluidRecipes.isEmpty() ? null : fluidRecipes.get(0);

        List<MixingVatItemRecipe> itemRecipes = this.getMatchingItemRecipes();
        MixingVatItemRecipe itemRecipe = itemRecipes.isEmpty() ? null : itemRecipes.get(0);

        boolean validRecipe = (fluidRecipe != null || itemRecipe != null);

        if (validRecipe && this.tickClock <= this.tickMax && this.activated) {
            this.tickClock++;
        } else if (validRecipe && this.tickMax > 0 && this.activated) {

            // Consume all the inventory items.
            this.itemStackHandler.setStackInSlot(0, ItemStack.EMPTY);
            this.itemStackHandler.setStackInSlot(1, ItemStack.EMPTY);
            this.itemStackHandler.setStackInSlot(2, ItemStack.EMPTY);

            if(fluidRecipe != null) {
                // Set the output fluid and any reagent/waste fluid.
                this.FLUID_TANK_INPUT.setFluid(fluidRecipe.getOutputFluidStack().copy());
                this.FLUID_TANK_OUTPUT.setFluid(fluidRecipe.getWasteFluidStack().copy());

            } else {
                // Consume all the inventory items and fluid.
                this.itemStackHandler.setStackInSlot(3, itemRecipe.getResultItemStack().copy());
                this.FLUID_TANK_INPUT.setFluid(FluidStack.EMPTY);
                this.FLUID_TANK_OUTPUT.setFluid(FluidStack.EMPTY);
            }

            this.resetTickClock();
        } else if (validRecipe && this.tickMax == -1) {
            if(fluidRecipe != null) {
                this.tickMax = fluidRecipe.getProcessingTime();
                this.activationTool = fluidRecipe.getActivationTool();
                this.resultActivationTool = ItemStack.EMPTY;
                this.requiresHeatSource = fluidRecipe.isHeatRequired();
            } else {
                this.tickMax = itemRecipe.getProcessingTime();
                this.activationTool = itemRecipe.getActivationTool();
                this.resultActivationTool = itemRecipe.getResultActivationTool();
                this.requiresHeatSource = itemRecipe.isHeatRequired();
            }

            level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_ALL);
        } else if(validRecipe && this.tickMax > 0 && !this.activated) {
            // Then do nothing, we are waiting activate the recipe.
        } else {
            this.resetTickClock();
        }

        level.sendBlockUpdated(
                this.getBlockPos(),
                this.getBlockState(),
                this.getBlockState(),
                Block.UPDATE_ALL
        );
    }

    public int getTickClock(String type) {
        return switch (type) {
            case "current" -> this.tickClock;
            case "max" -> this.tickMax;
            default -> 0;
        };
    }

    private void resetTickClock() {
        this.tickClock = 0;
        this.tickMax = -1;
        this.activated = false;
    }

    public List<MixingVatFluidRecipe> getMatchingFluidRecipes() {
        // Fail-fast if world context or required conditions are not met
        if (level == null || FLUID_TANK_INPUT.isEmpty() || FLUID_TANK_OUTPUT.isEmpty()) return Collections.emptyList();

        List<MixingVatFluidRecipe> matchingRecipes = new ArrayList<>();

        // Place the input slots into a List.
        List<ItemStack> currentItems = new ArrayList<>();
        for (int i = 0; i < itemStackHandler.getSlots() - 1; i++) {
            if (!itemStackHandler.getStackInSlot(i).isEmpty()) currentItems.add(itemStackHandler.getStackInSlot(i));
        }

        List<MixingVatFluidRecipe> recipes = this.level.getRecipeManager().getAllRecipesFor(
                MixingVatFluidRecipe.Type.INSTANCE
        );

        for(MixingVatFluidRecipe recipe : recipes) {
            if (recipe.matches(
                    this.FLUID_TANK_INPUT.getFluid().copy(),
                    this.FLUID_TANK_OUTPUT.getFluid().copy(),
                    currentItems,
                    isHeated())
            ) {
                matchingRecipes.add(recipe);
            }
        }

        return matchingRecipes;
    }

    /**
     * Returns a list of matching {@link MixingVatItemRecipe}s based on the current fluid input,
     * non-empty item input slots, and heat source status.
     *
     * @return a list of matching item recipes, or an empty list if no matches are found
     */
    public List<MixingVatItemRecipe> getMatchingItemRecipes() {
        // Fail-fast if world context or required conditions are not met
        if (level == null || !FLUID_TANK_OUTPUT.isEmpty() || FLUID_TANK_INPUT.isEmpty()) {
            return Collections.emptyList();
        }

        // Collect non-empty item stacks from input slots (excluding the last slot)
        List<ItemStack> currentItems = new ArrayList<>();
        int inputSlots = itemStackHandler.getSlots() - 1;

        for (int i = 0; i < inputSlots; i++) {
            ItemStack stack = itemStackHandler.getStackInSlot(i);
            if (!stack.isEmpty()) {
                currentItems.add(stack);
            }
        }

        // If no valid input items, return empty list
        if (currentItems.isEmpty()) {
            return Collections.emptyList();
        }

        // Find all recipes that match the current inputs
        return level.getRecipeManager().getAllRecipesFor(MixingVatItemRecipe.Type.INSTANCE).stream()
                .filter(recipe -> recipe.matches(FLUID_TANK_INPUT.getFluid(), currentItems, isHeated()))
                .toList();
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
        this.activated = nbt.getBoolean("IsActivated");
        this.requiresHeatSource = nbt.getBoolean("RequiresHeatSource");

        this.itemStackHandler.deserializeNBT(nbt.getCompound("inventory"));
        this.FLUID_TANK_INPUT.readFromNBT(nbt.getCompound("InputFluidTank"));
        this.FLUID_TANK_OUTPUT.readFromNBT(nbt.getCompound("ReagentFluidTank"));

        this.activationTool = ItemStack.of(nbt.getCompound("ActivationTool"));
        this.resultActivationTool = ItemStack.of(nbt.getCompound("ResultActivationTool"));

        if (nbt.contains("CustomName", 8)) {
            this.customName = Component.Serializer.fromJson(nbt.getString("CustomName"));
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void saveAdditional(CompoundTag nbt) {

        nbt.putInt("CurrentProcessTicks", this.tickClock);
        nbt.putInt("MaxProcessTicks", this.tickMax);
        nbt.putBoolean("IsActivated", this.activated);
        nbt.putBoolean("RequiresHeatSource", this.requiresHeatSource);

        nbt.put("inventory", itemStackHandler.serializeNBT());
        nbt.put("InputFluidTank", FLUID_TANK_INPUT.writeToNBT(new CompoundTag()));
        nbt.put("ReagentFluidTank", FLUID_TANK_OUTPUT.writeToNBT(new CompoundTag()));

        nbt.put("ActivationTool", this.activationTool.serializeNBT());
        nbt.put("ResultActivationTool", this.resultActivationTool.serializeNBT());

        if (this.customName != null) {
            nbt.putString("CustomName", Component.Serializer.toJson(this.customName));
        }

        super.saveAdditional(nbt);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        super.onDataPacket(net, pkt);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        inputFluidHandler = LazyOptional.of(() -> FLUID_TANK_INPUT);
        reagentFluidHandler = LazyOptional.of(() -> FLUID_TANK_OUTPUT);
        inventoryHandler = LazyOptional.of(() -> itemStackHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        inputFluidHandler.invalidate();
        reagentFluidHandler.invalidate();
        inventoryHandler.invalidate();
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.FLUID_HANDLER) {
            if (DirectionUtils.isSide(side)) {
                return this.reagentFluidHandler.cast();
            } else if (DirectionUtils.isTop(side)) {
                return this.inputFluidHandler.cast();
            }
        } else if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return this.inventoryHandler.cast();
        }

        return super.getCapability(cap, side);
    }

    public ItemStackHandler getInventoryHandler() {
        return itemStackHandler;
    }

    public void setFluidStackInTank(int tankID, FluidStack fluidStack) {
        switch (tankID) {
            case 0 -> this.FLUID_TANK_INPUT.setFluid(fluidStack);
            case 1 -> this.FLUID_TANK_OUTPUT.setFluid(fluidStack);
            default -> {
                throw new IndexOutOfBoundsException(
                        String.format("MixingVatBlockEntity at %s: setFluidStackInTank(int %d, FluidStack %s) is not a valid tank ID.",
                                this.getBlockPos(), tankID, fluidStack.getFluid().toString()));
            }
        }
    }

    public FluidStack getFluidStackInTank(String tankName) {
        return switch (tankName) {
            case "input" -> this.getFluidStackInTank(0);
            case "reagent" -> this.getFluidStackInTank(1);
            default -> {
                throw new IndexOutOfBoundsException(
                        String.format("MixingVatBlockEntity at %s: %s is not a valid tank name.",
                                this.getBlockPos(), tankName));
            }
        };
    }

    public FluidStack getFluidStackInTank(int tankID) {
        return tankID == 0 ? this.FLUID_TANK_INPUT.getFluid() : this.FLUID_TANK_OUTPUT.getFluid();
    }

    public FluidTank getFluidTank(String tankName) {
        return switch (tankName) {
            case "input" -> this.getFluidTank(0);
            case "reagent" -> this.getFluidTank(1);
            default -> {
                throw new IndexOutOfBoundsException(
                        String.format("MixingVatBlockEntity at %s: %s is not a valid tank name.",
                                this.getBlockPos(), tankName));
            }
        };
    }

    public FluidTank getFluidTank(int tankID) {
        return tankID == 0 ? this.FLUID_TANK_INPUT : this.FLUID_TANK_OUTPUT;
    }

    /**
     * Attempt to activate the current recipe with an item.
     * <p>
     * Activation is true if ...
     * - Activation tool is not null.
     * - The checked item matches the recipe defined activation.
     * - The block has a heat source and the recipe requires a heat source.
     *
     * @param stack ItemStack Item to use to activate the recipe for the block.
     * @return boolean If the current block was activated with this check.
     */
    public boolean activateRecipe(ItemStack stack) {
        // Fail fast if vat is already activated or if there is a result itemstack that still needs to be removed.
        if(this.activated || !this.itemStackHandler.getStackInSlot(3).isEmpty()) return false;

        this.activated = this.getActivationTool() != null
                && this.getActivationTool().getItem() == stack.getItem()
                && isHeated() == this.requiresHeatSource;

        // Reset the activation tool if recipe is activated
        if (this.activated) this.activationTool = ItemStack.EMPTY;

        return this.activated;
    }

    /**
     * Get the tool needed to activate the current recipe. Will return ItemStack.EMPTY there isn't a current recipe.
     *
     * @return ItemStack Item needed to activate the current recipe.
     */
    public ItemStack getActivationTool() {
        return this.activationTool;
    }

    public ItemStack getResultActivationTool() {
        return this.resultActivationTool;
    }

    public boolean activateResult(Player player, ItemStack resultActivationTool) {
        if (this.getResultActivationTool() != null
                && this.getResultActivationTool().getItem() == resultActivationTool.getItem()
        ) {
            ItemStack itemStack = this.itemStackHandler.extractItemWithProperActivationItem(
                    3,
                    this.itemStackHandler.getStackInSlot(3).getCount(),
                    false);

            if (!player.getInventory().add(itemStack)) {
                player.drop(itemStack, false);
            }

            // Reset the resultActivationTool
            this.resultActivationTool = ItemStack.EMPTY;

            return true;
        }
        return false;
    }

    public boolean isProcessing() {
        return this.tickClock > 0;
    }

    public int getPercentProgress() {
        float progress = (float) this.tickClock / this.tickMax;
        float percentage = progress * 100;
        return Math.round(percentage);
    }

    public void playSound(String sound) {
        if (Objects.equals(sound, "open") && this.level != null) {
            this.level.playSound(null, this.getBlockPos(), SoundEvents.IRON_DOOR_OPEN, SoundSource.BLOCKS);
        }
    }

    public boolean isHeated() {
        boolean heated = BlockStateUtils.isHeatedFromBelow(this.level, this.getBlockPos());
        // Only change the blockstate if it is different.
        if(this.getBlockState().getValue(LIT).booleanValue() != heated) {
            this.level.setBlock(this.getBlockPos(), this.getBlockState().setValue(LIT, heated), Block.UPDATE_ALL);
        }
        return heated;
    }
}
