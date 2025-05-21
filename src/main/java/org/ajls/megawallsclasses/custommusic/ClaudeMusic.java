package org.ajls.megawallsclasses.custommusic;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Note;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ClaudeMusic extends JavaPlugin {

    private final Map<String, MidiTrackPlayer> activePlayers = new HashMap<>();
    private File midiDirectory;

    @Override
    public void onEnable() {
        // Create plugin directory if it doesn't exist
        midiDirectory = new File(getDataFolder(), "midis");
        if (!midiDirectory.exists()) {
            midiDirectory.mkdirs();
        }

        getLogger().info("MidiPlayer has been enabled!");
        getCommand("midi").setExecutor(this);
    }

    @Override
    public void onDisable() {
        // Stop all playing tracks
        for (MidiTrackPlayer player : activePlayers.values()) {
            player.stop();
        }
        activePlayers.clear();

        getLogger().info("MidiPlayer has been disabled!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage(ChatColor.YELLOW + "MIDI Player Commands:");
            player.sendMessage(ChatColor.GOLD + "/midi play <filename> - Play a MIDI file");
            player.sendMessage(ChatColor.GOLD + "/midi stop - Stop playing the current MIDI file");
            player.sendMessage(ChatColor.GOLD + "/midi list - List available MIDI files");
            return true;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "play":
                if (args.length < 2) {
                    player.sendMessage(ChatColor.RED + "Please specify a MIDI file to play.");
                    return true;
                }
                playMidi(player, args[1]);
                break;
            case "stop":
                stopMidi(player);
                break;
            case "list":
                listMidis(player);
                break;
            default:
                player.sendMessage(ChatColor.RED + "Unknown command. Use /midi for help.");
                break;
        }

        return true;
    }

    private void playMidi(Player player, String filename) {
        // Stop any current playback
        stopMidi(player);

        // Add .mid extension if not provided
        if (!filename.toLowerCase().endsWith(".mid")) {
            filename += ".mid";
        }

        File midiFile = new File(midiDirectory, filename);
        if (!midiFile.exists()) {
            player.sendMessage(ChatColor.RED + "MIDI file not found: " + filename);
            return;
        }

        try {
            Sequence sequence = MidiSystem.getSequence(midiFile);
            MidiTrackPlayer trackPlayer = new MidiTrackPlayer(player, sequence, this);
            activePlayers.put(player.getName(), trackPlayer);
            trackPlayer.start();
            player.sendMessage(ChatColor.GREEN + "Playing MIDI: " + filename);
        } catch (InvalidMidiDataException | IOException e) {
            player.sendMessage(ChatColor.RED + "Error loading MIDI file: " + e.getMessage());
            getLogger().warning("Error loading MIDI file: " + e.getMessage());
        }
    }

    private void stopMidi(Player player) {
        MidiTrackPlayer trackPlayer = activePlayers.remove(player.getName());
        if (trackPlayer != null) {
            trackPlayer.stop();
            player.sendMessage(ChatColor.YELLOW + "MIDI playback stopped.");
        }
    }

    private void listMidis(Player player) {
        File[] files = midiDirectory.listFiles((dir, name) -> name.toLowerCase().endsWith(".mid"));

        if (files == null || files.length == 0) {
            player.sendMessage(ChatColor.YELLOW + "No MIDI files found. Place MIDI files in plugins/MidiPlayer/midis/");
            return;
        }

        player.sendMessage(ChatColor.YELLOW + "Available MIDI files:");
        for (File file : files) {
            player.sendMessage(ChatColor.GOLD + "- " + file.getName());
        }
    }

    // Inner class to handle MIDI track playback
    private static class MidiTrackPlayer {
        private final Player player;
        private final Sequence sequence;
        private final JavaPlugin plugin;
        private BukkitRunnable task;
        private boolean running = false;

        public MidiTrackPlayer(Player player, Sequence sequence, JavaPlugin plugin) {
            this.player = player;
            this.sequence = sequence;
            this.plugin = plugin;
        }

        public void start() {
            running = true;

            task = new BukkitRunnable() {
                @Override
                public void run() {
                    try {
                        playSequence();
                    } catch (Exception e) {
                        player.sendMessage(ChatColor.RED + "Error playing MIDI: " + e.getMessage());
                        plugin.getLogger().warning("Error playing MIDI: " + e.getMessage());
                        stop();
                    }
                }
            };

            task.runTaskAsynchronously(plugin);
        }

        public void stop() {
            running = false;
            if (task != null) {
                task.cancel();
                task = null;
            }
        }

        private void playSequence() throws InvalidMidiDataException, MidiUnavailableException {
            float divisionType = sequence.getDivisionType();
            long tickLength = sequence.getTickLength();
            long microsecondLength = sequence.getMicrosecondLength();

            // Create a sequencer for timing
            Sequencer sequencer = MidiSystem.getSequencer(false);
            sequencer.open();
            sequencer.setSequence(sequence);

            // Get microseconds per tick for timing
            float microsPerTick = (float) microsecondLength / tickLength;

            // Process all tracks
            Track[] tracks = sequence.getTracks();

            // Combine all track events and sort by tick
            Map<Long, Note.Tone> noteEvents = new HashMap<>();

            for (Track track : tracks) {
                for (int i = 0; i < track.size(); i++) {
                    MidiEvent event = track.get(i);
                    MidiMessage message = event.getMessage();

                    if (message instanceof ShortMessage) {
                        ShortMessage sm = (ShortMessage) message;
                        if (sm.getCommand() == ShortMessage.NOTE_ON && sm.getData2() > 0) {
                            // Get note info
                            int key = sm.getData1();
                            // Convert MIDI note to Minecraft note
                            Note.Tone tone = convertMidiToMinecraftTone(key);
                            if (tone != null) {
                                noteEvents.put(event.getTick(), tone);
                            }
                        }
                    }
                }
            }

            // Play the notes with appropriate timing
            long lastTick = 0;
            for (Map.Entry<Long, Note.Tone> entry : noteEvents.entrySet()) {
                if (!running) break;

                long tick = entry.getKey();
                Note.Tone tone = entry.getValue();

                // Calculate delay
                long tickDiff = tick - lastTick;
                if (tickDiff > 0) {
                    long sleepTime = (long) (tickDiff * microsPerTick / 1000);
                    try {
                        Thread.sleep(Math.max(sleepTime, 10)); // Minimum 10ms between notes
                    } catch (InterruptedException e) {
                        break;
                    }
                }

                // Play the note on the main thread
                final Note.Tone finalTone = tone;
                Bukkit.getScheduler().runTask(plugin, () -> {
                    if (player.isOnline() && running) {
                        player.playNote(player.getLocation(), org.bukkit.Instrument.PIANO, new Note(0, finalTone, false));
                    }
                });

                lastTick = tick;
            }

            sequencer.close();
        }

        private Note.Tone convertMidiToMinecraftTone(int midiNote) {
            // MIDI notes range from 0 to 127
            // Minecraft has limited note range with specific tones
            // Map MIDI notes to the closest Minecraft tones

            // Normalize to octave (mod 12)
            int note = midiNote % 12;

            switch (note) {
                case 0: return Note.Tone.F_SHARP; // C -> F#
                case 1: return Note.Tone.G;       // C# -> G
                case 2: return Note.Tone.G_SHARP; // D -> G#
                case 3: return Note.Tone.A;       // D# -> A
                case 4: return Note.Tone.A_SHARP; // E -> A#
                case 5: return Note.Tone.B;       // F -> B
                case 6: return Note.Tone.C;       // F# -> C
                case 7: return Note.Tone.C_SHARP; // G -> C#
                case 8: return Note.Tone.D;       // G# -> D
                case 9: return Note.Tone.D_SHARP; // A -> D#
                case 10: return Note.Tone.E;      // A# -> E
                case 11: return Note.Tone.F;      // B -> F
                default: return Note.Tone.F;
            }
        }
    }
}
