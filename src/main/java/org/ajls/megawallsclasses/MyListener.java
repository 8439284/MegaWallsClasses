package org.ajls.megawallsclasses;

import com.destroystokyo.paper.event.entity.EntityKnockbackByEntityEvent;
import org.ajls.lib.advanced.HashMapInteger;
import org.ajls.lib.utils.ItemStackU;
import org.ajls.lib.utils.PlayerU;
import org.ajls.lib.utils.ScoreboardU;
import org.ajls.megawallsclasses.commands.Energetic;
import org.ajls.megawallsclasses.commands.PlayerUtils;
import org.ajls.megawallsclasses.container.WardenDarknessTargetTimestamp;
import org.ajls.megawallsclasses.nmsmodify.SnowGolemShoot;
import org.ajls.megawallsclasses.nmsmodify.TamedTeleport;
import org.ajls.megawallsclasses.rating.Rating;
import org.ajls.megawallsclasses.utils.EventU;
import org.ajls.megawallsclasses.utils.PotionU;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.SculkSensor;
import org.bukkit.configuration.Configuration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockReceiveGameEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.util.BlockIterator;
import org.jetbrains.annotations.NotNull;

//import static org.ajls.megawallsclasses.MegaWallsClasses.plugin;
import java.util.*;

//import static org.ajls.megawallsclasses.MegaWallsClasses.plugin;
import static org.ajls.megawallsclasses.ActiveSkills.*;
import static org.ajls.megawallsclasses.ClassU.getClassEnum;
import static org.ajls.megawallsclasses.MegaWallsClasses.*;
import static org.ajls.megawallsclasses.commands.Order.*;
import static org.ajls.megawallsclasses.CustomEventsOld.playerDeathEvent;
import static org.ajls.megawallsclasses.EnergyAccumulate.*;
import static org.ajls.megawallsclasses.FoodAndSaturation.addFoodLevel;
import static org.ajls.megawallsclasses.GameManager.*;
import static org.ajls.megawallsclasses.GamemodeUtils.isPlayerPlayable;
import static org.ajls.megawallsclasses.GamemodeUtils.isPlayerPlayableEnemy;
import static org.ajls.megawallsclasses.InitializeClass.*;
import static org.ajls.megawallsclasses.InventoryManager.*;
import static org.ajls.megawallsclasses.ItemStackModify.*;
import static org.ajls.megawallsclasses.KillsManager.*;
import static org.ajls.megawallsclasses.PassiveSkills.*;
import static org.ajls.megawallsclasses.ScoreboardsAndTeams.*;
import static org.ajls.megawallsclasses.Utils.random;
import static org.ajls.tractorcompass.MyListener.*;
import static org.bukkit.Bukkit.*;

public class MyListener implements Listener {
    HashMap<Arrow, Player> blast_arrows = new HashMap<Arrow, Player>();
    public static HashMap<UUID, BukkitTask> tasks = new HashMap<>();
//    public static HashMap<UUID, Integer> n5ll_invisibility = new HashMap<UUID, Integer>();
    public static HashMapInteger<UUID> n5ll_invisibility = new HashMapInteger<>();
    public static ArrayList<UUID> dread_lord_witherSkulls = new ArrayList<>();
    public static HashMap<UUID, UUID> player_hit_dread_lord = new HashMap<>(); // player can only be hit once
    public static HashMap<UUID, UUID> marker_player = new HashMap<>();// 303 explosion by marker don't damage 303
    public static HashMap<UUID, UUID> tnt_creeper = new HashMap<>(); // find the creeper of the tnt
    public static HashMap<UUID, UUID> skeleton_horse_undead_knight = new HashMap<>();//fing the undead_knight of the skeleton horse
    public static ArrayList<UUID> dashed_skeleton_horse = new ArrayList<>(); // dashed skeleton horse
    public static HashMap<UUID, Double> spider_damage = new HashMap<>(); //damage of a spider
    public static HashMap<UUID, UUID> trident_drownking = new HashMap<>();
    public static HashMap<UUID, Integer> drownking_activeSkillTimes = new HashMap<>();
    public static HashMap<UUID, UUID> snowGolem_snowman = new HashMap<>();
    public static HashMap<UUID, BukkitTask> snowGolem_shootTask = new HashMap<>();
    public static HashMap<UUID, Integer> elaina_switchHotbarTimes = new HashMap<>();
    public static HashMap<UUID, UUID> skeleton_skeleton_lord = new HashMap<>(); // find the skeleton lord of that skeleton
    public static HashMap<UUID, UUID> skeleton_general_skeleton_lord = new HashMap<>(); // skeleton general
    static HashMap<UUID, UUID> skeleton_lord_player= new HashMap<>(); // find the player that skeleton lord marked
//    HashMap<Player, ArrayList> skeleton_lord_player = new HashMap<>();
    ArrayList<UUID> attacked = new ArrayList<>(); // player attacked by others and invincible
    HashMap<UUID, Location> player_deathLocation = new HashMap<>();
    static HashMap<UUID, BukkitTask> player_activeSkillReady = new HashMap<>();
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
//        player.setCollidable(true);
//        player.setHealth(100);
        Configuration configuration = plugin.getConfig();
        String playerName = player.getName();
        if(!player.hasPlayedBefore()) {
            player.setMaxHealth(40);
            player.getInventory().setItem(0, new ItemStack(Material.CLOCK));
            player.setGameMode(GameMode.SURVIVAL);  //adventure
//            Configuration configuration = plugin.getConfig();
//            String playerName = player.getName();
//            if (configuration.get("custom_inventory_order." + playerName) == null) {
//                saveInventoryOrder(player, createReorderInventory(player));
//            }


//            Location loc = new Location(getWorld("world"), configuration.getInt("locations.loc_join_spawn.x")+0.5, configuration.getInt("locations.loc_join_spawn.y"), configuration.getInt("locations.loc_join_spawn.z")+0.5);
//            player.teleport(loc);
            //just use setworldspawn


//            if (org.ajls.lib.utils.ScoreboardU.getPlayerTeam(player) == null) {
//                ScoreboardU.joinTeam("red_team", player);
//            }
            BukkitScheduler bukkitScheduler = getScheduler();
            bukkitScheduler.runTaskLater(getPlugin(), ()-> {
                player.sendMessage(ChatColor.RED +"右键钟打开菜单" + ChatColor.GREEN + "尽情和好友玩吧");
            }, 100L);
        }
        if (configuration.get("custom_inventory_order." + playerName) == null) {
            saveInventoryOrder(player, createReorderInventory(player));
        }
        tryCreateReorderInventorySection(player, "hay_block");
        tryCreateReorderInventorySection(player, "squid_potion_everyone_1");
//        if (org.ajls.lib.utils.ScoreboardU.getPlayerTeam(player) == null) {
//            ScoreboardU.joinTeam("red_team", player);
//        }
//        configuration.set("1", "test");
//        plugin.saveConfig();
        ScoreboardManager scoreboardManager = getScoreboardManager();
        Scoreboard scoreboard = scoreboardManager.getNewScoreboard();
        Objective objective = scoreboardManager.getMainScoreboard().getObjective("class");
        if (!objective.getScore(playerName).isScoreSet()) {
            setScore(player, "class", 1);
        }
        if (!player_finalKills.containsKey(player.getUniqueId())) {
            player_finalKills.put(player.getUniqueId(), 0);
        }
        if (gameStage >= 0) {
            // >=1
            Cooldown.displayCooldown(player);
//            InitializeClass.initializeAutoEnergyAccumulation(player);
            addEnergy(player, 0);
        }
//        if (gameStage >= 0) {
//            InitializeClass.initializeAutoEnergyAccumulation(player);
//        }
//        else if (gameStage == 4) {
//            InitializeClass.initializeDeathMatchAutoEnergyAccumulation(player);
//        }


//        player.setScoreboard(scoreboard);
//        createPlayerScoreboard(player);
//        createPlayerScoreboardSidebar(player);
//        createPlayerScoreboardBelowname(player);
        Rating.tryCreateRating(player);
//        player.setPlayerWeather(WeatherType.DOWNFALL);

    }


    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        BukkitTaskUtils.cancelTask(uuid, Cooldown.player_cooldownTask);
        BukkitTaskUtils.cancelTask(uuid, cancelWhenOfflineTask);
    }

//    @EventHandler
//    public void onPlayerMove(PlayerMoveEvent event) {
//        Player player = event.getPlayer();
//        if ()
//    }
    @EventHandler
    public void onPlayerSwitchHotbar(PlayerItemHeldEvent event) {
//        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
//        scheduler.scheduleSyncDelayedTask(MegaWallsClasses.getPlugin(), () -> {
//
//        }, 1L);
        int index = event.getNewSlot();
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        ItemStack item = event.getPlayer().getInventory().getItem(index);

        if (item != null) {
            Material material = item.getType();
            if (item.getType() == Material.NETHER_STAR) {
//                Player player = event.getPlayer();
                event.setCancelled(true);
//                Bukkit.broadcastMessage("skill activated by netherstar");
                tryActiveSkill(player);
            }
            if (getScore(player, "class") == 15) {
                int times = HashMapUtils.hashMapIncrease(playerUUID, elaina_switchHotbarTimes);
                    if(containsLore(item, "speed_potion") ) {  //&& !Cooldown.player_passiveSkill2Cooldown.containsKey(playerUUID)
                        BukkitScheduler scheduler = getServer().getScheduler();
                        scheduler.runTaskLater(getPlugin(), () -> {
                            if (times == elaina_switchHotbarTimes.get(playerUUID)) {
                                if (containsLore(player.getInventory().getItemInMainHand(), "speed_potion") && !Cooldown.player_passiveSkill2Cooldown.containsKey(playerUUID)) {
                                    elaina_passive_skill_2(player);
                                    Cooldown.player_passiveSkill2Cooldown.put(playerUUID, 500);
                                }
                            }
                        }, 40);
                }
            }

        }
    }

    @EventHandler
    public void onEntityPickupItem(EntityPickupItemEvent event) {
//        Entity entity = event.getEntity();
//        ItemStack itemStack = event.getItem().getItemStack();
//        Material material = itemStack.getType();
//        if (entity instanceof Player) {
//            Player player = (Player) entity;
//            UUID playerUUID = player.getUniqueId();
//            if (material == Material.TRIDENT) {
//                if (ClassU.getClass(player) == 10) {
//                    event.setCancelled(true);
//                    int slotIndex = drownking_tridentThrownSlot.get(playerUUID);
//                    if (slotIndex == -1) {
//                        ItemStack replacedItem = player.getInventory().getItemInOffHand();
//                        player.getInventory().setItemInOffHand(itemStack);
////                        player.getInventory().addItem(replacedItem);
//                        if (replacedItem != null) {
//                            player.getInventory().addItem(replacedItem);
//                        }
//                    }
//                    else {
//                        ItemStack replacedItem = player.getInventory().getItem(slotIndex);
//                        player.getInventory().setItem(slotIndex, itemStack);
//                        if (replacedItem != null) {
//                            player.getInventory().addItem(replacedItem);
//                        }
//                    }
//                }
//            }
//        }
    }

    @EventHandler
    public void onPlayerPickupArrow(PlayerPickupArrowEvent event) {
//        event.getFlyAtPlayer()
        @NotNull AbstractArrow abstractArrow = event.getArrow();
        Player player = event.getPlayer();
        Item item = event.getItem();
        ItemStack itemStack = event.getItem().getItemStack();
        Material material = itemStack.getType();
        UUID playerUUID = player.getUniqueId();
        if (material == Material.TRIDENT) {
            if (ClassU.getClass(player) == 10) {
                event.setCancelled(true);
//                item.remove();
                abstractArrow.remove();
                int slotIndex = drownking_tridentThrownSlot.remove(playerUUID);
                if (slotIndex == -1) {
                    ItemStack replacedItem = player.getInventory().getItemInOffHand();
                    player.getInventory().setItemInOffHand(itemStack);
//                        player.getInventory().addItem(replacedItem);
                    if (replacedItem != null) {
                        player.getInventory().addItem(replacedItem);
                    }
                }
                else {
                    ItemStack replacedItem = player.getInventory().getItem(slotIndex);
                    player.getInventory().setItem(slotIndex, itemStack);
                    if (replacedItem != null) {
                        player.getInventory().addItem(replacedItem);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerAttemptPickupItem(PlayerAttemptPickupItemEvent event) {
//        Bukkit.broadcastMessage("attempt pick up");
    }



    HashMap<UUID, ItemStack> player_mainHandRightClickItem = new HashMap<>();
    HashMap<UUID, ItemStack> player_offHandRightClickItem = new HashMap<>();
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        EquipmentSlot hand = event.getHand();
        Player player = event.getPlayer();
        UUID playerUUID = event.getPlayer().getUniqueId();
        ItemStack itemStack = event.getItem();
        if (hand == EquipmentSlot.HAND) {
            player_mainHandRightClickItem.put(playerUUID, itemStack);
        }
        if (hand == EquipmentSlot.OFF_HAND) {
            player_offHandRightClickItem.put(playerUUID, itemStack);
        }
        if (itemStack != null) {
            if (itemStack.getType().isEdible()) {
                if (ClassU.getClass(player) == 14) {
                    if (containsLore(itemStack, "junk_food")) {
                        if (player.getFoodLevel() >= 20) {
//                player.setFoodLevel(19); // Temporarily reduce the hunger level
//                player.setSaturation(0); // Remove saturation to allow eating
//                player.setFoodLevel(20); // Reset hunger after eating
//                    player.sendMessage("try to eat in full hunger");
                            player.setFoodLevel(19);
//                    player.setSaturation(114514);
//                    player.sendMessage(String.valueOf(player.getSaturation()));
                            BukkitScheduler scheduler = getServer().getScheduler();
                            scheduler.runTaskLater(getPlugin(), new Runnable() {
                                public void run() {
                                    player.setFoodLevel(20);
//                            player.sendMessage(String.valueOf(player.getSaturation()));
                                }
                            }, 0L);
//                    player.setFoodLevel(20);

                        }
                    }
                }
            }
        }

//        if (event.getHand() == null) Bukkit.broadcastMessage(event.toString());
        if (event.getHand() != null) {  //sculk shrieker
            if(event.getHand().name().equals("HAND")) {
//            ItemStack itemInHandStack = player.getInventory().getItemInMainHand();
                Material itemInHand = player.getInventory().getItemInMainHand().getType();
                if (itemInHand != null) {
                    Action action = event.getAction();
                    if ((action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)) { // || action == Action.RIGHT_CLICK_BLOCK
//                    player.sendMessage("right clicked");
                        if (itemInHand == Material.STONE_SWORD || itemInHand == Material.IRON_SWORD || itemInHand == Material.DIAMOND_SWORD || itemInHand == Material.NETHERITE_SWORD ||itemInHand == Material.STICK || containsLore(itemStack, "classSword")){
                            event.setCancelled(true);
                            if (getScore(player, "class") == 15) {
                                elaina_switch_mode(player);
                            }
                            else {
//                            Bukkit.broadcastMessage("skill active by rightclick");
                                tryActiveSkill(player);
                            }
                            //|| action == Action.RIGHT_CLICK_BLOCK) {
                        }
                        else if (itemInHand.toString().contains("SHOVEL")) {
                            if (getScore(player, "class") == 13) {
                                event.setCancelled(true);
                                snowman_active_skill_2(player);
                            }
                        }
                        else if (itemInHand == Material.CLOCK) {
                            if (gameStage == 0) {
                                Inventory inventory = createLobbyMenuInventory(player);
                                player.openInventory(inventory);
                            }
                            else if (gameStage >= 1) {
                                Inventory inventory = createToolKitInventory(player);
                                player.openInventory(inventory);
                            }
                            event.setCancelled(true);
                        }
                        else if (itemInHand == Material.ENDER_CHEST) {
                            player.openInventory(player.getEnderChest());
//                        player.sendMessage("末影箱目前还没做");
                            event.setCancelled(true);
                        }

                        //&& User.isAlive(player)
//                        && (itemInHand == Material.IRON_SWORD || itemInHand == Material.DIAMOND_SWORD))

//            while (player.isBlocking() || player.isHandRaised()) {
//                if (hasEggSprayCharge(player)) {
//                    eggSpray(player);
//                }
//            }
                    }
                    else if (action == Action.LEFT_CLICK_BLOCK || action == Action.LEFT_CLICK_AIR) {

                        if (itemInHand == Material.BOW){
//                        Bukkit.broadcastMessage("left click");
                            event.setCancelled(true);
                            tryActiveSkill(player);
                            //|| action == Action.RIGHT_CLICK_BLOCK) {
                        }
                    }
//                if ((action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)) {
//
//                }
                }
            }
        }



    }
    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (event.getHand().name().equals("HAND")) {
            Player player = event.getPlayer();
            Entity entity = event.getRightClicked();
            ItemStack itemInHand = player.getInventory().getItemInMainHand();
            Material itemInHandMaterial = itemInHand.getType();
            if (entity instanceof Player) {
                Player clickedPlayer =(Player) entity;
                if (isPlayerPlayableEnemy(clickedPlayer, player) && getScore(player, "class") == 28 && getScore(player, "energy") < 100) {
                    if (itemInHandMaterial == Material.IRON_SWORD || itemInHandMaterial == Material.DIAMOND_SWORD || itemInHandMaterial == Material.NETHERITE_SWORD || itemInHandMaterial == Material.STICK) {
//                        player.sendMessage("我知道你是特殊职业可以标记玩家但我不会写代码");
                        skeleton_lord_player.put(player.getUniqueId(), clickedPlayer.getUniqueId());
                        player.sendMessage("你标记了" + clickedPlayer.getName());
                        clickedPlayer.sendMessage("你被" + player.getName() + "标记了");
//                        if (getNearbyPlayers(player, 20 ,1).get(0) != null) {
//                            Player player1 = getNearbyPlayers(player, 20 ,1).get(0);
////                            if (skeleton_lord_player.containsKey(player.getUniqueId())) {
////                                skeleton_lord_player.remove(player.getUniqueId());
////                            }
//
//                        }
//                        else {
//                            player.sendMessage("周围没人");
//                        }
                    }
                }
            }
        }
    }
    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event){
        Player player = event.getPlayer();
        ItemStack itemStack = event.getItem();
        Material material = itemStack.getType();
        if (itemStack != null) {
            if (itemStack.getType().isEdible()) {
//                player.sendMessage("eat edible");
                if (ClassU.getClass(player) == 14) {
                    if (containsLore(itemStack, "junk_food")) {
                        switch (material) {
                            case COOKIE:
                                addEnergy(player, 5);
                                break;
                            case PUMPKIN_PIE:
                                addEnergy(player, 20);
                                break;
                            case BEEF:
                                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 1));
                                break;

                        }
                    }
                }
            }
        }
    }
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        if (player.hasMetadata("ReorderInventory")) {
            player.removeMetadata("ReorderInventory", plugin);
        }
    }
    @EventHandler
    /*
    blast arrows
     */
    public void onEntityShoot(EntityShootBowEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player) {
            Player player = (Player) entity;
            if (getScore(player, "class") == 3 && getScore(player, "energy") >= 100) {
//                addHealth(player, 7);
                Arrow arrow = (Arrow) event.getProjectile();
//                UUID uuid = arrow.getUniqueId();
                blast_arrows.put(arrow, player);
                BukkitScheduler scheduler = getServer().getScheduler();
                BukkitTask task = scheduler.runTaskTimer(getPlugin(), () -> {

                    Particle.DustOptions dustOptions = new Particle.DustOptions(Color.RED, 1.0F);
                    getWorld("world").spawnParticle(Particle.DUST, arrow.getLocation(), 1, dustOptions);

                    // Do something
//                    if (arrow.isOnGround() || arrow.isDead() || arrow.isInBlock()) {;
//                        scheduler.cancelTask(t);
//                    }

                }, 0L, 1L);
                tasks.put(arrow.getUniqueId(), task);
//                autoEnergyAccumulation(player, 1, 20);
                player.playSound(player, Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1, 1);

                player.sendMessage(ChatColor.RED + "爆炸箭");
                setScore(player, "energy", 0);
                player.setLevel(0);
                if (ClassU.isTransformationMaster(player)) {
                    transformation_master_active_skill(player);
                }
                addEnergy(player, 0);
            }
        }
    }

    @EventHandler
    public void onEntityRegainHealth(EntityRegainHealthEvent event) {
        Entity entity = event.getEntity();
        if (wither_team.containsKey(entity.getUniqueId())) {
            event.setCancelled(true);
        }
    }

