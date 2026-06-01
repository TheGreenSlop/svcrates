package dev.vissca.svcrates.client;

import dev.vissca.svcrates.block.entity.render.CrateBlockEntitylModelLoader;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;

@Environment(EnvType.CLIENT)
public class SvCratesClient implements ClientModInitializer {
	/// Makes sure that all my efforts in the model rendering aren't in vain and registers it so the game
	/// Actually does something with my code lol.
	@Override
	public void onInitializeClient() {
		ModelLoadingPlugin.register(new CrateBlockEntitylModelLoader());
	}
}