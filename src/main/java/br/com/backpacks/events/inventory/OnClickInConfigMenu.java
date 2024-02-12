package br.com.backpacks.events.inventory;

import br.com.backpacks.Main;
import br.com.backpacks.events.upgrades.Furnace;
import br.com.backpacks.events.upgrades.Jukebox;
import br.com.backpacks.recipes.RecipesNamespaces;
import br.com.backpacks.recipes.RecipesUtils;
import br.com.backpacks.upgrades.FurnaceUpgrade;
import br.com.backpacks.upgrades.JukeboxUpgrade;
import br.com.backpacks.utils.BackPack;
import br.com.backpacks.utils.BackpackAction;
import br.com.backpacks.utils.Upgrade;
import br.com.backpacks.utils.UpgradeType;
import br.com.backpacks.utils.inventory.InventoryBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class OnClickInConfigMenu implements Listener {

    @EventHandler
    private void onClick(InventoryClickEvent event){
        if(event.getClickedInventory() == null) return;
        if(!BackpackAction.getAction((Player) event.getWhoClicked()).equals(BackpackAction.Action.CONFIGMENU)) return;
        event.setCancelled(true);

        BackPack backPack = Main.backPackManager.getBackpackFromId(Main.backPackManager.getCurrentBackpackId().get(event.getWhoClicked().getUniqueId()));
        if(backPack == null) return;
        Player player = (Player) event.getWhoClicked();

        if(event.getRawSlot() < InventoryBuilder.getFreeUpgradesSlots(backPack.getType())){
            if(event.getCurrentItem() == null) return;
            Upgrade upgrade = RecipesUtils.getUpgradeFromItem(event.getCurrentItem());
            if(upgrade == null) return;
            if(!Main.backPackManager.getUpgradeHashMap().containsKey(upgrade.getId())){
                Main.backPackManager.getUpgradeHashMap().put(upgrade.getId(), upgrade);
            }

            switch (upgrade.getType()) {
                case CRAFTING -> {
                    BackpackAction.removeAction(player);
                    event.getWhoClicked().openWorkbench(null, true);
                    BackpackAction.setAction(player, BackpackAction.Action.UPGCRAFTINGGRID);
                    event.setCancelled(true);
                }

                case FURNACE, SMOKER, BLAST_FURNACE -> {
                    BackpackAction.removeAction(player);
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
                    BackpackAction.removeAction(player);
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
                    BackpackAction.removeAction(player);
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
                    BackpackAction.removeAction(player);
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
                    BackpackAction.removeAction(player);
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
                    BackpackAction.removeAction(player);
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

        switch (event.getRawSlot()) {
            //go back to the previous page
            case 45 -> player.closeInventory();
            //equip or un-equip backpack in the back
            case 53 -> {
                if(backPack.isBlock())  return;
                if(event.getClickedInventory().getItem(53).getType().equals(Material.GRAY_STAINED_GLASS_PANE)) return;

                if (backPack.getOwner() != null && backPack.getOwner().equals(player.getUniqueId())){
                    player.getInventory().addItem(RecipesUtils.getItemFromBackpack(backPack));
                    player.getPersistentDataContainer().remove(new RecipesNamespaces().getHAS_BACKPACK());
                    if(!backPack.getUpgradesFromType(UpgradeType.JUKEBOX).isEmpty()){
                        JukeboxUpgrade upgrade = (JukeboxUpgrade) backPack.getUpgradesFromType(UpgradeType.JUKEBOX).get(0);
                        if(upgrade.getSound() != null){
                            upgrade.clearLoopingTask();
                            Jukebox.stopSound(player, upgrade);
                        }
                    }
                    backPack.setOwner(null);
                } else if(backPack.getOwner() == null){
                    player.getInventory().remove(RecipesUtils.getItemFromBackpack(backPack));
                    player.getPersistentDataContainer().set(new RecipesNamespaces().getHAS_BACKPACK(), PersistentDataType.INTEGER, backPack.getId());
                    backPack.setOwner(player.getUniqueId());
                }

                InventoryBuilder.updateConfigInv(backPack);
        }
            //rename backpack
            case 52 -> {
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
                BackpackAction.removeAction(player);
                player.openInventory(InventoryBuilder.getUpgradesInv(backPack));
                BackpackAction.setAction(player, BackpackAction.Action.UPGMENU);
                event.setCancelled(true);
            }

            case 49 -> {
                BackpackAction.removeAction(player);
                player.openInventory(InventoryBuilder.getIOInv());
                BackpackAction.setAction(player, BackpackAction.Action.IOMENU);
                event.setCancelled(true);
            }
        }
    }
}