package org.ajls.megawallsclasses;

import org.ajls.lib.utils.ScoreboardU;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.entity.CraftMob;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
import org.checkerframework.checker.units.qual.C;

public class ScoreboardsAndTeams {
    //create scoreboard
    public static void createScoreboard(String name) {//@Nullable String type, @Nullable String displayname
        if (Bukkit.getScoreboardManager().getMainScoreboard().getObjective(name) == null) {
            ScoreboardManager manager = Bukkit.getScoreboardManager();
            Scoreboard board = manager.getMainScoreboard();
            Objective objective = board.registerNewObjective(name, "dummy");
        }
    }


    public static Objective getScoreboard(String name){
        //get the scoreboard of this name
        Objective objective = Bukkit.getScoreboardManager().getMainScoreboard().getObjective(name);
        return objective;
    }

    public static int getScore(Player player, String name) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getMainScoreboard();
        Objective objective = board.getObjective(name);
        int score;
        score = objective.getScore(player.getName()).getScore();
        return score;
    }

    public static void setScore(Player player, String name, int score) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getMainScoreboard();
        Objective objective = board.getObjective(name);
        objective.getScore(player.getName()).setScore(score);
    }

    public static void addScore(Player player, String name, int score) {
        setScore(player, name, getScore(player, name) + score);
    }

    public static void registerTeam(String teamName){
        // register team
        if (Bukkit.getScoreboardManager().getMainScoreboard().getTeam(teamName) == null) {
            Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam(teamName);
        }
    }

    public static void setTeamRule(String teamName, Team.Option option, Team.OptionStatus optionStatus){
        ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
        Team team = scoreboardManager.getMainScoreboard().getTeam(teamName);
        team.setOption(option,optionStatus);
    }

    public static void setFriendlyFire(String teamName, boolean friendlyFire){
        ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
        Team team = scoreboardManager.getMainScoreboard().getTeam(teamName);
        team.setAllowFriendlyFire(friendlyFire);
    }

    public static void setTeamColor(String teamName, ChatColor chatColor){
        ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
        Team team = scoreboardManager.getMainScoreboard().getTeam(teamName);
        team.setColor(chatColor);
    }

    public static Team getTeam(String teamName){
        //get the team of this name
        Team team = Bukkit.getScoreboardManager().getMainScoreboard().getTeam(teamName);
        return team;
    }

    public static Team getPlayerTeam(Player player) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = scoreboard.getPlayerTeam(player);
        return team;
    }

    public static String getPlayerTeamName(Player player) {
//        return getPlayerTeam(player).getName();
        Team team = getPlayerTeam(player);
        if (team == null) return null;
        return team.getName();
    }

    public static Team.OptionStatus getPlayerTeamOption(Player player, Team.Option option) {
        Team team = getPlayerTeam(player);
        return team.getOption(option);
    }

    public static void setPlayerTeamFriendlyFire(Player player, boolean friendlyFire){
        getPlayerTeam(player).setAllowFriendlyFire(friendlyFire);
    }

    public static boolean getPlayerTeamFriendlyFire(Player player){
        return getPlayerTeam(player).allowFriendlyFire();
    }

    public static ChatColor getPlayerTeamColor(Player player, boolean notNull){
        if (getPlayerTeam(player) == null) {
            if (notNull) {
                return ChatColor.WHITE;
            }
            return null;
        }
        return getPlayerTeam(player).getColor();
    }

    public static ChatColor getPlayerTeamColor(Player player) {
        return getPlayerTeamColor(player, true);
    }

    public static boolean isPlayerSameTeam(Player player1, Player player2) {
        String teamName1 = ScoreboardU.getPlayerTeamName(player1);
        String teamName2 = ScoreboardU.getPlayerTeamName(player2);
//        String teamName1 = getPlayerTeam(player1).getName();
//        String teamName2 = getPlayerTeam(player2).getName();
        if (player1 == player2) return true;
        if (teamName1 == null || teamName2 == null) return false;
        return teamName1.equals(teamName2);
    }

}

