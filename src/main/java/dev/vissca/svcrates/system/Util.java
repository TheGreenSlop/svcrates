package dev.vissca.svcrates.system;

import dev.vissca.svcrates.Vars;

/// Full of my sloppy helper methods :D.
public class Util {

    /// Delicious method for getting stuff from my crate maps, a bit jank, but it works :p.
    public static String getCrateIdByInt(Integer index){
        return Vars.crateDataMap.keySet().stream().toList().get(index);
    }
}
