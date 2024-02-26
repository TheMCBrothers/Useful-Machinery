package net.themcbrothers.usefulmachinery.compat.jade;

import net.themcbrothers.usefulmachinery.block.AbstractMachineBlock;
import net.themcbrothers.usefulmachinery.block.entity.AbstractMachineBlockEntity;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

/**
 * Jade compatibility
 */
@WailaPlugin
public class MachineryJadePlugin implements IWailaPlugin {
    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(MachineProvider.INSTANCE, AbstractMachineBlockEntity.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(MachineProvider.INSTANCE, AbstractMachineBlock.class);
        registration.registerBlockComponent(RemoveVanillaStuffProvider.INSTANCE, AbstractMachineBlock.class);
    }
}
