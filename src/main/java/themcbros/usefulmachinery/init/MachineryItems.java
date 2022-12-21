package themcbros.usefulmachinery.init;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;
import themcbros.usefulmachinery.UsefulMachinery;
import themcbros.usefulmachinery.items.BatteryItem;
import themcbros.usefulmachinery.items.TierUpgradeItem;

import static themcbros.usefulmachinery.init.Registration.ITEMS;

public class MachineryItems {
    public static final RegistryObject<BatteryItem> BATTERY = ITEMS.register("battery", () -> new BatteryItem(new Item.Properties().tab(UsefulMachinery.GROUP).stacksTo(1)));
    public static final RegistryObject<Item> MACHINE_FRAME = ITEMS.register("machine_frame", () -> new Item(new Item.Properties().tab(UsefulMachinery.GROUP)));
    public static final RegistryObject<Item> COMPACTOR_KIT = ITEMS.register("compactor_kit", () -> new Item(new Item.Properties().tab(UsefulMachinery.GROUP).stacksTo(1)));
    public static final RegistryObject<TierUpgradeItem> TIER_UPGRADE = ITEMS.register("tier_upgrade", () -> new TierUpgradeItem(new Item.Properties().tab(UsefulMachinery.GROUP).stacksTo(1)));

    protected static void init() {
    }
}
