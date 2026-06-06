package dev.vissca.svcrates.statistic;

import dev.vissca.svcrates.SvCrates;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModStatistics {
    public static final Identifier FISH_UP_CRATE = Identifier.of(SvCrates.MOD_ID, "fish_up_crate");
    public static final Identifier OPEN_CRATE = Identifier.of(SvCrates.MOD_ID, "open_crate");

    public static void registerStatistics(){
        SvCrates.LOGGER.info("Registering Statistics for SvCrates!");
        Registry.register(Registries.CUSTOM_STAT, FISH_UP_CRATE, FISH_UP_CRATE);
        Registry.register(Registries.CUSTOM_STAT, OPEN_CRATE, OPEN_CRATE);
    }
}
