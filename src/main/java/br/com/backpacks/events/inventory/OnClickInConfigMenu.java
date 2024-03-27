package br.com.backpacks.events.inventory;

import br.com.backpacks.Main;
import br.com.backpacks.events.upgrades.Furnace;
import br.com.backpacks.events.upgrades.Jukebox;
import br.com.backpacks.recipes.BackpackRecipes;
import br.com.backpacks.recipes.RecipesUtils;
import br.com.backpacks.upgrades.FurnaceUpgrade;
import br.com.backpacks.upgrades.JukeboxUpgrade;
import br.com.backpacks.utils.Upgrade;
import br.com.backpacks.utils.UpgradeManager;
import br.com.backpacks.utils.UpgradeType;
import br.com.backpacks.utils.backpacks.BackPack;
import br.com.backpacks.utils.backpacks.BackpackAction;
import br.com.backpacks.utils.inventory.InventoryBuilder;
import net.kyori.adventure.text.Component;
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
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class OnClickInConfigMenu implements Listener {

    @EventHandler
    private void onClick(InventoryClickEvent event){
        if(event.getClickedInventory() == null) return;
        if(!BackpackAction.getAction(event.getWhoClicked()).equals(BackpackAction.Action.CONFIGMENU)) return;
        event.setCancelled(true);

        BackPack backPack = Main.backPackManager.getBackpackFromId(Main.backPackManager.getCurrentBackpackId().get(event.getWhoClicked().getUniqueId()));
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
                    event.getWhoClicked().openWorkbench(null, true);
                    BackpackAction.setAction(player, BackpackAction.Action.UPGCRAFTINGGRID);
                    event.setCancelled(true);
                }

                case FURNACE, SMOKER, BLAST_FURNACE -> {
                    BackpackAction.clearPlayerAction(player);
                    event.getWhoClicked().openInventory(upgrade.getInventory());
                    BukkitTask task = new BukkitRunnable(){
                        @Override
                        public void run() {
                            BackpackAction.setAction(player, BackpackAction.Action.UPGFURNACE);
                            Furnace.currentFurnace.put(player.getUniqueId(), ((FurnaceUpgrade) upgrade));
                        }
                    }.runTaskLater(Main.getMain(), 1L);
                    event.setCancelled(true);
                }

                case JUKEBOX -> {
                    BackpackAction.clearPlayerAction(player);
                    event.getWhoClicked().openInventory(upgrade.getInventory());
                    BukkitTask task = new BukkitRunnable(){
                        @Override
                        public void run() {
                            BackpackAction.setAction(player, BackpackAction.Action.UPGJUKEBOX);
                        }
                    }.runTaskLater(Main.getMain(), 1L);
                    event.setCancelled(true);
                }

                case AUTOFEED -> {
                    BackpackAction.clearPlayerAction(player);
                    event.getWhoClicked().openInventory(upgrade.getInventory());
                    BukkitTask task = new BukkitRunnable(){
                        @Override
                        public void run() {
                            BackpackAction.setAction(player, BackpackAction.Action.UPGAUTOFEED);
                        }
                    }.runTaskLater(Main.getMain(), 1L);
                    event.setCancelled(true);
                }

                case VILLAGERSFOLLOW -> {
                    BackpackAction.clearPlayerAction(player);
                    event.getWhoClicked().openInventory(upgrade.getInventory());
                    BukkitTask task = new BukkitRunnable(){
                        @Override
                        public void run() {
                            BackpackAction.setAction(player, BackpackAction.Action.UPGVILLAGERSFOLLOW);
                        }
                    }.runTaskLater(Main.getMain(), 1L);
                    event.setCancelled(true);
                }

                case COLLECTOR -> {
                    BackpackAction.clearPlayerAction(player);
                    event.getWhoClicked().openInventory(upgrade.getInventory());
                    BukkitTask task = new BukkitRunnable(){
                        @Override
                        public void run() {
                            BackpackAction.setAction(player, BackpackAction.Action.UPGCOLLECTOR);
                        }
                    }.runTaskLater(Main.getMain(), 1L);
                    event.setCancelled(true);
                }

                case LIQUIDTANK -> {
                    BackpackAction.clearPlayerAction(player);
                    event.getWhoClicked().openInventory(upgrade.getInventory());
                    BukkitTask task = new BukkitRunnable(){
                        @Override
                        public void run() {
                            BackpackAction.setAction(player, BackpackAction.Action.UPGTANKS);
                        }
                    }.runTaskLater(Main.getMain(), 1L);
                    event.setCancelled(true);
                }
            }

            return;
        }
        if(BackpackAction.getSpectators().containsKey(event.getWhoClicked().getUniqueId())) return;

        switch (event.getRawSlot()) {
            //go back to the previous page
            case 45 -> player.closeInventory();
            //equip or un-equip backpack in the back
            case 53 -> {
                if(backPack.isBlock())  return;
                if (backPack.getOwner() != null && backPack.getOwner().equals(player.getUniqueId())){
                    player.getInventory().addItem(RecipesUtils.getItemFromBackpack(backPack));
                    player.getPersistentDataContainer().remove(new BackpackRecipes().getHAS_BACKPACK());
                    if(backPack.getUpgradeFromType(UpgradeType.JUKEBOX) != null){
                        JukeboxUpgrade upgrade = (JukeboxUpgrade) backPack.getUpgradeFromType(UpgradeType.JUKEBOX);
                        if(upgrade.getSound() != null){
                            upgrade.clearLoopingTask();
                            Jukebox.stopSound(player, upgrade);
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
                    if(backPack.isBlock()){
                        Barrel barrel = (Barrel) backPack.getLocation().getBlock().getState();
                        barrel.close();
                    }
                }
                BackpackAction.clearPlayerAction(player);
                BackpackAction.setAction(player, BackpackAction.Action.RENAMING);
                player.sendMessage(Main.PREFIX + "§eType the new name of the backpack");
                player.closeInventory();
            }

            case 51 -> {
                if (event.getClickedInventory().getItem(53).getType().equals(Material.GRAY_STAINED_GLASS_PANE)) return;
                backPack.setLocked(!backPack.isLocked());
                InventoryBuilder.updateConfigInv(backPack);
            }

            case 36 ->{
                BackpackAction.clearPlayerAction(player);
                player.openInventory(InventoryBuilder.getUpgradesInv(backPack));
                BackpackAction.setAction(player, BackpackAction.Action.UPGMENU);
                event.setCancelled(true);
            }

            case 49 -> {
                BackpackAction.clearPlayerAction(player);
                player.openInventory(InventoryBuilder.getIOInv());
                BackpackAction.setAction(player, BackpackAction.Action.IOMENU);
                event.setCancelled(true);
            }

            case 48 ->{
                if(!backPack.isBlock()) return;
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
                    backPack.setMarker(marker.getUniqueId());
                    backPack.setShowNameAbove(true);
                }   else{
                    backPack.getMarkerEntity().remove();
                    backPack.setMarker(null);
                    backPack.setShowNameAbove(false);
                }
                InventoryBuilder.updateConfigInv(backPack);
            }
        }
    }
}