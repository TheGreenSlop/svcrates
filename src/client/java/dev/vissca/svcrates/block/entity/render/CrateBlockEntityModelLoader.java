package dev.vissca.svcrates.block.entity.render;

import dev.vissca.svcrates.SvCrates;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class CrateBlockEntityModelLoader implements ModelLoadingPlugin {
    // Variables
    public static final ModelIdentifier CRATE_BLOCK_ENTITY_MODEL =
            new ModelIdentifier(Identifier.of(SvCrates.MOD_ID, "crate_block"), "");
    public static final ModelIdentifier CRATE_ITEM_MODEL =
            new ModelIdentifier(Identifier.of(SvCrates.MOD_ID, "crate_block"), "inventory");

    /// Initializes and put the models where they freakinge belong, otherwise it falls back to their original models
    /// Hopefully this never breaks.
    @Override
    public void onInitializeModelLoader(Context pluginContext) {
        pluginContext.modifyModelOnLoad().register((originalModel, context) -> {
            // Local Vars
            final ModelIdentifier id = context.topLevelId();

            if(id != null && id.equals(CRATE_BLOCK_ENTITY_MODEL) || id != null && id.equals(CRATE_ITEM_MODEL))  {
                return new CrateBlockEntityBakedModel();
            } else {
                return originalModel;
            }
        });
    }
}
