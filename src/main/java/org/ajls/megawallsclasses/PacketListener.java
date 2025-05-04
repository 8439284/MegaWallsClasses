package org.ajls.megawallsclasses;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ConnectionSide;
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
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.ajls.megawallsclasses.ColorAndChatColor.translateChatColorToColor;
import static org.ajls.megawallsclasses.ScoreboardsAndTeams.getPlayerTeamColor;

public class PacketListener {
    private static final Class<?> CLIENTBOUND_LEVEL_CHUNK_PACKET_DATA = MinecraftReflection.getMinecraftClass("network.protocol.game.ClientboundLevelChunkPacketData");
    private static final FieldAccessor BUFFER = Accessors.getFieldAccessor(CLIENTBOUND_LEVEL_CHUNK_PACKET_DATA, byte[].class, true);

    public static void registerPacketListener() {
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();  //this is useless do not use it
        ProtocolLibrary.getProtocolManager().addPacketListener(
                new PacketAdapter(MegaWallsClasses.getPlugin(), PacketType.Play.Server.ENTITY_EQUIPMENT) {
                    @Override
                    public void onPacketSending(PacketEvent event) {
                        PacketContainer packet = event.getPacket();
                        //                        ItemStack stack = packet.getItemModifier().read(0);
                        List<Pair<EnumWrappers.ItemSlot, ItemStack>> list = new ArrayList<>();
                        StructureModifier<List<Pair<EnumWrappers.ItemSlot, ItemStack>>> test = packet.getSlotStackPairLists();
                        list = packet.getSlotStackPairLists().read(0);
                        ItemStack stackget = null;
                        Player player = null;
                        int entityID = packet.getIntegers().read(0);
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            if (p.getEntityId() == entityID) {
                                player = p;
                                break;
                            }
                        } //get sender
                        //                        if (!nullPassiveSkillDisable.contains(player.getUniqueId())) {
                        if (player == null) return;
                        boolean tf = ClassU.isTransformationMaster(player);
                        if (false) {
//                            if (tf) {
//                                list = new ArrayList<>();
//                                list.add(new Pair<>(EnumWrappers.ItemSlot.HEAD, new ItemStack(Material.LEATHER_HELMET)));
//                                list.add(new Pair<>(EnumWrappers.ItemSlot.CHEST, new ItemStack(Material.LEATHER_CHESTPLATE)));
//                                list.add(new Pair<>(EnumWrappers.ItemSlot.LEGS, new ItemStack(Material.LEATHER_LEGGINGS)));
//                                list.add(new Pair<>(EnumWrappers.ItemSlot.FEET, new ItemStack(Material.LEATHER_BOOTS)));
//                                list.add(new Pair<>(EnumWrappers.ItemSlot.MAINHAND, new ItemStack(Material.AIR)));
//                                list.add(new Pair<>(EnumWrappers.ItemSlot.OFFHAND, new ItemStack(Material.AIR)));
//                                for (int i = 0; i < list.size(); i++) {
//                                    Pair pair = list.get(i);
//                                    ItemStack stack = (ItemStack) pair.getSecond();
//                                    ItemMeta meta2 = stack.getItemMeta();
//                                    if (meta2 instanceof LeatherArmorMeta) {
//                                        LeatherArmorMeta meta = (LeatherArmorMeta) stack.getItemMeta();
//                                        meta.setColor(translateChatColorToColor(getPlayerTeamColor(player, false)));  //Color.fromBGR(color)
//                                        stack.setItemMeta(meta);
//                                        pair.setSecond(stack);
//                                        list.set(i, pair);
//                                    }
//                                }
//                            }
                        }
                        else {
                            for (int i = 0; i < list.size(); i++) {
                                Pair pair = list.get(i);
                                stackget = (ItemStack) pair.getSecond();
                                EnumWrappers.ItemSlot itemSlot = (EnumWrappers.ItemSlot) pair.getFirst();
                                if (stackget == null) continue;
                                if (stackget.equals(new ItemStack(Material.AIR))) continue;
                                ItemStack stack = stackget.clone();
                                //                                    ItemStack stack = stackget.clone(); //modify the cloned itemstack without interfere with the original item stack
                                boolean dontModify = false;
                                if (!tf) {
                                    if (stackget.getItemMeta() == null) continue;


                                    // Only modify leather armor
                                    if (stack != null && Objects.requireNonNull(stack.getItemMeta()).getEnchants().isEmpty()) { //&& stack.getType().name().contains("IRON")
                                        // The problem turned out to be that certain Minecraft functions update
                                        // every player with the same packet for an equipment, whereas other
                                        // methods update the equipment with a different packet per player.
                                        // To fix this, we'll simply clone the packet before we modify it
                                        //                                event.setPacket(packet = packet.deepClone());
                                        //                                stack = packet.getItemModifier().read(0);
                                        switch (stack.getType().name()) {
                                            case "IRON_HELMET":
                                                stack.setType(Material.LEATHER_HELMET);
                                                break;
                                            case "IRON_CHESTPLATE":
                                                stack.setType(Material.LEATHER_CHESTPLATE);
                                                break;
                                            case "IRON_LEGGINGS":
                                                stack.setType(Material.LEATHER_LEGGINGS);
                                                break;
                                            case "IRON_BOOTS":
                                                stack.setType(Material.LEATHER_BOOTS);
                                                break;
                                            default:
                                                dontModify = true;
                                                break;

                                        }
                                        if (dontModify) {
                                            continue;  //this is a very old bug. Now it's fixed by modifying the break to continue
                                        }
                                    } else {
                                        dontModify = true;
                                    }
                                }
                                else {
                                    if (itemSlot == EnumWrappers.ItemSlot.MAINHAND || itemSlot == EnumWrappers.ItemSlot.OFFHAND) {
                                        stack = new ItemStack(Material.AIR);
                                        dontModify = true;
                                    }
                                    else  {
                                        String name = stack.getType().name();
                                        if (name.contains("HELMET")) {
                                            stack = new ItemStack(Material.LEATHER_HELMET);
                                        }
                                        else if (name.contains("CHESTPLATE")) {
                                            stack = new ItemStack(Material.LEATHER_CHESTPLATE);
                                        }
                                        else if (name.contains("LEGGINGS")) {
                                            stack = new ItemStack(Material.LEATHER_LEGGINGS);
                                        }
                                        else if (name.contains("BOOTS")) {
                                            stack = new ItemStack(Material.LEATHER_BOOTS);
                                        }
                                        else {
                                            dontModify = true;
                                        }
                                    }


//                                    switch (stack.getType().name()) {
//                                        case "IRON_HELMET":
//                                            stack.setType(Material.LEATHER_HELMET);
//                                            break;
//                                        case "IRON_CHESTPLATE":
//                                            stack.setType(Material.LEATHER_CHESTPLATE);
//                                            break;
//                                        case "IRON_LEGGINGS":
//                                            stack.setType(Material.LEATHER_LEGGINGS);
//                                            break;
//                                        case "IRON_BOOTS":
//                                            stack.setType(Material.LEATHER_BOOTS);
//                                            break;
//                                        default:
//                                            dontModify = true;
//                                            break;
//
//                                    }
                                }



                                //                                Player player = event.getPlayer();
                                //                            Entity entity = world.getEnti
                                // Color that depends on the player's name
                                //                                String recieverName = event.getPlayer().getName();
                                //                            int color = recieverName.hashCode() & 0xFFFFFF;

                                // Change the color
                                if (player != null && !dontModify) {
                                    LeatherArmorMeta meta = (LeatherArmorMeta) stack.getItemMeta();
                                    meta.setColor(translateChatColorToColor(getPlayerTeamColor(player, false)));  //Color.fromBGR(color)
                                    stack.setItemMeta(meta);
                                    pair.setSecond(stack);
                                    list.set(i, pair);

                                }

                            }
                        }
                        packet.getSlotStackPairLists().write(0, list);
                        //                        }
                        //                        else {
                        //                            nullPassiveSkillDisable.remove(player.getUniqueId());
                        //                        }

                    }
                });
        ProtocolLibrary.getProtocolManager().addPacketListener(
                new PacketAdapter(MegaWallsClasses.getPlugin(), PacketType.Play.Server.BLOCK_CHANGE) {
                    @Override
                    public void onPacketSending(PacketEvent event) {
                        PacketContainer packet = event.getPacket();
                        StructureModifier<Material> material = packet.getBlocks();
//                        Block block = ne() {
//                        }
//                        WrappedBlockData data = WrappedBlockData.createData(.getBlockData());
                        WrappedBlockData data = packet.getBlockData().read(0);
//                        Material material = data.getType();
//                        if (data.getType() == Material.IRON_ORE) {
//                            data.setType(Material.WHITE_CONCRETE);
//                        }
//                        else if(data.getType() == Material.COAL_ORE) {
//                            data.setType(Material.BROWN_CONCRETE);
//                        }
//                        else if(data.getType() == Material.STONE) {
//                            data.setType(Material.PINK_CONCRETE);
//                        }
//                        else if (data.getType() == Material.SAND) {
//                            data.setType(Material.PINK_CONCRETE_POWDER);
//                        }
                        packet.getBlockData().write(0, data);
//                        Bukkit.broadcastMessage(material.toString());
//                        Material material1 = material.read(0);
//                        packet.getBlocks().
                        List<Material> materials = material.getValues();
//                        packet.getBlocks().write(0, Material.BEDROCK);
//                        Bukkit.broadcastMessage(materials.toString());
//                        Material blockMaterial = material.read(0);
//                        material.write(0, Material.BEDROCK);
//                        packet.getBlocks().write(0, blockMaterial);
                    }
                }
        );
        ProtocolLibrary.getProtocolManager().addPacketListener(
                new PacketAdapter(MegaWallsClasses.getPlugin(), PacketType.Play.Server.MAP_CHUNK) {
                    @Override
                    public void onPacketSending(PacketEvent event) {
                        handleMapChunkPacket(event);  //gpt
                        PacketContainer packet = event.getPacket();
                        StructureModifier<WrappedBlockData> wrappedBlockData = packet.getBlockData();
//                        StructureModifier<byte[]> chunkData = packet.getByteArrays();  //disable because gpt
//                        chunkData.read(1);
//                        WrappedClientboundLevelChunkPacketData packetData = new WrappedClientboundLevelChunkPacketData(packet);
                        ClientboundLevelChunkPacketData test = (ClientboundLevelChunkPacketData) packet.getSpecificModifier(CLIENTBOUND_LEVEL_CHUNK_PACKET_DATA).read(0);
                        byte[] buffer = (byte[]) BUFFER.get(test);
//                        BitSet selectionMask = packet.getSpecificModifier(BitSet.class).read(0);
//                        byte [] buffer = packet.getSpecificModifier(byte[][].class).read(0)[0];
                        int nonAirAmount = buffer[0];
                        int bitsPerEntry = buffer[1];
                        int length = buffer[2];
                        StructureModifier<int[]> intArrays = packet.getIntegerArrays();
                        StructureModifier<byte[]> byteArrays = packet.getByteArrays();
                        int chunkX = packet.getIntegers().read(0);
                        int chunkZ = packet.getIntegers().read(1);
                        int baseX = chunkX << 4;
                        int baseZ = chunkZ << 4;
                        Player player = event.getPlayer();
                        World world = event.getPlayer().getWorld();

                        //gpt
//                        WrappedChunk wrappedChunk = new WrappedChunk(event.getPacket().getChunkData().read(0));
//
//                        // Iterate through all blocks in the chunk
//                        for (int x = 0; x < 16; x++) {
//                            for (int y = 0; y < wrappedChunk.getHeight(); y++) {
//                                for (int z = 0; z < 16; z++) {
//                                    // Get the current block
//                                    WrappedBlockData blockData = wrappedChunk.getBlock(x, y, z);
//
//                                    // Replace a specific block type with another
//                                    if (blockData.getType() == Material.STONE) {
//                                        wrappedChunk.setBlock(x, y, z, WrappedBlockData.createData(Material.DIAMOND_BLOCK));
//                                    }
//                                }
//                            }
//                        }
//
//                        // Write the modified chunk data back to the packet
//                        event.getPacket().getChunkData().write(0, wrappedChunk);
//                        //gptend

                        //gpt
//                         //Read the raw byte data from the chunk packet
//                        net.minecraft.network.FriendlyByteBuf byteBuf = (FriendlyByteBuf) event.getPacket().getModifier().read(0);   //changed ByteBuf to FriendlyByteBuf
//
//                        if (byteBuf != null) {
//                            byte[] bytes = new byte[byteBuf.readableBytes()];
//                            byteBuf.getBytes(0, bytes);
//
//                            // Convert raw bytes into a modifiable format
//                            // NOTE: You'll need to parse the byte data to find and replace blocks.
//                            // This is an advanced process and requires understanding Minecraft's chunk format.
//
//                            // Example: Simple block replacement logic (conceptual)
//                            for (int i = 0; i < bytes.length - 1; i++) {
//                                // Assuming bytes[i] is block data (this is simplified, real format is more complex)
//                                if (bytes[i] == Material.STONE.ordinal()) {
//                                    bytes[i] = (byte) Material.DIAMOND_BLOCK.ordinal();
//                                }
//                            }
//
//                            // Write the modified bytes back into the packet
//                            byteBuf.clear();
//                            byteBuf.writeBytes(bytes);
//                        }
                        //gptend
                        //gpt
//                        try {
//                            // Use ProtocolLib's API to read chunk data
//                            Object chunkData = event.getPacket().getModifier().read(0);
//
//                            if (chunkData instanceof WrappedBlockData) {
//                                WrappedBlockData blockData = (WrappedBlockData) chunkData;
//
//                                // Replace specific block types if necessary
//                                if (blockData.getType() == Material.STONE) {
//                                    event.getPacket().getModifier().write(0, WrappedBlockData.createData(Material.DIAMOND_BLOCK));
//                                }
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
                        //gptend

//                        //gpt
//                        // Get the raw chunk data from the packet
//                        byte[] chunkData = event.getPacket().getByteArrays().read(0);
//
//                        if (chunkData != null) {
//                            ByteBuf buffer = Unpooled.wrappedBuffer(chunkData);
//
//                            // Example: Iterate through the byte data (simplified for demonstration)
//                            // Note: The actual format is complex and requires parsing chunk sections correctly
//                            for (int i = 0; i < buffer.capacity(); i++) {
//                                byte currentByte = buffer.getByte(i);
//
//                                // Replace some data as an example (you'll need to adjust this for actual block replacements)
//                                if (currentByte == (byte) Material.STONE.ordinal()) {
//                                    buffer.setByte(i, (byte) Material.DIAMOND_BLOCK.ordinal());
//                                }
//                            }
//
//                            // Write the modified byte array back into the packet
//                            byte[] modifiedData = new byte[buffer.readableBytes()];
//                            buffer.getBytes(0, modifiedData);
//                            event.getPacket().getByteArrays().write(0, modifiedData);
//                        }
//                        //gptend


                        BukkitScheduler scheduler = Bukkit.getScheduler();
                        scheduler.runTaskLater(MegaWallsClasses.getPlugin(), () -> {
                            for (int x = baseX; x < baseX + 16; x++) {
                                for (int z = baseZ; z < baseZ + 16; z++) {
                                    for (int y = -64; y < 320; y++) {
//                                        Block block = world.getBlockAt(x, y, z);
//                                        Material material = block.getType();
//                                        Location location = new Location(world, x, y, z);
//                                        if (block.getType() == Material.BEDROCK) {
//                                            player.sendBlockChange(new Location(world, x, y, z), Bukkit.createBlockData(Material.PINK_GLAZED_TERRACOTTA));
//                                        }
//                                        else if (material == Material.IRON_ORE) {
//                                            player.sendBlockChange(location, Bukkit.createBlockData(Material.WHITE_CONCRETE));
//                                        }
//                                        else if (material == Material.COAL_ORE) {
//                                            player.sendBlockChange(location, Bukkit.createBlockData(Material.BROWN_CONCRETE));
//                                        }
//                                        else if (material == Material.STONE) {
//                                            player.sendBlockChange(location, Bukkit.createBlockData(Material.PINK_CONCRETE));
//                                        }
//                                        else if (material == Material.SAND) {
//                                            player.sendBlockChange(location, Bukkit.createBlockData(Material.PINK_CONCRETE_POWDER));
//                                        }
                                    }
                                }
                            }
                        }, 0);
                        if (nonAirAmount == 1231324) {
//                            World world = event.getPlayer().getWorld();
//                        int chunkX = intArrays.read(0)[0]; 	 // packet.a;
//                        int chunkZ = intArrays.read(1)[0]; 	 // packet.b;
//                        int chunkMask = intArrays.read(2)[0];  // packet.c;
//                        int extraMask = intArrays.read(3)[0];  // packet.d;
//                        processor.data = byteArray.read(1);  // packet.inflatedBuffer;
                            int startIndex = 0;
                            net.minecraft.world.level.Level nmsWorld = ((CraftWorld) world).getHandle();
                            net.minecraft.world.level.chunk.LevelChunk nmsChunk = nmsWorld.getChunk(0,0);  //x >> 4, z >> 4
                            BlockState blockState = nmsChunk.getBlockState(0,-64,0);
                            Material material = CraftBlockData.fromData(blockState).getMaterial();
                            int id = net.minecraft.world.level.block.Block.getId(blockState);
//                        Block block1 = world.getBlockAt(0,0,0);
//                        CraftBlock craftBlock = (CraftBlock) block1;
//                        Block block = craftBlock.getHandle();
//                        Block block = new net.minecraft.world.level.block.Block();

//                        ImmutableList<BlockState> possibleBlockStates = block.getStateDefinition().getPossibleStates();
//
//                        for (BlockState blockState : possibleBlockStates) {
//                            Material material = CraftBlockData.fromData(blockState).getMaterial();
//                        }

//                        Bukkit.broadcastMessage(String.valueOf(nonAirAmount) + " " + String.valueOf(nonAirAmount2) + " " + bitsPerEntry);
////                        buffer[145] = 1;
////                        if (buffer[15] == 0) {
////
////                        }
//
                            for (int x = 0; x < 16; x++) {
                                for (int y = 0; y < 16; y++) {
                                    for (int z = 0; z < 16; z++) {
                                        int index = getIndex(x, y, z);
                                        if (index < buffer.length) {
                                            int blockId = buffer[index] ;  //& 0xFF
                                            if (blockId == 0) {   // 17 dirt  // 39 plains biome // 34 grass // 51
//                                                buffer[index] = (byte) 17;
                                            }
//                                    byte bufferByte = buffer.getByte(index);
//                                    if (bufferByte == 0) {
//                                        bufferByte = 1;
//                                        buffer.setByte(index, bufferByte);
//                                    }
                                        }
                                    }
                                }
                            }
                        }

//                        BUFFER.set(test, buffer);
//                        packet.getByteArrays().write(0, buffer);


//                        packet.getSpecificModifier(CLIENTBOUND_LEVEL_CHUNK_PACKET_DATA).write(0, (Object) test);


//                        test.getReadBuffer()
//                        byte[] data = packet.getByteArrays().read(0);
//                        packet.getByteArrays().read(0);
//                        packet.getModifier().withType(ByteArray.class);
////                        Bukkit.broadcastMessage(chunkData.toString());
//                        chunkData.read(0);


//                        ClientboundLevelChunkWithLightPacket target = (ClientboundLevelChunkWithLightPacket) chunkData.getTarget();
//                        ClientboundLevelChunkPacketData clientboundLevelChunkPacketData = target.getChunkData();
//
//                        FriendlyByteBuf buffer = clientboundLevelChunkPacketData.getReadBuffer();
//
//
////                        chunkData.
////                        byte[] buffer = packet.getByteArrays().read(0); //chunkData.read(0);

//                        clientboundLevelChunkPacketData.write((RegistryFriendlyByteBuf) buffer);
//                        target.
//                        packet.getByteArrays();
////                        byteChunkData.
//
//                        chunkData.write(0, buffer);
//                        wrappedBlockData.read(0);
//                        Bukkit.broadcastMessage(wrappedBlockData.read(0).toString());

                    }
                }
        );
        protocolManager.addPacketListener(  //ProtocolLibrary.getProtocolManager()
                new PacketAdapter(MegaWallsClasses.getPlugin(), PacketType.Play.Server.UPDATE_HEALTH) {
                    public void onPacketSending(PacketEvent event) {
                        PacketContainer packet = event.getPacket();
                        Player player = event.getPlayer();
                        if (packet.getIntegers().read(0) == 20) {  //packet.getIntegers().read(1)  player.getFoodLevel() == 20
//                            packet.getIntegers().write(0, 7);
//                            player.sendMessage("hunger 20");
                        }

//                        packet.getIntegers().read(1);
//                        packet.get
                    }
                }
        );
    }

    public static int getIndex(int x, int y, int z) {
//        return y + (z * 16) + (x * 16 * 16);
        return z + y * 16 + x * 16 * 16;
    }

    private static void handleMapChunkPacket(PacketEvent event) {
        PacketContainer packet = event.getPacket();

        // Get the chunk data (assuming Minecraft 1.21 format)
//        byte[] chunkData = packet.getByteArrays().read(0);

        ClientboundLevelChunkPacketData test = (ClientboundLevelChunkPacketData) packet.getSpecificModifier(CLIENTBOUND_LEVEL_CHUNK_PACKET_DATA).read(0);
        byte[] buffer = (byte[]) BUFFER.get(test);  //chunkdata cant get because 0 is out of bound

        World world = event.getPlayer().getWorld();

        // Modify the chunk data here
        byte[] modifiedData = changeBlocksInChunk(buffer, world);

        // Update the packet with modified chunk data
//        packet.getByteArrays().write(0, modifiedData);
        BUFFER.set(test, buffer);
//        packet.getByteArrays().write(0, buffer);


//        packet.getSpecificModifier(CLIENTBOUND_LEVEL_CHUNK_PACKET_DATA).write(0, (Object) test);
    }

    private static byte[] changeBlocksInChunk(byte[] chunkData, World world) {
        // Example: Change all instances of STONE to DIAMOND_BLOCK
        BlockData targetBlock = Material.STONE.createBlockData();
        BlockData replacementBlock = Material.DIAMOND_BLOCK.createBlockData();

        // Modify chunkData to replace the target block with the replacement
        for (int i = 0; i < chunkData.length - 1; i++) {
            // Adjust this logic to handle block IDs correctly for Minecraft 1.21
//            if (chunkData[i] == 12) {  //(byte) targetBlock.getMaterial().getId()
//                chunkData[i] = 19;  //(byte) replacementBlock.getMaterial().getId()
//            }
        }

        return chunkData;
    }

}
