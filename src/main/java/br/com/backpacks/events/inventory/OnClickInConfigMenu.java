package br.com.backpacks.events.inventory;

import br.com.backpacks.Main;
import br.com.backpacks.events.upgrades.JukeboxUpgradeEvents;
import br.com.backpacks.recipes.BackpackRecipes;
import br.com.backpacks.recipes.RecipesUtils;
import br.com.backpacks.upgrades.JukeboxUpgrade;
import br.com.backpacks.utils.Upgrade;
import br.com.backpacks.utils.UpgradeManager;
import br.com.backpacks.utils.UpgradeType;
import br.com.backpacks.utils.backpacks.BackPack;
import br.com.backpacks.utils.backpacks.BackpackAction;
import br.com.backpacks.utils.backpacks.BackpackManager;
import br.com.backpacks.utils.inventory.InventoryBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Barrel;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.persistence.PersistentDataType;

public class OnClickInConfigMenu implements Listener {

    @EventHandler
    private void onClick(InventoryClickEvent event){
        if(event.getClickedInventory() == null) return;
        if(!BackpackAction.getAction(event.getWhoClicked()).equals(BackpackAction.Action.CONFIGMENU)) return;
        event.setCancelled(true);

        BackPack backPack = BackpackManager.getBackpackFromId(BackpackManager.getCurrentBackpackId().get(event.getWhoClicked().getUniqueId()));
        if(backPack == null) return;
        Player player = (Player) event.getWhoClicked();

        if(event.getRawSlot() < InventoryBuilder.getFreeUpgradesSlots(backPack.getType())){
            if(event.getCurrentItem() == null) return;
            Upgrade upgrade = RecipesUtils.getUpgradeFromItem(event.getCurrentItem());
            if(upgrade == null) return;
            if(!UpgradeManager.getUpgrades().containsKey(upgrade.getId())){
                UpgradeManager.getUpgrades().put(upgrade.getId(), upgrade);
            }

            switch (upgrade.getType()) {
                case CRAFTING -> {
                    BackpackAction.clearPlayerAction(player);
                    Bukkit.getScheduler().runTaskLater(Main.getMain(), ()->{
                        event.getWhoClicked().openWorkbench(null, true);
                        BackpackAction.setAction(player, BackpackAction.Action.UPGCRAFTINGGRID);
                        UpgradeManager.setPlayerCurrentUpgrade(player, upgrade.getId());
                    }, 1L);
                }

                case FURNACE, SMOKER, BLAST_FURNACE -> {
                    BackpackAction.clearPlayerAction(player);
                    Bukkit.getScheduler().runTaskLater(Main.getMain(), ()->{
                        event.getWhoClicked().openInventory(upgrade.getInventory());
                        BackpackAction.setAction(player, BackpackAction.Action.UPGFURNACE);
                        UpgradeManager.setPlayerCurrentUpgrade(player, upgrade.getId());
                    }, 1L);
                }

                case JUKEBOX -> {
                    BackpackAction.clearPlayerAction(player);
                    Bukkit.getScheduler().runTaskLater(Main.getMain(), ()->{
                        event.getWhoClicked().openInventory(upgrade.getInventory());
                        BackpackAction.setAction(player, BackpackAction.Action.UPGJUKEBOX);
                        UpgradeManager.setPlayerCurrentUpgrade(player, upgrade.getId());
                    }, 1L);
                }

                case AUTOFEED -> {
                    BackpackAction.clearPlayerAction(player);
                    Bukkit.getScheduler().runTaskLater(Main.getMain(), ()->{
                        event.getWhoClicked().openInventory(upgrade.getInventory());
                        BackpackAction.setAction(player, BackpackAction.Action.UPGAUTOFEED);
                        UpgradeManager.setPlayerCurrentUpgrade(player, upgrade.getId());
                    }, 1L);
                }

                case VILLAGERSFOLLOW -> {
                    BackpackAction.clearPlayerAction(player);
                    Bukkit.getScheduler().runTaskLater(Main.getMain(), ()->{
                        event.getWhoClicked().openInventory(upgrade.getInventory());
                        BackpackAction.setAction(player, BackpackAction.Action.UPGVILLAGERSFOLLOW);
                        UpgradeManager.setPlayerCurrentUpgrade(player, upgrade.getId());
                    }, 1L);
                }

                case COLLECTOR -> {
                    BackpackAction.clearPlayerAction(player);
                    Bukkit.getScheduler().runTaskLater(Main.getMain(), ()->{
                        event.getWhoClicked().openInventory(upgrade.getInventory());
                        BackpackAction.setAction(player, BackpackAction.Action.UPGCOLLECTOR);
                        UpgradeManager.setPlayerCurrentUpgrade(player, upgrade.getId());
                    }, 1L);
                }

                case LIQUIDTANK -> {
                    BackpackAction.clearPlayerAction(player);
                    Bukkit.getScheduler().runTaskLater(Main.getMain(), ()->{
                        event.getWhoClicked().openInventory(upgrade.getInventory());
                        BackpackAction.setAction(player, BackpackAction.Action.UPGTANKS);
                        UpgradeManager.setPlayerCurrentUpgrade(player, upgrade.getId());
                    }, 1L);
                }

                case ADVANCED_FILTER, FILTER -> {
                    BackpackAction.clearPlayerAction(player);
                    Bukkit.getScheduler().runTaskLater(Main.getMain(), ()->{
                        event.getWhoClicked().openInventory(upgrade.getInventory());
                        BackpackAction.setAction(player, BackpackAction.Action.UPGFILTER);
                        UpgradeManager.setPlayerCurrentUpgrade(player, upgrade.getId());
                    }, 1L);
                }
            }

            return;
        }
        if(BackpackAction.getSpectators().containsKey(event.getWhoClicked().getUniqueId())) return;

        switch (event.getRawSlot()) {
            //go back to the previous page
            case 45 -> Bukkit.getScheduler().runTaskLater(Main.getMain(), ()-> player.closeInventory(), 1L);

            //equip or un-equip backpack in the back
            case 53 -> {
                if(backPack.getLocation() != null)  return;
                if (backPack.getOwner() != null && backPack.getOwner().equals(player.getUniqueId())){
                    player.getInventory().addItem(RecipesUtils.getItemFromBackpack(backPack));
                    player.getPersistentDataContainer().remove(new BackpackRecipes().getHAS_BACKPACK());
                    if(backPack.getUpgradeFromType(UpgradeType.JUKEBOX) != null){
                        JukeboxUpgrade upgrade = (JukeboxUpgrade) backPack.getUpgradeFromType(UpgradeType.JUKEBOX);
                        if(upgrade.getSound() != null){
                            upgrade.clearLoopingTask();
                            JukeboxUpgradeEvents.stopSound(player, upgrade);
                        }
                    }

                    backPack.setOwner(null);
                } else if(backPack.getOwner() == null){
                    if(player.getPersistentDataContainer().has(new BackpackRecipes().getHAS_BACKPACK(), PersistentDataType.INTEGER)) return;
                    player.getInventory().remove(RecipesUtils.getItemFromBackpack(backPack));
                    player.getPersistentDataContainer().set(new BackpackRecipes().getHAS_BACKPACK(), PersistentDataType.INTEGER, backPack.getId());
                    backPack.setOwner(player.getUniqueId());
                }

                InventoryBuilder.updateConfigInv(backPack);
        }
            //rename backpack
            case 52 -> {
                backPack.getViewersIds().remove(player.getUniqueId());
                if(backPack.getViewersIds().isEmpty()){
                    if(backPack.getLocation() != null){
                        Barrel barrel = (Barrel) backPack.getLocation().getBlock().getState();
                        barrel.close();
                    }
                }
                BackpackAction.clearPlayerAction(player);
                BackpackAction.setAction(player, BackpackAction.Action.RENAMING);
                player.sendMessage(Main.PREFIX + "§eType the new name of the backpack");
                Bukkit.getScheduler().runTaskLater(Main.getMain(), ()-> player.closeInventory(), 1L);
            }

            case 51 -> {
                if (event.getClickedInventory().getItem(53).getType().equals(Material.GRAY_STAINED_GLASS_PANE)) return;
                backPack.setLocked(!backPack.isLocked());
                InventoryBuilder.updateConfigInv(backPack);
            }

            case 36 ->{
                BackpackAction.clearPlayerAction(player);
                Bukkit.getScheduler().runTaskLater(Main.getMain(), ()->{
                    player.openInventory(InventoryBuilder.getUpgradesInv(backPack));
                    BackpackAction.setAction(player, BackpackAction.Action.UPGMENU);
                }, 1L);
            }

            case 49 -> {
                BackpackAction.clearPlayerAction(player);
                Bukkit.getScheduler().runTaskLater(Main.getMain(), ()->{
                    player.openInventory(InventoryBuilder.getIOInv());
                    BackpackAction.setAction(player, BackpackAction.Action.IOMENU);
                }, 1L);
            }

            case 48 ->{
                if(backPack.getLocation() == null) return;
                if(!backPack.isShowingNameAbove()){
                    ArmorStand marker = (ArmorStand) player.getWorld().spawnEntity(backPack.getLocation().clone().add(0, 1, 0).toCenterLocation(), EntityType.ARMOR_STAND, CreatureSpawnEvent.SpawnReason.CUSTOM);
                    marker.setVisible(false);
                    marker.setSmall(true);
                    marker.customName(Component.text(backPack.getName()));
                    marker.setCustomNameVisible(true);
                    marker.setCanTick(false);
                    marker.setCanMove(false);
                    marker.setCollidable(false);
                    marker.setInvulnerable(true);
                    marker.setBasePlate(false);
                    marker.setMarker(true);
                    backPack.setMarkerId(marker.getUniqueId());
                    backPack.setShowNameAbove(true);
                }   else{
                    backPack.getMarkerEntity().remove();
                    backPack.setMarkerId(null);
                    backPack.setShowNameAbove(false);
                }
                InventoryBuilder.updateConfigInv(backPack);
            }
        }
    }
}