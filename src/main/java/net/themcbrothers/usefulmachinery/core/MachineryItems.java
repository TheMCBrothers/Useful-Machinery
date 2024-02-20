package net.themcbrothers.usefulmachinery.core;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.themcbrothers.usefulmachinery.item.BatteryItem;
import net.themcbrothers.usefulmachinery.item.CreativePowerCellItem;
import net.themcbrothers.usefulmachinery.item.TierUpgradeItem;
import net.themcbrothers.usefulmachinery.item.UpgradeItem;

import static net.themcbrothers.usefulmachinery.core.Registration.ITEMS;

public final class MachineryItems {
    public static final DeferredItem<Item> MACHINE_FRAME = ITEMS.registerSimpleItem("machine_frame");
    public static final DeferredItem<Item> COMPACTOR_KIT = ITEMS.registerSimpleItem("compactor_kit");

    // Special items
    public static final DeferredItem<BatteryItem> BATTERY = ITEMS.registerItem("battery", BatteryItem::new, new Item.Properties().stacksTo(1));
    public static final DeferredItem<TierUpgradeItem> TIER_UPGRADE = ITEMS.registerItem("tier_upgrade", TierUpgradeItem::new);
    public static final DeferredItem<UpgradeItem> EFFICIENCY_UPGRADE = ITEMS.registerItem("efficiency_upgrade", UpgradeItem::new);
    public static final DeferredItem<UpgradeItem> PRECISION_UPGRADE = ITEMS.registerItem("precision_upgrade", UpgradeItem::new);
    public static final DeferredItem<UpgradeItem> SUSTAINED_UPGRADE = ITEMS.registerItem("sustained_upgrade", UpgradeItem::new);


    static void init() {
    }
}
