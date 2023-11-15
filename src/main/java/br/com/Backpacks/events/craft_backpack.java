package br.com.Backpacks.events;

import br.com.Backpacks.BackpackType;
import br.com.Backpacks.Main;
import br.com.Backpacks.recipes.Recipes;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class craft_backpack implements Listener {


    private int generate_id(Player player){
        if(!Main.back.backPackManager.getBackpacks_ids().containsKey(player)){
            Main.back.backPackManager.getBackpacks_ids().put(player, new ArrayList<>());
            Main.back.backPackManager.getBackpacks_ids().get(player).add(0);
            return 0;
        }

        Main.back.backPackManager.getBackpacks_ids().get(player).add(Main.back.backPackManager.getBackpacks_ids().get(player).size());
        return Main.back.backPackManager.getBackpacks_ids().get(player).size() - 1;
    }

    @EventHandler
    private void craft_backpack_event(CraftItemEvent event){
        if(!event.getRecipe().getResult().getItemMeta().getPersistentDataContainer().has(new Recipes().getIS_BACKPACK())){
            event.setCancelled(true);
            return;
        }
        if(event.getRecipe().getResult().getItemMeta().getPersistentDataContainer().has(new Recipes().getNAMESPACE_LEATHER_BACKPACK())){
            int id = generate_id(((Player) event.getWhoClicked()));
            Main.back.backPackManager.createBackPack((Player) event.getWhoClicked(), 18, "Leather Backpack", id);
            update_result(event, id);
            return;
        }

        if(event.getRecipe().getResult().getItemMeta().getPersistentDataContainer().has(new Recipes().getNAMESPACE_IRON_BACKPACK())){
            int old_id = 0;
            for(ItemStack itemStack : event.getInventory().getMatrix()){
                if(itemStack == null) continue;
                if(itemStack.getType().equals(org.bukkit.Material.CHEST) && !itemStack.getItemMeta().getPersistentDataContainer().has(new Recipes().getNAMESPACE_LEATHER_BACKPACK())){
                    event.setCancelled(true);
                    return;
                }

                if(itemStack.getType().equals(org.bukkit.Material.CHEST) && itemStack.getItemMeta().getPersistentDataContainer().has(new Recipes().getNAMESPACE_LEATHER_BACKPACK())){
                    old_id = itemStack.getItemMeta().getPersistentDataContainer().get(new Recipes().getNAMESPACE_BACKPACK_ID(), PersistentDataType.INTEGER);
                }
            }

            Main.back.backPackManager.upgrade_backpack((Player) event.getWhoClicked(), BackpackType.LEATHER, old_id);

            update_result(event, old_id);
            return;
        }

        if(event.getRecipe().getResult().getItemMeta().getPersistentDataContainer().has(new Recipes().getNAMESPACE_GOLD_BACKPACK())){
            int old_id = 0;
            for(ItemStack itemStack : event.getInventory().getMatrix()){
                if(itemStack == null) continue;
                if(itemStack.getType().equals(org.bukkit.Material.CHEST) && !itemStack.getItemMeta().getPersistentDataContainer().has(new Recipes().getNAMESPACE_IRON_BACKPACK())){
                    event.setCancelled(true);
                    return;
                }

                if(itemStack.getType().equals(org.bukkit.Material.CHEST) && itemStack.getItemMeta().getPersistentDataContainer().has(new Recipes().getNAMESPACE_IRON_BACKPACK())){
                    old_id = itemStack.getItemMeta().getPersistentDataContainer().get(new Recipes().getNAMESPACE_BACKPACK_ID(), PersistentDataType.INTEGER);
                }
            }
            Main.back.backPackManager.upgrade_backpack((Player) event.getWhoClicked(), BackpackType.IRON, old_id);
            update_result(event, old_id);
            return;
        }

        if(event.getRecipe().getResult().getItemMeta().getPersistentDataContainer().has(new Recipes().getNAMESPACE_LAPIS_BACKPACK())){
            int old_id = 0;
            for(ItemStack itemStack : event.getInventory().getMatrix()){
                if(itemStack == null) continue;
                if(itemStack.getType().equals(org.bukkit.Material.CHEST) && !itemStack.getItemMeta().getPersistentDataContainer().has(new Recipes().getNAMESPACE_GOLD_BACKPACK())){
                    event.setCancelled(true);
                    return;
                }

                if(itemStack.getType().equals(org.bukkit.Material.CHEST) && itemStack.getItemMeta().getPersistentDataContainer().has(new Recipes().getNAMESPACE_GOLD_BACKPACK())){
                    old_id = itemStack.getItemMeta().getPersistentDataContainer().get(new Recipes().getNAMESPACE_BACKPACK_ID(), PersistentDataType.INTEGER);
                }
            }
            Main.back.backPackManager.upgrade_backpack((Player) event.getWhoClicked(), BackpackType.GOLD, old_id);
            update_result(event, old_id);
            return;
        }

        if(event.getRecipe().getResult().getItemMeta().getPersistentDataContainer().has(new Recipes().getNAMESPACE_AMETHYST_BACKPACK())){
            int old_id = 0;
            for(ItemStack itemStack : event.getInventory().getMatrix()){
                if(itemStack == null) continue;
                if(itemStack.getType().equals(org.bukkit.Material.CHEST) && !itemStack.getItemMeta().getPersistentDataContainer().has(new Recipes().getNAMESPACE_LAPIS_BACKPACK())){
                    event.setCancelled(true);
                    return;
                }

                if(itemStack.getType().equals(org.bukkit.Material.CHEST) && itemStack.getItemMeta().getPersistentDataContainer().has(new Recipes().getNAMESPACE_LAPIS_BACKPACK())){
                    old_id = itemStack.getItemMeta().getPersistentDataContainer().get(new Recipes().getNAMESPACE_BACKPACK_ID(), PersistentDataType.INTEGER);
                }
            }

            Main.back.backPackManager.upgrade_backpack((Player) event.getWhoClicked(), BackpackType.LAPIS, old_id);
            update_result(event, old_id);
            return;
        }

        if(event.getRecipe().getResult().getItemMeta().getPersistentDataContainer().has(new Recipes().getNAMESPACE_DIAMOND_BACKPACK())){
            int old_id = 0;
            for(ItemStack itemStack : event.getInventory().getMatrix()){
                if(itemStack == null) continue;
                if(itemStack.getType().equals(org.bukkit.Material.CHEST) && !itemStack.getItemMeta().getPersistentDataContainer().has(new Recipes().getNAMESPACE_AMETHYST_BACKPACK())){
                    event.setCancelled(true);
                    return;
                }

                if(itemStack.getType().equals(org.bukkit.Material.CHEST) && itemStack.getItemMeta().getPersistentDataContainer().has(new Recipes().getNAMESPACE_AMETHYST_BACKPACK())){
                    old_id = itemStack.getItemMeta().getPersistentDataContainer().get(new Recipes().getNAMESPACE_BACKPACK_ID(), PersistentDataType.INTEGER);
                }
            }
            Main.back.backPackManager.upgrade_backpack((Player) event.getWhoClicked(), BackpackType.AMETHYST, old_id);
            update_result(event, old_id);
            return;
        }

        if(event.getRecipe().getResult().getItemMeta().getPersistentDataContainer().has(new Recipes().getNAMESPACE_NETHERITE_BACKPACK())){
            int old_id = 0;
            for(ItemStack itemStack : event.getInventory().getMatrix()){
                if(itemStack == null) continue;
                if(itemStack.getType().equals(org.bukkit.Material.CHEST) && !itemStack.getItemMeta().getPersistentDataContainer().has(new Recipes().getNAMESPACE_DIAMOND_BACKPACK())){
                    event.setCancelled(true);
                    return;
                }

                if(itemStack.getType().equals(org.bukkit.Material.CHEST) && itemStack.getItemMeta().getPersistentDataContainer().has(new Recipes().getNAMESPACE_DIAMOND_BACKPACK())){
                    old_id = itemStack.getItemMeta().getPersistentDataContainer().get(new Recipes().getNAMESPACE_BACKPACK_ID(), PersistentDataType.INTEGER);
                }
            }

            Main.back.backPackManager.upgrade_backpack((Player) event.getWhoClicked(), BackpackType.DIAMOND, old_id);
            update_result(event, old_id);
        }
    }

    private void update_result(CraftItemEvent event, int id) {
        ItemStack itemStack = event.getRecipe().getResult();
        ItemMeta meta = itemStack.getItemMeta();

        meta.getPersistentDataContainer().set(new Recipes().getNAMESPACE_BACKPACK_ID(), PersistentDataType.INTEGER, id);
        itemStack.setItemMeta(meta);
        event.getInventory().setResult(itemStack);
    }

}
