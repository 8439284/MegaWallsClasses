package org.ajls.megawallsclasses;

import org.ajls.lib.utils.ScoreboardU;
import org.bukkit.entity.Player;

public class ClassU {
    public static int getClass(Player player) {
        return ScoreboardsAndTeams.getScore(player, "class");
    }

    public static void setClass(Player player, int index) {
        if (index == 29) {
            ScoreboardU.setScore(player, "tf", 1);

            //幻变大师
        }
        else {
            ScoreboardU.resetScore(player, "tf");
            ScoreboardsAndTeams.setScore(player, "class", index);
        }
    }

    public static ClassEnum getClassEnum(Player player) {
        return GetClassEnum.getClassEnum(ScoreboardU.getScore(player, "class"));
    }

    public static boolean isTransformationMaster(Player player) {
        return ScoreboardU.isScoreSet(player, "tf", false);
    }
}
