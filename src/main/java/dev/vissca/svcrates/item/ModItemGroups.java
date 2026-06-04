package dev.vissca.svcrates.item;

import dev.vissca.svcrates.SvCrates;
import dev.vissca.svcrates.block.ModBlocks;
import dev.vissca.svcrates.item.custom.CrateItem;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
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

    public static void addGroups(String key, String lootTable){
        RegistryKey<ItemGroup> itemGroup = RegistryKey.of(RegistryKeys.ITEM_GROUP,
                Identifier.of(SvCrates.MOD_ID, "crate_items_tab"));

        // To prevent duplicates this was my workaround, I don't know if I can reset the items
        // In the tab, I'll figure out eventually, should include data-driven crates too tho!
        if (ModItemGroups.shouldRegen) {
            ItemStack tabItemStack = new ItemStack(ModBlocks.CRATE_BLOCK.asItem());
            tabItemStack.set(CrateItem.CRATE_ID, key);
            tabItemStack.set(CrateItem.CRATE_LOOT_ID, lootTable);

            ItemGroupEvents.modifyEntriesEvent(itemGroup).register(entries -> {
                entries.add(tabItemStack);
            });
        }
    }
}
