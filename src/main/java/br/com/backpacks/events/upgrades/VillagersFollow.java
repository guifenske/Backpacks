package br.com.backpacks.events.upgrades;

import br.com.backpacks.Main;
import br.com.backpacks.backpackUtils.BackPack;
import br.com.backpacks.backpackUtils.BackpackAction;
import br.com.backpacks.backpackUtils.Upgrade;
import br.com.backpacks.backpackUtils.UpgradeType;
import br.com.backpacks.recipes.RecipesNamespaces;
import br.com.backpacks.upgrades.VillagersFollowUpgrade;
import com.destroystokyo.paper.entity.Pathfinder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;

public class VillagersFollow implements Listener {
    public static void tick(){
        Bukkit.getScheduler().runTaskTimer(Main.getMain(), () -> {
            for(Player player : Bukkit.getOnlinePlayers()){
                if(!player.getPersistentDataContainer().has(new RecipesNamespaces().getHAS_BACKPACK())) continue;
                if(!player.getInventory().getItemInMainHand().getType().equals(Material.EMERALD_BLOCK) && !player.getInventory().getItemInOffHand().getType().equals(Material.EMERALD_BLOCK)){
                    continue;
                }
                BackPack backpack = Main.backPackManager.getBackpackFromId(player.getPersistentDataContainer().get(new RecipesNamespaces().getHAS_BACKPACK(), PersistentDataType.INTEGER));
                List<Upgrade> list = backpack.getUpgradesFromType(UpgradeType.VILLAGERSFOLLOW);
                if(list.isEmpty()) continue;
                VillagersFollowUpgrade upgrade = (VillagersFollowUpgrade) list.get(0);

                if(!upgrade.isEnabled()){
                    continue;
                }
                for(Entity entity : player.getNearbyEntities(10, 10, 10)){
                    if(entity.getType().equals(EntityType.VILLAGER))  moveToPlayer((Mob) entity, player);
                }
            }
        }, 0L, 10L);
    }

    @EventHandler
    private static void onClick(InventoryClickEvent event){
        if(BackpackAction.getAction((Player) event.getWhoClicked()) != BackpackAction.Action.UPGVILLAGERSFOLLOW) return;
        event.setCancelled(true);
        BackPack backPack = Main.backPackManager.getBackpackFromId(Main.backPackManager.getCurrentBackpackId().get(event.getWhoClicked().getUniqueId()));
        boolean canUse = event.getWhoClicked().getPersistentDataContainer().has(new RecipesNamespaces().getHAS_BACKPACK());
        if(canUse) canUse = backPack.getId() == event.getWhoClicked().getPersistentDataContainer().get(new RecipesNamespaces().getHAS_BACKPACK(), PersistentDataType.INTEGER);
        if(!canUse){
            event.setCancelled(true);
            event.getWhoClicked().sendMessage("§cYou can't use this upgrade because this backpack is not in your back.");
            return;
        }
        List<Upgrade> list = backPack.getUpgradesFromType(UpgradeType.VILLAGERSFOLLOW);
        VillagersFollowUpgrade upgrade = (VillagersFollowUpgrade) list.get(0);

        if(event.getRawSlot() == 13){
            upgrade.setEnabled(!upgrade.isEnabled());
            upgrade.updateInventory();
        }
    }

    @EventHandler
    private static void onClose(InventoryCloseEvent event){
        if(BackpackAction.getAction((Player) event.getPlayer()) != BackpackAction.Action.UPGVILLAGERSFOLLOW) return;
        BackPack backPack = Main.backPackManager.getBackpackFromId(Main.backPackManager.getCurrentBackpackId().get(event.getPlayer().getUniqueId()));
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                backPack.open((Player) event.getPlayer());
            }
        }.runTaskLater(Main.getMain(), 1L);
    }

    private static void moveToPlayer(Mob entity, Player player){
        Pathfinder pathfinder = entity.getPathfinder();
        pathfinder.moveTo(player, 0.55D);
    }
}
