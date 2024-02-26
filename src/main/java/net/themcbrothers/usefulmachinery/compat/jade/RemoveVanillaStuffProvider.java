package net.themcbrothers.usefulmachinery.compat.jade;

import net.minecraft.resources.ResourceLocation;
import net.themcbrothers.usefulmachinery.UsefulMachinery;
import net.themcbrothers.usefulmachinery.block.entity.AbstractMachineBlockEntity;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.Identifiers;
import snownee.jade.api.config.IPluginConfig;

public enum RemoveVanillaStuffProvider implements IBlockComponentProvider {
    INSTANCE;

    private static final ResourceLocation UID = UsefulMachinery.rl("remove_vanilla_stuff");

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig pluginConfig) {
        if (accessor.getBlockEntity() instanceof AbstractMachineBlockEntity) {
            tooltip.remove(Identifiers.UNIVERSAL_ITEM_STORAGE);
        }
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public int getDefaultPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isRequired() {
        return true;
    }
}