//    @EventHandler
//    public void onHorseRear(Rear)
    public static HashMap<UUID, Integer> player_whichClassReorder = new HashMap<>();
    public static HashMap<UUID, Integer> player_nextClass = new HashMap<>();
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        InventoryView inventoryView = event.getView();
        Inventory topInventory = inventoryView.getTopInventory();
        Inventory bottomInventory = inventoryView.getBottomInventory();
        String title = event.getView().getTitle();
        if (title.equals("Select Class")) {
            if (event.getClickedInventory() == null) return;
            if (event.getClickedInventory().getType() != InventoryType.CHEST) return;  // and cancel event
            if (event.getCurrentItem() != null) {
                Player player = (Player) event.getWhoClicked();
                UUID playerUUID = player.getUniqueId();
                int index = event.getSlot() + 1;
                if (player_damagers.get(playerUUID) == null || player_damagers.get(playerUUID).isEmpty()) {
                    InitializeClass.resetPlayerCondition(player);
                    ClassU.setClass(player, index);
//                    setScore(player, "class", index);
                    elaina_disable(player);

                    player.sendMessage(Documentation.getClassDocumentation(index));


                    if (!ClassU.isTransformationMaster(player)) {
//                        initializeClass(player);
                        InitializeClass.refreshClassOnChangeClass(player);
                    }
                    else {
                        InitializeClass.transformation_master_initialize_class(player); // init specific and use active skill to change class
                    }
//                    disableAutoEnergyAccumulation(player);
////                    initializeAutoEnergyAccumulation(player);
////                    initializeDeathMatchAutoEnergyAccumulation(player);
//                    addEnergy(player, 0);
//                    Cooldown.displayCooldown(player);

//                    InitializeClass.refreshClass(player);

                    BukkitTask task = player_activeSkillReady.get(player.getUniqueId());
                    if (task != null) {
                        task.cancel();
                    }
                    setScore(player, "energy" , 0);
                    player.setLevel(0);

//                    player.sendMessage(Documentation.getClassDocumentation(index));
                }
                else {
                    player.sendMessage(ChatColor.YELLOW + "脱战15秒后才能重选职业");
                }

//                player_nextClass.put(player.getUniqueId(), index);
//                player.sendMessage(ChatColor.YELLOW + "死亡就能刷新了");

//                switch (event.getCurrentItem().getType()) {
//                    case ROTTEN_FLESH:
//                        MegaWallsClasses.setScore(player, "class", 1);
////                        initializeClass(player);
//                        break;
//                    case DIAMOND_SWORD:
//                        MegaWallsClasses.setScore(player, "class", 2);
////                        initializeClass(player);
//                        break;
//                    case BONE:
//                        MegaWallsClasses.setScore(player, "class", 3);
////                        initializeClass(player);
//                        break;
//                    case ENDER_PEARL:
//                        MegaWallsClasses.setScore(player, "class", 4);
////                        initializeClass(player);
//                        break;
//                    case LIGHT_GRAY_STAINED_GLASS_PANE:
//                        MegaWallsClasses.setScore(player, "class", 5);
////                        initializeClass(player);
//                        break;
//                    case WITHER_SKELETON_SKULL:
//                        MegaWallsClasses.setScore(player, "class", 6);
////                        initializeClass(player);
//                        break;
//                    case TOTEM_OF_UNDYING:
//                        MegaWallsClasses.setScore(player, "class", 7);
////                        initializeClass(player);
//                        break;
//                    case GUNPOWDER:
//                        MegaWallsClasses.setScore(player, "class", 8);
//                        break;
//                    case STONE_SWORD:
//                        MegaWallsClasses.setScore(player, "class", 9);
//                        break;
//                    case TRIDENT:
//                        MegaWallsClasses.setScore(player, "class", 10);
//                        break;
//                    case STRING:
//                        MegaWallsClasses.setScore(player, "class", 11);
//                        break;
//                    case WIND_CHARGE:
//                        ScoreboardsAndTeams.setScore(player, "class", 12);
//                        break;
//                    case SNOW_BLOCK:
//                        ScoreboardsAndTeams.setScore(player, "class", 13);
//                        break;
//                    case DEBUG_STICK:
//                        ScoreboardsAndTeams.setScore(player, "class", 15);
//                        break;
//                    case SKELETON_SKULL:
//                        MegaWallsClasses.setScore(player, "class", 28);
////                        initializeClass(player);
//                        break;
//                    //default: player.sendMessage(ChatColor.RED + "NOT ENOUGH ITEMS");
//                }




//                refreshClass(player);
            }
//            player.sendMessage("test");
            event.setCancelled(true);
        }
        else if (title.equals("Reorder inventory")) {
            if (event.getCurrentItem() != null) {
                if (!event.getCurrentItem().getType().equals(Material.AIR)) {
                    Player player = (Player) event.getWhoClicked();
                    Inventory inventory = event.getClickedInventory();
//                    InventoryView inventoryView = player.getOpenInventory();
//                    Inventory topInventory = inventoryView.getTopInventory();
//                    inventoryView.getTitle()
                    switch (event.getCurrentItem().getType()) {
                        case GREEN_CONCRETE:
                            saveInventoryOrder(player, topInventory);
                            event.setCancelled(true);
                            player.closeInventory();
                            player.sendMessage(ChatColor.GREEN + "物品栏成功保存");
                            break;
                        case YELLOW_CONCRETE:
                            saveInventoryOrder(player, createReorderInventory(player));
                            event.setCancelled(true);
                            player.closeInventory();
                            player.sendMessage(ChatColor.YELLOW + "物品栏成功重置到默认");
                            break;
                    }
                    if (inventory.equals(player.getInventory())) {
                        event.setCancelled(true);
                        player.sendMessage("是重新排列新物品栏内物品，而不是将物品放到自己物品栏");
                    }
                }
            }
        }
        else if (title.equals("ClassReorderInventory")) {
            if (event.getClickedInventory() == null) return;
            if (event.getClickedInventory().getType() != InventoryType.CHEST) return;
            if (event.getCurrentItem() != null) {
                if (!event.getCurrentItem().getType().equals(Material.AIR)) {
                    Player player = (Player) event.getWhoClicked();
//                    int index = (int) player.getMetadata("WhichClassReorder").getFirst();
                    int index = player_whichClassReorder.get(player.getUniqueId());
                    Inventory inventory = event.getClickedInventory();

                    switch (event.getCurrentItem().getType()) {
                        case GREEN_CONCRETE:
                            saveClassInventoryOrder(player, topInventory, index);
                            event.setCancelled(true);
                            player.openInventory(createWhichClassReorderInventory(player));
                            player.sendMessage(ChatColor.GREEN + "职业物品栏成功保存");
                            break;
                        case YELLOW_CONCRETE:
//                            saveClassInventoryOrder(player, createClassReorderInventory(player, index), index);
                            resetClassReorderInventory(player, index);
                            event.setCancelled(true);
                            player.openInventory(createWhichClassReorderInventory(player));
                            player.sendMessage(ChatColor.YELLOW + "职业物品栏成功重置到默认");
                            break;
                    }
                    if (inventory.equals(player.getInventory())) {
                        event.setCancelled(true);
                        player.sendMessage("是重新排列新物品栏内物品，而不是将物品放到自己物品栏");
                    }
                }
            }
        }
        else if (title.equals("WhichClassReorder")) {
            if (event.getCurrentItem() != null) {
                if (!event.getCurrentItem().getType().equals(Material.AIR)) {
                    Player player = (Player) event.getWhoClicked();
                    int index = event.getSlot() + 1;
//                    player.setMetadata("WhichClassReorder", new FixedMetadataValue(plugin, index));
                    player_whichClassReorder.put(player.getUniqueId(), index);
                    player.openInventory(loadClassReorderInventory(player, index));
                }
            }
        }
        else if (title.equals("TeamSelection")) {
            if (event.getClickedInventory() != null) {
                if (event.getClickedInventory().getType() == InventoryType.CHEST) {
                    if (event.getCurrentItem() != null) {
                        if (!event.getCurrentItem().getType().equals(Material.AIR)) {
                            Player player = (Player) event.getWhoClicked();
                            int index = event.getSlot();
                            String teamName = null;
                            switch (index) {
                                case 0:
//                            ScoreboardU.joinTeam("red_team", player);
                                    teamName = "red_team";
                                    break;
                                case 1:
                                    teamName = "blue_team";
                                    break;
                                case 2:
                                    teamName = null;
                                    break;
                            }
//                    String teamName = switch (index) {
//                        case 0 ->
////                            ScoreboardU.joinTeam("red_team", player);
//                                "red_team";
//                        case 1 -> "blue_team";
//                        default -> null;
//                    };
                            if (teamName != null) {
                                ScoreboardU.joinTeam(teamName, player);
                                player.closeInventory();
                                player.sendMessage(teamName);
                            }
                            else {
                                ScoreboardU.leaveTeam(player);
                                player.closeInventory();
                                player.sendMessage("各自为战");
                            }


                        }
                    }
                }
            }
            event.setCancelled(true);


        }
        else if(title.equals("MW Menu")) {
//            if (event.getCurrentItem() != null) {
//                if (!event.getCurrentItem().getType().equals(null)) {
//                    if (!event.getCurrentItem().getType().equals(Material.AIR)) {
//                        Bukkit.broadcastMessage("hi");
//                    }
//                }
//            }
            if (event.getClickedInventory() != null) {
                if (event.getClickedInventory().getType() == InventoryType.CHEST) {
                    if (event.getCurrentItem() != null) {
                        Player player = (Player) event.getWhoClicked();
                        int index = event.getSlot();
                        switch (index) {
                            case 0:
                                player.openInventory(createClassSelectionInventory(player));
                                break;
                            case 1:
//                        player.openInventory(createReorderInventory(player));
                                player.openInventory(loadReorderInventoryFromConfig(player));
                                player.setMetadata("ReorderInventory", new FixedMetadataValue(plugin, true));
                                break;
                            case 2:
                                player.openInventory(createWhichClassReorderInventory(player));
                                break;
                            case 3:
                                player.openInventory(createTeamSelectionInventory(player));
                                break;
                            case 4:
                                player.openInventory(createDocumentsInventory(player));
                                break;
                        }
                        switch (event.getCurrentItem().getType()) {
                            case IRON_SWORD:
                                player.openInventory(createClassSelectionInventory(player));
                                break;
                            case CHEST:
//                        player.openInventory(createReorderInventory(player));
                                player.openInventory(loadReorderInventoryFromConfig(player));
                                player.setMetadata("ReorderInventory", new FixedMetadataValue(plugin, true));
                                break;
                            case TRAPPED_CHEST:
                                player.openInventory(createWhichClassReorderInventory(player));
                                break;
                        }
                    }
                }
            }
            event.setCancelled(true);
        }
        else if(title.equals("MW Tool Kit")) {
            event.getCurrentItem();
            if (event.getCurrentItem() != null) {
                Player player = (Player) event.getWhoClicked();
                switch (event.getCurrentItem().getType()) {
                    case DIAMOND_SWORD:
                        showEveryTeamFinalKill(player);
                        player.closeInventory();
                        break;
                }
            }
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryMoveItem(InventoryMoveItemEvent event) {
        InventoryHolder inventoryHolder = event.getSource().getHolder();
        if (inventoryHolder instanceof Villager) {
            Villager villager = (Villager) inventoryHolder;
            UUID uuid = villager.getUniqueId();
            if (ReorderInventory.contains(uuid)) {
                broadcastMessage("hi");
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onStrengthTest(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        Entity damagerEntity = event.getDamager();
        if (damagerEntity instanceof LivingEntity) {
            LivingEntity damagerLivingEntity = (LivingEntity) damagerEntity;
            if (damagerLivingEntity.hasPotionEffect(PotionEffectType.STRENGTH)) {
                int multiplier = 2;
                for (int i = 0; i < damagerLivingEntity.getPotionEffect(PotionEffectType.STRENGTH).getAmplifier(); i++) {
                    multiplier *= 2;
                }
                event.setDamage(event.getDamage() * multiplier);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDamagePlayer(EntityDamageByEntityEvent event) {
        if (event.getDamage() <= 0.1) return; //damage that causes kb effect
        double finalDamage = org.ajls.lib.utils.EventU.getFinalDamage(event);// event.getFinalDamage();
//        Bukkit.broadcastMessage("damage delt: " + finalDamage);
        Entity entity = event.getEntity();
        Entity damagerEntity = event.getDamager();
        if (damagerEntity instanceof Player) {
            Player damager = (Player) damagerEntity;

            ItemStack itemStack = damager.getInventory().getItemInMainHand();
            Material material = itemStack.getType();
            if (material == Material.BOW) {
                event.setCancelled(true);
                if (getScore(damager, "class") == 28 && getScore(damager, "energy") < 100) {
                    if (entity instanceof Player) {
                        Player player = (Player) entity;
                        if (isPlayerPlayableEnemy(damager, player)) {
                            skeleton_lord_player.put(damager.getUniqueId(), player.getUniqueId());
                            damager.sendMessage("你标记了" + player.getName());
                            player.sendMessage("你被" + damager.getName() + "标记了");
                        }
                    }
                }
                tryActiveSkill(damager);
            }
        }
        if (event.getEntity() instanceof Player && !event.isCancelled()) {
            Player player = (Player) entity;
            UUID playerUUID = player.getUniqueId();
            if (event.getDamager() instanceof Player) {
                Player damager = (Player) event.getDamager();
                UUID damagerUUID = damager.getUniqueId();
//                if (damager.hasPotionEffect(PotionEffectType.STRENGTH)) {
//                    int multiplier = 2;
//                    for (int i = 0; i < damager.getPotionEffect(PotionEffectType.STRENGTH).getAmplifier(); i++) {
//                        multiplier *= 2;
//                    }
//                    event.setDamage(event.getDamage() * multiplier);
//                    finalDamage = org.ajls.lib.utils.EventU.getFinalDamage(event);
//                }

//                Player player = (Player) event.getEntity();
                registerPlayerHit(player, damager);
                Rating.loser_winnerMap.addWeight(player, damager, finalDamage);
                if (!attacked.contains(player.getUniqueId())) {
                    attacked.add(player.getUniqueId());
                    BukkitScheduler scheduler = getServer().getScheduler();
                    scheduler.scheduleSyncDelayedTask(getPlugin(), () -> {
                        attacked.remove(player.getUniqueId());
                        // Do something
                    }, 10L);
                    attackEnergyAccumulate(damager);
                    attackedEnergyAccumulate(player);
                    if (getScore(damager, "class") == 15) {
                        if (elaina_mode.containsKey(damagerUUID)) {
                            if (elaina_mode.get(damagerUUID) == 0) {
                                elaina_enemy.put(damagerUUID, playerUUID);
                            }
                        }
                    }
                    if (getScore(player, "class") == 15) {
                        if (elaina_mode.containsKey(playerUUID)) {
                            if (elaina_mode.get(playerUUID) == 2) {
                                elaina_enemy.put(playerUUID, damagerUUID);
                            }
                        }
                    }
                    switch (getScore(damager, "class")) {
                        case 2:
                            herobrine_passive_skill_1(damager);
                            break;
                        case 5:
                            null_passive_skill_1_increase(damager);
                            break;
                        case 6:
                            if (Utils.random(1, 5) == 1) {
                               addFoodLevel(damager, 3);  //soul eater
                               addHealth(damager, 2);
                            }
                            break;
                        case 12:
                            shaman_passive_skill_1_increase(damager, player);
//                            PassiveSkills.shaman_passive_skill_2_increase(damager, player);
                            break;
                        case 15:
                            break;
                        case 28:
                            skeleton_lord_passive_skill_2(damager, player);
                            break;
                        case 30:
                            HashSet<WardenDarknessTargetTimestamp> targetTimestamps = warden_darknessTargetTimestamp.getValues(damagerUUID, true);
                            for (WardenDarknessTargetTimestamp targetTimestamp : targetTimestamps) {
                                if (targetTimestamp.getTargetUUID().equals(playerUUID)) {
//                                    targetTimestamp.setTargetUUID(playerUUID);
                                    targetTimestamp.setTimestamp(targetTimestamp.getTimestamp() - 20);
                                    break;
                                }
                            }
                            break;
                    }
                    switch (getScore(player, "class")) {
//                        case 2:
//                            herobrine_passive_skill_1(damager);
//                            break;
//                        case 5:
//                            null_passive_skill_1(damager);
//                            break;
                        case 12:
//                            PassiveSkills.shaman_passive_skill_1_increase(damager, player);
                            shaman_passive_skill_2_increase(damager, player);
                            break;
//                        case 15:
//                            break;
//                        case 28:
//                            skeleton_lord_passive_skill_2(damager, player);
//                            break;
                    }

//                    switch (MegaWallsClasses.getScore(player, "class")) {
//                        case 1:
//                            MegaWallsClasses.addScore(player, "energy", 1);
//                            break;
//                        case 5:
//                            for (Player online : Bukkit.getOnlinePlayers()) {
////                                online.showPlayer(player);
//                            ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
//                            PacketContainer hide_armor_packet = protocolManager.createPacket(PacketType.Play.Server.ENTITY_EQUIPMENT);
//                            hide_armor_packet.getIntegers().write(0, player.getEntityId());
//                            List<Pair<EnumWrappers.ItemSlot, ItemStack>> list = new ArrayList<>();
//                                list.add(new Pair<>(EnumWrappers.ItemSlot.HEAD, player.getInventory().getHelmet()));
//                                list.add(new Pair<>(EnumWrappers.ItemSlot.CHEST, player.getInventory().getChestplate()));
//                                list.add(new Pair<>(EnumWrappers.ItemSlot.LEGS, player.getInventory().getLeggings()));
//                                list.add(new Pair<>(EnumWrappers.ItemSlot.FEET, player.getInventory().getBoots()));
//                                list.add(new Pair<>(EnumWrappers.ItemSlot.MAINHAND, player.getInventory().getItemInMainHand()));
//                                hide_armor_packet.getSlotStackPairLists().write(0, list);
//                                try {
//                                    protocolManager.sendServerPacket(online, hide_armor_packet);
//                                } catch (InvocationTargetException e) {
//                                    throw new RuntimeException(e);
//                                }
//                            }
//                    }


                }
            }
            else if (event.getDamager() instanceof Arrow) {
                if (event.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE)) {
//                    Player player = (Player) entity;
                    Arrow arrow = (Arrow) event.getDamager();
                    ProjectileSource projectileSource =arrow.getShooter();
                    BukkitScheduler scheduler = getServer().getScheduler();
                    scheduler.scheduleSyncDelayedTask(plugin, () -> {
                        player.setArrowsInBody(0);
                    }, 0L);
                    if (projectileSource instanceof Player) {
                        Player damager = (Player) projectileSource;
                        UUID damagerUUID = damager.getUniqueId();
//                        Player player = (Player) event.getEntity();
                        registerPlayerHit(player, damager);
                        if (!attacked.contains(player.getUniqueId())) {
                            attacked.add(player.getUniqueId());
//                            BukkitScheduler scheduler = getServer().getScheduler();
                            scheduler.scheduleSyncDelayedTask(getPlugin(), () -> {
                                attacked.remove(player.getUniqueId());
                                // Do something
                            }, 10L);
                            shootEnergyAccumulate(damager);
                            attackedEnergyAccumulate(player);
                            if (getScore(damager, "class") == 15) {
                                if (elaina_mode.containsKey(damagerUUID)) {
                                    if (elaina_mode.get(damagerUUID) == 0) {
                                        elaina_enemy.put(damagerUUID, playerUUID);
                                    }
                                }
                            }
                            if (getScore(player, "class") == 15) {
                                if (elaina_mode.containsKey(playerUUID)) {
                                    if (elaina_mode.get(playerUUID) == 2) {
                                        elaina_enemy.put(playerUUID, damagerUUID);
                                    }
                                }
                            }
                            switch (getScore(damager, "class")) {
                                case 2:
                                    herobrine_passive_skill_1(damager);
                                    break;
                                case 3:
                                    damager.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 120, 0, true, true));
                                    damager.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 120, 1, true, true));
                                    if (random(1, 10) <= 9) {
                                        damager.getInventory().addItem(new ItemStack(Material.ARROW, 4));
//                                        int foodLevel = damager.getFoodLevel();
//                                        foodLevel += 1;
//                                        if (foodLevel > 20) foodLevel = 20;
//                                        damager.setFoodLevel(foodLevel);
                                        PlayerU.addFoodLevel(damager, 1);
                                    }
                                case 5:
                                    null_passive_skill_1_increase(damager);
                                    break;
                                case 12:
                                    shaman_passive_skill_1_increase(damager, player);
//                                    PassiveSkills.shaman_passive_skill_2_increase(damager, player);
                                    break;
                                case 15:
                                    break;
                                case 28:
                                    skeleton_lord_passive_skill_2(damager, player);
                                    break;
                            }
                            switch (getScore(player, "class")) {
//                        case 2:
//                            herobrine_passive_skill_1(damager);
//                            break;
//                        case 5:
//                            null_passive_skill_1(damager);
//                            break;
                                case 1:
                                    zombie_passive_skill_2(player);
                                    break;
                                case 12:
//                            PassiveSkills.shaman_passive_skill_1_increase(damager, player);
                                    shaman_passive_skill_2_increase(damager, player);
                                    break;
//                        case 15:
//                            break;
//                        case 28:
//                            skeleton_lord_passive_skill_2(damager, player);
//                            break;
                            }
//                            switch (MegaWallsClasses.getScore(player, "class")) {
//                                case 1:
//                                    MegaWallsClasses.addScore(player, "energy", 1);
//                                    break;
//                                case 5:
//                                    for (Player online : Bukkit.getOnlinePlayers()) {
////                                online.showPlayer(player);
//                                        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
//                                        PacketContainer hide_armor_packet = protocolManager.createPacket(PacketType.Play.Server.ENTITY_EQUIPMENT);
//                                        hide_armor_packet.getIntegers().write(0, player.getEntityId());
//                                        List<Pair<EnumWrappers.ItemSlot, ItemStack>> list = new ArrayList<>();
//                                        list.add(new Pair<>(EnumWrappers.ItemSlot.HEAD, player.getInventory().getHelmet()));
//                                        list.add(new Pair<>(EnumWrappers.ItemSlot.CHEST, player.getInventory().getChestplate()));
//                                        list.add(new Pair<>(EnumWrappers.ItemSlot.LEGS, player.getInventory().getLeggings()));
//                                        list.add(new Pair<>(EnumWrappers.ItemSlot.FEET, player.getInventory().getBoots()));
//                                        list.add(new Pair<>(EnumWrappers.ItemSlot.MAINHAND, player.getInventory().getItemInMainHand()));
//                                        hide_armor_packet.getSlotStackPairLists().write(0, list);
//                                        try {
//                                            protocolManager.sendServerPacket(online, hide_armor_packet);
//                                        } catch (InvocationTargetException e) {
//                                            throw new RuntimeException(e);
//                                        }
//                                    }
//                                    break;
//
//                            }

//                            testSkillReady(player);
                        }
                        if (getScore(damager, "class") == 9) {
                            if (!Cooldown.player_passiveSkill1Cooldown.containsKey(damager.getUniqueId())) {
                                player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 100, 2, true, true));
                                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 100 , 0, true, true));
//                            player.setMetadata("PassiveSkill1", new FixedMetadataValue(plugin, MegaWallsClasses.time));
                                Cooldown.player_passiveSkill1Cooldown.put(damager.getUniqueId(), 170);
                            }
                            else {
                                damager.sendMessage(String.valueOf(Cooldown.player_passiveSkill1Cooldown.get(damager.getUniqueId())));
                            }
                        }
                        else if (getClassEnum(damager) == ClassEnum.ENTITY_303) {  // not sure counts if someone hit the enemy first
                            if (random(1, 100) <= 35) {
                                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 30, 2, true, true));
                                damager.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 50, 2, true, true));
                            }
                        }
                    }
                }
                else if (event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)) {
                    Arrow arrow = (Arrow) event.getDamager();
                    Player damager = (Player) arrow.getShooter();
//                    Player player = (Player) event.getEntity();
//                    String teamName = getPlayerTeam(damager).getName();
//                    DamageSource damageSource = new DamageSource() {
//                        @Override
//                        public DamageType getDamageType() {
//                            return DamageType.GENERIC_KILL;
//                        }
//
//                        @Override
//                        public Entity getCausingEntity() {
//                            return damager;
//                        }
//
//                        @Override
//                        public Entity getDirectEntity() {
//                            return arrow;
//                        }
//
//                        @Override
//                        public Location getDamageLocation() {
//                            return arrow.getLocation();
//                        }
//
//                        @Override
//                        public Location getSourceLocation() {
//                            return arrow.getLocation();
//                        }
//
//                        @Override
//                        public boolean isIndirect() {
//                            return true;
//                        }
//
//                        @Override
//                        public float getFoodExhaustion() {
//                            return 114514;
//                        }
//
//                        @Override
//                        public boolean scalesWithDifficulty() {
//                            return false;
//                        }
//                    };
                    event.setDamage(0.0000000000000000000000000000000000000000000007006492321625); //0.0000000000000000000000000000000000000000000007006492321625
//                    if (!getPlayerTeam(player).getName().equals(teamName)) {
//                        player.damage(111, );
                    addHealth(player, -6);
                    attackedEnergyAccumulate(player);
                    player.sendMessage(ChatColor.RED + "!爆炸箭! " + ChatColor.RED + "HP " + ChatColor.YELLOW + "-6");

//                        switch (MegaWallsClasses.getScore(player, "class")) {
//                            case 1:
//                                MegaWallsClasses.addScore(player, "energy", 1);
//                                break;
//                            case 5:
//                                for (Player online : Bukkit.getOnlinePlayers()) {
////                                online.showPlayer(player);
//                                    ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
//                                    PacketContainer hide_armor_packet = protocolManager.createPacket(PacketType.Play.Server.ENTITY_EQUIPMENT);
//                                    hide_armor_packet.getIntegers().write(0, player.getEntityId());
//                                    List<Pair<EnumWrappers.ItemSlot, ItemStack>> list = new ArrayList<>();
//                                    list.add(new Pair<>(EnumWrappers.ItemSlot.HEAD, player.getInventory().getHelmet()));
//                                    list.add(new Pair<>(EnumWrappers.ItemSlot.CHEST, player.getInventory().getChestplate()));
//                                    list.add(new Pair<>(EnumWrappers.ItemSlot.LEGS, player.getInventory().getLeggings()));
//                                    list.add(new Pair<>(EnumWrappers.ItemSlot.FEET, player.getInventory().getBoots()));
//                                    list.add(new Pair<>(EnumWrappers.ItemSlot.MAINHAND, player.getInventory().getItemInMainHand()));
//                                    hide_armor_packet.getSlotStackPairLists().write(0, list);
//                                    try {
//                                        protocolManager.sendServerPacket(online, hide_armor_packet);
//                                    } catch (InvocationTargetException e) {
//                                        throw new RuntimeException(e);
//                                    }
//                                }
//                                break;

//                        }
//                        testSkillReady(player);
//                    }
                }
            }
            else if (event.getDamager() instanceof Skeleton) {
                if (event.getEntity() instanceof Player) {
                    Skeleton skeleton = (Skeleton) event.getDamager();
                    if (skeleton_skeleton_lord.containsKey(skeleton.getUniqueId())) {
                        if (random(1,100) <= 8) {
                            Player skeleton_lord = getPlayer(skeleton_skeleton_lord.get(skeleton.getUniqueId()));
                            addHealth(skeleton_lord, 3);
                            skeleton_lord.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 5 * 20, 1, false, false));  // original 5
                            skeleton_lord.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE));
                        }
                    }
                }
            }
            else if (damagerEntity instanceof TNTPrimed) {
                TNTPrimed tntPrimed = (TNTPrimed) damagerEntity;
                UUID tntPrimedUUID = tntPrimed.getUniqueId();
                if (tnt_creeper.containsKey(tntPrimedUUID)) {
                    event.setDamage(0.00001);
//                    if (!isPlayerPlayableEnemy(player, getPlayer(tnt_creeper.get(tntPrimedUUID)))) {
////                        event.setDamage(0.00001);
//                    }
                    if (isPlayerPlayableEnemy(player, getPlayer(tnt_creeper.get(tntPrimedUUID)))) {
                        if (tntPrimed.hasMetadata("onPeople")) {
                            addHealth(player, -4);
                            player.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, 100, 0, false, false));
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 80, 1, false, false));
                        }
                        else {
//                            event.setDamage(2);
                            addHealth(player, -2);
                            player.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, 50, 0, false, false));
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 40, 1, false, false));
                        }
                    }
                }
            }
            else if(damagerEntity instanceof Trident) {
                Trident trident = (Trident) damagerEntity;
                UUID tridentUUID = trident.getUniqueId();
                if (trident_drownking.containsKey(tridentUUID)) {
                    UUID damagerUUID = trident_drownking.get(tridentUUID);
                    Player damager = (Player) trident.getShooter(); //getPlayer(playerUUID);
                    registerPlayerHit(player, damager);
                    if (drownking_activeSkillTimes.containsKey(damagerUUID)) {
//                        activeSkill(damager);
                        double playerHealth = player.getHealth();
                        playerHealth = playerHealth - event.getFinalDamage();  // use EventU to get final health
                        addHealth(player, (player.getMaxHealth() - playerHealth) * -0.38);
                        World world = player.getWorld();
                        world.strikeLightningEffect(player.getLocation());
//                        HashMapUtils.hashMapIncrease(damagerUUID, drownking_activeSkillTimes); // prevent reset when skill is already used // don't need to because the next time active skill will add times thus remove function won't work
                        drownking_activeSkillTimes.remove(damagerUUID);
                        addEnergy(damager, 30);
                        broadcastMessage(player.getName() + " was impaled by " + damager.getName() + " damage: " + (player.getMaxHealth() - playerHealth) * -0.38);
                    }
                    else if(getScore(damager, "energy") >= 100) {  // && ScoreboardsAndTeams.getScore(damager, "class") == 10) useless because checked when trident_dk contains key
                        activeSkill(damager);
                        //                        activeSkill(damager);
                        double playerHealth = player.getHealth();
                        playerHealth = playerHealth - event.getFinalDamage();
                        addHealth(player, (player.getMaxHealth() - playerHealth) * -0.38);
                        World world = player.getWorld();
                        world.strikeLightningEffect(player.getLocation());
//                        HashMapUtils.hashMapIncrease(damagerUUID, drownking_activeSkillTimes); // prevent reset when skill is already used // don't need to because the next time active skill will add times thus remove function won't work
                        drownking_activeSkillTimes.remove(damagerUUID);
                        addEnergy(damager, 30);
                        broadcastMessage(player.getName() + " was impaled by " + damager.getName() + " damage: " + (player.getMaxHealth() - playerHealth) * -0.38);
                    }
                    shootEnergyAccumulate(damager);
                }
            }
        }
        else if (entity instanceof Wither) {
            Wither wither = (Wither) entity;
//            Entity damagerEntity = event.getDamager();
            if (damagerEntity instanceof Player) {
                Player damager = (Player) damagerEntity;
                if (getPlayerTeamName(damager).equals(wither_team.get(wither.getUniqueId()))) {
                    damager.sendMessage("这是你家凋灵");
                    event.setCancelled(true);
                }
            }
        }
        else if (entity instanceof Skeleton) {
            Skeleton skeleton = (Skeleton) entity;
            UUID skeletonUUID = skeleton.getUniqueId();
            if (damagerEntity instanceof Player) {
                Player damager = (Player) damagerEntity;
                UUID playerUUID = damager.getUniqueId();
                if (skeleton_skeleton_lord.get(skeletonUUID) == playerUUID) {
                    event.setCancelled(true);
                    damager.sendMessage("别打自己的骷髅小兵");
                }
                else if (skeleton_general_skeleton_lord.get(skeletonUUID) == playerUUID) {
                    event.setCancelled(true);
                    damager.sendMessage("别打自己的骷髅将军");
                }
            }
        }
    }


    @EventHandler
    /*
    dread lord wither skull
    disable entity_303 only break block explosion from damaging entities
     */
    public void onEntityDamagedByEntity(EntityDamageByEntityEvent event) {
//        Bukkit.broadcastMessage("damage");
        Entity entity = event.getEntity();
        Entity damager_entity = event.getDamager();
        if (entity instanceof Player) {
            Player player = (Player) entity;
            if(event.getDamager() instanceof WitherSkull) {
                WitherSkull witherSkull = (WitherSkull) event.getDamager();
                if (dread_lord_witherSkulls.contains(witherSkull.getUniqueId())) {
//                    EntityDamageEvent.DamageCause damageCause = event.getCause();
//                    event.setCancelled(true);
                    if (event.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE)) {
//                        if (event.getEntity() instanceof Player) {
//                            event.setCancelled(true);
                            if (!player.equals(witherSkull.getShooter())) {
                                if (!player_hit_dread_lord.containsKey(player.getUniqueId())) {
//                                    dread_lord_witherSkulls.remove(witherSkull.getUniqueId());
                                    Player damager = (Player) witherSkull.getShooter();
                                    event.setDamage(0.0000000000000000000000000000000000000000000007006492321625);
//                                    player.damage(0.0000000000000000000000000000000000000000000007006492321625, witherSkull);
                                    if (damager.hasPotionEffect(PotionEffectType.STRENGTH)) {
                                        addHealth(player, -16);
                                    }
                                    else {
                                        addHealth(player, -8);
                                    }
                                    attackedEnergyAccumulate(player);
                                    player_hit_dread_lord.put(player.getUniqueId(), damager.getUniqueId());
                                    BukkitScheduler scheduler = getServer().getScheduler();
                                    scheduler.scheduleSyncDelayedTask(getPlugin(), () -> {
                                        player_hit_dread_lord.remove(player.getUniqueId()); //bug:can't be hit by other dreadlord //solve method:create a global int refer to the times skills activated, and store the number with the skull. when hit transfer that number to the player, and if the player has same number cancel hit and let skull fly pass the player
                                    }, 2L);
//                                    addHealth(player, -8);
                                    player.sendMessage("你被死亡加载打中了");
                                }
//                            }
//                            else {
//                                event.setCancelled(true);
//                            }
                            }
//                        else{
//                            //set damage 8 but don't display message
//                        }
                    }
                    else if (event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)) {
                        //event.setDamage(0.0000000000000000000000000000000000000000000007006492321625);  //making dread lord explosion do damage
//                        event.setCancelled(true);
                    }
//                    else {
//                        EntityDamageEvent.DamageCause cause = event.getCause();
//                    }
                } // remove skull from map after a delay , disable other skulls knockback by the skull explosion
                else if (witherSkulls.contains(witherSkull.getUniqueId())) {
                    Wither wither = (Wither) witherSkull.getShooter();
                    String teamName = wither_team.get(wither.getUniqueId());
                    if (!getPlayerTeamName(player).equals(teamName)) {
                        if (event.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE)) {
                            player.sendMessage("你被凋零打中了");
                            addFoodLevel(player, -2);
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 20, 1, false, false));  // amplifier still don't know first 3 then 1
                        }
                        else if (event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)) {
                            player.sendMessage("你被凋零炸中了");
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 20, 1, false, false));
                        }
                    }
                }
            }
            else if (damager_entity instanceof Player) {
                if (event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)) {
                    Player damager = (Player) damager_entity;   // 303
                    if (getScore(damager, "class") == 7) {
                        event.setCancelled(true);
                    }
                    else if (getScore(damager, "class") == 11) {
                        event.setDamage(0.00001);
                        UUID damagerUUID = damager.getUniqueId();
                        double spiderDamage = spider_damage.get(damagerUUID);
//                        BukkitScheduler scheduler = getServer().getScheduler();  //disabled because i want to disable
//                        scheduler.runTaskLater(getPlugin(), ()-> {
//                            spider_damage.remove(damagerUUID);
//                        }, 0);
                        if (spiderDamage > 8) {
                            spiderDamage = 8;
                        }
                        addHealth(player, -(spiderDamage * 2));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 100, 1, true, true));
                    }
                }
            }
            else if (damager_entity instanceof Marker) {
                Marker marker = (Marker) damager_entity;
                if (marker_player.containsKey(marker.getUniqueId())) {
                    UUID uuid = marker_player.get(marker.getUniqueId());
                    attackedEnergyAccumulate(player);
//                    BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
//                    scheduler.scheduleSyncDelayedTask(MegaWallsClasses.getPlugin(), () -> {
//                        marker_player.remove(marker.getUniqueId());
//                    }, 1L);
                    if (player.getUniqueId().equals(uuid)) {
                        event.setDamage(0.0000000000000000000000000000000000000000000007006492321625);
                    }
                }
            }
            if (getScore(player, "class") == 5) {
                null_passive_skill_disable(player);
            }
            else if (getScore(player, "class") == 15) {
                elaina_passive_skill_2_disable(player);
            }
            else if (getClassEnum(player) == ClassEnum.ZOMBIE) {
                zombie_passive_skill_1(player);
            }
        }
