package br.com.Backpacks.events;

import br.com.Backpacks.Main;
import br.com.Backpacks.backpackUtils.BackPack;
import br.com.Backpacks.recipes.RecipesNamespaces;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class equip_backpack implements Listener {

    @EventHandler
    private void general_equip_backpack_event(PlayerSwapHandItemsEvent event){
        if(event.getOffHandItem() != null) return;
        if(!event.getPlayer().isSneaking()) return;
        if(event.getMainHandItem() == null) return;
        if(!event.getMainHandItem().getItemMeta().getPersistentDataContainer().has(new RecipesNamespaces().getIS_BACKPACK())) return;

        int id = event.getMainHandItem().getItemMeta().getPersistentDataContainer().get(new RecipesNamespaces().getNAMESPACE_BACKPACK_ID(), PersistentDataType.INTEGER);

        event.getPlayer().getPersistentDataContainer().set(new RecipesNamespaces().getNAMESPACE_HAS_BACKPACK(), PersistentDataType.INTEGER, 1);
        event.getPlayer().getPersistentDataContainer().set(new RecipesNamespaces().getNAMESPACE_BACKPACK_ID(), PersistentDataType.INTEGER, id);

        event.setMainHandItem(null);
        event.setCancelled(true);
    }

    @EventHandler
    private void general_unequip_backpack(PlayerSwapHandItemsEvent event) {
        if (event.getOffHandItem() != null) return;
        if (!event.getPlayer().isSneaking()) return;
        if (event.getMainHandItem() != null) return;
        if(!new RecipesNamespaces().has_backpack(event.getPlayer())) return;
        event.setCancelled(true);
        event.getPlayer().getPersistentDataContainer().remove(new RecipesNamespaces().getNAMESPACE_HAS_BACKPACK());
        event.getPlayer().getPersistentDataContainer().remove(new RecipesNamespaces().getNAMESPACE_BACKPACK_ID());

        ItemStack itemStack = new ItemStack(Material.CHEST);
        ItemMeta meta = itemStack.getItemMeta();

        BackPack backPack = Main.back.backPackManager.get_backpack_from_id(event.getPlayer().getPersistentDataContainer().get(new RecipesNamespaces().getNAMESPACE_BACKPACK_ID(), PersistentDataType.INTEGER));
        meta.setDisplayName(backPack.getName());
        meta.getPersistentDataContainer().set(new RecipesNamespaces().getIS_BACKPACK(), PersistentDataType.INTEGER, 1);
        meta.getPersistentDataContainer().set(new RecipesNamespaces().getNAMESPACE_BACKPACK_ID(), PersistentDataType.INTEGER, backPack.getBackpack_id());
        meta.getPersistentDataContainer().set(backPack.getNamespaceOfBackpackType(), PersistentDataType.INTEGER, 1);

        itemStack.setItemMeta(meta);
        event.setMainHandItem(itemStack);
    }
}
