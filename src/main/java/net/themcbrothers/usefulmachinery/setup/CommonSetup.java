package net.themcbrothers.usefulmachinery.setup;

import net.minecraft.core.Holder;
import net.minecraft.stats.Stats;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.InterModComms;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import net.neoforged.neoforge.items.wrapper.SidedInvWrapper;
import net.themcbrothers.lib.energy.EnergyContainerItem;
import net.themcbrothers.lib.util.Version;
import net.themcbrothers.usefulmachinery.core.MachineryBlocks;
import net.themcbrothers.usefulmachinery.core.MachineryItems;
import net.themcbrothers.usefulmachinery.core.Registration;
import net.themcbrothers.usefulmachinery.network.MachineryPacketHandler;

import static net.themcbrothers.usefulmachinery.core.MachineryBlockEntities.*;

public class CommonSetup {
    protected CommonSetup(IEventBus modEventBus, ModContainer modContainer) {
        // Register stuff
        Registration.register(modEventBus);

        modEventBus.addListener(this::setup);
        modEventBus.addListener(this::enqueueIMC);
        modEventBus.addListener(this::capabilities);

        // Networking
        new MachineryPacketHandler(modEventBus, new Version(modContainer));
    }

    private void setup(final FMLCommonSetupEvent event) {
        // Make sure the stats appear in the menu
        event.enqueueWork(() -> Registration.CUSTOM_STATS.getEntries().stream()
                .map(Holder::value)
                .forEach(Stats.CUSTOM::get));
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        if (ModList.get().isLoaded("theoneprobe")) {
            InterModComms.sendTo(
                    "theoneprobe", "getTheOneProbe",
                    net.themcbrothers.usefulmachinery.compat.top.TheOneProbeSupport::new);
        }
    }

    private void capabilities(final RegisterCapabilitiesEvent event) {
        // Items
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, COAL_GENERATOR.get(), (sidedContainer, side) -> side == null ? new InvWrapper(sidedContainer) : new SidedInvWrapper(sidedContainer, side));
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, COMPACTOR.get(), (sidedContainer, side) -> side == null ? new InvWrapper(sidedContainer) : new SidedInvWrapper(sidedContainer, side));
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, CRUSHER.get(), (sidedContainer, side) -> side == null ? new InvWrapper(sidedContainer) : new SidedInvWrapper(sidedContainer, side));
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ELECTRIC_SMELTER.get(), (sidedContainer, side) -> side == null ? new InvWrapper(sidedContainer) : new SidedInvWrapper(sidedContainer, side));
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, LAVA_GENERATOR.get(), (sidedContainer, side) -> side == null ? new InvWrapper(sidedContainer) : new SidedInvWrapper(sidedContainer, side));

        // Energy
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, COAL_GENERATOR.get(), (machine, context) -> machine.getEnergyStorage());
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, COMPACTOR.get(), (machine, context) -> machine.getEnergyStorage());
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, CRUSHER.get(), (machine, context) -> machine.getEnergyStorage());
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, ELECTRIC_SMELTER.get(), (machine, context) -> machine.getEnergyStorage());
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, LAVA_GENERATOR.get(), (machine, context) -> machine.getEnergyStorage());
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, CREATIVE_POWER_CELL.get(), (machine, context) -> machine);

        // Fluid
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, LAVA_GENERATOR.get(), (machine, context) -> machine.getLavaTank());

        // Item Energy
        event.registerItem(Capabilities.EnergyStorage.ITEM, this::energyItem, MachineryBlocks.CREATIVE_POWER_CELL, MachineryItems.BATTERY);
    }

    private IEnergyStorage energyItem(ItemStack stack, Void context) {
        if (stack.getItem() instanceof EnergyContainerItem energyItem) {
            return new IEnergyStorage() {
                @Override
                public int receiveEnergy(int maxReceive, boolean simulate) {
                    return energyItem.receiveEnergy(stack, maxReceive, simulate);
                }

                @Override
                public int extractEnergy(int maxExtract, boolean simulate) {
                    return energyItem.extractEnergy(stack, maxExtract, simulate);
                }

                @Override
                public int getEnergyStored() {
                    return energyItem.getEnergyStored(stack);
                }

                @Override
                public int getMaxEnergyStored() {
                    return energyItem.getMaxEnergyStored(stack);
                }

                @Override
                public boolean canExtract() {
                    return extractEnergy(1, true) > 0;
                }

                @Override
                public boolean canReceive() {
                    return receiveEnergy(1, true) > 0;
                }
            };
        }

        return null;
    }
}
