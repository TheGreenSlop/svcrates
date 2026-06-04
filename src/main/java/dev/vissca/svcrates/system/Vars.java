package dev.vissca.svcrates.system;

import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/// I don't know if this is bad practice, maybe it is maybe it isn't.
/// Never seen other mods have a global variables sorta class, but
/// I made one! It is very handy.
public class Vars {
    public static Map<String, CrateData> crateDataMap = new HashMap<>();
    public static Map<String, List<String>> biomeMap = new HashMap<>();
    public static Map<String, List<String>> crateSprites = new HashMap<>();
    public static Map<String, List<String>> dimensionMap = new HashMap<>();
    public static String getSprite(String spriteId){
        return "svcrates:" + "block/" + spriteId;
    }

    /// This is where the JSON files end up, allows me to condense everything into one very simple Map.
    public record CrateData(String lootTableId, List<String> biomes, List<String> textures, String id, Integer chance, List<String> dimension) {}

    // not gonna explain these, just getters and setters.
    public static String getCrateDataId(CrateData crateDataVal){
        return crateDataVal.id;
    }
    public static String getCrateDataTexture(CrateData crateDataVal, Integer id){
        return crateDataVal.textures.get(id);
    }
    public record CratedUp(List<ItemStack> stack, Integer chance){ }
    public static Integer getCrateDataChance(CrateData crateDataVal){
        return crateDataVal.chance;
    }
}
