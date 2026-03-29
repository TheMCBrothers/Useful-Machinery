package net.themcbrothers.usefulmachinery;

import net.minecraft.resources.Identifier;
import net.neoforged.fml.common.Mod;
import net.themcbrothers.lib.util.ComponentFormatter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(UsefulMachinery.MOD_ID)
public class UsefulMachinery {
    public static final String MOD_ID = "usefulmachinery";
    public static final Logger LOGGER = LogManager.getLogger();
    public static final ComponentFormatter TEXT_UTILS = new ComponentFormatter(MOD_ID);

    public UsefulMachinery() {
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}
