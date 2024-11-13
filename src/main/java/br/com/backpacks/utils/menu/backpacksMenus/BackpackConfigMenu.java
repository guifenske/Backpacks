package br.com.backpacks.utils.menu.backpacksMenus;

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
import br.com.backpacks.utils.menu.Button;
import br.com.backpacks.utils.menu.DynamicMenu;
import br.com.backpacks.utils.menu.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Barrel;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Arrays;
import java.util.List;

public class BackpackConfigMenu extends DynamicMenu {
    private final BackPack backPack;

    public BackpackConfigMenu(int size, String title, BackPack backPack){
        super(size, title);
        this.backPack = backPack;

       refreshMenu();
    }

    public void refreshMenu(){
        ItemStack blank = new ItemCreator(Material.GRAY_STAINED_GLASS_PANE, " ").build();
        ItemStack equipBackpack = new ItemCreator(Material.CHEST, "Equip Backpack").build();
        ItemStack unequipBackpack = new ItemCreator(Material.ENDER_CHEST, "Unequip Backpack").build();
        ItemStack close = new ItemCreator(Material.BARRIER, "Close").build();
        ItemStack rename = new ItemCreator(Material.NAME_TAG, "Rename Backpack").build();
        ItemStack lock = new ItemCreator(Material.WRITABLE_BOOK, "Lock Backpack", Arrays.asList("§7§nLock the access to this backpack", "§7§n from other players when in your back.")).build();
        ItemStack unlock = new ItemCreator(Material.WRITTEN_BOOK, "Unlock Backpack", Arrays.asList("§7§nUnlock the access to this backpack", "§7§n from other players when in your back.")).build();
        ItemStack editUpgrades = new ItemCreator(Material.ANVIL, "Edit Upgrades").build();
        ItemStack editIO = new ItemCreator(Material.HOPPER, "Sets the I/O inventory", Arrays.asList("§7Select upgrades to be the I/O inventories.")).build();
        ItemStack enableNameAbove = new ItemCreator(Material.POTATO, "Show name above backpack block").build();
        ItemStack disableNameAbove = new ItemCreator(Material.POISONOUS_POTATO, "Don't show name above backpack block").build();

        addUpgradesInView();

        for(int i = backPack.getType().getMaxUpgrades(); i < inventory.getSize(); i++){
            addButton(new Button(i) {
                @Override
                public ItemStack getItem() {
                    return blank;
                }

                @Override
                public void onClick(Player player) {
                }
            });
        }

        if(!backPack.isBlock()) {

            if(backPack.getOwner() != null){
                addButton(new Button(51) {

                    @Override
                    public ItemStack getItem() {
                        if(backPack.isLocked()) {
                            return unlock;
                        }

                        return lock;
                    }

                    @Override
                    public void onClick(Player player) {
                        backPack.setLocked(!backPack.isLocked());
                        refreshButtonInMenu(this);
                    }
                });
            }

            addButton(new Button(53) {
                @Override
                public ItemStack getItem() {

                    if (backPack.getOwner() != null) {
                        return unequipBackpack;
                    }

                    return equipBackpack;
                }

                @Override
                public void onClick(Player player) {
                    if(backPack.isBlock())  return;

                    if (backPack.getOwner() != null && backPack.getOwner().equals(player.getUniqueId())){
                        player.getInventory().addItem(RecipesUtils.getItemFromBackpack(backPack));
                        player.getPersistentDataContainer().remove(BackpackRecipes.HAS_BACKPACK);

                        if(backPack.getFirstUpgradeFromType(UpgradeType.JUKEBOX) != null){
                            JukeboxUpgrade upgrade = (JukeboxUpgrade) backPack.getFirstUpgradeFromType(UpgradeType.JUKEBOX);
                            if(upgrade.getSound() != null){
                                upgrade.clearLoopingTask();
                                Jukebox.stopSound(player, upgrade);
                            }
                        }

                        backPack.setOwner(null);
                    }

                    else if(backPack.getOwner() == null){
                        if(player.getPersistentDataContainer().has(BackpackRecipes.HAS_BACKPACK)) return;
                        backPack.removeBackpackItem(player);

                        player.getPersistentDataContainer().set(BackpackRecipes.HAS_BACKPACK, PersistentDataType.INTEGER, backPack.getId());
                        backPack.setOwner(player.getUniqueId());
                    }

                    refreshMenu();
                }
            });

        }   else{

            addButton(new Button(48) {
                @Override
                public ItemStack getItem() {

                    if(!backPack.isShowingNameAbove()){
                        return enableNameAbove;
                    }

                    return disableNameAbove;
                }

                @Override
                public void onClick(Player player) {
                    if(!backPack.isShowingNameAbove()){
                        ArmorStand marker = (ArmorStand) player.getWorld().spawnEntity(backPack.getLocation().clone().add(0.5, 1, 0.5), EntityType.ARMOR_STAND);

                        marker.setVisible(false);
                        marker.setSmall(true);
                        marker.setCustomName(backPack.getName());
                        marker.setCustomNameVisible(true);
                        marker.setCollidable(false);
                        marker.setInvulnerable(true);
                        marker.setBasePlate(false);
                        marker.setMarker(true);
                        marker.setPersistent(true);

                        backPack.setMarker(marker.getUniqueId());
                        backPack.setShowNameAbove(true);
                    }

                    else{
                        backPack.getMarkerEntity().remove();
                        backPack.setMarker(null);
                        backPack.setShowNameAbove(false);
                    }

                    refreshButtonInMenu(this);
                }
            });
        }

        addButton(new Button(36) {
            @Override
            public ItemStack getItem() {
                return editUpgrades;
            }

            @Override
            public void onClick(Player player) {
                BackpackAction.clearPlayerAction(player);
                backPack.getUpgradesMenu().displayTo(player);
                BackpackAction.setAction(player, BackpackAction.Action.UPGMENU);
            }
        });

        addButton(new Button(45) {
            @Override
            public ItemStack getItem() {
                return close;
            }

            @Override
            public void onClick(Player player) {
                player.closeInventory();
            }
        });

        addButton(new Button(49) {
            @Override
            public ItemStack getItem() {
                return editIO;
            }

            @Override
            public void onClick(Player player) {
                BackpackAction.clearPlayerAction(player);
                backPack.getUpgradesInputOutputMenu().displayTo(player);
                BackpackAction.setAction(player, BackpackAction.Action.IOMENU);
            }
        });

        addButton(new Button(52) {
            @Override
            public ItemStack getItem() {
                return rename;
            }

            @Override
            public void onClick(Player player) {
                backPack.getViewersIds().remove(player.getUniqueId());

                if(backPack.getViewersIds().isEmpty() && backPack.isBlock()){
                    Barrel barrel = (Barrel) backPack.getLocation().getBlock().getState();
                    barrel.close();
                }

                BackpackAction.clearPlayerAction(player);
                BackpackAction.setAction(player, BackpackAction.Action.RENAMING);

                player.sendMessage(Main.getMain().PREFIX + "§eType the new name of the backpack");
                player.closeInventory();
            }
        });
    }

