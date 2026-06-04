package dev.vissca.svcrates.system.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
/// Main class that handles configs.
public class ModConfigManager {
    private static final Path PATH = FabricLoader.getInstance()
            .getConfigDir()
            .resolve("crates_config.json");

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    public static ModConfig config;

    /// Creates the config file if it don't exist already. It sets config to its contents which I can use to access its contents.
    public static void load() {
        try {
            if (Files.exists(PATH)) {
                try (BufferedReader reader = Files.newBufferedReader(PATH)) {
                    config = GSON.fromJson(reader, ModConfig.class);
                    return;
                }
            }

            config = new ModConfig();

            Files.createDirectories(PATH.getParent());
            Files.writeString(PATH, GSON.toJson(config));

        } catch (Exception e) {
            throw new RuntimeException("Config broken.", e);
        }
    }
}