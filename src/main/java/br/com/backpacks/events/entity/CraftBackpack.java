package br.com.backpacks.events.entity;

import br.com.backpacks.Main;
import br.com.backpacks.recipes.BackpackRecipes;
import br.com.backpacks.recipes.RecipesUtils;
import br.com.backpacks.recipes.UpgradesRecipes;
import br.com.backpacks.utils.Upgrade;
import br.com.backpacks.utils.UpgradeManager;
import br.com.backpacks.utils.UpgradeType;
import br.com.backpacks.utils.backpacks.BackPack;
import br.com.backpacks.utils.backpacks.BackpackManager;
import br.com.backpacks.utils.backpacks.BackpackType;
import br.com.backpacks.utils.inventory.InventoryBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class CraftBackpack implements Listener {

    private final String DONTHAVEBACKPACKMSG =  Main.PREFIX + "§cIt looks that you don't have a backpack to upgrade in the recipe!";
    private final String NOTCORRECTTYPE =  Main.PREFIX + "§cYou don't have the correct type of backpack to upgrade!";

    @EventHandler
    private void craftBackpackEvent(CraftItemEvent event){
        if(!event.getRecipe().getResult().hasItemMeta()){
            switch (event.getRecipe().getResult().getType()){
                case CHEST, BARREL, COMPOSTER -> {
                    if(!event.getWhoClicked().hasDiscoveredRecipe(new BackpackRecipes().getNAMESPACE_LEATHER_BACKPACK())){
                        event.getWhoClicked().discoverRecipe(new BackpackRecipes().getNAMESPACE_LEATHER_BACKPACK());
                    }   else{
                        event.getWhoClicked().discoverRecipe(new UpgradesRecipes().getCollector());
                    }
                }
                case FURNACE, SMOKER, BLAST_FURNACE -> {
                    event.getWhoClicked().discoverRecipe(new UpgradesRecipes().getFurnace());
                }
                case JUKEBOX, NOTE_BLOCK -> {
                    event.getWhoClicked().discoverRecipe(new UpgradesRecipes().getJukebox());
                }
                case GOLDEN_CARROT, GOLDEN_APPLE, BREAD, HAY_BLOCK, BEETROOT_SOUP, RABBIT_STEW, MUSHROOM_STEW, SUSPICIOUS_STEW -> {
                    event.getWhoClicked().discoverRecipe(new UpgradesRecipes().getAutoFeed());
                }
                case SMITHING_TABLE, FLETCHING_TABLE, CARTOGRAPHY_TABLE, LECTERN, EMERALD_BLOCK, BREWING_STAND -> {
                    event.getWhoClicked().discoverRecipe(new UpgradesRecipes().getVillagersFollow());
                }
                case END_CRYSTAL, BEACON, NETHERITE_INGOT -> {
                    event.getWhoClicked().discoverRecipe(new UpgradesRecipes().getUnbreaking());
                    event.getWhoClicked().discoverRecipe(new UpgradesRecipes().getEncapsulate());
                }
                case BUCKET, CAULDRON -> {
                    event.getWhoClicked().discoverRecipe(new UpgradesRecipes().getLiquidTank());
                }
            }
            return;
        }

        Player player = (Player) event.getWhoClicked();
        if(event.getRecipe().getResult().getItemMeta().getPersistentDataContainer().has(new UpgradesRecipes().isUpgrade(), PersistentDataType.INTEGER)){
            if(event.getRecipe().getResult().getItemMeta().getPersistentDataContainer().has(new UpgradesRecipes().getAdvancedFilter(), PersistentDataType.INTEGER)){
                if(!checkUpgradeInMatrix(event, UpgradeType.FILTER)){
                    event.setCancelled(true);
                    event.getWhoClicked().sendMessage(Main.PREFIX + "§cYou don't have a normal upgrade to make a advanced one!");
                    return;
                }
            }

            ItemStack itemStack = event.getRecipe().getResult();
            ItemMeta meta = itemStack.getItemMeta();
            meta.getPersistentDataContainer().set(new UpgradesRecipes().getUPGRADE_ID(), PersistentDataType.INTEGER, UpgradeManager.lastUpgradeID + 1);
            itemStack.setItemMeta(meta);
            event.getInventory().setResult(itemStack);
            UpgradeManager.lastUpgradeID++;
            RecipesUtils.getUpgradeFromItem(itemStack);
        }

        int oldId = -1;
        if(!event.getRecipe().getResult().getItemMeta().getPersistentDataContainer().has(new BackpackRecipes().isBackpack(), PersistentDataType.INTEGER)) return;

        if(event.getRecipe().getResult().getItemMeta().getPersistentDataContainer().has(new BackpackRecipes().getNAMESPACE_LEATHER_BACKPACK(), PersistentDataType.INTEGER)){
            int id = BackpackManager.lastBackpackID + 1;
            player.discoverRecipe(new BackpackRecipes().getNAMESPACE_IRON_BACKPACK());
            player.discoverRecipe(new UpgradesRecipes().getCraftingGrid());
            BackPack backPack =  new BackPack("Leather Backpack", Bukkit.createInventory(null, 18, "Leather Backpack"), id, BackpackType.LEATHER);
            updateResult(event, id);
            BackpackManager.getBackpacks().put(id, backPack);
            BackpackManager.lastBackpackID++;
            new InventoryBuilder(InventoryBuilder.MenuType.CONFIG, backPack).build();
            new InventoryBuilder(InventoryBuilder.MenuType.UPGMENU, backPack).build();
            new InventoryBuilder(InventoryBuilder.MenuType.EDIT_IO_MENU, backPack).build();
            return;
        }

        if(event.getRecipe().getResult().getItemMeta().getPersistentDataContainer().has(new BackpackRecipes().getNAMESPACE_IRON_BACKPACK(), PersistentDataType.INTEGER)){
            oldId = getBackpackIdInMatrix(event, oldId, BackpackType.IRON);
            if(oldId == -1) return;
            player.discoverRecipe(new BackpackRecipes().getNAMESPACE_GOLD_BACKPACK());
            player.discoverRecipe(new UpgradesRecipes().getEncapsulate());
            updateResult(event, oldId);
            BackpackManager.lastBackpackID++;
            BackPack backPack = BackpackManager.upgradeBackpack(BackpackType.LEATHER, oldId);
            new InventoryBuilder(InventoryBuilder.MenuType.CONFIG, backPack).build();
            new InventoryBuilder(InventoryBuilder.MenuType.UPGMENU, backPack).build();
            new InventoryBuilder(InventoryBuilder.MenuType.EDIT_IO_MENU, backPack).build();
            return;
        }

        if(event.getRecipe().getResult().getItemMeta().getPersistentDataContainer().has(new BackpackRecipes().getNAMESPACE_GOLD_BACKPACK(), PersistentDataType.INTEGER)){
            oldId = getBackpackIdInMatrix(event, oldId, BackpackType.GOLD);
            if(oldId == -1) return;
            player.discoverRecipe(new BackpackRecipes().getNAMESPACE_LAPIS_BACKPACK());
            player.discoverRecipe(new UpgradesRecipes().getLiquidTank());
            updateResult(event, oldId);
            BackpackManager.lastBackpackID++;
            BackPack backPack = BackpackManager.upgradeBackpack(BackpackType.IRON, oldId);
            new InventoryBuilder(InventoryBuilder.MenuType.CONFIG, backPack).build();
            new InventoryBuilder(InventoryBuilder.MenuType.UPGMENU, backPack).build();
            new InventoryBuilder(InventoryBuilder.MenuType.EDIT_IO_MENU, backPack).build();
            return;
        }

        if(event.getRecipe().getResult().getItemMeta().getPersistentDataContainer().has(new BackpackRecipes().getNAMESPACE_LAPIS_BACKPACK(), PersistentDataType.INTEGER)){
            oldId = getBackpackIdInMatrix(event, oldId, BackpackType.LAPIS);
            if(oldId == -1) return;
            player.discoverRecipe(new BackpackRecipes().getNAMESPACE_AMETHYST_BACKPACK());
            player.discoverRecipe(new UpgradesRecipes().getUnbreaking());
            updateResult(event, oldId);
            BackpackManager.lastBackpackID++;
            BackPack backPack = BackpackManager.upgradeBackpack(BackpackType.GOLD, oldId);
            new InventoryBuilder(InventoryBuilder.MenuType.CONFIG, backPack).build();
            new InventoryBuilder(InventoryBuilder.MenuType.UPGMENU, backPack).build();
            new InventoryBuilder(InventoryBuilder.MenuType.EDIT_IO_MENU, backPack).build();
            return;
        }

        if(event.getRecipe().getResult().getItemMeta().getPersistentDataContainer().has(new BackpackRecipes().getNAMESPACE_AMETHYST_BACKPACK(), PersistentDataType.INTEGER)){
            oldId = getBackpackIdInMatrix(event, oldId, BackpackType.AMETHYST);
            if(oldId == -1) return;
            player.discoverRecipe(new BackpackRecipes().getNAMESPACE_DIAMOND_BACKPACK());
            updateResult(event, oldId);
            BackpackManager.lastBackpackID++;
            BackPack backPack = BackpackManager.upgradeBackpack(BackpackType.LAPIS, oldId);
            new InventoryBuilder(InventoryBuilder.MenuType.CONFIG, backPack).build();
            new InventoryBuilder(InventoryBuilder.MenuType.UPGMENU, backPack).build();
            new InventoryBuilder(InventoryBuilder.MenuType.EDIT_IO_MENU, backPack).build();
            return;
        }

        if(event.getRecipe().getResult().getItemMeta().getPersistentDataContainer().has(new BackpackRecipes().getNAMESPACE_DIAMOND_BACKPACK(), PersistentDataType.INTEGER)){
            oldId = getBackpackIdInMatrix(event, oldId, BackpackType.DIAMOND);
            if(oldId == -1) return;
            player.discoverRecipe(new BackpackRecipes().getNAMESPACE_NETHERITE_BACKPACK());
            updateResult(event, oldId);
            BackpackManager.lastBackpackID++;
            BackPack backPack = BackpackManager.upgradeBackpack(BackpackType.AMETHYST, oldId);
            new InventoryBuilder(InventoryBuilder.MenuType.CONFIG, backPack).build();
            new InventoryBuilder(InventoryBuilder.MenuType.UPGMENU, backPack).build();
            new InventoryBuilder(InventoryBuilder.MenuType.EDIT_IO_MENU, backPack).build();
            return;
        }

        if(event.getRecipe().getResult().getItemMeta().getPersistentDataContainer().has(new BackpackRecipes().getNAMESPACE_NETHERITE_BACKPACK(), PersistentDataType.INTEGER)){
            oldId = getBackpackIdInMatrix(event, oldId, BackpackType.NETHERITE);
            if(oldId == -1) return;
            updateResult(event, oldId);
            BackpackManager.lastBackpackID++;
            BackPack backPack = BackpackManager.upgradeBackpack(BackpackType.DIAMOND, oldId);
            new InventoryBuilder(InventoryBuilder.MenuType.CONFIG, backPack).build();
            new InventoryBuilder(InventoryBuilder.MenuType.UPGMENU, backPack).build();
            new InventoryBuilder(InventoryBuilder.MenuType.EDIT_IO_MENU, backPack).build();
        }
    }

    private boolean checkUpgradeInMatrix(CraftItemEvent event, UpgradeType type){
        ItemStack upgradeItem = event.getInventory().getItem(5);
        Upgrade upgrade;
        if(upgradeItem == null) return false;
        if(!upgradeItem.getItemMeta().getPersistentDataContainer().has(new UpgradesRecipes().isUpgrade(), PersistentDataType.INTEGER)) return false;

        if(upgradeItem.getItemMeta().getPersistentDataContainer().has(new UpgradesRecipes().getUPGRADE_ID(), PersistentDataType.INTEGER)){
            upgrade = UpgradeManager.getUpgradeFromId(upgradeItem.getItemMeta().getPersistentDataContainer().get(new UpgradesRecipes().getUPGRADE_ID(), PersistentDataType.INTEGER));
            if(upgrade == null) return false;
            return upgrade.getType() == type;
        }

        return false;
    }


    private int getBackpackIdInMatrix(CraftItemEvent event, int oldId, BackpackType type) {
        ItemStack backpack = event.getInventory().getItem(5);

        if(backpack.getItemMeta().getPersistentDataContainer().has(new BackpackRecipes().isBackpack(), PersistentDataType.INTEGER)){
            oldId = backpack.getItemMeta().getPersistentDataContainer().get(new BackpackRecipes().getNAMESPACE_BACKPACK_ID(), PersistentDataType.INTEGER);
        }

        switch (type){
            case IRON -> {
                if(!backpack.getItemMeta().getPersistentDataContainer().has(new BackpackRecipes().getNAMESPACE_LEATHER_BACKPACK(), PersistentDataType.INTEGER)){
                    event.getWhoClicked().sendMessage(NOTCORRECTTYPE);
                    event.setCancelled(true);
                    return -1;
                }
            }

            case GOLD -> {
                if(!backpack.getItemMeta().getPersistentDataContainer().has(new BackpackRecipes().getNAMESPACE_IRON_BACKPACK(), PersistentDataType.INTEGER)){
                    event.getWhoClicked().sendMessage(NOTCORRECTTYPE);
                    event.setCancelled(true);
                    return -1;
                }
            }

            case LAPIS -> {
                if(!backpack.getItemMeta().getPersistentDataContainer().has(new BackpackRecipes().getNAMESPACE_GOLD_BACKPACK(), PersistentDataType.INTEGER)){
                    event.getWhoClicked().sendMessage(NOTCORRECTTYPE);
                    event.setCancelled(true);
                    return -1;
                }
            }

            case AMETHYST -> {
                if(!backpack.getItemMeta().getPersistentDataContainer().has(new BackpackRecipes().getNAMESPACE_LAPIS_BACKPACK(), PersistentDataType.INTEGER)){
                    event.getWhoClicked().sendMessage(NOTCORRECTTYPE);
                    event.setCancelled(true);
                    return -1;
                }
            }

            case DIAMOND -> {
                if(!backpack.getItemMeta().getPersistentDataContainer().has(new BackpackRecipes().getNAMESPACE_AMETHYST_BACKPACK(), PersistentDataType.INTEGER)){
                    event.getWhoClicked().sendMessage(NOTCORRECTTYPE);
                    event.setCancelled(true);
                    return -1;
                }
            }

            case NETHERITE -> {
                if(!backpack.getItemMeta().getPersistentDataContainer().has(new BackpackRecipes().getNAMESPACE_DIAMOND_BACKPACK(), PersistentDataType.INTEGER)){
                    event.getWhoClicked().sendMessage(NOTCORRECTTYPE);
                    event.setCancelled(true);
                    return -1;
                }
            }

            default -> {
                event.getWhoClicked().sendMessage(DONTHAVEBACKPACKMSG);
                event.setCancelled(true);
                return -1;
            }
        }


        if(oldId == -1){
            event.getWhoClicked().sendMessage(DONTHAVEBACKPACKMSG);
            event.setCancelled(true);
            return -1;
        }

        return oldId;
    }

    private void updateResult(CraftItemEvent event, int id) {
        ItemStack itemStack = event.getRecipe().getResult();
        ItemMeta meta = itemStack.getItemMeta();
        meta.getPersistentDataContainer().set(new BackpackRecipes().getNAMESPACE_BACKPACK_ID(), PersistentDataType.INTEGER, id);
        itemStack.setItemMeta(meta);
        event.getInventory().setResult(itemStack);
    }

}