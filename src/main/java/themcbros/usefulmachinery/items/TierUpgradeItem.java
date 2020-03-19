package themcbros.usefulmachinery.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import themcbros.usefulmachinery.machine.MachineTier;
import themcbros.usefulmachinery.tileentity.MachineTileEntity;

public class TierUpgradeItem extends UpgradeItem {

    public TierUpgradeItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        final PlayerEntity playerEntity = context.getPlayer();
        final World world = context.getWorld();
        final BlockPos pos = context.getPos();
        final ItemStack stack = context.getItem();
        final TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof MachineTileEntity) {
            MachineTileEntity machineTileEntity = (MachineTileEntity) tileEntity;
            MachineTier machineTier = machineTileEntity.machineTier;
            if (stack.hasTag() && stack.getTag() != null && stack.getTag().contains("Tier", Constants.NBT.TAG_INT)) {
                MachineTier itemTier = MachineTier.byOrdinal(stack.getTag().getInt("Tier"));
                if (itemTier.ordinal() == machineTier.ordinal() + 1) {
                    machineTileEntity.machineTier = itemTier;
                    machineTileEntity.markDirty();
                    if (playerEntity != null)
                        playerEntity.sendStatusMessage(new StringTextComponent("Successfully upgraded machine to " + itemTier.getName())
                                .applyTextStyle(TextFormatting.GREEN), true);
                    return ActionResultType.SUCCESS;
                }
            } else if (playerEntity != null) {
                playerEntity.sendStatusMessage(new StringTextComponent("This is not a valid upgrade item").applyTextStyle(TextFormatting.RED), true);
            }
        }
        return ActionResultType.PASS;
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack stack = new ItemStack(this);
        CompoundNBT tag = new CompoundNBT();
        tag.putInt("Tier", 0);
        stack.setTag(tag);
        return stack;
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        if (!stack.isEmpty() && stack.hasTag() && stack.getTag() != null) {
            if (stack.getTag().contains("Tier", Constants.NBT.TAG_INT)) {
                MachineTier tier = MachineTier.byOrdinal(stack.getTag().getInt("Tier"));
                return this.getTranslationKey() + "_" + tier.getName();
            }
        }
        return this.getTranslationKey();
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if(this.isInGroup(group)) {
            for(MachineTier tier : MachineTier.values()) {
                if (tier != MachineTier.LEADSTONE) {
                    ItemStack stack = new ItemStack(this);
                    CompoundNBT tag = new CompoundNBT();
                    tag.putInt("Tier", tier.ordinal());
                    stack.setTag(tag);
                    items.add(stack);
                }
            }
        }
    }
}
