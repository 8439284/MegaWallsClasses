package org.ajls.megawallsclasses.custommusic;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A utility class for playing MIDI music in Minecraft using the Spigot API.
 * Supports all pitches, including higher values with octave transposition.
 */
public class MidiMusicPlayer {

    private final Plugin plugin;
    private final Map<Integer, Sound> instrumentMap;

    /**
     * Constructor for the MidiMusicPlayer.
     *
     * @param plugin Your Spigot plugin instance
     */
    public MidiMusicPlayer(Plugin plugin) {
        this.plugin = plugin;
        this.instrumentMap = createInstrumentMap();
    }

    /**
     * Creates a mapping between MIDI instruments and Minecraft sounds.
     *
     * @return A map of MIDI program numbers to Minecraft sounds
     */
    private Map<Integer, Sound> createInstrumentMap() {
        Map<Integer, Sound> map = new HashMap<>();

        // Default instruments to Minecraft sounds
        // Piano family (0-7)
        for (int i = 0; i <= 7; i++) {
            map.put(i, Sound.BLOCK_NOTE_BLOCK_HARP);
        }

        // Chromatic percussion (8-15)
        for (int i = 8; i <= 15; i++) {
            map.put(i, Sound.BLOCK_NOTE_BLOCK_BELL);
        }

        // Organ (16-23)
        for (int i = 16; i <= 23; i++) {
            map.put(i, Sound.BLOCK_NOTE_BLOCK_FLUTE);
        }

        // Guitar (24-31)
        for (int i = 24; i <= 31; i++) {
            map.put(i, Sound.BLOCK_NOTE_BLOCK_GUITAR);
        }

        // Bass (32-39)
        for (int i = 32; i <= 39; i++) {
            map.put(i, Sound.BLOCK_NOTE_BLOCK_BASS);
        }

        // Strings (40-47)
        for (int i = 40; i <= 47; i++) {
            map.put(i, Sound.BLOCK_NOTE_BLOCK_HARP);
        }

        // Ensemble (48-55)
        for (int i = 48; i <= 55; i++) {
            map.put(i, Sound.BLOCK_NOTE_BLOCK_CHIME);
        }

        // Brass (56-63)
        for (int i = 56; i <= 63; i++) {
            map.put(i, Sound.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE);
        }

        // Reed (64-71)
        for (int i = 64; i <= 71; i++) {
            map.put(i, Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO);
        }

        // Pipe (72-79)
        for (int i = 72; i <= 79; i++) {
            map.put(i, Sound.BLOCK_NOTE_BLOCK_FLUTE);
        }

        // Synth Lead (80-87)
        for (int i = 80; i <= 87; i++) {
            map.put(i, Sound.BLOCK_NOTE_BLOCK_BIT);
        }

        // Synth Pad (88-95)
        for (int i = 88; i <= 95; i++) {
            map.put(i, Sound.BLOCK_NOTE_BLOCK_COW_BELL);
        }

        // Synth Effects (96-103)
        for (int i = 96; i <= 103; i++) {
            map.put(i, Sound.BLOCK_NOTE_BLOCK_PLING);
        }

        // Ethnic (104-111)
        for (int i = 104; i <= 111; i++) {
            map.put(i, Sound.BLOCK_NOTE_BLOCK_BANJO);
        }

        // Percussive (112-119)
        for (int i = 112; i <= 119; i++) {
            map.put(i, Sound.BLOCK_NOTE_BLOCK_HAT);
        }

        // Sound Effects (120-127)
        for (int i = 120; i <= 127; i++) {
            map.put(i, Sound.BLOCK_NOTE_BLOCK_XYLOPHONE);
        }

        // Percussion channel mapping (channel 9)
        map.put(128, Sound.BLOCK_NOTE_BLOCK_BASEDRUM); // Bass drum
        map.put(129, Sound.BLOCK_NOTE_BLOCK_SNARE);    // Snare
        map.put(130, Sound.BLOCK_NOTE_BLOCK_HAT);      // Hi-hat

        return map;
    }

    /**
     * Play a MIDI file to all players within a specified radius of a location.
     *
     * @param midiFile The MIDI file to play
     * @param location The center location to play the music
     * @param radius The radius in blocks within which players will hear the music
     * @throws InvalidMidiDataException If the MIDI file is invalid
     * @throws IOException If there's an error reading the file
     */
    public void playMidiToNearbyPlayers(File midiFile, Location location, double radius)
            throws InvalidMidiDataException, IOException {
        Sequence sequence = MidiSystem.getSequence(midiFile);
        List<Player> players = getNearbyPlayers(location, radius);
        playMidiSequence(sequence, players);
    }

    /**
     * Play a MIDI file to specific players.
     *
     * @param midiFile The MIDI file to play
     * @param players List of players who will hear the music
     * @throws InvalidMidiDataException If the MIDI file is invalid
     * @throws IOException If there's an error reading the file
     */
    public void playMidiToPlayers(File midiFile, List<Player> players)
            throws InvalidMidiDataException, IOException {
        Sequence sequence = MidiSystem.getSequence(midiFile);
        playMidiSequence(sequence, players);
    }

