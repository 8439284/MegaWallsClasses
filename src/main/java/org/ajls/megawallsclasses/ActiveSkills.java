package org.ajls.megawallsclasses;

import org.ajls.lib.advanced.BukkitTaskMap;
import org.ajls.lib.advanced.HashMapInteger;
import org.ajls.lib.advanced.HaxhMap;
import org.ajls.lib.advanced.hashMap.HashMapDouble;
import org.ajls.lib.utils.PlayerU;
import org.ajls.lib.utils.ScoreboardU;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.Configuration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Math.*;
import static org.ajls.megawallsclasses.BlocksModify.isInBounds;
import static org.ajls.megawallsclasses.EnergyAccumulate.addEnergy;
import static org.ajls.megawallsclasses.EnergyAccumulate.autoEnergyAccumulation;
import static org.ajls.megawallsclasses.GamemodeUtils.*;
import static org.ajls.megawallsclasses.ItemStackModify.setUnbreakable;
import static org.ajls.megawallsclasses.MegaWallsClasses.plugin;
import static org.ajls.megawallsclasses.MyListener.*;
import static org.ajls.megawallsclasses.ScoreboardsAndTeams.getPlayerTeam;
import static org.ajls.megawallsclasses.ScoreboardsAndTeams.getScore;
import static org.bukkit.Bukkit.getWorld;

public class ActiveSkills {
//    static HashMap<UUID, Location> tnt_location = new HashMap<>(); //record the previous location of the tnt
    static HashMap<UUID, BukkitTask> tnt_explode_task = new HashMap<>();
    static HashMap<UUID, UUID> tornado_shaman = new HashMap<>();
    static HashMap<UUID, BukkitTask> snowman_blizzardTask = new HashMap<>();
    static HashMap<Block, Integer> block_blizzardTimes = new HashMap<>(); // check the latest time block was turned into blizzard preventing previous blizzard make block normal
    static HashMap<Block, BlockState> block_blizzardBlockState = new HashMap<>();

    public static HashSet<HashMap<UUID, BukkitTask>> cancelWhenOfflineTask = new HashSet<>();


    //herobrine

