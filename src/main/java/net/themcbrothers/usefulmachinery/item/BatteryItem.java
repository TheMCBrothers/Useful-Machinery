package net.themcbrothers.usefulmachinery.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.themcbrothers.lib.LibDataComponents;
import net.themcbrothers.lib.config.Config;
import net.themcbrothers.lib.energy.BasicEnergyContainerItem;
import net.themcbrothers.lib.energy.EnergyUnit;

import java.util.List;

import static net.themcbrothers.lib.TheMCBrosLib.TEXT_UTILS;

public class BatteryItem extends BasicEnergyContainerItem {
    public BatteryItem(Properties properties) {
        super(10_000, 100, 100, properties);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return stack.getOrDefault(LibDataComponents.ENERGY, 0) > 0;
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return 0xFF0000;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        int stored = this.getCapacity() - stack.getOrDefault(LibDataComponents.ENERGY, 0) + 1;
        int max = this.getCapacity() + 1;

        return (int) Math.round(13.0 - stored * 13.0 / max);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        EnergyUnit energyUnit = Config.CLIENT_CONFIG.getEnergyUnit();

        MutableComponent component = TEXT_UTILS.energyWithMax(stack.getOrDefault(LibDataComponents.ENERGY, 0), this.getCapacity(), energyUnit);
        tooltipComponents.add(component.withStyle(ChatFormatting.GRAY));
    }
}
