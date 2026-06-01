package dev.vissca.svcrates.item;

import dev.vissca.svcrates.SvCrates;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

/// Item, you guessed it, registers items.
public class ModItems {
    /// Work around to have a custom icon for the item group.
    public static final Item CRATE_ITEM_ICON = register(
            new Item(new Item.Settings()),
            "crate_item_icon"
    );

    /// Register items, similar system to registering blocks, needs no change tho.
    public static Item register(Item item, String id) {
        Identifier itemID = Identifier.of(SvCrates.MOD_ID, id);
        return Registry.register(Registries.ITEM, itemID, item);
    }

    /// WAKE UP! LOAD MY CLASS!
    public static void registerModItems(){SvCrates.LOGGER.info("Registering Items for SvCrates!");}
}
