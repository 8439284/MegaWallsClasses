package org.ajls.megawallsclasses;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ConnectionSide;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.ajls.megawallsclasses.ColorAndChatColor.translateChatColorToColor;
import static org.ajls.megawallsclasses.ScoreboardsAndTeams.getPlayerTeamColor;

public class PacketListener {
//    ProtocolLibrary.getProtocolManager().addPacketListener(
//    new PacketAdapter(this, ConnectionSide.SERVER_SIDE,
//    Packets.Server.ENTITY_EQUIPMENT) {
//        @Override
//        public void onPacketSending(PacketEvent event) {
//            PacketContainer packet = event.getPacket();
//            ItemStack stack = packet.getItemModifier().read(0);
//
//            // Only modify leather armor
//            if (stack != null && stack.getType().name().contains("LEATHER")) {
//                // The problem turned out to be that certain Minecraft functions update
//                // every player with the same packet for an equipment, whereas other
//                // methods update the equipment with a different packet per player.
//                // To fix this, we'll simply clone the packet before we modify it
//                event.setPacket(packet = packet.deepClone());
//                stack = packet.getItemModifier().read(0);
//
//                // Color that depends on the player's name
//                String recieverName = event.getPlayer().getName();
//                int color = recieverName.hashCode() & 0xFFFFFF;
//
//                // Change the color
//                LeatherArmorMeta meta = (LeatherArmorMeta) stack.getItemMeta();
//                meta.setColor(Color.fromBGR(color));
//                stack.setItemMeta(meta);
//            }
//        }
//    });
//    ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
//    protocolManager. // addPacketListener(new PacketAdapter(this, PacketType.Play.Server.ENTITY_EQUIPMENT) {
//        @Override
//        public void onPacketSending(PacketEvent event) {
//            PacketContainer packet = event.getPacket();
////                        ItemStack stack = packet.getItemModifier().read(0);
//            List<Pair<EnumWrappers.ItemSlot, ItemStack>> list = new ArrayList<>();
//            list = packet.getSlotStackPairLists().read(0);
//            ItemStack stackget = null;
//            Player player = null;
//            int entityID = packet.getIntegers().read(0);
//            for (Player p : Bukkit.getOnlinePlayers()) {
//                if (p.getEntityId() == entityID) {
//                    player = p;
//                    break;
//                }
//            } //get sender
////                        if (!nullPassiveSkillDisable.contains(player.getUniqueId())) {
//            for (int i = 0; i < list.size(); i++) {
//                Pair pair = list.get(i);
//                stackget = (ItemStack) pair.getSecond();
//                if (stackget == null) continue;
//                if (stackget.equals(new ItemStack(Material.AIR))) continue;
//                if (stackget.getItemMeta() == null) continue;
//                ItemStack stack = stackget.clone(); //modify the cloned itemstack without interfere with the original item stack
//                boolean not_armor = false;
//                // Only modify leather armor
//
//                if (stack != null && Objects.requireNonNull(stack.getItemMeta()).getEnchants().isEmpty()) { //&& stack.getType().name().contains("IRON")
//                    // The problem turned out to be that certain Minecraft functions update
//                    // every player with the same packet for an equipment, whereas other
//                    // methods update the equipment with a different packet per player.
//                    // To fix this, we'll simply clone the packet before we modify it
////                                event.setPacket(packet = packet.deepClone());
////                                stack = packet.getItemModifier().read(0);
//                    switch (stack.getType().name()) {
//                        case "IRON_HELMET":
//                            stack.setType(Material.LEATHER_HELMET);
//                            break;
//                        case "IRON_CHESTPLATE":
//                            stack.setType(Material.LEATHER_CHESTPLATE);
//                            break;
//                        case "IRON_LEGGINGS":
//                            stack.setType(Material.LEATHER_LEGGINGS);
//                            break;
//                        case "IRON_BOOTS":
//                            stack.setType(Material.LEATHER_BOOTS);
//                            break;
//                        default:
//                            not_armor = true;
//                            break;
//
//                    }
//                    if (not_armor) {
//                        break;
//                    }
//
////                                Player player = event.getPlayer();
////                            Entity entity = world.getEnti
//                    // Color that depends on the player's name
////                                String recieverName = event.getPlayer().getName();
////                            int color = recieverName.hashCode() & 0xFFFFFF;
//
//                    // Change the color
//                    if (player != null) {
//                        LeatherArmorMeta meta = (LeatherArmorMeta) stack.getItemMeta();
//                        meta.setColor(translateChatColorToColor(getPlayerTeamColor(player)));  //Color.fromBGR(color)
//                        stack.setItemMeta(meta);
//                        pair.setSecond(stack);
//                        list.set(i, pair);
//
//                    }
//                }
//            }
//            packet.getSlotStackPairLists().write(0, list);
////                        }
////                        else {
////                            nullPassiveSkillDisable.remove(player.getUniqueId());
////                        }
//
//        }
//    });

}
