package br.com.backpacks.backpackUtils;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BackpackAction {

    public enum Action {
        CONFIGMENU,

        NOTHING,

        RENAMING,

        OPENED;
    }

    private static Map<UUID, Action> playerAction = new HashMap<>();

    public static void setAction(Player player, Action action){
        playerAction.put(player.getUniqueId(), action);
    }

    public static Action getAction(Player player){
        if(!playerAction.containsKey(player.getUniqueId())) return Action.NOTHING;
        return playerAction.get(player.getUniqueId());
    }
}