//        if (entity instanceof WitherSkull) {
//            WitherSkull witherSkull = (WitherSkull) event.getEntity();
//            if (dread_lord_witherSkulls.contains(witherSkull.getUniqueId())) {
//                event.setCancelled(true);
//            }
//        }
    }
    
    @EventHandler
    public void onEntityKnockbackByEntity(EntityKnockbackByEntityEvent event) {
//        Bukkit.broadcastMessage(String.valueOf(event.getKnockback().length()));
//        Bukkit.broadcastMessage(String.valueOf(event.getKnockback().getX()));
//        Bukkit.broadcastMessage(String.valueOf(event.getKnockback().getY()));
//        Bukkit.broadcastMessage(String.valueOf(event.getKnockback().getZ()));
        Entity knockbackEntity = event.getHitBy();
        LivingEntity LivingEntity = event.getEntity();
        if (LivingEntity instanceof WitherSkull witherSkull) {
            if (dread_lord_witherSkulls.contains(witherSkull.getUniqueId())) {
                event.setCancelled(true);
                Bukkit.broadcastMessage("dre skull kbed by " + knockbackEntity.getName());
            }
        }
    }


    public static HashSet<UUID> tfSpider = new HashSet<>();
    @EventHandler(priority = EventPriority.MONITOR)  //prevent executing before player damaged by player event thus causing register death then register hit  // lowest runs first monitor runs last  //turned high to monitor because cactusgenerator need information to process event
    //detailed explanation:  first go to cacgen if not cancelled(preventing other cancelled events going, then if player will die cancel the event
    public void onEntityDamaged(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        LivingEntity livingEntity = null;
        if (entity instanceof LivingEntity) {
            livingEntity = (LivingEntity) entity;
        }
//        double finalDamage = event.getFinalDamage();
//        broadcastMessage("damage delt : " + finalDamage);
//        broadcastMessage("player health: " + livingEntity.getHealth());
////        livingEntity.setLastDamage(livingEntity.getLastDamage()+0.00001);
////        livingEntity.setLastDamage(event.getFinalDamage());
//        broadcastMessage("last damage  : " + livingEntity.getLastDamage());

//            event.setDamage(0);
//            player.setLastDamage(player.getLastDamage());
        
        if (entity instanceof Player) {
            Player player = (Player) entity;
//            double playerHealthBefore = player.getHealth();
//            double playerHealthAfter = playerHealthBefore - event.getFinalDamage();
            double playerHealthBefore = player.getHealth();
            double playerHealthAfter = EventU.getFinalHealth(event);
            double damage =event.getDamage();



            if (( getScore(player, "class") == 11 || tfSpider.contains(player.getUniqueId()) ) && event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                World world = player.getWorld();
                Location location = player.getLocation();
                if (event.getFinalDamage() >= 4) {
                    spider_damage.put(player.getUniqueId(), event.getFinalDamage());
                    world.createExplosion(location, 3f, false, true, player);
                }
                else {
                    player.sendMessage(event.getFinalDamage() + " less than 4 no explosion");
                }
            }
            if (ClassU.getClass(player) == 5) {
                if (playerHealthAfter < 16) {
                    if (!nullTelepathy_tasks.contains(player.getUniqueId())) {
                        null_passive_skill_2(player);
                    }
                }
                else {
                    // this should be in player regain health section
                }
            }
            if (ClassU.getClass(player) == 18) {
                if (playerHealthAfter < 18) {
                    squid_passive_skill_2(player);
                }
            }
            else if (getClassEnum(player) == ClassEnum.UNDEAD_KNIGHT) {
                if (undead_knight_damageTaken.containsKey(player.getUniqueId())) {
                    if (undead_knight_damageTaken.add(player.getUniqueId(), event.getDamage(), true) >= 14) {
                        undead_knight_damageTaken.put(player.getUniqueId(), null);
                        player.getAttribute(Attribute.GENERIC_STEP_HEIGHT).setBaseValue(0.6);  //GENERIC_STEP_HEIGHT  STEP_HEIGHT
//                        ArrayList<PotionEffect> removedPotionEffects = new ArrayList<>();
                        for (PotionEffect potionEffect : player.getActivePotionEffects()) {
                            if (potionEffect.getType().equals(PotionEffectType.SPEED)) {
                                if (potionEffect.getAmplifier() == 3) {
                                    player.removePotionEffect(potionEffect.getType());
                                }
                            }
                        }
//                        for (PotionEffect potionEffect : removedPotionEffects) {
//                            player.getActivePotionEffects().remove(potionEffect);
//                        }
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 160, 0, false, false));
//                        player.removeP

                    };
                }
            }
            if (playerHealthAfter == 0) { //player.getHealth() <= event.getFinalDamage()  //player will die
                if (!event.isCancelled()) {
                    event.setCancelled(true);
//                    event.setDamage(0);
//                    Bukkit.getServer().getPluginManager().callEvent(new EntityDamageEvent(event.getEntity(), event.getCause(), event.getDamageSource(), event.getDamage()));
//                    new EntityDamageEvent()
                }


                playerDeathEvent(player);
            }
        }
    }

    HashMap<UUID, Integer> drownking_tridentThrownSlot = new HashMap<>();
    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        Projectile projectile = event.getEntity();
        ProjectileSource projectileSource = projectile.getShooter();
        if (projectile instanceof WitherSkull) {
            WitherSkull witherSkull = (WitherSkull) projectile;
            UUID witherSkullUUID = witherSkull.getUniqueId();
            if (projectileSource instanceof Wither) {
                Wither wither = (Wither) projectileSource;
                UUID witherUUID = wither.getUniqueId();
                if (wither_team.containsKey(witherUUID)) { // wither tries to fire skulls that wasn't created by me && !witherSkulls.contains(witherSkullUUID)
                    event.setCancelled(true);
                }
            }
        }
        else if( projectile instanceof Trident) {
            Trident trident = (Trident) projectile;
            UUID tridentUUID = trident.getUniqueId();
            if (projectileSource instanceof Player) {
                Player player = (Player) projectileSource;
                UUID playerUUID = player.getUniqueId();
                if (player.getInventory().getItemInOffHand().getType() == Material.TRIDENT) {
                    drownking_tridentThrownSlot.put(player.getUniqueId(), -1);
                }
                if (player.getInventory().getItemInMainHand().getType() == Material.TRIDENT) {
                    drownking_tridentThrownSlot.put(player.getUniqueId(), player.getInventory().getHeldItemSlot());
                }
//                if (player_offHandRightClickItem.get(playerUUID) != null) {
//                    if (player_offHandRightClickItem.get(playerUUID).getType() == Material.TRIDENT) {
//                        drownking_tridentThrownSlot.put(player.getUniqueId(), -1);
//                    }
//                }
//                if (player_mainHandRightClickItem.get(playerUUID) != null) {
//                    if (player_mainHandRightClickItem.get(playerUUID).getType() == Material.TRIDENT) {
//                        drownking_tridentThrownSlot.put(player.getUniqueId(), player.getInventory().getHeldItemSlot());
//                    }
//                }
                if (getScore(player, "class") == 10) {
                    trident_drownking.put(tridentUUID, player.getUniqueId());

                }
            }
        }
