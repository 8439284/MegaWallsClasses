package org.ajls.megawallsclasses.rating;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

import static org.ajls.megawallsclasses.rating.Rating.normalize;
import static org.ajls.megawallsclasses.rating.Rating.update;

public class WinnerWeightMap extends HashMap<UUID, Double> {
    void add(UUID uuid, double weight) {
        double before = getOrDefault(uuid, 0.0);
        put(uuid, before + weight);
    }

    public void normalizeAndUpdate(Player loser) {
        normalize(this);
        update(loser, this);
    }
}