    public void addUpgradesInView(){
        for(int i = 0; i < backPack.getType().getMaxUpgrades(); i++){
            inventory.setItem(i, null);
        }

        if(!backPack.getBackpackUpgrades().isEmpty()) {
            List<Upgrade> upgrades = backPack.getBackpackUpgrades();

            int i = 0;
            for(Upgrade upgrade : upgrades) {
                if(upgrade == null){
                    i++;
                    continue;
                }

                addButton(new Button(i) {
                    @Override
                    public ItemStack getItem() {
                        return RecipesUtils.getItemFromUpgrade(upgrade);
                    }

                    @Override
                    public void onClick(Player player) {
                        if(!UpgradeManager.getUpgrades().containsKey(upgrade.getId())){
                            UpgradeManager.getUpgrades().put(upgrade.getId(), upgrade);
                        }

                        if(upgrade.getType().equals(UpgradeType.CRAFTING_GRID)){
                            BackpackAction.clearPlayerAction(player);
                            player.openWorkbench(null, true);
                        }

                        else{
                            if(upgrade.getInventory() == null){
                                return;
                            }

                            BackpackAction.clearPlayerAction(player);
                            player.openInventory(upgrade.getInventory());
                        }


                        Bukkit.getScheduler().runTaskLater(Main.getMain(), ()->{
                            BackpackAction.setAction(player, upgrade.getType().getAction());

                            if(upgrade.getType().equals(UpgradeType.FURNACE)){
                                Furnace.currentFurnace.put(player.getUniqueId(), ((FurnaceUpgrade) upgrade));

                                Bukkit.getScheduler().runTaskLater(Main.getMain(), ()->{
                                    FurnaceUpgrade furnaceUpgrade = (FurnaceUpgrade) upgrade;

                                    for(Player player1 : Bukkit.getOnlinePlayers()){
                                        player1.sendBlockChange(furnaceUpgrade.getFurnace().getLocation(), Material.AIR.createBlockData());
                                    }

                                }, 1L);
                            }
                        }, 1L);

                    }
                });
                i++;
            }
        }
    }

    @Override
    public void onClose(Player player, BackPack backPack) {
        BackpackAction.clearPlayerAction(player);

        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                backPack.open(player);
            }
        }.runTaskLater(Main.getMain(), 1L);
    }
}
