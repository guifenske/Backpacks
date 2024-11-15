package br.com.backpacks.events.entity;

import br.com.backpacks.Main;
import br.com.backpacks.recipes.BackpackRecipes;
import br.com.backpacks.recipes.RecipesUtils;
import br.com.backpacks.recipes.UpgradesRecipes;
import br.com.backpacks.utils.UpgradeManager;
import br.com.backpacks.utils.backpacks.BackPack;
import br.com.backpacks.utils.backpacks.BackpackType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class CraftBackpack implements Listener {

    private static final String DONTHAVEBACKPACKMSG =  Main.getMain().PREFIX + "§cIt looks that you don't have a backpack to upgrade in the recipe!";
    private static final String NOTCORRECTTYPE =  Main.getMain().PREFIX + "§cYou don't have the correct type of backpack to upgrade!";

    @EventHandler
    private static void craftBackpackEvent(CraftItemEvent event){
        if(!event.getRecipe().getResult().hasItemMeta()){
            switch (event.getRecipe().getResult().getType()){

                case CHEST, BARREL, COMPOSTER -> {
                    if(!event.getWhoClicked().hasDiscoveredRecipe(BackpackRecipes.NAMESPACE_LEATHER_BACKPACK)){
                        event.getWhoClicked().discoverRecipe(BackpackRecipes.NAMESPACE_LEATHER_BACKPACK);
                    }

                    else{
                        event.getWhoClicked().discoverRecipe(UpgradesRecipes.COLLECTOR);
                    }

                }

                case FURNACE, SMOKER, BLAST_FURNACE -> {
                    event.getWhoClicked().discoverRecipe(UpgradesRecipes.FURNACE);
                }

                case JUKEBOX, NOTE_BLOCK -> {
                    event.getWhoClicked().discoverRecipe(UpgradesRecipes.JUKEBOX);
                }

                case GOLDEN_CARROT, GOLDEN_APPLE, BREAD, HAY_BLOCK, BEETROOT_SOUP, RABBIT_STEW, MUSHROOM_STEW, SUSPICIOUS_STEW -> {
                    event.getWhoClicked().discoverRecipe(UpgradesRecipes.AUTOFEED);
                }

                case SMITHING_TABLE, FLETCHING_TABLE, CARTOGRAPHY_TABLE, LECTERN, EMERALD_BLOCK, BREWING_STAND -> {
                    event.getWhoClicked().discoverRecipe(UpgradesRecipes.VILLAGER_BAIT);
                }

                case END_CRYSTAL, BEACON, NETHERITE_INGOT -> {
                    event.getWhoClicked().discoverRecipe(UpgradesRecipes.UNBREAKABLE);
                    event.getWhoClicked().discoverRecipe(UpgradesRecipes.ENCAPSULATE);
                }

                case BUCKET, CAULDRON -> {
                    event.getWhoClicked().discoverRecipe(UpgradesRecipes.LIQUID_TANK);
                }

            }
            return;
        }

        Player player = (Player) event.getWhoClicked();
        if(event.getRecipe().getResult().getItemMeta().getPersistentDataContainer().has(UpgradesRecipes.IS_UPGRADE)){
            ItemStack itemStack = event.getRecipe().getResult();
            ItemMeta meta = itemStack.getItemMeta();

            meta.getPersistentDataContainer().set(UpgradesRecipes.UPGRADE_ID, PersistentDataType.INTEGER, UpgradeManager.lastUpgradeID + 1);
            itemStack.setItemMeta(meta);

            event.getInventory().setResult(itemStack);
            UpgradeManager.lastUpgradeID++;

            RecipesUtils.getUpgradeFromItem(itemStack);
        }

        if(!event.getRecipe().getResult().getItemMeta().getPersistentDataContainer().has(BackpackRecipes.IS_BACKPACK)) return;

        if(event.getRecipe().getResult().getItemMeta().getPersistentDataContainer().has(BackpackRecipes.NAMESPACE_LEATHER_BACKPACK, PersistentDataType.INTEGER)){
            int id = Main.backPackManager.getLastBackpackID() + 1;
            player.discoverRecipe(BackpackRecipes.NAMESPACE_IRON_BACKPACK);
            player.discoverRecipe(UpgradesRecipes.CRAFTING_GRID);

            BackPack backPack =  new BackPack(BackpackType.LEATHER, id);
            updateResult(event, id);

            Main.backPackManager.getBackpacks().put(id, backPack);
            Main.backPackManager.setLastBackpackID(Main.backPackManager.getLastBackpackID() + 1);

            return;
        }

        BackPack backpackInMatrix = getBackpackInMatrix(event);
        BackPack desiredBackpack = RecipesUtils.getBackpackFromItem(event.getInventory().getResult());

        if(backpackInMatrix == null){
            player.sendMessage(DONTHAVEBACKPACKMSG);
            event.setCancelled(true);
            return;
        }

        if(canUpgradeBackpack(event, backpackInMatrix, desiredBackpack.getType())){
            updateResult(event, backpackInMatrix.getId());
            Main.backPackManager.upgradeBackpack(backpackInMatrix);
        }
    }

    private static boolean canUpgradeBackpack(CraftItemEvent event, BackPack backPackInMatrix, BackpackType desiredType) {
        switch (desiredType){
            case IRON -> {
                if(!backPackInMatrix.getType().equals(BackpackType.LEATHER)){
                    event.getWhoClicked().sendMessage(NOTCORRECTTYPE);
                    event.setCancelled(true);
                    return false;
                }
            }

            case GOLD -> {
                if(!backPackInMatrix.getType().equals(BackpackType.IRON)){
                    event.getWhoClicked().sendMessage(NOTCORRECTTYPE);
                    event.setCancelled(true);
                    return false;
                }
            }

            case LAPIS -> {
                if(!backPackInMatrix.getType().equals(BackpackType.GOLD)){
                    event.getWhoClicked().sendMessage(NOTCORRECTTYPE);
                    event.setCancelled(true);
                    return false;
                }
            }

            case AMETHYST -> {
                if(!backPackInMatrix.getType().equals(BackpackType.LAPIS)){
                    event.getWhoClicked().sendMessage(NOTCORRECTTYPE);
                    event.setCancelled(true);
                    return false;
                }
            }

            case DIAMOND -> {
                if(!backPackInMatrix.getType().equals(BackpackType.AMETHYST)){
                    event.getWhoClicked().sendMessage(NOTCORRECTTYPE);
                    event.setCancelled(true);
                    return false;
                }
            }

            case NETHERITE -> {
                if(!backPackInMatrix.getType().equals(BackpackType.DIAMOND)){
                    event.getWhoClicked().sendMessage(NOTCORRECTTYPE);
                    event.setCancelled(true);
                    return false;
                }
            }
        }

        return true;
    }

    private static BackPack getBackpackInMatrix(CraftItemEvent event){
        ItemStack backpack = event.getInventory().getItem(5);
        int id;

        if(backpack.getItemMeta().getPersistentDataContainer().has(BackpackRecipes.IS_BACKPACK)){
            id = backpack.getItemMeta().getPersistentDataContainer().get(BackpackRecipes.BACKPACK_ID, PersistentDataType.INTEGER);
            return Main.backPackManager.getBackpackFromId(id);
        }

        return null;
    }

    private static void updateResult(CraftItemEvent event, int id) {
        ItemStack itemStack = event.getRecipe().getResult();
        ItemMeta meta = itemStack.getItemMeta();

        meta.getPersistentDataContainer().set(BackpackRecipes.BACKPACK_ID, PersistentDataType.INTEGER, id);
        itemStack.setItemMeta(meta);

        event.getInventory().setResult(itemStack);
    }

}