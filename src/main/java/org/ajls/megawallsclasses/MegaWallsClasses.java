package org.ajls.megawallsclasses;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.reflect.accessors.Accessors;
import com.comphenix.protocol.reflect.accessors.FieldAccessor;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.Pair;
import com.comphenix.protocol.wrappers.WrappedBlockData;
import net.minecraft.network.protocol.game.ClientboundLevelChunkPacketData;
import net.minecraft.world.level.block.state.BlockState;
import org.ajls.lib.utils.ScoreboardU;
import org.ajls.megawallsclasses.commands.*;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.Configuration;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.util.*;

import static org.ajls.megawallsclasses.ActiveSkills.block_moleBlockState;
import static org.ajls.megawallsclasses.ColorAndChatColor.translateChatColorToColor;
//import static org.ajls.megawallsclasses.PassiveSkills.nullPassiveSkillDisable;
import static org.ajls.megawallsclasses.GameManager.wither_team;
import static org.ajls.megawallsclasses.ScoreboardsAndTeams.*;
import static org.ajls.tractorcompass.MyListener.setTracked_teams;

public final class MegaWallsClasses extends JavaPlugin {
    public static MegaWallsClasses plugin;
    ArrayList<String> tracked_teams = new ArrayList<>();
//    public static TractorCompass tractorCompass = new TractorCompass();

    public static MegaWallsClasses getPlugin() {
        return plugin;
    }

//    public static int time = 0;
    /*
    取消玩家自己射自己加能量
    黑君隐身被攻击显示提示//message and color 冷却变金色
    黑军隐身换职业没了
    力量双倍伤害包括了力量原本的伤害加成
     */



    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        tracked_teams.add("blue_team");
//        org.ajls.
        tracked_teams.add("red_team");
        setTracked_teams(tracked_teams);
        getServer().getPluginManager().registerEvents(new MyListener(), this);
        getCommand("class").setExecutor(new ChangeClass());
        getCommand("blue").setExecutor(new Blue());
        getCommand("red").setExecutor(new Red());
        getCommand("start").setExecutor(new Start());
        getCommand("order").setExecutor(new Order());
        getCommand("backdoor").setExecutor(new Backdoor());
        getCommand("test").setExecutor(new Test());
        getCommand("l").setExecutor(new L());
        getCommand("energetic").setExecutor(new Energetic());

        PacketListener.registerPacketListener();

        World world = getServer().getWorld("world");
        Configuration configuration = getConfig();
        Location mapMinLoc = new Location(world, configuration.getInt("locations.loc_map_min.x"), configuration.getInt("locations.loc_map_min.y"), configuration.getInt("locations.loc_map_min.z"));
        Location mapMaxLoc = new Location(world, configuration.getInt("locations.loc_map_max.x"), configuration.getInt("locations.loc_map_max.y"), configuration.getInt("locations.loc_map_max.z"));
//        forceloadMap(mapMinLoc, mapMaxLoc);
        world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
        world.setGameRule(GameRule.DO_MOB_LOOT, false);
//        world.setGameRule(GameRule.KEEP_INVENTORY, true);
        world.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
        world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        world.setGameRule(GameRule.SHOW_DEATH_MESSAGES, false);

