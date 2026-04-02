package net.themcbrothers.usefulmachinery.compat.jade;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.themcbrothers.usefulmachinery.UsefulMachinery;
import net.themcbrothers.usefulmachinery.block.entity.AbstractMachineBlockEntity;
import net.themcbrothers.usefulmachinery.core.MachineryDataComponentTypes;
import net.themcbrothers.usefulmachinery.core.MachineryItems;
import net.themcbrothers.usefulmachinery.machine.MachineTier;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.StreamServerDataProvider;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.JadeUI;

import java.util.Arrays;
import java.util.List;

/**
 * Adds the machine's progress and tier information
 */
public class MachineComponentProvider implements StreamServerDataProvider<BlockAccessor, MachineComponentProvider.Data> {
    private static final Identifier UID = UsefulMachinery.id("machine");
    public static final MachineComponentProvider INSTANCE = new MachineComponentProvider();

    @Override
    public Data streamData(BlockAccessor accessor) {
        AbstractMachineBlockEntity machine = accessor.typedBlockEntity();
        List<ItemStack> inputs = Arrays.stream(machine.getInputSlots()).mapToObj(machine::getItem).toList();
        List<ItemStack> outputs = Arrays.stream(machine.getOutputSlots()).mapToObj(machine::getItem).toList();

        return new Data(inputs, outputs, machine.getProcessTime(), machine.getProcessTimeTotal(), machine.getMachineTier().ordinal());
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, Data> streamCodec() {
        return Data.STREAM_CODEC;
    }

    @Override
    public Identifier getUid() {
        return UID;
    }

    public record Data(
            List<ItemStack> inputs,
            List<ItemStack> outputs,
            int progress,
            int total,
            int machineTier
    ) {
        public static final StreamCodec<RegistryFriendlyByteBuf, MachineComponentProvider.Data> STREAM_CODEC;

        static {
            STREAM_CODEC = StreamCodec.composite(
                    ItemStack.OPTIONAL_LIST_STREAM_CODEC, Data::inputs,
                    ItemStack.OPTIONAL_LIST_STREAM_CODEC, Data::outputs,
                    ByteBufCodecs.INT, Data::progress,
                    ByteBufCodecs.INT, Data::total,
                    ByteBufCodecs.INT, Data::machineTier,
                    MachineComponentProvider.Data::new
            );
        }
    }

    public static class Client implements IBlockComponentProvider {
        private static final Identifier UID = UsefulMachinery.id("machine");
        public static final Client INSTANCE = new Client();

        @Override
        public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig pluginConfig) {
            Data data = MachineComponentProvider.INSTANCE.decodeFromData(accessor).orElse(null);

            if (data == null) {
                return;
            }

            if (data.progress() != 0 && !data.inputs.isEmpty() && !data.outputs.isEmpty()) {
                int progress = data.progress();
                int total = data.total();

                int count = 0;

                for (ItemStack input : data.inputs()) {
                    if (count++ == 0) {
                        tooltip.add(JadeUI.item(input));
                    } else {
                        tooltip.append(JadeUI.item(input));
                    }
                }

                // Progress Bar
                tooltip.append(JadeUI.spacer(4, 0));
                tooltip.append(JadeUI.progressArrow((float) progress / (float) total)
                        .offset(-2, 0));

                data.outputs().forEach(output -> {
                    tooltip.append(JadeUI.item(output));
                });
            }

            MachineTier tier = MachineTier.byOrdinal(data.machineTier());

            if (accessor.getPlayer().isShiftKeyDown() && tier.ordinal() > 0) {
                ItemStack itemStack = new ItemStack(MachineryItems.TIER_UPGRADE.get(), 1);
                itemStack.set(MachineryDataComponentTypes.TIER, tier);

                // Tier
                tooltip.add(JadeUI.smallItem(itemStack));
                tooltip.append(JadeUI.spacer(4, 0));
                tooltip.append(JadeUI.text(Component.literal(tier.getSerializedName())));
            }
        }

        @Override
        public Identifier getUid() {
            return UID;
        }
    }
}
