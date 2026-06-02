package dev.vissca.svcrates.system;

import com.google.gson.*;
import dev.vissca.svcrates.SvCrates;
import dev.vissca.svcrates.Vars;
import dev.vissca.svcrates.block.ModBlocks;
import dev.vissca.svcrates.item.ModItemGroups;
import dev.vissca.svcrates.item.custom.CrateItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

/// This is what allows the mod to be data driven!
/// It reads the JSON files from both the datapack and the resource pack.
public class ModResourceReloadListener implements SimpleSynchronousResourceReloadListener {
    /// Tells the game where to look for the files in the datapack and resource pack.
    @Override
    public Identifier getFabricId() {
        return Identifier.of("svcrates", "data_reload");
    }

    /// Reads any JSON file at "svcrates/crates/*.json"
    /// Being honest no clue how this works, just copied it online, will try breaking it down eventually
    /// Because I'd like to know what is going on.
    public void gatherCrateResources(ResourceManager manager){
        Vars.crateDataMap.clear();
        Vars.biomeMap.clear();

        Map<Identifier, Resource> resources = manager.findResources(
                "crates", path -> path.getPath().endsWith(".json"));
        RegistryKey<ItemGroup> itemGroup = RegistryKey.of(RegistryKeys.ITEM_GROUP,
                Identifier.of(SvCrates.MOD_ID, "crate_items_tab"));

        for (Map.Entry<Identifier, Resource> entry : resources.entrySet()) {
            try {
                InputStream inputStream = entry.getValue().getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

                JsonObject obj = JsonParser.parseReader(reader).getAsJsonObject();
                int index = 0;

                for (Map.Entry<String, JsonElement> crateEntry : obj.entrySet()) {
                    String key = crateEntry.getKey(); // Eg: "wooden"
                    JsonArray arr = crateEntry.getValue().getAsJsonArray();

                    String lootTable = arr.get(0).getAsString(); // Loot

                    // Biomes
                    List<String> biomes = new ArrayList<>();
                    for (JsonElement b : arr.get(1).getAsJsonArray()) {
                        biomes.add(b.getAsString());
                    }
                    for (String biome : biomes) {
                        if (!Vars.biomeMap.containsKey(biome)) {
                            Vars.biomeMap.put(biome, new ArrayList<>());
                            Vars.biomeMap.get(biome).add(key);
                        } else {
                            Vars.biomeMap.get(biome).add(key);
                        }
                        SvCrates.LOGGER.info(String.valueOf(Vars.biomeMap.get(biome)));
                    }

                    // Textures
                    List<String> textures = new ArrayList<>();
                    for (JsonElement t : arr.get(2).getAsJsonArray()) {
                        textures.add(t.getAsString());
                    }
                    for (String texture : textures) {
                        if (!Vars.crateSprites.containsKey(key)) {
                            Vars.crateSprites.put(key, new ArrayList<>());
                            Vars.crateSprites.get(key).add(texture);
                        } else {
                            Vars.crateSprites.get(key).add(texture);
                        }
                    }

                    // Weight
                    Integer chance = arr.get(3).getAsInt();

                    // Dimensions
                    List<String> dimensions = new ArrayList<>();
                    for (JsonElement b : arr.get(4).getAsJsonArray()) {
                        dimensions.add(b.getAsString());
                    }
                    for (String dimension : dimensions) {
                        if (!Vars.dimensionMap.containsKey(dimension)) {
                            Vars.dimensionMap.put(dimension, new ArrayList<>());
                            Vars.dimensionMap.get(dimension).add(key);
                        } else {
                            Vars.dimensionMap.get(dimension).add(key);
                        }
                        SvCrates.LOGGER.info(String.valueOf(Vars.dimensionMap.get(dimension)));
                    }
                    Vars.crateDataMap.put(key, new Vars.CrateData(lootTable, biomes, textures, key, chance, dimensions));
                    index = index + 1;

                    // To prevent duplicates this was my workaround, I don't know if I can reset the items
                    // In the tab, I'll figure out eventually, should include data-driven crates too tho!
                    if (ModItemGroups.shouldRegen) {
                        ItemStack tabItemStack = new ItemStack(ModBlocks.CRATE_BLOCK.asItem());
                        tabItemStack.set(CrateItem.CRATE_ID, key);
                        tabItemStack.set(CrateItem.CRATE_LOOT_ID, lootTable);

                        ItemGroupEvents.modifyEntriesEvent(itemGroup).register(entries -> {
                            entries.add(tabItemStack);
                        });
                    }
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        ModItemGroups.shouldRegen = false;
        gatherSprites();
    }

    /// This is where the reload actually happens, I made it its own
    /// Method juuust for the sake of cleaning the code up.
    @Override
    public void reload(ResourceManager manager) {
        gatherCrateResources(manager);
    }

    /// Gathers and saves all textures and sprite id paths. Eg: "wooden": "svcrates:block/wooden_crate_top".
    /// Made this to more easily get the textures on the client, since a lot of client related functions cannot
    /// Run on the server, and vice versa.
    public void gatherSprites(){
        Vars.crateSprites.clear();
        for (int crate = 0;crate < Vars.crateDataMap.size(); crate = crate + 1){
            String currentKey = Util.getCrateIdByInt(crate);
            Vars.CrateData selectedCrate = Vars.crateDataMap.get(currentKey);
            List<String> spriteList = new ArrayList<>();

            for (int curTxtr = 0; curTxtr < selectedCrate.textures().size(); curTxtr = curTxtr + 1){
                String texture = Vars.getSprite(Vars.getCrateDataTexture(selectedCrate, curTxtr));
                spriteList.add(texture);
            }
            Vars.crateSprites.put(selectedCrate.id(), spriteList);
        }
    }
}
