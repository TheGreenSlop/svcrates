package dev.vissca.svcrates.datagen;

import dev.vissca.svcrates.block.entity.render.CrateBlockEntityModelLoader;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

// I'd like to note that all these notes are for myself, I'm not gonna remember any of this since it's my first mod
// So I figured I'd just write it in a way I can understand, which is a casual explanation.
/// Registers my model's data generator, can register other things related to data gens afaik too.
public class SvCratesDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		ModelLoadingPlugin.register(new CrateBlockEntityModelLoader());
	}
}
