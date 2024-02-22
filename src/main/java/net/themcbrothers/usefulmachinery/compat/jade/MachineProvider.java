package net.themcbrothers.usefulmachinery.compat.jade;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import net.themcbrothers.usefulmachinery.UsefulMachinery;
import net.themcbrothers.usefulmachinery.block.entity.AbstractMachineBlockEntity;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElementHelper;

public enum MachineProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    INSTANCE;

    private static final ResourceLocation UID = UsefulMachinery.rl("machine");

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig pluginConfig) {
        CompoundTag data = accessor.getServerData();
        if (data.contains("progress")) {
            ListTag machineItems = data.getList("machine", ListTag.TAG_COMPOUND);
            NonNullList<ItemStack> inventory = NonNullList.withSize(data.getInt("size"), ItemStack.EMPTY);

            for (int i = 0; i < machineItems.size(); ++i) {
                inventory.set(i, ItemStack.of(machineItems.getCompound(i)));
            }

            IElementHelper helper = IElementHelper.get();

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

            // progress bar
            tooltip.append(helper.spacer(4, 0));
            tooltip.append(helper.progress((float) progress / (float) total).translate(new Vec2(-2.0F, 0.0F)));

            for (int slot : data.getIntArray("outputs")) {
                tooltip.append(helper.item(inventory.get(slot)));
            }

        }
    }

    @Override
    public void appendServerData(CompoundTag data, BlockAccessor accessor) {
        AbstractMachineBlockEntity machine = (AbstractMachineBlockEntity) accessor.getBlockEntity();
        if (machine.showJadeProgress()) {
            ListTag items = new ListTag();

            for (int i = 0; i < machine.getContainerSize(); ++i) {
                items.add(machine.getItem(i).save(new CompoundTag()));
            }

            data.putIntArray("inputs", machine.getInputsForJade());
            data.putIntArray("outputs", machine.getOutputsForJade());

            data.putInt("size", machine.getContainerSize());
            data.put("machine", items);
            CompoundTag furnaceTag = machine.saveWithoutMetadata();
            data.putInt("progress", furnaceTag.getInt("ProcessTime"));
            data.putInt("total", furnaceTag.getInt("ProcessTimeTotal"));
        }
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }
}
