package org.ajls.megawallsclasses.utils;

import org.bukkit.util.Vector;

public class VelocityU {
    public static int hitWall(Vector vector) {
        if (vector.getX() == 0) return 1;
        if (vector.getY() == 0) return 2;
        if (vector.getZ() == 0) return 3;
        return 0;
    }
}
