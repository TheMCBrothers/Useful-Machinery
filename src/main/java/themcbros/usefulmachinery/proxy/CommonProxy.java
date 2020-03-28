package themcbros.usefulmachinery.proxy;

import dan200.computercraft.api.ComputerCraftAPI;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import themcbros.usefulmachinery.UsefulMachinery;
import themcbros.usefulmachinery.compat.computercraft.CCCompat;
import themcbros.usefulmachinery.compat.top.TheOneProbeSupport;
import themcbros.usefulmachinery.networking.Networking;

import java.util.stream.Collectors;

public class CommonProxy {

    CommonProxy() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);

        Networking.init();
    }

    private void setup(final FMLCommonSetupEvent event) {

    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        if (ModList.get().isLoaded("theoneprobe"))
            InterModComms.sendTo("theoneprobe", "getTheOneProbe", TheOneProbeSupport::new);
        if (ModList.get().isLoaded("computercraft")) {
            ComputerCraftAPI.registerBundledRedstoneProvider(new CCCompat());
            ComputerCraftAPI.registerPeripheralProvider(new CCCompat());
        }
    }

    private void processIMC(final InterModProcessEvent event) {
        // some example code to receive and process InterModComms from other mods
        UsefulMachinery.LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m -> m.getMessageSupplier().get()).
                collect(Collectors.toList()));
    }

}
