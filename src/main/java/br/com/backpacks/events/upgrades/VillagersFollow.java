package br.com.backpacks.events.upgrades;

import br.com.backpacks.Main;
import br.com.backpacks.backpackUtils.BackPack;
import br.com.backpacks.backpackUtils.BackpackAction;
import br.com.backpacks.backpackUtils.Upgrade;
import br.com.backpacks.backpackUtils.inventory.ItemCreator;
import br.com.backpacks.recipes.RecipesNamespaces;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class VillagersFollow implements Listener {
    HashMap<UUID, List<Villager>> villagersFollow = new HashMap<>();

    @EventHandler
    private void onPlayerMove(PlayerMoveEvent event) {
        if(!event.getPlayer().getPersistentDataContainer().has(new RecipesNamespaces().getHAS_BACKPACK()))  return;
        if(!event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.EMERALD_BLOCK) || !event.getPlayer().getInventory().getItemInOffHand().getType().equals(Material.EMERALD_BLOCK)) return;

        BackPack backpack = Main.backPackManager.getBackpackFromId(event.getPlayer().getPersistentDataContainer().get(new RecipesNamespaces().getHAS_BACKPACK(), PersistentDataType.INTEGER));

        if(!backpack.containsUpgrade(Upgrade.VILLAGERSFOLLOW) || !backpack.isVillagersEnabled()){
            villagersFollow.remove(event.getPlayer().getUniqueId());
            return;
        }
        List<Villager> entities = new ArrayList<>();
        for(Entity entity : event.getPlayer().getNearbyEntities(13, 10, 13)){
            if(entity.getType().equals(EntityType.VILLAGER)) entities.add((Villager) entity);
        }
        if(entities.isEmpty())  return;
        villagersFollow.put(event.getPlayer().getUniqueId(), entities);

        for(Villager entity : entities){
            if(entity.getTarget() != null && entity.getTarget().equals(event.getPlayer())) continue;
            entity.setTarget(event.getPlayer());
        }
    }

    public static Inventory inventory(Player player, BackPack backPack){
        Inventory inventory = Bukkit.createInventory(player, 27, "§6§lVillagers Follow");
        ItemStack enable = new ItemCreator(Material.GREEN_STAINED_GLASS_PANE, "Enable").get();
        ItemStack disable = new ItemCreator(Material.RED_STAINED_GLASS_PANE, "Disable").get();

        if(!backPack.isVillagersEnabled()){
            inventory.setItem(13, enable);
        }   else inventory.setItem(13, disable);

        return null;
    }

    @EventHandler
    private static void onClick(InventoryClickEvent event){
        if(BackpackAction.getAction((Player) event.getWhoClicked()) != BackpackAction.Action.UPGVILLAGERSFOLLOW) return;
        event.setCancelled(true);
        BackPack backPack = Main.backPackManager.getBackpackFromId(Main.backPackManager.getCurrentBackpackId().get(event.getWhoClicked().getUniqueId()));
        if(backPack == null) return;

        if(event.getRawSlot() == 13){
            backPack.setVillagersIsEnabled(!backPack.isVillagersEnabled());
        }
    }
}
