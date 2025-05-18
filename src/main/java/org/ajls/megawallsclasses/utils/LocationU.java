package org.ajls.megawallsclasses.utils;

import org.bukkit.util.Vector;

public class LocationU {
    public static float getYaw(Vector vector) {
        double dx = vector.getX();
        double dz = vector.getZ();
        double yaw = 0;

        // Calculate yaw
        if (dx != 0) {
            // Convert from radians to degrees
            yaw = Math.toDegrees(Math.atan2(-dx, dz));
        } else if (dz < 0) {
            yaw = 180;
        }

        return (float) yaw;
    }

    // Calculate pitch from a vector (direction)
    public static float getPitch(Vector vector) {
        double dx = vector.getX();
        double dy = vector.getY();
        double dz = vector.getZ();
        double horizontalDistance = Math.sqrt(dx * dx + dz * dz);
        double pitch = 0;

        // Calculate pitch
        if (horizontalDistance != 0) {
            // Convert from radians to degrees
            pitch = Math.toDegrees(Math.atan2(-dy, horizontalDistance));
        } else if (dy < 0) {
            pitch = 90;
        } else {
            pitch = -90;
        }

        return (float) pitch;
    }
}
