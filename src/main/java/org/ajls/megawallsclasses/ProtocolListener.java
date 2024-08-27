//package org.ajls.megawallsclasses;
//
//import com.comphenix.protocol.events.PacketAdapter;
//import com.comphenix.protocol.events.PacketEvent;
//
//import java.util.Objects;
//import java.util.stream.Collectors;
//
//public class ProtocolListener extends PacketAdapter {
//
//    		super(orebfuscator, PACKET_TYPES.stream()
//            .filter(Objects::nonNull)
//				.filter(PacketType::isSupported)
//				.collect(Collectors.toList()));
//    @Override
//    public void onPacketSending(PacketEvent event) {
//
//    }
//}

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

//@EventHandler
//public void onBlockBreak(BlockBreakEvent event) {
//    Player player = event.getPlayer();
//    ScoreboardManager manager = Bukkit.getScoreboardManager();
//    Scoreboard board = manager.getMainScoreboard();
//    Objective objective = board.getObjective("挖掘榜");
//    objective.getScore(player.getName()).setScore(objective.getScore(player.getName()).getScore() + 1);
//    objective.getScore(player.getName()).
//}
