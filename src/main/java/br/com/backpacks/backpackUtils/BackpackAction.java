package br.com.backpacks.backpackUtils;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

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

        BPLIST,

        IOMENU,

        EDITINPUT,

        EDITOUTPUT,

        OPENED;
    }

    private static final HashMap<UUID, Action> playerAction = new HashMap<>();

    public static HashMap<UUID, Action> getHashMap(){
        return playerAction;
    }

    public static void removeAction(Player player){
        playerAction.remove(player.getUniqueId());
    }

    public static void setAction(Player player, Action action){
        playerAction.put(player.getUniqueId(), action);
    }

    public static Action getAction(Player player){
        if(!playerAction.containsKey(player.getUniqueId())) return Action.NOTHING;
        return playerAction.get(player.getUniqueId());
    }

    public static Action getAction(HumanEntity player) {
        if(!playerAction.containsKey(player.getUniqueId())) return Action.NOTHING;
        return playerAction.get(player.getUniqueId());
    }
}
