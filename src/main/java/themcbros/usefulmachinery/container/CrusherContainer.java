package themcbros.usefulmachinery.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraft.world.World;
import themcbros.usefulmachinery.init.ModContainers;
import themcbros.usefulmachinery.recipes.ModRecipeTypes;
import themcbros.usefulmachinery.tileentity.CrusherTileEntity;
import themcbros.usefulmachinery.tileentity.MachineTileEntity;

public class CrusherContainer extends MachineContainer {

    private World world;

    public CrusherContainer(int id, PlayerInventory playerInventory) {
        this(id, playerInventory, new CrusherTileEntity(), new IntArray(5));
    }

    public CrusherContainer(int id, PlayerInventory playerInventory, MachineTileEntity tileEntity, IIntArray fields) {
        super(ModContainers.CRUSHER, id, playerInventory, tileEntity, fields);
        this.world = playerInventory.player.world;

        this.addSlot(new Slot(tileEntity, 0, 56, 17));
        this.addSlot(new Slot(tileEntity, 1, 56, 53));
        this.addSlot(new Slot(tileEntity, 2, 116, 35));

        this.addPlayerSlots(playerInventory);

    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack lvt_3_1_ = ItemStack.EMPTY;
        Slot lvt_4_1_ = this.inventorySlots.get(index);
        if (lvt_4_1_ != null && lvt_4_1_.getHasStack()) {
            ItemStack lvt_5_1_ = lvt_4_1_.getStack();
            lvt_3_1_ = lvt_5_1_.copy();
            if (index == 2) {
                if (!this.mergeItemStack(lvt_5_1_, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }

                lvt_4_1_.onSlotChange(lvt_5_1_, lvt_3_1_);
            } else if (index != 1 && index != 0) {
                if (this.canCrush(lvt_5_1_)) {
                    if (!this.mergeItemStack(lvt_5_1_, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.isItemFuel(lvt_5_1_)) {
                    if (!this.mergeItemStack(lvt_5_1_, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 3 && index < 30) {
                    if (!this.mergeItemStack(lvt_5_1_, 30, 39, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 30 && index < 39 && !this.mergeItemStack(lvt_5_1_, 3, 30, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(lvt_5_1_, 3, 39, false)) {
                return ItemStack.EMPTY;
            }

            if (lvt_5_1_.isEmpty()) {
                lvt_4_1_.putStack(ItemStack.EMPTY);
            } else {
                lvt_4_1_.onSlotChanged();
            }

            if (lvt_5_1_.getCount() == lvt_3_1_.getCount()) {
                return ItemStack.EMPTY;
            }

            lvt_4_1_.onTake(playerIn, lvt_5_1_);
        }

        return lvt_3_1_;
    }

    protected boolean canCrush(ItemStack stack) {
        return this.world.getRecipeManager()
                .getRecipe(ModRecipeTypes.CRUSHING, new Inventory(stack), this.world)
                .isPresent();
    }

    protected boolean isItemFuel(ItemStack stack) {
        return AbstractFurnaceTileEntity.isFuel(stack);
    }

}
