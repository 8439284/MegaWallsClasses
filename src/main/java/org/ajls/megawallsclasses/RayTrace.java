package org.ajls.megawallsclasses;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;

import static org.bukkit.Bukkit.getWorld;

public class RayTrace {
    //ray trace entities and blocks and return an array including an array including first entity/block hit
    //block priority high
    public static ArrayList<Object> rayTraceFirstBlockOrEntity(Player player, double gap, int count) { //gap * count = distance
        ArrayList<Object> returns = new ArrayList<>();
//        Location first = null;
//        boolean blocked = false;
        Vector vector = player.getEyeLocation().getDirection();
//        vector = vector.normalize(); // unit vector
        vector = vector.multiply(gap); // vector  step
        Vector start = new Vector(player.getEyeLocation().getX(), player.getEyeLocation().getY(), player.getEyeLocation().getZ()); // start vector from origin to player
//        Vector vector2 = new Vector(0,0,0); //vector 2 away from start
//        Vector vector3 = new Vector(); // vector start + vector 2
        for (int i = 0; i <= count; i++) {
            Location loc = new Location(player.getWorld(), start.getX(), start.getY(), start.getZ());
//            spawnParticles(loc);
            Block block = loc.getBlock();
            if (!block.getType().equals(Material.AIR)) {
//                first = loc;
//                blocked = true;
                returns.add(loc);
                returns.add(true);
                return returns;
            }
            for (Player p : player.getWorld().getPlayers()) {
                if (!p.equals(player)) {
                    if (p.getBoundingBox().contains(start)) {
                        Bukkit.broadcastMessage(start.toString());
                        Bukkit.broadcastMessage(p.getBoundingBox().toString());
//                    first = loc;
                        returns.add(loc);
                        returns.add(false);
                        return returns;
                    }
                }
            }
//            if (first != null) {
//                break;
//            }
//            vector2 = vector2 .add(vector);
//            vector3 = vector3.add(vector2);
            start = start.add(vector);
        }
        returns.add(null);
        returns.add(false);
        return returns;
    }


//    static void spawnParticles(Location location) {
//        Particle.DustOptions dustOptions = new Particle.DustOptions(Color.RED, 1.0F);
//        getWorld("world").spawnParticle(Particle.DUST, location, 1, dustOptions);
//    }
}
