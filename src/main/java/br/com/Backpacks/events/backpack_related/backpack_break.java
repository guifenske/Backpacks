package br.com.Backpacks.events.backpack_related;

import br.com.Backpacks.Main;
import br.com.Backpacks.backpackUtils.BackPack;
import br.com.Backpacks.recipes.RecipesNamespaces;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class backpack_break implements Listener {

    @EventHandler
    private void general_break_backpack_chest_event(BlockBreakEvent event){
        if(!event.getBlock().getType().equals(Material.CHEST)) return;
        if(Main.back.backPackManager.get_backpack_from_location(event.getBlock().getLocation()) == null) return;

        event.setDropItems(false);

        BackPack backPack = Main.back.backPackManager.get_backpack_from_location(event.getBlock().getLocation());
        ItemStack backpack_item = new ItemStack(Material.CHEST);
        ItemMeta meta = backpack_item.getItemMeta();

        meta.setDisplayName(backPack.getName());
        meta.getPersistentDataContainer().set(new RecipesNamespaces().getIS_BACKPACK(), PersistentDataType.INTEGER, 1);
        meta.getPersistentDataContainer().set(new RecipesNamespaces().getNAMESPACE_BACKPACK_ID(), PersistentDataType.INTEGER, backPack.getBackpack_id());
        meta.getPersistentDataContainer().set(backPack.getNamespaceOfBackpackType(), PersistentDataType.INTEGER, 1);
        backpack_item.setItemMeta(meta);

        Location location = event.getBlock().getLocation();
        event.getPlayer().getWorld().dropItemNaturally(location, backpack_item);
    }

}
