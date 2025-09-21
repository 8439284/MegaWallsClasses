package org.ajls.megawallsclasses;

import org.ajls.lib.utils.ScoreboardU;
import org.ajls.megawallsclasses.utils.FloodFillU;
import org.ajls.megawallsclasses.utils.LocationU;
import org.bukkit.*;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.*;

import static org.ajls.megawallsclasses.BlocksModify.fill;
import static org.ajls.megawallsclasses.GamemodeUtils.isPlayerPlayable;
import static org.ajls.megawallsclasses.InitializeClass.initializeAutoEnergyAccumulation;
import static org.ajls.megawallsclasses.MegaWallsClasses.plugin;
import static org.ajls.megawallsclasses.MyListener.initializeClass;
import static org.ajls.megawallsclasses.ScoreboardsAndTeams.*;

public class GameManager {
    public static int gameStage = 0;
    public static HashMap<UUID, String> wither_team = new HashMap<>();
    public static ArrayList<UUID> witherSkulls = new ArrayList<>();
//    public static int blue_wither_dead = 0;
//    public static int red_wither_dead = 0;
    public static HashSet<String> witherDeadTeams = new HashSet<>();
    public static HashMap<UUID, BukkitTask> wither_witherTeleportTask = new HashMap<>();
//    public static HashMap<UUID, BukkitTask> wither_witherFireTask = new HashMap<>();
    public static ArrayList<BukkitTask> witherFireTask = new ArrayList<>();

