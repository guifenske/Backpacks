package br.com.backpacks.events.backpacks;

import br.com.backpacks.Main;
import br.com.backpacks.backpackUtils.BackPack;
import br.com.backpacks.recipes.RecipesNamespaces;
import br.com.backpacks.recipes.RecipesUtils;
import br.com.backpacks.recipes.UpgradesRecipesNamespaces;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class BackpackBreak implements Listener {

    @EventHandler
    private void playerBreak(BlockBreakEvent event){
        if(!event.getBlock().getType().equals(Material.BARREL)) return;
        if(Main.backPackManager.getBackpackFromLocation(event.getBlock().getLocation()) == null) return;

        event.setDropItems(false);

        BackPack backPack = Main.backPackManager.getBackpackFromLocation(event.getBlock().getLocation());
        ItemStack backpack_item = RecipesUtils.getItemFromBackpack(backPack);

        backPack.setLocation(null);
        Location location = event.getBlock().getLocation();
        Main.backPackManager.getBackpacksPlacedLocations().remove(location);
        event.getPlayer().getWorld().dropItemNaturally(location, backpack_item);
    }

    @EventHandler
    private void explosionBreak(EntityExplodeEvent event){
        for(Block block : event.blockList()){
            if(!block.getType().equals(Material.BARREL)) continue;
            if(Main.backPackManager.getBackpackFromLocation(block.getLocation()) == null) continue;

            event.setCancelled(true);
            block.setType(Material.AIR);

            BackPack backPack = Main.backPackManager.getBackpackFromLocation(block.getLocation());
            ItemStack backpack_item = RecipesUtils.getItemFromBackpack(backPack);

            Location location = block.getLocation();
            backPack.setLocation(null);
            Main.backPackManager.getBackpacksPlacedLocations().remove(location);
            block.getWorld().dropItemNaturally(location, backpack_item);
            break;
       }
    }

    @EventHandler
    private void despawn(ItemDespawnEvent event){
       if(!event.getEntity().getItemStack().hasItemMeta())  return;
       if(!event.getEntity().getItemStack().getItemMeta().getPersistentDataContainer().has(new RecipesNamespaces().getIS_BACKPACK(), PersistentDataType.INTEGER)){
           if(event.getEntity().getItemStack().getItemMeta().getPersistentDataContainer().has(new UpgradesRecipesNamespaces().isUpgrade(), PersistentDataType.INTEGER)){
               int id = event.getEntity().getItemStack().getItemMeta().getPersistentDataContainer().get(new UpgradesRecipesNamespaces().isUpgrade(), PersistentDataType.INTEGER);
               Main.backPackManager.getUpgradeHashMap().remove(id);
           }
           return;
       }

        int id = event.getEntity().getItemStack().getItemMeta().getPersistentDataContainer().get(new RecipesNamespaces().getNAMESPACE_BACKPACK_ID(), PersistentDataType.INTEGER);
        if(!Main.backPackManager.getBackpacks().containsKey(id)) return;
        BackPack backPack = Main.backPackManager.getBackpacks().get(id);
        if(!backPack.getUpgradesIds().isEmpty()){
            for(int i : backPack.getUpgradesIds()){
                Main.backPackManager.getUpgradeHashMap().remove(i);
            }
        }
        Main.backPackManager.getBackpacks().remove(id);
    }

    @EventHandler
    private void onDamageGeneral(EntityDamageEvent event){
        if(!(event.getEntity() instanceof Item)) return;
        if(!((Item) event.getEntity()).getItemStack().hasItemMeta()) return;
        if(!((Item) event.getEntity()).getItemStack().getItemMeta().getPersistentDataContainer().has(new RecipesNamespaces().getIS_BACKPACK(), PersistentDataType.INTEGER)){
            if(((Item) event.getEntity()).getItemStack().getItemMeta().getPersistentDataContainer().has(new UpgradesRecipesNamespaces().isUpgrade(), PersistentDataType.INTEGER)){
                int id = ((Item) event.getEntity()).getItemStack().getItemMeta().getPersistentDataContainer().get(new UpgradesRecipesNamespaces().isUpgrade(), PersistentDataType.INTEGER);
                Main.backPackManager.getUpgradeHashMap().remove(id);
            }
            return;
        }

        if(((Item) event.getEntity()).getHealth() > 1) return;
        Main.getMain().debugMessage("deleting backpack");

        int id = ((Item) event.getEntity()).getItemStack().getItemMeta().getPersistentDataContainer().get(new RecipesNamespaces().getNAMESPACE_BACKPACK_ID(), PersistentDataType.INTEGER);
        if(!Main.backPackManager.getBackpacks().containsKey(id)) return;
        BackPack backPack = Main.backPackManager.getBackpacks().get(id);
        if(!backPack.getUpgradesIds().isEmpty()){
            for(int i : backPack.getUpgradesIds()){
                Main.backPackManager.getUpgradeHashMap().remove(i);
            }
        }
        Main.backPackManager.getBackpacks().remove(id);
    }
}