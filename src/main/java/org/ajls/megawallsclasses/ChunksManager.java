package org.ajls.megawallsclasses;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class ChunksManager {

    public static void forceloadMap(Location minLoc, Location maxLoc) {
        World world = Bukkit.getWorld("world");
        for (int x = minLoc.getChunk().getX(); x <= maxLoc.getChunk().getX(); x++) {
            for (int z = minLoc.getChunk().getZ(); z <= maxLoc.getChunk().getZ(); z++) {
             world.getChunkAt(x, z).setForceLoaded(true);
            }
        }
    }
}
