package net.themcbrothers.usefulmachinery.item;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import net.themcbrothers.usefulmachinery.block.AbstractMachineBlock;
import net.themcbrothers.usefulmachinery.block.entity.AbstractMachineBlockEntity;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.themcbrothers.usefulmachinery.UsefulMachinery.TEXT_UTILS;


public class UpgradeItem extends Item {
    private final List<AbstractMachineBlock> supportedBy = new ArrayList<>();

    public UpgradeItem(Properties props, AbstractMachineBlock... machines) {
        super(props);

        this.supportedBy.addAll(Arrays.stream(machines).toList());
    }

    @Override
    public final InteractionResult useOn(UseOnContext context) {
        return this.useOn(context.getPlayer(), context.getHand(), context.getItemInHand(), context.getLevel(), context.getClickedPos());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag tooltipFlag) {

        if (GLFW.glfwGetKey(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_SHIFT) == GLFW.GLFW_PRESS) {

            MutableComponent header = TEXT_UTILS.translate("tooltip", "upgrade.shift.header").withStyle(ChatFormatting.GRAY);

            components.add(Component.empty());
            components.add(header);

            this.supportedBy.stream()
                    .map(machine -> Component.literal(" ")
                            .append(Component.translatable(machine.getDescriptionId()))
                            .withStyle(ChatFormatting.DARK_GREEN))
                    .forEach(components::add);

        } else {
            MutableComponent header = TEXT_UTILS.translate("tooltip", "upgrade.header").withStyle(ChatFormatting.GRAY);

            components.add(header);
        }
    }

    public InteractionResult useOn(@Nullable Player player, InteractionHand hand, ItemStack stack, Level level, BlockPos pos) {
        if (!this.isSupported(level.getBlockState(pos))) {
            return InteractionResult.PASS;
        }

        if (level.getBlockEntity(pos) instanceof AbstractMachineBlockEntity blockEntity) {
            InvWrapper upgradeWrapper = new InvWrapper(blockEntity.getUpgradeContainer());
            ItemStack result = ItemHandlerHelper.insertItemStacked(upgradeWrapper, stack, false);

            if (player != null) {
                player.setItemInHand(hand, result);
            }

            if (!ItemStack.matches(stack, result)) {
                return InteractionResult.sidedSuccess(level.isClientSide());
            }
        }

        return InteractionResult.PASS;
    }

    public boolean isSupported(BlockState machine) {
        return this.supportedBy.stream().anyMatch(machine::is);
    }
}