    public static void herobrine_active_skill(Player player) {
        ArrayList<Player> nearby = new ArrayList<Player>();
        nearby = herobrineGetNearbyPlayers(player, 5, 1);
//                player.sendMessage("test");
        if (!nearby.isEmpty()) {
            for (int i = 0; i < nearby.size(); i++) {
                Player player1 = nearby.get(i);
                if (!player1.equals(player)) {
                    player1.damage(0.0000000000000000000000000000000000000000000007006492321625, player);
                    addHealth(player1, -5);
                    player1.sendMessage(ChatColor.RED + "!天谴! " + ChatColor.RED + "HP " + ChatColor.YELLOW + "-5");
//                        player1.playSound(player1, Sound.ENTITY_ZOMBIE_HURT, 1, 1);
                    player1.getWorld().strikeLightningEffect(player1.getLocation());
                }
            }
            player.sendMessage(ChatColor.WHITE + "天谴");
            ScoreboardsAndTeams.setScore(player, "energy", 0);
            player.setLevel(0);
        }
        else {
            player.sendMessage(ChatColor.YELLOW + "5格内 " + ChatColor.RED + "没有敌人");
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_SNARE, 1, 1);
        }
    }

    static ArrayList<Player> herobrineGetNearbyPlayers(Player player, double range, int count) {
        Location center = player.getLocation();
        HashMap<Double, Player> distancelist = new HashMap<Double, Player>();
        ArrayList<Player> nearby = new ArrayList<Player>();
        World world = player.getWorld();
//        String teamName = getPlayerTeam(player).getName();
        for (Player p : world.getPlayers()) {
            if (PlayerU.isPlayerPlayableEnemy(p, player)) {
                Location loc = p.getLocation();
                double distance = loc.distance(center);
                if (distance <= range) {
                    distancelist.put(distance, p);
                }
            }
        }
        for (int i = 0; i < Math.min(distancelist.size(), count); i++) {
            double nearest_distance = range + 1;
            for (double distance2 : distancelist.keySet()) {
                if (nearest_distance > distance2) {
                    nearest_distance = distance2;
                }
            }
            Player player1 = distancelist.get(nearest_distance);
            nearby.add(player1);
            distancelist.remove(nearest_distance);
        }
        return nearby;
    }

    //enderman
    public static void enderman_active_skill(Player player) {
        //                ArrayList<Player> nearby_enderman = new ArrayList<Player>();
//                nearby_enderman = getNearbyPlayers(player, 26, 1);
//                RayTraceResult result = Bukkit.getWorld("world").rayTraceEntities(player.getLocation(),player.getLocation().getDirection(), 26);
//                Entity entity = result.getHitEntity();
//                player.sendMessage("test");
//                Entity entity = (Entity) getTarget(player, 26).get(0).get(0);
//                boolean through_wall = (boolean) getTarget(player)
        // direct copy of method
        BlockIterator bItr = new BlockIterator(player.getEyeLocation(), 0, 26);
        Block block;
        Location loc;
        Player player1 = null;
        boolean through_wall = false;
        int bx, by, bz;
        double ex, ey, ez;
        // loop through player's line of sight
        while (bItr.hasNext()) {
            block = bItr.next();
            if (!block.isPassable()) {  //block.getType() != Material.AIR
                through_wall = true;
            }
            bx = block.getX();
            by = block.getY();
            bz = block.getZ();
            // check for entities near this block in the line of sight
            for (Player p : Bukkit.getWorld("world").getPlayers()) {
                if (isPlayerPlayableEnemy(p, player)){
                    loc = p.getLocation();
                    ex = loc.getX();
                    ey = loc.getY();
                    ez = loc.getZ();
                    if ((bx-.75 <= ex && ex <= bx+1.75) && (bz-.75 <= ez && ez <= bz+1.75) && (by-1 <= ey && ey <= by+2.5)) {
                        // entity is close enough, set target and stop
                        player1 = p;
                        break;
                    }
                }
            }
            if (player1 != null) {
                break;
            }
        }
        // copy end
        if (player1 != null && player1 instanceof Player) {
//                        Player player1 = nearby_enderman.get(0);
            player.teleport(player1.getLocation());
            player1.sendMessage(ChatColor.RED + "!传送!");
            player.addPotionEffect((new PotionEffect(PotionEffectType.SPEED, 100, 2, false, false)));
            if (through_wall){
                player.addPotionEffect((new PotionEffect(PotionEffectType.WEAKNESS, 60, 0, false, false))); //original weakness 2 but can hurt anyone with iron sword
                player.sendMessage(ChatColor.RED + "!穿墙! " + ChatColor.GRAY + "虚弱 " + ChatColor.YELLOW + "I " + ChatColor.AQUA + "3s");
            }
            getWorld("world").playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
            player.sendMessage(ChatColor.LIGHT_PURPLE + "传送 " + ChatColor.WHITE + "速度 " + ChatColor.YELLOW + "III " + ChatColor.AQUA + "5s");
            ScoreboardsAndTeams.setScore(player, "energy", 0);
            player.setLevel(0);
        }
//                if (nearby_enderman.size() > 0) {
//                    Player player1 = nearby_enderman.get(0);
//                    player.teleport(player1.getLocation());
//                    player.addPotionEffect((new PotionEffect(PotionEffectType.SPEED, 100, 2, false, false)));
//                    getWorld("world").playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
//                    player.sendMessage(ChatColor.GREEN + "放放你的");
//                    MegaWallsClasses.setScore(player, "energy", 0);
//                    player.setLevel(0);
//                }
        else {
            player.sendMessage(ChatColor.YELLOW + "准心26格内 " + ChatColor.RED + "没有敌人");
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_SNARE, 1, 1);
        }
    }

    //dreadlord
    public static void dread_lord_active_skill(Player player, double speed) {
        double degree = toRadians(15); //original 30
        World world = player.getWorld();
        Location loc = player.getLocation();
        Location loc2 = loc.clone();
        Location loc3 = loc.clone();
        float yaw = loc.getYaw();
        float pitch = loc.getPitch();
        double y = sin(toRadians(pitch)); //x方+y方=1
        double x = cos(toRadians(pitch));
//        pitch = pitch * (float) cos(degree);
        y = y * cos(degree);   //按照转角度等比缩放
        x = x * cos(degree);
        double z = sin(degree);
        pitch = (float) toDegrees(asin(y));  //坐标变角度
//        double delta_x = cos(pitch);
//        double delta_z = sin(degree);
        float delta_yaw = (float) toDegrees(atan(z/x));
        loc2.setPitch(pitch);
        loc3.setPitch(pitch);
        loc2.setYaw(yaw+delta_yaw);
        loc3.setYaw(yaw-delta_yaw);
        Vector vector = loc.getDirection();
        Vector vector2 = loc2.getDirection();
        Vector vector3 = loc3.getDirection();
        vector = vector.multiply(speed);
        vector2 = vector2.multiply(speed);
        vector3 = vector3.multiply(speed);
        WitherSkull witherSkull = player.launchProjectile(WitherSkull.class, vector);
        WitherSkull witherSkull1 = player.launchProjectile(WitherSkull.class, vector2);
        WitherSkull witherSkull2 = player.launchProjectile(WitherSkull.class, vector3);
        dread_lord_witherSkulls.add(witherSkull.getUniqueId());
        dread_lord_witherSkulls.add(witherSkull1.getUniqueId());
        dread_lord_witherSkulls.add(witherSkull2.getUniqueId());
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_WITHER_SHOOT, 1, 1);
//        Bukkit.broadcastMessage(witherSkull.getShooter().toString());
        player.sendMessage(ChatColor.RED + "dre主动名字忘记了 " + ChatColor.RED + "HP " + ChatColor.GREEN + "+7");
        ScoreboardsAndTeams.setScore(player, "energy", 0);
        player.setLevel(0);
    }

    //303
    public static void entity_303_active_skill(Player player) {
        if (isPlayerPlayable(player)) {
            Location first = null;
            boolean blocked = false;
            Vector vector = player.getEyeLocation().getDirection();
            vector = vector.multiply(0.1); // vector  step
            Vector start = new Vector(player.getEyeLocation().getX(), player.getEyeLocation().getY(), player.getEyeLocation().getZ()); // start vector from origin to player
            Configuration configuration = plugin.getConfig();
            for (int i = 0; i <= 114514; i++) {
                Location loc = new Location(player.getWorld(), start.getX(), start.getY(), start.getZ());
                if (isInBounds(start.getX(), start.getY(), start.getZ(), configuration.getInt("locations.loc_map_min.x"), configuration.getInt("locations.loc_map_min.y"), configuration.getInt("locations.loc_map_min.z"), configuration.getInt("locations.loc_map_max.x"), configuration.getInt("locations.loc_map_max.y"), configuration.getInt("locations.loc_map_max.z"))) {
//                    spawnParticles(loc);
                    if ((i %= 10) == 0) {
                        spawnParticles(loc);
                    }
                    Block block = loc.getBlock();
                    BoundingBox blockBoundingBox = block.getBoundingBox();
                    if (blockBoundingBox.contains(start)) {  //!block.getType().equals(Material.AIR)
                        first = loc;
                        if (i != 0) {
                            start.subtract(vector);
                        }
//                else {
//                    Location pro_loc = loc.clone();
//                }
                        Location pre_loc = new Location(player.getWorld(), start.getX(), start.getY(), start.getZ());
                        World world = player.getWorld();
                        Entity marker = world.spawnEntity(new Location(world, 0, 114514,0), EntityType.MARKER);
                        marker_player.put(marker.getUniqueId(), player.getUniqueId());
                        marker.remove();
                        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
                        scheduler.scheduleSyncDelayedTask(MegaWallsClasses.getPlugin(), () -> {
                            marker_player.remove(marker.getUniqueId());
                        }, 2L);
                        world.createExplosion(pre_loc, 1.5F, false, false, marker); // damage and kb
                        world.createExplosion(pre_loc, 2F, false, true, player); // damage blocks
                        blocked = true;
                        break;
                    }
                    for (Player p : player.getWorld().getPlayers()) {
                        if (!p.equals(player)) {
                            if (p.getBoundingBox().contains(start)) {
//                        Bukkit.broadcastMessage(start.toString());
//                        Bukkit.broadcastMessage(p.getBoundingBox().toString());
                                addHealth(p, -5);
                                p.damage(0.000000000000000000000000000000000000000000000700649232162408535461864791644958065640135, player);
                                p.setFireTicks(120);
                                addEnergy(player, 18);
                                first = loc;
                                break;
                            }
                        }
                    }
                    if (first != null) {
                        break;
                    }
                    start = start.add(vector);
                }
                else {
                    break;
                }
            }
        }
        else {
            player.sendMessage("好吧这是我第一次看到303放技能时死掉");
        }
    }

    static void spawnParticles(Location location) {
        Particle.DustOptions dustOptions = new Particle.DustOptions(Color.RED, 1.0F);
        getWorld("world").spawnParticle(Particle.DUST, location, 1, dustOptions);
    }

    //creeper
    public static void creeper_active_skill(Player player) {
        World world = player.getWorld();
        Location location = player.getEyeLocation();
        Vector vector = location.getDirection();
        TNTPrimed tntPrimed = (TNTPrimed) world.spawnEntity(location, EntityType.TNT);
        tntPrimed.setFuseTicks(114514);
        tntPrimed.setVelocity(vector.multiply(3));
        UUID tntPrimedUUID = tntPrimed.getUniqueId();
        UUID playerUUID = player.getUniqueId();
        tnt_creeper.put(tntPrimedUUID, playerUUID);
//        tnt_location.put(tntPrimedUUID, tntPrimed.getLocation());
//        tntPrimed.setMetadata("player", new FixedMetadataValue(plugin, playerUUID));
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        BukkitTask task = scheduler.runTaskTimer(plugin, () -> {
            BoundingBox tntPrimedBoundingBox = tntPrimed.getBoundingBox();
            boolean onPeople = false;
            for (Player p : world.getPlayers()) {
                if (p.getUniqueId().equals(playerUUID)) continue;
                if (tntPrimedBoundingBox.overlaps(p.getBoundingBox())) {
                    tntPrimed.setFuseTicks(0);
                    tntPrimed.setMetadata("onPeople", new FixedMetadataValue(plugin, true));
                    onPeople = true;
                    scheduler.scheduleSyncDelayedTask(plugin, () -> {
                        BukkitTask task1 = tnt_explode_task.remove(tntPrimedUUID);
                        if (task1 != null) {
                            task1.cancel();
                        }
                    }, 2L);
                }
            }
            if (!onPeople) {
                if (tntPrimed.getVelocity().getX() == 0 || tntPrimed.getVelocity().getY() == 0 || tntPrimed.getVelocity().getZ() == 0) {
                    tntPrimed.setFuseTicks(0);
                    scheduler.scheduleSyncDelayedTask(plugin, () -> {
                        BukkitTask task1 = tnt_explode_task.remove(tntPrimedUUID);
                        if (task1 != null) {
                            task1.cancel();
                        }
                    }, 2L);
                }
            }
//            if (tntPrimed.getLocation().getX() == tnt_location.get(tntPrimedUUID).getX() || tntPrimed.getLocation().getY() == tnt_location.get(tntPrimedUUID).getY() || tntPrimed.getLocation().getZ() == tnt_location.get(tntPrimedUUID).getZ()) {
//                tntPrimed.setFuseTicks(0);
//            }
//            tnt_location.put(tntPrimedUUID, tntPrimed.getLocation());
        }, 0, 1);
        tnt_explode_task.put(tntPrimedUUID, task);
        player.sendMessage(ChatColor.RED + "creeper主动名字忘记了 " + ChatColor.RED + "HP " + ChatColor.GREEN + "+7");
        ScoreboardsAndTeams.setScore(player, "energy", 0);
        player.setLevel(0);
        autoEnergyAccumulation(player, 7, 20);
    }

    //undead_knight
    public static HashMapDouble<UUID> undead_knight_damageTaken = new HashMapDouble<>();
    static {
        undead_knight_damageTaken.setDefaultValue(null);
    }
    public static void undead_knight_active_skill(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 160, 3, true, true));
        undead_knight_damageTaken.put(player.getUniqueId(), 0.0);
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.runTaskLater(MegaWallsClasses.getPlugin(), () -> {
//            skeleton_horse_undead_knight.remove(skeletonHorse.getUniqueId());
//            BukkitTask task = tasks.remove(skeletonHorse.getUniqueId());
//            if (task != null) {
//                task.cancel();
//            }
//            skeletonHorse.remove();
            undead_knight_damageTaken.put(player.getUniqueId(), null);
        }, 160L);
        /*
        World world = player.getWorld();
        SkeletonHorse skeletonHorse = (SkeletonHorse) world.spawnEntity(player.getLocation(), EntityType.SKELETON_HORSE);
        skeleton_horse_undead_knight.put(skeletonHorse.getUniqueId(), player.getUniqueId());
//        skeletonHorse.setRearing(false);
        skeletonHorse.setOwner(player);
        skeletonHorse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
        skeletonHorse.setJumpStrength(1.0);
        skeletonHorse.setVelocity(player.getVelocity().multiply(2));
        skeletonHorse.setPassenger(player); //addPassenger do the same thing
        skeletonHorse.setMaxHealth(40); //original 14
        skeletonHorse.setCustomName(player.getName() + "'s Skeleton Horse");
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask(MegaWallsClasses.getPlugin(), () -> {
            skeleton_horse_undead_knight.remove(skeletonHorse.getUniqueId());
            BukkitTask task = tasks.remove(skeletonHorse.getUniqueId());
            if (task != null) {
                task.cancel();
            }
            skeletonHorse.remove();
        }, 160L);
        BukkitTask task = scheduler.runTaskTimer(MegaWallsClasses.getPlugin(), () -> {
            skeletonHorse.setRearing(false);
            Vector vector = skeletonHorse.getLocation().getDirection();
            vector.setY(0);
            vector.normalize();
            double degree = 45;
            degree = toRadians(degree);
            double horse_degree = toRadians(-skeletonHorse.getLocation().getYaw());
            double degree_left = horse_degree + degree;
            double degree_right = horse_degree - degree;
            Vector vector_left = new Vector(sin(degree_left),0,cos(degree_left));
            Vector vector_right = new Vector(sin(degree_right),0,cos(degree_right));
            vector_left.multiply(sqrt(2));
            vector_right.multiply(sqrt(2));
            Vector start = skeletonHorse.getLocation().toVector();
            vector.add(start);
            vector_left.add(start);
            vector_right.add(start);
            Vector y = new Vector(0,1,0);
//            Vector x = new Vector(1,0,0);
//            Vector z = new Vector(0,0,1);
            vector.add(y);
            vector_left.add(y);
            vector_right.add(y);
            Location loc = new Location(skeletonHorse.getWorld(), vector.getX(), vector.getY(), vector.getZ());
            skeletonHorseBreakBlock(skeletonHorse, loc);


            Location loc_left = new Location(skeletonHorse.getWorld(), vector_left.getX(), vector_left.getY(), vector_left.getZ());
            skeletonHorseBreakBlock(skeletonHorse, loc_left);

            Location loc_right = new Location(skeletonHorse.getWorld(), vector_right.getX(), vector_right.getY(), vector_right.getZ());
            skeletonHorseBreakBlock(skeletonHorse, loc_right);

         //   /*
            double health = skeletonHorse.getHealth();
            if (!dashed_skeleton_horse.contains(skeletonHorse.getUniqueId())) {
                for (Player p : world.getPlayers()) {
                    if (!isPlayerPlayableEnemy(player, p)) {
                        if (health > 0 ) {
                            if (skeletonHorse.getBoundingBox().overlaps(p.getBoundingBox())) {
                                addHealth(p, -4);  //originally -4
                                player.sendMessage("-6");
                                p.damage(0.00001, skeletonHorse);
                                addHealth(skeletonHorse, -6);
                                health = health - 6 ;
                                skeletonHorse.setVelocity(new Vector(0,0,0));
                                if (!dashed_skeleton_horse.contains(skeletonHorse.getUniqueId())) {
                                    dashed_skeleton_horse.add(skeletonHorse.getUniqueId());
                                    scheduler.scheduleSyncDelayedTask(MegaWallsClasses.getPlugin(), () -> {
                                        dashed_skeleton_horse.remove(skeletonHorse.getUniqueId());
                                    }, 12L);
                                }
                            }
                        }
                    }
                }
            }

            // ./
        },0L , 1L);
        tasks.put(skeletonHorse.getUniqueId(), task);
        */
        player.sendMessage(ChatColor.RED + "死灵主动名字忘记了 " + ChatColor.RED + "HP " + ChatColor.GREEN + "+7");
        ScoreboardsAndTeams.setScore(player, "energy", 0);
        player.setLevel(0);
        autoEnergyAccumulation(player, 1, 20);
    }

    static void skeletonHorseBreakBlock(SkeletonHorse skeletonHorse, Location loc) {
        World world = skeletonHorse.getWorld();
        Block block = loc.getBlock();
        Location block_loc = block.getLocation();
        Location loc_up = loc.clone();
        loc_up.setY(loc.getY()+1);
        Block block_up = loc_up.getBlock();
        if (!block.isPassable() && block_up.isPassable() && skeletonHorse.getHealth() > 0) {  //getType().equals(Material.AIR)
            addHealth(skeletonHorse, -2);
            BlockData block_data = block.getBlockData();
//                world.spawnParticle(Particle.BLOCK, block_loc, 1, block_data);  //broken

//
//            world.playSound(loc, block_data.getSoundGroup().getBreakSound(), 1, 1);  //replaced by trigger effect



//                ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
//                PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.WORLD_EVENT);
//
//                packet.getIntegers().write(0, 2001);
//                packet.getBlockPositionModifier().write(0, new BlockPosition(vector.getBlockX(), vector.getBlockY(), vector.getBlockZ()));
//                packet.getIntegers().write(1, this.getBlockStateId()); // broken
//                packet.getBooleans().write(0, false);
//
//                try {
//                    for (final Entity entity : loc.getWorld().getNearbyEntities(loc, 16, 16, 16))
//                        if (entity instanceof Player viewer)
//                            protocolManager.sendServerPacket(viewer, packet);
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                world


            block.breakNaturally(true);//            block.breakNaturally();
        }
    }

    //dronwking
    public static void drownking_active_skill(Player player) {
        UUID playerUUID = player.getUniqueId();
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60 , 2, true, true));
        player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 60 , 1, true, true));
        int activeSkillTimes = HashMapUtils.hashMapIncrease(playerUUID, drownking_activeSkillTimes);
        BukkitScheduler scheduler = Bukkit.getScheduler();
        scheduler.scheduleSyncDelayedTask(plugin, () -> {
            if (drownking_activeSkillTimes.containsKey(playerUUID)) { //null check
                if (drownking_activeSkillTimes.get(playerUUID) == activeSkillTimes) {
                    drownking_activeSkillTimes.remove(playerUUID);
                }
            }
        }, 120);
        player.sendMessage(ChatColor.RED + "drownking主动名字忘记了 " + ChatColor.RED + "HP " + ChatColor.GREEN + "+7");
        ScoreboardsAndTeams.setScore(player, "energy", 0);
        player.setLevel(0);
        if (GameManager.gameStage >= 0) { // 决战
            autoEnergyAccumulation(player, 1, 20);
        }
    }

    //spider
    public static void spider_active_skill(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 120, 1, true, true));
        player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 60 , 0, true, true));
        float pitch = -abs(player.getLocation().getPitch());
        Location location = player.getLocation();
        location.setPitch(pitch);
        Vector vector = location.getDirection();
        player.setVelocity(vector.multiply(1.75));
        player.sendMessage(ChatColor.RED + "spider主动名字忘记了 " + ChatColor.RED + "HP " + ChatColor.GREEN + "+7");
        ScoreboardsAndTeams.setScore(player, "energy", 0);
        player.setLevel(0);
    }

    //shaman
    public static void shaman_active_skill(Player player) {
        World world = player.getWorld();
        Location location = player.getEyeLocation();
        Vector vector = location.getDirection();
        vector.multiply(0.05);
        Marker marker = (Marker) world.spawnEntity(location, EntityType.MARKER);
        tornado_shaman.put(marker.getUniqueId(), player.getUniqueId());
        player.sendMessage(ChatColor.RED + "shaman主动名字忘记了 " + ChatColor.RED + "HP " + ChatColor.GREEN + "+7");
        ScoreboardsAndTeams.setScore(player, "energy", 0);
        player.setLevel(0);
        BukkitTaskUtils.cancelTask(player.getUniqueId(), player_activeSkillReady);
        tornado_damage(marker);
        AtomicInteger i = new AtomicInteger(1);
        BukkitScheduler scheduler = Bukkit.getScheduler();
        new BukkitRunnable() {
            @Override
            public void run() {
                Location markerLocation = marker.getLocation();
                markerLocation.add(vector);
                marker.teleport(markerLocation);
                markerLocation.getWorld().spawnParticle(Particle.FIREWORK, markerLocation, 1);  //, 0.5, 0.5, 0.5
                if (i.get() % 20 == 0) {
                    tornado_damage(marker);
                }
//                tornado_damage(marker);
                if (i.get() >= 20 * 6) {
                    tornado_shaman.remove(marker.getUniqueId());
                    marker.remove();
                    cancel();
//                    Bukkit.broadcastMessage("cancel");
                }

                i.getAndIncrement();
            }
        }.runTaskTimer(MegaWallsClasses.getPlugin(), 1, 1);



//        scheduler.runTaskTimer(plugin, new Runnable() {
//
//        }, 1, 1);
    }

    static void tornado_damage(Marker marker) {
        Player player = Bukkit.getPlayer(tornado_shaman.get(marker.getUniqueId()));
        World world = marker.getWorld();
        ArrayList<Player> enemies = new ArrayList<>();
        boolean heroism = false;
        for (Player p : world.getPlayers()) {
            if (isPlayer2PlayableEnemy(player, p)) {
                if (p.getLocation().distance(marker.getLocation()) <= 5.5) {
                    Bat bat = (Bat) marker.getWorld().spawnEntity(marker.getLocation(), EntityType.BAT);
                    p.damage(0.000001, bat);
                    bat.remove();
//                    p.damage(0.00001, marker);
                    addHealth(p, -2.5);
                    EnergyAccumulate.addEnergy(player, 7);
                    enemies.add(p);
                    int amount = PassiveSkills.shaman_passive_skill_1_increase(player, null);
                    if (amount == 5) {
                        heroism = true;
                    }
                }
            }
        }
        if (heroism) {
            for (Player p : enemies) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 0, true, true));
            }
        }
