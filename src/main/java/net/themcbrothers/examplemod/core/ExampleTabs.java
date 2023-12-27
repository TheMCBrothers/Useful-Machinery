package net.themcbrothers.examplemod.core;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.neoforge.registries.DeferredHolder;

import static net.themcbrothers.examplemod.core.ExampleBlocks.EXAMPLE_BLOCK;
import static net.themcbrothers.examplemod.core.ExampleItems.EXAMPLE_ITEM;
import static net.themcbrothers.examplemod.core.Registration.CREATIVE_MODE_TABS;

public final class ExampleTabs {
    static void init() {
    }

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE = CREATIVE_MODE_TABS.register("example", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> EXAMPLE_ITEM.get().getDefaultInstance())
            .title(Component.translatable("itemGroup.examplemod.example"))
            .displayItems((parameters, output) -> {
                output.accept(EXAMPLE_ITEM);
                output.accept(EXAMPLE_BLOCK);
            }).build());
}
