package net.themcbrothers.usefulmachinery.compat.jade;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import net.themcbrothers.usefulmachinery.UsefulMachinery;
import net.themcbrothers.usefulmachinery.block.entity.AbstractMachineBlockEntity;
import net.themcbrothers.usefulmachinery.core.MachineryDataComponentTypes;
import net.themcbrothers.usefulmachinery.core.MachineryItems;
import net.themcbrothers.usefulmachinery.machine.MachineTier;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElementHelper;

import java.util.Arrays;

/**
 * Adds the machine's progress and tier information
 */
public enum MachineProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    INSTANCE;

    private static final ResourceLocation UID = UsefulMachinery.rl("machine");

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig pluginConfig) {
        CompoundTag data = accessor.getServerData();
        IElementHelper helper = IElementHelper.get();

        if (data.contains("progress")) {
            ListTag machineItems = data.getList("machine", ListTag.TAG_COMPOUND);
            NonNullList<ItemStack> inventory = NonNullList.withSize(data.getInt("size"), ItemStack.EMPTY);

            for (int i = 0; i < machineItems.size(); ++i) {
                inventory.set(i, ItemStack.parseOptional(accessor.getLevel().registryAccess(), machineItems.getCompound(i)));
            }

            int progress = data.getInt("progress");
            int total = data.getInt("total");

            int count = 0;

            for (int slot : data.getIntArray("inputs")) {
                if (count++ == 0) {
                    tooltip.add(helper.item(inventory.get(slot)));
                } else {
                    tooltip.append(helper.item(inventory.get(slot)));
                }
            }

            // Progress Bar
            tooltip.append(helper.spacer(4, 0));
            tooltip.append(helper.progress((float) progress / (float) total)
                    .translate(new Vec2(-2.0F, 0.0F)));

            Arrays.stream(data.getIntArray("outputs"))
                    .mapToObj(inventory::get)
                    .map(helper::item)
                    .forEach(tooltip::append);
        }

        MachineTier tier = MachineTier.byOrdinal(data.getInt("tier"));

        if (accessor.getPlayer().isShiftKeyDown() && tier.ordinal() > 0) {
            ItemStack itemStack = new ItemStack(MachineryItems.TIER_UPGRADE.get(), 1);
            itemStack.set(MachineryDataComponentTypes.TIER, tier);

            // Tier
            tooltip.add(helper.smallItem(itemStack));
            tooltip.append(helper.spacer(4, 0));
            tooltip.append(helper.text(Component.literal(tier.getSerializedName())));
        }
    }

    @Override
    public void appendServerData(CompoundTag data, BlockAccessor accessor) {
        AbstractMachineBlockEntity machine = (AbstractMachineBlockEntity) accessor.getBlockEntity();

        boolean showJadeProgress = Arrays.stream(machine.getSlotsForFace(null))
                .mapToObj(machine::getItem)
                .anyMatch(itemStack -> !itemStack.isEmpty());

        if (showJadeProgress) {
            ListTag items = new ListTag();

            RegistryAccess registryAccess = accessor.getLevel().registryAccess();

            for (int i = 0; i < machine.getContainerSize(); i++) {
                items.add(machine.getItem(i).saveOptional(registryAccess));
            }

            data.putIntArray("inputs", machine.getInputSlots());
            data.putIntArray("outputs", machine.getOutputSlots());

            data.putInt("size", machine.getContainerSize());
            data.put("machine", items);

            CompoundTag furnaceTag = machine.saveWithoutMetadata(registryAccess);

            data.putInt("progress", furnaceTag.getInt("ProcessTime"));
            data.putInt("total", furnaceTag.getInt("ProcessTimeTotal"));

        }

        data.putInt("tier", machine.getMachineTier(accessor.getBlockState()).ordinal());
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }
}
