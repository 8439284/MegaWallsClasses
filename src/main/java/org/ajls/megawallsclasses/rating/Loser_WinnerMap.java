package org.ajls.megawallsclasses.rating;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class Loser_WinnerMap extends HashMap<UUID, WinnerWeightMap> {
    public void addWeight(Player loser, Player winner, double damage) {
        WinnerWeightMap winnerWeightMap = new WinnerWeightMap();

        UUID loserUUID = loser.getUniqueId();
        if  (containsKey(loserUUID)) {
            winnerWeightMap = get(loserUUID);
        }
        UUID winnerUUID = winner.getUniqueId();
//        WinnerWeightMap winnerWeightMap = get(loserUUID);
        winnerWeightMap.add(winnerUUID, damage);
        put(loserUUID, winnerWeightMap);
    }

    public void settle(Player loser) {
        UUID uuid = loser.getUniqueId();
        WinnerWeightMap winnerWeightMap = get(uuid);
        if (winnerWeightMap == null) { return;}  //nobody damaged him
        winnerWeightMap.normalizeAndUpdate(loser);
        remove(uuid);
    }
}
