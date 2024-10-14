package br.com.backpacks.events.backpacks;

import br.com.backpacks.Main;
import br.com.backpacks.events.upgrades.Jukebox;
import br.com.backpacks.recipes.BackpackRecipes;
import br.com.backpacks.recipes.RecipesUtils;
import br.com.backpacks.recipes.UpgradesRecipes;
import br.com.backpacks.upgrades.JukeboxUpgrade;
import br.com.backpacks.utils.UpgradeManager;
import br.com.backpacks.utils.UpgradeType;
import br.com.backpacks.utils.backpacks.BackPack;
import br.com.backpacks.utils.backpacks.BackpackAction;
import br.com.backpacks.utils.inventory.InventoryBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Barrel;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class BackpackBreak implements Listener {

    @EventHandler(ignoreCancelled = true)
    private void playerBreak(BlockBreakEvent event){

        if(!event.getBlock().getType().equals(Material.BARREL)) return;
        if(!Main.backPackManager.canOpen()){
            event.setCancelled(true);
            return;
        }

        if(Main.backPackManager.getBackpackFromLocation(event.getBlock().getLocation()) == null) return;

        event.setCancelled(true);


        BackPack backPack = Main.backPackManager.getBackpackFromLocation(event.getBlock().getLocation());
        ItemStack backpackItem = RecipesUtils.getItemFromBackpack(backPack);

        if(backPack.getFirstUpgradeFromType(UpgradeType.JUKEBOX) != null){
            JukeboxUpgrade upgrade = (JukeboxUpgrade) backPack.getFirstUpgradeFromType(UpgradeType.JUKEBOX);
            if(upgrade.getSound() != null){
                upgrade.clearParticleTask();
                upgrade.clearLoopingTask();
                Jukebox.stopSound(backPack, upgrade);
            }
        }

        for(UUID uuid : BackpackAction.getHashMap().keySet()){
            Player player = Bukkit.getPlayer(uuid);
            if(player == null) continue;
            BackpackAction.clearPlayerAction(player);
            BackpackAction.getSpectators().remove(uuid);
            Main.backPackManager.getCurrentPage().remove(uuid);
            Main.backPackManager.getCurrentBackpackId().remove(uuid);
            backPack.getViewersIds().remove(uuid);
            player.closeInventory();
        }

        backPack.setShowNameAbove(false);
        if(backPack.getMarker() != null) backPack.getMarkerEntity().remove();
        backPack.setMarker(null);
        backPack.setLocation(null);
        backPack.setIsBlock(false);
        backPack.setOwner(null);
        InventoryBuilder.updateConfigInv(backPack);
        Location location = event.getBlock().getLocation();
        Main.backPackManager.getBackpacksPlacedLocations().remove(location);
        event.getBlock().setType(Material.AIR, true);
        event.getPlayer().getWorld().dropItemNaturally(location, backpackItem);
    }

    @EventHandler(ignoreCancelled = true)
    private void explosionBreak(EntityExplodeEvent event){
        for(Block block : event.blockList()){
            if(!block.getType().equals(Material.BARREL)) continue;
            if(!Main.backPackManager.canOpen()){
                event.setCancelled(true);
                continue;
            }

            if(Main.backPackManager.getBackpackFromLocation(block.getLocation()) == null) continue;

            event.setCancelled(true);
            block.setType(Material.AIR);

            BackPack backPack = Main.backPackManager.getBackpackFromLocation(block.getLocation());
            ItemStack backpackItem = RecipesUtils.getItemFromBackpack(backPack);

            Location location = block.getLocation();
            if(backPack.getFirstUpgradeFromType(UpgradeType.JUKEBOX) != null){
                JukeboxUpgrade upgrade = (JukeboxUpgrade) backPack.getFirstUpgradeFromType(UpgradeType.JUKEBOX);
                if(upgrade.getSound() != null){
                    upgrade.clearParticleTask();
                    upgrade.clearLoopingTask();
                    Jukebox.stopSound(backPack, upgrade);
                }
            }

            for(UUID uuid : BackpackAction.getHashMap().keySet()){
                Player player = Bukkit.getPlayer(uuid);
                if(player == null) continue;
                BackpackAction.clearPlayerAction(player);
                BackpackAction.getSpectators().remove(uuid);
                Main.backPackManager.getCurrentPage().remove(uuid);
                Main.backPackManager.getCurrentBackpackId().remove(uuid);
                backPack.getViewersIds().remove(uuid);
                player.closeInventory();
            }

            backPack.setShowNameAbove(false);
            if(backPack.getMarker() != null) backPack.getMarkerEntity().remove();
            backPack.setMarker(null);
            backPack.setLocation(null);
            backPack.setIsBlock(false);
            backPack.setOwner(null);
            InventoryBuilder.updateConfigInv(backPack);
            Main.backPackManager.getBackpacksPlacedLocations().remove(location);
            block.getWorld().dropItemNaturally(location, backpackItem);
            break;
       }
    }

    @EventHandler(ignoreCancelled = true)
    private void onItemDespawn(ItemDespawnEvent event){
        if(!event.getEntity().getItemStack().hasItemMeta())  return;

        if(!event.getEntity().getItemStack().getItemMeta().getPersistentDataContainer().has(BackpackRecipes.IS_BACKPACK, PersistentDataType.INTEGER)){

            if(event.getEntity().getItemStack().getItemMeta().getPersistentDataContainer().has(UpgradesRecipes.IS_UPGRADE, PersistentDataType.INTEGER)){
                if(!Main.backPackManager.canOpen()){
                    event.setCancelled(true);
                    return;
                }

                int id = event.getEntity().getItemStack().getItemMeta().getPersistentDataContainer().get(UpgradesRecipes.UPGRADE_ID, PersistentDataType.INTEGER);
                UpgradeManager.getUpgrades().remove(id);
            }

            return;
        }

        if(!Main.backPackManager.canOpen()){
            event.setCancelled(true);
            return;
        }

        int id = event.getEntity().getItemStack().getItemMeta().getPersistentDataContainer().get(BackpackRecipes.BACKPACK_ID, PersistentDataType.INTEGER);
        if(!Main.backPackManager.getBackpacks().containsKey(id)) return;
        BackPack backPack = Main.backPackManager.getBackpacks().get(id);

        if(backPack.getFirstUpgradeFromType(UpgradeType.UNBREAKABLE) != null){
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
        Main.backPackManager.getBackpacks().remove(id);
    }
}