package dev.vissca.svcrates.item;

import dev.vissca.svcrates.SvCrates;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

/// Item groups are the so-called "Creative Tabs", on fabric, just felt like mentioning that.
public class ModItemGroups {
    public static Boolean shouldRegen = true;
    public static final ItemGroup CRATE_ITEMS_TAB = Registry.register(Registries.ITEM_GROUP,
            Identifier.of(SvCrates.MOD_ID, "crate_items_tab"),
            FabricItemGroup.builder()
                    .displayName(Text.translatable("itemgroup.svcrates.crate_items_tab"))
                    .icon(ModItems.CRATE_ITEM_ICON::getDefaultStack).build());

    /// Register my freakinge groups. My class wake-uper...
    public static void registerItemGroups(){SvCrates.LOGGER.info("Registering ItemGroups for SvCrates!");}
}
