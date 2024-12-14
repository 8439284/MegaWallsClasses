package org.ajls.megawallsclasses.utils;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class RayTraceU {


    /**
     * Casts a ray from the player's eye location in the direction they're looking
     * and finds the first entity it intersects with along with the intersection point.
     */
    public static IntersectionResult rayTraceEntities(Player player, double range) {
        Location start = player.getEyeLocation();
        Vector direction = player.getLocation().getDirection();

        // Get all nearby entities within the specified range
        List<Entity> entities = new ArrayList<>();
        for (Entity entity : player.getWorld().getNearbyEntities(start, range, range, range)) {
            // Skip the player itself
            if (entity != player) entities.add(entity);
        }

        // Loop through entities to find the intersection
        for (Entity entity : entities) {
            Location intersection = getRayIntersection(start, direction, entity, range);
            if (intersection != null) {
                return new IntersectionResult(entity, intersection);
            }
        }
        return null;
    }

    /**
     * Calculates the intersection point between a ray and an entity's bounding box.
     */
    public static Location getRayIntersection(Location start, Vector direction, Entity entity, double range) {
        Location min = entity.getBoundingBox().getMin().toLocation(entity.getWorld());
        Location max = entity.getBoundingBox().getMax().toLocation(entity.getWorld());

        // Ray tracing through the AABB (Axis-Aligned Bounding Box)
        Vector invDir = new Vector(1.0 / direction.getX(), 1.0 / direction.getY(), 1.0 / direction.getZ());
        double tMin = (min.getX() - start.getX()) * invDir.getX();
        double tMax = (max.getX() - start.getX()) * invDir.getX();
        if (tMin > tMax) { double temp = tMin; tMin = tMax; tMax = temp; }

        double tYMin = (min.getY() - start.getY()) * invDir.getY();
        double tYMax = (max.getY() - start.getY()) * invDir.getY();
        if (tYMin > tYMax) { double temp = tYMin; tYMin = tYMax; tYMax = temp; }

        if ((tMin > tYMax) || (tYMin > tMax)) return null;
        if (tYMin > tMin) tMin = tYMin;
        if (tYMax < tMax) tMax = tYMax;

        double tZMin = (min.getZ() - start.getZ()) * invDir.getZ();
        double tZMax = (max.getZ() - start.getZ()) * invDir.getZ();
        if (tZMin > tZMax) { double temp = tZMin; tZMin = tZMax; tZMax = temp; }

        if ((tMin > tZMax) || (tZMin > tMax)) return null;
        if (tZMin > tMin) tMin = tZMin;
        if (tZMax < tMax) tMax = tZMax;

        // If there's a valid intersection, calculate the intersection point
        if (tMin < range && tMin >= 0) {
            Vector intersectionPoint = start.toVector().add(direction.multiply(tMin));
            return intersectionPoint.toLocation(entity.getWorld());
        }
        return null;
    }

    public static Location getRayInterSection(Entity start, Entity entity, double range) {
        Location location = start.getLocation();
        return getRayIntersection(location, location.getDirection(), entity, range);
    }

    /**
     * Represents the result of a ray trace, containing the entity and the intersection point.
     */
    public static class IntersectionResult {
        private final Entity entity;
        private final Location intersection;

        public IntersectionResult(Entity entity, Location intersection) {
            this.entity = entity;
            this.intersection = intersection;
        }

        public Entity getEntity() {
            return entity;
        }

        public Location getIntersection() {
            return intersection;
        }
    }

}
