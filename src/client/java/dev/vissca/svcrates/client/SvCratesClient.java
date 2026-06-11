package dev.vissca.svcrates.client;

import dev.vissca.svcrates.SvCrates;
import dev.vissca.svcrates.block.entity.render.CrateBlockEntityModelLoader;
import dev.vissca.svcrates.item.ModItemGroups;
import dev.vissca.svcrates.system.ClientResourceReloadListener;
import dev.vissca.svcrates.system.ModResourceReloadListener;
import dev.vissca.svcrates.system.Vars;
import dev.vissca.svcrates.system.networking.CrateDataPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.resource.ResourceType;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class SvCratesClient implements ClientModInitializer {
	/// Handler for the server payloads.
	private static void handleCrateDataReceived(CrateDataPayload payload, ClientPlayNetworking.Context context) {
		ClientPlayerEntity player = context.client().player;
		assert player != null;
		Vars.crateDataMap.put(payload.name(), payload.data());
		SvCrates.LOGGER.info(String.valueOf(Vars.crateDataMap));
		rebuildCrateSprites(payload.name(), payload.data());
		ModItemGroups.addGroups(payload.name(), payload.data().lootTableId());
	}

	/// Registers most things on the client.
	@Override
	public void onInitializeClient() {
		// Registers
		ModelLoadingPlugin.register(
				new CrateBlockEntityModelLoader());

		// Resource Registers
		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES)
				.registerReloadListener(new ModResourceReloadListener());
		ResourceManagerHelper.get(ResourceType.SERVER_DATA)
				.registerReloadListener(new ModResourceReloadListener());

		// Payload Registers
		ClientPlayNetworking.registerGlobalReceiver(CrateDataPayload.ID, SvCratesClient::handleCrateDataReceived);

		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES)
				.registerReloadListener(new ClientResourceReloadListener());
	}

	/// Basic ass helper method for redoing the sprite textures in case Vars.crateSprites doesn't get filled at all on the client.
	private static void rebuildCrateSprites(String id, Vars.CrateData data) {
		// Local Vars
		List<String> spriteList = new ArrayList<>();

		for (String spritePath : data.textures()) {
			String gottenSprite = Vars.getSprite(spritePath);
			spriteList.add(gottenSprite);
		}

		Vars.crateSprites.put(id, spriteList);
	}

}