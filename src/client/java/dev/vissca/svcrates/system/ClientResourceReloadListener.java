package dev.vissca.svcrates.system;

import dev.vissca.svcrates.SvCrates;
import dev.vissca.svcrates.system.networking.RequestCrateDataPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

/// Listens to F3+T or when you reload your res packs.
public class ClientResourceReloadListener implements SimpleSynchronousResourceReloadListener {
    @Override
    public Identifier getFabricId() {
        return Identifier.of(SvCrates.MOD_ID, "asset_reload");
    }

    @Override
    public void reload(ResourceManager manager) {
        /// Requests the server to send data again.
        if (MinecraftClient.getInstance().getNetworkHandler() != null) {
            ClientPlayNetworking.send(new RequestCrateDataPayload());
        }
    }
}
