package br.com.backpacks.events.upgrades_related;

import org.bukkit.Sound;
import org.bukkit.entity.Entity;

import java.util.HashMap;
import java.util.UUID;

public class Jukebox {
    private static final HashMap<UUID, net.kyori.adventure.sound.Sound> playing = new HashMap<>();

    public static void startPlaying(Entity entity, Sound sound){                                                                                //apparently this is the max volume
        net.kyori.adventure.sound.Sound sound1 = net.kyori.adventure.sound.Sound.sound(sound, net.kyori.adventure.sound.Sound.Source.MASTER, 2147483647, 1);

        playing.put(entity.getUniqueId(), sound1);
        entity.playSound(sound1, net.kyori.adventure.sound.Sound.Emitter.self());
    }

    public static void stopPlaying(Entity entity){
        if(!playing.containsKey(entity.getUniqueId())) return;
        entity.stopSound(playing.get(entity.getUniqueId()));
        playing.remove(entity.getUniqueId());
    }
}