//        else if(projectile instanceof Snowball) {
//            Snowball snowball = (Snowball) projectile;
//            Bukkit.broadcastMessage(String.valueOf(snowball.getVelocity().length()));
//            if (projectileSource instanceof Snowman) {
//                Snowman snowman = (Snowman) projectileSource;
//                if (snowGolem_snowman.containsKey(snowman.getUniqueId())) {
//                    Bukkit.broadcastMessage(String.valueOf(snowball.getVelocity().length()));
//                    event.setCancelled(true);
//                }
//            }
//        }
    }
    @EventHandler
    /*
    listen for blast arrow
    disable dread lord break blocks
    */
    public void onProjectileHit(ProjectileHitEvent event) {
//        event.setCancelled(true);
        if (event.getEntity() instanceof Arrow ) {
//            event.setCancelled(true);
            Arrow arrow = (Arrow) event.getEntity();
            if (blast_arrows.get(arrow) != null) {
                blast_arrow(arrow);
//                blast_arrows.remove(arrow);
            }
        }
        else if(event.getEntity() instanceof WitherSkull) {
            WitherSkull witherSkull = (WitherSkull) event.getEntity();
            UUID witherSkullUUID = witherSkull.getUniqueId();
            BukkitScheduler scheduler = getServer().getScheduler();
            if (dread_lord_witherSkulls.contains(witherSkullUUID)) {
                scheduler.scheduleSyncDelayedTask(getPlugin(), () ->{
                    dread_lord_witherSkulls.remove(witherSkullUUID);
                    //event getHitEntity can see whether fly pass or damage
                }, 2L);
            }
            else if (witherSkulls.contains(witherSkullUUID)) {
                scheduler.scheduleSyncDelayedTask(getPlugin(), () ->{
                    witherSkulls.remove(witherSkullUUID);
                }, 2L);
            }
        }
        else if (event.getEntity() instanceof Trident) {
            Trident trident = (Trident) event.getEntity();
            UUID tridentUUID = trident.getUniqueId();
            if (trident_drownking.containsKey(tridentUUID)) {
                BukkitScheduler scheduler = getServer().getScheduler();
                scheduler.scheduleSyncDelayedTask(getPlugin(), () ->{
                    trident_drownking.remove(tridentUUID);
                }, 2L);  // try 0l
            }
        }
        else if (event.getEntity() instanceof Snowball) {
            Snowball snowball = (Snowball) event.getEntity();
            ProjectileSource projectileSource = snowball.getShooter();
            Entity hitEntity = event.getHitEntity();
            if (hitEntity instanceof Player) {
                Player hitplayer = (Player) hitEntity;
                if (projectileSource instanceof Snowman) {
                    Snowman snowman = (Snowman) projectileSource;
                    UUID SnowmanUUID = snowman.getUniqueId();
                    if (snowGolem_snowman.containsKey(SnowmanUUID)) {
                        Player player = getPlayer(snowGolem_snowman.get(SnowmanUUID));
                        if (GamemodeUtils.isPlayer2PlayableEnemy(player, hitplayer)) {
                            hitplayer.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 20, 1, true, true));
                            Bat bat = (Bat) snowball.getWorld().spawnEntity(snowball.getLocation(), EntityType.BAT);
                            hitplayer.damage(0.000001, bat);

                            bat.remove();
                            if (isPlayerPlayable(player)) {
                                addEnergy(player, 4);
                            }
                        }
                    }
                }
            }

        }
    }
