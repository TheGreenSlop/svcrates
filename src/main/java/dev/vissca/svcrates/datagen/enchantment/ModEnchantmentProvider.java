package dev.vissca.svcrates.datagen.enchantment;

import dev.vissca.svcrates.enchantment.ModEnchantmentEffects;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.*;
import net.minecraft.registry.tag.ItemTags;

import java.util.concurrent.CompletableFuture;

public class ModEnchantmentProvider extends FabricDynamicRegistryProvider {
    public ModEnchantmentProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup registries, Entries entries) {
        entries.addAll(registries.getWrapperOrThrow(RegistryKeys.ENCHANTMENT));
    }

    @Override
    public String getName() {
        return "Enchantments";
    }

    private static void register(Registerable<Enchantment> context, RegistryKey<Enchantment> key, Enchantment.Builder builder) {
        context.register(key, builder.build(key.getRegistry()));
    }

    public static void bootstrap(Registerable<Enchantment> context) {
        register(context, ModEnchantmentEffects.UNBOXING,
                Enchantment.builder(
                        Enchantment.definition(context.getRegistryLookup(RegistryKeys.ITEM).getOrThrow(
                                ItemTags.FISHING_ENCHANTABLE),
                                1,
                                3,
                                Enchantment.leveledCost(1,6),
                                Enchantment.leveledCost(1,12),
                                6)
                ));
    }
}
