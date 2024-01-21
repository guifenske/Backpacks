package br.com.backpacks.advancements;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;

public class BackpacksAdvancements {

    private Style style;
    private NamespacedKey key;
    private String icon;
    private String message;
    private String description;

    public static void displayTo(Player player, String icon, String message, String description, Style style, NamespacedKey key){
        new BackpacksAdvancements(icon, message, description, style, key).grantAdvancement(player);
    }

    private BackpacksAdvancements(String icon, String message, String description, Style style, NamespacedKey key){
        this.key = key;
        this.icon = icon;
        this.message = message;
        this.style = style;
        this.description = description;
    }


    public static void createAdvancement(NamespacedKey key, String icon, String message, String description, Style style){
        if(Bukkit.getAdvancement(key) != null) return;
        Bukkit.getUnsafe().loadAdvancement(key, "{\n" +
                "    \"criteria\": {\n" +
                "        \"trigger\": {\n" +
                "            \"trigger\": \"minecraft:impossible\"\n" +
                "        }\n" +
                "    },\n" +
                "    \"display\": {\n" +
                "        \"icon\": {\n" +
                "            \"item\": \"minecraft:" + icon + "\"\n" +
                "        },\n" +
                "        \"title\": {\n" +
                "            \"text\": \"" + message.replace("|", "\n") + "\"\n" +
                "        },\n" +
                "        \"description\": {\n" +
                "            \"text\": \"" + description.replace("|", "\n") + "\"\n" +
                "        },\n" +
                "        \"background\": \"minecraft:textures/gui/advancements/backgrounds/adventure.png\",\n" +
                "        \"frame\": \"" + style.toString().toLowerCase() + "\",\n" +
                "        \"announce_to_chat\": true,\n" +
                "        \"show_toast\": true,\n" +
                "        \"hidden\": false\n" +
                "    },\n" +
                "    \"requirements\": [\n" +
                "        [\n" +
                "            \"trigger\"\n" +
                "        ]\n" +
                "    ]\n" +
                "}");

    }

    public void grantAdvancement(Player player){
        if(player.getAdvancementProgress(Bukkit.getAdvancement(key)).isDone()) return;
        player.getAdvancementProgress(Bukkit.getAdvancement(key)).awardCriteria("trigger");
    }

    public void revokeAdvancement(Player player){
        player.getAdvancementProgress(Bukkit.getAdvancement(key)).revokeCriteria("trigger");
    }

    public enum Style {
        GOAL,
        TASK,
        CHALLENGE
    }
}
