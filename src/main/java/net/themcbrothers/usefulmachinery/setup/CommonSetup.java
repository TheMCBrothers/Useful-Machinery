package net.themcbrothers.usefulmachinery.setup;

import net.minecraft.core.Holder;
import net.minecraft.stats.Stats;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.transfer.energy.InfiniteEnergyHandler;
import net.neoforged.neoforge.transfer.item.VanillaContainerWrapper;
import net.neoforged.neoforge.transfer.item.WorldlyContainerWrapper;
import net.themcbrothers.lib.util.Version;
import net.themcbrothers.usefulmachinery.UsefulMachinery;
import net.themcbrothers.usefulmachinery.core.MachineryBlocks;
import net.themcbrothers.usefulmachinery.core.Registration;
import net.themcbrothers.usefulmachinery.network.MachineryPacketHandler;
import net.themcbrothers.usefulmachinery.util.RecipeHelper;

import static net.themcbrothers.usefulmachinery.core.MachineryBlockEntities.*;

@Mod(UsefulMachinery.MOD_ID)
public class CommonSetup {
    public CommonSetup(IEventBus modEventBus, ModContainer modContainer) {
        // Register stuff
        Registration.register(modEventBus);

        modEventBus.addListener(this::setup);
        modEventBus.addListener(this::enqueueIMC);
        modEventBus.addListener(this::capabilities);

        // NeoForge event
        NeoForge.EVENT_BUS.register(new RecipeHelper());

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
//        if (ModList.get().isLoaded("theoneprobe")) {
//            InterModComms.sendTo(
//                    "theoneprobe", "getTheOneProbe",
//                    net.themcbrothers.usefulmachinery.compat.top.TheOneProbeSupport::new);
//        }
    }

    private void capabilities(final RegisterCapabilitiesEvent event) {
        // Items
        event.registerBlockEntity(Capabilities.Item.BLOCK, COAL_GENERATOR.get(), (sidedContainer, side) -> side == null ? VanillaContainerWrapper.of(sidedContainer) : new WorldlyContainerWrapper(sidedContainer, side));
        event.registerBlockEntity(Capabilities.Item.BLOCK, COMPACTOR.get(), (sidedContainer, side) -> side == null ? VanillaContainerWrapper.of(sidedContainer) : new WorldlyContainerWrapper(sidedContainer, side));
        event.registerBlockEntity(Capabilities.Item.BLOCK, CRUSHER.get(), (sidedContainer, side) -> side == null ? VanillaContainerWrapper.of(sidedContainer) : new WorldlyContainerWrapper(sidedContainer, side));
        event.registerBlockEntity(Capabilities.Item.BLOCK, ELECTRIC_SMELTER.get(), (sidedContainer, side) -> side == null ? VanillaContainerWrapper.of(sidedContainer) : new WorldlyContainerWrapper(sidedContainer, side));
        event.registerBlockEntity(Capabilities.Item.BLOCK, LAVA_GENERATOR.get(), (sidedContainer, side) -> side == null ? VanillaContainerWrapper.of(sidedContainer) : new WorldlyContainerWrapper(sidedContainer, side));

        // Energy
        event.registerBlockEntity(Capabilities.Energy.BLOCK, COAL_GENERATOR.get(), (machine, _) -> machine.getEnergyStorage());
        event.registerBlockEntity(Capabilities.Energy.BLOCK, COMPACTOR.get(), (machine, _) -> machine.getEnergyStorage());
        event.registerBlockEntity(Capabilities.Energy.BLOCK, CRUSHER.get(), (machine, _) -> machine.getEnergyStorage());
        event.registerBlockEntity(Capabilities.Energy.BLOCK, ELECTRIC_SMELTER.get(), (machine, _) -> machine.getEnergyStorage());
        event.registerBlockEntity(Capabilities.Energy.BLOCK, LAVA_GENERATOR.get(), (machine, _) -> machine.getEnergyStorage());
        event.registerBlockEntity(Capabilities.Energy.BLOCK, CREATIVE_POWER_CELL.get(), (creativePowerCell, _) -> creativePowerCell.getEnergyStorage());
        event.registerItem(Capabilities.Energy.ITEM, (_, _) -> InfiniteEnergyHandler.INSTANCE, MachineryBlocks.CREATIVE_POWER_CELL.get());

        // Fluid
        event.registerBlockEntity(Capabilities.Fluid.BLOCK, LAVA_GENERATOR.get(), (generator, _) -> generator.getLavaTankHandler());
    }
}
