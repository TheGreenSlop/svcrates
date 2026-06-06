package dev.vissca.svcrates.enchantment;

import dev.vissca.svcrates.SvCrates;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ModEnchantments {
    private static RegistryKey<Enchantment> key(String path) {
        Identifier id = Identifier.of(SvCrates.MOD_ID, path);
        return RegistryKey.of(RegistryKeys.ENCHANTMENT, id);
    }
    public static final RegistryKey<Enchantment> UNBOXING = key("unboxing");

    public static void registerModEnchantments() {
        SvCrates.LOGGER.info("Registering Enchantment Effects for SvCrates!");
    }
}