//    @EventHandler
//    public void onEntityKnockback(EntityKnockbackEvent event) {
//        Entity entity = event.getEntity();
//        Bukkit.broadcastMessage("kb");
//        if (dread_lord_witherSkulls.contains(entity.getUniqueId())) {
//            event.setCancelled(true);
//        }
//    }
    @EventHandler
    /*
    disable dread lord wither_skull break blocks
     */
    public void onExplode(EntityExplodeEvent event) {
        Entity entity = event.getEntity();
        if(event.getEntity() instanceof WitherSkull) {
            WitherSkull witherSkull = (WitherSkull) event.getEntity();
            UUID witherSkullUUID = witherSkull.getUniqueId();
            if (dread_lord_witherSkulls.contains(witherSkullUUID)) {
//                event.blockList().clear(); //注释掉因为ec恐惧可以破坏方块了
            }
            else if (witherSkulls.contains(witherSkullUUID)) {
                event.blockList().clear();
            }
        }
        else if (event.getEntity() instanceof TNTPrimed) {
            TNTPrimed tntPrimed = (TNTPrimed) entity;
            UUID tntPrimedUUID = tntPrimed.getUniqueId();
            if (tnt_creeper.containsKey(tntPrimedUUID)) {
                for (Block block: event.blockList()) {
                    blockRegenWhenDisabled.put(block.getLocation(), block.getState());
                }
            }
        }
    }

    @EventHandler
    public void onEntityDismount(EntityDismountEvent event) {
        Entity dismounted = event.getDismounted();  // horse etc
        Entity entity = event.getEntity();  // player etc
        UUID dismountedUUID = dismounted.getUniqueId();
        if (elaina_slime.containsValue(dismountedUUID)) {
            if (entity instanceof Player) {
                Player player = (Player) entity;
                elaina_passive_skill_2_disable(player);
            }
            else {
                broadcastMessage("为什么有其他实体那里骑史莱姆");
            }
        }
        else if (skeleton_horse_undead_knight.containsKey(dismounted.getUniqueId())) {
            Player player = getPlayer(skeleton_horse_undead_knight.get(dismounted.getUniqueId()));
            dismounted.remove();
//            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 160, 0, false, false));
            skeleton_horse_undead_knight.remove(dismounted.getUniqueId());
            BukkitTask task = tasks.remove(dismounted.getUniqueId());
            if(task != null) {
                task.cancel();
            }
        }
    }

//    @EventHandler
//    public void onEntityCollision(VehicleEntityCollisionEvent event) {
//        Vehicle vehicle = event.getVehicle();
//        Entity entity = event.getEntity();
//        if (skeleton_horse_undead_knight.containsKey(vehicle.getUniqueId())) {
//            SkeletonHorse skeletonHorse = (SkeletonHorse) vehicle;
//            if (!dashed_skeleton_horse.contains(skeletonHorse.getUniqueId())) {
//                if (entity instanceof Player) {
//                    Player player = (Player) entity;
////                    if (skeletonHorse.getBoundingBox().contains(player.getLocation().toVector())) {
//                    addHealth(player, -3);
//                    player.damage(0.00001, skeletonHorse);
//                    addHealth(skeletonHorse, -6);
//                    skeletonHorse.setVelocity(new Vector(0,0,0));
//                    if (!dashed_skeleton_horse.contains(skeletonHorse.getUniqueId())) {
//                        dashed_skeleton_horse.add(skeletonHorse.getUniqueId());
//                        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
//                        scheduler.scheduleSyncDelayedTask(MegaWallsClasses.getPlugin(), () -> {
//                            dashed_skeleton_horse.remove(skeletonHorse.getUniqueId());
//                        }, 30L);
//                    }
////                    }
//                }
//            }
//        }
//    }
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        String teamName = getPlayerTeamName(player);
        registerPlayerDeath(player);
        player_deathLocation.put(player.getUniqueId(), player.getLocation());
