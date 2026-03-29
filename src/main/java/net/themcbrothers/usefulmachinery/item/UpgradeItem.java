package net.themcbrothers.usefulmachinery.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.VanillaContainerWrapper;
import net.themcbrothers.usefulmachinery.block.AbstractMachineBlock;
import net.themcbrothers.usefulmachinery.block.entity.AbstractMachineBlockEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static net.themcbrothers.usefulmachinery.UsefulMachinery.TEXT_UTILS;


public class UpgradeItem extends Item {
    private final List<AbstractMachineBlock> supportedBy = new ArrayList<>();

    public UpgradeItem(Properties props, AbstractMachineBlock... machines) {
        super(props);

        this.supportedBy.addAll(Arrays.stream(machines).toList());
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
        if (tooltipFlag.hasShiftDown()) {

            MutableComponent header = TEXT_UTILS.translate("tooltip", "upgrade.shift.header").withStyle(ChatFormatting.GRAY);

            builder.accept(Component.empty());
            builder.accept(header);

            this.supportedBy.stream()
                    .map(machine -> Component.literal(" ")
                            .append(Component.translatable(machine.getDescriptionId()))
                            .withStyle(ChatFormatting.DARK_GREEN))
                    .forEach(builder);

        } else {
            MutableComponent header = TEXT_UTILS.translate("tooltip", "upgrade.header").withStyle(ChatFormatting.GRAY);

            builder.accept(header);
        }
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        InteractionHand hand = context.getHand();

        if (!this.isSupported(level.getBlockState(pos))) {
            return InteractionResult.PASS;
        }

        if (level.getBlockEntity(pos) instanceof AbstractMachineBlockEntity blockEntity) {
            ResourceHandler<ItemResource> upgradeWrapper = VanillaContainerWrapper.of(blockEntity.getUpgradeContainer());
            ItemStack result = ItemHandlerHelper.insertItemStacked(IItemHandler.of(upgradeWrapper), stack, false);

            if (player != null) {
                player.setItemInHand(hand, result);
            }

            if (!ItemStack.matches(stack, result)) {
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }

    public boolean isSupported(BlockState machine) {
        return this.supportedBy.stream().anyMatch(machine::is);
    }
}
