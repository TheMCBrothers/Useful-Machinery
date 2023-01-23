package themcbros.usefulmachinery.items;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import themcbros.usefulmachinery.blockentity.AbstractMachineBlockEntity;
import themcbros.usefulmachinery.machine.MachineTier;

import javax.annotation.Nonnull;

public class TierUpgradeItem extends UpgradeItem {
    public TierUpgradeItem(Properties properties) {
        super(properties);
    }

    @Nonnull
    @Override
    public InteractionResult useOn(UseOnContext context) {
        final Player playerEntity = context.getPlayer();
        final Level level = context.getLevel();
        final BlockPos pos = context.getClickedPos();
        final ItemStack stack = context.getItemInHand();
        final BlockEntity blockEntity = level.getBlockEntity(pos);
        final BlockState blockState = context.getLevel().getBlockState(pos);

        if (blockEntity instanceof AbstractMachineBlockEntity abstractMachineBlockEntity) {
            MachineTier machineTier = abstractMachineBlockEntity.getMachineTier();

            if (stack.hasTag() && stack.getTag() != null && stack.getTag().contains("Tier", Tag.TAG_INT)) {
                MachineTier itemTier = MachineTier.byOrdinal(stack.getTag().getInt("Tier"));
                if (itemTier.ordinal() == machineTier.ordinal() + 1) {
                    abstractMachineBlockEntity.setMachineTier(itemTier);
                    abstractMachineBlockEntity.setChanged();
                    abstractMachineBlockEntity.getEnergyStorage().setMaxEnergyStored(20000 * (itemTier.ordinal() + 1));
                    level.sendBlockUpdated(pos, blockState, blockState, 4);
                    if (playerEntity != null) {
                        playerEntity.displayClientMessage(Component.translatable("Successfully upgraded machine to " + itemTier.getSerializedName()).withStyle(ChatFormatting.GREEN), true);
                    }
                    return InteractionResult.SUCCESS;
                }
            } else if (playerEntity != null) {
                playerEntity.displayClientMessage(Component.translatable("This is not a valid upgrade item").withStyle(ChatFormatting.RED), true);
            }
        }

        return InteractionResult.PASS;
    }

    @Nonnull
    @Override
    public ItemStack getDefaultInstance() {
        ItemStack stack = new ItemStack(this);
        CompoundTag tag = new CompoundTag();

        tag.putInt("Tier", 0);
        stack.setTag(tag);

        return stack;
    }

    @Nonnull
    @Override
    public String getDescriptionId(ItemStack stack) {
        if (!stack.isEmpty() && stack.hasTag() && stack.getTag() != null) {
            if (stack.getTag().contains("Tier", Tag.TAG_INT)) {
                MachineTier tier = MachineTier.byOrdinal(stack.getTag().getInt("Tier"));
                return this.getDescriptionId() + "_" + tier.getSerializedName();
            }
        }

        return this.getDescriptionId();
    }

    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
        if (this.allowedIn(group)) {
            for (MachineTier tier : MachineTier.values()) {
                if (tier != MachineTier.SIMPLE) {
                    ItemStack stack = new ItemStack(this);
                    CompoundTag tag = new CompoundTag();

                    tag.putInt("Tier", tier.ordinal());
                    stack.setTag(tag);

                    items.add(stack);
                }
            }
        }
    }
}
