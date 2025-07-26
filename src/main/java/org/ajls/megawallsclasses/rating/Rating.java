package org.ajls.megawallsclasses.rating;

import org.ajls.lib.advanced.HashMapInteger;
import org.ajls.megawallsclasses.MegaWallsClasses;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class Rating {
    //rating.playername: amount(double)
    //ratings.playername.rating1: amount //we use the upper
    //玩家分数分配  玩家太久没玩的平均值和散度调整  玩家回归的重新预估  意外死亡的分数去处
    /*
    考虑到越近伤害的玩家胁迫感越高 所以玩家伤害权会随时间指数减少
    考虑到血药不喝 = 最后那些人打掉血药血 没喝的血药按伤害加权平摊
    考虑到不喝血药就是菜 但是血药的血没有原本血那么重要（？可以保命 但是需要时间喝下） 所以需要对血药血加权扣分
    考虑到鱿鱼药被抢也算菜 对鱿鱼药加权扣分
    考虑到随着时间回血 等于伤害白打 需要移除伤害的权
    目前系统 按总伤害权扣除分数 后面会更改
    自然伤害（比如摔落）要考虑
     */
    public static Loser_WinnerMap loser_winnerMap = new Loser_WinnerMap();
    public static boolean tryCreateRating(Player player) {
        Configuration configuration = MegaWallsClasses.getPlugin().getConfig();
        String playerName = player.getName();
        if (configuration.get("rating." + playerName) == null) {
            configuration.set("rating." + playerName, 0);
            MegaWallsClasses.getPlugin().saveConfig();
            return true;
        }
        return false;
    }

    public static void setRating(Player player, double rating) {
//        Configuration configuration = MegaWallsClasses.getPlugin().getConfig();
//        String playerName = player.getName();
//        configuration.set("rating." + playerName, rating);
        setRating(player.getName(), rating);
    }

    public static void setRating(String playerName, double rating) {
        Configuration configuration = MegaWallsClasses.getPlugin().getConfig();
//        String playerName = player.getName();
        configuration.set("rating." + playerName, rating);
        MegaWallsClasses.getPlugin().saveConfig();
    }

    public static double getRating(Player player) {
//        Configuration configuration = MegaWallsClasses.getPlugin().getConfig();
//        String playerName = player.getName();
//        return configuration.getDouble("rating." + playerName);
        return getRating(player.getName());
    }

    public static double getRating(String playerName) {
        Configuration configuration = MegaWallsClasses.getPlugin().getConfig();
        return configuration.getDouble("rating." + playerName);
    }

    public static void addRating(Player player, double rating) {
//        Configuration configuration = MegaWallsClasses.getPlugin().getConfig();
//        String playerName = player.getName();
//        double value = configuration.getDouble("rating." + playerName);
//        configuration.set("rating." + playerName, value + rating);
        addRating(player.getName(), rating);
    }

    public static double addRating(String playerName, double rating) {
        Configuration configuration = MegaWallsClasses.getPlugin().getConfig();
//        String playerName = player.getName();
        double value = configuration.getDouble("rating." + playerName);
        double newValue = value + rating;
        configuration.set("rating." + playerName, newValue);
        MegaWallsClasses.getPlugin().saveConfig();
        return newValue;
    }

    public static void update(Player loser, HashMap<UUID, Double> winners_weight) {


//        Player winner, Player loser, double weight)
        double totalAmount = 0;
        double loserRating = getRating(loser);
        for (UUID uuid : winners_weight.keySet()) {
            double weight = winners_weight.get(uuid);
            String winnerName = Bukkit.getOfflinePlayer(uuid).getName();
            double winnerRating = getRating(winnerName);
            double diff = (winnerRating - loserRating)/100; //add a hundred makes it look nicer  // winner: novice loser: master odds really small 1/10000 opposite 10000/1 prob=1/{1+1/opposite} = 1/{1+odd}
            double expDiff = Math.pow(10, diff);
            double probability = 1 / (1+expDiff);
            double amount = probability * weight * 10; // * k which is 1, can be changed later  // now k = 0.1 or else grow too fast
            //by default is 10/2 =5, so if you score less than 5 pts you are better than the other
            totalAmount += amount;
            addRating(winnerName, amount);
            Player winner = Bukkit.getPlayer(uuid);
            if (winner != null) {
                winner.sendMessage("You have won " + (int) amount + " points.");
                winner.sendMessage("Current points: " + (int) getRating(winner));
            }
        }
        addRating(loser, -totalAmount);
        loser.sendMessage("You have lost " + (int) totalAmount + " points.");
        loser.sendMessage("Current points: " + (int) getRating(loser));
        MegaWallsClasses.getPlugin().saveConfig();



    }

    public static HashMap<UUID, Double> normalize(HashMap<UUID, Double> winners_weight) {
        double totalWeight = 0;
        for (UUID uuid : winners_weight.keySet()) {
            double weight = winners_weight.get(uuid);
            totalWeight += weight;
        }
        for (UUID uuid : winners_weight.keySet()) {
            double weight = winners_weight.get(uuid);
            weight /= totalWeight;
            winners_weight.put(uuid, weight);
        }
        return winners_weight;
    }

    public static void normalizeAndUpdate(Player loser, HashMap<UUID, Double> winners_weight) {
        normalize(winners_weight);
        update(loser, winners_weight);
    }
}
