package org.ajls.megawallsclasses.custommusic;

import org.ajls.megawallsclasses.MegaWallsClasses;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class CodeMusic {
    /*
    To convert musical pitches (do, re, mi) to Minecraft's playsound pitch parameter, I'll explain the conversion rules.
In Minecraft, the pitch parameter accepts values from 0.5 (lower pitch) to 2.0 (higher pitch), with 1.0 being the default/normal pitch. This parameter changes the playback speed and pitch of the sound proportionally.
Here's how musical notes convert to Minecraft pitch values:
Each semitone (half step) increase in music corresponds to multiplying the pitch value by the 12th root of 2 (approximately 1.059463). This is because an octave, which consists of 12 semitones, doubles the frequency, and 2^(1/12) ≈ 1.059463.
For a practical conversion:

Start with the default pitch of 1.0 for middle C (or "do" in a standard C major scale)
For each semitone higher, multiply by 1.059463
For each semitone lower, divide by 1.059463

For a C major scale (do, re, mi, fa, sol, la, ti, do):

Do (C) = 1.000
Re (D) = 1.122
Mi (E) = 1.260
Fa (F) = 1.335
Sol (G) = 1.498
La (A) = 1.682
Ti (B) = 1.888
Do (C, octave higher) = 2.000

Would you like me to create a more comprehensive chart with more octaves or specific notes? Or do you need help implementing this in a command block system?
     */
    public static final float semitoneMultiplier = 1.059463F;
    public void testMusic(Player player, ArrayList<CodeMusicNote> notes) {  //bpm 72
//        player.playSound(player, "custom:music/test", 1.0f, 1.0f);
        BukkitScheduler scheduler = Bukkit.getScheduler();
        AtomicInteger currentTicks = new AtomicInteger();
        AtomicInteger currentNote = new AtomicInteger();
//        for () {
//
//        }
        scheduler.runTaskTimer(MegaWallsClasses.getPlugin(), () -> {
            CodeMusicNote note = notes.get(currentNote.get());
            if (currentTicks.get() >= note.delay) {
                player.playSound(player, Sound.BLOCK_NOTE_BLOCK_HARP, 1, multiplySemitone(note.pitch));  //12+6 f5+
                currentNote.getAndIncrement();
                currentTicks.addAndGet(-note.delay);

            }
            currentTicks.getAndIncrement();
        },0, 1);
        player.playSound(player, Sound.BLOCK_NOTE_BLOCK_HARP, 1, multiplySemitone(18));  //12+6 f5+
//        player.getWorld().playSound(player.getLocation(),);
    }

    public float multiplySemitone(double semitone) {
        float pitch = 1.0F;
        for (int i = 0; i < semitone; i++) {
            pitch *= semitoneMultiplier;
        }
        return pitch;
    }
    static {

    }


}
