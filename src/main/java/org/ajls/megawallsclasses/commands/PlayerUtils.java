package org.ajls.megawallsclasses.commands;

import org.ajls.megawallsclasses.GamemodeUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class PlayerUtils {
    public static Player getClosestPlayableEnemy(Player player, double distance) {
        Location loc = player.getLocation();
        return getClosestPlayableEnemy(player, loc, distance);
    }

    public static Player getClosestPlayableEnemy(Player player, Location location, double distance) {
        World world = player.getWorld();
        Player target = null;
//        double distance = Double.MAX_VALUE;
        for (Player p : world.getPlayers()) {
            if (GamemodeUtils.isPlayer2PlayableEnemy(player, p)) {
                Location loc2 = p.getLocation();
                double distance2 = loc2.distance(location);
                if (distance2 <= distance) {
                    distance = distance2;
                    target = p;
//                    targetLoc = loc2;
                }
            }
        }
        return target;
    }

    public static Location getMiddleLocation(LivingEntity entity) {
        Location loc = entity.getLocation();
        Location eyeLoc = entity.getEyeLocation();
        loc.setY( (loc.getY() + eyeLoc.getY()) / 2);
        return loc;
    }
}
