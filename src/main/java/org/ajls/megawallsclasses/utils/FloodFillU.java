package org.ajls.megawallsclasses.utils;

import org.ajls.lib.advanced.HaxhMap;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.HashSet;

public class FloodFillU {
    public static void floodFill(Location location, int distance) {

    }

    public static HashSet<Location> floodFillRespawn(Location location, int distance) {
        HashSet<Vector> directions = new HashSet<>();
        directions.add(new Vector(-1 ,0, 0));
        directions.add(new Vector(1, 0, 0));
        directions.add(new Vector(0, -1, 0));
        directions.add(new Vector(0 ,1, 0));
        directions.add(new Vector(0, 0, -1));
        directions.add(new Vector(0, 0, 1));
        HaxhMap<Location, Vector> currents = new HaxhMap<>();
        HaxhMap<Location, Vector> nexts = new HaxhMap<>();
        HashSet<Location> returnSet = new HashSet<>();
        HashSet<Location> checkedSet = new HashSet<>();
        currents.put(location, (Vector) null);
        for (int i = 0; i < distance; i++) {
            for (Location currentLocation: currents.keySet()) {
                HashSet<Vector> BirthDirections = currents.getValues(currentLocation, true);
                for (Vector vector: directions) {
                    if (!BirthDirections.contains(vector)) {  //!BirthDirections.contains(vector)  !checkedSet
                        Location next = currentLocation.clone().add(vector.multiply(-1));  //multiply -1 to make the goal vector , or just subtract vector
                        if (next.getBlock().getType() == Material.AIR) {  //todo make it check passable and also it would be good to avoid 1 block gap so only flood when there a 2 block gap so that steve can cross
                            //todo check if the original code makes people respawn on grass when using             int height = world.getHighestBlockYAt(teleportLocation);

                            nexts.put(next, vector.multiply(-1));
                        }
                    }
                }

            }
//            if (di) <= 16
            for (Location nextLocation: nexts.keySet()) {
                checkedSet.add(nextLocation);
                //if standable add to return set
                if (nextLocation.clone().add(0, -1, 0).getBlock().getType() != Material.AIR){                    //TODO: make it standable instead of air, so a button will trick the plugin.(Actually get highest block also tricks it)
                    if (nextLocation.getBlock().getType() == Material.AIR) {
                        if (nextLocation.clone().add(0 ,1 ,0).getBlock().getType() == Material.AIR) {
                            returnSet.add(nextLocation);
                        }
                    }
                }
            }
            //        temp = currents;
            currents = nexts;
            nexts = new HaxhMap<>();
        }
        return returnSet;
    }

//    public class FloodFillDirections {
//        FloodFillDirections()
//    }

    public enum FloodFillDirection {
        X_P,
        X_N,
        Z_P,
        Z_N,
    }
}