//        if (witherDeadTeams.contains(teamName)) {
//            player.setGameMode(GameMode.SPECTATOR);
//        }
        //check if drops have lore classItem and remove
        List<ItemStack> itemStacks = new ArrayList<>(event.getDrops()); // clone the drops item
        for (ItemStack itemStack : itemStacks) {
            if (containsLore(itemStack, "classItem")) {
                event.getDrops().remove(itemStack);
            }
        }
    }
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        String teamName = getPlayerTeamName(player);
        player.setGameMode(GameMode.SPECTATOR);
        event.setRespawnLocation(player_deathLocation.get(player.getUniqueId()));
        if (witherDeadTeams.contains(teamName)) {
            player.sendMessage("Too bad you died so young and early");
        }
        else {
            BukkitScheduler scheduler = getServer().getScheduler();
            player.sendMessage("respawn in 5s");
            scheduler.scheduleSyncDelayedTask(plugin, () -> {
                teamTeleportSpawn(player);
                player.setGameMode(GameMode.SURVIVAL);
                initializeClass(event.getPlayer());
            }, 100L);
        }
    }
    @EventHandler
    public void onEntityTarget(EntityTargetEvent event) {
        Entity entity = event.getEntity();
        UUID entityUUID = entity.getUniqueId();
        if (skeleton_skeleton_lord.containsKey(entity.getUniqueId())) { // skeleton belongs to skeleton lord
            Player skeleton_lord = getPlayer(skeleton_skeleton_lord.get(entity.getUniqueId()));
            Skeleton skeleton = (Skeleton) entity;
            if (skeleton_lord_player.containsKey(skeleton_lord.getUniqueId())) { // skeleton lord haven't marked player
                Player marked = getPlayer(skeleton_lord_player.get(skeleton_lord.getUniqueId()));
                event.setTarget(marked);
//                Bukkit.broadcastMessage(marked.getName());

            }
            else {
                event.setCancelled(true);
                skeleton.getPathfinder().moveTo(skeleton_lord);
//                event.setCancelled(true);
//                Bukkit.broadcastMessage("null");
            }
        }
        if (skeleton_general_skeleton_lord.containsKey(entity.getUniqueId())) {
            Player skeleton_lord = getPlayer(skeleton_general_skeleton_lord.get(entity.getUniqueId()));
            if (skeleton_lord_player.containsKey(skeleton_lord.getUniqueId())) { // skeleton lord haven't marked player
                Player marked = getPlayer(skeleton_lord_player.get(skeleton_lord.getUniqueId()));
                event.setTarget(marked);
//                broadcastMessage(marked.getName());

            }
            else {
                event.setTarget(null);
//                event.setCancelled(true);
//                broadcastMessage("null");
            }
        }
        else if(snowGolem_snowman.containsKey(entityUUID)) {
            event.setCancelled(true);
            Snowman snowman = (Snowman) entity;
            snowman.getPathfinder().findPath(getPlayer(snowGolem_snowman.get(entityUUID)));
        }
//        if (wither_team.containsKey(entityUUID)) {
//            event.setTarget(null);
//            Wither wither = (Wither) event.getEntity();
//            String teamName = wither_team.get(entityUUID);
//            World world = wither.getWorld();
//            Location loc = wither.getLocation();
//            double distance = Double.MAX_VALUE;
//            Player target = null;
//            Location targetLoc = null;
//            for (Player p : world.getPlayers()) {
//                if (isPlayerPlayable(p) && !getPlayerTeamName(p).equals(teamName)) {
//                    Location loc2 = p.getLocation();
//                    double distance2 = loc2.distance(loc);
//                    if (distance2 < distance) {
//                        distance = distance2;
//                        target = p;
//                        targetLoc = loc2;
//                    }
//                }
//            }
//            if (target != null) {
//                event.setTarget(target);
//            }
//        }
//        else {
//            event.setCancelled(true);
//        }
//        event.setTarget();
    }
    @EventHandler
    public void onItemConsume(PlayerItemConsumeEvent event) {
        ItemStack item = event.getItem();
        EquipmentSlot hand = event.getHand();
        if (item.getType().equals(new ItemStack(Material.POTION).getType()) ) {
            PotionMeta potionMeta = (PotionMeta) item.getItemMeta();
            Player player = event.getPlayer();
            int heldItemSlot = player.getInventory().getHeldItemSlot();
            if (hand == EquipmentSlot.OFF_HAND) {
                heldItemSlot = 40;
            }

            ItemStack itemClone = item.clone();
            BukkitScheduler scheduler = getServer().getScheduler();
            scheduler.scheduleSyncDelayedTask(getPlugin(), () -> player.getInventory().remove(Material.GLASS_BOTTLE), 1L);
            if (getLore(item) != null) {
                if (containsLore(item, "heal_potion")) {  //MegaWallsClasses.getLore(item).equals(MegaWallsClasses.getLore(setLore(new ItemStack(Material.POTION), "heal_potion"))
                    event.setCancelled(true);
                    addHealth(player, PotionU.getDuration(item, 0));
//                    player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));

                    ItemStack removedPotion = removeAmount(item, 1);
//                    player.updateInventory();  //doesn't work because the item comes from event not the playaer
                    player.getEquipment().setItem(hand, removedPotion);
//                    player.getInventory().setItemInMainHand(removedPotion);

                }
//                else if (containsLore(item, "speed_potion")) {
//                    event.setCancelled(true);
//                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, PotionU.getDuration(item, 0), PotionU.getAmplifier(item, 0)));
//                    ItemStack removedPotion = ItemStackModify.removeAmount(item, 1);
//                    player.getEquipment().setItem(hand, removedPotion);
//                }
                else if(containsLore(item, "custom_potion")) {
                    event.setCancelled(true);
                    List<PotionEffect> potionEffects = potionMeta.getCustomEffects();
//                    potionEffects.removeFirst();
                    player.addPotionEffects(potionEffects);
//                    for (int i = 0; i < potionMeta.getCustomEffects())
//
//                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, PotionU.getDuration(item, 0), PotionU.getAmplifier(item, 0)));
                    ItemStack removedPotion = removeAmount(item, 1);
                    player.getEquipment().setItem(hand, removedPotion);
                }
                if (ClassU.getClass(player) == 18) {
                    for (Player affectedPlayer: herobrineGetNearbyPlayers(player, 4, 114514)) {
                        affectedPlayer.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 50, 0, false, true));
                        addEnergy(player, 30);
                    }
                }
            }
            if (itemClone.getAmount() == 1) { // consumed and gone
                scheduler.scheduleSyncDelayedTask(getPlugin(), () -> {
                    int size = player.getInventory().getSize();
                    for (int slot = 0; slot < size; slot++) {
//                        if (slot == heldItemSlot) continue;
                        ItemStack is = player.getInventory().getItem(slot);
                        if (is == null) continue;
                        ItemStack isClone = is.clone();
                        isClone.setAmount(1); // if item consumed amount == 1 then replace // wrong so that the item stack with any amount can replace the one consumed
                        if (isClone.equals(itemClone)) {
                            player.getInventory().clear(slot);
//                            player.sendMessage(slot + "");
                            player.getInventory().setItemInMainHand(is);
//                        return;
                            break;
                        }
                    }
                }, 1L);
            }
        }
    }

    @EventHandler
    public void onPotionSplash(PotionSplashEvent event) {
        ThrownPotion thrownPotion = event.getPotion();
        ItemStack potion = thrownPotion.getItem();
        Collection<LivingEntity> affectedEntities = event.getAffectedEntities();
        if (containsLore(potion, "elaina_potion")) {
            for (LivingEntity livingEntity : affectedEntities) {
                event.setIntensity(livingEntity, 1);
            }
        }
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        Entity entity = event.getEntity();
        BukkitScheduler scheduler = getScheduler();
        scheduler.runTaskLater(getPlugin(), () -> {
            if (entity instanceof Snowman) {
//            Snowman snowman = (Snowman) entity;
//            if (!snowGolem_snowman.containsKey(snowman.getUniqueId())) {
//                event.setCancelled(true);
//            }
////            snowman.is
            }
//        else if (entity instanceof IronGolem) {
//            IronGolem ironGolem = (IronGolem) entity;
//            ironGolem.setPlayerCreated(true);
//        }
            else if (entity instanceof Slime) {
                Slime slime = (Slime) entity;
//            slime.setAI(false);

            }
        }, 0);

    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event){
        Entity entity = event.getEntity();
        UUID entityUUID = entity.getUniqueId();
        if (entity instanceof Player) {
            Player player = (Player) entity;
            if (skeleton_lord_player.containsValue(player.getUniqueId())) {
                for (UUID uuid : skeleton_lord_player.keySet()) {
                    if (skeleton_lord_player.get(uuid).equals(player.getUniqueId())) {
                        skeleton_lord_player.remove(uuid);
                    }
                }
            }
        }
        else if (skeleton_skeleton_lord.containsKey(entity.getUniqueId())) {
            skeleton_skeleton_lord.remove(entity.getUniqueId());
            BukkitTask task = tasks.remove(entity.getUniqueId()); // remove from map if exist
            if(task != null) { // task found
                task.cancel();
            }
            BukkitTaskUtils.cancelTask(entity, skeleton_pathFindTask);
        }
        else if (skeleton_general_skeleton_lord.containsKey(entity.getUniqueId())) {
            skeleton_general_skeleton_lord.remove(entity.getUniqueId());
            skeleton_pathFindTask.remove(entityUUID);
        }
        else if (skeleton_horse_undead_knight.containsKey(entity.getUniqueId())) {
            Player player = getPlayer(skeleton_horse_undead_knight.get(entity.getUniqueId()));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 160, 0, false, false));
            skeleton_horse_undead_knight.remove(entity.getUniqueId());
            BukkitTask task = tasks.remove(entity.getUniqueId());
            if(task != null) {
                task.cancel();
            }
        }
        else if (wither_team.containsKey(entityUUID)) {
            String teamName = wither_team.remove(entityUUID);
            witherDeadTeams.add(teamName);
            setJust_not_tracked_team(teamName);
            ArrayList<String> tracked_teams = getTracked_teams();
//            if (tracked_teams.contains(teamName)) {
//                tracked_teams.remove(teamName);
            if (witherDeadTeams.size() >= tracked_teams.size()) {
                tracked_teams.add("blue_team");
                tracked_teams.add("red_team");
//                    gameStage = 4;
                broadcastMessage("决战开始了");
                setNot_tracked_teams(new ArrayList<String>());
                for (Player player : getOnlinePlayers()) {
                    UUID playerUUID = player.getUniqueId();
                    if (getScore(player, "energy") < 100) {
                        initializeDeathMatchAutoEnergyAccumulation(player);
                    }
                }
            }
            else {
                setNot_tracked_teams(new ArrayList<>(witherDeadTeams));
            }
            setTracked_teams(tracked_teams);
//            }
//            switch (teamName) {
//                case "blue_team":
//                    blue_wither_dead = 1;
//                    break;
//                case "red_team":
//                    red_wither_dead = 1;
//                    break;
//            }
            wither_witherTeleportTask.remove(entityUUID);
        }
        else if(snowGolem_snowman.containsKey(entity.getUniqueId())) {
            snowGolem_snowman.remove(entity.getUniqueId());
            BukkitTaskUtils.cancelTask(entityUUID, snowGolem_shootTask);
        }
        else if(elaina_slime.containsValue(entityUUID)) {
            Player elaina = getPlayer(HashMapUtils.getFirstKey(entityUUID, elaina_slime));
            elaina_passive_skill_2_disable(elaina);
        }

    }


    public static HashMap<Block, UUID> sensor_warden = new HashMap<>();
    public static HashMapInteger<UUID> warden_sensorAmount = new HashMapInteger<>(2, 0, 2);
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        Material material = block.getType();
        ItemStack itemStack = event.getItemInHand();
//        block_moleBlockState.remove(block.getLocation()); //placed new block  //disabled so that even when new block placed old block will emerge when reload(so there wont be cobblestone sticking in the ground
        Location location = block.getLocation();
        World world = block.getWorld();
        Player player = event.getPlayer();
        EquipmentSlot hand = event.getHand();
        UUID playerUUID = player.getUniqueId();
//        player.setFoodLevel();
//        CraftHumanEntity s
        if (getScore(event.getPlayer(), "class") == 13) {
            if (event.getBlock().getType() == Material.PUMPKIN) {
                boolean snow = true;
                for (double y = location.getY() - 1 ; y >= location.getY() - 2; y--) {
                    Location loc = new Location(world, location.getX(), y, location.getZ());
                    Block block1 = loc.getBlock(); // world.getBlockAt(loc)
                    if (block1.getType() != Material.SNOW_BLOCK) {
                        snow = false;
                    }
//                block1.setType(Material.STONE);
                }
                if (snow) {
                    block.setType(Material.AIR);
                    for (double y = location.getY(); y >= location.getY() - 2; y--) {
                        Location loc = new Location(world, location.getX(), y, location.getZ());
                        Block block1 = loc.getBlock(); // world.getBlockAt(loc)
                        block1.setType(Material.AIR);
                    }
                    Location spawnLocation = new Location(world, location.getX() + 0.5 , location.getY() - 2, location.getZ() + 0.5);
                    Snowman snowman = (Snowman) world.spawnEntity(spawnLocation, EntityType.SNOW_GOLEM);
                    snowman.setMaxHealth(16);
                    snowman.setHealth(16);
                    snowGolem_snowman.put(snowman.getUniqueId(), event.getPlayer().getUniqueId());
                    BukkitScheduler scheduler = getScheduler();
                    BukkitTask task = scheduler.runTaskTimer(plugin, () -> {
                        Location snowmanLocation = snowman.getEyeLocation();
                        Player enemy = PlayerUtils.getClosestPlayableEnemy(player, snowmanLocation, 10);
                        if (enemy != null) {
                            Location enemyLocation = enemy.getEyeLocation();
//                            enemyLocation.setY( ( enemyLocation.getY() + enemy.getEyeLocation().getY() ) / 2);
//                            snowman.setTarget(enemy);


                            SnowGolemShoot.snowGolemShoot(snowman, enemy);
//                            Snowball snowball = (Snowball) world.spawnEntity(snowmanLocation, EntityType.SNOWBALL);
//                            snowball.setShooter(snowman);
//                            snowball.setVelocity(enemyLocation.toVector().subtract(snowmanLocation.toVector()).normalize());


//                            enemy.damage(0.0001, snowball);
//                            snowman.launchProjectile(Snowball.class, enemyLocation.toVector().subtract(snowmanLocation.toVector()).normalize());
                        }
                        if (snowman.getLocation().distance(player.getLocation()) > 12) {
//                            snowman.teleport(player.getLocation());
//                            Zombie zombie = (Zombie) world.spawnEntity(location, EntityType.ZOMBIE);
//                            Bukkit.getMobGoals()
//                            CraftLivingEntity craftLivingEntitySnowman = (CraftLivingEntity) snowman;
//                            CraftLivingEntity craftLivingEntityPlayer = (CraftLivingEntity) player;
                            TamedTeleport tamedTeleport = new TamedTeleport();
                            tamedTeleport.teleportToOwner(snowman, player);  //craftLivingEntitySnowman.getHandle(), craftLivingEntityPlayer.getHandle()

                        }
//                        snowman.getPathfinder().findPath(player);
                        snowman.getPathfinder().moveTo(player);

                    }, 0, 15);
//                    scheduler.scheduleSyncDelayedTask(plugin, () -> {
//                        snowman.getPathfinder().moveTo(player);
//                    }, 0);
                    snowGolem_shootTask.put(snowman.getUniqueId(), task);
                    scheduler.scheduleSyncDelayedTask(plugin, () -> {
                        snowman.remove();
                        snowGolem_snowman.remove(snowman.getUniqueId());
                        BukkitTaskUtils.cancelTask(snowman.getUniqueId(), snowGolem_shootTask);
                    }, 600);
                }
            }
        }
        else if(ClassU.getClassEnum(player) == ClassEnum.WARDEN) {
            if (block.getType() == Material.SCULK_SENSOR) {
                SculkSensor sculkSensor = (SculkSensor) block.getState();
                sculkSensor.setListenerRange(24); //10
                sculkSensor.update(true);
                if (warden_sensorAmount.get(playerUUID) <= 0) {
                    event.setCancelled(true);
                }
                else {

                    if (warden_sensorAmount.get(playerUUID) == 2) {
                        Cooldown.player_passiveSkill2Cooldown.put(playerUUID, 20*15);  //20*30
                    }

                    warden_sensorAmount.decrement(playerUUID);
                    if (warden_sensorAmount.get(playerUUID) == 0) {
                        ItemStack playerItem = null;
                        playerItem = new ItemStack(Material.COMMAND_BLOCK);  //BARRIER
                        ItemStackU.setAttributePlayerBlockRange(playerItem, -114514);
                        ItemStackU.setStringPersistentData(playerItem, NameSpacedKeys.ITEM_TYPE, "warden_sensor");
                        if (hand == EquipmentSlot.HAND) {
                            player.getEquipment().setItemInMainHand(playerItem);
                        }
                        else if (hand == EquipmentSlot.OFF_HAND) {
                            player.getEquipment().setItemInOffHand(playerItem);
                        }
                    }
                    sensor_warden.put(block, player.getUniqueId());
                    BukkitScheduler scheduler = Bukkit.getScheduler();
                    scheduler.runTaskLater(MegaWallsClasses.getPlugin(), () -> {
                        if (sensor_warden.containsKey(block)) {
                            sensor_warden.remove(block);
                            block.setType(Material.AIR);
                        }
                    }, 25*20L); //30* 25
                }


//                Warden warden = (Warden) block.getWorld().spawnEntity(block.getLocation(), EntityType.WARDEN);
//                warden.setTarget(player);
//                warden.setMaxHealth(40);
//                warden.setHealth(40);
//                warden.setCustomName("Warden");
//                warden.setCustomNameVisible(true);
//                sensor_warden.put(block, player.getUniqueId());
            }
        }
        else if (ClassU.getClassEnum(player) == ClassEnum.MOLE) {
            if (block.getType() == Material.SAND || block.getType() == Material.RED_SAND || block.getType() == Material.GRAVEL || block.getType() == Material.DIRT || block.getType() == Material.GRASS_BLOCK || block.getType() == Material.DIRT_PATH || block.getType() == Material.FARMLAND) {
                block_moleBlockState.put(block.getLocation(), block.getState()); // regen block when disable //unless dug by mole
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Location blockLocation = block.getLocation();
        Material material = block.getType();
        Player player = event.getPlayer();
        ItemStack itemInMainHand = player.getEquipment().getItemInMainHand();
        Collection<ItemStack> drops = block.getDrops(itemInMainHand);
        UUID playerUUID = player.getUniqueId();
//        if (block_blizzardTimes.containsKey(block)) {
//            HashMapUtils.hashMapIncrease(block, block_blizzardTimes); // add 1 so can't be turned into original block ,except reblizzared it
//        }
        block_blizzardBlockState.remove(block); // destroyed
        sensor_warden.remove(block);
        if (material == Material.SNOW_BLOCK) {
            event.setDropItems(false);
        }
        if (getClassEnum(player) == ClassEnum.MOLE) {
            if (material == Material.SAND || material == Material.RED_SAND || material == Material.GRAVEL || material == Material.DIRT || material == Material.GRASS_BLOCK || material == Material.DIRT_PATH || material == Material.FARMLAND) {
                int previousMineCount = mole_mineCount.pageUp(playerUUID);
                if (previousMineCount == 3) {
                    mole_passive_skill_1(player);
                }
                block_moleBlockState.put(blockLocation, block.getState()); // regen block when disable //unless dug by mole
            }
        }

    }

    @EventHandler
    public void onBlockReceiveGame(BlockReceiveGameEvent event) {
        GameEvent gameEvent = event.getEvent();
        Block block = event.getBlock();
        Entity entity = event.getEntity();

        // Check if the event is related to a sculk sensor and the detected entity is a player
        if (block.getType() == Material.SCULK_SENSOR) {  //block.getType().toString().contains("SCULK_SENSOR")  //gameEvent == GameEvent.BLOCK_ACTIVATE &&
            if (event.getEntity() instanceof Player) {
                Player player = (Player) event.getEntity();
                // Here you can add your custom logic when the sculk sensor senses a player
                UUID wardenUUID = sensor_warden.get(block);
                if (wardenUUID != null) {
                    Player warden = getPlayer(wardenUUID);
                    if (isPlayerPlayableEnemy(warden, player)) {
                        addEnergy(warden, 3);  //1 //5
                        player.sendMessage("The sculk sensor has detected you!");
                        player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 60, 0, false, false));
                    }
                }

            }
        }
    }

