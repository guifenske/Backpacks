package br.com.Backpacks.events;

import br.com.Backpacks.menus.backpackMenu;
import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import java.util.HashMap;

public class openBackpack implements Listener {

    public static HashMap<Player, Integer> isBackChest = new HashMap<>();

    @EventHandler
    public void openBackpackEvent(PlayerSwapHandItemsEvent e) {
        if(e.getPlayer().isSneaking()) {
            if(e.getPlayer().getInventory().getChestplate() == null)    return;

            NBTItem nbtItem = new NBTItem(e.getPlayer().getInventory().getChestplate());

            if(!nbtItem.hasKey("backpack_tags"))    return;

            e.setCancelled(true);

            NBTCompound compound = nbtItem.getCompound("backpack_tags");

            isBackChest.put(e.getPlayer(), 1);

            backpackMenu.actiontypeback.put(e.getPlayer(), "chestplate");

            e.getPlayer().openInventory(backpackMenu.createBackpack(e.getPlayer(), backpackMenu.backpackName(compound), compound.getInteger("uuidback"), backpackMenu.backpackType(compound), compound.getUUID("ownerUuid")));

        }
    }
}
