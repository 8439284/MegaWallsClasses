package org.ajls.megawallsclasses;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockType;

public class BlocksModify {
    public static void fill (int minX, int minY, int minZ, int maxX, int maxY, int maxZ, Material material) {
        for(int x = minX; x <= maxX; x++){
            for(int y = minY; y <= maxY; y++){
                for(int z = minZ; z <= maxZ; z++){
                    Block block = Bukkit.getWorld("world").getBlockAt(x, y, z);
                    block.setType(material);
                }
            }
        }
    }

    public static boolean isInBounds(double x, double y , double z, double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        return x > minX && x < maxX && y > minY && y < maxY && z > minZ && z < maxZ;
    }
}