    public static void Start() {
        if (gameStage >= 0){
            gameStage = 0;
            Bukkit.broadcastMessage("THE GAME HAS STARTED");
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.setGameMode(GameMode.SURVIVAL);
                initializeClass(p);
                initializeAutoEnergyAccumulation(p);
                InitializeClass.initializeDeathMatchAutoEnergyAccumulation(p); // for test only remember to remove
                Cooldown.displayCooldown(p);
//                Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
//                String teamName = scoreboard.getPlayerTeam(p).getName();
                if (getPlayerTeam(p) == null) {
                    int red_team_number = 0;
                    int blue_team_number = 0;
                    for (Player p2 : Bukkit.getOnlinePlayers()) {
                        if (getPlayerTeam(p2) != null) {
                            if (getPlayerTeam(p2).getName().equals("blue_team")) {
                                blue_team_number++;
                            }
                            else if (getPlayerTeam(p2).getName().equals("red_team")) {
                                red_team_number++;
                            }
                        }
                    }
                    if (blue_team_number > red_team_number) {
                        getTeam("red_team").addEntry(p.getName());
                    }
                    else {
                        getTeam("blue_team").addEntry(p.getName());
                    }
                }
                teamTeleportSpawn(p);
//                teleportNearPlayers(p);
                p.closeInventory();


//            String teamName = getPlayerTeam(p).getName();
//                MyListener.pReset(p, teamName);
            }
//            spawnTeamWither();
            //disabled because nobody likes killing withers
            wallCollapse();
//            Location loc_blue_iron = new Location(Bukkit.getWorld("world"), plugin.getConfig().getInt("locations.loc_blue_iron.x"), plugin.getConfig().getInt("locations.loc_blue_iron.y"), plugin.getConfig().getInt("locations.loc_blue_iron.z"));
//            Location loc_red_iron = new Location(Bukkit.getWorld("world"), plugin.getConfig().getInt("locations.loc_red_iron.x"), plugin.getConfig().getInt("locations.loc_red_iron.y"), plugin.getConfig().getInt("locations.loc_red_iron.z"));
//            BukkitScheduler scheduler = plugin.getServer().getScheduler();
//            scheduler.scheduleSyncRepeatingTask(plugin, () -> {
//                // Do something
//                Bukkit.getWorld("world").dropItemNaturally(loc_blue_iron, new ItemStack(Material.IRON_INGOT));
//                Bukkit.getWorld("world").dropItemNaturally(loc_red_iron, new ItemStack(Material.IRON_INGOT));
//
//            }, 0L, 20L);
        }
    }

    public static void teamTeleportSpawn (Player player) {
        if (getPlayerTeam(player) != null ) {

        }
        String teamName = ScoreboardU.getPlayerTeamName(player, true);//getPlayerTeam(player).getName();
        Configuration configuration = plugin.getConfig();
        if (teamName.equals("blue_team")) {
            Location loc_spawn_blue = new Location(Bukkit.getWorld("world"), configuration.getInt("locations.loc_blue_spawn.x")+0.5, configuration.getInt("locations.loc_blue_spawn.y"), configuration.getInt("locations.loc_blue_spawn.z")+0.5, configuration.getInt("locations.loc_blue_spawn.yaw"), configuration.getInt("locations.loc_blue_spawn.pitch"));
            player.teleport(loc_spawn_blue);
        }
        else if (teamName.equals("red_team")) {
            Location loc_spawn_red = new Location(Bukkit.getWorld("world"), configuration.getInt("locations.loc_red_spawn.x")+0.5, configuration.getInt("locations.loc_red_spawn.y"), configuration.getInt("locations.loc_red_spawn.z")+0.5, configuration.getInt("locations.loc_red_spawn.yaw"), configuration.getInt("locations.loc_red_spawn.pitch"));
            player.teleport(loc_spawn_red);
        }
        else {
            Location loc_default = new Location(Bukkit.getWorld("world"), configuration.getInt("locations.loc_red_spawn.x")+0.5, configuration.getInt("locations.loc_red_spawn.y"), configuration.getInt("locations.loc_red_spawn.z")+0.5, configuration.getInt("locations.loc_red_spawn.yaw"), configuration.getInt("locations.loc_red_spawn.pitch"));
            player.teleport(loc_default);
//            player.teleport(Bukkit.getWorld("world").getSpawnLocation());
        }
//        else {
//            int red_team_number = 0;
//            int blue_team_number = 0;
//            for (Player p : Bukkit.getOnlinePlayers()) {
//                if (getPlayerTeam(p) != null) {
//                    if (getPlayerTeam(p).getName().equals("blue_team")) {
//                        blue_team_number++;
//                    }
//                    else if (getPlayerTeam(p).getName().equals("red_team")) {
//                        red_team_number++;
//                    }
//                }
//                if (blue_team_number > red_team_number) {
//                    getTeam("red_team").addEntry(player.getName());
//                }
//                else {
//                    getTeam("blue_team").addEntry(player.getName());
//                }
//            }
//            teamTeleportSpawn(player);
//        }
    }

    public static Location teleportNearPlayers(Player player) {
        UUID playerUUID = player.getUniqueId();
        World world = Bukkit.getWorld("world");
        List<Player> players = world.getPlayers();
        boolean removedOutVictim = players.remove(player);
        if (!removedOutVictim) {
            Bukkit.broadcastMessage("Oh no the one that should be teleported can't be deselect");
            for (int i = players.size()-1; i >= 0; i--) {
                Player target = players.get(i);
                if (target.getUniqueId() == playerUUID) {
                    players.remove(i);  //TODO: remove for same team players
                    Bukkit.broadcastMessage("Removed using uuid comparison");
                }
            }
//            for (Player target: players) {
//                UUID targetUUID = target.getUniqueId();
//                if (targetUUID == playerUUID) {
//
//                }
//            }
        }
        if (players.size() > 0) {
//            Random random = new Random();
//            int randomIndex = random.nextInt(players.size());
            int randomIndex = Utils.random(0, players.size()-1);
            Player target = players.get(randomIndex);
            Location targetLocation = target.getLocation();
            HashSet<Location> possibleSpawns = FloodFillU.floodFillRespawn(targetLocation, 32);  //TODO; return 2 sets and 1 range from 17 to 32 (Should be 16 to 31) and the other range from 0 to 16(should be 15. If the first one is empty choose a random in the second one.
            //or ignore the toD0 to add an element of surprise(farther distances are much more which causes a triangular dist, and if thought of faces it is x^2 dist)
             ArrayList<Location> possibleSpawnsArrayList = new ArrayList<>(possibleSpawns);
//            Location[] possibleSpawnsArray = (Location[]) possibleSpawns.toArray();

            int xOffset =  Utils.random(-32,32,false);
            int zOffset = Utils.random(-32,32,false);
            player.sendMessage("x: " + xOffset+",z: " +zOffset);
            Location teleportLocation = targetLocation.clone().add(xOffset, 0, zOffset);  //originally 17 32 but due to that can't +1, +20 and for extra bit of fun changed to -32 32

            if (possibleSpawns == null || possibleSpawns.isEmpty()) {
                teleportLocation = targetLocation;
            }
            else {
                int index = Utils.random(0 , possibleSpawns.size() -1);
                teleportLocation = possibleSpawnsArrayList.get(index);
            }



            /*
            int height = world.getHighestBlockYAt(teleportLocation);
            teleportLocation.setY(height+1);
            teleportLocation.setPitch(0);

             */


            Vector direction = targetLocation.toVector().subtract(teleportLocation.toVector());
//            direction.setY(0);
            teleportLocation.setYaw(LocationU.getYaw(direction));
            teleportLocation.setPitch(LocationU.getPitch(direction));

            player.teleport(teleportLocation);
            return teleportLocation;
        }
        else {
            Bukkit.broadcastMessage("No players to teleport to");
            return player.getLocation();
        }
//        player.addPotionEffect(new PotionEffect(PotionEffectType.INSTANT_HEALTH, 20*3, 255, false, true));
//        player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20*3, 255, false, true));
    }

    static void wallCollapse() {
        fill(-826, 38, -190, -725 ,73, -190, Material.AIR);
        fill(-826, 38, -89, -725 ,73, -89, Material.AIR);
    }

    static void spawnTeamWither() {
        Configuration configuration = plugin.getConfig();
        World world = Bukkit.getWorld("world");
        Location blue_location = new Location(world, configuration.getInt("locations.loc_blue_wither.x"), configuration.getInt("locations.loc_blue_wither.y"), configuration.getInt("locations.loc_blue_wither.z"));
        Wither blue_wither = (Wither) world.spawnEntity(blue_location, EntityType.WITHER);
        Location red_location = new Location(world, configuration.getInt("locations.loc_red_wither.x"), configuration.getInt("locations.loc_red_wither.y"), configuration.getInt("locations.loc_red_wither.z"));
        Wither red_wither = (Wither) world.spawnEntity(red_location, EntityType.WITHER);
        blue_wither.setMaxHealth(1200);
        red_wither.setMaxHealth(1200);
        blue_wither.setHealth(100);
        red_wither.setHealth(100);
        UUID blue_wither_UUID = blue_wither.getUniqueId();
        UUID red_wither_UUID = red_wither.getUniqueId();
        wither_team.put(blue_wither_UUID, "blue_team");
        wither_team.put(red_wither_UUID, "red_team");
        witherTeleportTask(blue_wither, blue_location);
        witherTeleportTask(red_wither, red_location);
//        BukkitScheduler scheduler = Bukkit.getScheduler();
//        BukkitTask task = scheduler.runTaskTimer(plugin, () -> {
//            blue_location.setYaw(blue_wither.getLocation().getYaw());
//            red_location.setYaw(red_wither.getLocation().getYaw());
//            blue_location.setPitch(blue_wither.getLocation().getPitch());
//            red_location.setPitch(red_wither.getLocation().getPitch());
//            blue_wither.teleport(blue_location);
//            red_wither.teleport(red_location);
////            blue_wither.launchProjectile(WitherSkull.class, blue_wither.getLocation().getDirection());
//
//        },0, 1);
//        wither_witherTeleportTask.put()
        BukkitScheduler scheduler = plugin.getServer().getScheduler();
        BukkitTask task = scheduler.runTaskTimer(plugin, () -> {
            for (UUID witherUUID : wither_team.keySet()) {
                String teamName = wither_team.get(witherUUID);
                Wither wither = (Wither) Bukkit.getEntity(witherUUID);
//                World world = wither.getWorld();
                Location loc = wither.getLocation();
                double distance = Double.MAX_VALUE;
                Player target = null;
                Location targetLoc = null;
                for (Player p : world.getPlayers()) {
                    if (isPlayerPlayable(p) && !getPlayerTeamName(p).equals(teamName)) {
                        Location loc2 = p.getLocation();
                        double distance2 = loc2.distance(loc);
                        if (distance2 < distance && distance2 <= 50) {
                            distance = distance2;
                            target = p;
                            targetLoc = loc2;
                        }
                    }
                }
                if (target != null) {
                    Location targetEyeLoc = target.getEyeLocation();
                    double targetEyeLocY = targetEyeLoc.getY();
                    double deltaY = targetEyeLocY - targetLoc.getY();
                    Location targetMiddleLoc = targetLoc.clone();
                    targetMiddleLoc.setY(targetEyeLocY - (deltaY/2) );
                    Location witherEyeLoc = wither.getEyeLocation();
                    Vector vector = targetMiddleLoc.toVector().subtract(witherEyeLoc.toVector());
                    WitherSkull witherSkull = (WitherSkull) world.spawnEntity(witherEyeLoc, EntityType.WITHER_SKULL);
                    witherSkull.setVelocity(vector.normalize());
                    witherSkull.setShooter(wither);
//                    WitherSkull witherSkull = wither.launchProjectile(WitherSkull.class, vector.normalize());
                    witherSkulls.add(witherSkull.getUniqueId());
//                    Bukkit.broadcastMessage(target.getName());
                }
            }
        },0, 80);
        witherFireTask.add(task);
    }

    private static void witherTeleportTask(Wither wither, Location location) {
        BukkitScheduler scheduler = Bukkit.getScheduler();
        BukkitTask task = scheduler.runTaskTimer(plugin, () -> {
            location.setYaw(wither.getLocation().getYaw());
            location.setPitch(wither.getLocation().getPitch());
            wither.teleport(location);
//            blue_wither.launchProjectile(WitherSkull.class, blue_wither.getLocation().getDirection());

        },0, 1);
        wither_witherTeleportTask.put(wither.getUniqueId(), task);
    }

}
