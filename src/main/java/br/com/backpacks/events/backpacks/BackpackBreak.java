package br.com.backpacks.events.backpacks;

import br.com.backpacks.Main;
import br.com.backpacks.events.upgrades.Jukebox;
import br.com.backpacks.recipes.BackpackRecipes;
import br.com.backpacks.recipes.RecipesUtils;
import br.com.backpacks.recipes.UpgradesRecipes;
import br.com.backpacks.upgrades.JukeboxUpgrade;
import br.com.backpacks.upgrades.UpgradeManager;
import br.com.backpacks.upgrades.UpgradeType;
import br.com.backpacks.backpack.Backpack;
import br.com.backpacks.backpack.BackpackAction;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class BackpackBreak implements Listener {

    @EventHandler(ignoreCancelled = true)
    private void playerBreak(BlockBreakEvent event){

        if(!event.getBlock().getType().equals(Material.BARREL)) return;

        if(Main.backpackManager.getBackpackFromLocation(event.getBlock().getLocation()) == null) return;

        event.setCancelled(true);

        Backpack backpack = Main.backpackManager.getBackpackFromLocation(event.getBlock().getLocation());

        if(backpack.getFirstUpgradeFromType(UpgradeType.JUKEBOX) != null){
            JukeboxUpgrade upgrade = (JukeboxUpgrade) backpack.getFirstUpgradeFromType(UpgradeType.JUKEBOX);

            if(upgrade.getSound() != null){
                upgrade.clearParticleTask();
                upgrade.clearLoopingTask();
                Jukebox.stopSound(backpack, upgrade);
            }
        }

        for(UUID uuid : backpack.getViewersIds()){
            Player player = Bukkit.getPlayer(uuid);
            if(player == null) continue;

            BackpackAction.clearPlayerAction(player);
            BackpackAction.getSpectators().remove(uuid);

            Main.backpackManager.clearPlayerCurrentPage(player);
            Main.backpackManager.clearPlayerCurrentBackpack(player);

            backpack.getViewersIds().remove(uuid);
            player.closeInventory();
        }

        backpack.setShowNameAbove(false);
        if(backpack.getMarker() != null) backpack.getMarkerEntity().remove();
        backpack.setMarker(null);
        backpack.setLocation(null);
        backpack.setIsBlock(false);
        backpack.setOwner(null);

        backpack.getConfigMenu().refreshMenu();

        Location location = event.getBlock().getLocation();
        Main.backpackManager.getBackpacksPlacedLocations().remove(location);
        event.getBlock().setType(Material.AIR, true);
        event.getPlayer().getWorld().dropItemNaturally(location, RecipesUtils.getItemFromBackpack(backpack)).setThrower(event.getPlayer().getUniqueId());
    }

    @EventHandler(ignoreCancelled = true)
    private void explosionBreak(EntityExplodeEvent event){
        for(Block block : event.blockList()){
            if(!block.getType().equals(Material.BARREL)) continue;

            if(Main.backpackManager.getBackpackFromLocation(block.getLocation()) == null) continue;

            event.blockList().remove(block);
            block.setType(Material.AIR);

            Backpack backpack = Main.backpackManager.getBackpackFromLocation(block.getLocation());

            Location location = block.getLocation();
            if(backpack.getFirstUpgradeFromType(UpgradeType.JUKEBOX) != null){
                JukeboxUpgrade upgrade = (JukeboxUpgrade) backpack.getFirstUpgradeFromType(UpgradeType.JUKEBOX);
                if(upgrade.getSound() != null){
                    upgrade.clearParticleTask();
                    upgrade.clearLoopingTask();
                    Jukebox.stopSound(backpack, upgrade);
                }
            }

            for(UUID uuid : backpack.getViewersIds()){
                Player player = Bukkit.getPlayer(uuid);
                if(player == null) continue;

                BackpackAction.clearPlayerAction(player);
                BackpackAction.getSpectators().remove(uuid);

                Main.backpackManager.clearPlayerCurrentPage(player);
                Main.backpackManager.clearPlayerCurrentBackpack(player);

                backpack.getViewersIds().remove(uuid);
                player.closeInventory();
            }

            backpack.setShowNameAbove(false);
            if(backpack.getMarker() != null) backpack.getMarkerEntity().remove();

            backpack.setMarker(null);
            backpack.setLocation(null);
            backpack.setIsBlock(false);
            backpack.setOwner(null);

            backpack.getConfigMenu().refreshMenu();

            Main.backpackManager.getBackpacksPlacedLocations().remove(location);
            block.getWorld().dropItemNaturally(location, RecipesUtils.getItemFromBackpack(backpack));
            break;
       }
    }

    @EventHandler(ignoreCancelled = true)
    private void onItemDespawn(ItemDespawnEvent event){
        if(!event.getEntity().getItemStack().hasItemMeta())  return;

        if(!event.getEntity().getItemStack().getItemMeta().getPersistentDataContainer().has(BackpackRecipes.IS_BACKPACK)){

            if(event.getEntity().getItemStack().getItemMeta().getPersistentDataContainer().has(UpgradesRecipes.IS_UPGRADE)){

                int id = event.getEntity().getItemStack().getItemMeta().getPersistentDataContainer().get(UpgradesRecipes.UPGRADE_ID, PersistentDataType.INTEGER);

                UpgradeManager.getUpgradeFromId(id).stopTicking();
                UpgradeManager.getUpgrades().remove(id);
            }

            return;
        }

        int id = event.getEntity().getItemStack().getItemMeta().getPersistentDataContainer().get(BackpackRecipes.BACKPACK_ID, PersistentDataType.INTEGER);
        if(!Main.backpackManager.getBackpacks().containsKey(id)) return;
        Backpack backpack = Main.backpackManager.getBackpacks().get(id);

        if(backpack.getFirstUpgradeFromType(UpgradeType.UNBREAKABLE) != null){
            event.setCancelled(true);
            event.getEntity().setInvulnerable(true);
            return;
        }

        if(!backpack.getUpgradesIds().isEmpty()){
            for(int i : backpack.getUpgradesIds()){
                UpgradeManager.getUpgradeFromId(i).stopTicking();
                UpgradeManager.getUpgrades().remove(i);
            }
        }

        Main.backpackManager.getBackpacks().remove(id);
    }
}