    /**
     * Play a MIDI file from a resource within your plugin to specific players.
     *
     * @param resourcePath Path to the resource within your plugin
     * @param players List of players who will hear the music
     * @throws InvalidMidiDataException If the MIDI file is invalid
     * @throws IOException If there's an error reading the file
     */
    public void playMidiResourceToPlayers(String resourcePath, List<Player> players)
            throws InvalidMidiDataException, IOException {
        try (InputStream is = plugin.getResource(resourcePath)) {
            if (is == null) {
                throw new IOException("Resource not found: " + resourcePath);
            }
            Sequence sequence = MidiSystem.getSequence(is);
            playMidiSequence(sequence, players);
        }
    }

    /**
     * Play a MIDI sequence to a list of players.
     *
     * @param sequence The MIDI sequence to play
     * @param players List of players who will hear the music
     */
    private void playMidiSequence(Sequence sequence, List<Player> players) {
        if (players.isEmpty()) {
            return;
        }

        float divisionType = sequence.getDivisionType();
        int resolution = sequence.getResolution();
        Track[] tracks = sequence.getTracks();

        // Store all events sorted by tick
        List<MidiEvent> allEvents = new ArrayList<>();
        Map<Integer, Integer> channelInstruments = new HashMap<>();

        // Initialize all channels to piano (program 0)
        for (int i = 0; i < 16; i++) {
            channelInstruments.put(i, 0);
        }

        // Collect all events from all tracks
        for (Track track : tracks) {
            for (int i = 0; i < track.size(); i++) {
                MidiEvent event = track.get(i);
                MidiMessage message = event.getMessage();

                // Store program changes to map instruments
                if (message instanceof ShortMessage) {
                    ShortMessage sm = (ShortMessage) message;
                    if (sm.getCommand() == ShortMessage.PROGRAM_CHANGE) {
                        int channel = sm.getChannel();
                        int program = sm.getData1();
                        channelInstruments.put(channel, program);
                    }
                }

                allEvents.add(event);
            }
        }

        // Sort events by tick
        allEvents.sort((e1, e2) -> Long.compare(e1.getTick(), e2.getTick()));

        // Convert ticks to milliseconds based on tempo
        // Default tempo is 120 BPM
        double msPerTick = 500000.0 / resolution / 1000.0;

        // Schedule note events
        long lastTick = 0;
        long currentTimeOffset = 0;

        for (MidiEvent event : allEvents) {
            MidiMessage message = event.getMessage();
            long tick = event.getTick();

            // Calculate time difference
            long tickDiff = tick - lastTick;
            long timeDelay = (long) (tickDiff * msPerTick);
            currentTimeOffset += timeDelay;
            lastTick = tick;

            // Process tempo changes
            if (message instanceof MetaMessage) {
                MetaMessage mm = (MetaMessage) message;
                if (mm.getType() == 0x51) { // Tempo change
                    byte[] data = mm.getData();
                    int tempo = ((data[0] & 0xFF) << 16) | ((data[1] & 0xFF) << 8) | (data[2] & 0xFF);
                    msPerTick = tempo / resolution / 1000.0;
                }
            }

            // Process note events
            if (message instanceof ShortMessage) {
                ShortMessage sm = (ShortMessage) message;
                int command = sm.getCommand();
                int channel = sm.getChannel();
                int data1 = sm.getData1(); // Note
                int data2 = sm.getData2(); // Velocity

                if (command == ShortMessage.NOTE_ON && data2 > 0) {
                    // Schedule note playback
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            playNote(players, channel, data1, data2, channelInstruments.get(channel));
                        }
                    }.runTaskLaterAsynchronously(plugin, currentTimeOffset / 50); // Convert to ticks (50ms per tick)
                }
            }
        }
        //players.get(0).sendMessage(currentTimeOffset + "ms delay for note: " );  //+ data1 + " velocity: " + data2
        //this allows to calculation how long the song will end
    }

    /**
     * Play a single note to all specified players.
     *
     * @param players The players who will hear the note
     * @param channel The MIDI channel
     * @param pitch The MIDI pitch (0-127)
     * @param velocity The note velocity (volume)
     * @param instrument The MIDI program number
     */
    private void playNote(List<Player> players, int channel, int pitch, int velocity, int instrument) {
        // Skip notes that are too quiet
//        if (velocity < 10) {
//            return;
//        }

        // Handle percussion channel differently (channel 9)
        Sound sound;
        if (channel == 9) {
            // Map percussion instruments
            sound = instrumentMap.getOrDefault(128 + (pitch % 3), Sound.BLOCK_NOTE_BLOCK_BASEDRUM);
        } else {
            sound = instrumentMap.getOrDefault(instrument, Sound.BLOCK_NOTE_BLOCK_HARP);
        }

        // Convert MIDI pitch to exact frequency
        float notePitch = convertMidiPitchToExactFrequency(pitch);
        int offset = 0;
        while (notePitch > 2.0) {
            notePitch /= 4.0; // Transpose down an octave
            offset++;
        }
        while (notePitch < 0.5) {
            notePitch *= 4.0; // Transpose up an octave
            offset--;
        }
        String soundPath =  "block.note_block.harp" + "_" + offset;   //sound.name().toLowerCase(Locale.ROOT)

        // Calculate volume (0.0-1.0)
        float volume = velocity / 127.0f;

        // Play the sound to each player with exact pitch
        for (Player player : players) {
            player.stopSound(SoundCategory.MUSIC);  //stop the background music

            // Using Bukkit scheduler to ensure thread safety
            int finalOffset = offset;
            float finalNotePitch = notePitch;
            if (finalOffset != 0) {
                player.playSound(player, soundPath, SoundCategory.RECORDS, volume, finalNotePitch);
//                player.sendMessage("Playing sound extended: " + soundPath + " with pitch: " + finalNotePitch);
            }
            else {
                player.playSound(player, sound, SoundCategory.RECORDS, volume, finalNotePitch);
//                player.sendMessage("Playing sound: " + sound + " with pitch: " + finalNotePitch);
            }

//            Bukkit.getScheduler().runTask(plugin, () -> {
//                // Using the exact frequency with custom sound category for better accuracy
//
////                player.playSound(player.getLocation(), sound, org.bukkit.SoundCategory.RECORDS, volume, notePitch);
////                player.playSound(player, "custom:music/note", SoundCategory.RECORDS, volume, notePitch);
//
//            });
        }
    }

    /**
     * Converts a MIDI pitch (0-127) to an exact frequency pitch value.
     * Preserves the original pitch without rounding to semitones.
     *
     * @param midiPitch The MIDI pitch value
     * @return An exact pitch frequency value
     */
    private float convertMidiPitchToExactFrequency(int midiPitch) {
        // A4 (MIDI note 69) = 440 Hz
        // To convert MIDI note to frequency: f = 2^((n-69)/12) * 440
        // Each MIDI note is exactly one semitone apart
        // Minecraft's pitch is a frequency multiplier relative to the sound's base frequency

        // Using exact mathematics to calculate precise frequencies
        // Relative to A4 (MIDI 69) = 440Hz
        double exactFrequency = Math.pow(2, (midiPitch - 66) / 12.0);// * 440;  //original 69 a4 changed to 66 f4# to correspond noteblock interacted 12 times(pitch 1)

        // Assuming Minecraft's base frequency is around A4 (440Hz)
        // We convert to a relative pitch multiplier
        double pitchMultiplier = exactFrequency / 440.0;

        // Ensure the value is within playable range
        // Minecraft technically accepts values outside the 0.5-2.0 range
        // Some clients may cap these values, but the server will send the exact value

        // For extreme pitch values, we'll still transpose to ensure audibility
        // while maintaining the exact pitch within an audible octave
//        while (pitchMultiplier > 4.0) {
//            pitchMultiplier /= 2.0;  // Octave down but preserve exact pitch within that octave
//        }
//
//        while (pitchMultiplier < 0.25) {
//            pitchMultiplier *= 2.0;  // Octave up but preserve exact pitch within that octave
//        }

        return (float) exactFrequency;  //pitchMultiplier
    }

    /**
     * Get all players within a specific radius of a location.
     *
     * @param center The center location
     * @param radius The radius in blocks
     * @return A list of players within the radius
     */
    private List<Player> getNearbyPlayers(Location center, double radius) {
        List<Player> nearbyPlayers = new ArrayList<>();
        double radiusSquared = radius * radius;

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getWorld().equals(center.getWorld()) &&
                    player.getLocation().distanceSquared(center) <= radiusSquared) {
                nearbyPlayers.add(player);
            }
        }

        return nearbyPlayers;
    }

    /**
     * Stop all currently playing MIDI sequences.
     * Note: This will only prevent future notes from playing, not stop sounds already played.
     */
    public void stopAllSequences() {
        Bukkit.getScheduler().cancelTasks(plugin);
    }

    /**
     * Play a simple test scale to verify the MIDI player is working.
     *
     * @param player The player who will hear the scale
     */
    public void playTestScale(Player player) {
        List<Player> players = new ArrayList<>();
        players.add(player);

        // Play a C major scale
        int[] notes = {60, 62, 64, 65, 67, 69, 71, 72};
        long delay = 0;

        for (int note : notes) {
            int finalNote = note;
            new BukkitRunnable() {
                @Override
                public void run() {
                    playNote(players, 0, finalNote, 100, 0);
                }
            }.runTaskLaterAsynchronously(plugin, delay);
            delay += 5; // 5 ticks = 0.25 seconds
        }
    }
}