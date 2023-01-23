package themcbros.usefulmachinery.init;

import net.minecraft.world.item.Item;
import net.themcbrothers.lib.registration.object.ItemObject;
import themcbros.usefulmachinery.UsefulMachinery;
import themcbros.usefulmachinery.items.BatteryItem;
import themcbros.usefulmachinery.items.TierUpgradeItem;

import static themcbros.usefulmachinery.init.Registration.ITEMS;

public class MachineryItems {
    public static final ItemObject<BatteryItem> BATTERY = ITEMS.register("battery", () -> new BatteryItem(new Item.Properties().tab(UsefulMachinery.GROUP).stacksTo(1)));
    public static final ItemObject<Item> MACHINE_FRAME = ITEMS.register("machine_frame", () -> new Item(new Item.Properties().tab(UsefulMachinery.GROUP)));
    public static final ItemObject<Item> COMPACTOR_KIT = ITEMS.register("compactor_kit", () -> new Item(new Item.Properties().tab(UsefulMachinery.GROUP).stacksTo(1)));
    public static final ItemObject<TierUpgradeItem> TIER_UPGRADE = ITEMS.register("tier_upgrade", () -> new TierUpgradeItem(new Item.Properties().tab(UsefulMachinery.GROUP).stacksTo(1)));

    protected static void init() {
    }
}
