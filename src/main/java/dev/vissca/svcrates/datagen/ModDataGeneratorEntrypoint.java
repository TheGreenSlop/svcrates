package dev.vissca.svcrates.datagen;

import dev.vissca.svcrates.datagen.advancement.ModAdvancementProvider;
import dev.vissca.svcrates.datagen.enchantment.ModEnchantmentProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;

/// Mother of all generators or something IDK... Makes the providers actually do their thing.
public class ModDataGeneratorEntrypoint implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(ModAdvancementProvider::new);
        pack.addProvider(ModEnchantmentProvider::new);

    }
    @Override
    public void buildRegistry(RegistryBuilder registryBuilder) {
        registryBuilder.addRegistry(RegistryKeys.ENCHANTMENT, ModEnchantmentProvider::bootstrap);
    }
}
