package net.themcbrothers.usefulmachinery.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.VanillaContainerWrapper;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.themcbrothers.usefulmachinery.component.MachineContents;
import net.themcbrothers.usefulmachinery.core.MachineryBlockEntities;
import net.themcbrothers.usefulmachinery.core.MachineryItems;
import net.themcbrothers.usefulmachinery.machine.RedstoneMode;
import net.themcbrothers.usefulmachinery.menu.LavaGeneratorMenu;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

import static net.themcbrothers.usefulmachinery.UsefulMachinery.TEXT_UTILS;
import static net.themcbrothers.usefulmachinery.core.MachineryDataComponentTypes.CONTENTS;
import static net.themcbrothers.usefulmachinery.core.MachineryDataComponentTypes.TANK;

public class LavaGeneratorBlockEntity extends AbstractMachineBlockEntity {
    public static final int TANK_CAPACITY = 4000; // TODO config
    public static final int TICKS_PER_MB = 5; // TODO config
    public static final int MB_PER_USE = 20; // TODO config
    public static final int BASE_TICKING_ENERGY = 150; // TODO evaluate if should be in config
    private int burnTime;
    private int burnTimeTotal;
    private final FluidStacksResourceHandler lavaTankHandler;
    private final ContainerData fields = new ContainerData() {
        @Override
        public int getCount() {
            return 9;
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 2 -> LavaGeneratorBlockEntity.this.redstoneMode = RedstoneMode.byOrdinal(value);
                case 4 -> LavaGeneratorBlockEntity.this.burnTime = value;
                case 5 -> LavaGeneratorBlockEntity.this.burnTimeTotal = value;
                default -> {
                }
            }
        }

        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> LavaGeneratorBlockEntity.this.getEnergyStored();
                case 1 -> LavaGeneratorBlockEntity.this.getMaxEnergyStored();
                case 2 -> LavaGeneratorBlockEntity.this.redstoneMode.ordinal();
                case 3 -> LavaGeneratorBlockEntity.this.getUpgradeSlotSize();
                case 4 -> LavaGeneratorBlockEntity.this.burnTime;
                case 5 -> LavaGeneratorBlockEntity.this.burnTimeTotal;
                case 6 -> LavaGeneratorBlockEntity.this.lavaTankHandler.getAmountAsInt(0);
                case 7 -> LavaGeneratorBlockEntity.this.lavaTankHandler.getCapacityAsInt(0, FluidResource.EMPTY);
                case 8 ->
                        BuiltInRegistries.FLUID.getId(LavaGeneratorBlockEntity.this.lavaTankHandler.getResource(0).getFluid());
                default -> 0;
            };
        }
    };

    public LavaGeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(MachineryBlockEntities.LAVA_GENERATOR.get(), pos, state, true);

        this.lavaTankHandler = new FluidStacksResourceHandler(1, TANK_CAPACITY) {
            @Override
            public boolean isValid(int index, FluidResource resource) {
                return resource.is(Tags.Fluids.LAVA);
            }
        };
    }

    @Override
    public int[] getInputSlots() {
        return new int[]{0};
    }

    @Override
    public int[] getOutputSlots() {
        return new int[0];
    }

    @Override
    protected boolean canRun() {
        boolean canRun = this.redstoneMode.canRun(this);
        boolean canGenerate = this.energyStorage.getAmountAsInt() <= this.energyStorage.getCapacityAsInt() - BASE_TICKING_ENERGY;

        return this.level != null && canRun && canGenerate;
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter components) {
        super.applyImplicitComponents(components);

        MachineContents contents = components.get(CONTENTS.get());

        if (contents != null) {
            this.burnTime = contents.burnTime();
            this.burnTimeTotal = contents.burnTimeTotal();
        }

        SimpleFluidContent simpleFluidContent = components.get(TANK.get());

        if (simpleFluidContent != null) {
            int amount = simpleFluidContent.getAmount();
            FluidResource fluidResource = FluidResource.of(simpleFluidContent.getFluid());

            this.lavaTankHandler.set(0, fluidResource, amount);
        }
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder builder) {
        super.collectImplicitComponents(builder);

        builder.set(CONTENTS.get(), new MachineContents(ItemContainerContents.fromItems(
                this.upgradeContainer.getItems()),
                this.energyStorage.getAmountAsInt(),
                this.redstoneMode,
                this.processTime,
                this.processTimeTotal,
                this.burnTime,
                this.burnTimeTotal
        ));
        builder.set(TANK.get(), SimpleFluidContent.copyOf(FluidUtil.getStack(this.lavaTankHandler, 0)));
    }

    @Override
    public void removeComponentsFromTag(ValueOutput output) {
        super.removeComponentsFromTag(output);

        output.discard("Tank");
    }

    @Override
    public ContainerData getContainerData() {
        return this.fields;
    }

    @Override
    public int getContainerSize() {
        return 3;
    }

    @Override
    public Component getDefaultName() {
        return TEXT_UTILS.translate("container", "lava_generator");
    }

    public AbstractContainerMenu createMenu(int id, Inventory playerInventory) {
        return new LavaGeneratorMenu(id, playerInventory, this, this.getUpgradeContainer(), this.getContainerData());
    }

    @Override
    public void saveAdditional(ValueOutput compound) {
        super.saveAdditional(compound);

        compound.putInt("BurnTime", this.burnTime);
        compound.putInt("BurnTimeTotal", this.burnTimeTotal);

        compound.store("Tank", FluidStack.OPTIONAL_CODEC, FluidUtil.getStack(this.lavaTankHandler, 0));
    }

    @Override
    public void loadAdditional(ValueInput input) {
        super.loadAdditional(input);

        this.burnTime = input.getIntOr("BurnTime", 0);
        this.burnTimeTotal = input.getIntOr("BurnTimeTotal", 0);

        Optional<FluidStack> tank = input.read("Tank", FluidStack.OPTIONAL_CODEC);
        int amount = tank.map(FluidStack::amount).orElse(0);
        Fluid fluid = tank.map(FluidStack::getFluid).orElse(FluidStack.EMPTY.getFluid());

        this.lavaTankHandler.set(0, FluidResource.of(fluid), amount);
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack stack, @Nullable Direction direction) {
        boolean isLavaInputSlot = index == 0;
        boolean isItemFluidValid = this.lavaTankHandler.isValid(0, FluidResource.of(FluidUtil.getFirstStackContained(stack)));

        return isLavaInputSlot && isItemFluidValid;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return index == 1;
    }

    @Override
    public void tick() {
        super.tick();

        boolean shouldLit = false;
        boolean shouldSave = false;

        this.transferFluid();

        if (this.canRun() && this.hasFuel() && this.burnTime <= 0) {
            shouldLit = this.consumeFuel();
        }

        if (this.burnTime > 0) {
            --this.burnTime;

            int upgradeCount = this.getUpgradeCount(MachineryItems.SUSTAINED_UPGRADE);

            // Calc the multiplier for the generation
            double multiplier = upgradeCount * BASE_TICKING_ENERGY / (double) this.upgradeContainer.getMaxStackSize();

            // Generate the energy
            this.energyStorage.growEnergy((int) (BASE_TICKING_ENERGY + multiplier));

            shouldLit = true;
            shouldSave = true;
        }

        this.sendEnergyToSlot();
        this.sendUpdate(shouldLit);
        this.sendEnergy();

        if (shouldSave) {
            this.setChanged();
        }
    }

    private void transferFluid() {
        final ItemStack bucketStack = this.getItems().get(0);

        // Temporary single-slot handler seeded with the current stack
        ResourceHandler<ItemResource> tempHandler = VanillaContainerWrapper.of(new SimpleContainer(bucketStack));
        ItemAccess itemAccess = ItemAccess.forHandlerIndexStrict(tempHandler, 0);
        ResourceHandler<FluidResource> itemFluidCapability = itemAccess.getCapability(Capabilities.Fluid.ITEM);

        if (itemFluidCapability == null) {
            return;
        }

        FluidResource itemFluidResource = itemFluidCapability.getResource(0);

        if (!bucketStack.isEmpty()) {
            try (Transaction transaction = Transaction.openRoot()) {
                int itemAmount = itemFluidCapability.getAmountAsInt(0);
                int insertedAmount = this.lavaTankHandler.insert(itemFluidResource, itemAmount, transaction);

                // Exit if insertion failed. Most likely when tank is already full.
                if (insertedAmount != itemAmount) {
                    return;
                }

                itemFluidCapability.extract(itemFluidResource, insertedAmount, transaction);

                ItemStack outputSlotStack = this.getItems().get(1);
                ItemStack resultStack = tempHandler.getResource(0).toStack();

                if (ItemStack.isSameItem(resultStack, outputSlotStack) && resultStack.getMaxStackSize() > 1 && outputSlotStack.getCount() <= outputSlotStack.getMaxStackSize() - resultStack.getCount()) {
                    outputSlotStack.grow(resultStack.getCount());
                    bucketStack.shrink(1);
                } else if (outputSlotStack.isEmpty()) {
                    this.getItems().set(1, resultStack);

                    bucketStack.shrink(1);
                }

                transaction.commit();
            }
        }
    }

    private boolean hasFuel() {
        return this.lavaTankHandler.getAmountAsInt(0) >= MB_PER_USE;
    }

    private boolean consumeFuel() {
        try (Transaction transaction = Transaction.openRoot()) {
            FluidResource resource = this.lavaTankHandler.getResource(0);
            int extractedAmount = this.lavaTankHandler.extract(resource, MB_PER_USE, transaction);

            this.burnTime = this.calcBurnTime(TICKS_PER_MB * extractedAmount);
            this.burnTimeTotal = LavaGeneratorBlockEntity.TICKS_PER_MB * LavaGeneratorBlockEntity.MB_PER_USE;

            transaction.commit();

            return true;
        }
    }

    public FluidStacksResourceHandler getLavaTankHandler() {
        return this.lavaTankHandler;
    }
}
