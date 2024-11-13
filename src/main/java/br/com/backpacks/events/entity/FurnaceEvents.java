package br.com.backpacks.events.entity;

import br.com.backpacks.Main;
import br.com.backpacks.recipes.BackpackRecipes;
import br.com.backpacks.upgrades.FurnaceUpgrade;
import br.com.backpacks.utils.Upgrade;
import br.com.backpacks.utils.UpgradeManager;
import br.com.backpacks.utils.UpgradeType;
import br.com.backpacks.utils.backpacks.BackPack;
import br.com.backpacks.utils.backpacks.RandomBackpackBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockCookEvent;
import org.bukkit.event.inventory.FurnaceStartSmeltEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class FurnaceEvents implements Listener {

    @EventHandler
    private void onSmelt(BlockCookEvent event){
        if(!event.getResult().getItemMeta().getPersistentDataContainer().has(BackpackRecipes.NAMESPACE_DRIED_BACKPACK)) return;

        ItemStack driedBackpack = new ItemStack(Material.BARREL);
        ItemMeta meta = driedBackpack.getItemMeta();
        meta.setDisplayName("Unknown Backpack");

        meta.getPersistentDataContainer().set(BackpackRecipes.IS_BACKPACK, PersistentDataType.INTEGER, 1);
        meta.getPersistentDataContainer().set(BackpackRecipes.BACKPACK_ID, PersistentDataType.INTEGER, Main.backPackManager.getLastBackpackID() + 1);

        RandomBackpackBuilder randomBackpackBuilder = new RandomBackpackBuilder("Unknown Backpack", meta.getPersistentDataContainer().get(BackpackRecipes.BACKPACK_ID, PersistentDataType.INTEGER));
        BackPack backPack = randomBackpackBuilder.generateBackpack();

        meta.getPersistentDataContainer().set(backPack.getType().getKey(), PersistentDataType.INTEGER, 1);
        driedBackpack.setItemMeta(meta);
        Main.backPackManager.setLastBackpackID(Main.backPackManager.getLastBackpackID() + 1);

        event.setResult(driedBackpack);
    }

    @EventHandler
    private void onStartSmelt(FurnaceStartSmeltEvent event){
        for(Upgrade upgrade : UpgradeManager.getUpgradesFromType(UpgradeType.FURNACE)){
            FurnaceUpgrade furnaceUpgrade = (FurnaceUpgrade) upgrade;

            if(furnaceUpgrade.getFurnace() == null) continue;
            if(!furnaceUpgrade.getFurnace().getLocation().equals(event.getBlock().getLocation())) continue;

            Bukkit.getScheduler().runTaskLater(Main.getMain(), ()->{
                for(Player player : Bukkit.getOnlinePlayers()){
                    player.sendBlockChange(event.getBlock().getLocation(), Material.AIR.createBlockData());
                }
            }, 2L);

            return;
        }
    }
}