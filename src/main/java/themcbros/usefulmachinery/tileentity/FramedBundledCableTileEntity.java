package themcbros.usefulmachinery.tileentity;

import com.google.common.collect.Maps;
import net.minecraft.item.DyeColor;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import themcbros.usefulmachinery.bundled_cable.FramedBundledCableNetwork;
import themcbros.usefulmachinery.bundled_cable.FramedBundledCableNetworkManager;
import themcbros.usefulmachinery.init.ModTileEntities;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;

public class FramedBundledCableTileEntity extends TileEntity {

    public final Map<DyeColor, Boolean> signals = Maps.newHashMap();

    private static final DyeColor[] COLORS = Arrays.stream(DyeColor.values()).sorted(Comparator.comparingInt(DyeColor::getId)).toArray(DyeColor[]::new);

    public FramedBundledCableTileEntity() {
        super(ModTileEntities.FRAMED_BUNDLED_CABLE);
        for (DyeColor value : COLORS) {
            this.signals.put(value, Boolean.FALSE);
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        for (DyeColor color : COLORS) {
            compound.putBoolean(color.getTranslationKey(), this.signals.get(color));
        }
        return super.write(compound);
    }

    @Override
    public void read(CompoundNBT compound) {
        for (DyeColor color : COLORS) {
            Boolean value = compound.getBoolean(color.getTranslationKey());
            this.signals.put(color, value);
        }
        super.read(compound);
    }

    public int getBundledOutput() {
        int colors = 0;
        for (DyeColor color : DyeColor.values()) {
            if (getColorValue(color)) {
                colors += (int) Math.pow(2, color.getId());
            }
        }
        return colors;
    }

    public void setColorValue(DyeColor color, Boolean value) {
        this.signals.put(color, value);
        this.markDirty();
        FramedBundledCableNetworkManager.invalidateNetwork(this.world, this.pos);
    }

    public Boolean getColorValue(DyeColor color) {
        if (this.world != null && this.pos != null)
            return Objects.requireNonNull(FramedBundledCableNetworkManager.get(this.world, this.pos)).getSignals().get(color);
        return this.signals.get(color);
    }

    @Override
    public void remove() {
        if (this.world != null) {
            FramedBundledCableNetworkManager.invalidateNetwork(this.world, this.pos);
        }
        super.remove();
    }

    public String getNetworkInfos() {
        if (world == null) return "world is null";

        FramedBundledCableNetwork net = FramedBundledCableNetworkManager.get(world, pos);
        return net != null ? net.toString() : "null";
    }
}