//    @EventHandler
//    public void onChunkLoad(Chunk event) {
//        event.getP
//    }
    static void levelEqualsEnergy(Player player) {
        if (getScore(player, "energy") >100) {
            setScore(player, "energy", 100);
        }
        else if(getScore(player, "energy" )<0) {
            setScore(player, "energy", 0);
        }
        player.setLevel(getScore(player, "energy"));
        player.setExp((float) getScore(player, "energy") /100);
    }



    void tryActiveSkill(Player player) {
        if (Energetic.energeticPlayers.contains(player.getUniqueId())) {
            addEnergy(player,114514);
        }
        if (getScore(player, "energy") >= 100 ) {
//            if (MegaWallsClasses.getScore(player, "energy") >= 100 ) {
//                BukkitTask task1 = tasks.remove(player.getUniqueId()); // remove from map if exist
//                if(task1 != null) { // task found
//                    task1.cancel();
//                }
//            }
            if (getScore(player, "class") != 3) {
                activeSkill(player);
            }
            else if (getScore(player, "class") == 3) {
                player.sendMessage( ChatColor.AQUA + "射箭自动触发");
            }
        }
//        else if ((MegaWallsClasses.getScore(player, "energy") < 100 && MegaWallsClasses.getScore(player, "class") == 28)){
//            player.sendMessage("我知道你是特殊职业可以标记玩家但我不会写代码");
//            if (getNearbyPlayers(player, 20 ,1).get(0) != null) {
//                Player player1 = getNearbyPlayers(player, 20 ,1).get(0);
//                if (skeleton_lord_player.containsKey(player.getUniqueId())) {
//                    skeleton_lord_player.remove(player.getUniqueId());
//                }
//                skeleton_lord_player.put(player.getUniqueId(), player1.getUniqueId());
//                player.sendMessage("你标记了" + player1.getName());
//                player1.sendMessage("你被" + player.getName() + "标记了");
//            }
//            else {
//                player.sendMessage("周围没人");
//            }
//        }
        else {
            player.sendMessage(ChatColor.RED + "差 " + ChatColor.YELLOW + (100 - getScore(player, "energy")) + ChatColor.RED + " 能量");
        }
    }

    public static void testSkillReady(Player player) {
        levelEqualsEnergy(player);
        UUID playerUUID = player.getUniqueId();
        if (getScore(player, "energy") >= 100 ) {

            if (!player_activeSkillReady.containsKey(playerUUID)) {
                disableAutoEnergyAccumulation(player);
                BukkitScheduler scheduler = getServer().getScheduler();
                BukkitTask task = scheduler.runTaskTimer(plugin, () -> {
                    player.setExp(1);
                    scheduler.scheduleSyncDelayedTask(plugin, () -> {
                        player.setExp(0);
                    }, 10);
                },0 ,20);
                player_activeSkillReady.put(playerUUID, task);
                player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 114514, 1);  //ENTITY_EXPERIENCE_ORB_PICKUP
                if (getScore(player, "class") != 3) {
                    player.sendMessage(ChatColor.GREEN + "终极技能 " + ChatColor.YELLOW + "准备就绪");
                }
                else if (getScore(player, "class") == 3) {
                    player.sendMessage(ChatColor.GREEN + "终极技能" + ChatColor.YELLOW + " 准备就绪 " + ChatColor.AQUA + "射箭自动触发");
                }
                if (ClassU.getClass(player) == 4) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 1));  //灵魂爆发
                    player.sendMessage("末影人灵魂爆发回血");
                }
            }

        }
//        else {
//            levelEqualsEnergy(player);
//        }
    }

    void activeSkill(Player player) {
//        int failed = 0;
        if (isPlayerPlayable(player)) {
            //        player.sendMessage("放放你的");
//        MegaWallsClasses.setScore(player, "energy", 0);
//        player.setLevel(0);
            boolean activeSuccess = false;
            switch (getScore(player, "class")) {
                case 1:
//                double health = player.getHealth();
                    addHealth(player, 7);
                    getWorld("world").playSound(player, Sound.ENTITY_ZOMBIE_HURT, 1, 1);
                    player.sendMessage(ChatColor.RED + "治疗之环 " + ChatColor.RED + "HP " + ChatColor.GREEN + "+7");
                    setScore(player, "energy", 0);
                    player.setLevel(0);
                    break;
                case 2:
                    herobrine_active_skill(player);
                    break;
                case 4:
                    enderman_active_skill(player);
                    break;
                case 5:
                    ArrayList<Player> nearby_n5ll = new ArrayList<Player>();
                    nearby_n5ll = getNearbyPlayers(player, 5, 114514);
                    if (nearby_n5ll.size() > 0) {
                        for (int i = 1; i <= nearby_n5ll.size(); i++) {
                            Player player2 = nearby_n5ll.get(i-1);
                            player2.addPotionEffect((new PotionEffect(PotionEffectType.BLINDNESS, 90, 0, false, false)));
                            player2.addPotionEffect((new PotionEffect(PotionEffectType.SLOWNESS, 100, 2, false, false)));
                            player2.sendMessage(ChatColor.RED + "!湮灭之力! " + ChatColor.GRAY + "失明 " + ChatColor.YELLOW + "I " + ChatColor.AQUA + "4.5s");
                            player2.sendMessage(ChatColor.RED + "!湮灭之力! " + ChatColor.GRAY + "缓慢 " + ChatColor.YELLOW + "III " + ChatColor.AQUA + "5s");
//                        player2.damage(0.0000000000000000000000000000000000000000000007006492321625, player);
//                        addHealth(player2, -5);
                            player2.playSound(player2, Sound.ENTITY_EVOKER_PREPARE_WOLOLO, 1F, 1F);
//                        player2.getWorld().strikeLightningEffect(player2.getLocation());

                        }
                        player.sendMessage(ChatColor.DARK_GRAY + "湮灭之力");
                        setScore(player, "energy", 0);
                        player.setLevel(0);
                    }
                    else {
                        player.sendMessage(ChatColor.YELLOW + "5格内" + ChatColor.RED + "没有敌人");
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_SNARE, 1, 1);
                    }
                    break;
                case 6:
                    dread_lord_active_skill(player, 2);
//                ArrayList<Object> firstBlockOrEntity = rayTraceFirstBlockOrEntity(player, 0.5, 114514);
//                player.sendMessage(firstBlockOrEntity.get(0).toString());
//                player.sendMessage(firstBlockOrEntity.get(1).toString());
                    break;
                case 7:
                    addEnergy(player, -100);
                    entity_303_active_skill(player);
                    BukkitScheduler scheduler = getServer().getScheduler();
                    scheduler.scheduleSyncDelayedTask(getPlugin(), () -> {
                        entity_303_active_skill(player);
                    }, 12L);
                    scheduler.scheduleSyncDelayedTask(getPlugin(), () -> {
                        entity_303_active_skill(player);
                    }, 24L);
//                    autoEnergyAccumulation(player, 1, 20);
                    player.sendMessage(ChatColor.RED + "303主动名字忘记了 " + ChatColor.RED + "HP " + ChatColor.GREEN + "+7");
//                    MegaWallsClasses.setScore(player, "energy", 0);
//                    player.setLevel(0);
                    activeSuccess = true;
                    break;
                case 8:
                    creeper_active_skill(player);
                    break;
                case 9:
                    undead_knight_active_skill(player);
                    break;
                case 10:
                    drownking_active_skill(player);
                    break;
                case 11:
                    spider_active_skill(player);
                    break;
                case 12:
                    shaman_active_skill(player);
                    activeSuccess = true; //tornado will give energy
                    break;
                case 13:
                    addEnergy(player, -100);
                    break;
                case 14:
                    mole_active_skill(player);
                    break;
                case 15:
                    addEnergy(player, -100);
                    break;
                case 18:
                    squid_active_skill(player);
                    break;
                case 28:
                    int number = 0;
                    UUID skeleton_lord_uuid = player.getUniqueId();
                    for (UUID uuid : skeleton_skeleton_lord.values()) {
                        if (uuid.equals(skeleton_lord_uuid)) {
                            number ++;
                        }
                    }
                    if (number == 3) {
                        skeleton_lord_active_skill_1(player, 1);
                    }
                    else if (number <= 2) {
                        skeleton_lord_active_skill_1(player, 2);
                    }
                    else if (number >= 4) {
//                        if (player.getHealth() > 20) {
                        skeleton_lord_active_skill_2(player);
//                        }
                    }
                    break;
                case 30:
                    warden_active_skill(player);
                    break;
            }
//            initializeAutoEnergyAccumulation(player);
            if (ClassU.isTransformationMaster(player) && (EnergyAccumulate.getEnergy(player) == 0 || activeSuccess) ) { // snowman should use return boolean to judge
                transformation_master_active_skill(player);
            }
            addEnergy(player, 0);
        }
        else {
            player.sendMessage("死了还想通灵?");
        }
        if (getScore(player, "energy") == 0) {
            BukkitTask task = player_activeSkillReady.remove(player.getUniqueId());
            if (task != null) {
                task.cancel();
            }
        }
    }

    void blast_arrow(Arrow arrow) {
//        Bukkit.broadcastMessage("爆炸弓箭");
//        player.sendMessage("你的箭爆炸了");
        World world = arrow.getWorld();
        world.createExplosion(arrow.getLocation(), 3, false, true, arrow);
        BukkitTask task = tasks.remove(arrow.getUniqueId()); // remove from map if exist
        if(task != null) { // task found
            task.cancel();
        }
        arrow.remove();
        blast_arrows.remove(arrow);
    }

    public static void addHealth(LivingEntity entity, double health) {
        double absorptionPlayer = entity.getAbsorptionAmount();
        double healthPlayer = entity.getHealth();
        if (health < 0 ) {  //damage
            getServer().getPluginManager().callEvent(new EntityDamageEvent(entity, EntityDamageEvent.DamageCause.CUSTOM, -health));
            if (absorptionPlayer >= -health) { // absorption able to absorb damage
                absorptionPlayer += health;
                health = 0;
            }
            else {  // absorption not able to absorb damage
                health += absorptionPlayer; // damage remove absorption amount
                absorptionPlayer = 0;
//                healthPlayer += health; // damage to player's health
            }
        }
        entity.setAbsorptionAmount(absorptionPlayer);
        healthPlayer += health;  // damage to player's health
//        else { // healing
//            healthPlayer += health;
//        }
//        healthPlayer = healthPlayer + health;
        if (0 < healthPlayer && healthPlayer < entity.getMaxHealth()) {
            entity.setHealth(healthPlayer);
//            EntityRegainHealthEvent entityRegainHealthEvent = new EntityRegainHealthEvent(entity, health, EntityRegainHealthEvent.RegainReason.CUSTOM);
//            Bukkit.getServer().getPluginManager().callEvent(entityRegainHealthEvent);
        }
        else if (healthPlayer <= 0) {
            if (entity instanceof Player) {
                Player player = (Player) entity;
//                playerDeathEvent(player);  //disabled because call event will handle death
            }
            else {
                entity.setHealth(0);
            }
        }
        else {
            entity.setHealth(entity.getMaxHealth());
        }
    }


    public static void initializeClass(Player player) {
        initializeClass(player, false);
    }
    public static void initializeClass(Player player, boolean loadAsTF) {
        if (loadAsTF) {
            initializeClassSpecific(player, loadAsTF);
            return;
        }
        Configuration configuration = plugin.getConfig();
        String playerName = player.getName();
        PlayerInventory playerInventory = player.getInventory();
        initializeClassSpecific(player, loadAsTF);
        ItemStack helmet = setUnbreakable(new ItemStack(Material.IRON_HELMET));
        helmet.addEnchantment(Enchantment.UNBREAKING, 3);
        addLore(helmet, "dont_load");
        setClassItem(helmet);
        ItemStack chestplate = setUnbreakable(new ItemStack(Material.IRON_CHESTPLATE));
        chestplate.addEnchantment(Enchantment.UNBREAKING, 3);
        addLore(chestplate, "dont_load");
        setClassItem(chestplate);
        ItemStack leggings = setUnbreakable(new ItemStack(Material.IRON_LEGGINGS));
        leggings.addEnchantment(Enchantment.UNBREAKING, 3);
        addLore(leggings, "dont_load");
        setClassItem(leggings);
        ItemStack boots = setUnbreakable(new ItemStack(Material.IRON_BOOTS));
        boots.addEnchantment(Enchantment.UNBREAKING, 3);
        addLore(boots, "dont_load");
        setClassItem(boots);
//        player.getInventory().setHelmet();
//        player.getInventory().setChestplate();
//        player.getInventory().setLeggings();
//        player.getInventory().setBoots();
        switch (getScore(player, "class")) {
            case 1:
//                player.setDisplayName(Color.RED + "[LEG]" + Color.ORANGE + "[ZOM]" + Color.WHITE + player.getName());
//                player.setDisplayName("test");
                initializeClassBase(player);
                ItemStack helmet_zombie = new ItemStack(Material.IRON_HELMET);
                helmet_zombie.addEnchantment(Enchantment.PROTECTION, 1);
                helmet_zombie = setUnbreakable(helmet_zombie);
                setClassItem(helmet_zombie);
                helmet = helmet_zombie;
                ItemStack chestplate_zombie = new ItemStack(Material.DIAMOND_CHESTPLATE);
                chestplate_zombie.addEnchantment(Enchantment.PROTECTION, 3);  //original 2
                chestplate_zombie = setUnbreakable(chestplate_zombie);
                setClassItem(chestplate_zombie);
                chestplate = chestplate_zombie;


//                ItemStack sword_zombie = new ItemStack(Material.IRON_SWORD);
//                sword_zombie.addEnchantment(Enchantment.UNBREAKING, 3);
//                sword_zombie = setUnbreakable(sword_zombie);
//                setClassItem(sword_zombie);
//                ItemStack speed_potion_zombie = new ItemStack(Material.POTION);
//                speed_potion_zombie = MegaWallsClasses.setEffect(speed_potion_zombie, PotionEffectType.SPEED, 300, 1);
//                speed_potion_zombie = setDisplayName(speed_potion_zombie, "15s II");
//                setClassItem(speed_potion_zombie);
//                ItemStack heal_potion = new ItemStack(Material.POTION);
//                heal_potion = MegaWallsClasses.setEffect(heal_potion, PotionEffectType.INSTANT_HEALTH, 20, 0);
//                heal_potion = setDisplayName(heal_potion, "20 HP");
//                heal_potion = setLore(heal_potion, "heal_potion");
//                setClassItem(heal_potion);
//                player.getInventory().setHelmet(helmet_zombie);
//                player.getInventory().setChestplate(chestplate_zombie);
//                player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".iron_sword"), sword_zombie);


//                player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".speed_potion_1"), speed_potion_zombie);
//                player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".speed_potion_2"), speed_potion_zombie);
//                player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".heal_potion_1"), heal_potion);
//                    player.getInventory().setHelmet(new ItemStack(setUnbreakable(addEnchantment(new ItemStack(Material.IRON_HELMET), Enchantment.PROTECTION, 1))));
                break;
            case 2:
                initializeClassBase(player);
                ItemStack helmet_herobrine = new ItemStack(Material.IRON_HELMET);
                helmet_herobrine.addEnchantment(Enchantment.PROTECTION, 2);
                helmet_herobrine.addEnchantment(Enchantment.BLAST_PROTECTION, 1);
                helmet_herobrine = setUnbreakable(helmet_herobrine);
                helmet = helmet_herobrine;

//                ItemStack sword_herobrine = new ItemStack(Material.DIAMOND_SWORD);
//                sword_herobrine.addEnchantment(Enchantment.UNBREAKING, 3);
//                sword_herobrine = setUnbreakable(sword_herobrine);
//                ItemStack speed_potion_herobrine = new ItemStack(Material.POTION);
//                speed_potion_herobrine = MegaWallsClasses.setEffect(speed_potion_herobrine, PotionEffectType.SPEED, 300, 1);
//                speed_potion_herobrine = setDisplayName(speed_potion_herobrine, "15s II");
//                ItemStack heal_potion_herobrine = new ItemStack(Material.POTION);
//                heal_potion_herobrine = MegaWallsClasses.setEffect(heal_potion_herobrine, PotionEffectType.INSTANT_HEALTH, 16, 0);
//                heal_potion_herobrine = setDisplayName(heal_potion_herobrine, "16 HP");
//                heal_potion_herobrine = setLore(heal_potion_herobrine, "heal_potion");
//                player.getInventory().setHelmet(helmet_herobrine);
//                player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".iron_sword"), sword_herobrine);


//                player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".speed_potion_1"), speed_potion_herobrine);
//                player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".speed_potion_2"), speed_potion_herobrine);
//                player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".heal_potion_1"), heal_potion_herobrine);
//                player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".heal_potion_2"), heal_potion_herobrine);
                break;
            case 3:
                initializeClassBase(player);
                ItemStack helmet_skeleton = new ItemStack(Material.DIAMOND_HELMET);
                helmet_skeleton.addEnchantment(Enchantment.PROTECTION, 1);
                helmet_skeleton.addEnchantment(Enchantment.PROJECTILE_PROTECTION, 2);
                helmet_skeleton = setUnbreakable(helmet_skeleton);
                ItemStack bow_skeleton = new ItemStack(Material.BOW);
                bow_skeleton.addEnchantment(Enchantment.POWER, 3);
                bow_skeleton = setUnbreakable(bow_skeleton);
                ItemStack speed_potion_skeleton = new ItemStack(Material.POTION);
                speed_potion_skeleton = MegaWallsClasses.setEffect(speed_potion_skeleton, PotionEffectType.SPEED, 300, 1);
                speed_potion_skeleton = setDisplayName(speed_potion_skeleton, "15s II");
                ItemStack heal_potion_skeleton = new ItemStack(Material.POTION);
                heal_potion_skeleton = MegaWallsClasses.setEffect(heal_potion_skeleton, PotionEffectType.INSTANT_HEALTH, 16, 0);
                heal_potion_skeleton = setDisplayName(heal_potion_skeleton, "16 HP");
                heal_potion_skeleton = setLore(heal_potion_skeleton, "heal_potion");
                player.getInventory().setHelmet(helmet_skeleton);
                player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".bow"), bow_skeleton);