//        if (player != null) {
//
//        }
    }

    //snowman
    public static void snowman_active_skill_1(Player player) {

    }

    public static void snowman_active_skill_2(Player player) {//blizzard
//        AtomicBoolean canceled = new AtomicBoolean(false);
        AtomicInteger i = new AtomicInteger();
        UUID playerUUID = player.getUniqueId();
        if (!snowman_blizzardTask.containsKey(playerUUID)) {
            if (!Cooldown.player_passiveSkill1Cooldown.containsKey(player.getUniqueId())) {
                if (getScore(player, "energy") >= 6) {
                    EnergyAccumulate.addEnergy(player, -6);
//                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 60, 0, true, true));
                    addHealth(player, 1);
                    Cooldown.player_passiveSkill1Cooldown.put(player.getUniqueId(), 260);
                    levelEqualsEnergy(player);
                    i.set(-1);
                    BukkitScheduler scheduler = Bukkit.getScheduler();
                    BukkitTask task = scheduler.runTaskTimer(plugin, () -> {
                        boolean canceled = false;
                        i.getAndIncrement();
                        if (i.get() >= 4) { //original 20
                            i.set(0);
                            if (getScore(player, "energy") >= 6) {
                                EnergyAccumulate.addEnergy(player, -6);
                                addHealth(player, 1);
//                            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 60, 0, true, true));
                            }
                            else {
                                BukkitTaskUtils.cancelTask(player, snowman_blizzardTask);
                                canceled = true;
                                player.sendMessage(ChatColor.RED + "blizzard disabled because no energy");
                            }
                        }
                        if (!canceled && PlayerU.isPlayerPlayable(player)) {
                            snowman_generate_blizzard(player);
//                        player.sendMessage("blizz!!");
                        }
//                    player.sendMessage(player.getMetadata("snowmanBlizzard").get(0).toString());
                    },0, 5); //original 1
//                player.setMetadata("snowmanBlizzard", new FixedMetadataValue(plugin, task));
                    snowman_blizzardTask.put(player.getUniqueId(), task);
                    player.sendMessage(ChatColor.GREEN + "blizzard activated");
//                player.sendMessage(player.getMetadata("snowmanBlizzard").get(0).toString());


//                    new BukkitRunnable() {
//                        @Override
//                        public void run() {
//                            cancel();
//                        }
//                    }.runTaskLater(plugin, 20);


                }
                else {
                    player.sendMessage("能量不够");
                }
            }
            else {
                player.sendMessage("冷却ing");
            }

        }
        else {
            BukkitTaskUtils.cancelTask(player, snowman_blizzardTask);
//            i.set(0);
//            canceled.set(true);
            player.sendMessage(ChatColor.YELLOW + "blizzard disabled manually");
        }
    }

    static void snowman_generate_blizzard(Player player) {
//        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 50, 0, true, true));
        Location location = player.getLocation();
        for (Player player1 : player.getWorld().getPlayers()) {
            if (isPlayerPlayableEnemy(player, player1)) {
                Location location1 = player1.getLocation();
                if (location1.distance(location) <= 5.5) {
                    player1.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 2, 1, true, true));
                }
            }
        }
        World world = location.getWorld();
        for (double y = location.getY() - 1; y <= location.getY() + 2 ; y++ ) { //never mind, first ++ then <= // why there is an equal
            for (double x = location.getX() - 2; x <= location.getX() + 2 ; x++ ) {
                for (double z = location.getZ() - 2; z <= location.getZ() + 2 ; z++ ) {
                    Location location1 = new Location(world, x, y, z);
                    Block block = location1.getBlock();
//                    Material material = block.getType();
                    if (!block.isPassable()) {  //!block.isEmpty() &&
                        boolean turn = false;
                        if (!block_blizzardBlockState.containsKey(block)) { // block wasn't blizzard or destroyed
                            turn = true;
                            BlockState state = block.getState();
                            block_blizzardBlockState.put(block, state);
                        }
                        if (turn) {
                            block.setType(Material.SNOW_BLOCK, false);
                        }
                        if (block.getType() != Material.SNOW_BLOCK) {

                        }
                        /*
                                                if (!block_blizzardBlockState.containsKey(block)) { // block wasn't blizzard or destroyed
                            BlockState state = block.getState();
                            block_blizzardBlockState.put(block, state);
                        }
                        if (block.getType() != Material.SNOW_BLOCK) {
                            block.setType(Material.SNOW_BLOCK, false);
                        }
                         */
                        int blizzardTimes = HashMapUtils.hashMapIncrease(block, block_blizzardTimes);
                        BukkitScheduler scheduler = Bukkit.getScheduler();
                        scheduler.scheduleSyncDelayedTask(plugin, () -> {
//                        Block newBlock = location1.getBlock();
                            if ( block_blizzardTimes.get(block) == blizzardTimes) {
                                block_blizzardTimes.remove(block);
                                BlockState blockState = block_blizzardBlockState.remove(block);
                                if (blockState != null) {
                                    blockState.update(true);
                                }
                            }
//                            if ( block_blizzardTimes.get(block) == blizzardTimes) {   //block.getType().equals(Material.SNOW_BLOCK) &&
//                                state.update(true); // update the block by this state
////                                block.setType(material, false);
//                            }
                        }, 200);
                    }
                }
            }
        }
    }

    //mole
    static HashMap<Location, BlockState> block_moleBlockState = new HashMap<>();
    static HashMapInteger<Block> block_moleTimes = new HashMapInteger<>();
    public static BukkitTaskMap<UUID> mole_dig = new BukkitTaskMap<>();
    public static HaxhMap<UUID, UUID> mole_diggedPlayers = new HaxhMap<>();
    public static void mole_active_skill(Player player) {
        UUID playerUUID = player.getUniqueId();
//        Vector direction = player.getLocation().getDirection();
//        direction.multiply(0.75);
        BukkitScheduler scheduler = Bukkit.getScheduler();
//        player.setGravity(false);
//        player.setFrictionState(TriState.FALSE);
        BukkitTask bukkitTask = scheduler.runTaskTimer(MegaWallsClasses.getPlugin(), ()-> {
            World world = player.getLocation().getWorld();
            Vector direction = player.getLocation().getDirection();
            direction.multiply(0.75);
            player.setVelocity(direction);
            Location location = player.getLocation();
            for (Player otherPlayer : player.getWorld().getPlayers()) {
                UUID otherPlayerUUID = otherPlayer.getUniqueId();
                if (PlayerU.isPlayerPlayableEnemy(player, otherPlayer) && (mole_diggedPlayers.getValues(playerUUID) == null || !mole_diggedPlayers.getValues(playerUUID).contains(otherPlayerUUID))) {
                    Location otherLocation = otherPlayer.getLocation();

                    BoundingBox digBoundingBox = new BoundingBox(location.getX() -2, location.getY()-1, location.getZ()-2, location.getX()+2, location.getY()+2, location.getZ() + 2);
                    BoundingBox otherPlayerBoundingBox = otherPlayer.getBoundingBox();
                    if (digBoundingBox.overlaps(otherPlayerBoundingBox)) {
                        otherPlayer.damage(0.0000000001, player);
                        addHealth(otherPlayer, -8);
                        mole_diggedPlayers.put(playerUUID, otherPlayerUUID);
                    }
//                    // use bounding box overlaps
//                    if (isInBounds(otherLocation.getX(), otherLocation.getY(), otherLocation.getZ(), location.getX() -2, location.getY()-1, location.getZ()-2, location.getX()+2, location.getY()+2, location.getZ() + 2)) {
//                        otherPlayer.damage(0.0000000001, player);
//                        addHealth(otherPlayer, -8);
//                        mole_diggedPlayers.put(playerUUID, otherPlayerUUID);
//                    }
                }
            }
            for (double y = location.getY() - 1; y <= location.getY() + 2 ; y++ ) {
                for (double x = location.getX() - 2; x <= location.getX() + 2 ; x++ ) {
                    for (double z = location.getZ() - 2; z <= location.getZ() + 2 ; z++ ) {
                        Location location1 = new Location(world, x, y, z);
                        Block block = location1.getBlock();
//                    Material material = block.getType();
                        if (!(block.isEmpty() || block.isLiquid())) {  //!block.isEmpty() &&
                            BlockState state = block.getState();
                            block_moleBlockState.put(block.getLocation(), state);
                            block.setType(Material.AIR, false);
//                            if (!block_moleBlockState.containsKey(block)) { // block wasn't digged
//                                BlockState state = block.getState();
//                                block_moleBlockState.put(block, state);
//                            }
//                            if (block.getType() != Material.AIR) {
//                                block.setType(Material.AIR, false);
//                            }
//                            int moleTimes = block_moleTimes.increment(block) + 1;
////                            BukkitScheduler scheduler = Bukkit.getScheduler();
//                            scheduler.scheduleSyncDelayedTask(plugin, () -> {
////                        Block newBlock = location1.getBlock();
//                                if ( block_moleTimes.get(block) == moleTimes) {
//                                    block_moleTimes.put(block, 0);
//                                    BlockState blockState = block_moleBlockState.remove(block);
//                                    if (blockState != null) {
//                                        blockState.update(true);
//                                    }
//                                }
////                            if ( block_blizzardTimes.get(block) == blizzardTimes) {   //block.getType().equals(Material.SNOW_BLOCK) &&
////                                state.update(true); // update the block by this state
//////                                block.setType(material, false);
////                            }
//                            }, 200);
                        }
                    }
                }
            }
        },0, 1);
        mole_dig.put(playerUUID, bukkitTask);
        scheduler.runTaskLater(MegaWallsClasses.getPlugin(), ()-> {
            mole_dig.remove(playerUUID);
            mole_diggedPlayers.removeValues(playerUUID);
//            player.setGravity(true);
//            player.setFrictionState(TriState.NOT_SET);
//            player.setVelocity(new Vector(0, 0, 0));
        }, 10);  //original 5
        player.sendMessage(ChatColor.RED + "mole主动名字忘记了 " + ChatColor.RED + "HP " + ChatColor.GREEN + "+7");
        addEnergy(player, -100);
//        player.damage(1, );
    }

    static void squid_active_skill(Player player) {
        player.sendMessage(ChatColor.RED + "squid主动名字忘记了 " + ChatColor.RED + "HP " + ChatColor.GREEN + "+7");
//        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 140, 0));
        ArrayList<Player> affected_players = herobrineGetNearbyPlayers(player, 5, 114514);
        if (!affected_players.isEmpty()) {
            for (Player affectedPlayer : affected_players) {
                addHealth(affectedPlayer, -3);
                addHealth(player, 2.1);
//            addHealth(player, 2.1);  //3x0.7
                affectedPlayer.damage(0.0000001, player);
                affectedPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 60, 2));  //true, true
                affectedPlayer.setVelocity(player.getLocation().toVector().subtract(affectedPlayer.getLocation().toVector()).multiply(0.4));  //normalize  //0.2
            }
            addEnergy(player, -100);
        }
        else {
            player.sendMessage(ChatColor.YELLOW + "5格内 " + ChatColor.RED + "没有敌人");
        }

    }

    //skeleton_lord
    public static void skeleton_lord_active_skill_1(Player player, int number) {
        for (int i = 0; i < number; i++) {
            skeleton_lord_create_skeleton(player);
        }
        getWorld("world").playSound(player, Sound.ENTITY_ZOMBIE_HURT, 1, 1);
        player.sendMessage(ChatColor.RED + "治疗之环 " + ChatColor.RED + "HP " + ChatColor.GREEN + "+7");
        ScoreboardsAndTeams.setScore(player, "energy", 0);
        player.setLevel(0);
        autoEnergyAccumulation(player, 1, 20);
    }

    public static void skeleton_lord_active_skill_2(Player player) {
//         MyListener.addHealth(player, -20);
         UUID playerUniqueId = player.getUniqueId();
        for (UUID uuid : skeleton_skeleton_lord.keySet()) {
            if (playerUniqueId.equals(skeleton_skeleton_lord.get(uuid))) {
                Entity entity = Bukkit.getEntity(uuid);
                BukkitTask task = tasks.remove(uuid);
                if (task != null) {
                    task.cancel();
                }
//                skeleton_skeleton_lord.remove(uuid);
                if (entity != null) {
                    entity.remove();
                }
            }
        }
        skeleton_skeleton_lord.clear();
        skeleton_lord_create_skeleton_general(player);
        getWorld("world").playSound(player, Sound.ENTITY_ZOMBIE_HURT, 1, 1);
        player.sendMessage(ChatColor.RED + "治疗之环 " + ChatColor.RED + "HP " + ChatColor.GREEN + "+7");
        ScoreboardsAndTeams.setScore(player, "energy", 0);
        player.setLevel(0);
        autoEnergyAccumulation(player, 1, 20);
    }

    static HashMap<UUID, BukkitTask> skeleton_pathFindTask = new HashMap<>();

    static void skeleton_lord_create_skeleton(Player player) {
        World world = player.getWorld();
        LivingEntity entity = (LivingEntity) world.spawnEntity(player.getLocation(), EntityType.SKELETON);
        Skeleton skeleton = (Skeleton) entity;
        skeleton_skeleton_lord.put(entity.getUniqueId(), player.getUniqueId());
        entity.setMaxHealth(40);
        entity.setHealth(40);
        entity.setCustomName(player.getName() + "'s Skeleton");
//        entity.setCustomNameVisible(true);
        entity.getEquipment().setHelmet(setUnbreakable(MegaWallsClasses.setColor(new ItemStack(Material.LEATHER_HELMET), Color.WHITE)));
        entity.getEquipment().setChestplate(setUnbreakable(MegaWallsClasses.setColor(new ItemStack(Material.LEATHER_CHESTPLATE), Color.WHITE)));
        entity.getEquipment().setLeggings(setUnbreakable(MegaWallsClasses.setColor(new ItemStack(Material.LEATHER_LEGGINGS), Color.WHITE)));
        entity.getEquipment().setBoots(setUnbreakable(MegaWallsClasses.setColor(new ItemStack(Material.LEATHER_BOOTS), Color.WHITE)));
        ItemStack stone_sword = new ItemStack(Material.STONE_SWORD);
        stone_sword.addEnchantment(Enchantment.KNOCKBACK, 2);
        stone_sword = setUnbreakable(stone_sword);
        entity.getEquipment().setItemInMainHand(stone_sword);
        entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
        getPlayerTeam(player).addEntry(String.valueOf(entity));
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        BukkitTask task = scheduler.runTaskTimer(MegaWallsClasses.getPlugin(), () -> {
            MyListener.addHealth(entity, 1);
        }, 20L, 20);
        tasks.put(entity.getUniqueId(), task);
        task = scheduler.runTaskTimer(MegaWallsClasses.getPlugin(), () -> {
            if (skeleton_lord_player.containsKey(player.getUniqueId())) {
                Player marked = Bukkit.getPlayer(skeleton_lord_player.get(player.getUniqueId()));
                skeleton.setTarget(marked);
            }
            else {
                skeleton.getPathfinder().moveTo(player);
                skeleton.setTarget(null);
            }

        }, 0, 1);
        skeleton_pathFindTask.put(player.getUniqueId(), task);
    }

    static void skeleton_lord_create_skeleton_general(Player player) {
        World world = player.getWorld();
        LivingEntity entity = (LivingEntity) world.spawnEntity(player.getLocation(), EntityType.SKELETON);
        skeleton_general_skeleton_lord.put(entity.getUniqueId(), player.getUniqueId());
        entity.setCustomName(player.getName() + "'s Skeleton General");
        entity.setMaxHealth(60);
        entity.setHealth(60);
        ItemStack diamond_helmet = new ItemStack(Material.DIAMOND_HELMET);
        diamond_helmet.addEnchantment(Enchantment.PROTECTION, 1);
        diamond_helmet = setUnbreakable(diamond_helmet);
        ItemStack diamond_chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
        diamond_chestplate.addEnchantment(Enchantment.PROTECTION, 1);
        diamond_chestplate = setUnbreakable(diamond_chestplate);
        ItemStack diamond_leggings = new ItemStack(Material.DIAMOND_LEGGINGS);
        diamond_leggings.addEnchantment(Enchantment.PROTECTION, 1);
        diamond_leggings = setUnbreakable(diamond_leggings);
        ItemStack diamond_boots = new ItemStack(Material.DIAMOND_BOOTS);
        diamond_boots.addEnchantment(Enchantment.PROTECTION, 1);
        diamond_boots = setUnbreakable(diamond_boots);
        ItemStack netherite_sword = new ItemStack(Material.NETHERITE_SWORD);
        netherite_sword.addEnchantment(Enchantment.UNBREAKING, 3);
        netherite_sword = setUnbreakable(netherite_sword);
        entity.getEquipment().setItemInMainHand(netherite_sword);
        entity.getEquipment().setHelmet(setUnbreakable(diamond_helmet));
        entity.getEquipment().setChestplate(setUnbreakable(diamond_chestplate));
        entity.getEquipment().setLeggings(setUnbreakable(diamond_leggings));
        entity.getEquipment().setBoots(setUnbreakable(diamond_boots));
        getPlayerTeam(player).addEntry(String.valueOf(entity));
    }
}
