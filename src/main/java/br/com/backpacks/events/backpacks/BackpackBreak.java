package br.com.backpacks.events.backpacks;

import br.com.backpacks.events.upgrades.JukeboxUpgradeEvents;
import br.com.backpacks.recipes.BackpackRecipes;
import br.com.backpacks.recipes.RecipesUtils;
import br.com.backpacks.recipes.UpgradesRecipes;
import br.com.backpacks.upgrades.JukeboxUpgrade;
import br.com.backpacks.utils.UpgradeManager;
import br.com.backpacks.utils.UpgradeType;
import br.com.backpacks.utils.backpacks.BackPack;
import br.com.backpacks.utils.backpacks.BackpackAction;
import br.com.backpacks.utils.backpacks.BackpackManager;
import br.com.backpacks.utils.inventory.InventoryBuilder;
import com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class BackpackBreak implements Listener {

    @EventHandler(ignoreCancelled = true)
    private void playerBreak(BlockBreakEvent event){
        if(!event.getBlock().getType().equals(Material.BARREL)) return;
        if(!BackpackManager.canOpen()){
            event.setCancelled(true);
            return;
        }
        if(BackpackManager.getBackpackFromLocation(event.getBlock().getLocation()) == null) return;

        event.setDropItems(false);

        BackPack backPack = BackpackManager.getBackpackFromLocation(event.getBlock().getLocation());
        ItemStack backpackItem = RecipesUtils.getItemFromBackpack(backPack);

        if(backPack.getUpgradeFromType(UpgradeType.JUKEBOX) != null){
            JukeboxUpgrade upgrade = (JukeboxUpgrade) backPack.getUpgradeFromType(UpgradeType.JUKEBOX);
            if(upgrade.getSound() != null){
                upgrade.clearParticleTask();
                upgrade.clearLoopingTask();
                JukeboxUpgradeEvents.stopSound(backPack, upgrade);
            }
        }
        for(UUID uuid : BackpackAction.getHashMap().keySet()){
            Player player = Bukkit.getPlayer(uuid);
            if(player == null) continue;
            BackpackAction.clearPlayerAction(player);
            BackpackAction.getSpectators().remove(uuid);
            BackpackManager.getCurrentPage().remove(uuid);
            BackpackManager.getCurrentBackpackId().remove(uuid);
            backPack.getViewersIds().remove(uuid);
            player.closeInventory(InventoryCloseEvent.Reason.CANT_USE);
        }

        backPack.setShowNameAbove(false);
        if(backPack.getMarkerId() != null) backPack.getMarkerEntity().remove();
        backPack.setMarkerId(null);
        backPack.setLocation(null);
        backPack.setOwner(null);
        InventoryBuilder.updateConfigInv(backPack);
        Location location = event.getBlock().getLocation();
        BackpackManager.getBackpacksPlacedLocations().remove(location);
        event.getPlayer().getWorld().dropItemNaturally(location, backpackItem);
    }

    @EventHandler(ignoreCancelled = true)
    private void explosionBreak(EntityExplodeEvent event){
        for(Block block : event.blockList()){
            if(!block.getType().equals(Material.BARREL)) continue;
            if(!BackpackManager.canOpen()){
                event.setCancelled(true);
                continue;
            }
            if(BackpackManager.getBackpackFromLocation(block.getLocation()) == null) continue;

            event.setCancelled(true);
            block.setType(Material.AIR);

            BackPack backPack = BackpackManager.getBackpackFromLocation(block.getLocation());
            ItemStack backpackItem = RecipesUtils.getItemFromBackpack(backPack);

            Location location = block.getLocation();
            if(backPack.getUpgradeFromType(UpgradeType.JUKEBOX) != null){
                JukeboxUpgrade upgrade = (JukeboxUpgrade) backPack.getUpgradeFromType(UpgradeType.JUKEBOX);
                if(upgrade.getSound() != null){
                    upgrade.clearParticleTask();
                    upgrade.clearLoopingTask();
                    JukeboxUpgradeEvents.stopSound(backPack, upgrade);
                }
            }

            for(UUID uuid : BackpackAction.getHashMap().keySet()){
                Player player = Bukkit.getPlayer(uuid);
                if(player == null) continue;
                BackpackAction.clearPlayerAction(player);
                BackpackAction.getSpectators().remove(uuid);
                BackpackManager.getCurrentPage().remove(uuid);
                BackpackManager.getCurrentBackpackId().remove(uuid);
                backPack.getViewersIds().remove(uuid);
                player.closeInventory(InventoryCloseEvent.Reason.CANT_USE);
            }

            backPack.setShowNameAbove(false);
            if(backPack.getMarkerId() != null) backPack.getMarkerEntity().remove();
            backPack.setMarkerId(null);
            backPack.setLocation(null);
            backPack.setOwner(null);
            InventoryBuilder.updateConfigInv(backPack);
            BackpackManager.getBackpacksPlacedLocations().remove(location);
            block.getWorld().dropItemNaturally(location, backpackItem);
            break;
       }
    }

    @EventHandler(ignoreCancelled = true)
    private void onItemDespawn(ItemDespawnEvent event){
        if(!event.getEntity().getItemStack().hasItemMeta())  return;
        if(!event.getEntity().getItemStack().getItemMeta().getPersistentDataContainer().has(new BackpackRecipes().isBackpack(), PersistentDataType.INTEGER)){
            if(event.getEntity().getItemStack().getItemMeta().getPersistentDataContainer().has(new UpgradesRecipes().isUpgrade(), PersistentDataType.INTEGER)){
                if(!BackpackManager.canOpen()){
                    event.setCancelled(true);
                    return;
                }
                int id = event.getEntity().getItemStack().getItemMeta().getPersistentDataContainer().get(new UpgradesRecipes().getUPGRADE_ID(), PersistentDataType.INTEGER);
                UpgradeManager.getUpgrades().remove(id);
            }
            return;
        }

        if(!BackpackManager.canOpen()){
            event.setCancelled(true);
            return;
        }

        int id = event.getEntity().getItemStack().getItemMeta().getPersistentDataContainer().get(new BackpackRecipes().getNAMESPACE_BACKPACK_ID(), PersistentDataType.INTEGER);
        if(!BackpackManager.getBackpacks().containsKey(id)) return;
        BackPack backPack = BackpackManager.getBackpacks().get(id);

        if(backPack.getUpgradeFromType(UpgradeType.UNBREAKABLE) != null){
            event.setCancelled(true);
            event.getEntity().setInvulnerable(true);
            return;
        }

        if(!backPack.getUpgradesIds().isEmpty()){
            for(int i : backPack.getUpgradesIds()){
                UpgradeManager.getUpgrades().remove(i);
            }
        }

        InventoryBuilder.deleteAllMenusFromBackpack(backPack);
        BackpackManager.getBackpacks().remove(id);
    }

    @EventHandler(ignoreCancelled = true)
    private void onEntityDeath(EntityRemoveFromWorldEvent event){
        if(!(event.getEntity() instanceof Item eventItem)) return;
        if(!eventItem.getItemStack().hasItemMeta())  return;
        if(!eventItem.getItemStack().getItemMeta().getPersistentDataContainer().has(new BackpackRecipes().isBackpack(), PersistentDataType.INTEGER)){
            if(eventItem.getItemStack().getItemMeta().getPersistentDataContainer().has(new UpgradesRecipes().isUpgrade(), PersistentDataType.INTEGER)){
                if(!BackpackManager.canOpen()){
                    return;
                }
                int id = eventItem.getItemStack().getItemMeta().getPersistentDataContainer().get(new UpgradesRecipes().getUPGRADE_ID(), PersistentDataType.INTEGER);
                UpgradeManager.getUpgrades().remove(id);
            }
            return;
        }

        if(!BackpackManager.canOpen()){
            return;
        }

        int id = eventItem.getItemStack().getItemMeta().getPersistentDataContainer().get(new BackpackRecipes().getNAMESPACE_BACKPACK_ID(), PersistentDataType.INTEGER);
        if(!BackpackManager.getBackpacks().containsKey(id)) return;
        BackPack backPack = BackpackManager.getBackpacks().get(id);

        if(backPack.getUpgradeFromType(UpgradeType.UNBREAKABLE) != null){
            Entity entity = eventItem.getWorld().dropItem(eventItem.getLocation(), RecipesUtils.getItemFromBackpack(backPack));
            entity.setInvulnerable(true);
            return;
        }

        if(!backPack.getUpgradesIds().isEmpty()){
            for(int i : backPack.getUpgradesIds()){
                UpgradeManager.getUpgrades().remove(i);
            }
        }

        InventoryBuilder.deleteAllMenusFromBackpack(backPack);
        BackpackManager.getBackpacks().remove(id);
    }
}