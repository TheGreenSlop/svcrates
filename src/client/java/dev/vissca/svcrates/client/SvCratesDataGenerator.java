package dev.vissca.svcrates.client;

import dev.vissca.svcrates.block.entity.render.CrateBlockEntitylModelLoader;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

// I'd like to note that all these notes are for myself, I'm not gonna remember any of this since its my first mod
// So I figured I'd just write it in a way I can understand, which is a casual explanation.
public class SvCratesDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		ModelLoadingPlugin.register(new CrateBlockEntitylModelLoader());
	}
}
