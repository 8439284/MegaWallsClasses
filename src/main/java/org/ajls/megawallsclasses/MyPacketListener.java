package org.ajls.megawallsclasses;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import org.bukkit.plugin.Plugin;

public class MyPacketListener extends PacketAdapter {
    public MyPacketListener(Plugin plugin) {
        super(plugin, ListenerPriority.NORMAL, PacketType.Play.Client.USE_ENTITY);
        // Here, PacketType.Play.Client.USE_ENTITY is just an example.
        // You can change it to the packet type you want to listen for.
    }

}
