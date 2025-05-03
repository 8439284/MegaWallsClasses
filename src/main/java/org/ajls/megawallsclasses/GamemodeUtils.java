package org.ajls.megawallsclasses;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import static org.ajls.megawallsclasses.ScoreboardsAndTeams.getPlayerTeam;
import static org.ajls.megawallsclasses.ScoreboardsAndTeams.isPlayerSameTeam;

public class GamemodeUtils {

    public static boolean isPlayerSpectator(Player player) {
        if (player == null) return false;
        return player.getGameMode().equals(GameMode.SPECTATOR);
    }

    public static boolean isPlayerDead(Player player) {
        if (player == null) return false;
        return player.isDead();
    }

    public static boolean isPlayerUnplayable(Player player) {
        if (player == null) return true;
        return isPlayerSpectator(player) || isPlayerDead(player);
    }

    public static boolean isPlayerPlayable(Player player) {
        return !isPlayerUnplayable(player);
    }

    public static boolean isPlayerPlayableEnemy(Player player1, Player player2) {
        return !isPlayerUnplayable(player1) && !isPlayerUnplayable(player2) && !isPlayerSameTeam(player1, player2);
    }

    public static boolean isPlayer2PlayableEnemy(Player player1, Player player2) {
        return !isPlayerUnplayable(player2) && !isPlayerSameTeam(player1, player2);
    }

    public static boolean isPlayerAttackableEnemy(Player player1, Player player2) {
        boolean result;
        result = !isPlayerSameTeam(player1, player2); //enemy
        if (!result) { //sameteam
            result = getPlayerTeam(player1).allowFriendlyFire();
        }
        return result;
    }
}
