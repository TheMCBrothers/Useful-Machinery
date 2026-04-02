package net.themcbrothers.usefulmachinery.menu;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import net.themcbrothers.lib.inventory.EnergySlot;
import net.themcbrothers.lib.util.ContainerHelper;
import net.themcbrothers.usefulmachinery.block.entity.AbstractMachineBlockEntity;
import net.themcbrothers.usefulmachinery.block.entity.LavaGeneratorBlockEntity;
import net.themcbrothers.usefulmachinery.block.entity.extension.UpgradeContainer;
import net.themcbrothers.usefulmachinery.core.MachineryMenus;
import net.themcbrothers.usefulmachinery.menu.slot.FluidItemSlot;
import net.themcbrothers.usefulmachinery.menu.slot.OutputSlot;

import static net.themcbrothers.usefulmachinery.core.MachineryItems.SUSTAINED_UPGRADE;

public class LavaGeneratorMenu extends AbstractMachineMenu {
    public LavaGeneratorMenu(int id, Inventory inventory, FriendlyByteBuf byteBuf) {
        this(id, inventory, ContainerHelper.getBlockEntity(AbstractMachineBlockEntity.class, inventory, byteBuf),
                new UpgradeContainer(byteBuf.readInt()), new SimpleContainerData(byteBuf.readInt()));
    }

    public LavaGeneratorMenu(int id, Inventory inventory, AbstractMachineBlockEntity blockEntity, Container upgradeContainer, ContainerData fields) {
        super(MachineryMenus.LAVA_GENERATOR.get(), id, blockEntity, fields, upgradeContainer.getContainerSize(), inventory);

        this.addSlot(new FluidItemSlot(blockEntity, 0, 26, 17, stack -> stack.getFluid().isSame(Fluids.LAVA)));
        this.addSlot(new OutputSlot(blockEntity, 1, 26, 51));
        this.addSlot(new EnergySlot(blockEntity, 2, 134, 33));

        this.addUpgradeSlots(upgradeContainer);
        this.addPlayerSlots(inventory);
    }

    @Override
    protected boolean supportsUpgrade(ItemStack stack) {
        return stack.is(SUSTAINED_UPGRADE.get());
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        // information about slot indexes
        final int containerSize = this.blockEntity.getContainerSize();
        final int invSlotStart = containerSize + this.upgradeSlotCount;
        final int invSlotEnd = invSlotStart + 27;
        final int hotbarSlotStart = invSlotEnd;
        final int hotbarSlotEnd = hotbarSlotStart + 9;

        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            stack = slotStack.copy();

            FluidStacksResourceHandler lavaTankHandler = ((LavaGeneratorBlockEntity) this.blockEntity).getLavaTankHandler();
            ItemAccess itemAccess = ItemAccess.forStack(slotStack);
            ResourceHandler<FluidResource> itemFluidHandler = itemAccess.getCapability(Capabilities.Fluid.ITEM);
            boolean isSuccess;

            if (itemFluidHandler == null) {
                return ItemStack.EMPTY;
            }

            FluidResource itemFluidResource = itemFluidHandler.getResource(0);

            // Checking if shift clicking stack out of inventory into the machine
            if (index >= invSlotStart) {
                try (Transaction transaction = Transaction.openRoot()) {
                    int itemAmount = itemFluidHandler.getAmountAsInt(0);
                    int amount = lavaTankHandler.insert(itemFluidResource, itemAmount, transaction);

                    isSuccess = amount > 0;
                } catch (Exception e) {
                    return ItemStack.EMPTY;
                }

                if (isSuccess) {
                    // Checking if stack has not been moved into fuel slot
                    if (!this.moveItemStackTo(slotStack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.isEnergyItem(slotStack, EnergySlot.ItemMode.RECEIVE)) {
                    // Checking if stack has not been moved into energy slot
                    if (!this.moveItemStackTo(slotStack, containerSize - 1, containerSize, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.supportsUpgrade(slotStack)) {
                    // Checking if stack has not been moved into the upgrade container
                    if (!this.moveItemStackTo(slotStack, containerSize, invSlotStart, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                // Checking if shift clicking stack from the inventory into the hotbar
                else if (index < invSlotEnd) {
                    // Checking if stack has not been moved into the hotbar
                    if (!this.moveItemStackTo(slotStack, invSlotEnd, hotbarSlotEnd, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                // Checking if shift clicking stack from the hotbar into the inventory
                else if (index < hotbarSlotEnd) {
                    // Checking if stack has not been moved into the inventory
                    if (!this.moveItemStackTo(slotStack, invSlotStart, invSlotEnd, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            }
            // Checking if shift clicking stack from the hotbar into the inventory
            else if (!this.moveItemStackTo(slotStack, invSlotStart, hotbarSlotEnd, false)) {
                return ItemStack.EMPTY;
            }

            // If here then logic successful
            if (slotStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (slotStack.getCount() == stack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, slotStack);
        }

        return stack;
    }

    @Override
    public boolean isProcessing() {
        return this.fields.get(4) > 0;
    }

    @Override
    public int getProgressScaled(int size) {
        int burnTime = this.fields.get(4);
        int totalBurnTime = this.fields.get(5);

        return totalBurnTime != 0 ? burnTime * size / totalBurnTime : 0;
    }

    public int getFluidAmount() {
        return this.fields.get(6);
    }

    public int getTankCapacity() {
        return this.fields.get(7) > 0 ? this.fields.get(7) : LavaGeneratorBlockEntity.TANK_CAPACITY;
    }

    public Fluid getTankFluid() {
        return BuiltInRegistries.FLUID.byId(this.fields.get(8));
    }

    public ResourceHandler<FluidResource> getFluidTankHandler() {
        return new ResourceHandler<>() {
            @Override
            public int size() {
                return 0;
            }

            @Override
            public FluidResource getResource(int index) {
                return FluidResource.of(LavaGeneratorMenu.this.getTankFluid());
            }

            @Override
            public long getAmountAsLong(int index) {
                return LavaGeneratorMenu.this.getFluidAmount();
            }

            @Override
            public long getCapacityAsLong(int index, FluidResource resource) {
                return LavaGeneratorMenu.this.getTankCapacity();
            }

            @Override
            public boolean isValid(int index, FluidResource resource) {
                return true;
            }

            @Override
            public int insert(int index, FluidResource resource, int amount, TransactionContext transaction) {
                return 0;
            }

            @Override
            public int extract(int index, FluidResource resource, int amount, TransactionContext transaction) {
                return 0;
            }
        };
    }
}
