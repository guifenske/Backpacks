package br.com.backpacks.utils.backpacks;

import org.bukkit.entity.HumanEntity;

import java.util.HashMap;
import java.util.UUID;

public class BackpackAction {
    public enum Action {
        CONFIGMENU,

        NOTHING,

        RENAMING,

        UPGCRAFTINGGRID,

        UPGMENU,

        UPGTANKS,

        UPGFURNACE,

        UPGJUKEBOX,

        UPGAUTOFEED,

        UPGVILLAGERSFOLLOW,

        UPGCOLLECTOR,

        IOMENU,

        EDITINPUT,

        EDITOUTPUT,

        OPENED;
    }

    private static final HashMap<UUID, Action> playerAction = new HashMap<>();
    private static final HashMap<UUID, Boolean> spectators = new HashMap<>();

    public static HashMap<UUID, Action> getHashMap(){
        return playerAction;
    }

    public static HashMap<UUID, Boolean> getSpectators(){
        return spectators;
    }

    public static void clearPlayerAction(HumanEntity player){
        playerAction.remove(player.getUniqueId());
    }

    public static void setAction(HumanEntity player, Action action){
        playerAction.put(player.getUniqueId(), action);
    }

    public static Action getAction(HumanEntity player) {
        if(!playerAction.containsKey(player.getUniqueId())) return Action.NOTHING;
        return playerAction.get(player.getUniqueId());
    }
}