//                player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".speed_potion_1"), speed_potion_skeleton);
//                player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".speed_potion_2"), speed_potion_skeleton);
//                player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".heal_potion_1"), heal_potion_skeleton);
//                player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".heal_potion_2"), heal_potion_skeleton);
//                autoEnergyAccumulation(player, 1, 20);
                break;
            case 4:
                initializeClassBase(player);
                ItemStack boots_enderman = new ItemStack(Material.DIAMOND_BOOTS);
                boots_enderman.addEnchantment(Enchantment.PROTECTION, 1);
                boots_enderman.addEnchantment(Enchantment.FEATHER_FALLING, 4);
                boots_enderman = setUnbreakable(boots_enderman);
                setClassItem(boots_enderman);
                ItemStack sword_enderman = new ItemStack(Material.IRON_SWORD);
                sword_enderman.addEnchantment(Enchantment.UNBREAKING, 3);
                sword_enderman = setUnbreakable(sword_enderman);
                setClassItem(sword_enderman);
                ItemStack speed_potion_enderman = new ItemStack(Material.POTION);
                speed_potion_enderman = MegaWallsClasses.setEffect(speed_potion_enderman, PotionEffectType.SPEED, 300, 1);
                speed_potion_enderman = setDisplayName(speed_potion_enderman, "15s II");
                setClassItem(speed_potion_enderman);
                ItemStack heal_potion_enderman = new ItemStack(Material.POTION);
                heal_potion_enderman = MegaWallsClasses.setEffect(heal_potion_enderman, PotionEffectType.INSTANT_HEALTH, 16, 0);
                heal_potion_enderman = setDisplayName(heal_potion_enderman, "16 HP");
                heal_potion_enderman = setLore(heal_potion_enderman, "heal_potion");
                setClassItem(heal_potion_enderman);
                player.getInventory().setBoots(boots_enderman);
                player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".iron_sword"), sword_enderman);
//                player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".speed_potion_1"), speed_potion_enderman);
//                player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".speed_potion_2"), speed_potion_enderman);
//                player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".heal_potion_1"), heal_potion_enderman);
//                player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".heal_potion_2"), heal_potion_enderman);
                break;
            case 5:
                initializeClassBase(player);
                ItemStack helmet_n5ll = new ItemStack(Material.LEATHER_HELMET);
                helmet_n5ll.addEnchantment(Enchantment.PROTECTION, 3);
                helmet_n5ll.addEnchantment(Enchantment.FIRE_PROTECTION, 2);
                setColor(helmet_n5ll, Color.BLACK);
                helmet_n5ll = setUnbreakable(helmet_n5ll);
                ItemStack sword_n5ll = new ItemStack(Material.NETHERITE_SWORD);
                sword_n5ll.addEnchantment(Enchantment.UNBREAKING, 3);
                sword_n5ll = setUnbreakable(sword_n5ll);
                ItemStack speed_potion_n5ll = new ItemStack(Material.POTION);
                speed_potion_n5ll = MegaWallsClasses.setEffect(speed_potion_n5ll, PotionEffectType.SPEED, 300, 1);
                speed_potion_n5ll = setDisplayName(speed_potion_n5ll, "15s II");
                ItemStack heal_potion_n5ll = new ItemStack(Material.POTION);
                heal_potion_n5ll = MegaWallsClasses.setEffect(heal_potion_n5ll, PotionEffectType.INSTANT_HEALTH, 16, 0);
                heal_potion_n5ll = setDisplayName(heal_potion_n5ll, "16 HP");
                heal_potion_n5ll = setLore(heal_potion_n5ll, "heal_potion");
                player.getInventory().setHelmet(helmet_n5ll);
                player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".iron_sword"), sword_n5ll);
//                player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".speed_potion_1"), speed_potion_n5ll);
//                player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".speed_potion_2"), speed_potion_n5ll);
//                player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".heal_potion_1"), heal_potion_n5ll);
//                player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".heal_potion_2"), heal_potion_n5ll);
                break;
            case 6:
                initializeClassBase(player);
                ItemStack helmet_dreadlord = new ItemStack(Material.DIAMOND_HELMET);
                helmet_dreadlord.addEnchantment(Enchantment.PROTECTION, 1);
                helmet_dreadlord.addEnchantment(Enchantment.BLAST_PROTECTION, 2);
                helmet_dreadlord.addEnchantment(Enchantment.FIRE_PROTECTION, 1);
                helmet_dreadlord = setUnbreakable(helmet_dreadlord);
                ItemStack sword_dreadlord = new ItemStack(Material.DIAMOND_SWORD);
                sword_dreadlord.addEnchantment(Enchantment.SMITE, 1);
                sword_dreadlord = setUnbreakable(sword_dreadlord);
                ItemStack speed_potion_dreadlord = new ItemStack(Material.POTION);
                speed_potion_dreadlord = MegaWallsClasses.setEffect(speed_potion_dreadlord, PotionEffectType.SPEED, 300, 1);
                speed_potion_dreadlord = setDisplayName(speed_potion_dreadlord, "15s II");
                ItemStack heal_potion_dreadlord = new ItemStack(Material.POTION);
                heal_potion_dreadlord = MegaWallsClasses.setEffect(heal_potion_dreadlord, PotionEffectType.INSTANT_HEALTH, 16, 0);
                heal_potion_dreadlord = setDisplayName(heal_potion_dreadlord, "16 HP");
                heal_potion_dreadlord = setLore(heal_potion_dreadlord, "heal_potion");
                player.getInventory().setHelmet(helmet_dreadlord);
                player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".iron_sword"), sword_dreadlord);
//                player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".speed_potion_1"), speed_potion_dreadlord);
//                player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".speed_potion_2"), speed_potion_dreadlord);
//                player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".heal_potion_1"), heal_potion_dreadlord);
////                player.getInventory().setItem(12, heal_potion_dreadlord);
                break;
            case 7:
                entity_303_initialize_class(player);
                break;
            case 8:
                creeper_initialize_class(player);
                break;
            case 9:
                undead_knight_initialize_class(player);
                break;
            case 10:
                drownking_initialize_class(player);
                break;
            case 11:
                spider_initialize_class(player);
                break;
            case 12:
                shaman_initialize_class(player);
                break;
            case 13:
                snowman_initialize_class(player);
                break;
            case 14:
                helmet = getClassItem(Material.GOLDEN_HELMET);
                helmet.addEnchantment(Enchantment.PROTECTION, 2);
                leggings = getClassItem(Material.DIAMOND_LEGGINGS);
                leggings.addEnchantment(Enchantment.PROTECTION, 2);
                break;
            case 15:
                elaina_initialize_class(player);
                break;
            case 18:
                boots.setType(Material.DIAMOND_BOOTS);
                boots.addEnchantment(Enchantment.PROTECTION, 2);
                boots.addEnchantment(Enchantment.DEPTH_STRIDER, 2);
                player.getInventory().setBoots(boots);
                break;
            case 28:
                initializeClassBase(player);

                ItemStack chestplate_skeleton_lord = new ItemStack(Material.DIAMOND_CHESTPLATE);
                chestplate_skeleton_lord.addEnchantment(Enchantment.PROTECTION, 3);
                chestplate_skeleton_lord = setUnbreakable(chestplate_skeleton_lord);
                ItemStack leggings_skeleton_lord = new ItemStack(Material.IRON_LEGGINGS);
                leggings_skeleton_lord.addEnchantment(Enchantment.PROTECTION, 2);
                leggings_skeleton_lord = setUnbreakable(leggings_skeleton_lord);
                ItemStack sword_skeleton_lord = new ItemStack(Material.STICK);
                sword_skeleton_lord.addUnsafeEnchantment(Enchantment.SHARPNESS, 4); //original 3
                sword_skeleton_lord = setUnbreakable(sword_skeleton_lord);
                ItemStack speed_potion_skeleton_lord = new ItemStack(Material.POTION);
                speed_potion_skeleton_lord = MegaWallsClasses.setEffect(speed_potion_skeleton_lord, PotionEffectType.SPEED, 300, 1);
                speed_potion_skeleton_lord =setDisplayName(speed_potion_skeleton_lord, "15s II");
                ItemStack heal_potion_skeleton_lord = new ItemStack(Material.POTION);
                heal_potion_skeleton_lord = MegaWallsClasses.setEffect(heal_potion_skeleton_lord, PotionEffectType.INSTANT_HEALTH, 24, 0);
                heal_potion_skeleton_lord = setDisplayName(heal_potion_skeleton_lord, "24 HP");
                heal_potion_skeleton_lord = setLore(heal_potion_skeleton_lord, "heal_potion");
                player.getInventory().setChestplate(chestplate_skeleton_lord);
                player.getInventory().setLeggings(leggings_skeleton_lord);
                player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".iron_sword"), sword_skeleton_lord);
//                player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".speed_potion_1"), speed_potion_skeleton_lord);
//                player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".speed_potion_2"), speed_potion_skeleton_lord);
//                player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".heal_potion_1"), heal_potion_skeleton_lord);
//                autoEnergyAccumulation(player, 1, 20);
//                    player.getInventory().setHelmet(new ItemStack(setUnbreakable(addEnchantment(new ItemStack(Material.IRON_HELMET), Enchantment.PROTECTION, 1))));
                break;
//            case 29:
//                initializeClassBase(player);
//                transformation_master_active_skill(player);
//                break;
            case 30:
                boots.addEnchantment(Enchantment.FEATHER_FALLING, 1);
                playerInventory.setBoots(boots);
                chestplate = getClassItem(Material.NETHERITE_CHESTPLATE);
                chestplate.addEnchantment(Enchantment.PROTECTION, 1);
                InitializeClass.warden_initialize_class(player);
                break;

        }
        if (!containsLore(helmet, "dont_load")) {
            player.getInventory().setHelmet(helmet);
        }
        if (!containsLore(chestplate, "dont_load")) {
            player.getInventory().setChestplate(chestplate);
        }
        if (!containsLore(leggings, "dont_load")) {
            player.getInventory().setLeggings(leggings);
        }
        if (!containsLore(boots, "dont_load")) {
            player.getInventory().setBoots(boots);
        }
//        initializeClassSpecific(player);


//        initializeAutoEnergyAccumulation(player);
    }



    public static ArrayList<Player> getNearbyPlayers(Player player, double range, int count){
        Location center = player.getLocation();
        HashMap<Double, Player> distancelist = new HashMap<Double, Player>();
        ArrayList<Player> nearby = new ArrayList<Player>();
        for (Entity e : player.getNearbyEntities(range, range, range)) {
            if (e instanceof Player){
                Player player1 = (Player) e;
                if (isPlayerPlayableEnemy(player, player1)) {
                    Location loc = e.getLocation();
                    double distance = Math.pow(loc.getX()-center.getX(), 2) + Math.pow(loc.getY()-center.getY(), 2) + Math.pow(loc.getZ()-center.getZ(), 2);
                    if (distance <= Math.pow(range, 2)){
                        distancelist.put(distance, player1);
                    }
                }

            }
        }
        if (distancelist.size() > count){
            for (int i = 0; i < count; i++) {
                double nearest_distance = Math.pow(range, 2) + 1 ;
                for (double distance2 : distancelist.keySet()) {
                    if (nearest_distance > distance2) {
                        nearest_distance = distance2;
                    }
                }
                Player player1 = distancelist.get(nearest_distance);
                nearby.add(player1);
                distancelist.remove(nearest_distance);
            }
        }
        else {
            for (Player player1 : distancelist.values() ) {
                nearby.add(player1);
            }
        }
        return nearby;
    }



    ArrayList<ArrayList> getTarget(Player player, int max_distance) {
        BlockIterator bItr = new BlockIterator(player.getLocation(), 0, max_distance);
        Block block;
        Location loc;
        Player player1 = null;
        boolean through_wall = false;
        ArrayList<Player> players = new ArrayList<>();
        ArrayList<Boolean> through_walls = new ArrayList<>();
        ArrayList<ArrayList> arrayLists = new ArrayList<>();
        int bx, by, bz;
        double ex, ey, ez;
        // loop through player's line of sight
        while (bItr.hasNext()) {
            block = bItr.next();
            if (block.getType() != Material.AIR) {
                through_wall = true;
            }
            bx = block.getX();
            by = block.getY();
            bz = block.getZ();
            // check for entities near this block in the line of sight
            for (Player p : getWorld("world").getPlayers()) {
                if (!p.isDead() && !p.equals(player)){
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
        players.add(player1);
        through_walls.add(through_wall);
        arrayLists.add(players);
        arrayLists.add(through_walls);
        return arrayLists;
    }

//    public static ItemStack addEnchantment(ItemStack itemStack, Enchantment enchantment, int level) {
//        ItemMeta meta = itemStack.getItemMeta();
//        meta.addEnchant(enchantment, level, true);
//        itemStack.setItemMeta(meta);
//        return itemStack;
//    }
}