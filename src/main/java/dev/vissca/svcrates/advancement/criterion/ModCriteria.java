package dev.vissca.svcrates.advancement.criterion;

import dev.vissca.svcrates.SvCrates;
import dev.vissca.svcrates.datagen.advancement.criterion.GetCratesCriterion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

/// Register class for registering things, in this case, criterias.
public class ModCriteria {

    public static final GetCratesCriterion GET_CRATES =
            Registry.register(
                    Registries.CRITERION,
                    Identifier.of(SvCrates.MOD_ID, "get_crates"),
                    new GetCratesCriterion()
            );

    public static void registerModCriteria() {SvCrates.LOGGER.info("Registering CustomCriteria for SvCrates!");}
}
