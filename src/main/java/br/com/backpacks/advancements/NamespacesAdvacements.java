package br.com.backpacks.advancements;

import br.com.backpacks.Main;
import org.bukkit.NamespacedKey;

public class NamespacesAdvacements {

    private static final NamespacedKey CAUGHT_A_BACKPACK = new NamespacedKey(Main.getMain(), "caughtabackpack");
    private static final NamespacedKey THEFIRSTOFUS = new NamespacedKey(Main.getMain(), "thefirstofus");

    public static NamespacedKey getCAUGHT_A_BACKPACK() {
        return CAUGHT_A_BACKPACK;
    }
    public static NamespacedKey getTHEFIRSTOFUS() {
        return THEFIRSTOFUS;
    }

}