        //newgamerule below
//        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        world.setDifficulty(Difficulty.EASY);
        registerTeam("blue_team");
        registerTeam("red_team");
        setTeamRule("blue_team", Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
        setTeamRule("red_team", Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
        setFriendlyFire("blue_team", false);
        setFriendlyFire("red_team", false);
        setTeamColor("blue_team", ChatColor.BLUE);
        setTeamColor("red_team", ChatColor.RED);
        if (getScoreboard("class") == null) {
            createScoreboard("class");
        }
        if (getScoreboard("energy") == null) {
            createScoreboard("energy");
        }
        ScoreboardU.createScoreboard("tf"); //transformation master
//        PassiveSkills.elaina_icicleSpinTask();

//        BukkitScheduler scheduler = plugin.getServer().getScheduler();
//        scheduler.scheduleSyncRepeatingTask(plugin, () -> {
//            // Do something
//        }, 0L, 1L);
        Cooldown.playerCooldowns.add(Cooldown.player_passiveSkill1Cooldown);
        Cooldown.playerCooldowns.add(Cooldown.player_passiveSkill2Cooldown);
//        HashSet<HashMap<UUID, Integer>> playerCooldownsClone = (HashSet<HashMap<UUID, Integer>>) Cooldown.playerCooldowns.clone();
        BukkitScheduler scheduler = plugin.getServer().getScheduler();
//        Cooldown.registerCooldowns();
        scheduler.runTaskTimer(plugin, () -> {
//            time++;
            PassiveSkills.baseDegree++;
            if (PassiveSkills.baseDegree >= 360) {
                PassiveSkills.baseDegree = 0;
            }
            for (int i = 0; i < Cooldown.playerCooldowns.size(); i++) {
                HashMap<UUID, Integer> cooldown = Cooldown.playerCooldowns.get(i);
                Cooldown.removeCooldown(cooldown, 1, i);
            }
//            for (HashMap<UUID, Integer> cooldown : Cooldown.playerCooldowns) {
//                Cooldown.removeCooldown(cooldown, 1);
//            }
//            Cooldown.removeCooldown(Cooldown.player_passiveSkill1Cooldown, 1);
//            Cooldown.removeCooldown(Cooldown.player_passiveSkill2Cooldown, 1);
        }, 1, 1);
//        BlocksModify.fill(configuration.getInt("locations.loc_map_min.x"), configuration.getInt("locations.loc_map_min.y"), configuration.getInt("locations.loc_map_min.z"), configuration.getInt("locations.loc_map_max.x"), configuration.getInt("locations.loc_map_max.y"), configuration.getInt("locations.loc_map_max.z"));configuration.getInt("locations.loc_map_min.x"), configuration.getInt("locations.loc_map_min.y"), configuration.getInt("locations.loc_map_min.z"), configuration.getInt("locations.loc_map_max.x"), configuration.getInt("locations.loc_map_max.y"), configuration.getInt("locations.loc_map_max.z")
        BlocksModify.setBiome(configuration.getInt("locations.loc_map_min.x"), configuration.getInt("locations.loc_map_min.y"), configuration.getInt("locations.loc_map_min.z"), configuration.getInt("locations.loc_map_max.x"), configuration.getInt("locations.loc_map_max.y"), configuration.getInt("locations.loc_map_max.z"), Biome.SNOWY_PLAINS);
    }


    public static HashMap<Location, org.bukkit.block.BlockState> blockRegenWhenDisabled = new HashMap<>();
    @Override
    public void onDisable() {
        // Plugin shutdown logic
        for (UUID uuid : wither_team.keySet()) {
            Wither wither = (Wither) Bukkit.getEntity(uuid);
            wither.remove();
        }

        for (org.bukkit.block.BlockState blockState : block_moleBlockState.values()) {
            blockState.update(true);
        }
        for (org.bukkit.block.BlockState blockState : blockRegenWhenDisabled.values()) {
            blockState.update(true);
        }
    }

    public static void createScoreboard(String name) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getMainScoreboard();
        Objective objective = board.registerNewObjective(name, "dummy");
    }

    public static Objective getScoreboard(String name){
        //get the scoreboard of this name
        Objective objective = Bukkit.getScoreboardManager().getMainScoreboard().getObjective(name);
        return objective;
    }

    public static void addScore(Player player, String name, int score) {
        setScore(player, name, getScore(player, name) + score);
    }





    public static ItemStack setColor(ItemStack stack, Color color) {
        LeatherArmorMeta meta = (LeatherArmorMeta) stack.getItemMeta();
        meta.setColor(color);
        stack.setItemMeta(meta);
        return stack;
    }

    public static List<String> getLore(ItemStack itemStack){
        ItemMeta meta = itemStack.getItemMeta();
        List<String> loresList = new ArrayList<String>();
        loresList = meta.getLore();
        return loresList;
    }

    public static ItemStack setEffect(ItemStack itemstack, PotionEffectType effectType, int duration, int amplifier, boolean ambient, boolean particles) {
        PotionMeta meta = (PotionMeta) itemstack.getItemMeta();
        meta.addCustomEffect(new PotionEffect(effectType, duration, amplifier, ambient, particles), true);
        itemstack.setItemMeta(meta);
        return itemstack;
    }

    public static ItemStack setEffect(ItemStack itemstack, PotionEffectType effectType, int duration, int amplifier) {
        return  setEffect(itemstack, effectType, duration, amplifier, false, true);
    }

}
