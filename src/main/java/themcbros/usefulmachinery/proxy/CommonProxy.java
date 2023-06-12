package themcbros.usefulmachinery.proxy;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.themcbrothers.lib.energy.EnergyContainerItem;
import themcbros.usefulmachinery.UsefulMachinery;
import themcbros.usefulmachinery.compat.top.TheOneProbeSupport;
import themcbros.usefulmachinery.init.MachineryCreativeModeTabs;
import themcbros.usefulmachinery.init.Registration;
import themcbros.usefulmachinery.items.TierUpgradeItem;
import themcbros.usefulmachinery.machine.MachineTier;
import themcbros.usefulmachinery.networking.Networking;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.stream.Collectors;

import static themcbros.usefulmachinery.items.BatteryItem.ENERGY;

public class CommonProxy {
    CommonProxy() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::enqueueIMC);
        modEventBus.addListener(this::processIMC);
        modEventBus.addListener(this::buildContentsCreativeModeTab);

        Networking.init();
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        if (ModList.get().isLoaded("theoneprobe")) {
            InterModComms.sendTo("theoneprobe", "getTheOneProbe", TheOneProbeSupport::new);
        }
    }

    private void processIMC(final InterModProcessEvent event) {
        // some example code to receive and process InterModComms from other mods
        UsefulMachinery.LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m -> m.messageSupplier().get()).
                collect(Collectors.toList()));
    }

    private void buildContentsCreativeModeTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTab() == MachineryCreativeModeTabs.BASE.get()) {
            Registration.BLOCKS.getEntries().forEach(block -> event.accept(block.get(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS));
            Registration.ITEMS.getEntries().forEach((item) -> event.acceptAll(considerSpecialNeeds(item.get()), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS));
        }
    }

    private Collection<ItemStack> considerSpecialNeeds(ItemLike itemLike) {
        if (itemLike.asItem() instanceof EnergyContainerItem energyContainerItem) {
            ItemStack itemStack = new ItemStack(itemLike);
            CompoundTag tag = new CompoundTag();

            tag.putInt(ENERGY, energyContainerItem.getMaxEnergyStored(itemStack));
            itemStack.setTag(tag);
            return Collections.singleton(itemStack);
        } else if (itemLike.asItem() instanceof TierUpgradeItem) {
            Collection<ItemStack> itemStacks = new HashSet<>();

            for (MachineTier tier : MachineTier.values()) {
                if (tier != MachineTier.SIMPLE) {
                    ItemStack stack = new ItemStack(itemLike);
                    CompoundTag tag = new CompoundTag();

                    tag.putInt("Tier", tier.ordinal());
                    stack.setTag(tag);

                    itemStacks.add(stack);
                }
            }

            return itemStacks.stream()
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
}
