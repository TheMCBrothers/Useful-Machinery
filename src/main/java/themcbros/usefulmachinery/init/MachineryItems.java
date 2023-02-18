package themcbros.usefulmachinery.init;

import net.minecraft.world.item.Item;
import net.themcbrothers.lib.registration.object.ItemObject;
import themcbros.usefulmachinery.items.BatteryItem;
import themcbros.usefulmachinery.items.TierUpgradeItem;
import themcbros.usefulmachinery.items.UpgradeItem;

import static themcbros.usefulmachinery.init.Registration.ITEMS;

public class MachineryItems {
    public static final ItemObject<BatteryItem> BATTERY = ITEMS.register("battery", () -> new BatteryItem(new Item.Properties().stacksTo(1)));
    public static final ItemObject<Item> MACHINE_FRAME = ITEMS.register("machine_frame", () -> new Item(new Item.Properties()));
    public static final ItemObject<Item> COMPACTOR_KIT = ITEMS.register("compactor_kit", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final ItemObject<TierUpgradeItem> TIER_UPGRADE = ITEMS.register("tier_upgrade", () -> new TierUpgradeItem(new Item.Properties().stacksTo(1)));
    public static final ItemObject<UpgradeItem> EFFICIENCY_UPGRADE = ITEMS.register("efficiency_upgrade", () -> new UpgradeItem(new Item.Properties()));
    public static final ItemObject<UpgradeItem> PRECISION_UPGRADE = ITEMS.register("precision_upgrade", () -> new UpgradeItem(new Item.Properties()));
    public static final ItemObject<UpgradeItem> SUSTAINED_UPGRADE = ITEMS.register("sustained_upgrade", () -> new UpgradeItem(new Item.Properties()));

    protected static void init() {
    }
}
