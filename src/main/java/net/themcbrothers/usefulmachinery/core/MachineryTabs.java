package net.themcbrothers.usefulmachinery.core;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.themcbrothers.lib.energy.EnergyContainerItem;
import net.themcbrothers.usefulfoundation.core.FoundationTabs;
import net.themcbrothers.usefulmachinery.item.TierUpgradeItem;
import net.themcbrothers.usefulmachinery.machine.MachineTier;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

import static net.themcbrothers.lib.energy.EnergyContainerItem.TAG_ENERGY;
import static net.themcbrothers.usefulmachinery.UsefulMachinery.TEXT_UTILS;
import static net.themcbrothers.usefulmachinery.core.Registration.CREATIVE_MODE_TABS;

public final class MachineryTabs {
    public static final DeferredHolder<CreativeModeTab, ?> BASE = CREATIVE_MODE_TABS.register("base", () -> CreativeModeTab.builder()
            .withTabsBefore(FoundationTabs.BASE.getId())
            .icon(() -> new ItemStack(MachineryItems.BATTERY.get()))
            .title(TEXT_UTILS.translate("itemGroup", "base"))
            .displayItems((params, output) -> {
                Registration.BLOCKS.getEntries().forEach(block -> output.accept(block.get()));
                Registration.ITEMS.getEntries().stream()
                        .map(DeferredHolder::get)
                        .map(MachineryTabs::considerSpecialNeeds)
                        .forEach(output::acceptAll);
            })
            .build());

    private static Collection<ItemStack> considerSpecialNeeds(ItemLike itemLike) {
        if (itemLike.asItem() instanceof EnergyContainerItem energyContainerItem) {
            ItemStack stack = new ItemStack(itemLike);
            CompoundTag tag = new CompoundTag();

            tag.putInt(TAG_ENERGY, energyContainerItem.getMaxEnergyStored(stack));
            stack.setTag(tag);

            return Collections.singleton(stack);
        } else if (itemLike.asItem() instanceof TierUpgradeItem) {
            Collection<ItemStack> stacks = new HashSet<>();

            for (MachineTier tier : MachineTier.values()) {
                if (tier != MachineTier.SIMPLE) {
                    ItemStack stack = new ItemStack(itemLike);
                    CompoundTag tag = new CompoundTag();

                    tag.putInt("Tier", tier.ordinal());
                    stack.setTag(tag);

                    stacks.add(stack);
                }
            }

            return stacks.stream()
                    .sorted(Comparator.comparingInt(value -> {
                        CompoundTag tag = value.getTag();
                        if (tag != null) {
                            return tag.getInt("Tier");
                        }
                        return 0;
                    }))
                    .toList();
        }

        return Collections.singleton(new ItemStack(itemLike));
    }

    static void init() {
    }
}
