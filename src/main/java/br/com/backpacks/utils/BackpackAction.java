package br.com.backpacks.utils;

import org.bukkit.entity.HumanEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    private static final HashMap<UUID, List<Action>> playerAction = new HashMap<>();

    public static HashMap<UUID, List<Action>> getHashMap(){
        return playerAction;
    }

    public static void clearPlayerActions(HumanEntity player){
        playerAction.remove(player.getUniqueId());
    }

    public static void addAction(HumanEntity player, Action action){
        if(!playerAction.containsKey(player.getUniqueId())){
            playerAction.put(player.getUniqueId(), new ArrayList<>());
            playerAction.get(player.getUniqueId()).add(action);
            return;
        }
        playerAction.get(player.getUniqueId()).add(action);
    }

    public static List<Action> getActions(HumanEntity player) {
        if(!playerAction.containsKey(player.getUniqueId())) return new ArrayList<>();
        return playerAction.get(player.getUniqueId());
    }
}
