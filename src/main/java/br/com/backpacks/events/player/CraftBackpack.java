package br.com.backpacks.events.player;

import br.com.backpacks.Main;
import br.com.backpacks.backpackUtils.BackpackType;
import br.com.backpacks.recipes.RecipesNamespaces;
import br.com.backpacks.recipes.UpgradesRecipesNamespaces;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class CraftBackpack implements Listener {

    private static final String DONTHAVEBACKPACKMSG =  Main.PREFIX + "§cIt looks that you don't have a backpack to upgrade in the recipe!";
    private static final String NOTCORRECTTYPE =  Main.PREFIX + "§cYou don't have the correct type of backpack to upgrade!";

    public static int generateId(){
        return Main.backPackManager.getBackpacks().size() + 1;
    }

    @EventHandler
    private static void craftBackpackEvent(CraftItemEvent event){
        if(!event.getRecipe().getResult().hasItemMeta()) return;
        if(!event.getRecipe().getResult().getItemMeta().getPersistentDataContainer().has(new RecipesNamespaces().getIS_BACKPACK())){
            switch (event.getRecipe().getResult().getType()){
                case CHEST -> {
                    if(!event.getWhoClicked().hasDiscoveredRecipe(new RecipesNamespaces().getNAMESPACE_LEATHER_BACKPACK())) event.getWhoClicked().discoverRecipe(new RecipesNamespaces().getNAMESPACE_LEATHER_BACKPACK());
                }
                case FURNACE -> {
                    if(event.getWhoClicked().hasDiscoveredRecipe(new RecipesNamespaces().getNAMESPACE_LEATHER_BACKPACK()) && !event.getWhoClicked().hasDiscoveredRecipe(new UpgradesRecipesNamespaces().getFurnaceGrid())){
                        event.getWhoClicked().discoverRecipe(new UpgradesRecipesNamespaces().getFurnaceGrid());
                    }
                }
            }
            return;
        }

        Player player = (Player) event.getWhoClicked();

        int oldId = -1;

        if(event.getRecipe().getResult().getItemMeta().getPersistentDataContainer().has(new RecipesNamespaces().getNAMESPACE_LEATHER_BACKPACK())){
            int id = generateId();
            player.discoverRecipe(new RecipesNamespaces().getNAMESPACE_IRON_BACKPACK());
            Main.backPackManager.createBackPack(18, "Leather Backpack", id, BackpackType.LEATHER);
            updateResult(event, id);
            return;
        }

        if(event.getRecipe().getResult().getItemMeta().getPersistentDataContainer().has(new RecipesNamespaces().getNAMESPACE_IRON_BACKPACK())){
            oldId = checkBackpackInTheMatrix(event, oldId, BackpackType.IRON);
            if(oldId == -1) return;
            player.discoverRecipe(new RecipesNamespaces().getNAMESPACE_GOLD_BACKPACK());
            updateResult(event, oldId);
            Main.backPackManager.upgradeBackpack(BackpackType.LEATHER, oldId);
            return;
        }

        if(event.getRecipe().getResult().getItemMeta().getPersistentDataContainer().has(new RecipesNamespaces().getNAMESPACE_GOLD_BACKPACK())){
            oldId = checkBackpackInTheMatrix(event, oldId, BackpackType.GOLD);
            if(oldId == -1) return;
            player.discoverRecipe(new RecipesNamespaces().getNAMESPACE_LAPIS_BACKPACK());
            updateResult(event, oldId);
            Main.backPackManager.upgradeBackpack(BackpackType.IRON, oldId);
            return;
        }

        if(event.getRecipe().getResult().getItemMeta().getPersistentDataContainer().has(new RecipesNamespaces().getNAMESPACE_LAPIS_BACKPACK())){
            oldId = checkBackpackInTheMatrix(event, oldId, BackpackType.LAPIS);
            if(oldId == -1) return;
            player.discoverRecipe(new RecipesNamespaces().getNAMESPACE_AMETHYST_BACKPACK());
            updateResult(event, oldId);
            Main.backPackManager.upgradeBackpack(BackpackType.GOLD, oldId);
            return;
        }

        if(event.getRecipe().getResult().getItemMeta().getPersistentDataContainer().has(new RecipesNamespaces().getNAMESPACE_AMETHYST_BACKPACK())){
            oldId = checkBackpackInTheMatrix(event, oldId, BackpackType.AMETHYST);
            if(oldId == -1) return;
            player.discoverRecipe(new RecipesNamespaces().getNAMESPACE_DIAMOND_BACKPACK());
            updateResult(event, oldId);
            Main.backPackManager.upgradeBackpack(BackpackType.LAPIS, oldId);
            return;
        }

        if(event.getRecipe().getResult().getItemMeta().getPersistentDataContainer().has(new RecipesNamespaces().getNAMESPACE_DIAMOND_BACKPACK())){
            oldId = checkBackpackInTheMatrix(event, oldId, BackpackType.DIAMOND);
            if(oldId == -1) return;
            player.discoverRecipe(new RecipesNamespaces().getNAMESPACE_NETHERITE_BACKPACK());
            updateResult(event, oldId);
            Main.backPackManager.upgradeBackpack(BackpackType.AMETHYST, oldId);
            return;
        }

        if(event.getRecipe().getResult().getItemMeta().getPersistentDataContainer().has(new RecipesNamespaces().getNAMESPACE_NETHERITE_BACKPACK())){
            oldId = checkBackpackInTheMatrix(event, oldId, BackpackType.NETHERITE);
            if(oldId == -1) return;
            updateResult(event, oldId);
            Main.backPackManager.upgradeBackpack(BackpackType.DIAMOND, oldId);
        }
    }

    private static int checkBackpackInTheMatrix(CraftItemEvent event, int oldId, BackpackType type) {

        ItemStack itemStack = event.getInventory().getItem(5);

        if(itemStack.getItemMeta().getPersistentDataContainer().has(new RecipesNamespaces().getIS_BACKPACK())){
            oldId = itemStack.getItemMeta().getPersistentDataContainer().get(new RecipesNamespaces().getNAMESPACE_BACKPACK_ID(), PersistentDataType.INTEGER);
        }

        switch (type){
            case IRON -> {
                if(!itemStack.getItemMeta().getPersistentDataContainer().has(new RecipesNamespaces().getNAMESPACE_LEATHER_BACKPACK())){
                    event.getWhoClicked().sendMessage(NOTCORRECTTYPE);
                    event.setCancelled(true);
                    return -1;
                }
            }

            case GOLD -> {
                if(!itemStack.getItemMeta().getPersistentDataContainer().has(new RecipesNamespaces().getNAMESPACE_IRON_BACKPACK())){
                    event.getWhoClicked().sendMessage(NOTCORRECTTYPE);
                    event.setCancelled(true);
                    return -1;
                }
            }

            case LAPIS -> {
                if(!itemStack.getItemMeta().getPersistentDataContainer().has(new RecipesNamespaces().getNAMESPACE_GOLD_BACKPACK())){
                    event.getWhoClicked().sendMessage(NOTCORRECTTYPE);
                    event.setCancelled(true);
                    return -1;
                }
            }

            case AMETHYST -> {
                if(!itemStack.getItemMeta().getPersistentDataContainer().has(new RecipesNamespaces().getNAMESPACE_LAPIS_BACKPACK())){
                    event.getWhoClicked().sendMessage(NOTCORRECTTYPE);
                    event.setCancelled(true);
                    return -1;
                }
            }

            case DIAMOND -> {
                if(!itemStack.getItemMeta().getPersistentDataContainer().has(new RecipesNamespaces().getNAMESPACE_AMETHYST_BACKPACK())){
                    event.getWhoClicked().sendMessage(NOTCORRECTTYPE);
                    event.setCancelled(true);
                    return -1;
                }
            }

            case NETHERITE -> {
                if(!itemStack.getItemMeta().getPersistentDataContainer().has(new RecipesNamespaces().getNAMESPACE_DIAMOND_BACKPACK())){
                    event.getWhoClicked().sendMessage(NOTCORRECTTYPE);
                    event.setCancelled(true);
                    return -1;
                }
            }
        }


        if(oldId == -1){
            event.getWhoClicked().sendMessage(DONTHAVEBACKPACKMSG);
            event.setCancelled(true);
            return -1;
        }

        return oldId;
    }

    private static void updateResult(CraftItemEvent event, int id) {
        ItemStack itemStack = event.getRecipe().getResult();
        ItemMeta meta = itemStack.getItemMeta();
        meta.getPersistentDataContainer().set(new RecipesNamespaces().getNAMESPACE_BACKPACK_ID(), PersistentDataType.INTEGER, id);
        itemStack.setItemMeta(meta);
        event.getInventory().setResult(itemStack);
    }

}