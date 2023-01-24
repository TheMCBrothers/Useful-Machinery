package themcbros.usefulmachinery.data;

import net.minecraft.client.renderer.texture.atlas.sources.SingleFile;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SpriteSourceProvider;
import themcbros.usefulmachinery.UsefulMachinery;

import java.util.Optional;

public class MachinerySpriteSourceProvider extends SpriteSourceProvider {
    public MachinerySpriteSourceProvider(PackOutput output, ExistingFileHelper fileHelper) {
        super(output, fileHelper, UsefulMachinery.MOD_ID);
    }

    @Override
    protected void addSources() {
        this.atlas(BLOCKS_ATLAS).addSource(new SingleFile(UsefulMachinery.getId("item/energy_slot"), Optional.empty()));
        this.atlas(BLOCKS_ATLAS).addSource(new SingleFile(UsefulMachinery.getId("block/machine_side_tier_overlay"), Optional.empty()));
    }
}
