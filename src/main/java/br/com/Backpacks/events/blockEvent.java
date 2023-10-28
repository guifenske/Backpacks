package br.com.Backpacks.events;

import br.com.Backpacks.Main;
import br.com.Backpacks.menus.backpackMenu;
import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.server.ServerLoadEvent;

public class blockEvent implements Listener {

    @EventHandler
    private void onPlaceBackpack(BlockPlaceEvent e) {
        if (e.getBlock().getType().equals(Material.CHEST)) {
            NBTItem nbtItem = new NBTItem(e.getItemInHand());
            if (nbtItem.hasKey("backpack_tags")) {

                NBTCompound list = nbtItem.getCompound("backpack_tags");

                if(!list.hasTag("ownerUuid"))   return;

                e.setCancelled(true);

                backpackMenu.handUsed.put(e.getPlayer(), e.getHand().name());

                e.getPlayer().openInventory(backpackMenu.createBackpack(e.getPlayer(), backpackMenu.backpackName(list), list.getInteger("uuidback"), backpackMenu.backpackType(list), list.getUUID("ownerUuid")));
                backpackMenu.actiontypeback.put(e.getPlayer(), "block");
            }

        }
    }
}
