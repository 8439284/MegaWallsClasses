package org.ajls.megawallsclasses;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import static org.ajls.megawallsclasses.KillsManager.player_finalAssists;
import static org.ajls.megawallsclasses.KillsManager.player_finalKills;
import static org.ajls.megawallsclasses.ScoreboardsAndTeams.getPlayerTeamColor;
import static org.bukkit.Bukkit.getScoreboardManager;

public class SidebarManager {

    public static void createPlayerScoreboard(Player player) {
        ScoreboardManager scoreboardManager = getScoreboardManager();
        Scoreboard scoreboard = scoreboardManager.getNewScoreboard();
        player.setScoreboard(scoreboard);
    }

    public static void createPlayerScoreboardSidebar(Player player) {
        Scoreboard scoreboard = player.getScoreboard();
        Objective objective = scoreboard.registerNewObjective(player.getName() + "megawalls", "dummy");
        objective.setDisplayName(getPlayerTeamColor(player) + "MEGA WALLS");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        Team finals = scoreboard.registerNewTeam("finals");
        String teamKeyFinals = ChatColor.WHITE.toString();
        finals.addEntry(teamKeyFinals);
        finals.setPrefix(ChatColor.GREEN + "0 " + ChatColor.WHITE + "F.Kills");
        finals.setSuffix(ChatColor.GREEN + " 0 " + ChatColor.WHITE + "F.Assists");
        objective.getScore(teamKeyFinals).setScore(0);
//        String teamKeyFinalAssists = ChatColor.WHITE.toString();
//        Team finalAssists = scoreboard.registerNewTeam("finalAssists");
//        finalAssists.addEntry(teamKeyFinalAssists);
//        finalAssists.setPrefix(ChatColor.GREEN + "0");
//        finalAssists.setSuffix(ChatColor.WHITE + " F.Assists");
//        objective.getScore(teamKeyFinalAssists).setScore(0);
    }

    public static void createPlayerScoreboardBelowname(Player player) {
        Scoreboard scoreboard = player.getScoreboard();
        Objective showhealth = scoreboard.registerNewObjective( "showhealth", "health");
        showhealth.setDisplayName(ChatColor.RED + "❤");
        showhealth.setDisplaySlot(DisplaySlot.BELOW_NAME);
    }

    public static void updateFinalKills(Player player) {
        Scoreboard scoreboard = player.getScoreboard();
        Team teamDisplay = scoreboard.getTeam("finals");
//        teamDisplay.getEntries().forEach(entry -> {})
        teamDisplay.setPrefix(ChatColor.GREEN + "" + player_finalKills.get(player.getUniqueId()) + ChatColor.WHITE + " F.Kills");
    }

    public static void updateFinalAssists(Player player) {
        Scoreboard scoreboard = player.getScoreboard();
        Team teamDisplay = scoreboard.getTeam("finals");
//        teamDisplay.getEntries().forEach(entry -> {})
        teamDisplay.setSuffix(ChatColor.GREEN + " " + player_finalAssists.get(player.getUniqueId()) + ChatColor.WHITE + " F.Assists");
    }
}
