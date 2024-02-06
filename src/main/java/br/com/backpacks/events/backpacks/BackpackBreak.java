package br.com.backpacks.events.backpacks;

import br.com.backpacks.Main;
import br.com.backpacks.backpackUtils.BackPack;
import br.com.backpacks.backpackUtils.UpgradeType;
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
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class BackpackBreak implements Listener {

    @EventHandler
    private void playerBreak(BlockBreakEvent event){
        if(!event.getBlock().getType().equals(Material.BARREL)) return;
        if(!Main.backPackManager.canOpen()){
            event.setCancelled(true);
            return;
        }
        if(Main.backPackManager.getBackpackFromLocation(event.getBlock().getLocation()) == null) return;

        event.setDropItems(false);

        BackPack backPack = Main.backPackManager.getBackpackFromLocation(event.getBlock().getLocation());
        ItemStack backpackItem = RecipesUtils.getItemFromBackpack(backPack);

        backPack.setLocation(null);
        Location location = event.getBlock().getLocation();
        Main.backPackManager.getBackpacksPlacedLocations().remove(location);
        event.getPlayer().getWorld().dropItemNaturally(location, backpackItem);
    }

    @EventHandler
    private void explosionBreak(EntityExplodeEvent event){
        for(Block block : event.blockList()){
            if(!block.getType().equals(Material.BARREL)) continue;
            if(!Main.backPackManager.canOpen()){
                event.setCancelled(true);
                continue;
            }
            if(Main.backPackManager.getBackpackFromLocation(block.getLocation()) == null) continue;

            event.setCancelled(true);
            block.setType(Material.AIR);

            BackPack backPack = Main.backPackManager.getBackpackFromLocation(block.getLocation());
            ItemStack backpackItem = RecipesUtils.getItemFromBackpack(backPack);

            Location location = block.getLocation();
            backPack.setLocation(null);
            Main.backPackManager.getBackpacksPlacedLocations().remove(location);
            block.getWorld().dropItemNaturally(location, backpackItem);
            break;
       }
    }

    @EventHandler
    private void despawn(ItemDespawnEvent event){
        if(!event.getEntity().getItemStack().hasItemMeta())  return;
        if(!event.getEntity().getItemStack().getItemMeta().getPersistentDataContainer().has(new RecipesNamespaces().isBackpack())){
            if(event.getEntity().getItemStack().getItemMeta().getPersistentDataContainer().has(new UpgradesRecipesNamespaces().isUpgrade())){
                if(!Main.backPackManager.canOpen()){
                    event.setCancelled(true);
                    return;
                }
                int id = event.getEntity().getItemStack().getItemMeta().getPersistentDataContainer().get(new UpgradesRecipesNamespaces().getUPGRADEID(), PersistentDataType.INTEGER);
                Main.backPackManager.getUpgradeHashMap().remove(id);
            }
            return;
        }

        if(!Main.backPackManager.canOpen()){
            event.setCancelled(true);
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
    private void onEntityDeath(EntityDeathEvent event){
        if(!(event.getEntity() instanceof Item)) return;
        Item eventItem = (Item) event.getEntity();
        if(!eventItem.getItemStack().hasItemMeta())  return;
        if(!eventItem.getItemStack().getItemMeta().getPersistentDataContainer().has(new RecipesNamespaces().isBackpack())){
            if(eventItem.getItemStack().getItemMeta().getPersistentDataContainer().has(new UpgradesRecipesNamespaces().isUpgrade())){
                if(!Main.backPackManager.canOpen()){
                    event.setCancelled(true);
                    return;
                }
                int id = eventItem.getItemStack().getItemMeta().getPersistentDataContainer().get(new UpgradesRecipesNamespaces().getUPGRADEID(), PersistentDataType.INTEGER);
                Main.backPackManager.getUpgradeHashMap().remove(id);
            }
            return;
        }

        if(!Main.backPackManager.canOpen()){
            event.setCancelled(true);
            return;
        }

        int id = eventItem.getItemStack().getItemMeta().getPersistentDataContainer().get(new RecipesNamespaces().getNAMESPACE_BACKPACK_ID(), PersistentDataType.INTEGER);
        if(!Main.backPackManager.getBackpacks().containsKey(id)) return;
        BackPack backPack = Main.backPackManager.getBackpacks().get(id);

        if(backPack.getUpgradesFromType(UpgradeType.UNBREAKABLE).isEmpty()){
            event.setCancelled(true);
            return;
        }

        if(!backPack.getUpgradesIds().isEmpty()){
            for(int i : backPack.getUpgradesIds()){
                Main.backPackManager.getUpgradeHashMap().remove(i);
            }
        }
        Main.backPackManager.getBackpacks().remove(id);
    }
}