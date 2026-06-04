package dev.vissca.svcrates.enchantment;

import com.mojang.serialization.MapCodec;
import dev.vissca.svcrates.SvCrates;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ModEnchantmentEffects {
    private static RegistryKey<Enchantment> key(String path) {
        Identifier id = Identifier.of(SvCrates.MOD_ID, path);
        return RegistryKey.of(RegistryKeys.ENCHANTMENT, id);
    }
    public static MapCodec<UnboxingEnchantmentEffect> UNBOXING_ENCHANT = register("unboxing", UnboxingEnchantmentEffect.CODEC);
    public static final RegistryKey<Enchantment> UNBOXING = key("svcrates.unboxing");

    private static <T extends EnchantmentEntityEffect> MapCodec<T> register(String id, MapCodec<T> codec) {
        return Registry.register(Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE, Identifier.of(SvCrates.MOD_ID, id), codec);
    }
    
    public static void registerModEnchantmentEffects() {
        SvCrates.LOGGER.info("Registering Enchantment Effects for SvCrates!");
    }
}
