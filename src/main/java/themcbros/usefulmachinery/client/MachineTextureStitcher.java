package themcbros.usefulmachinery.client;

import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import themcbros.usefulmachinery.UsefulMachinery;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = UsefulMachinery.MOD_ID, value = Dist.CLIENT)
public class MachineTextureStitcher {
    @SubscribeEvent
    public static void onTextureStitch(final TextureStitchEvent.Pre event) {
        if (event.getAtlas().location() == TextureAtlas.LOCATION_BLOCKS) {
            UsefulMachinery.LOGGER.debug("Stitching textures to block atlas...");
            event.addSprite(UsefulMachinery.getId("item/energy_slot"));
            event.addSprite(UsefulMachinery.getId("block/machine_side_tier_overlay"));
        }
    }
}
